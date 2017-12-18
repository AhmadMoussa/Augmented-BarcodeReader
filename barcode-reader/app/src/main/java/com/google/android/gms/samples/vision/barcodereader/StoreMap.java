package com.google.android.gms.samples.vision.barcodereader;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

/**
 * Created by christinebaertl on 17.12.17.
 */

public class StoreMap extends Activity {

    ArrayList<Code> codes = new ArrayList<Code>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_store_map);

        codes = (ArrayList<Code>) getIntent().getSerializableExtra("CodesArrayList");

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.map);

        if(codes.size() == 1){
            addItem1(rl, codes.get(0));
        }else if(codes.size() == 2){
            addItem1(rl, codes.get(0));
            addItem2(rl, codes.get(1));
        }else if(codes.size() == 3){
            addItem1(rl, codes.get(0));
            addItem2(rl, codes.get(1));
            addItem3(rl, codes.get(2));
        }
    }

    private void addItem1(RelativeLayout rl, Code code){
        ImageView iv;
        RelativeLayout.LayoutParams params;
        iv = new ImageView(this);
        iv.setBackground(getDrawable(R.drawable.ic_add_circle_outline_black_24px));
        params = new RelativeLayout.LayoutParams(90, 90);

        //System.out.println(codes.get(i).getX() + "");
        //int x = Integer.parseInt(codes.get(i).getX());
        params.leftMargin = 280;

        //System.out.println(codes.get(i).getX() + "");
        params.topMargin = 400;

        final Code finalCode = new Code(code);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, 0, 0, finalCode);
            }
        });
        rl.addView(iv, params);
    }

    private void addItem2(RelativeLayout rl, Code code){
        ImageView iv;
        RelativeLayout.LayoutParams params;
        iv = new ImageView(this);
        iv.setBackground(getDrawable(R.drawable.ic_add_circle_outline_black_24px));
        params = new RelativeLayout.LayoutParams(90, 90);

        //System.out.println(codes.get(i).getX() + "");
        //int x = Integer.parseInt(codes.get(i).getX());
        params.leftMargin = 730;

        //System.out.println(codes.get(i).getX() + "");
        params.topMargin = 100;

        final Code finalCode = new Code(code);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, 0, 0, finalCode);
            }
        });
        rl.addView(iv, params);
    }

    //When you scan a barcode that is already in your cart,
    //It will automatically color the list entry green,
    //Such as to tell you that the item has been added to your cart

    private void addItem3(RelativeLayout rl, Code code){
        ImageView iv;
        RelativeLayout.LayoutParams params;
        iv = new ImageView(this);
        iv.setBackground(getDrawable(R.drawable.ic_add_circle_outline_black_24px));
        params = new RelativeLayout.LayoutParams(90, 90);

        //System.out.println(codes.get(i).getX() + "");
        //int x = Integer.parseInt(codes.get(i).getX());
        params.leftMargin = 790;

        //System.out.println(codes.get(i).getX() + "");
        params.topMargin = 500;

        final Code finalCode = new Code(code);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, 0, 0, finalCode);
            }
        });
        rl.addView(iv, params);
    }

    // The app can also help the user find a route to the store.

    // The map will display markers on the location of the currently supported stores.

    // Once the user clicks on one of those markers it will draw the shortest route towards that store.

    // noticing that the displayed routes may look wonky at first, the function takes into
    // consideration the direction of trafic

    // We only support a few stores currently, but more will be added in the future.

    public void showPopup(View anchorView, float offsetX, float offsetY, Code code) {

        View popupView = getLayoutInflater().inflate(R.layout.map_popup, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        ((TextView) popupView.findViewById(R.id.item_name)).setText(code.getTitle() + "");
        ((TextView) popupView.findViewById(R.id.item_description)).setText(code.getDescription() + "");

        final Code bestCode = code;

//        //get the button and set the onclick listener that will add the item to the list
//        popupView.findViewById(R.id.add_button)
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
        // Initialize more widgets from `popup_layout.xml`

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        //popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,location[0], location[1] + anchorView.getHeight());

        popupWindow.showAsDropDown(anchorView, (int) offsetX, (int) offsetY, Gravity.NO_GRAVITY);
    }
}
