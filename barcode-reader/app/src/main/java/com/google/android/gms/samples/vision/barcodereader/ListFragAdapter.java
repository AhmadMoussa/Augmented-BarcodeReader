package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

public class ListFragAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private ArrayList<Code> codes;
    private double total;

    public ListFragAdapter(Context mContext, ArrayList<Code> codes) {
        this.mContext = mContext;
        this.codes = codes;
        this.total = 0.0;
    }

    public void add(Code code) {
        codes.add(code);
        total += code.getPrice();
    }

    public void remove(int position) {
//        for(int i = 0; i< codes.size(); i++){
//            if(codes.get(i).getBarcode() == barcode){
//                codes.remove(i);
//                Log.d("INDEX","removed");
//            }
//        }
        total -= codes.get(position).getPrice();

        codes.remove(position);
        notifyDataSetChanged();

        Log.d("INDEX", "not removed");
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    //ATTENTION: Never bind listener or fill values in generateView.
    // You have to do that in fillValues method.
    @Override
    public View generateView(int position, ViewGroup parent) {
        final View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "Swipe left to delete", Toast.LENGTH_SHORT).show();
            }
        });

        final int pos = position;
        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String index = (String)((TextView) v.findViewById(R.id.list_entry_barcode)).getText();
//                Toast.makeText(mContext, index, Toast.LENGTH_SHORT).show();
                //((ViewGroup)view.getParent()).removeView(view);
                TextView tv = (TextView) v.getRootView().findViewById(R.id.bill_container);

                remove(pos);
                tv.setText(total + "");
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.list_fragment, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_entry_title);
        TextView barcode = (TextView) convertView.findViewById(R.id.list_entry_barcode);

        name.setText(codes.get(position).getTitle());
        barcode.setText(codes.get(position).getBarcode());
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
        return position;
    }

    public double getPrice(int index) {
        return codes.get(index).getPrice();
    }

    public double getTotal() {
        return this.total;
    }
}