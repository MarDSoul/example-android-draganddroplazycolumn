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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.channels.Channel

@Composable
fun rememberDragAndDropListState(
    lazyListState: LazyListState,
    onMove: (Int, Int) -> Unit
): DragAndDropListState {
    val currentOnMove by rememberUpdatedState(onMove)
    val state = remember { DragAndDropListState(lazyListState) { from, to -> currentOnMove(from, to) } }
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
    // channel for emitting scroll events
    val scrollChannel = Channel<Float>()

    // the index of item that is being dragged
    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)
        private set

    // the item that is being dragged
    private val currentElement: LazyListItemInfo?
        get() = currentIndexOfDraggedItem?.let { lazyListState.getVisibleItemInfo(it) }

    // the initial index of item that is being dragged
    private var initialDraggingElement by mutableStateOf<LazyListItemInfo?>(null)

    // the initial offset of the item when it is dragged
    private val initialOffsets: Pair<Int, Int>?
        get() = initialDraggingElement?.let { Pair(it.offset, it.offsetEnd) }

    // distance that has been dragged
    private var draggingDistance by mutableFloatStateOf(0f)

    // calculates the vertical displacement of the dragged element
    // relative to its original position in the LazyList.
    val elementDisplacement: Float?
        get() = currentIndexOfDraggedItem
            ?.let { lazyListState.getVisibleItemInfo(it) }
            ?.let { itemInfo ->
                (initialDraggingElement?.offset
                    ?: 0f).toFloat() + draggingDistance - itemInfo.offset
            }


    /**
     * Starts the dragging operation when the user initiates a drag gesture.
     *
     * This function determines which item in the LazyList is being dragged based on the
     * starting offset of the drag gesture. It then stores the information about the
     * dragged item and its index for use during the drag operation.
     * */
    fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { item -> offset.y.toInt() in item.offset..item.offsetEnd }
            ?.also {
                initialDraggingElement = it
                currentIndexOfDraggedItem = it.index
            }
    }

    /**
     * Called when a drag operation is interrupted or canceled.
     *
     * Resets the state of the ongoing drag by clearing the dragged element reference,
     * the dragged item index, and the dragging distance.  This is invoked when a drag
     * is canceled, fails, or completes.
     */
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
            val middleOffset = (startOffset + endOffset) / 2f

            currentElement?.let { current ->
                val targetElement = lazyListState.layoutInfo.visibleItemsInfo.find {
                    middleOffset.toInt() in it.offset..it.offsetEnd && current.index != it.index
                }
                if (targetElement != null) {
                    currentIndexOfDraggedItem?.let {
                        onMove.invoke(it, targetElement.index)
                    }
                    currentIndexOfDraggedItem = targetElement.index
                } else {
                    checkOverscroll()
                }
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