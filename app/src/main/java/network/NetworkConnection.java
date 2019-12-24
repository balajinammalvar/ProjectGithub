package network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.sql.Connection;


public class NetworkConnection {
    private Boolean checkingInternet;
    private Connection connection;
    private Context context;
    private SQLiteDatabase db;
    private boolean dataisbeing;


    public NetworkConnection(Context context) {
        this.checkingInternet = Boolean.valueOf(false);
        this.context = context;
    }


    public boolean CheckInternet() {
        ConnectivityManager connec = (ConnectivityManager) this.context.getSystemService("connectivity");

        NetworkInfo wifi = connec.getNetworkInfo(1);
        NetworkInfo mobile = connec.getNetworkInfo(0);
        if (wifi.isConnected()) {
            this.checkingInternet = Boolean.valueOf(true);
        } else if (mobile.isConnected()) {
            this.checkingInternet = Boolean.valueOf(true);
        } else {
            this.checkingInternet = Boolean.valueOf(false);
        }
        return this.checkingInternet.booleanValue();
    }


}
