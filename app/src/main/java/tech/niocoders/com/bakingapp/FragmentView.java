package tech.niocoders.com.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tech.niocoders.com.task.Ingredients;

public class FragmentView extends BaseAdapter{
    private int index;
    private Context context;
    private ArrayList<Ingredients>data;
    public FragmentView(Context context, ArrayList<Ingredients>data)
    {
        this.context =context;
        this.data=data;
        index = 0;
    }

    @Override
    public int getCount() {
        return  data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
       //currently the view holds data from my previous fragment but I want to inflate a new layout and return a new view
        View newView =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_view,viewGroup,false);

        TextView stepN =  newView.findViewById(R.id.tutorial_step);
        TextView IngredientText =  newView.findViewById(R.id.Ingredient_text);
        TextView Measure = newView.findViewById(R.id.measure);
        TextView quantity =  newView.findViewById(R.id.quantity);


        Ingredients ingredientData =  data.get(position);
        stepN.setText("("+position+")");
        IngredientText.setText(ingredientData.getIngredient());
        Measure.setText(ingredientData.getMeasure());
        quantity.setText(""+ingredientData.getQuantity());

        return newView;

    }
}
