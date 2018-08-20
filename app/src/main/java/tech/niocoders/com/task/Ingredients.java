package tech.niocoders.com.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredients {

@SerializedName("quantity")
@Expose
private double quantity;

@SerializedName("measure")
@Expose
private String measure;

@SerializedName("ingredient")
@Expose
private String  ingredient;

    //get quantity
    public double getQuantity() {
        return quantity;
    }

    //get measure
    public String getMeasure() {
        return measure;
    }

    //ingredients
    public String getIngredient() {
        return ingredient;
    }

    //setters

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
