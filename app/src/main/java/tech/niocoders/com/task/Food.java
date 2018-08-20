package tech.niocoders.com.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*author luis manon*/
public class Food{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredients> ingredients;

    @SerializedName("steps")
    @Expose
    private List<Steps> steps;

    @SerializedName("servings")
    @Expose
    private int servings;

    @SerializedName("image")
    @Expose
    private String image;

    //setters
    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    //getters

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public int getId() {
        return id;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
