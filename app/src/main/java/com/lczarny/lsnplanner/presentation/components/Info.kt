package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes

@Composable
fun InfoCard(modifier: Modifier = Modifier, text: String, iconVisible: Boolean = true) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.CARD_ELEVATION),
        shape = RoundedCornerShape(AppSizes.ITEM_SHAPE_CORNER_RAD),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppPadding.SCREEN_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            if (iconVisible) InfoIcon(modifier = Modifier.padding(end = AppPadding.SCREEN_PADDING))
            Text(text, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Start)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfoChip(modifier: Modifier = Modifier, label: String, imageVector: ImageVector) {
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .shadow(4.dp, shape)
            .clip(shape)
            .background(color = MaterialTheme.colorScheme.background)
            .widthIn(max = 160.dp)
    ) {
        Icon(
            imageVector,
            modifier = Modifier
                .padding(start = AppPadding.SM_PADDING, top = AppPadding.XSM_PADDING, bottom = AppPadding.XSM_PADDING)
                .size(AppSizes.SM_ICON),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            label,
            modifier = Modifier.padding(vertical = AppPadding.XSM_PADDING, horizontal = AppPadding.SM_PADDING),
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AddFirstItemInfo(
    modifier: Modifier = Modifier,
    label: String,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppPadding.SCREEN_PADDING),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddFirstItemIcon()

        Text(
            label,
            modifier = Modifier.padding(vertical = AppPadding.MD_PADDING),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        PrimaryButton(
            modifier = Modifier.padding(vertical = AppPadding.MD_PADDING),
            text = buttonLabel,
            onClick = onClick
        )
    }
}