package com.lczarny.lsnplanner.presentation.components

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
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
    clickAction: () -> Unit,
    startAction: DraggableCardAction? = null,
    endAction: DraggableCardAction? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val density = LocalDensity.current
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    var contentHeight by remember { mutableStateOf(0.dp) }

    val dragState = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
        )
    }

    val anchors = remember(density) {
        val startOffset = with(density) { 100.dp.toPx() }
        val endOffset = with(density) { -100.dp.toPx() }

        DraggableAnchors {
            DragAnchors.Center at 0f
            startAction?.let { DragAnchors.Start at startOffset }
            endAction?.let { DragAnchors.End at endOffset }
        }
    }

    SideEffect {
        dragState.updateAnchors(anchors)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RectangleShape)
            .onGloballyPositioned { coordinates -> contentHeight = with(density) { coordinates.size.height.toDp() } }
    ) {
        Box(modifier = Modifier.anchoredDraggable(dragState, Orientation.Horizontal)) {
            DraggableCardActions(contentHeight, startAction, endAction)
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .offset {
                        IntOffset(
                            x = dragState
                                .requireOffset()
                                .roundToInt(), y = 0
                        )
                    }
                    .clickable { clickAction.invoke() },
                elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
                content = content
            )
        }
    }
}

@Composable
private fun DraggableCardActions(height: Dp, startAction: DraggableCardAction?, endAction: DraggableCardAction?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        startAction?.let {
            Card(
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight()
                    .clickable { it.onClick.invoke() },
                elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
                colors = CardDefaults.cardColors(containerColor = it.color)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.0f)
                        .padding(end = 50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = it.imageVector, contentDescription = it.contentDescription)
                    Text(text = it.label)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        endAction?.let {
            Card(
                modifier = Modifier
                    .width(150.dp)
                    .height(height)
                    .clickable {
                        Log.d("TEST", "click inside")
                        it.onClick.invoke()
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
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
                    Icon(imageVector = it.imageVector, contentDescription = it.contentDescription)
                    Text(text = it.label)
                }
            }
        }
    }
}