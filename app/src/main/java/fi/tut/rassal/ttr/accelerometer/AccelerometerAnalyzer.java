package fi.tut.rassal.ttr.accelerometer;

import fi.tut.rassal.ttr.TransportationType;
import fi.tut.rassal.ttr.common.AnalyzeResult;
import fi.tut.rassal.ttr.common.TravelPart;

import java.util.ArrayList;
import java.util.List;


public class AccelerometerAnalyzer implements  AccelerometerAnalyzeStrategy {
  final double NOISE=0.35;
  final double MAX_WALK=10;

  @Override
  public AnalyzeResult analyze(List<AccelerometerInfo> data) {
    List<TravelPart> travelParts=new ArrayList<>();
    TransportationType travelType=null;
    List<Float> xValues = new ArrayList<>(),yValues=new ArrayList<>(),zValues=new ArrayList<>();
    List<Double>rValues=new ArrayList<>();
    for(int i=0;i<data.size();i++){
      if (data.get(i)!=null){
        float newX=data.get(i)._x;
        float newY=data.get(i)._y;
        float newZ=data.get(i)._z;
        xValues.add(newX);
        yValues.add(newY);
        zValues.add(newZ);
        rValues.add(resultant(newX,newY,newZ));
      }
    }
    double xMean=getMean(xValues);
    double yMean=getMean(yValues);
    double zMean=getMean(zValues);
    double rMean=getMeanD(rValues);

    double xDesv=getStdDev(xValues);
    double yDesv=getStdDev(yValues);
    double zDesv=getStdDev(zValues);
    double rDesv=getStdDevD(rValues);

    double xVar=getVariance(xValues);
    double yVar=getVariance(yValues);
    double zVar=getVariance(zValues);
    double rVar=getVarianceD(rValues);

    if(Math.abs(rMean)<=NOISE){
      travelType=TransportationType.Static;
    }

    travelParts.add(new TravelPart(data.get(0)._time,data.get(data.size())._time,travelType));

    return new AnalyzeResult(travelParts);
  }

  double getMean(List<Float> data)
  {
    float sum = 0;
    for(float a : data)
      sum += a;
    return sum/data.size();
  }

  double getMeanD(List<Double> data)
  {
    double sum = 0;
    for(double a : data)
      sum += a;
    return sum/data.size();
  }
  double getVariance(List<Float> data)
  {
    double mean = getMean(data);
    float temp = 0;
    for(float a :data)
      temp += (mean-a)*(mean-a);
    return temp/data.size();
  }
  double getVarianceD(List<Double> data)
  {
    double mean = getMeanD(data);
    double temp = 0;
    for(double a :data)
      temp += (mean-a)*(mean-a);
    return temp/data.size();
  }

  double getStdDev(List<Float> data)
  {
    return Math.sqrt(getVariance(data));
  }

  double getStdDevD(List<Double> data)
  {
    return Math.sqrt(getVarianceD(data));
  }


  double resultant (float a, float b,float c){
    return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2));
  }
}
