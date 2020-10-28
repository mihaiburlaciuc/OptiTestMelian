package com.example.optitestmelian.model;

import android.os.AsyncTask;

import com.example.optitestmelian.presenter.MainPresenterI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class RetrievePageTask extends AsyncTask<String, Void, String> {
    private MainPresenterI mainPresenter;

    public RetrievePageTask(MainPresenterI mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String result = getHTML(strings[0]);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getHTML(String s) throws IOException {
        URL url = new URL(s);
        URLConnection con = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuilder builder = new StringBuilder();

        while ((line=in.readLine())!=null) {
            builder.append(line);
        }

        return builder.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        mainPresenter.receiveWebsiteResponse(s);
    }
}
