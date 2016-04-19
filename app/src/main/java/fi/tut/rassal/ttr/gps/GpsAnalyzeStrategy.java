package fi.tut.rassal.ttr.gps;

import fi.tut.rassal.ttr.common.AnalyzeResult;

import java.util.List;

public interface GpsAnalyzeStrategy {
  //region Methods

  AnalyzeResult analyze(List<LocationInfo> data);

  //endregion
}
