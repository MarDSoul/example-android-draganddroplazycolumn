package example.mardsoul.draganddroplazycolumn.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import example.mardsoul.draganddroplazycolumn.data.UserRepositoryImpl
import example.mardsoul.draganddroplazycolumn.domain.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

	@Binds
	@Singleton
	abstract fun bindRepository(repository: UserRepositoryImpl): UserRepository
}