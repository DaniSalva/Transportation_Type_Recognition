package fi.tut.rassal.ttr.features;

import fi.tut.rassal.ttr.common.ArgumentCheck;

public class Features {
  //region Fields

  private final StatisticData _xyData;
  private final StatisticData _zData;

  //endregion

  //region Constructors

  public Features(StatisticData xyData, StatisticData zData) {
    ArgumentCheck.notNull(xyData);
    ArgumentCheck.notNull(zData);

    _xyData = xyData;
    _zData = zData;
  }

  //endregion

  //region Properties

  public StatisticData getXYData() {
    return _xyData;
  }

  public StatisticData getZData() {
    return _zData;
  }

  //endregion

  //region Object impl

  @Override
  public String toString() {
    return "Features{" +
            "_xyData=" + _xyData +
            ", _zData=" + _zData +
            '}';
  }


  //endregion
}
