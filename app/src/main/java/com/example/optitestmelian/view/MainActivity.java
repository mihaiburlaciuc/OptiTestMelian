package com.example.optitestmelian.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.optitestmelian.R;
import com.example.optitestmelian.model.Constants;
import com.example.optitestmelian.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {
    private MainPresenter mainPresenter;
    private WebView webView;
    private Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter(this);
        mainPresenter.checkWebsiteData();

        initWebView();
        initDialog();
    }

    public void initWebView() {
        webView = findViewById(R.id.webView);
        webView.loadUrl(Constants.URL);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
    }

    private void initDialog() {
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.alert_dialog_text));

        alertDialog = new Dialog(this);
        alertDialog.setContentView(textView);
    }

    public void alertContentIsNotValid() {
        alertDialog.show();
    }
}