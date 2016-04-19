package fi.tut.rassal.ttr.gps;

import fi.tut.rassal.ttr.common.Observable;
import fi.tut.rassal.ttr.common.StartStoppable;

public interface LocationInfoService extends StartStoppable{
  //region Events

  Observable<LocationInfo> getNewPosition();

  //endregion

  //region Properties

  LocationInfo getLastPosition();

  boolean isTracking();

  boolean isTrackingAvailable();

  //endregion
}
