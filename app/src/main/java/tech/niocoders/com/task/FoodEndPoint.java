package tech.niocoders.com.task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodEndPoint {


    @GET("baking.json")
    Call<List<Food>> getMyJSON();

}
