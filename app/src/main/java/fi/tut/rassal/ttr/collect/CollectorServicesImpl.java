package fi.tut.rassal.ttr.collect;

import android.location.Location;
import android.os.Handler;
import fi.tut.rassal.ttr.accelerometer.AccelerometerInfo;
import fi.tut.rassal.ttr.accelerometer.AccelerometerService;
import fi.tut.rassal.ttr.common.Observable;
import fi.tut.rassal.ttr.common.StartStoppable;
import fi.tut.rassal.ttr.gps.LocationInfo;
import fi.tut.rassal.ttr.gps.LocationInfoService;

import java.util.List;

public class CollectorServicesImpl implements LocationCollectorService, AccelerometerCollectorService, StartStoppable {
  //region Constants

  public static final int LOCATION_BATCH_SIZE = 15;
  public static final int ACCELEROMETER_BATCH_SIZE = 100;

  //endregion

  //region Fields

  private final LocationCollector _locationCollector;
  private final AccelerometerCollector _accelerometerCollector;

  //endregion

  //region Constructors

  public CollectorServicesImpl(LocationInfoService locationInfoService, AccelerometerService accelerometerService) {
    _locationCollector = new LocationCollector(LOCATION_BATCH_SIZE, locationInfoService.getNewPosition());
    _accelerometerCollector = new AccelerometerCollector(ACCELEROMETER_BATCH_SIZE, accelerometerService.getNewAccelerometerInfo());
  }


  //endregion

  //region LocationCollectorService impl

  @Override
  public Observable<LocationInfoBatch> getOnNextLocationBatch() {
    return _locationCollector.getOnNextSampleBatch();
  }

  @Override
  public List<LocationInfo> getLastLocationSamples() {
    return _locationCollector.getLastSamples();
  }

  //endregion

  //region AccelerometerCollectorService impl

  @Override
  public Observable<AccelerometerInfoBatch> getOnNextAccelerometerBatch() {
    return _accelerometerCollector.getOnNextSampleBatch();
  }

  @Override
  public List<AccelerometerInfo> getLastAccelerometerSamples() {
    return _accelerometerCollector.getLastSamples();
  }

  //endregion

  //region Methods

  public void start() {
    _accelerometerCollector.start();
    _locationCollector.start();
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < 5; i++) {
          Location loc = new Location("GPS");
          loc.setSpeed(10);
          _locationCollector.onNewData(new LocationInfo(loc));
        }
        for (int i = 0; i < 5; i++) {
          Location loc = new Location("GPS");
          loc.setSpeed(1);
          _locationCollector.onNewData(new LocationInfo(loc));
        }
        for (int i = 0; i < 5; i++) {
          Location loc = new Location("GPS");
          loc.setSpeed(25);
          _locationCollector.onNewData(new LocationInfo(loc));
        }
      }
    });
  }



  public void stop() {
    _accelerometerCollector.stop();
    _locationCollector.stop();
  }

  //endregion

  //region Nested classes

  static class LocationCollector extends ServiceEventDataCollectorBase<LocationInfo, LocationInfoBatch> {
    public LocationCollector(int batchSize, Observable<LocationInfo> locationInfoObservable) {
      super(batchSize, locationInfoObservable);
    }

    @Override
    protected LocationInfoBatch createBatch(List<LocationInfo> data) {
      return new LocationInfoBatch(data);
    }
  }

  static class AccelerometerCollector extends ServiceEventDataCollectorBase<AccelerometerInfo, AccelerometerInfoBatch> {
    public AccelerometerCollector(int batchSize, Observable<AccelerometerInfo> accelerometerInfoObservable) {
      super(batchSize, accelerometerInfoObservable);
    }

    @Override
    protected AccelerometerInfoBatch createBatch(List<AccelerometerInfo> data) {
      return new AccelerometerInfoBatch(data);
    }

    private int counter;

//    @Override
//    protected void onNewData(AccelerometerInfo accelerometerInfo) {
//      if (counter++ % 10 == 0) {
//        super.onNewData(accelerometerInfo);
//      }
//    }
  }

  //endregion
}
