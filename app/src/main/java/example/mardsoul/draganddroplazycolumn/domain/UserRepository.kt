package example.mardsoul.draganddroplazycolumn.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
	suspend fun addUser(user: UserEntity)
	fun getAllUsers(): Flow<List<UserEntity>>
	suspend fun updateUser(userId: Int, newName: String)
	suspend fun deleteUserById(userId: Int)
	suspend fun deleteAll()
}