package edu.rit.gis.doctoreducator.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

import com.artifex.mupdfdemo.MuPDFCore;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * Provides search support for PDF files.
 * Create a PdfSearchProvider and send it a file. You can then perform searches of that file.
 */
public class PdfSearchProvider extends BaseSearchProvider {

    private MuPDFCore mPdfFile;
    private String mFilename;

    /**
     * Create a PdfSearchProvider with a new PDF file.
     *
     * @param context - application context
     * @param file - the PDF file to load
     * @throws IOException when MuPDF fails to load the file
     */
    public PdfSearchProvider(Context context, String file) throws IOException {
        mPdfFile = new MuPDFCore(context, file);
        mFilename = file;
    }

    @Override
    protected Collection<ISearchResult> performSearch(String text) {
        List<ISearchResult> resultSet = new LinkedList<>();
        for(int page = 0; page < mPdfFile.countPages(); page++) {
            RectF[] results = mPdfFile.searchPage(page, text);
            for(RectF rect : results) {
                PdfSearchResult search = new PdfSearchResult(page, rect);
                resultSet.add(search);
            }
        }
        return resultSet;
    }

    /**
     * Custom implementation of ISearchResult for PDFs.
     */
    protected class PdfSearchResult implements ISearchResult {

        private RectF mSearchArea;
        private int mPage;

        public PdfSearchResult(int page, RectF searchArea) {
            mPage = page;
            mSearchArea = searchArea;
        }

        @Override
        public void open(ITaskChanger parent) {
            // TODO I think I need to rework this...
        }

        @Override
        public View getView(Context context, Point size, View convertView) {
            ImageView view = null;
            if (convertView != null && convertView instanceof ImageView) {
                view = (ImageView) convertView;
            } else {
                view = new ImageView(context);
            }

            Rect viewArea = new Rect(0, 0, size.x, size.y);
            int left = (int)(mSearchArea.left - (size.x - mSearchArea.width()) / 2);
            int top = (int)(mSearchArea.top - (size.y - mSearchArea.height()) / 2);

            // make sure we aren't off the screen
            left = Math.max(left, 0);
            top = Math.max(top, 0);
            viewArea.offsetTo(left, top);

            // no idea what a Cookie is but apparently we need it
            MuPDFCore.Cookie cookie = mPdfFile.new Cookie();
            Bitmap preview = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
            mPdfFile.drawPage(preview, mPage, size.x, size.y, viewArea.left,
                    viewArea.top, viewArea.width(), viewArea.height(), cookie);
            cookie.destroy();

            view.setImageBitmap(preview);
            return view;
        }

        @Override
        public String getLocation() {
            return mFilename + " - Page " + mPage;
        }

        @Override
        public float getMatchPercent() {
            return 1.0f;
        }
    }
}
