package fi.tut.rassal.ttr.collect;

import fi.tut.rassal.ttr.common.DataBatch;
import fi.tut.rassal.ttr.common.EventArgs;
import fi.tut.rassal.ttr.common.Observable;
import fi.tut.rassal.ttr.common.StartStoppable;
import fi.tut.rassal.ttr.gps.LocationInfo;

import java.util.Collections;
import java.util.List;

public interface LocationCollectorService{
  //region Events

  Observable<LocationInfoBatch> getOnNextLocationBatch();

  //endregion

  //region Methods

  /**
   * Provides gathered data to analyze from last period
   *
   * @return List of gathered location data
   */
  List<LocationInfo> getLastLocationSamples();

  //endregion

  //region Nested classes

  class LocationInfoBatch extends DataBatch<LocationInfo> {
    public LocationInfoBatch(List<LocationInfo> data) {
      super(data);
    }
  }

  //endregion
}
