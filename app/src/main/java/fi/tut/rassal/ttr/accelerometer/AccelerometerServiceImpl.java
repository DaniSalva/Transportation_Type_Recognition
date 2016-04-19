package fi.tut.rassal.ttr.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import fi.tut.rassal.ttr.common.ArgumentCheck;
import fi.tut.rassal.ttr.common.Observable;
import fi.tut.rassal.ttr.common.ObservableImpl;

import java.util.Arrays;

public class AccelerometerServiceImpl implements AccelerometerService {
  //region Constants

  private static final String TAG = AccelerometerServiceImpl.class.getSimpleName();

  //endregion

  //region Fields

  private final SensorManager _sensorManager;

  private ObservableImpl<AccelerometerInfo> _accelerometerInfoObservable;
  private AccelerometerInfo _lastInfo;

  private final AccelerometerListener _listener;

  private boolean _running;

  //endregion

  //region Constructors

  public AccelerometerServiceImpl(SensorManager sensorManager) {
    ArgumentCheck.notNull(sensorManager, "sensorManager");

    _sensorManager = sensorManager;
    _listener = new AccelerometerListener(this);
    _running = false;
  }

  //endregion

  //region AccelerometerService impl

  @Override
  public Observable<AccelerometerInfo> getNewAccelerometerInfo() {
    if (_accelerometerInfoObservable == null) {
      _accelerometerInfoObservable = new ObservableImpl<>();
    }

    return _accelerometerInfoObservable;
  }

  @Override
  public AccelerometerInfo getLastInfo() {
    return _lastInfo;
  }

  @Override
  public boolean isListening() {
    return _running;
  }

