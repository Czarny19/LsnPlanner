package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import kotlin.math.roundToInt

enum class DragAnchors { Start, Center, End }

data class DraggableCardAction(
    val color: Color,
    val imageVector: ImageVector,
    val contentDescription: String,
    val label: String,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableCard(
    modifier: Modifier = Modifier,
    clickAction: () -> Unit,
    colors: CardColors? = null,
    startAction: DraggableCardAction? = null,
    endAction: DraggableCardAction? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val density = LocalDensity.current
    var contentHeight by remember { mutableStateOf(0.dp) }

    val dragState = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Center at 0f
                startAction?.let { DragAnchors.Start at with(density) { 100.dp.toPx() } }
                endAction?.let { DragAnchors.End at with(density) { -100.dp.toPx() } }
            }
        )
    }

    Box(modifier = Modifier.anchoredDraggable(dragState, Orientation.Horizontal)) {
        DraggableCardActions(modifier, contentHeight, startAction, endAction)

        val offset = IntOffset(x = dragState.requireOffset().roundToInt(), y = 0)

        Card(
            modifier = modifier
                .fillMaxSize()
                .offset { offset }
                .onSizeChanged { size -> contentHeight = with(density) { size.height.toDp() } }
                .clickable { clickAction.invoke() },
            colors = colors ?: CardDefaults.cardColors(),
            elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.CARD_ELEVATION),
            content = content
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DraggableCardActions(
    modifier: Modifier = Modifier,
    height: Dp,
    startAction: DraggableCardAction?,
    endAction: DraggableCardAction?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        DraggableCardAction(height, startAction)
        Spacer(modifier = Modifier.weight(1.0f))
        DraggableCardAction(height, endAction)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableCardAction(height: Dp, action: DraggableCardAction?) {
    action?.let {
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(height)
                .clickable { it.onClick.invoke() },
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = it.color)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
                    .padding(start = 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(it.imageVector, it.contentDescription)
                Text(it.label)
            }
        }
    }
}