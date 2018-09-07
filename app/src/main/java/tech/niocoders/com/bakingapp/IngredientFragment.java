package tech.niocoders.com.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import tech.niocoders.com.task.Ingredients;

public class IngredientFragment extends Fragment{
    private ArrayList<Ingredients> ingridients;
    private String TAG= IngredientFragment.class.getSimpleName();
    private String INGREDIENTS_TAG = "INGREDIENTS_TAG";

    //MUST BE EMPTY SO THE FRAGMENT MANAGER CAN INSTANTIATE THE FRAGMENT CLASS
   public IngredientFragment(){}

    public void setIngridients(ArrayList<Ingredients> ingridients) {
        this.ingridients = ingridients;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if(savedInstanceState!=null)
        {
            ingridients =  savedInstanceState.getParcelableArrayList(INGREDIENTS_TAG);
        }
        View view =  inflater.inflate(R.layout.fragments_ingredient_part,container,false);
        GridView ingredientsRecycle = view.findViewById(R.id.ingredients_part_fragment);
        IngredientFragmentView fragmentAdapter =  new IngredientFragmentView(getContext(),ingridients);
        ingredientsRecycle.setAdapter(fragmentAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(INGREDIENTS_TAG,ingridients);
        super.onSaveInstanceState(outState);
    }
}
