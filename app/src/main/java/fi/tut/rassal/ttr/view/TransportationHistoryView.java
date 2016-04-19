package fi.tut.rassal.ttr.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fi.tut.rassal.ttr.R;
import fi.tut.rassal.ttr.common.TravelPart;

import java.util.Date;
import java.util.List;

public class TransportationHistoryView extends FrameLayout {
  //region Fields

  @InjectView(android.R.id.list) ListView _listView;
  @InjectView(android.R.id.empty) View _emptyView;

  //endregion

  //region Constructors

  public TransportationHistoryView(Context context) {
    super(context);
    init();
  }

  public TransportationHistoryView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TransportationHistoryView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public TransportationHistoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  //endregion

  //region Methods

  private void init(){
    LayoutInflater.from(getContext()).inflate(R.layout.view_transportation_history, this);
    ButterKnife.inject(this, this);

    _listView.setEmptyView(_emptyView);
  }

  public void showData(List<TravelPart> parts) {
    HistoryAdapter historyAdapter = new HistoryAdapter(getContext());
    historyAdapter.addAll(parts);

    _listView.setAdapter(historyAdapter);
  }

  //endregion

  //region nested classes

  static class HistoryAdapter extends ArrayAdapter<TravelPart> {
    public HistoryAdapter(Context context) {
      super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_travel_part, null);
        convertView.setTag(new RowHolder(convertView));
      }

      TravelPart travelPart = getItem(position);

      RowHolder holder = (RowHolder) convertView.getTag();

      holder._iconView.setImageResource(travelPart.getType()._iconRes);

      //TODO: format betetr time
      String timeText = new Date(travelPart.getBegin()) + " " + new Date(travelPart.getEnd());
      holder._text.setText(timeText);

      return convertView;
    }
  }

  static class RowHolder {
    @InjectView(android.R.id.icon) ImageView _iconView;
    @InjectView(android.R.id.text1) TextView _text;

    public RowHolder(View parent) {
      ButterKnife.inject(this, parent);
    }
  }

  //endregion
}
