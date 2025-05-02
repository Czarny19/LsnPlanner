package com.lczarny.lsnplanner.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppPadding

data class FabMenuItem(val visible: Boolean = true, val label: String, val imageVector: ImageVector, val onClick: () -> Unit)

@Composable
fun FabMenu(items: List<FabMenuItem>) {
    var filterFabExpanded by rememberSaveable { mutableStateOf(false) }
    val changeExpanded = { filterFabExpanded = !filterFabExpanded }

    val transition = updateTransition(targetState = filterFabExpanded, label = stringResource(R.string.fab_menu_transition))

    val fabRotationDegree by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 150, easing = FastOutSlowInEasing) },
        label = stringResource(R.string.fab_menu_rotation),
        targetValueByState = { if (it) 90f else 0f }
    )

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING, Alignment.Bottom)
    ) {
        FabMenuItems(visible = filterFabExpanded, items = items.filter { it.visible }, onClick = changeExpanded)

        FloatingActionButton(modifier = Modifier.rotate(fabRotationDegree), onClick = changeExpanded) {
            Icon(AppIcons.FAB_MENU, contentDescription = stringResource(R.string.more))
        }
    }
}

@Composable
private fun FabMenuItems(visible: Boolean, items: List<FabMenuItem>, onClick: () -> Unit) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        )
    }

    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(0, easing = FastOutSlowInEasing)
        ) + fadeOut(
            animationSpec = tween(0, easing = FastOutSlowInEasing)
        )
    }

    AnimatedVisibility(visible = visible, enter = enterTransition, exit = exitTransition) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING),
        ) {
            items.forEach { menuItem -> FabMenuOption(menuItem = menuItem, onClick = onClick) }
        }
    }
}


@Composable
private fun FabMenuOption(menuItem: FabMenuItem, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = {
            onClick.invoke()
            menuItem.onClick.invoke()
        },
        elevation = FloatingActionButtonDefaults.elevation(0.dp),
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary
    ) {
        Text(menuItem.label, modifier = Modifier.padding(horizontal = AppPadding.MD_PADDING))
        Icon(menuItem.imageVector, contentDescription = menuItem.label)
    }
}