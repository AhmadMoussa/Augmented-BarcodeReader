package com.google.android.gms.samples.vision.barcodereader;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by christinebaertl on 05.12.17.
 */

public class Code {

    private int id;
    private String title;
    private double price;
    private String barcode;
    private String description;
    private int quantity;

    Code() {

    }

    Code(JSONObject obj) {
        try {
            setTitle(obj.getString("BarName"));
            setDescription(obj.getString("Description"));
            setBarcode(obj.getString("Barcode"));
            setPrice(Double.parseDouble(obj.getString("Price")));
        } catch (JSONException e) {
            Log.d("Exception", "couldn't set");
        }

    }

    Code(String title, double price, String barcode, String description) {
        setPrice(price);
        setTitle(title);
    }

    Code(double price) {
        setPrice(price);
        setTitle("Someitem");
        setQuantity(1);
    }

    Code(Code code){
        setTitle(code.getTitle());
        setDescription(code.getDescription());
        setBarcode(code.getBarcode());
        setPrice(code.getPrice());
    }

    public void setFromJSON(JSONObject obj) {
        try {
            setTitle(obj.getString("BarName"));
            setDescription(obj.getString("Description"));
            setBarcode(obj.getString("Barcode"));
            setPrice(Double.parseDouble(obj.getString("Price")));
        } catch (JSONException e) {
            Log.d("Exception", "couldn't set");
        }
    }

    private void setTitle(String title) {
        this.title = title;
    }

    protected String getTitle() {
        return this.title;
    }

    protected void setPrice(double price) {
        this.price = price;
    }

    protected double getPrice() {
        return this.price;
    }

    private void setQuantity(int qt) {
        this.quantity = qt;
    }

    protected int getQuantity() {
        return this.quantity;
    }

    private void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    protected String getBarcode() {
        return this.barcode;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    protected String getDescription() {
        return this.description;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected int getId() {
        return this.id;
    }
}
