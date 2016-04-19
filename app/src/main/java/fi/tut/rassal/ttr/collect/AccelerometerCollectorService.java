package fi.tut.rassal.ttr.collect;

import fi.tut.rassal.ttr.accelerometer.AccelerometerInfo;
import fi.tut.rassal.ttr.common.DataBatch;
import fi.tut.rassal.ttr.common.Observable;
import fi.tut.rassal.ttr.common.StartStoppable;

import java.util.List;

public interface AccelerometerCollectorService{
  //region Events

  Observable<AccelerometerInfoBatch> getOnNextAccelerometerBatch();

  //endregion

  //region Methods

  /**
   * Provides gathered data to analyze from last period
   *
   * @return List of gathered location data
   */
  List<AccelerometerInfo> getLastAccelerometerSamples();

  //endregion

  //region Nested classes

  class AccelerometerInfoBatch extends DataBatch<AccelerometerInfo> {
    public AccelerometerInfoBatch(List<AccelerometerInfo> data) {
      super(data);
    }
  }

  //endregion
}
