package tech.niocoders.com.bakingapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DescriptionFragmentView extends LinearLayout{

    private Context context;
    private String short_desc;
    private String full_desc;

    public DescriptionFragmentView(Context context) {
        super(context);
        this.context= context;

    }


    public void initView(String short_desc,String full_desc)
    {
        this.short_desc = short_desc; this.full_desc =  full_desc;
        View newView =  LayoutInflater.from(context).inflate(R.layout.steps_list_view,this,false);

        TextView shortDesc =  newView.findViewById(R.id.short_description);
        TextView fullDesc =  newView.findViewById(R.id.description);
        shortDesc.setText(short_desc);
        fullDesc.setText(full_desc);
        addView(newView);
    }



}
