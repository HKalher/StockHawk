package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by Henu on 15/08/16.
 */
public class StockWidgetService extends RemoteViewsService {

//    public StockWidgetService() {
//        super();
//    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        return super.onBind(intent);
//    }

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        RemoteViewsFactory remoteViewsFactory = new RemoteViewsFactory() {
            private Cursor stockDataCursor = null;
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if(stockDataCursor != null){
                    stockDataCursor.close();
                }
                final long token = Binder.clearCallingIdentity();
                String queryString[] = { QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE, QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP};
                stockDataCursor = getContentResolver().query(
                        QuoteProvider.Quotes.CONTENT_URI, queryString, QuoteColumns.ISCURRENT + " = ?", new String []{"1"}, null);
                Binder.restoreCallingIdentity(token);
            }

            @Override
            public void onDestroy() {
                if(stockDataCursor != null){
                    stockDataCursor.close();
                    stockDataCursor = null;
                }
            }

            @Override
            public int getCount() {
                if(stockDataCursor == null){
                    return 0;
                }else {
                    return stockDataCursor.getCount();
                }
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if(position == AdapterView.INVALID_POSITION || !stockDataCursor.moveToPosition(position) || stockDataCursor == null){
                    return null;
                }
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.stock_widget_list_view);

                remoteViews.setTextViewText(R.id.stock_widget_list_symbol, stockDataCursor.getString(stockDataCursor.getColumnIndex("symbol")));
                remoteViews.setTextViewText(R.id.stock_widget_list_bid_price, stockDataCursor.getString(stockDataCursor.getColumnIndex("bid_price")));

                if(stockDataCursor.getInt(stockDataCursor.getColumnIndex(QuoteColumns.ISUP)) == 1){
                    remoteViews.setInt(R.id.stock_widget_list_change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                }else {
                    remoteViews.setInt(R.id.stock_widget_list_change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                }

                if(Utils.showPercent){
                    remoteViews.setTextViewText(R.id.stock_widget_list_change, stockDataCursor.getString(stockDataCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)));
                }else {
                    remoteViews.setTextViewText(R.id.stock_widget_list_change, stockDataCursor.getString(stockDataCursor.getColumnIndex(QuoteColumns.CHANGE)));
                }
                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if(stockDataCursor != null && stockDataCursor.moveToPosition(position) == true){
                    return stockDataCursor.getLong(0);
                }else {
                    return position;
                }
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
        return remoteViewsFactory;
    }
}
