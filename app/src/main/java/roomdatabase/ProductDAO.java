package roomdatabase;

import android.media.Image;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDAO {


    @Insert
    void insertusername(Users users);

    @Query("SELECT * FROM Users")
    List<Users> getusers();

    @Query("UPDATE  users SET UserName=:username,password=:password WHERE id=:id")
    void updatetable(String username,String password,String id);


    @Insert
    void insertimage(ImagePath imagePath);

    @Query("SELECT * FROM imagepaths")
    List<ImagePath> getimage();

}
