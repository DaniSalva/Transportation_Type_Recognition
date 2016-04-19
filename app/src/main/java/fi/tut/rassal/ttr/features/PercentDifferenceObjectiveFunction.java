package fi.tut.rassal.ttr.features;

public class PercentDifferenceObjectiveFunction implements DifferenceObjectiveFunction{
  //region Constants

  public static final float HIGH_WEIGHT = 7.0f;
  public static final float MIDDLE_WEIGHT = 4.0f;
  public static final float LOW_WEIGHT = 2.0f;
  public static final float SUPER_SMALL = 0.00001f;

  //endregion

  //region DifferenceObjectiveFunction impl

  public float getDifference(Features first, Features second) {

    float zDiff = getDifference(first.getZData(), second.getZData());
    float xyDiff = getDifference(first.getXYData(), second.getXYData());

    //sqrt because we have squares there
    return (float) (zDiff + Math.sqrt(xyDiff));
  }

  //endregion

  //region Methods

  private float getDifference(StatisticData first, StatisticData second) {
    float meanDiff = percentDifference(first._mean, second._mean);
    float deviationDiff = percentDifference(first._standardDeviation, second._standardDeviation);
    float negativeMean = percentDifference(first._negativeValuesMean, second._negativeValuesMean);
    float positiveMean = percentDifference(first._positiveValuesMean, second._positiveValuesMean);
    float _10percentileDiff = percentDifference(first._10percentile, second._10percentile);
    float _25percentileDiff = percentDifference(first._25percentile, second._25percentile);
    float _50percentileDiff = percentDifference(first._50percentile, second._50percentile);
    float _75percentileDiff = percentDifference(first._75percentile, second._75percentile);
    float _90percentileDiff = percentDifference(first._90percentile, second._90percentile);

    //deviation and diff have highest weight
    float diff = meanDiff * HIGH_WEIGHT + deviationDiff * HIGH_WEIGHT;
    diff += _10percentileDiff * MIDDLE_WEIGHT + _25percentileDiff * MIDDLE_WEIGHT +
            _50percentileDiff + MIDDLE_WEIGHT + _75percentileDiff + MIDDLE_WEIGHT + _90percentileDiff * MIDDLE_WEIGHT;

    diff +=  positiveMean * LOW_WEIGHT;

    return diff;
  }

  private float percentDifference(float first, float second) {
    if(Math.abs(first) < SUPER_SMALL || Math.abs(second) < SUPER_SMALL){
      return 99999;
    }

    float one = Math.abs((100 * first) / second);
    float two = Math.abs((100 * second) / first);

    return Math.max(one, two);
  }

  //endregion
}
