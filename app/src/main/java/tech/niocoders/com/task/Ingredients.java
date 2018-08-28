package tech.niocoders.com.task;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredients implements Parcelable {

@SerializedName("quantity")
@Expose
private double quantity;

@SerializedName("measure")
@Expose
private String measure;

@SerializedName("ingredient")
@Expose
private String  ingredient;


    public Ingredients(String measure, String ingredient, double quantity)
    {
        this.measure =  measure;
        this.ingredient =  ingredient;
        this.quantity =  quantity;
    }
    public Ingredients(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }


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


    public static final Parcelable.Creator<Ingredients> CREATOR = new Parcelable.Creator<Ingredients>()
    {
        public Ingredients createFromParcel(Parcel in)
        {
            return new Ingredients(in);
        }
        public Ingredients[] newArray(int size)
        {
            return new Ingredients[size];
        }
    };
}
