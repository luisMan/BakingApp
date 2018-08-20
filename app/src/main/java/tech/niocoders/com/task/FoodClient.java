package tech.niocoders.com.task;

/*author luis manon*/

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodClient {

    private static final String URL  = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    //lets get the retosfit instance
    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    //get api services
    public static FoodEndPoint getFoodEndPointService()
    {
        return getRetrofitInstance().create(FoodEndPoint.class);
    }

}
