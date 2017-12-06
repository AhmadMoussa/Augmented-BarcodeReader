package com.google.android.gms.samples.vision.barcodereader;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by christinebaertl on 26.11.17.
 */

public class ListFrag extends ListFragment {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    List<Code> codes = new ArrayList<Code>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ListFragAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new ListFragAdapter(getActivity(), (ArrayList<Code>) codes);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(Code code) {
        System.out.println("hi");
        adapter.notifyDataSetChanged();
        adapter.add(code);
    }

    public ListFragAdapter getAdapter(){
        return this.adapter;
    }
}
