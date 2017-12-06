package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christinebaertl on 05.12.17.
 */

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<Code> resultList = new ArrayList<Code>();

    public AutoCompleteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    public AutoCompleteAdapter getAutoCompleteAdapter(){
        return this;
    }

    public Code getCode(int index){
        return resultList.get(index);
    }

    public AutoCompleteAdapter getAutoAdapter(){
        return this;
    }

    @Override
    public Code getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position).getTitle());
        ((TextView) convertView.findViewById(R.id.text2)).setText(getItem(position).getPrice());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Code> codes = findBooks(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = codes;
                    filterResults.count = codes.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Code>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    // Here I retrieve stuff from the database
    private List<Code> findBooks(Context context, String bookTitle) {
        // The connection URL
        String url = "https://frysht0l68.execute-api.us-east-2.amazonaws.com/prod/get?barcode=" + bookTitle + "";

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        Log.d("reaching here", "reachin here");
        // Make the HTTP GET request, marshaling the response to a String
        String result = restTemplate.getForObject(url, String.class, "Android");

        Log.d("url", url);
        Log.d("RESULT", result);
        Code code = new Code(result);
        List<Code> codes = new ArrayList<>();
        codes.add(code);

        return codes;
    }
}