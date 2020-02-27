package com.example.projectgithub;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.LocalSocketAddress;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dbhelper.PartDetailsDB;
import network.NetworkChangeReceiver;
import network.NetworkConnection;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import roomdatabase.AppDatabase;
import roomdatabase.ImagePath;
import roomdatabase.Users;
import service.BGServicenormal;

public class LocationAddress extends AppCompatActivity  implements  GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	com.google.android.gms.location.LocationListener,
	android.location.LocationListener {

	private static final String TAG ="mainactivityatag" ;
	//location declartion
	private GoogleApiClient googleApiClient;
	private LocationRequest mLocationRequest;
	public static Location lastLocation;
	private LocationManager locationManager;
	protected final int TWO_MINUTES = 2* 60 * 1000;
	protected int MIN_DISTANCE_CHANGE_FOR_UPDATES=500;
	protected final int REQUEST_LOCATION_SETTINGS = 100;
	protected static final int RESULT_LOCATION_CODE_CANCELLED = 0;
	private LocationCallback mLocationCallback;
	private String addressss="";
	private static final int THREE_MINUTES = 3* 60 * 1000;
	//location declaration ending intials location manager on OnCreate
	private TextView locationaddress;
	private NetworkConnection networkConnection;

	private ProgressDialog progressDialog;
	private Button startservice,stopservice,openmap,pickimage,notification;

	//service
	public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
	public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

	//pick image declare check activity result to get selected image
	private ImagePicker imagePicker;
	private String mImageName = "";
	private File mImageFile;
	String imagename="";
	private Uri i;
	private ImageView selectedimage,alereadyimage;

	//broadcast receiver to check interner connection
	private BroadcastReceiver broadcastReceiver;

	//room database
	private AppDatabase appDatabase;
	//notification
	private Integer j= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationactivity);

		//location managner intials
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		//initial for location

		startservice=findViewById(R.id.startservice);
		stopservice=findViewById(R.id.stopservice);
		openmap=findViewById(R.id.openmap);
		pickimage=findViewById(R.id.pickimage);
		selectedimage=findViewById(R.id.selectedimage);
		alereadyimage=findViewById(R.id.alereadyimage);
		notification=findViewById(R.id.notification);
		networkConnection=new NetworkConnection(LocationAddress.this);
		locationaddress = findViewById(R.id.locationaddress);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		progressDialog.setTitle("Retrieving your Location Details");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		PartDetailsDB partDetailsDB1=new PartDetailsDB(this);

//		byte[] image=partDetailsDB1.getimage();
//		ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
//		Bitmap theImage= BitmapFactory.decodeStream(imageStream);
//		selectedimage.setImageBitmap(theImage);
//		selectedimage.setVisibility(View.VISIBLE);
//		Users users=new Users();
//		users.setUserName("balaji");
//		users.setPassword("balajisachin");
//
//
//
		appDatabase=AppDatabase.getInstance(this);
		List <ImagePath> imagePaths=appDatabase.productDAO().getimage();

//		Picasso.with(LocationAddress.this)
//				.load(new File(imagePaths.get(0).getImagePath()))
//				.memoryPolicy(MemoryPolicy.NO_CACHE )
//				.networkPolicy(NetworkPolicy.NO_CACHE)
//				.into(alereadyimage);
//		appDatabase.productDAO().insertusername(users);

//		List<Users> users1 = appDatabase.productDAO().getusers();


