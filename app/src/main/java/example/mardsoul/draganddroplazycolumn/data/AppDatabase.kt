package example.mardsoul.draganddroplazycolumn.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntityDao::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
	abstract fun userDao(): UserDao
}