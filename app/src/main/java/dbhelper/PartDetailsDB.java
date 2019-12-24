package dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.example.projectgithub.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;


public class PartDetailsDB {

    private Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private PartModel partModel;
    private static final String TAG = "PartDetailsDB";
    public static final String CART_TABLE_NAME = "CartItems";
    public static final String COLUMN_PART_SEGMENT = "segments";
    public static final String COLUMN_PART_NUMBER = "partnumber";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_SNO="SNO";
    public static final String COLUMN_TOTALAMT="totalamunt";
    public static final String COLUMN_DEALERMOBILE="dealermobile";



    public PartDetailsDB(Context context){
        this.context=context;
        dbHelper=new DbHelper(context);
        onCreateCartTable();
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

    public void onCreateCartTable() {
        openDatabase();
        db.execSQL("create table if not exists "+ CART_TABLE_NAME+" ( "
                +COLUMN_SNO+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,"
                +COLUMN_PART_SEGMENT+" NVARCHAR, "
                +COLUMN_PART_NUMBER+" NVARCHAR, "+COLUMN_QUANTITY+" NVARCHAR ,"
                +COLUMN_DESCRIPTION+" NVARCHAR, "+COLUMN_PRICE+" NVARCHAR,"+COLUMN_TOTALAMT+" NVARCHAR,"+COLUMN_DEALERMOBILE+" NVARCHAR);");
        Log.d(TAG, "onCreate: ");
    }

    public void insertPartsData(PartModel partModel, String partnumber, String qty, String selectedmobile)
    {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        String partNum=(partnumber);//DatabaseUtils.sqlEscapeString
        String partDes= partModel.getDescription();
        //DatabaseUtils.sqlEscapeString
        String mrp=partModel.getMrp();
        String totalamt=partModel.getTotalamt();
        String qtys=(qty);
        String seg=partModel.getSegement();
        String dealermobile=selectedmobile;

        contentValues.put(COLUMN_PART_SEGMENT,seg);
        contentValues.put(COLUMN_PART_NUMBER,partNum);
        contentValues.put(COLUMN_QUANTITY,""+qty);
        contentValues.put(COLUMN_DESCRIPTION,partDes);
        contentValues.put(COLUMN_PRICE,mrp);
        contentValues.put(COLUMN_TOTALAMT,totalamt);
        contentValues.put(COLUMN_DEALERMOBILE,dealermobile);
        try {
            String query="insert into "+CART_TABLE_NAME+ "( "+COLUMN_PART_SEGMENT+" , "+COLUMN_PART_NUMBER+" , "
                    +COLUMN_QUANTITY+" , "+COLUMN_DESCRIPTION+" , "+COLUMN_PRICE+","+COLUMN_TOTALAMT+","+COLUMN_DEALERMOBILE+" ) values ('"+seg+"','"+partNum+"','"
                    +qtys+"','"+partDes+"' ,'"+mrp+"','"+totalamt+"','"+dealermobile+"')";
            if (getCartItems(partNum) == 0) {
//            if (partDes.equals("")) {
              db.execSQL(query);
            Cursor cursor=db.rawQuery(query,null);
                 cursor.moveToNext();
                long k = db.insert(CART_TABLE_NAME, null,contentValues);
                if (k != -1)
                {
                    StyleableToast st = new StyleableToast(context,
                            ""+ partNum + " is added to Cart", Toast.LENGTH_SHORT);
                    st.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    st.setTextColor(Color.WHITE);
                    st.setMaxAlpha();
                    st.show();
                    closeDatabase();
                }
                else {
                    StyleableToast st = new StyleableToast(context,
                            "Please try again", Toast.LENGTH_SHORT);
                    st.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    st.setTextColor(Color.WHITE);
                    st.setMaxAlpha();
                    st.show();
                }
           Log.d(TAG, "insertPartsData: k value " + k);
           closeDatabase();
     } else {
//           String qt=getCartQuantity(partNum);
//            qty= Integer.toString(Integer.parseInt(qt)+1);
                updateCartQuantity(partNum, qty,totalamt,partDes);
            }
            Log.d(TAG, "insertPartsData: ");
        }
        catch (Exception ed)
        {
            ed.printStackTrace();
        }
    }

    public String getCartQuantityhome()
    {
        String count="";
        String query="Select Count (*)"+COLUMN_PART_NUMBER+" from "+CART_TABLE_NAME+"";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            count=cursor.getString(cursor.getColumnIndex(COLUMN_PART_NUMBER));
        }

        return count;
    }
    public void updateCartQuantity(String partNo, String qty, String totalamt, String partDes)
    {
        openDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_QUANTITY,qty);
        values.put(COLUMN_TOTALAMT,totalamt);
        values.put(COLUMN_DESCRIPTION,partDes);
        int k=db.update(CART_TABLE_NAME,values,COLUMN_PART_NUMBER+" = '"+partNo+"'",null);
        Log.d(TAG, "updateCartQuantity: in k Value "+k);
        if(k<=-1)
        {

            StyleableToast st = new StyleableToast(context,
                    "Please try again", Toast.LENGTH_SHORT);
            st.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            st.setTextColor(Color.WHITE);
            st.setMaxAlpha();
            st.show();
//            Toast.makeText(context,""+partNo+" is added to Cart",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StyleableToast st = new StyleableToast(context,
                    ""+partNo+"'s quantity is changed", Toast.LENGTH_SHORT);
            st.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            st.setTextColor(Color.WHITE);
            st.setMaxAlpha();
            st.show();
        }
