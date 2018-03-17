package shishkin.cleanarchitecture.note.screen.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.cleanarchitecture.sl.ui.activity.AbsContentActivity;


import shishkin.cleanarchitecture.note.R;

public class MainActivity extends AbsContentActivity<MainModel> {

    public static final String NAME = MainActivity.class.getName();
    private Intent mIntent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setModel(new MainModel(this));

        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mIntent = intent;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mIntent != null) {
            final String action = mIntent.getAction();
            if ("android.intent.action.MAIN".equalsIgnoreCase(action)) {
                getModel().getRouter().showMainFragment();
            }
        } else {
            getModel().getRouter().showMainFragment();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getContentResId() {
        return R.id.content;
    }
}
