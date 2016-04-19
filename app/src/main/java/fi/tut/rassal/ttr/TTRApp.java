package fi.tut.rassal.ttr;

import android.app.Application;
import android.hardware.SensorManager;
import android.location.LocationManager;
import fi.tut.rassal.ttr.accelerometer.*;
import fi.tut.rassal.ttr.collect.AccelerometerCollectorService;
import fi.tut.rassal.ttr.collect.CollectorServicesImpl;
import fi.tut.rassal.ttr.collect.LocationCollectorService;
import fi.tut.rassal.ttr.features.PercentDifferenceObjectiveFunction;
import fi.tut.rassal.ttr.features.StatisticAnalyzer;
import fi.tut.rassal.ttr.features.StatisticalComparisonClassifier;
import fi.tut.rassal.ttr.gps.GpsAnalyzeStrategy;
import fi.tut.rassal.ttr.gps.GpsAnalyzer;
import fi.tut.rassal.ttr.gps.LocationInfoService;
import fi.tut.rassal.ttr.gps.LocationInfoServiceImpl;
import fi.tut.rassal.ttr.record.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;

public final class TTRApp extends Application {
  //region Fields

  private LocationInfoService _locationInfoService;
  private AccelerometerService _accelerometerService;

  private CollectorServicesImpl _collectorServicesImpl;
  private GpsAnalyzer _locationAnalyzer;

  private DatabaseAdapter _databaseAdapter;

  private AccelerometerAnalyzer _accelerometerAnalyzer;
  private StatisticalComparisonClassifier _classifier;

  //endregion

  //region Properties

  public LocationInfoService getLocationInfoService() {
    if (_locationInfoService == null) {
      _locationInfoService = new LocationInfoServiceImpl((LocationManager) getSystemService(LOCATION_SERVICE));
    }

    return _locationInfoService;
  }

  public AccelerometerService getAccelerometerService() {
    if (_accelerometerService == null) {
      _accelerometerService = new AccelerometerServiceImpl((SensorManager) getSystemService(SENSOR_SERVICE));
    }

    return _accelerometerService;
  }

  private CollectorServicesImpl getCollectorServicesImpl() {
    if (_collectorServicesImpl == null) {
      _collectorServicesImpl = new CollectorServicesImpl(getLocationInfoService(), getAccelerometerService());
    }

    return _collectorServicesImpl;
  }

  private GpsAnalyzer getGPSAnalyzer() {
    if (_locationAnalyzer == null) {
      _locationAnalyzer = new GpsAnalyzer();
    }

    return _locationAnalyzer;
  }

  private AccelerometerAnalyzer getSAccelerometerAnalyzer() {
    if (_accelerometerAnalyzer == null) {
      _accelerometerAnalyzer = new AccelerometerAnalyzer();
    }

    return _accelerometerAnalyzer;
  }

  public StatisticalComparisonClassifier getClassifier() {
    if(_classifier == null){
      _classifier = new StatisticalComparisonClassifier(getDatabaseAdapter(),
              new StatisticAnalyzer(), new PercentDifferenceObjectiveFunction());
    }

    return _classifier;
  }

  public LocationCollectorService getLocationCollectorService() {
    return getCollectorServicesImpl();
  }

  public AccelerometerCollectorService getAccelerometerCollectorService() {
    return getCollectorServicesImpl();
  }

  public GpsAnalyzeStrategy getLocationAnalyzer() {
    return getGPSAnalyzer();
  }

  public AccelerometerAnalyzeStrategy getAccelerometerAnalyzerStrategy() {
    return getClassifier();
  }

  public DatabaseAdapter getDatabaseAdapter() {
    if (_databaseAdapter == null) {
      _databaseAdapter = new DatabaseAdapter(this);
    }

    return _databaseAdapter;
  }

  //endregion

  //region Application overrides

  @Override
  public void onCreate() {
    super.onCreate();

    //getClassifier().setup();

    getAccelerometerService().start();
    getLocationInfoService().start();

    getCollectorServicesImpl().start();
  }

  //endregion
}
