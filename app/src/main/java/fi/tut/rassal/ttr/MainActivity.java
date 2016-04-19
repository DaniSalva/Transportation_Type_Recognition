package fi.tut.rassal.ttr;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fi.tut.rassal.ttr.accelerometer.AccelerometerAnalyzeStrategy;
import fi.tut.rassal.ttr.accelerometer.AccelerometerInfo;
import fi.tut.rassal.ttr.accelerometer.AccelerometerService;
import fi.tut.rassal.ttr.collect.AccelerometerCollectorService;
import fi.tut.rassal.ttr.collect.LocationCollectorService;
import fi.tut.rassal.ttr.common.Observer;
import fi.tut.rassal.ttr.common.TravelPart;
import fi.tut.rassal.ttr.gps.GpsAnalyzeStrategy;
import fi.tut.rassal.ttr.gps.LocationInfo;
import fi.tut.rassal.ttr.gps.LocationInfoService;
import fi.tut.rassal.ttr.view.AccelerometerDisplayView;
import fi.tut.rassal.ttr.view.LocationDisplayView;
import fi.tut.rassal.ttr.view.TransportationHistoryView;

import java.util.List;
import java.util.Stack;


public class MainActivity extends BaseActivity {

  //region Fields

  @InjectView(R.id.accelerometer_view) AccelerometerDisplayView _accelerometerDisplayView;
  @InjectView(R.id.location_info_view) LocationDisplayView _locationDisplayView;
  @InjectView(android.R.id.tabhost) TabHost _tabHost;
  @InjectView(R.id.accelerometer_transportation_history_view)
  TransportationHistoryView _accelerometerTransportationHistoryView;
  @InjectView(R.id.location_transportation_history_view) TransportationHistoryView _locationTransportationHistoryView;

  private Observer<AccelerometerInfo> _accelerometerObserver;
  private Observer<LocationInfo> _locationObserver;

  private Observer<LocationCollectorService.LocationInfoBatch> _locationCollectorObserver;
  private Observer<AccelerometerCollectorService.AccelerometerInfoBatch> _accelerometerCollectorObserver;

  private Stack<TravelPart> _accelerometerTravelParts = new Stack<>();


  //endregion

  //region Properties
  public AccelerometerService getAccelerometerService() {
    return getTTRApp().getAccelerometerService();
  }

  public LocationInfoService getLocationService() {
    return getTTRApp().getLocationInfoService();
  }

  public LocationCollectorService getLocationCollectorService() {
    return getTTRApp().getLocationCollectorService();
  }

  public AccelerometerCollectorService getAccelerometerCollectorService() {
    return getTTRApp().getAccelerometerCollectorService();
  }


  public GpsAnalyzeStrategy getLocationAnalyzer() {
    return getTTRApp().getLocationAnalyzer();
  }

  public AccelerometerAnalyzeStrategy getAccelerometerAnalyzer() {
    return getTTRApp().getAccelerometerAnalyzerStrategy();
  }


  //endregion

  //region Activity overrides

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.inject(this);

    _accelerometerObserver = new Observer<AccelerometerInfo>() {
      @Override
      public void update(Object sender, AccelerometerInfo args) {
        _accelerometerDisplayView.showData(args);
      }
    };

    _tabHost.setup();

    TabHost.TabSpec accelerometerTabSpec = _tabHost.newTabSpec("Accelerometer");
    accelerometerTabSpec.setIndicator(getString(R.string.accelerometer_history_tab_title));
    accelerometerTabSpec.setContent(R.id.accelerometer_transportation_history_view);
    _tabHost.addTab(accelerometerTabSpec);

    TabHost.TabSpec locationTabSpec = _tabHost.newTabSpec("Location");
    locationTabSpec.setIndicator(getString(R.string.location_history_tab_title));
    locationTabSpec.setContent(R.id.location_transportation_history_view);
    _tabHost.addTab(locationTabSpec);

    getAccelerometerService().getNewAccelerometerInfo().registerObserver(_accelerometerObserver);


    _locationObserver = new Observer<LocationInfo>() {
      @Override
      public void update(Object sender, LocationInfo args) {
        if (args != null) {
          _locationDisplayView.showData(args);
        }
      }
    };
    getLocationService().getNewPosition().registerObserver(_locationObserver);

    _locationCollectorObserver = new Observer<LocationCollectorService.LocationInfoBatch>() {
      @Override
      public void update(Object sender, LocationCollectorService.LocationInfoBatch args) {
        List<TravelPart> travelParts = getLocationAnalyzer().analyze(args.getData()).getTravelPartsReadOnly();
        _locationTransportationHistoryView.showData(travelParts);
      }
    };
    getLocationCollectorService().getOnNextLocationBatch().registerObserver(_locationCollectorObserver);

    _accelerometerCollectorObserver = new Observer<AccelerometerCollectorService.AccelerometerInfoBatch>() {
      @Override
      public void update(Object sender, AccelerometerCollectorService.AccelerometerInfoBatch args) {
        List<TravelPart> travelParts = getAccelerometerAnalyzer().analyze(args.getData()).getTravelPartsReadOnly();

        for (TravelPart travelPart : travelParts) {
          _accelerometerTravelParts.push(travelPart);
        }

        _accelerometerTransportationHistoryView.showData(_accelerometerTravelParts);
      }
    };

    getAccelerometerCollectorService().getOnNextAccelerometerBatch().registerObserver(_accelerometerCollectorObserver);
  }

  @Override
  protected void onDestroy() {
    getAccelerometerService().getNewAccelerometerInfo().unregisterObserver(_accelerometerObserver);
    getLocationService().getNewPosition().unregisterObserver(_locationObserver);
    getLocationCollectorService().getOnNextLocationBatch().unregisterObserver(_locationCollectorObserver);
    getAccelerometerCollectorService().getOnNextAccelerometerBatch().unregisterObserver(_accelerometerCollectorObserver);

    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_recording) {
      Intent intent = new Intent(this, AccelerometerDataRecordActivity.class);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  //endregion
}
