package fi.tut.rassal.ttr.collect;

import android.widget.Toast;
import fi.tut.rassal.ttr.common.*;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceEventDataCollectorBase<TData extends EventArgs, TBatchType extends EventArgs>
        implements Observer<TData>, StartStoppable {
  //region Fields

  private final CircularFifoQueue<TData> _dataBuffer;

  private ObservableImpl<TBatchType> _onNewBatch;

  private int _samplesCounter = 0;
  private final int _batchSize;

  private Observable<TData> _dataObservable;

  //endregion

  //region Constructors

  public ServiceEventDataCollectorBase(int batchSize, Observable<TData> dataObservable) {
    if (batchSize < 0) {
      throw new IllegalArgumentException("Batch size must be greater than 0");
    }

    ArgumentCheck.notNull(dataObservable);

    _batchSize = batchSize;
    _dataObservable = dataObservable;
    _dataBuffer = new CircularFifoQueue<>(batchSize);
  }


  //endregion

  //region Properties

  public Observable<TBatchType> getOnNextSampleBatch() {
    if (_onNewBatch == null) {
      _onNewBatch = new ObservableImpl<>();
    }

    return _onNewBatch;
  }

  public List<TData> getLastSamples() {
    return new ArrayList<>(_dataBuffer);
  }

  //endregion

  //region Observer impl

  @Override
  public void update(Object sender, TData args) {
    onNewData(args);
  }

  //endregion

  //region Methods

  public void start() {
    _dataObservable.registerObserver(this);
  }

  public void stop() {
    _dataObservable.unregisterObserver(this);
  }

  protected void onNewData(TData data) {
    _dataBuffer.add(data);

    _samplesCounter++;
    if (_samplesCounter % _batchSize == 0) {
      onNextBatch();
    }
  }

  protected void onNextBatch() {
    if (_onNewBatch != null) {
      TBatchType batch = createBatch(new ArrayList<>(_dataBuffer));
      _onNewBatch.notify(this, batch);
    }
  }

  protected abstract TBatchType createBatch(List<TData> data);

  //endregion
}
