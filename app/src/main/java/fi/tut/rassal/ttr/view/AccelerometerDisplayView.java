package fi.tut.rassal.ttr.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fi.tut.rassal.ttr.R;
import fi.tut.rassal.ttr.accelerometer.AccelerometerInfo;

public class AccelerometerDisplayView extends LinearLayout {
  //region Fields

  @InjectView(R.id.accelerometer_x) TextView _xText;
  @InjectView(R.id.accelerometer_y) TextView _yText;
  @InjectView(R.id.accelerometer_z) TextView _zText;

  float x;
  float y;
  float z;

  //endregion

  //region Constructors

  public AccelerometerDisplayView(Context context) {
    super(context);
    init();
  }

  public AccelerometerDisplayView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public AccelerometerDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public AccelerometerDisplayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  //endregion

  //region Methods

  private void init() {
    setOrientation(VERTICAL);

    LayoutInflater.from(getContext()).inflate(R.layout.view_accelerometer, this);

    ButterKnife.inject(this, this);
  }

  public void showData(AccelerometerInfo info) {

    //TODO: remove the maximum values
    if(info._x > x)
      x = info._x;

    if(info._y > y)
      y = info._y;

    if(info._z > z)
      z = info._z;

    _xText.setText(" X-> "+String.valueOf(info._x) + "\t Highest: " + x);
    _yText.setText(" Y-> "+String.valueOf(info._y)+ "\t Highest: " + y);
    _zText.setText(" Z-> "+String.valueOf(info._z)+ "\t Highest: " + z);
  }

  //endregion


}
