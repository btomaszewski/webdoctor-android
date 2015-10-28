package edu.rit.gis.doctoreducator.tip_of_day;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.rit.gis.doctoreducator.R;
import edu.rit.gis.doctoreducator.main.MainActivity;

/**
 * Created by siddeshpillai on 10/26/15.
 */
public class TipOfTheDay extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_of_the_day_webview);

        webView = (WebView) findViewById(R.id.webView);
//        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.nejm.org/toc/nejm/medical-journal");
        webView.setWebViewClient(new MyBrowser());
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