//		appDatabase.productDAO().updatetable("sachin","balaji","1");
//		List<Users> users2 = appDatabase.productDAO().getusers();
		if (networkConnection.CheckInternet()){
			//if network present
		}

		//using broadcast receiver to find network state
		// reffer on start and on stop method and registerNetworkBroadcastForNougat() to using broadcast
		broadcastReceiver=new NetworkChangeReceiver() {
			@Override
			protected void onNewPosition(Boolean availability) {
				if (availability){
					//internet connection true
				}else {
					//internet connection false
				}
			}
		};

		startservice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//start services
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					ContextCompat.startForegroundService(   LocationAddress.this,new Intent(LocationAddress.this, BGServicenormal.class).setAction(ACTION_START_FOREGROUND_SERVICE));
				}
				else {
					startService(new Intent(LocationAddress.this, BGServicenormal.class).setAction(ACTION_START_FOREGROUND_SERVICE));
				}
			}
		});
		notification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				NotificationManager notif=(NotificationManager)LocationAddress.this.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notify=new Notification.Builder
						(LocationAddress.this.getApplicationContext()).
						setContentTitle(LocationAddress.this.getResources().getString(R.string.app_name)).
						setContentText(String.valueOf(lastLocation.getLongitude()+lastLocation.getLatitude())).
						setVibrate(new long[]{100,250,100,250,100,250}).
						setSound(alarmSound).
						setSmallIcon(R.drawable.ic_launcher_background).build();
				notify.flags |= Notification.FLAG_AUTO_CANCEL;
				notify.number+=1;
				notif.notify(j++, notify);
			}
		});
		stopservice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//stop
				//service
				stopService(new Intent(LocationAddress.this, BGServicenormal.class).setAction(ACTION_STOP_FOREGROUND_SERVICE));
			}
		});
		openmap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent map=new Intent(getApplicationContext(),Maps.class);
				map.putExtra("latitude",lastLocation.getLatitude());
				map.putExtra("longtitude",lastLocation.getLongitude());
				startActivity(map);
			}
		});

		pickimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				imagePicker=new ImagePicker(LocationAddress.this, null, new OnImagePickedListener() {
					@Override
					public void onImagePicked(Uri imageUri) {
						selectedimage.setImageURI(imageUri);
						mImageName = getFileName(imageUri);
						mImageFile = getFileFromImage();
						String imagepath= mImageFile.getAbsolutePath();

//						String imageurl="//media/external/images/media/44257/"+mImageName;
						ImagePath imagePathdao=new ImagePath();
						imagePathdao.setImagePath(imagepath);
						appDatabase.productDAO().insertimage(imagePathdao);
//						List <ImagePath> imagePaths=appDatabase.productDAO().getimage();
//						PartDetailsDB partDetailsDB=new PartDetailsDB(LocationAddress.this);
//						partDetailsDB.insertUserDetails(imagepath);

//						Glide.with(LocationAddress.this)
//								.load(new File(imagePaths.get(0).toString())
//								.into(alereadyimage);

//						Picasso.with(LocationAddress.this)
//								.load(new File(imagePaths.get(0).getImagePath()))
//								.memoryPolicy(MemoryPolicy.NO_CACHE )
//								.networkPolicy(NetworkPolicy.NO_CACHE)
//								.into(alereadyimage);
//						File imageFile = new File(imageurl);
//						BitmapDrawable d = new BitmapDrawable(getResources(), imageFile.getAbsolutePath());
////						selectedimage.setImageDrawable(d);
//						selectedimage.setVisibility(View.VISIBLE);
						i=imageUri;
						if (i.equals(null)){

						}
						else {
							selectedimage.setVisibility(View.VISIBLE);
						}
					}
				});
				imagePicker.choosePicture(true);
			}
		});

		initpermission();
	}

	private void getcamerapermission() {
		if (ContextCompat.checkSelfPermission(LocationAddress.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
//			qrcodescanoptionalert();
		}else {
			ActivityCompat.requestPermissions(LocationAddress.this,new String[] {Manifest.permission.CAMERA},9);
		}
	}

//	onActivityresultfor camerapermission
//	if (requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
//		qrcodescanoptionalert();
//	}else if (requestCode==9){
//		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//		dialog.setTitle("Give Permission" )
//				.setMessage("Need camera permission to scan")
//				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialoginterface, int i) {
//						dialoginterface.cancel();
//					}})
//				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialoginterface, int i) {
//						getcamerapermission();
//					}
//				}).show();
//	}
	private File getFileFromImage() {
		try {
			BitmapDrawable drawable = (BitmapDrawable) selectedimage.getDrawable();
			Bitmap bitmap = drawable.getBitmap();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			File directory = new File(getFilesDir(), "profile");
			if (!directory.exists())
				directory.mkdirs();
			File myappFile = new File(directory
					+ File.separator + mImageName);
			FileOutputStream fos = new FileOutputStream(myappFile);
			fos.write(byteArray);
//			mImageName = File_URL + myappFile.getName();
			return myappFile;
		} catch (Exception e) {
			e.printStackTrace();
			return new File("");
		}
	}

	public String getFileName(Uri uri) {
		String result = null;
		if (uri.getScheme().equals("content")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			} finally {
				cursor.close();
			}
		}
		if (result == null) {
			result = uri.getPath();
			int cut = result.lastIndexOf('/');
			if (cut != -1) {
				result = result.substring(cut + 1);
			}
		}
		return result;
	}

	//step1:
	private void initpermission() {
		if (ContextCompat.checkSelfPermission(LocationAddress.this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
		{
			getlocationinfo();
		}else {
			ActivityCompat.requestPermissions(LocationAddress.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},10);
		}
	}

	//step2
	@SuppressLint("MissingPermission")
	private void getlocationinfo() {
		mLocationCallback = new LocationCallback() {
			@SuppressLint("MissingPermission")
			@Override
			public void onLocationResult(LocationResult locationResult) {
				super.onLocationResult(locationResult);
				// location is received
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER,
						THREE_MINUTES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES,LocationAddress.this);
				lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                    mLastUpdateTime = DateFormat.gettimeinstance().format(new Date());
//                    updateLocationUI();
//                    progressDialog.dismiss();
			}
		};
		createGoogleApi();
		createLocationRequest();
		if (lastLocation == null) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					THREE_MINUTES,
					MIN_DISTANCE_CHANGE_FOR_UPDATES, LocationAddress.this);
			lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
			enableGPS();
		}
	}

	//step 3
	private synchronized void createGoogleApi() {
		{
//			googleApiClient = new GoogleApiClient.Builder(LocationAddress.this)
//				.addConnectionCallbacks(this)
//				.addOnConnectionFailedListener(this)
//				.addApi(LocationServices.API)
//				.build();

			googleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
		}
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(100);
		mLocationRequest.setFastestInterval(100);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(10);
	}

	//step 4
	private void enableGPS() {
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
			.addLocationRequest(mLocationRequest);

		PendingResult<LocationSettingsResult> result =
			LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
				final Status status = locationSettingsResult.getStatus();
				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.SUCCESS:
						setFusedLocationUpdate();
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						try {
							status.startResolutionForResult(
								LocationAddress.this,
								REQUEST_LOCATION_SETTINGS);
						} catch (IntentSender.SendIntentException e) {

						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						break;
				}
			}
		});
	}

	//step 5
	private void setFusedLocationUpdate() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    Activity#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for Activity#requestPermissions for more details.
				return;
			}
		}
		locationManager.requestLocationUpdates(
			LocationManager.NETWORK_PROVIDER,
			TWO_MINUTES,
			MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		if (googleApiClient.isConnected())
			LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
	}

	//step 6 to gps enabled or not after all permission given to enable gps call enableGPS() method declared above in step 4
	private boolean isGPSEnabled() {
		boolean gps_enabled = false;
		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		return gps_enabled;
		//step 7 in on onConnected method
	}

	// step 7 after gps connected on resume and on pause we have to connect googleAPIClient
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		progressDialog.show();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (LocationAddress.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && LocationAddress.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    Activity#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for Activity#requestPermissions for more details.
				return;
			}
		}
		if (lastLocation == null) {
			lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
		} if (isGPSEnabled()) {
			if (lastLocation != null) {
//                        "Long: " + lastLocation.getLongitude() +
//                        " | Lat: " + lastLocation.getLatitude());
				setFusedLocationUpdate();

			}else {
				getlocationinfo();
			}
		}
	}

	//step 8
	@Override
	public void onResume() {
		super.onResume();
		if (googleApiClient != null) {
//            if(!googleApiClient.isConnected())
			googleApiClient.connect();
		}
	}

	//step 9
	@Override
	public void onPause() {
		super.onPause();
		if (googleApiClient != null) {
//            if(!googleApiClient.isConnected())
			googleApiClient.connect();
		}
	}

	// TO Get address from latittude and longititude
	// example to call new GeocodeAsyncTask().execute(lastLocation.getLatitude(),lastLocation.getLongitude());
	private class GeocodeAsyncTask extends AsyncTask<Double, Void, Address> {

		String errorMessage = "";
		@SuppressLint("LongLogTag")
		@Override
		protected Address doInBackground(Double... latlang) {
			Geocoder geocoder = new Geocoder(LocationAddress.this, Locale.getDefault());
			List<Address> addresses = null;
			if (geocoder.isPresent()) {
				try {
					addresses = geocoder.getFromLocation(latlang[0], latlang[1], 1);
					Log.d(TAG, "doInBackground: ************");
				} catch (IOException ioException) {
					errorMessage = "Service Not Available";
					Log.e(TAG, errorMessage, ioException);
				} catch (IllegalArgumentException illegalArgumentException) {
					errorMessage = "Invalid Latitude or Longitude Used";
					Log.e(TAG, errorMessage + ". " +
						"Latitude = " + latlang[0] + ", Longitude = " +
						latlang[1], illegalArgumentException);
				}

				if (addresses != null && addresses.size() > 0)
					return addresses.get(0);
			}
//            else {
//                new GetGeoCodeAPIAsynchTask().execute(latlang[0], latlang[1]);
//            }
			return null;
		}

		@SuppressLint("LongLogTag")
		protected void onPostExecute(Address addresss) {

			if (addresss == null) {
//                new GetGeoCodeAPIAsynchTask().execute(mylocation.getLatitude(), mylocation.getLongitude());
				Log.d(TAG, "onPostExecute: *****");
			} else {
//                progressBar.setVisibility(View.GONE);
				String address = addresss.getAddressLine(0);
//                editor.putString("ToAddress", address);
//                editor.commit();
//                editor.apply();
				String City = addresss.getLocality();
				Log.d(TAG, "onPostExecute: **************************" + City);
				String city = addresss.getLocality();
				String state = addresss.getAdminArea();
				//create your custom title
				String title = city + "-" + state;
				addressss=address +
					"\n"
					+ title;
				locationaddress.setText(addressss);


//                editor.putString("FromAddress", address);
//                editor.commit();

                 Geocoder geocoder = new Geocoder(LocationAddress.this);
                try {
                    ArrayList<Address> addresses = (ArrayList<Address>) geocoder.getFromLocationName("karur", 50);
                    for (Address address3 : addresses) {
                        double lat = address3.getLatitude();
                        double lon = address3.getLongitude();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
				//create your custom title
//                String title = city + "-" + state;
//                Alertbox alertbox=new Alertbox(LocationAddress.this);
//                alertbox.showAlertboxwithback("Your Current location is "+city);

			}
		}
	}

	//step 10 to check permision request
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode==10&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {
			getlocationinfo();
		}
		else if (requestCode==10) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(LocationAddress.this);
			dialog.setTitle("Give Permission" )
				.setMessage("Need location permission to use the app")
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						LocationAddress.this.finish();
					}})
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						initpermission();
					}
				}).show();
		}
		init();

	}
	//step 11
	private void init() {
		if (getServicesAvailable()) {

//                if(googleApiClient==null)
			createGoogleApi();
//                if(mLocationRequest==null)
			createLocationRequest();
			if (lastLocation == null)
				lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

			if (!isGPSEnabled()) {
				enableGPS();
			}
//			else {
//				setFusedLocationUpdate();
//			}

		}
	}
	//step 12 to check google service available or not in device
	public boolean getServicesAvailable() {
		GoogleApiAvailability api = GoogleApiAvailability.getInstance();
		int isAvailable = api.isGooglePlayServicesAvailable(LocationAddress.this);
		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		} else if (api.isUserResolvableError(isAvailable)) {

			Dialog dialog = api.getErrorDialog(LocationAddress.this, isAvailable, 0);
			dialog.show();
			return false;
		} else {
			Toast.makeText(LocationAddress.this, "Cannot Connect To Play Services", Toast.LENGTH_SHORT).show();
			return false;
		}

	}

	// step 13 to check GPS is enabled or not while we request GPS
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_LOCATION_SETTINGS) {
			if (resultCode == RESULT_LOCATION_CODE_CANCELLED) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(LocationAddress.this);
				dialog.setTitle("Give Permission" )
					.setMessage("You cannot use this Application without turning ON your GPS")
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							LocationAddress.this.finish();
						}})
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							enableGPS();
						}
					}).show();
			} else {
				setFusedLocationUpdate();
			}
				//image picked

		}	if (requestCode==200) {
//			Uri targetUri = data.getData();
//			Bitmap mBitmap;
//			try {
////				mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
////				String imageurl=mBitmap.toString();
////				ByteArrayOutputStream baos = new ByteArrayOutputStream();
////				mBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
////				byte[] photo = baos.toByteArray();
////				selectedimage.setImageBitmap(mBitmap);
////				selectedimage.setVisibility(View.VISIBLE);
////				PartDetailsDB partDetailsDB=new PartDetailsDB(LocationAddress.this);
////				partDetailsDB.openDatabase();
////				partDetailsDB.insertUserDetails(photo);
//
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//
//			}

//			selectedimage.setImageBitmap(mBitmap);

			//             textTargetUri.setText(targetUri.toString());
			super.onActivityResult(requestCode, resultCode, data);
//            if (imagePicker.handleActivityResult(resultCode,requestCode,data!=null)) {
			if (resultCode == RESULT_OK) {
				imagePicker.handleActivityResult(resultCode, requestCode, data);
			}
		}
	}




	//Bulk Permission
	//declare in permission

	//	private String[] locationAccess = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET};

	//ask permission.

