package cn.panyunyi.healthylife.app.server.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.panyunyi.healthylife.app.server.R;

/**
 * Created by panyu on 2018/2/10.
 */

public class MainListAdapter extends BaseAdapter{

    private Context mContext;


    public MainListAdapter(Context context){
        mContext=context;
    }


        @Override
        public boolean areAllItemsEnabled() {
        return false;
    }

        @Override
        public boolean isEnabled(int i) {
        return false;
    }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

        @Override
        public int getCount() {
        return 4;
    }

        @Override
        public Object getItem(int i) {
        return null;
    }

        @Override
        public long getItemId(int i) {
        return 0;
    }

        @Override
        public boolean hasStableIds() {
        return false;
    }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
        int s[]={R.string.click_to_find_more_interesting_things,R.string.heart_rate,R.string.blood_pressure,R.string.blood_oxygen};
        int d[]={R.drawable.lamp,R.drawable.heart,R.drawable.heart_pressure,R.drawable.oxygen};
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.main_page_list_item,null);
        TextView textView=(TextView) view.findViewById(R.id.main_list_item_text);
        textView.setText(mContext.getText(s[i]));
        textView.setTextSize(20);
        Drawable drawable=mContext.getDrawable(d[i]);
        drawable.setBounds(20,0,60,40);
        textView.setCompoundDrawables(drawable,null,null,null);

        return view;
    }

        @Override
        public int getItemViewType(int i) {
        return 0;
    }

        @Override
        public int getViewTypeCount() {
        return 4;
    }

        @Override
        public boolean isEmpty() {
        return false;
    }



    }

