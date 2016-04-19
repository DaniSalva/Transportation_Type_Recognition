package fi.tut.rassal.ttr.common;

public interface Observer<TArgs extends EventArgs> {
  void update(Object sender, TArgs args);
}
