package com.olympics.olympicsandroid.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.olympics.olympicsandroid.OlympicsApplication;

/**
 * Created by tkmagz4 on 7/14/16.
 */
public class OlympicsAnalyticsHelper {

    public static void capturePageSelection(String pageName) {

        Tracker tracker = OlympicsApplication.getInstance().getDefaultTracker();
        if (tracker != null) {
            tracker.setScreenName("OlympicsPage-" + pageName);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public static void captureAction (String actionName) {

        Tracker tracker = OlympicsApplication.getInstance().getDefaultTracker();
        if (tracker != null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction(actionName)
                    .build());
        }
    }
}
