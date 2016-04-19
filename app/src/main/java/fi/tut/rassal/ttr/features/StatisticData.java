package fi.tut.rassal.ttr.features;

public class StatisticData {
  //region Fields

  public final float _mean;
  public final float _min;
  public final float _max;
  public final float _positiveValuesMean;
  public final float _negativeValuesMean;
  public final float _standardDeviation;
  public final float _10percentile;
  public final float _25percentile;
  public final float _50percentile;
  public final float _75percentile;
  public final float _90percentile;

  //endregion

  //region Constructors

  public StatisticData(float mean, float min, float max, float positiveValuesMean, float negativeValuesMean,
                       float standardDeviation, float a10percentile, float a25percentile, float a50percentile,
                       float a75percentile, float a90percentile) {
    _mean = mean;
    _min = min;
    _max = max;
    _positiveValuesMean = positiveValuesMean;
    _negativeValuesMean = negativeValuesMean;
    _standardDeviation = standardDeviation;
    _10percentile = a10percentile;
    _25percentile = a25percentile;
    _50percentile = a50percentile;
    _75percentile = a75percentile;
    _90percentile = a90percentile;
  }

  //endregion

  //region Object impl

  @Override
  public String toString() {
    return "StatisticData{" +
            "_mean=" + _mean +
            ", _min=" + _min +
            ", _max=" + _max +
            ", _positiveValuesMean=" + _positiveValuesMean +
            ", _negativeValuesMean=" + _negativeValuesMean +
            ", _standardDeviation=" + _standardDeviation +
            ", _10percentile=" + _10percentile +
            ", _25percentile=" + _25percentile +
            ", _50percentile=" + _50percentile +
            ", _75percentile=" + _75percentile +
            ", _90percentile=" + _90percentile +
            '}';
  }


  //endregion

  //region Nested classes

  public static class Builder {
    private float _mean;
    private float _min;
    private float _max;
    private float _positiveValuesMean;
    private float _negativeValuesMean;
    private float _standardDeviation;
    private float _10percentile;
    private float _25percentile;
    private float _50percentile;
    private float _75percentile;
    private float _90percentile;

    public Builder setMean(float mean) {
      _mean = mean;
      return this;
    }

    public Builder setMin(float min) {
      _min = min;
      return this;
    }

    public Builder setMax(float max) {
      _max = max;
      return this;
    }

    public Builder setPositiveValuesMean(float positiveValuesMean) {
      _positiveValuesMean = positiveValuesMean;
      return this;
    }

    public Builder setNegativeValuesMean(float negativeValuesMean) {
      _negativeValuesMean = negativeValuesMean;
      return this;
    }

    public Builder setStandardDeviation(float standardDeviation) {
      _standardDeviation = standardDeviation;
      return this;
    }

    public Builder set10percentile(float a10percentile) {
      _10percentile = a10percentile;
      return this;
    }

    public Builder set25percentile(float a25percentile) {
      _25percentile = a25percentile;
      return this;
    }

    public Builder set50percentile(float a50percentile) {
      _50percentile = a50percentile;
      return this;
    }

    public Builder set75percentile(float a75percentile) {
      _75percentile = a75percentile;
      return this;
    }

    public Builder set90percentile(float a90percentile) {
      _90percentile = a90percentile;
      return this;
    }

    public StatisticData create() {
      return new StatisticData(_mean, _min, _max, _positiveValuesMean, _negativeValuesMean, _standardDeviation, _10percentile, _25percentile, _50percentile, _75percentile, _90percentile);
    }
  }

  //endregion
}
