package fi.tut.rassal.ttr.common;

import java.util.Collections;
import java.util.List;

public class DataBatch<TData> implements EventArgs {
  //region Fields

  private final List<TData> _data;

  //endregion

  //region Constructors

  public DataBatch(List<TData> data) {
    _data = Collections.unmodifiableList(data);
  }

  //endregion

  //region Properties

  public List<TData> getData() {
    return _data;
  }

  //endregion
}
