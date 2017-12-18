package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.graphics.Color;
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

    public ArrayList<Code> getCodes(){
        return this.codes;
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
                v.setBackgroundColor(Color.GREEN);
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


    /*
        Let me take you through the application
        The main activity, is composed of several functional UI elements.
        At the top of the screen, there is a search bar.
        Which when in focus, and typed into, triggers a background task
        that searches the database for items and suggests options for you to select.

        We can then tap on one the suggested items to add it to our shopping list
        and see more details about the selected item.

        For now we'll go with the first item.

        It'll also update the total price to the left side of the screen.




        One of the main features of our application is a barcode detector.
        This detector, once it detects a barcode, creates a bounding box around said barcode.

        This also triggers a background task that fetches information about the item on display.

        We can then tap on the box to get a popup display more information, and then decide wether
        we want to add it or not.




        So now you can check wether the item's price tag is actually accurate or not.



        The user can also see where the items in his shopping list are currently located
        in the store.

        A store map is displayed, and markers are added on the location of the items.
        Those markers can be tapped to see additional information.

        The map is dynamically updated upon addition and removal of other items.

    */

    @Override
    public void fillValues(int position, View convertView) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.list_fragment, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_entry_title);
        TextView price = (TextView) convertView.findViewById(R.id.list_entry_price);
        TextView description = (TextView) convertView.findViewById(R.id.list_entry_description);

        name.setText(codes.get(position).getTitle());
        price.setText(codes.get(position).getPrice() + "");
        description.setText(codes.get(position).getDescription());
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