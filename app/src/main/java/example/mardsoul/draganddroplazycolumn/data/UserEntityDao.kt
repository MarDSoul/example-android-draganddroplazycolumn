package example.mardsoul.draganddroplazycolumn.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import example.mardsoul.draganddroplazycolumn.domain.UserEntity

@Entity(
	tableName = UserEntityDao.TABLE_NAME
)
data class UserEntityDao(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = COLUMN_ID)
	val id: Int,

	@ColumnInfo(name = COLUMN_NAME)
	val name: String
) {
	fun toUser(): UserEntity = UserEntity(id, name)

	companion object {
		const val TABLE_NAME = "user"
		const val COLUMN_ID = "id"
		const val COLUMN_NAME = "name"
	}
}