//		private void initPermission() {
//		if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(VisitReport.this, locationAccess[0]) ||
//			PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(VisitReport.this, locationAccess[1]) ||
//			PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(VisitReport.this, locationAccess[2]) ||
//			PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(VisitReport.this, locationAccess[3])
//		) {
//			ActivityCompat.requestPermissions(VisitReport.this, locationAccess, REQUEST_CHECK_SETTINGS);
//		} else {
//			permissionGranted = true;
//			init();
////            setMDM();
////            startLocationUpdate();
//		}
//	}

	// Check for permission to access Location
//	private boolean checkPermission() {
//		Log.d(TAG, "checkPermission()");
//		// Ask for permission if it wasn't granted yet
//		return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//			== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//			== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//			== PackageManager.PERMISSION_GRANTED);
//	}



	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {

	}

	@Override
	public void onProviderEnabled(String s) {

	}

	@Override
	public void onProviderDisabled(String s) {

	}



	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onLocationChanged(Location location) {

		if (lastLocation != null) {
			lastLocation.setLatitude(location.getLatitude());
			lastLocation.setLongitude(location.getLongitude());
			progressDialog.dismiss();
			new GeocodeAsyncTask().execute(lastLocation.getLatitude(), lastLocation.getLongitude());
			//Or Do whatever you want with your location
		} else if (lastLocation != null) {
			progressDialog.dismiss();
			new GeocodeAsyncTask().execute(lastLocation.getLatitude(), lastLocation.getLongitude());
		}


	}

