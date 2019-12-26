package dbhelperformap;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MapAddress {

    private Context context;
    private SQLiteDatabase db;
    public static final String MAPTABLE="MapTable";
    public static final String ADDRESS="Address";
    public static final String COLUMN_SNO="SNO";
    public static final String LATITUDE="Latitude";
    public static final String LOGTITUDE="Logitude";
    public static final String TIME="Time";


    private static final String TAG = "MapDatabase";
    private DBHelper dbHelper;

    public MapAddress(Context context) {

        this.context=context;
        dbHelper=new DBHelper(context);
        onCreateMapTable();
    }

    public void openDatabase()
    {
        this.db = this.dbHelper.readDataBase();
    }


    public void closeDatabase() {
        if (this.db != null) {
            this.db.close();
        }
    }
    public void onCreateMapTable() {
        openDatabase();
        db.execSQL("create table if not exists "+MAPTABLE+"("+COLUMN_SNO+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+ADDRESS+" NVARCHAR,"+LOGTITUDE+" NVARCHAR,"+LATITUDE+" NVARCHAR,"+TIME+", NVARCHAR);");
        Log.d(TAG, "onCreate: ");
    }

    public void insertcoordinate(String address, String longitute, String latitude, String time){
        openDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(ADDRESS,address);
        contentValues.put(LOGTITUDE,longitute);
        contentValues.put(LATITUDE,latitude);
        try{

            String query="insert into"+MAPTABLE+"("+ADDRESS+","+LOGTITUDE+","+LATITUDE+","+TIME+")values('"+address+"','"+longitute+"','"+latitude+"','"+time+"')";

            long k = db.insert(MAPTABLE, null, contentValues);
            if (k!=-1){
                Log.wtf(TAG, "inserted: ");

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
