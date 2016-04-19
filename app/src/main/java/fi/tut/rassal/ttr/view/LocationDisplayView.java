package fi.tut.rassal.ttr.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import fi.tut.rassal.ttr.R;
import fi.tut.rassal.ttr.gps.LocationInfo;

public class LocationDisplayView extends LinearLayout {
  //region fields

  @InjectView(R.id.view_location_gauge) SpeedometerGauge _speedometer;
  @InjectView(R.id.view_location_title) TextView _titleTextView;
  private LocationInfo _actualLocation;

  //endregion

  //region Constructors

  public LocationDisplayView(Context context) {
    this(context, null);
  }

  public LocationDisplayView(Context context, AttributeSet attrs) {
    super(context, attrs);

    init(context, attrs);
  }

  public LocationDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public LocationDisplayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    init(context, attrs);
  }

  //endregion

  //region methods

  private void init(Context context, AttributeSet attrs) {
    setOrientation(LinearLayout.VERTICAL);
    setGravity(Gravity.CENTER_VERTICAL);


    LayoutInflater.from(getContext()).inflate(R.layout.view_location, this);
    ButterKnife.inject(this, this);

    _speedometer.setMaxSpeed(50);
    _speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
      @Override
      public String getLabelFor(double progress, double maxProgress) {
        return String.valueOf((int) Math.round(progress));
      }
    });
    _speedometer.setMaxSpeed(30);
    _speedometer.setMajorTickStep(10);
    _speedometer.setMinorTicks(2);
    _speedometer.addColoredRange(0, 2, Color.GREEN);
    _speedometer.addColoredRange(2, 12, Color.YELLOW);
    _speedometer.addColoredRange(12, 30, Color.RED);
    //if (_actualLocation!=null)
    //  _speedometer.setSpeed(_actualLocation.getSpeed());
  }

  public void showData(LocationInfo info) {
    _speedometer.setSpeed(info.getSpeed());
  }

  //endregion
}
