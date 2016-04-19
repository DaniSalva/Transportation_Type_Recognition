package fi.tut.rassal.ttr.accelerometer;

import fi.tut.rassal.ttr.common.EventArgs;

public class AccelerometerInfo implements EventArgs {
  //region Fields

  public final float _x;
  public final float _y;
  public final float _z;
  public final long _time;

  //endregion

  //region Constructors


  public AccelerometerInfo(float x, float y, float z) {
    this(x, y, z, System.currentTimeMillis());
  }

  public AccelerometerInfo(float x, float y, float z, long time) {
    _x = x;
    _y = y;
    _z = z;
    _time = time;
  }

  //endregion

  //region Object impl

  @Override
  public String toString() {
    return "AccelerometerInfo{" +
            "_x=" + _x +
            ", _y=" + _y +
            ", _z=" + _z +
            ", _time=" + _time +
            '}';
  }


  //endregion
}
