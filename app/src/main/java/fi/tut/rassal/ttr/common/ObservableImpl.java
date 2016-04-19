package fi.tut.rassal.ttr.common;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Basic implementation of {@link Observable}.
 * <p/>
 * Not thread safe.
 *
 * @param <TArgs>
 */
public class ObservableImpl<TArgs extends EventArgs> implements Observable<TArgs> {
  //region Fields

  private Collection<Observer<TArgs>> _observers;

  //endregion

  //region Properties

  public Collection<Observer<TArgs>> getObservers() {
    if (_observers == null) {
      _observers = createObserversCollection();
    }

    return _observers;
  }

  //endregion

  //region Observable implementation

  @Override
  public boolean registerObserver(Observer<TArgs> observer) {
    return getObservers().add(observer);
  }

  @Override
  public boolean unregisterObserver(Observer observer) {
    return getObservers().remove(observer);
  }

  @Override
  public boolean hasObservers() {
    return !getObservers().isEmpty();
  }

  //endregion

  //region Methods

  public void notify(Object sender, TArgs eventArgs) {
    for (Observer<TArgs> observer : getObservers()) {
      observer.update(sender, eventArgs);
    }
  }

  public void unregisterAll() {
    _observers.clear();
  }

  protected Collection<Observer<TArgs>> createObserversCollection() {
    return new ArrayList<>();
  }

  //endregion
}
