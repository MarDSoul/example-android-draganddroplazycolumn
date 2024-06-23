package example.mardsoul.draganddroplazycolumn.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import example.mardsoul.draganddroplazycolumn.domain.UserEntity
import example.mardsoul.draganddroplazycolumn.domain.UserRepository
import example.mardsoul.draganddroplazycolumn.ui.components.toUiUser
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AppViewModel @Inject constructor(
	private val repository: UserRepository
) : ViewModel() {

	val uiState: StateFlow<UiState> =
		repository.getAllUsers()
			.map { it.map { userEntity -> userEntity.toUiUser() } }
			.map { UiState.Success(it) }
			.catch { UiState.Error() }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000),
				initialValue = UiState.Loading
			)

	fun clearList() {
		viewModelScope.launch {
			repository.deleteAll()
		}
	}

	fun addUser() {
		viewModelScope.launch {
			repository.addUser(generateUser())
		}
	}

	private fun generateUser(): UserEntity = UserEntity(0, "User ${Random.nextInt(100)}")

}

sealed interface UiState {
	object Loading : UiState
	data class Error(val message: String = ERROR_MESSAGE) : UiState
	data class Success(val data: List<UserEntityUi>) : UiState
}

private const val ERROR_MESSAGE = "Something went wrong"