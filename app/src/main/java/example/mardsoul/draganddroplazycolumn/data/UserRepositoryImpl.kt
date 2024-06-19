package example.mardsoul.draganddroplazycolumn.data

import example.mardsoul.draganddroplazycolumn.di.DefaultDispatcher
import example.mardsoul.draganddroplazycolumn.di.IoDispatcher
import example.mardsoul.draganddroplazycolumn.domain.UserEntity
import example.mardsoul.draganddroplazycolumn.domain.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
	private val userDao: UserDao,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher,
	@DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : UserRepository {
	override suspend fun addUser(user: UserEntity) =
		withContext(ioDispatcher) {
			userDao.addUser(user.toDao())
		}

	override fun getAllUsers(): Flow<List<UserEntity>> {
		return userDao.getAllUsers()
			.flowOn(ioDispatcher)
			.map { it.map { userDao -> userDao.toUser() } }
			.flowOn(defaultDispatcher)
	}

	override suspend fun updateUser(userId: Int, newName: String) =
		withContext(ioDispatcher) {
			userDao.updateUser(UserEntityDao(userId, newName))
		}

	override suspend fun deleteUserById(userId: Int) =
		withContext(ioDispatcher) {
			userDao.deleteUserById(userId)
		}

	override suspend fun deleteAll() =
		withContext(ioDispatcher) {
			userDao.deleteAll()
		}
}