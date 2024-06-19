package example.mardsoul.draganddroplazycolumn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import example.mardsoul.draganddroplazycolumn.data.AppDatabase
import example.mardsoul.draganddroplazycolumn.data.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

	@Provides
	@Singleton
	fun provideUserDao(database: AppDatabase): UserDao = database.userDao()


	@Provides
	@Singleton
	fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
		return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
			.build()
	}

}