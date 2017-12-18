package com.google.android.gms.samples.vision.barcodereader;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by christinebaertl on 05.12.17.
 */

public class Code implements Serializable{

    private int id;
    private String BarName;
    private double Price;
    private String Barcode;
    private String Description;
    private int quantity;
    private String X;
    private String Y;
    private boolean inCart = false;

    Code() {

    }

    Code(JSONObject obj) {
        try {
            setTitle(obj.getString("BarName"));
            setDescription(obj.getString("Description"));
            setBarcode(obj.getString("Barcode"));
            setPrice(Double.parseDouble(obj.getString("Price")));
            setX(obj.getString("X"));
            setY(obj.getString("Y"));
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
            setX(obj.getString("X"));
            setY(obj.getString("Y"));
        } catch (JSONException e) {
            Log.d("Exception", "couldn't set");
        }
    }

    public String getAsJSON(){
        return "{BarName:" + this.BarName + "}";
    }

    private void setTitle(String title) {
        this.BarName = title;
    }

    protected String getTitle() {
        return this.BarName;
    }

    protected void setPrice(double price) {
        this.Price = price;
    }

    protected double getPrice() {
        return this.Price;
    }

    private void setQuantity(int qt) {
        this.quantity = qt;
    }

    protected int getQuantity() {
        return this.quantity;
    }

    private void setBarcode(String barcode) {
        this.Barcode = barcode;
    }

    protected String getBarcode() {
        return this.Barcode;
    }

    private void setDescription(String description) {
        this.Description = description;
    }

    protected String getDescription() {
        return this.Description;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected int getId() {
        return this.id;
    }


    protected void setX(String x) {
        this.X = x;
    }

    protected String getX() {
        return this.X;
    }

    protected void setY(String y) {
        this.Y = y;
    }

    protected String getY() {
        return this.Y;
    }

    protected boolean getInCart(){
        return this.inCart;
    }

    protected void setInCart(boolean inCart){
            this.inCart = inCart;
    }
}
