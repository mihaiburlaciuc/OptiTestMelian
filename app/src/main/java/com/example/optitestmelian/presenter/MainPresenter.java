package com.example.optitestmelian.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.optitestmelian.model.Constants;
import com.example.optitestmelian.view.MainActivity;
import com.example.optitestmelian.model.RetrievePageTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainPresenter implements MainPresenterI {
    MainActivity mainActivity;
    private static final String WEBSITE_HASH_KEY = "WEBSITE_HASH_KEY";
    private static final String MAIN_PRESENTER_TAG = "MAIN_PRESENTER_TAG";

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean checkWebsiteData() {
        new RetrievePageTask(this).execute(Constants.URL);
        return false;
    }

    private String createHash(String s) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(s.getBytes());
            String stringHash = new String(messageDigest.digest());

            Log.d("TAG", stringHash);

            return stringHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void receiveWebsiteResponse(String response) {
        String currentWebsiteHash = createHash(response);

        String savedWebsiteHash = getSavedWebsiteHash();
        if (savedWebsiteHash == null) {
            // Saving website hash for the first time
            Log.d(MAIN_PRESENTER_TAG, "savedWebsiteHash == null");
            saveWebsiteHash(currentWebsiteHash);
        } else if (!savedWebsiteHash.equals(currentWebsiteHash)) {
            Log.d(MAIN_PRESENTER_TAG, "savedWebsiteHash != currentWebsiteHash");
            mainActivity.alertContentIsNotValid();
        } else {
            Log.d(MAIN_PRESENTER_TAG, "savedWebsiteHash == currentWebsiteHash");
        }
    }

    private void saveWebsiteHash(String websiteHash) {
        Log.d(MAIN_PRESENTER_TAG, "saveWebSiteHash: "  + websiteHash);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mainActivity.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(WEBSITE_HASH_KEY, websiteHash);
        editor.apply();
    }

    private String getSavedWebsiteHash() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mainActivity.getApplicationContext());
        String savedWebsiteHash = preferences.getString(WEBSITE_HASH_KEY, null);
        Log.d(MAIN_PRESENTER_TAG, "getSavedWebsiteHash: "  + savedWebsiteHash);

        return savedWebsiteHash;
    }
}
