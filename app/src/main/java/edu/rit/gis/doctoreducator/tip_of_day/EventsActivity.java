package edu.rit.gis.doctoreducator.tip_of_day;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by siddeshpillai on 10/26/15.
 */
public class EventsActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_of_the_day_webview);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.medical-events.com/index.php");
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
