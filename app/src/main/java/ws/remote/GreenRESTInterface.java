package ws.remote;

import java.lang.reflect.Array;

import model.GreenEntry;
import model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Tejal Shah.
 */
public interface GreenRESTInterface {
    @POST("UserServlet")
    Call<User> createUser(@Body User user);

    @GET("UserServlet")
    Call<User> getUserDetails(@Query("userId") int userId);

    @POST("EditUserServlet")
    Call<User> editUser(@Body User user);

    @POST("UserAuthServlet")
    Call<User> authenticateUser(@Body User user);

    @POST("GreenEntryServlet")
    Call<GreenEntry> createGreenEntry(@Body GreenEntry greenEntry);

    @GET("TimelineServlet")
    Call<GreenEntry[]> getTimeline(@Query("opId") int opId);

}
