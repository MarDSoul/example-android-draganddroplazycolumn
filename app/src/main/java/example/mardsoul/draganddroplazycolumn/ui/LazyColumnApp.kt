package example.mardsoul.draganddroplazycolumn.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import example.mardsoul.draganddroplazycolumn.R
import kotlinx.coroutines.launch

@Composable
fun LazyColumnApp(
	modifier: Modifier = Modifier,
	viewModel: AppViewModel = hiltViewModel(),
) {

	val uiState by viewModel.uiState.collectAsState()
	val lazyListState = rememberLazyListState()
	val coroutineScope = rememberCoroutineScope()

	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(dimensionResource(R.dimen.medium_padding))
	) {
		Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
			Button(onClick = {
				viewModel.addUser()
				if (uiState is UiState.Success) {
					if ((uiState as UiState.Success).data.isNotEmpty()) {
						val lastIndex = (uiState as UiState.Success).data.lastIndex
						coroutineScope.launch {
							lazyListState.animateScrollToItem(lastIndex)
						}
					}
				}
			}) {
				Text(text = stringResource(R.string.add_button_text))
			}
			Button(onClick = { viewModel.clearList() }) {
				Text(text = stringResource(R.string.clear_button_text))
			}
		}

		when (val state = uiState) {
			is UiState.Error -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.weight(1f),
					contentAlignment = Alignment.Center
				) {
					Text(text = state.message, style = MaterialTheme.typography.titleLarge)
				}
			}

			UiState.Loading -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.weight(1f),
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator()
				}
			}

			is UiState.Success -> {
				val users = state.data
				LazyColumn(
					state = lazyListState,
					modifier = Modifier
						.fillMaxSize()
						.weight(1f)
				) {
					items(users, key = { it.id }) {
						ItemCard(userEntityUi = it, modifier = Modifier.fillMaxWidth())

					}
				}
			}
		}
	}
}

@Composable
fun ItemCard(userEntityUi: UserEntityUi, modifier: Modifier = Modifier) {
	Card(
		shape = MaterialTheme.shapes.small,
		modifier = modifier.padding(dimensionResource(R.dimen.small_padding))
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(dimensionResource(R.dimen.small_padding))
		) {
			Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = null)
			Text(
				text = stringResource(R.string.user_id_label),
				style = MaterialTheme.typography.labelLarge,
				modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding))
			)
			Text(
				text = userEntityUi.id.toString(),
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(modifier = Modifier.size(dimensionResource(R.dimen.medium_padding)))
			Text(
				text = stringResource(R.string.user_name_label),
				style = MaterialTheme.typography.labelLarge,
				modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding))
			)
			Text(text = userEntityUi.name, style = MaterialTheme.typography.bodyMedium)
		}
	}
}