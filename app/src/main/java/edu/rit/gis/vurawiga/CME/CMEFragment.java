package edu.rit.gis.vurawiga.CME;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.rit.gis.vurawiga.R;

/**
 * Created by siddeshpillai on 10/5/15.
 */
public class CMEFragment extends Fragment {

    private WebView webView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cme_layout, container, false);

        webView = (WebView) rootView.findViewById(R.id.cme_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl("http://www.nejm.org/continuing-medical-education");
        webView.setWebViewClient(new MyBrowser());
        return rootView;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
