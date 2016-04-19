package fi.tut.rassal.ttr.gps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import fi.tut.rassal.ttr.common.ArgumentCheck;
import fi.tut.rassal.ttr.common.Observable;
import fi.tut.rassal.ttr.common.ObservableImpl;

public class LocationInfoServiceImpl implements LocationInfoService, LocationListener {


  final long MIN_TIME = 1000;
  final float MIN_DIST  = 4;

  //region Fields

  private LocationInfo _lastPosition;
  private final LocationManager _locationManager;

  private ObservableImpl<LocationInfo> _newLocationInfoEvent;
  private boolean _running;


  //endregion

  //region Constructors

  public LocationInfoServiceImpl(LocationManager locationManager) {
    ArgumentCheck.notNull(locationManager);

    _locationManager = locationManager;
    _running=false;
  }

  //endregion

  //region LocationInfoService impl

  @Override
  public Observable<LocationInfo> getNewPosition() {
    if (_newLocationInfoEvent == null) {
      _newLocationInfoEvent = new ObservableImpl<>();
    }

    return _newLocationInfoEvent;
  }

  @Override
  public LocationInfo getLastPosition() {
    if(_lastPosition!=null){
      return _lastPosition;
    }else{
      return new LocationInfo(_locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
    }
  }

  @Override
  public boolean isTracking() {
    return _running;
  }

  @Override
  public boolean isTrackingAvailable() {
    return _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  @Override
  public void start() {
    if (_running){
      return;
    }
    _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, this);
    _running=true;
  }

  @Override
  public void stop() {
    if(!_running){
      return;
    }
    _locationManager.removeUpdates(this);
    _running=false;
  }


  //endregion

  //region MethodsListener


  @Override
  public void onLocationChanged(Location location) {
    if (location!=null){
      LocationInfo locationInfo = new LocationInfo(location);

      if (_newLocationInfoEvent != null) {
        _newLocationInfoEvent.notify(this, locationInfo);
      }
      _lastPosition=locationInfo;
    }
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }
  //endregion

  }