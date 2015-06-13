package andresaraujo.github.io.designlibraryexample;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by andres on 6/13/15.
 */
public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
