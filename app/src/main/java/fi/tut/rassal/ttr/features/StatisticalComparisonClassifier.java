package fi.tut.rassal.ttr.features;

import android.util.Log;
import fi.tut.rassal.ttr.TransportationType;
import fi.tut.rassal.ttr.accelerometer.AccelerometerAnalyzeStrategy;
import fi.tut.rassal.ttr.accelerometer.AccelerometerInfo;
import fi.tut.rassal.ttr.common.AnalyzeResult;
import fi.tut.rassal.ttr.common.ArgumentCheck;
import fi.tut.rassal.ttr.common.TravelPart;
import fi.tut.rassal.ttr.record.DatabaseAdapter;
import fi.tut.rassal.ttr.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

public class StatisticalComparisonClassifier implements AccelerometerAnalyzeStrategy {
  //region Constants

  private static final String TAG = StatisticalComparisonClassifier.class.getName();

  //endregion

  //region Fields

  private final DatabaseAdapter _adapter;
  private final StatisticAnalyzer _statisticAnalyzer;

  private Features _staticFeatures;
  private Features _walkingFeatures;
  private Features _drivingFeatures;

  private final DifferenceObjectiveFunction _objectiveFunction;

  //endregion

  //region Constructors

  public StatisticalComparisonClassifier(DatabaseAdapter adapter, StatisticAnalyzer analyzer, DifferenceObjectiveFunction objectiveFunction) {
    ArgumentCheck.notNull(adapter);
    ArgumentCheck.notNull(analyzer);
    ArgumentCheck.notNull(objectiveFunction);

    _adapter = adapter;
    _statisticAnalyzer = analyzer;
    _objectiveFunction = objectiveFunction;
  }

  //endregion

  //region AccelerometerAnalyzeStrategy impl

  @Override
  public AnalyzeResult analyze(List<AccelerometerInfo> data) {
    if (data.size() == 0) {
      throw new IllegalArgumentException("Input data should not be empty");
    }

    ArrayList<TravelPart> travelParts = new ArrayList<>();
    if (!isSetup()) {
      TravelPart simplePart = singleTravelPart(data, TransportationType.Walking);
      travelParts.add(simplePart);
      return new AnalyzeResult(travelParts);
    }

    Features sampleFeatures = computeFeatures(data);

    float drivingDifference = _objectiveFunction.getDifference(sampleFeatures, _drivingFeatures);
    float walkingDifference = _objectiveFunction.getDifference(sampleFeatures, _walkingFeatures);
    float staticDifference = _objectiveFunction.getDifference(sampleFeatures, _staticFeatures);

    Log.e(TAG, "Driving diff: " + drivingDifference);
    Log.e(TAG, "Walking diff: " + walkingDifference);
    Log.e(TAG, "Static difference: " + staticDifference);

    if (drivingDifference < walkingDifference && drivingDifference < staticDifference) {
      travelParts.add(singleTravelPart(data, TransportationType.Driving));
      return new AnalyzeResult(travelParts);
    } else if (walkingDifference < staticDifference) {
      travelParts.add(singleTravelPart(data, TransportationType.Walking));
      return new AnalyzeResult(travelParts);
    } else {
      travelParts.add(singleTravelPart(data, TransportationType.Static));
      return new AnalyzeResult(travelParts);
    }
  }

  //endregion

  //region Methods

  private TravelPart singleTravelPart(List<AccelerometerInfo> data, TransportationType transportationType) {
    TravelPart singlePart = new TravelPart(data.get(0)._time, data.get(data.size() - 1)._time, transportationType);
    return singlePart;
  }

  public boolean isSetup() {
    return _walkingFeatures != null;
  }

  public void setup() {
    if (isSetup()) {
      return;
    }

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    _staticFeatures = computeFeatures(TransportationType.Static);
    _drivingFeatures = computeFeatures(TransportationType.Driving);
    _walkingFeatures = computeFeatures(TransportationType.Walking);

    stopWatch.stop();

    Log.e(TAG, "Setup time: " + stopWatch.getElapsedMs() + " ms");
  }

  private Features computeFeatures(TransportationType transportationType) {
    float[] allXYValues = _adapter.getAllXYValues(transportationType);
    StatisticData xyData = _statisticAnalyzer.analyze(allXYValues);

    float[] allZValues = _adapter.getAllZValues(transportationType);
    StatisticData zData = _statisticAnalyzer.analyze(allZValues);

    return new Features(xyData, zData);
  }

  private Features computeFeatures(List<AccelerometerInfo> data) {
    final int size = data.size();

    float[] xy = new float[size];
    float[] z = new float[size];

    for (int i = 0; i < size; i++) {
      AccelerometerInfo info = data.get(i);

      xy[i] = info._x * info._x + info._y + info._y;
      z[i] = info._z;
    }

    StatisticData xyData = _statisticAnalyzer.analyze(xy);
    StatisticData zData = _statisticAnalyzer.analyze(z);

    return new Features(xyData, zData);
  }

  //endregion
}
