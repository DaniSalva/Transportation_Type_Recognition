package fi.tut.rassal.ttr.gps;

import fi.tut.rassal.ttr.TransportationType;
import fi.tut.rassal.ttr.common.AnalyzeResult;
import fi.tut.rassal.ttr.common.TravelPart;

import java.util.ArrayList;
import java.util.List;

public class GpsAnalyzer implements GpsAnalyzeStrategy {

  private final int STATIC=0;
  private final int WALK=2;
  private final int DRIVE=12;

  @Override
  public AnalyzeResult analyze(List<LocationInfo> data) {
    List<TravelPart> travel = new ArrayList<>();
    long initialTime=data.get(0).getTime(); //Time 0
    TransportationType actualWay=whichWay(data.get(0)); //First way of transportation
    long beginTime=initialTime; //Time when a way of transportation begin

    for(int i=1;i<data.size();i++){
      if(actualWay!=whichWay(data.get(i))){
        travel.add(new TravelPart((beginTime-initialTime),data.get(i).getTime(),actualWay));
        beginTime=data.get(i).getTime();
        actualWay=whichWay(data.get(i));
      }
    }
    travel.add(new TravelPart((beginTime-initialTime),data.get(data.size()-1).getTime(),actualWay));
    return new AnalyzeResult(travel);
  }

  public TransportationType whichWay(LocationInfo loc){
    if (loc.getSpeed()>=STATIC && loc.getSpeed()<=WALK)
      return TransportationType.Static;
    else if (loc.getSpeed()>=WALK && loc.getSpeed()<=DRIVE)
      return TransportationType.Walking;
    else if (loc.getSpeed()>=WALK)
      return TransportationType.Driving;
    return null;
  }
}
