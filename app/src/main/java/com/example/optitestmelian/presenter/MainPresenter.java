package com.example.optitestmelian.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.optitestmelian.model.Constants;
import com.example.optitestmelian.view.MainActivity;
import com.example.optitestmelian.model.RetrievePageTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainPresenter implements MainPresenterI {
    MainActivity mainActivity;
    private static final String WEBSITE_HASH_KEY = "WEBSITE_HASH_KEY";

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
            saveWebsiteHash(currentWebsiteHash);
        } else if (!savedWebsiteHash.equals(currentWebsiteHash)) {
            mainActivity.alertContentIsNotValid();
        }
    }

    private void saveWebsiteHash(String websiteHash) {
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

        return savedWebsiteHash;
    }
}
