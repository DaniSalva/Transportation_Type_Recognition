package fi.tut.rassal.ttr.features;

import java.util.Arrays;

public class StatisticAnalyzer {
  //region Methods

  public StatisticData analyze(float[] data) {
    Arrays.sort(data);

    StatisticData.Builder builder = new StatisticData.Builder();

    builder.set10percentile(getPercentile(10, data));
    builder.set25percentile(getPercentile(25, data));
    builder.set50percentile(getPercentile(50, data));
    builder.set75percentile(getPercentile(75, data));
    builder.set90percentile(getPercentile(90, data));

    float positiveSum = 0;
    int positiveCount = 0;

    float negativeSum = 0;
    int negativeCount = 0;

    float max = Float.MIN_VALUE;
    float min = Float.MAX_VALUE;

    for (float value : data) {
      if (value > max) {
        max = value;
      }

      if (value < min) {
        min = value;
      }

      if (value < 0) {
        negativeSum += value;
        negativeCount++;
      } else {
        positiveSum += value;
        positiveCount++;
      }
    }

    builder.setMin(min);
    builder.setMax(max);

    float sum = positiveSum + negativeSum;
    float mean = sum / data.length;
    builder.setMean(mean);

    if (positiveCount == 0) {
      builder.setPositiveValuesMean(0);
    } else {
      float positiveMean = positiveSum / positiveCount;
      builder.setPositiveValuesMean(positiveMean);
    }

    if (negativeCount == 0) {
      builder.setNegativeValuesMean(0);
    } else {
      float negativeMean = negativeSum / negativeCount;
      builder.setNegativeValuesMean(negativeMean);
    }

    float standardDeviation = computeStandardDeviation(data, mean);
    builder.setStandardDeviation(standardDeviation);

    return builder.create();
  }

  private float computeStandardDeviation(float[] data, float mean) {
    float sum = 0;

    float delta;
    for (float value : data) {
      delta = value - mean;
      sum += delta * delta;
    }

    return (float) Math.sqrt(sum / data.length);
  }

  protected float getPercentile(int percentile, float[] data) {
    int index = (int) Math.round((percentile * data.length) / 100.0) - 1;

    if (index < 0) {
      index = 0;
    }

    return data[index];
  }

  //endregion
}
