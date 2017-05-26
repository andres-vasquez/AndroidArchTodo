package io.github.andres_vasquez.flatfoottodo.utils;

import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.ListView;

/**
 * Created by andresvasquez on 5/24/17.
 */

public class GlobalFunctions {

    /**
     * Fix Android 6 popup dialog bug
     * @param dialog
     */
    public static void fixDialogMarshmellow(AlertDialog dialog){
        if (Build.VERSION.SDK_INT >= 23) {
            ListView listView = dialog.getListView();
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    int size = view.getChildCount();
                    for (int i=0; i<size; i++) {
                        View v = view.getChildAt(i);
                        if (v instanceof CheckedTextView)
                            ((CheckedTextView)v).refreshDrawableState();
                    }
                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int size = view.getChildCount();
                    for (int i=0; i<size; i++) {
                        View v = view.getChildAt(i);
                        if (v instanceof CheckedTextView)
                            ((CheckedTextView)v).refreshDrawableState();
                    }
                }
            });
        }
    }
}
