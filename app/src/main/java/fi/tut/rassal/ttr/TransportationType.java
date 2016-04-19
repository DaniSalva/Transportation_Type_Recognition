package fi.tut.rassal.ttr;

public enum TransportationType {
  Static(R.drawable.ic_static),
  Walking(R.drawable.ic_walking),
  Driving(R.drawable.ic_car);

  public final int _iconRes;

  TransportationType(int iconRes) {
    _iconRes = iconRes;
  }

}
