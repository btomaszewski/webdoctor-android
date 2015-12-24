package edu.rit.gis.vurawiga.tip_of_day;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;

import edu.rit.gis.vurawiga.R;

/**
 * Created by siddeshpillai on 10/27/15.
 */
public class Medsearch extends Activity {

    private WebView webView;
    private String searchURL = "http://search.medscape.com/search?q=";
    private ImageButton button;
    private EditText searchText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medscape_layout);

        button = (ImageButton) findViewById(R.id.medscapeButton);
        searchText = (EditText) findViewById(R.id.searchTextMedscape);
        webView = (WebView) findViewById(R.id.searchMedscape);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.setInitialScale(1);
                webView.getSettings().setJavaScriptEnabled(true);
//                webView.setWebViewClient(new MyBrowser());
                webView.setWebChromeClient(new WebChromeClient());
                String url = searchURL + searchText.getText().toString();
                webView.loadUrl(url);
            }
        });

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
