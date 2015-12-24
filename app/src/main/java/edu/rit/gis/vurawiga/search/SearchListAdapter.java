package edu.rit.gis.vurawiga.search;

import android.database.DataSetObserver;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements the ListAdapter interface using a collection of {@code ISearchResult} objects.
 */
public class SearchListAdapter implements ListAdapter {

    private volatile List<ISearchResult> mResults;
    private List<DataSetObserver> mObservers;

    public SearchListAdapter() {
        mResults = new ArrayList<>();
        mObservers = new LinkedList<>();
    }

    public SearchListAdapter(Collection<ISearchResult> data) {
        // we need an actual list so we copy the data
        mResults = new ArrayList<>(data);
        mObservers = new LinkedList<>();
    }

    public void addAll(final Collection<ISearchResult> data) {
        mResults.addAll(data);
        callChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Object getItem(int position) {
        return mResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // try to determine size of view. guessing larger is better
        Point size;
        View child = null;
        if (convertView != null) {
            child = convertView;
        } else if (parent.getFocusedChild() != null) {
            child = parent.getFocusedChild();
        }

        if (child != null) {
            size = new Point(child.getWidth(), child.getHeight());
        } else {
            size = new Point(parent.getWidth(), parent.getHeight() / 4);
        }

        return mResults.get(position).getView(parent.getContext(), size, convertView);
    }

    @Override
    public int getItemViewType(int position) {
        // I'd really like to implement this but not now
        return ListAdapter.IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mResults.isEmpty();
    }

    private void callChanged() {
        for (DataSetObserver dso : mObservers) {
            dso.onChanged();
        }
    }
}
