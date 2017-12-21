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
 * Created by Ahmad Moussa on 17.12.17.
 */

public class StoreMap extends Activity {

    ArrayList<Code> codes = new ArrayList<Code>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_store_map);

        codes = (ArrayList<Code>) getIntent().getSerializableExtra("CodesArrayList");

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.map);

        //loop over the array of codes that has been passed as intent from the main view
        //each iteration will create a clickable circle to display an item on the map
        for (int i = 0; i < codes.size(); i++) {
            addItem(rl, codes.get(i));
        }
    }

    private void addItem(RelativeLayout rl, Code code) {

        //creating a new image View and encapsulating it in a Relative Layout
        ImageView iv;
        RelativeLayout.LayoutParams params;
        iv = new ImageView(this);

        //Adding the drawable that is the image
        iv.setBackground(getDrawable(R.drawable.ic_add_circle_outline_black_24px));
        params = new RelativeLayout.LayoutParams(90, 90);

        //Get the X and Y coordinates of the item from the code object to set 
        //the top and left margin such as to locate them correctly on the map
        params.leftMargin = Integer.parseInt(code.getX());
        params.topMargin = Integer.parseInt(code.getY());

        //making a copy of the code object to be able to set an OnClickListener for the imageView
        //which will show a popup once the circle is pressed
        final Code finalCode = new Code(code);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, 0, 0, finalCode);
            }
        });

        //attaching the image view to the parent layout
        rl.addView(iv, params);
    }

    //function that is triggered from the onclick listener on each imageview
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
