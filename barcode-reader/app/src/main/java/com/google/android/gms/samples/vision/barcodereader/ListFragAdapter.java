package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by christinebaertl on 05.12.17.
 */

public class ListFragAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Code> codes;

    public ListFragAdapter(Context context, ArrayList<Code> codes) {
        this.context = context;
        this.codes = codes;
    }

    public void add(Code code){
        codes.add(code);
    }

    public double getPrice(int index) {
       return Double.parseDouble(codes.get(index).getPrice());
    }

    @Override
    public int getCount() {
        return codes.size();
    }

    @Override
    public Object getItem(int position) {
        return codes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.list_fragment, null);
        }

        TextView name = (TextView)convertView.findViewById(R.id.list_entry_title);
        TextView summary=(TextView)convertView.findViewById(R.id.list_entry_summary);

        name.setText(codes.get(position).getTitle());
        summary.setText(codes.get(position).getPrice());

        return convertView;
    }
}