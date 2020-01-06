package service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.projectgithub.LocationAddress;
import com.example.projectgithub.Maps;
import com.example.projectgithub.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import dbhelperformap.MapAddress;
import retrofit2.Call;
import retrofit2.Response;


public class BGServicenormal extends Service {
    public static final String APIKEY = "AIzaSyC64zlV2Fq8qt__VYmRxCXxphHS44ih54I";

    private static final String TAG = "BGServicenormal";
    private static final String TAG2="Testing";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    private static final int TWENTY_MINUTES = 1* 60 * 1000;
    static final int NOTIFICATION_ID = 543;
    private boolean isServiceRunning = false;
    public LocationManager locationManager;
    public MyLocationListener listener;
    private Location mLastLocation, mCurrentLocation;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String address, city, state, description, locate, emaildi, phonenumber, formatdate, formatedtime,regid;
    String empid,empcode;
    int ntwCount=0;
    private double mLatitude;
    private double mLongtitude;
    int id, s_Id;
    String trackId;
    String name = "noName", phoneType = "";
    private float distance=0f;
    private String query;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private Date previousTime=null;
    private Location mylocation;
    private int onLocationCount=0;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private String daydetails,datedetails,dateandtime;


    private boolean isConnected()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        //boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        return isConnected;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            startForeground(12345678, startNotification());
            Log.wtf(TAG, "onCreate: Rm" + isServiceRunning);
            //date
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            datedetails = df.format(c.getTime());


            //Day
            SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
            Date date = new Date();
            daydetails = sdf_.format(date);
           // startServiceWithNotification();
            getLocationDetails();
//            sharedPreferences = getSharedPreferences(Config.SHARED_PREF_Userupdate, MODE_PRIVATE);
//            editor = sharedPreferences.edit();
//            name = sharedPreferences.getString("serviceenggnmae", "");
//            description = sharedPreferences.getString("description", "");
//            address = sharedPreferences.getString("address", "");
//            locate = sharedPreferences.getString("worklocation", "");
//            formatdate = sharedPreferences.getString("formateddate", "");
//            formatedtime = sharedPreferences.getString("formatedtime", "");
//            regid = sharedPreferences.getString("regid", "");
//            empid = sharedPreferences.getString("executiveid", "");
//            empcode=sharedPreferences.getString("KEY_log_User_Name", "");
//            mLastLocation = new Location("");
//            // mConnectivityReceiver = new ConnectivityReceiver(this);
//            phoneType = sharedPreferences.getString("fingerType", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    Notification startNotification()
    {
        Notification notification=null;
        try {

            Log.d(TAG, "startServiceWithNotification: first");
            Intent notificationIntent = new Intent(this, LocationAddress.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.logoicon);
            Log.d(TAG, "startServiceWithNotification: second");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setTicker(getResources().getString(R.string.app_name))
                    .setSmallIcon(R.drawable.route)
                    .setContentText("Travel in Progress")
//                    .setContentInfo(latitude)
//                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true);

            Log.d("distance", String.valueOf(distance));
            notification = builder.build();
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;// NO_CLEAR makes the notification stay when the user performs a "delete all" command
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Travel in Progress");
                builder.setChannelId(NOTIFICATION_CHANNEL_ID);

                Log.d(TAG, "startServiceWithNotification: four");
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.createNotificationChannel(channel);
            }
            Log.d(TAG, "startServiceWithNotification: five");
//            startForeground(NOTIFICATION_ID, notification);
//            mNotificationManager.notify(0 /* Request Code */, builder.build());
            startForeground(1, notification);

            Log.d(TAG, "startServiceWithNotification: six");
        }catch (Exception e)
        {
            e.printStackTrace();

            Log.d(TAG, "startServiceWithNotification: error");
        }
        return notification;
    }



    public void getLocationDetails() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        try {
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TWENTY_MINUTES, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TWENTY_MINUTES, 0, listener);

//          locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
         /*   locationManager.addGpsStatusListener(new android.location.GpsStatus.Listener() {
                public void onGpsStatusChanged(int event) {
                    switch (event) {
                        case GPS_EVENT_STARTED:
                            // do your tasks
                            break;
                        case GPS_EVENT_STOPPED:
                            // gpsstopprd();
                           *//* String message = "Sorry! Not connected to gps";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();*//*
                            break;
                    }
                }
            });*/


