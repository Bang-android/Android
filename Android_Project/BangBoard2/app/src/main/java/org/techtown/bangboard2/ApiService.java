package org.techtown.bangboard2;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ApiService {

    @FormUrlEncoded
    @POST("register.php")
    Call<String> getUserRegist(
            @Field("uid") String id,
            @Field("upassword") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<String> getUserLogin(
            @Field("uid") String id,
            @Field("upassword") String password
    );

    @FormUrlEncoded
    @POST("register_id_check.php")
    Call<String> idCheck(
            @Field("uid") String id);

    @Multipart
    @POST("UploadToServer.php")
    Call<String> uploadImage(
            @Part MultipartBody.Part imageFile);

    @FormUrlEncoded
    @POST("insert.php")
    Call<String> insertBoard(
            @Field("title") String title,
            @Field("content") String content,
            @Field("date") String date,
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<String> deleteBoard(
            @Field("title") String title,
            @Field("content") String content,
            @Field("date") String date,
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<String> updateBoard(
            @Field("oldTitle") String oldTitle,
            @Field("oldContent") String oldContent,
            @Field("oldDate") String oldDate,
            @Field("newTitle") String newTitle,
            @Field("newContent") String newContent,
            @Field("newDate") String newDate
    );

    @GET("select.php")
    Call<String> selectBoard();

    @GET("selectEquipment.php")
    Call<String> selectEquipment();
}
