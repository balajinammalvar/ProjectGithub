package com.example.projectgithub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;

import alertbox.Alertbox;
import api.APIService;
import api.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RefferenceClass {

	private ProgressDialog progressDialog;
	private Alertbox alertbox;

//	private void login(String uname, String upassword) {
//		try {
//			progressDialog = new ProgressDialog(this);
//			progressDialog.setMessage("Loading...");
//			progressDialog.setTitle("Retrieving your Location Details");
//			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			progressDialog.show();
//			APIService service = RetrofitClient.getApiService();
//			//Calling JSON
//			Call<LoginDetails> call = service.login(uname,upassword,imeiNumber1,imeiNumber2,usermobilemodel,addressss);
//			call.enqueue(new Callback<LoginDetails>() {
//				@Override
//				public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
//					progressDialog.dismiss();
//					if (response.body().getResult().equals("Success")) {
//
//					} else  if (response.body().getResult().equals("IMEI Changed Approval Pending")){
//						Sucess("Device has been changed, Please wait for admin approval ");
//					}else if (response.body().getResult().equals("UserDoesnotMatch")){
//						Sucess("Username and password does not match, please contact admin");
//					}
//				}
//				@Override
//				public void onFailure(Call<LoginDetails> call, Throwable t) {
//					progressDialog.dismiss();
//					Sucess("Failed to fetch data from server");
//					alertbox.alertdismiss("");
//				}
//			});
//		} catch (Exception ex) {
//			ex.getMessage();
//			Sucess("Exception error please try again later");
//		}
//	}

//	public void Sucess(String s){
//		Intent home=new Intent(getApplicationContext(),HomeScreenActivity.class);
//		home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//		Alertbox alert=new Alertbox(RefferenceClass.this);
//		alert.sucess(s,home);
//	}

	//upload images method
//	private void uploadimage(File selectimage) {
//		try {
//			loading = new ProgressDialog(ScanActivity.this);
//			loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			loading.setCancelable(false);
//			loading.setMessage("Loading...");
//			if (!loading.isShowing())
//				loading.show();
//			APIService service = RetrofitClient.getApiUplaodService();
//			String type="image/png";
//			imagename=selectimage.getName();
//			RequestBody requestBody = RequestBody.create(MediaType.parse(type), selectimage);
//			MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", selectimage.getName(), requestBody);
//			final Call<UploadImage> request = service.imageupload(filePart);
//			request.enqueue(new Callback<UploadImage>() {
//				@Override
//				public void onResponse(Call<UploadImage> call, Response<UploadImage> response) {
//					if (response.body().getMessage().equals("Success.")) {
//						loading.dismiss();
//						uploaduserdetails();
//					}
////                                else {
////                                    Nointernet("Please Try Again later");
////                                }
//				}
//				@Override
//				public void onFailure(Call<UploadImage> call, Throwable t) {
//					Log.v("Upload Exception", t.getMessage());
//					loading.dismiss();
//					t.printStackTrace();
//
//				}
//
//			});
//		}
//		catch (Exception ex) {
//			if (loading.isShowing())
//				loading.dismiss();
//			Log.v("Exception", ex.getMessage());
//
//		}
//	}

}
