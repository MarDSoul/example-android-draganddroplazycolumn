package example.mardsoul.draganddroplazycolumn.ui.components

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.channels.Channel

@Composable
fun rememberDragAndDropListState(
	lazyListState: LazyListState,
	onMove: (Int, Int) -> Unit
): DragAndDropListState {
	val state = remember { DragAndDropListState(lazyListState, onMove) }
	LaunchedEffect(state) {
		while (true) {
			val diff = state.scrollChannel.receive()
			state.lazyListState.scrollBy(diff)
		}
	}
	return state
}

class DragAndDropListState(
	val lazyListState: LazyListState,
	private val onMove: (Int, Int) -> Unit
) {
	val scrollChannel = Channel<Float>()

	private var draggingDistance by mutableFloatStateOf(0f)
	private var initialDraggingElement by mutableStateOf<LazyListItemInfo?>(null)
	var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)
	private val initialOffsets: Pair<Int, Int>?
		get() = initialDraggingElement?.let { Pair(it.offset, it.offsetEnd) }
	val elementDisplacement: Float?
		get() = currentIndexOfDraggedItem?.let {
			lazyListState.getVisibleItemInfo(it)
		}?.let { itemInfo ->
			(initialDraggingElement?.offset ?: 0f).toFloat() + draggingDistance - itemInfo.offset
		}
	private val currentElement: LazyListItemInfo?
		get() = currentIndexOfDraggedItem?.let {
			lazyListState.getVisibleItemInfo(it)
		}

	fun onDragStart(offset: Offset) {
		lazyListState.layoutInfo.visibleItemsInfo
			.firstOrNull { item -> offset.y.toInt() in item.offset..item.offsetEnd }
			?.also {
				initialDraggingElement = it
				currentIndexOfDraggedItem = it.index
			}
	}

	fun onDragInterrupted() {
		initialDraggingElement = null
		currentIndexOfDraggedItem = null
		draggingDistance = 0f
	}

	fun onDrag(offset: Offset) {
		draggingDistance += offset.y

		initialOffsets?.let { (top, bottom) ->
			val startOffset = top.toFloat() + draggingDistance
			val endOffset = bottom.toFloat() + draggingDistance

			currentElement?.let { current ->
				val targetElement = lazyListState.layoutInfo.visibleItemsInfo
					.filterNot { item ->
						item.offsetEnd < startOffset || item.offset > endOffset || current.index == item.index
					}
					.firstOrNull { item ->
						val delta = startOffset - current.offset
						when {
							delta < 0 -> item.offset > startOffset
							else -> item.offsetEnd < endOffset
						}
					}
				if (targetElement == null) {
					checkOverscroll()
				}
				targetElement
			}?.also { item ->
				currentIndexOfDraggedItem?.let { current ->
					onMove.invoke(current, item.index)
				}
				currentIndexOfDraggedItem = item.index
			}
		}
	}

	private fun checkOverscroll() {
		val overscroll = initialDraggingElement?.let {
			val startOffset = it.offset + draggingDistance
			val endOffset = it.offsetEnd + draggingDistance

			return@let when {
				draggingDistance > 0 -> {
					(endOffset - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 }
				}

				draggingDistance < 0 -> {
					(startOffset - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 }
				}

				else -> null
			}
		} ?: 0f

		if (overscroll != 0f) {
			scrollChannel.trySend(overscroll)
		}
	}

	private fun LazyListState.getVisibleItemInfo(itemPosition: Int): LazyListItemInfo? {
		return this.layoutInfo.visibleItemsInfo.getOrNull(itemPosition - this.firstVisibleItemIndex)
	}

	private val LazyListItemInfo.offsetEnd: Int
		get() = this.offset + this.size
}