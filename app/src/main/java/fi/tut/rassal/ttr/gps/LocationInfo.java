package fi.tut.rassal.ttr.gps;

import android.location.Location;
import fi.tut.rassal.ttr.common.EventArgs;

public class LocationInfo implements EventArgs{
  //region Fields

  private final Location _location;

  //endregion

  //region Constructors

  public LocationInfo(Location location) {
    _location = location;
  }

  //endregion

  //region Properties

  //TODO: publish out just properties we need

  public float getSpeed() {
    return _location.getSpeed();
  }

  public long getTime() {
    return _location.getTime();
  }

  public boolean hasSpeed() {
    return _location.hasSpeed();
  }

  //endregion
}
