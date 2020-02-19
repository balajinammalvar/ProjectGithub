package roomdatabase;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ProductDAO {


    @Insert
    void insertusername(Users users);

}
