package fi.tut.rassal.ttr.accelerometer;

import fi.tut.rassal.ttr.common.Observable;
import fi.tut.rassal.ttr.common.StartStoppable;

/**
 * Service returning acceleration already transformed according to earthc surface
 */
public interface AccelerometerService extends StartStoppable {

  //region Events

  Observable<AccelerometerInfo> getNewAccelerometerInfo();

  //endregion

  //region Properties

  AccelerometerInfo getLastInfo();

  boolean isListening();

  //endregion
}
