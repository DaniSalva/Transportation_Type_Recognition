package fi.tut.rassal.ttr.common;

import java.util.Collections;
import java.util.List;

public class AnalyzeResult {
  //region Fields

  private final List<TravelPart> _travelParts;

  //endregion

  //region Constructors

  public AnalyzeResult(List<TravelPart> travelParts) {
    _travelParts = Collections.unmodifiableList(travelParts);
  }

  //endregion

  //region Properties

  public List<TravelPart> getTravelPartsReadOnly() {
    return _travelParts;
  }

  //endregion
}
