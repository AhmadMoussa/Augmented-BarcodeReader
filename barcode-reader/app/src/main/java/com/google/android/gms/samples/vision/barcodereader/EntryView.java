package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by christinebaertl on 16.12.17.
 */

public class EntryView extends ActionBarActivity implements View.OnClickListener{
    private Button chooseStore;
    private Button addItems;
    private Button toMap;
    private Button toStoreMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_view);

        chooseStore = (Button) findViewById(R.id.choose_store);
        chooseStore.setOnClickListener(this);

        addItems = (Button) findViewById(R.id.add_items);
        addItems.setOnClickListener(this);

        toMap = (Button) findViewById(R.id.map_to_store);
        toMap.setOnClickListener(this);

        toStoreMap = (Button) findViewById(R.id.map_in_store);
        toStoreMap.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.choose_store) {

        }

        if (v.getId() == R.id.add_items) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.map_to_store) {
            Intent intent = new Intent(this, MapLocation.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.map_in_store) {

        }
    }
}