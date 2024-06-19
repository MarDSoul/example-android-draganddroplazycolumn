package example.mardsoul.draganddroplazycolumn.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import example.mardsoul.draganddroplazycolumn.data.UserEntityDao.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

	//Create
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun addUser(user: UserEntityDao)

	//Read
	@Query("SELECT * FROM $TABLE_NAME")
	fun getAllUsers(): Flow<List<UserEntityDao>>

	//Update
	@Update
	suspend fun updateUser(user: UserEntityDao)

	//Delete
	@Query("DELETE FROM $TABLE_NAME WHERE id = :userId")
	suspend fun deleteUserById(userId: Int)

	@Query("DELETE FROM $TABLE_NAME")
	suspend fun deleteAll()
}