package fi.tut.rassal.ttr.common;

import fi.tut.rassal.ttr.TransportationType;

public class TravelPart {
  //region Fields

  private final long _begin;
  private final long _end;
  private final TransportationType _type;
  private final float _probability;

  //endregion

  //region Constructors

  public TravelPart(long begin, long end, TransportationType type) {
    this(begin, end, type, 1.0f);
  }

  public TravelPart(long begin, long end, TransportationType type, float probability) {
    if (end < begin) {
      throw new IllegalArgumentException("End cannot be before start.");
    }

    ArgumentCheck.notNull(type);

    if (probability < 0 || probability > 1) {
      throw new IllegalArgumentException("Probability have to in interval <0,1>, not " + probability);
    }

    _begin = begin;
    _end = end;
    _type = type;
    _probability = probability;
  }

  //endregion

  //region Properties

  public long getBegin() {
    return _begin;
  }

  public long getEnd() {
    return _end;
  }

  public TransportationType getType() {
    return _type;
  }

  public float getProbability() {
    return _probability;
  }

  //endregion
}
