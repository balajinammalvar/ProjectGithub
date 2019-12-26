package dbhelperformap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static String DATABASE_PATH = null;
    private static final String FILENAME = "lucassalesorder.db";
    private static int version=1;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;


    @SuppressLint({"SdCardPath"})
    public DBHelper(Context context) {
        super(context, FILENAME, null, version);
        this.sqLiteDatabase = null;
        this.context = context;
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/balajimap/";
    }

    public SQLiteDatabase readDataBase() {
        try {
            InputStream in = this.context.getAssets().open(FILENAME);
            Log.wtf("sample", "Starting copying");
            File databaseFile = new File("/data/data/" + this.context.getPackageName() + "/databases/balajimap/");
            if (!databaseFile.exists())
            {
                boolean check = databaseFile.mkdirs();
                Log.v("Folder", "Created");
                Log.v("Folder", Boolean.toString(check));

                OutputStream out = new FileOutputStream(DATABASE_PATH + FILENAME);
                byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                while (true) {
                    int length = in.read(buffer);
                    if (length <= 0) {
                        break;
                    }
                    out.write(buffer, 0, length);
                }
                Log.e("sample", "Completed");
                out.flush();
                out.close();
                in.close();
            }
            this.sqLiteDatabase = SQLiteDatabase.openDatabase(DATABASE_PATH + FILENAME, null, 268435472);
            Log.d(TAG, "readDataBase: ");

        } catch (Exception e) {
            Log.v("errorr", e.getMessage());
        }
        return this.sqLiteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
