package roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import okhttp3.internal.Version;


//@databse entities give the table model class..............
@Database(entities ={Users.class,ImagePath.class},version=3)
public  abstract  class AppDatabase extends RoomDatabase  {

		private static AppDatabase INSTANCE;

		public abstract ProductDAO productDAO();

		public static AppDatabase getInstance(Context context){

			if (INSTANCE==null) {
				INSTANCE= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"roomdatabse")
					.allowMainThreadQueries()
					.fallbackToDestructiveMigration()
					.build();
			}
		return INSTANCE;
		}

}