//        closeDatabase();
    }

    public int getCartItems(String partNo)
    {
        openDatabase();
        String query="select "+COLUMN_QUANTITY+" from "+CART_TABLE_NAME+" where "+COLUMN_PART_NUMBER+" = '"+partNo+"'";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            Log.d(TAG, "getCartQuantity: "+cursor.getColumnIndex(COLUMN_QUANTITY));
            return Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY)));
        }
        else {
            return 0;
        }
    }

    public String getcartmobile(){

        String alreadyaddedmobile="";
        openDatabase();
        String query="select "+COLUMN_DEALERMOBILE+" from "+CART_TABLE_NAME+" ";
        Cursor cursor=db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            alreadyaddedmobile=cursor.getString(cursor.getColumnIndex(COLUMN_DEALERMOBILE));
        }
        return alreadyaddedmobile;
    }

    public String gettotalordervalue(){
        String totalcartvalue="";

        openDatabase();
        String query="SELECT  DISTINCT partnumber,segments,quantity,description,price,totalamunt FROM CartItems";
        Cursor cursor=db.rawQuery(query,null);
        int overalltotal=0;
        if (cursor.moveToFirst()) {
            do {
                String totalamt = cursor.getString(cursor.getColumnIndex(COLUMN_TOTALAMT));
                int tt = Integer.parseInt(totalamt);
                overalltotal += tt;
            }
            while (cursor.moveToNext());
            totalcartvalue= String.valueOf(overalltotal);
        }

        return totalcartvalue;
    }

//    public ArrayList<Order_Parts> getCartItem()
//    {
//        openDatabase();
//        String query= "SELECT  DISTINCT partnumber,segments,quantity,description,price,totalamunt FROM CartItems";
////        String query="select * from "+CART_TABLE_NAME;
//        ArrayList<Order_Parts> list=new ArrayList<>();
//
//        Cursor cursor=db.rawQuery(query,null);
//        if(cursor.moveToFirst()) {
//            do {
//                Order_Parts partModelClass = new Order_Parts();
//                String partNum = cursor.getString(cursor.getColumnIndex(COLUMN_PART_NUMBER));
//                String partDes = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
//                String partPrice = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));
//                String partQuantity = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
//                String totalamt = cursor.getString(cursor.getColumnIndex(COLUMN_TOTALAMT));
//                String segment=cursor.getString(cursor.getColumnIndex(COLUMN_PART_SEGMENT));
//
//                partModelClass.setPartNumber(partNum);
//                partModelClass.setDescription(partDes);
//                partModelClass.setPartPrice(partPrice);
//                partModelClass.setQuantity(partQuantity);
//                partModelClass.setDeliveryStatus("Pending");
//                partModelClass.setSegement(segment);
//                partModelClass.setTotalAmount(totalamt);
//                partModelClass.setActive("Y");
//
//                list.add(partModelClass);
//            } while (cursor.moveToNext());
//        }
////        closeDatabase();
//        return list;
//
//    }

    public boolean deleteCartItems(String partNum)
    {
        try {
            openDatabase();
            String query = "delete from " + CART_TABLE_NAME + " where " + COLUMN_PART_NUMBER + " = '" + partNum+"'";
            db.execSQL(query);
            closeDatabase();
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public void deleteTable()
    {
        try {
            openDatabase();
            String querys = "drop table " + CART_TABLE_NAME;
            db.execSQL(querys);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            closeDatabase();
        }


    }
}
