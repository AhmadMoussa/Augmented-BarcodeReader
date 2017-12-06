package com.google.android.gms.samples.vision.barcodereader;

/**
 * Created by christinebaertl on 05.12.17.
 */

public class Code {

    private String title;
    private String price;
    private int quantity;

    Code(String title, String price){
        setPrice(price);
        setTitle(title);
    }

    Code(String price){
        setPrice(price);
        setTitle("Someitem");
        setQuantity(1);
    }

    private void setTitle(String title){
        this.title = title;
    }

    protected String getTitle(){
        return this.title;
    }

    protected void setPrice(String price){
        this.price = price;
    }

    protected String getPrice(){
        return this.price;
    }

    private void setQuantity(int qt){
        this.quantity = qt;
    }

    protected int getQuantity(){
        return this.quantity;
    }
}
