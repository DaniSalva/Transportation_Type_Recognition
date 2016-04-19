package fi.tut.rassal.ttr.record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import fi.tut.rassal.ttr.TransportationType;
import fi.tut.rassal.ttr.accelerometer.AccelerometerInfo;
import fi.tut.rassal.ttr.common.ArgumentCheck;
import fi.tut.rassal.ttr.common.StartStoppable;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DatabaseAdapter implements StartStoppable, Runnable {
  //region Constants

  public static final String COLUMN_X = "x";
  public static final String COLUMN_Y = "y";
  public static final String COLUMN_Z = "z";
  public static final String COLUMN_TIME = "time";

  public static final String COLUMNS = "(" + COLUMN_X + " REAL, " + COLUMN_Y + " REAL, " + COLUMN_Z +
          " REAL, " + COLUMN_TIME + " INTEGER)";

  public static final String TABLE_STATIC = "static_data";
  public static final String TABLE_WALKING = "walking_data";
  public static final String TABLE_DRIVING = "driving_data";

  public static final int MAX_DATA_THRESHOLD = 100 * 1000;

  //endregion

  //region Fields

  private BlockingQueue<AccelerometerInfo> _dataQueue = new LinkedBlockingQueue<>();
  boolean _running;

  private int _counter;

  private String _currentTable = TABLE_STATIC;
  private final DatabaseHelper _helper;

  //endregion

  //region Constructors

  public DatabaseAdapter(Context context) {
    ArgumentCheck.notNull(context);

    _helper = new DatabaseHelper(context);
  }

  //endregion

  //region StartStoppable impl

  @Override
  public void start() {
    _running = true;
    Thread thread = new Thread(this);
    thread.start();
  }

  @Override
  public void stop() {
    _running = false;

    //add finishing record
    addRecord(new AccelerometerInfo(0, 0, 0));
  }

  //endregion

  //region Methods

  /**
   * Will return sum of square, no square root of it
   */
  public float[] getAllXYValues(TransportationType transportationType) {
    ArgumentCheck.notNull(transportationType);

    return getValues(transportationType, "x*x + y*y");
  }

  public float[] getAllZValues(TransportationType transportationType) {
    ArgumentCheck.notNull(transportationType);

    return getValues(transportationType, "z");
  }

  @SuppressWarnings("TryFinallyCanBeTryWithResources") //Android cannot
  private float[] getValues(TransportationType transportationType, String column) {
    String table = getTableForTransportationType(transportationType);
    Cursor dataCursor = getWritableDatabase().rawQuery("select " + column + " from " + table, null);

    try {
      int count = dataCursor.getCount();

      float[] data = new float[count];
      if (count > MAX_DATA_THRESHOLD) {
        count = MAX_DATA_THRESHOLD;
      }

      for (int i = 0; i < count; i++) {
        dataCursor.moveToNext();
        data[i] = dataCursor.getFloat(0);
      }

      return data;
    }
    finally {
      dataCursor.close();
    }
  }

  public void setCurrentTransportation(TransportationType transportation) {
    _currentTable = getTableForTransportationType(transportation);
  }

  private String getTableForTransportationType(TransportationType transportation) {
    switch (transportation) {

      case Static:
        return TABLE_STATIC;
      case Walking:
        return TABLE_WALKING;
      case Driving:
        return TABLE_DRIVING;

      default:
        throw new IllegalArgumentException();
    }
  }

  public void addRecord(AccelerometerInfo info) {
    _dataQueue.offer(info);
  }

  public void run() {
    SQLiteDatabase database = getWritableDatabase();
    database.beginTransaction();

    try {
      while (_running) {
        AccelerometerInfo info;
        try {
          info = _dataQueue.take();
        }
        catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        saveInfo(database, info);

        //commit once per batch of records
        if (_counter++ % 20 == 0) {
          database.setTransactionSuccessful();
          database.endTransaction();
          database.beginTransaction();
        }
      }

      //save remaining data
      ArrayList<AccelerometerInfo> restingData = new ArrayList<>(_dataQueue);

      for (AccelerometerInfo info : restingData) {
        saveInfo(database, info);
      }

      database.setTransactionSuccessful();
    }
    finally {
      database.endTransaction();
    }
  }

  private SQLiteDatabase getWritableDatabase() {
    return _helper.getWritableDatabase();
  }

  private void saveInfo(SQLiteDatabase database, AccelerometerInfo info) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_X, info._x);
    contentValues.put(COLUMN_Y, info._y);
    contentValues.put(COLUMN_Z, info._z);
    contentValues.put(COLUMN_TIME, info._time);

    database.insert(_currentTable, null, contentValues);
  }

  //endregion

  //region Nested classes

  static class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
      super(context, getDbPath(), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table " + TABLE_DRIVING + COLUMNS);
      db.execSQL("create table " + TABLE_STATIC + COLUMNS);
      db.execSQL("create table " + TABLE_WALKING + COLUMNS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    static String getDbPath() {
      return new File(Environment.getExternalStorageDirectory(), "TTRData.db").getAbsolutePath();
    }
  }

  //endregion
}
