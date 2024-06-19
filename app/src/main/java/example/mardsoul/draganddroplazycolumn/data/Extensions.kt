package example.mardsoul.draganddroplazycolumn.data

import example.mardsoul.draganddroplazycolumn.domain.UserEntity

fun UserEntity.toDao() = UserEntityDao(this.id, this.name)