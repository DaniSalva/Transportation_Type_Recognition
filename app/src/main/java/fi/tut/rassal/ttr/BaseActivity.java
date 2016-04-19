package fi.tut.rassal.ttr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
  private Toolbar _toolbar;
  //region Properties

  public TTRApp getTTRApp() {
    return (TTRApp) getApplication();
  }

  //endregion

  //region Activity overrides

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

//    setupToolbar();
  }


  //endregion

  //region Methods

//  protected final Toolbar setupToolbar() {
//    _toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//    if (_toolbar != null) {
//      setSupportActionBar(_toolbar);
//    }
//
//    return _toolbar;
//  }

  //endregion
}