//            myLocationData = getSharedPreferences("Registration", MODE_PRIVATE);
//            String id = myLocationData.getString("LastTravelId", null);
            Log.d(TAG, "onStartCommand: id*********");
//            myLocationData = getSharedPreferences("Registration", MODE_PRIVATE);
//            if(id!=null)
//            {
//                LastTravelId=myLocationData.getString("LastTravelId",null);
//                editor.putString("LastLat", myLocationData.getString("FromLat",null));
//                editor.putString("LastLang", myLocationData.getString("FromLang",null));
//                mLastLocation = new Location("");
//                editor.apply();
//                editor.commit();
//                mLastLocation.setLatitude(Float.parseFloat( myLocationData.getString("FromLat",null)));
//                mLastLocation.setLongitude(Float.parseFloat(myLocationData.getString("FromLang",null)));
//            } else {
//                Log.d(TAG, "onStartCommands: id=null ");
//                stopMyService();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void getLastLocation() {
//        int permissionLocation = ContextCompat.checkSelfPermission(Atte.this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
////            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
////            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
////            mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            locationRequest = new LocationRequest();
//            locationRequest.setInterval(100);
//            locationRequest.setFastestInterval(100);
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setSmallestDisplacement(10);
//            loc = true;
//        }
//    }



    @Override
    public void onTaskRemoved(final Intent rootIntent) {
//        startActivity(getPopupIntent());

    }

    @Override
    public void onDestroy() {
        try {
            if(previousTime==null)
            {
                Calendar cal = Calendar.getInstance();
                Date time = cal.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                previousTime =simpleDateFormat.parse (simpleDateFormat.format(time));
                Log.d(TAG, "onDestroy: lastTime "+previousTime.getTime());

//            if (CheckDeviceIsMoved(loc, mLastLocation))
                Log.d(TAG, "onDestroy: lat 1 " + mLatitude + " n lon 1 " + mLongtitude);
                getDistance((mLastLocation.getLatitude()), mLastLocation.getLongitude(), mLatitude, mLatitude);
                new GeocodeAsyncTask().execute(mLatitude, mLongtitude);
                sendlocationdetails();
//                new AddItems().execute(mLatitude, mLongtitude);
            }
            else if(diffTime(previousTime)>10)
            {
                Calendar cal = Calendar.getInstance();
                Date time = cal.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                previousTime =simpleDateFormat.parse (simpleDateFormat.format(time));
                Log.d(TAG, "onDestroy: lastTime "+previousTime.getTime());
                Log.d(TAG, "onDestroy: lat 1 " + mLatitude + " n lon 1 " + mLatitude);
                getDistance((mLastLocation.getLatitude()), mLastLocation.getLongitude(),mLatitude, mLatitude);
                new GeocodeAsyncTask().execute(mLatitude, mLongtitude);
                sendlocationdetails();
//                new AddItems().execute(mLatitude, mLongtitude);
            }
            else {
                Log.d(TAG, "onDestroy: time diff "+diffTime(previousTime));
                if(onLocationCount>=3)
                {
                    getDistance((mLastLocation.getLatitude()), mLastLocation.getLongitude(),mLatitude, mLatitude);
                    new GeocodeAsyncTask().execute(mLatitude, mLongtitude);
                    sendlocationdetails();
//                    new AddItems().execute(mLatitude, mLongtitude);
                }
            }
            Log.d(TAG, "onDestroyBGService: ");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        stopForeground(true);
        super.onDestroy();
    }

    //send location api
    private void sendlocationdetails() {
		MapAddress mapAddress=new MapAddress(BGServicenormal.this);
		mapAddress.insertcoordinate(address,String.valueOf(mCurrentLocation.getLongitude()),String.valueOf(mCurrentLocation.getLatitude()),"");
		Log.d("inserted", address);
//        try {
//            trackId = sharedPreferences.getString("trackid", "");
//            CategoryAPI service = RetroClient.getApiServicesales();
//            // Calling JSON
//            Log.d(TAG, "sendlocationdetails:");
//            Call<BackGroundServiceJson> call = service.background(name,empid,trackId,datedetails,address,dateandtime,distance,"",mLatitude, mLongtitude,empcode,daydetails);
//            Response<BackGroundServiceJson> response=call.execute();
//            if(response.isSuccessful())
//            {
//                Log.d(TAG2, "insertedjson");
//                Log.d(TAG2, address);
//                Log.d(TAG2, String.valueOf(distance));
//                Log.d(TAG2, "getDistance: Distance "+distance);
//            }
////            {
////                @Override
////                public void onResponse(Call<BackGroundServiceJson> call, Response<BackGroundServiceJson> response) {
////                    if (response.body().getResult().equals("Success")) {
////
////
////                    } else if (response.body().getResult().equals("NotSuccess")){
////
////
//////                        Toast.makeText(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_LONG).show();
////                    }
////                }
////                @Override
////                public void onFailure(Call<BackGroundServiceJson> call, Throwable t) {
////
////
////                }
////            });
//
//        } catch (Exception ex) {
//            ex.getMessage();
//
//        }
    }


    void stopMyService() {
        stopForeground(true);
        stopSelf();
        isServiceRunning = false;
    }

    private boolean CheckDeviceIsMoved(Location current, Location last) {
        try {
            double dist = current.distanceTo(last) / 1000;
//            Log.v("distance Calculate ", Double.toString(dist));

            NumberFormat df = DecimalFormat.getInstance();
            df.setMaximumFractionDigits(3);
            df.setGroupingUsed(false);

            String distanc = df.format(dist).toString().replaceAll(",", ".");
            dist = Double.parseDouble(distanc);

            //  dist = Double.parseDouble(df.format(dist));

//            Log.v("dist Calculate Decimal ", Double.toString(dist));
            if (dist <= 0.900) { //(in km, you can use 0.1 for metres etc.)
                //If it's within 1km, we assume we're not moving
//                Log.v("Device is = ", "Not  Moved");
//                return false;
                return true;
            } else {
//                Log.v("Device is = ", "Moved");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public long diffTime(Date lastTime) {
        long min = 0;
        long differences,difference,minutes=0 ;
        Log.d(TAG, "onLocationChanged diffTime: one");
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa"); // for 12-hour system, hh should be used instead of HH
//            // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0
//            Date date1 = simpleDateFormat.parse("09:40 AM");
//            Date date2 = simpleDateFormat.parse("10:20 AM");
            Calendar cal = Calendar.getInstance();
            Date time = cal.getTime();
            Log.d(TAG, "onLocationChanged diffTime: format "+simpleDateFormat.format(time));
            Date currentTime = simpleDateFormat.parse(simpleDateFormat.format(time));

            Log.d(TAG, "onLocationChanged diffTime: two");

            differences = (currentTime.getTime() - lastTime.getTime()) / 1000;
            Log.d(TAG, "diffTime: difs "+differences);
//            difference = (currentTime - lastTime) / 1000;
//            Log.d(TAG, "diffTime: diff "+difference);
//            long hours = difference % (24 * 3600) / 3600; // Calculating Hours
            minutes = differences % 3600 / 60; // Calculating minutes if there is any minutes difference
//            long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
//            min = minute + (hours * 60); // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins
//            Log.d(TAG, "diffTime: "+difference);
//            Log.d(TAG, "hours: "+hours);
            Log.d(TAG, "minutes: "+minutes);
//            Log.d(TAG, "minute: "+minute);
            Log.d(TAG, "min: "+min);
        } catch (Throwable e) {
            Log.d(TAG, " error diffTime: "+e.getMessage());
            e.printStackTrace();
        }
        return minutes;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand:");
        try {
            String regid = sharedPreferences.getString("regid", "");
            if(regid!=null) {
                startForeground(12345678,startNotification());
            }
            else
                stopMyService();
            // startTimer();
            //countDownTimer.start();
            Log.d(TAG, "onStartCommand: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_REDELIVER_INTENT;
     //   return START_NOT_STICKY ;
    }

    public String getDistance(double lat1, double lon1, double lat2, double lon2) {
        distance = 0f;
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        System.out.println(lat1 + " " + lon1 + " " + lat2 + " " + lon2);
        // String url = "http://maps.google.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric";
        String url = "https://maps.googleapis.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&key=" + APIKEY + "";

        String tag[] = {"value"};  //will give distance as string e.g 1.2 km
        // or tag[] = {"value"} if you want to get distance in metre e.g. 1234
        Log.i("URL display == ", url);
        String address[] = {"end_address"};

        //dependency http apache
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);

            if (doc != null) {
                NodeList nl, n2;
                ArrayList args = new ArrayList();
                ArrayList args1 = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add("0");
                    }
                    for (String s1 : address) {
                        n2 = doc.getElementsByTagName(s1);
                        if (n2.getLength() > 0) {
                            Node node = n2.item(n2.getLength() - 1);
                            args1.add(node.getTextContent());
                        } else {
                            args1.add("no address");
                        }
                    }
                }

                String dis = String.format("%s", args.get(0));

                distance = Float.parseFloat(dis) / 1000;
                Log.d(TAG, "getDistance: Distance "+distance);
//                end_address = args1.get(0).toString().replace("'", "");
            } else {
                System.out.print("Doc is null");
                Log.d(TAG, "getDistance: Doc is null ");
                if (lat1 != 0.0) {
                    Location locationA = new Location("point A");
                    locationA.setLatitude(lat1);
                    locationA.setLongitude(lon1);
                    Location locationB = new Location("point B");
                    locationB.setLatitude(lat2);
                    locationB.setLongitude(lon2);
                    distance = locationA.distanceTo(locationB) / 1000;
                    Log.d(TAG, "getDistance: small distance "+distance);
                }
            }
            if (distance == 0) {
                Log.d(TAG, "getDistance: inside distance is zero");
                if (lat1 != 0.0) {
                    Log.d(TAG, "getDistance: inside lat not one");
                    Location locationA = new Location("point A");
                    locationA.setLatitude(lat1);
                    locationA.setLongitude(lon1);
                    Location locationB = new Location("point B");
                    locationB.setLatitude(lat2);
                    locationB.setLongitude(lon2);
                    distance = locationA.distanceTo(locationB) / 1000;

                    //this code is for learning purpose
//                    Location home=new Location("home");
//                    home.setLatitude(11.5159);
//                    home.setLongitude(79.3269);
//
//                    Location hometwo=new Location("home2");
//                    hometwo.setLatitude(13.0827);
//                    hometwo.setLongitude(80.2707);
//
//                    distance=home.distanceTo(hometwo);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "getDistance: inside exception in getDistance");
            e.printStackTrace();
        }

        try {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(3);
            Log.d("distance from google **", Float.toString(distance));
            distance = Float.parseFloat(df.format(distance));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }



    private class GeocodeAsyncTask extends AsyncTask<Double, Void, Address> {

        String errorMessage = "";

        @Override
        protected Address doInBackground(Double... latlang) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            if (geocoder.isPresent()) {
                try {
                    addresses = geocoder.getFromLocation(latlang[0], latlang[1], 1);
//                    Log.d(TAG, "doInBackground: ************");
                } catch (IOException ioException) {
                    errorMessage = "Service Not Available";
//                    Log.e(TAG, errorMessage, ioException);
                } catch (IllegalArgumentException illegalArgumentException) {
                    errorMessage = "Invalid Latitude or Longitude Used";
//                    Log.e(TAG, errorMessage + ". " +
//                            "Latitude = " + latlang[0] + ", Longitude = " +
//                            latlang[1], illegalArgumentException);
                }

                if (addresses != null && addresses.size() > 0)
                    return addresses.get(0);
            } else {
//                new GetGeoCodeAPIAsynchTask().execute(latlang[0], latlang[1]);
            }

            return null;
        }

        protected void onPostExecute(Address addresss) {
            double lat=0,lon=0;
            if (addresss == null) {
                new GetGeoCodeAPIAsynchTask().execute(lat, lon);
            } else {
                address = addresss.getAddressLine(0); //0 to obtain first possible address
                city = addresss.getLocality();
                Log.d(TAG, "onPostExecute: **************************"+city);
                state = addresss.getAdminArea();
                //create your custom title
//                String title = city + "-" + state;
//                Alertbox alertbox=new Alertbox(MainActivity.this);
//                alertbox.showAlertboxwithback("Your Current location is "+city);
            }
        }
    }

    private class GetGeoCodeAPIAsynchTask extends AsyncTask<Double, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(Double... latlang) {
            String response;
            try {
                String URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latlang[0] + "," + latlang[1] + "&key=" + APIKEY;
//                Log.v("URL", URL);
                response = getLatLongByURL(URL);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                address = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONArray("address_components").getJSONObject(0).getString("long_name");

                city = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONArray("address_components").getJSONObject(2).getString("long_name");

                state = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONArray("address_components").getJSONObject(4).getString("long_name");

                Log.d(TAG, "onPostExecute: "+city);
            } catch (JSONException e) {
                e.printStackTrace();
                if(TextUtils.isEmpty(address)||address.equals(null))
                {
                    address="Address Not found";
                }
            }

        }

        public String getLatLongByURL(String requestURL) {
            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            try {
                //Time
                Calendar cal = Calendar.getInstance();
                Date currentTime = cal.getTime();
              SimpleDateFormat dftime = new SimpleDateFormat("HH:mm a");
                dateandtime = dftime.format(currentTime);
                Log.d(TAG, "Location changed");
                Log.d(TAG, "onLocationChanged: "+mLatitude);
                mLastLocation.setLatitude(mLatitude);
                mLastLocation.setLongitude(mLongtitude);

//                String latitude=String.valueOf(mLatitude+", "+mLongtitude+","+address);
//                builder.setContentTitle(address);
//                mNotificationManager.notify(10001,builder.build());
//                builder.build();
                //to find how many time it calls onLocationListener
                onLocationCount++;
                Log.d(TAG, "onLocationChanged: preTime " + (previousTime));
                if(previousTime==null)
                {
                    Calendar cals = Calendar.getInstance();
                    Date time = cals.getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                    previousTime =simpleDateFormat.parse (simpleDateFormat.format(time));
                    Log.d(TAG, "onLocationChanged: previousTime=null ");

                    onLocationCount=0;
                    mLatitude = loc.getLatitude();
                    mLongtitude = loc.getLongitude();
                    Log.d(TAG, "onLocationChanged: lat 1 " + loc.getLatitude() + " n lon 1 " + loc.getLongitude());
                    getDistance((mLastLocation.getLatitude()), mLastLocation.getLongitude(), loc.getLatitude(), loc.getLongitude());
                    if (isConnected()) {

                        new GeocodeAsyncTask().execute(loc.getLatitude(), loc.getLongitude());
                        getDistance((mLastLocation.getLatitude()), mLastLocation.getLongitude(), loc.getLatitude(), loc.getLongitude());
                        sendlocationdetails();
                        Log.d(TAG, "thirdtime");
//                        new AddItems().execute(loc.getLatitude(), loc.getLongitude());
                    }else {
                        ntwCount++;
                    }
                }
                else if(diffTime(previousTime)>10)
                {
                    onLocationCount=0;
                    Log.d(TAG, "onLocationChanged: " + (previousTime.getTime()));
                    Log.d(TAG, "onLocationChanged: diff " + (diffTime(previousTime)));
                    Calendar cals = Calendar.getInstance();
                    Date time = cals.getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                    previousTime =simpleDateFormat.parse (simpleDateFormat.format(time));
                    Log.d(TAG, "onLocationChanged: lastTime "+previousTime.getTime());
                    mLatitude = loc.getLatitude();
                    mLongtitude = loc.getLongitude();
                    Log.d(TAG, "onLocationChanged: lat 1 " + loc.getLatitude() + " n lon 1 " + loc.getLongitude());
                    getDistance((mLastLocation.getLatitude()), mLastLocation.getLongitude(), loc.getLatitude(), loc.getLongitude());
                    if (isConnected()) {
                        new GeocodeAsyncTask().execute(loc.getLatitude(), loc.getLongitude());
                        sendlocationdetails();
                        Log.d(TAG, "Secondtime");
//                        new AddItems().execute(loc.getLatitude(), loc.getLongitude());
                    }else {
                        ntwCount++;
                    }
                }
                else {
                    Log.d(TAG, "onLocationChanged: time diff "+diffTime(previousTime));
                    Log.d(TAG, "onLocationChanged: onLocationCount "+onLocationCount);
                    if(onLocationCount>=3)
                    {
                        if(isConnected()){
                            getDistance((mLastLocation.getLatitude()), mLastLocation.getLongitude(), loc.getLatitude(), loc.getLongitude());
                            new GeocodeAsyncTask().execute(loc.getLatitude(), loc.getLongitude());
                            sendlocationdetails();
//                            new AddItems().execute(loc.getLatitude(), loc.getLongitude());
                        }else {
                            ntwCount++;
                        }

                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                Log.d(TAG, "onLocationChanged: "+e.getCause());
                Log.d(TAG, "onLocationChanged: "+e.getMessage());
                Log.d(TAG, "onLocationChanged: "+e);
            }
        }

        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


    }

}
