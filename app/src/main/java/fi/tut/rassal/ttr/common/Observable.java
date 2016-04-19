package fi.tut.rassal.ttr.common;

public interface Observable<TArgs extends EventArgs> {
  boolean hasObservers();

  boolean registerObserver(Observer<TArgs> observer);

  boolean unregisterObserver(Observer<TArgs> observer);
}
