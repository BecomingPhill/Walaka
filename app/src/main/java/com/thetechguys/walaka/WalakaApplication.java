package com.thetechguys.walaka;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.thetechguys.walaka.utils.ParseConstants;

/**
 * Created by TheBank on 17/9/14.
 */
public class WalakaApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "1dN9Q1HEzQ8ipOIWdJEEWM5sunt110UzBIXAYYHA", "TqSZ10cwDajxnl5Yknw2TQPvvY4HRETBpWx7CK4X");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);


    }



    public static void updateParseInstallation(ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }
}