  @Override
  public void start() {
    if (_running) {
      return;
    }

    Sensor rotationSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    if (rotationSensor != null) {
      _sensorManager.registerListener(_listener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    } else {
      Log.e(TAG, "Cannot find rotation vector sensor, using just magnetic values");

      Sensor magneticFieldSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
      _sensorManager.registerListener(_listener, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    Sensor accelerometer = getDefaultSensorSafe(Sensor.TYPE_ACCELEROMETER);
    _sensorManager.registerListener(_listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    _running = true;
  }

  @Override
  public void stop() {
    if (!_running) {
      return;
    }

    _sensorManager.unregisterListener(_listener);

    _running = false;
  }

  //endregion

  //region Methods

  Sensor getDefaultSensorSafe(int sensor) {
    Sensor defaultSensor = _sensorManager.getDefaultSensor(sensor);
    if (defaultSensor == null) {
      throw new UnsupportedOperationException("Sensor " + sensor + " not found.");
    }

    return defaultSensor;
  }

  protected void onNewInfo(AccelerometerInfo info) {
    _lastInfo = info;

    if (_accelerometerInfoObservable != null) {
      _accelerometerInfoObservable.notify(this, info);
    }
  }

  private float[] _R = new float[9];
  private float[] _orientation;
  private float[] _accelerometerValues;


  private float[] gravity = new float[3];
  private static final float ALPHA = 0.8f;

  protected float[] highPass(float[] values) {
    float[] filteredValues = new float[3];

    gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * values[0];
    gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * values[1];
    gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * values[2];

    filteredValues[0] = values[0] - gravity[0];
    filteredValues[1] = values[1] - gravity[1];
    filteredValues[2] = values[2] - gravity[2];

    return filteredValues;
  }

  protected void onMagneticVectorValues(float[] values) {
//    Log.e(TAG, "Magnetic change");
    if (_accelerometerValues == null) {
      return;
    }

    SensorManager.getRotationMatrix(_R, null, _accelerometerValues, values);
    onRotationMatrixChange();
  }

  protected void onRotationVectorValues(float[] values) {
//    Log.e(TAG, "Rotation vector change");
    SensorManager.getRotationMatrixFromVector(_R, values);

    onRotationMatrixChange();
  }

  protected void onRotationMatrixChange() {
//    Log.e(TAG, "New rotation matrix: " + Arrays.toString(_R));

    if (_orientation == null) {
      _orientation = new float[3];
    }

    SensorManager.getOrientation(_R, _orientation);
  }

  protected void onAccelerometerValues(float[] accelerometerValues) {
    _accelerometerValues = accelerometerValues;

    if (_orientation == null) {
      return;
    }

    accelerometerValues = highPass(accelerometerValues);

    float[] resultAcceleration = new float[3];

//    float[] rotationMatrixMultiply = new float[3];

//    rotationMatrixMultiply[0] = _R[0] * accelerometerValues[0] + _R[1] * accelerometerValues[1] + _R[2] * accelerometerValues[2];
//    rotationMatrixMultiply[1] = _R[3] * accelerometerValues[0] + _R[4] * accelerometerValues[1] + _R[5] * accelerometerValues[2];
//    rotationMatrixMultiply[2] = _R[6] * accelerometerValues[0] + _R[7] * accelerometerValues[1] + _R[8] * accelerometerValues[2];

    resultAcceleration[0] = (float) (accelerometerValues[0] * (Math.cos(_orientation[2]) * Math.cos(_orientation[0]) + Math.sin(_orientation[2]) * Math.sin(_orientation[1]) * Math.sin(_orientation[0])) + accelerometerValues[1] * (Math.cos(_orientation[1]) * Math.sin(_orientation[0])) + accelerometerValues[2] * (-Math.sin(_orientation[2]) * Math.cos(_orientation[0]) + Math.cos(_orientation[2]) * Math.sin(_orientation[1]) * Math.sin(_orientation[0])));
    resultAcceleration[1] = (float) (accelerometerValues[0] * (-Math.cos(_orientation[2]) * Math.sin(_orientation[0]) + Math.sin(_orientation[2]) * Math.sin(_orientation[1]) * Math.cos(_orientation[0])) + accelerometerValues[1] * (Math.cos(_orientation[1]) * Math.cos(_orientation[0])) + accelerometerValues[2] * (Math.sin(_orientation[2]) * Math.sin(_orientation[0]) + Math.cos(_orientation[2]) * Math.sin(_orientation[1]) * Math.cos(_orientation[0])));
    resultAcceleration[2] = (float) (accelerometerValues[0] * (Math.sin(_orientation[2]) * Math.cos(_orientation[1])) + accelerometerValues[1] * (-Math.sin(_orientation[1])) + accelerometerValues[2] * (Math.cos(_orientation[2]) * Math.cos(_orientation[1])));

//    float[] delta = new float[3];
//    delta[0] = rotationMatrixMultiply[0] - resultAcceleration[0];
//    delta[1] = rotationMatrixMultiply[1] - resultAcceleration[1];
//    delta[2] = rotationMatrixMultiply[2] - resultAcceleration[2];
//
//    Log.e(TAG, "Delta: " + Arrays.toString(delta));

    AccelerometerInfo info = new AccelerometerInfo(resultAcceleration[0], resultAcceleration[1], resultAcceleration[2]);

//    AccelerometerInfo info = new AccelerometerInfo(rotationMatrixMultiply[0], rotationMatrixMultiply[1], rotationMatrixMultiply[2]);
    onNewInfo(info);
  }

  //endregion

  //region Nested classes

  static class AccelerometerListener implements SensorEventListener {
    private final AccelerometerServiceImpl _service;

    public AccelerometerListener(AccelerometerServiceImpl service) {
      _service = service;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
      switch (event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER: {
          _service.onAccelerometerValues(event.values);
          break;
        }
        case Sensor.TYPE_MAGNETIC_FIELD: {
          _service.onMagneticVectorValues(event.values);
          break;
        }
        case Sensor.TYPE_ROTATION_VECTOR: {
          _service.onRotationVectorValues(event.values);
          break;
        }
      }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
  }

  //endregion
}
