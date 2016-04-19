package fi.tut.rassal.ttr;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fi.tut.rassal.ttr.accelerometer.AccelerometerInfo;
import fi.tut.rassal.ttr.accelerometer.AccelerometerService;
import fi.tut.rassal.ttr.common.Observer;
import fi.tut.rassal.ttr.record.DatabaseAdapter;

public class AccelerometerDataRecordActivity extends BaseActivity implements Observer<AccelerometerInfo> {
  //region Fields

  private boolean _running = false;
  private int _counter;

  @InjectView(R.id.info_view) TextView _infoView;

  //endregion


  //region Properties

  public DatabaseAdapter getDatabaseAdapter() {
    return getTTRApp().getDatabaseAdapter();
  }

  public AccelerometerService getAccelerometerService() {
    return getTTRApp().getAccelerometerService();
  }

  //endregion

  //region Observer impl

  @Override
  public void update(Object sender, AccelerometerInfo args) {
    if (_running) {
      if (_counter++ % 10 == 0) {
        getDatabaseAdapter().addRecord(args);
      }
    }
  }

  //endregion

  //region Activity overrides

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_record_accelerometer);

    ButterKnife.inject(this);

    getDatabaseAdapter().start();

    getAccelerometerService().getNewAccelerometerInfo().registerObserver(this);
  }

  @Override
  protected void onDestroy() {
    getDatabaseAdapter().stop();

    getAccelerometerService().getNewAccelerometerInfo().unregisterObserver(this);

    super.onDestroy();
  }

  //endregion

  //region Methods

  @OnClick(R.id.accelerometer_record_btn_static)
  void onStaticClick() {
    getDatabaseAdapter().setCurrentTransportation(TransportationType.Static);
    _infoView.setText("Static");
    _running = true;
  }

  @OnClick(R.id.accelerometer_record_btn_walking)
  void onWalkingClick() {
    getDatabaseAdapter().setCurrentTransportation(TransportationType.Walking);
    _infoView.setText("Walking");
    _running = true;
  }

  @OnClick(R.id.accelerometer_record_btn_driving)
  void onDrivingClick() {
    getDatabaseAdapter().setCurrentTransportation(TransportationType.Driving);
    _infoView.setText("Driving");
    _running = true;
  }

  @OnClick(R.id.accelerometer_record_btn_stop)
  void onStopClick() {
    _infoView.setText("...");
    _running = false;
  }

  //endregion
}
