package org.softeg.slartus.forpdaplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.softeg.slartus.forpdaapi.search.SearchSettings;
import org.softeg.slartus.forpdacommon.ExtPreferences;
import org.softeg.slartus.forpdaplus.fragments.search.SearchSettingsDialogFragment;

/**
 * Created by radiationx on 24.10.15.
 */
public class FragmentActivity extends AppCompatActivity
        implements SearchSettingsDialogFragment.ISearchDialogListener {
    public static final String SENDER_ACTIVITY = "sender_activity";
    public static final String FORCE_EXIT_APPLICATION = "org.softeg.slartus.forpdaplus.FORCE_EXIT_APPLICATION";
    public LinearLayout statusBar;
    public boolean statusBarShowed = false;
    public boolean hack = false;

    protected void afterCreate() {

    }

    public Context getContext() {
        return this;
    }

    /*public ActionBar getSupportActionBar() {
        return getSupportActionBar();
    }
    public void setSupportProgressBarIndeterminateVisibility(boolean b) {
        setSupportProgressBarIndeterminateVisibility(b);
    }
    public void setSupportProgressBarIndeterminate(boolean b) {
        setSupportProgressBarIndeterminate(b);
    }*/

    @Override
    public void startActivity(android.content.Intent intent) {
        Log.e("kek", "startactivity");
        intent.putExtra(SENDER_ACTIVITY, getClass().toString());
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(android.content.Intent intent, int requestCode) {
        Log.e("kek", "startactivityforresult");
        intent.putExtra(SENDER_ACTIVITY, getClass().toString());
        super.startActivityForResult(intent, requestCode);
        hack = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("kek", "onactivity result");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        Log.e("kek", "activity reent");
        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfForceKill();
    }

    private void checkIfForceKill() {
        //CHECK IF I NEED TO KILL THE APP
        // Restore preferences
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean forceKill = settings.getBoolean(
                FORCE_EXIT_APPLICATION, false);

        if (forceKill) {
            //CLEAR THE FORCE_EXIT SETTINGS
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(FORCE_EXIT_APPLICATION, false);
            // Commit the edits!
            editor.apply();
            //HERE STOP ALL YOUR SERVICES
            finish();
        }
    }

    protected Bundle args = new Bundle();

    @Override
    public void doSearchDialogPositiveClick(SearchSettings searchSettings) {
        MainActivity.startForumSearch(searchSettings);
    }

    @Override
    public void doSearchDialogNegativeClick() {

    }

    protected boolean isTransluent() {
        return true;
    }

    @Override
    protected void onCreate(Bundle saveInstance) {
        setTheme(isTransluent() ? App.getInstance().getTransluentThemeStyleResID() : App.getInstance().getThemeStyleResID());
        super.onCreate(saveInstance);
        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (PreferenceManager.getDefaultSharedPreferences(App.getInstance()).getBoolean("coloredNavBar", true) &&
                android.os.Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(App.getInstance().getResources().getColor(getNavBarColor()));
/*
        if(PreferenceManager.getDefaultSharedPreferences(App.getContext()).getBoolean("statusbarTransparent",false)) {
            if (android.os.Build.VERSION.SDK_INT >= 21)
                getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            if (android.os.Build.VERSION.SDK_INT > 18) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout statusBarLay = (LinearLayout) inflater.inflate(R.layout.statusbar, null);
                statusBar = (LinearLayout) statusBarLay.findViewById(R.id.statusBar);
                statusBar.setMinimumHeight(getStatusBarHeight());
                if (App.getInstance().getCurrentThemeName().equals("white")) {
                    statusBar.setBackgroundColor(getResources().getColor(R.color.statusBar_wh));
                } else if (App.getInstance().getCurrentThemeName().equals("black")) {
                    statusBar.setBackgroundColor(getResources().getColor(R.color.statusBar_bl));
                }
                ViewGroup decor = (ViewGroup) getWindow().getDecorView();
                decor.addView(statusBarLay);
                statusBarShowed = true;
            }
        }
*/
        args.clear();
        if (getIntent().getExtras() != null) {
            args.putAll(getIntent().getExtras());
        }
        if (saveInstance != null) {
            args.putAll(saveInstance);
        }

        afterCreate();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadPreferences(prefs);
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public int getNavBarColor(){
        if(App.getInstance().isWhiteTheme())
            return R.color.actionbar_background_wh;
        else
            return R.color.actionbar_background_bl;
    }
    @Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        Log.e("kek", "activity onsave");
        if (args != null)
            outState.putAll(args);
        super.onSaveInstanceState(outState);
        if(hack){
            onStop();
            onStart();
        }
        hack= false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("kek", "activity onstart");
    }

    @Override
    protected void onRestoreInstanceState(android.os.Bundle outState) {
        Log.e("kek", "activity onrestore");
        args = outState;
        super.onRestoreInstanceState(outState);
    }


    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @SuppressWarnings("ResourceType")
    protected void loadPreferences(SharedPreferences prefs) {
        setRequestedOrientation(ExtPreferences.parseInt(prefs, "theme.ScreenOrientation", -1));
    }


}
