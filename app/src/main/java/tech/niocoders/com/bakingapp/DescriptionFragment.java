package tech.niocoders.com.bakingapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class DescriptionFragment extends Fragment {
    private String TAG= DescriptionFragment.class.getSimpleName();
    private String SHORT_DESC = "SHORT_DESC";
    private String FULL_DESC = "FULL_DESC";
    private String short_desc;
    private String full_Desc;

    //MUST BE EMPTY SO THE FRAGMENT MANAGER CAN INSTANTIATE THE FRAGMENT CLASS
    public DescriptionFragment(){}

    public void setShortAndFullDescription(String shortDesc,String fullDesc)
    {
        this.short_desc = shortDesc;
        this.full_Desc  = fullDesc;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if(savedInstanceState!=null)
        {
            short_desc =  savedInstanceState.getString(SHORT_DESC);
            full_Desc =  savedInstanceState.getString(FULL_DESC);

        }
        View view =  inflater.inflate(R.layout.fragments_description_type,container,false);
        RelativeLayout descriptions = view.findViewById(R.id.description_part);
        DescriptionFragmentView descriptionView  = new DescriptionFragmentView(container.getContext());
        descriptionView.initView(short_desc,full_Desc);
        descriptions.addView(descriptionView);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SHORT_DESC,short_desc);
        outState.putString(FULL_DESC,full_Desc);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null) {
            short_desc = savedInstanceState.getString(SHORT_DESC);
            full_Desc = savedInstanceState.getString(FULL_DESC);
        }
    }
}
