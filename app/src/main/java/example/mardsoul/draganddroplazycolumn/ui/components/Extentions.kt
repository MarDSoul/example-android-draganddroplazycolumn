package example.mardsoul.draganddroplazycolumn.ui.components

import example.mardsoul.draganddroplazycolumn.domain.UserEntity
import example.mardsoul.draganddroplazycolumn.ui.UserEntityUi

fun UserEntity.toUiUser() = UserEntityUi(this.id, this.name)