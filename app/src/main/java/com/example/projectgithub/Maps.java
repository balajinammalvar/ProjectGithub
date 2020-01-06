package com.example.projectgithub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dbhelperformap.MapAddress;

public class Maps extends FragmentActivity implements OnMapReadyCallback {



	private GoogleMap map;
	private FusedLocationProviderClient mFusedLocationClient;
	private FusedLocationProviderApi api;
	private Location currentLocation;
	private SettingsClient mSettingsClient;
	private LocationCallback locationCallback;
	private Double latitude, longitute;
	private LocationRequest mLocationRequest;
	private static final float DEFAULT_ZOOM = 15f;
	private boolean permissionGranted = false;
	private LocationSettingsRequest mLocationSettingsRequest;
	private Boolean mRequestingLocationUpdates;
	private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60000;
	private String TAG,getingaddress,inserttime;
	private static final int REQUEST_CHECK_SETTINGS = 100;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		getmap();
		latitude = getIntent().getDoubleExtra("latitude", 0.00);
		longitute = getIntent().getDoubleExtra("longtitude", 0.00);

		if (ActivityCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_FINE_LOCATION)
			!= PackageManager.PERMISSION_GRANTED &&
			ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			return;
		}
		initPermission();
		init();
                    Location home=new Location("home");
                    home.setLatitude(12.0879768);
                    home.setLongitude(79.2111239);

                    Location hometwo=new Location("home2");
                    hometwo.setLatitude(12.93991);
                    hometwo.setLongitude(79.8809852);
		 float distance=home.distanceTo(hometwo)/1000;
		Log.e("distance", String.valueOf(distance+" "+"km"));
	}

	private void getmap() {
		final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(Maps.this);
	}

	private void initPermission() {
		if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(Maps.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
			ActivityCompat.requestPermissions(Maps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
		} else {
			permissionGranted = true;
			init();
			startLocationUpdate();
		}
	}

	private void startLocationUpdate() {
		mSettingsClient
			.checkLocationSettings(mLocationSettingsRequest)
			.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
				@SuppressLint("MissingPermission")
				@Override
				public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
					Log.i(TAG, "All location settings are satisfied.");

					Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

					//noinspection MissingPermission
					mFusedLocationClient.requestLocationUpdates(mLocationRequest,
						locationCallback, Looper.myLooper());

//                        LocationManager manager= (LocationManager) getSystemService(LOCATION_SERVICE);

					updateLocationUI();
				}
			})
			.addOnFailureListener(this, new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					int statusCode = ((ApiException) e).getStatusCode();
					switch (statusCode) {
						case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
							Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
								"location settings ");
							try {
								// Show the dialog by calling startResolutionForResult(), and check the
								// result in onActivityResult().
								ResolvableApiException rae = (ResolvableApiException) e;
								rae.startResolutionForResult(Maps.this, REQUEST_CHECK_SETTINGS);
							} catch (IntentSender.SendIntentException sie) {
								Log.i(TAG, "PendingIntent unable to execute request.");
							}
							break;
						case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
							String errorMessage = "Location settings are inadequate, and cannot be " +
								"fixed here. Fix in Settings.";
							Log.e(TAG, errorMessage);

							Toast.makeText(Maps.this, errorMessage, Toast.LENGTH_LONG).show();
							break;
						case LocationSettingsStatusCodes.SUCCESS:
							permissionGranted = true;
							init();
					}
//                        moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM);
					updateLocationUI();
				}
			});
	}

	private void updateLocationUI() {
		if (currentLocation != null) {
			MapAddress mapAddress=new MapAddress (Maps.this);
			moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12.0f);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date=new Date();
			inserttime=dateFormat.format(date);
			new GeocodeAsyncTask().execute(currentLocation.getLatitude(),currentLocation.getLongitude());
		}
	}

	private void init() {
		if (permissionGranted) {
			mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
			mSettingsClient = LocationServices.getSettingsClient(this);

			locationCallback = new LocationCallback() {
				@Override
				public void onLocationResult(LocationResult locationResult) {
					super.onLocationResult(locationResult);
					// location is received
					currentLocation = locationResult.getLastLocation();
//                    moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM);
//                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
					updateLocationUI();
				}
			};

			mRequestingLocationUpdates = false;
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
			mLocationRequest.setFastestInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
			builder.addLocationRequest(mLocationRequest);
			mLocationSettingsRequest = builder.build();
//        startLocationButtonClick();
		}
	}


	private void moveCamera(LatLng latLng, float zoom) {
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		map.setMyLocationEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.animateCamera(CameraUpdateFactory.zoomTo( 17.0f ) );
		map.setTrafficEnabled(true);
//        CircleOptions circleOptions=new CircleOptions();
//        circleOptions.center(latLng);
//        circleOptions.radius(100);
//        circleOptions.fillColor(R.color.red);
//        circleOptions.clickable(true);
//        map.addCircle(circleOptions);
//        MarkerOptions marker = new MarkerOptions()
//                .position(latLng)
//                .title("Balaji");
//        map.addMarker(marker);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		switch (requestCode) {
			// Check for the integer request code originally supplied to startResolutionForResult().
			case REQUEST_CHECK_SETTINGS:
				switch (resultCode) {
					case Activity.RESULT_OK:
						Log.e(TAG, "User agreed to make required location settings changes.");
						// Nothing to do. startLocationupdates() gets called in onResume again.
						if(permissionGranted)
						{
							startLocationUpdate();
						}
						else {
							initPermission();
						}
						break;
					case Activity.RESULT_CANCELED:
						Log.e(TAG, "User chose not to make required location settings changes.");
						mRequestingLocationUpdates = false;
						break;
				}
				break;
		}
	}


	private class GeocodeAsyncTask extends AsyncTask<Double, Void, Address> {

		String errorMessage = "";
		@SuppressLint("LongLogTag")
		@Override
		protected Address doInBackground(Double... latlang) {
			Geocoder geocoder = new Geocoder(Maps.this, Locale.getDefault());
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

				String City = addresss.getLocality();
				Log.d(TAG, "onPostExecute: **************************" + City);
				String city = addresss.getLocality();
				String state = addresss.getAdminArea();
				//create your custom title
				String title = city + "-" + state;
//                addressss.setText(address +
//                        "\n"
//                        + title);
				getingaddress=address +
					"\n"
					+ title;
				MapAddress mapAddress=new MapAddress(Maps.this);
				mapAddress.insertcoordinate(getingaddress,String.valueOf(currentLocation.getLongitude()),String.valueOf(currentLocation.getLatitude()),inserttime);
//                UserVerified();
				Log.d("insert",getingaddress);
				Geocoder geocoder = new Geocoder(Maps.this);
				try {
					ArrayList<Address> addresses = (ArrayList<Address>) geocoder.getFromLocationName("karur", 50);
					for (Address address3 : addresses) {
						double lat = address3.getLatitude();
						double lon = address3.getLongitude();
//                        address2.setText(lat +
//                                "\n"
//                                + lon);

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				//create your custom title
//                String title = city + "-" + state;
//                Alertbox alertbox=new Alertbox(MainActivity.this);
//                alertbox.showAlertboxwithback("Your Current location is "+city);
			}
		}
	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		map.setMyLocationEnabled(true);
		moveCamera(new LatLng(latitude,longitute),12.0f);
	}
}
