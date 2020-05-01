package com.bringo.home.Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Sajid on 1/26/2019.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAVBHwHKM:APA91bG6fBMaCUxF8yeFrZkt47MOqAH9H4sP_xvPtT7rk52uVaJ6i5sBbcNcmDEnfK4Ppzro23yBjzn8sQkwBA_n9JpPkmyQDokJ1xnfNBXIq9n-lTC0Hn9i2LWm5gV0K--IrbrGmoR_"
            }
    )

    @POST("fcm/send")
    Call<MyResponce> sendNotification(@Body Sender body);
}
