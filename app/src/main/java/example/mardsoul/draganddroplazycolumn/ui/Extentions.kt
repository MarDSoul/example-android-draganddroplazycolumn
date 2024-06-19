package example.mardsoul.draganddroplazycolumn.ui

import example.mardsoul.draganddroplazycolumn.domain.UserEntity

fun UserEntity.toUiUser() = UserEntityUi(this.id, this.name)