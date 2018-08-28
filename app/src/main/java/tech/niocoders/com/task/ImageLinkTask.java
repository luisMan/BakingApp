package tech.niocoders.com.task;


import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;

import tech.niocoders.com.bakingapp.BakingAppUtilities;
import tech.niocoders.com.bakingapp.PicassoSingleton;
import tech.niocoders.com.bakingapp.R;
import tech.niocoders.com.fooddatabase.FoodView;

/**
 * Created by luism on 8/20/2018.
 */

public class ImageLinkTask extends AsyncTask<String, Void, String> {

    private Context myContext;
    private String ImageLink;
    private FoodView holder;
    private ImageView image;
    public ImageLinkTask(Context context, FoodView holder)
    {
        this.myContext = context;
        this.holder = holder;

    }
    public ImageLinkTask(Context context, ImageView holder)
    {
        this.myContext = context;
        this.image = holder;

    }

    public Context getMyContext() {
        return myContext;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String link)
    {this.ImageLink=link;}
    @Override
    protected String doInBackground(String... strings) {
        String id  =  strings[0];
        if(TextUtils.isEmpty(id))
            return null;
        try {
            //lets create our url path
            String apiKey = "6965345965c3f3aceffb197a9af1323d";
            String sharedSecret = "f62f8145e4e36069";
            REST rest = new REST();
            Flickr flickrClient = new Flickr(apiKey, sharedSecret, rest);

            SearchParameters searchParameters = new SearchParameters();
            searchParameters.setText(id);
            searchParameters.setTagMode(id);

            PhotoList photos = flickrClient.getPhotosInterface().search(searchParameters,5,1);
            if(photos!=null) {
                String photo_id = ((Photo) photos.get(0)).getId();
                String farm_id = ((Photo) photos.get(0)).getFarm();
                String secret = ((Photo) photos.get(0)).getSecret();
                String server = ((Photo) photos.get(0)).getServer();

                String url = BakingAppUtilities.getFoodUrl(photo_id, farm_id, server, secret).toString();
                return url;
            }

        }catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //lets notify our movieDetailActivity that we have some reviews available so we can show them to the viewer
        if(s!=null && !TextUtils.isEmpty(s))
        {
           Log.d("ImageLinkTask",s);
            int w = Integer.parseInt(myContext.getResources().getString(R.string.image_width));
            int h =  Integer.parseInt(myContext.getResources().getString(R.string.image_height));
            if(holder!=null){
                PicassoSingleton.populateImageView(s,holder.food_poster,w,h);
            }
            if(image!=null) {
                PicassoSingleton.populateImageView(s, image, w, h);
            }
        }

    }
}