//	public String getFileName(Uri uri) {
//		String result = null;
//		if (uri.getScheme().equals("content")) {
//			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//			try {
//				if (cursor != null && cursor.moveToFirst()) {
//					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//				}
//			} finally {
//				cursor.close();
//			}
//		}
//		if (result == null) {
//			result= getFileNames(uri);
//
//		}
//		return result;
//	}



//	public String getFileNames(Uri uri) {
//
//		String[] projection = {MediaStore.MediaColumns.DATA};
//		String path=null;
//		ContentResolver cr = getApplicationContext().getContentResolver();
//		Cursor metaCursor = cr.query(uri, projection, null, null, null);
//		if (metaCursor != null) {
//			try {
//				if (metaCursor.moveToFirst()) {
//					path = metaCursor.getString(0);
//				}
//			} finally {
//				metaCursor.close();
//			}
//		}
//		return path;
//	}

	//upload a image in server using retrofit

//	mImageFile= getFileFromImage();
//	String filepath=mImageFile.getName();

//	CategoryAPI service = RetroClient.getApiServiceUpload();
//	RequestBody Title = RequestBody.create(MediaType.parse("text/plain"),mImageFile);
//	RequestBody PDFreq = RequestBody.create(MediaType.parse("application/pdf"),mImageFile.getName());
//	MultipartBody.Part filePart = MultipartBody.Part.createFormData("pdf",mImageFile.getName(),PDFreq);
////	Call<UploadImage> call = service.UploadImage(filePart);

