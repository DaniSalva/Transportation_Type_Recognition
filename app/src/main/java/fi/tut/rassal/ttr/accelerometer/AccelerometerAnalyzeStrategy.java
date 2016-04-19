package fi.tut.rassal.ttr.accelerometer;

import fi.tut.rassal.ttr.common.AnalyzeResult;

import java.util.List;

public interface AccelerometerAnalyzeStrategy {
  //region Methods

  AnalyzeResult analyze(List<AccelerometerInfo> data);

  //endregion
}