//	if (("Success.").equals(response.body().getMessage()))

//	private File getFileFromImage() {
//		try {
////            BitmapDrawable drawable = (BitmapDrawable) selectedimage.getDrawable();
////            Bitmap bitmap = drawable.getBitmap();
////			ByteArrayOutputStream stream = new ByteArrayOutputStream();
////            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
////			byte[] byteArray = stream.toByteArray();
//			File directory = new File(getFilesDir(), "profile");
//			if (!directory.exists())
//				directory.mkdirs();
//			File myappFile = new File(directory
//				+ File.separator + getFileName(i));
////            FileOutputStream fos = new FileOutputStream(myappFile);
////            fos.write(byteArray);
//			return myappFile;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new File("");
//		}
//	}

	//broadcast receiver above noungat
	private void registerNetworkBroadcastForNougat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		}
	}

	//to call broadcast receiver on start of an activity
	@Override
	public void onStart() {
		super.onStart();
		registerNetworkBroadcastForNougat();
		googleApiClient.connect();
	}

	//on activity stop
	@Override
	public void onStop() {
		super.onStop();
		unregisterNetworkChanges();
		googleApiClient.disconnect();
	}

	protected void unregisterNetworkChanges() {
		try {
			unregisterReceiver(broadcastReceiver);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

}
