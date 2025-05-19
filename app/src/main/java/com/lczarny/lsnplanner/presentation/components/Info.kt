package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes

@Composable
@Stable
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
                .padding(AppPadding.MD_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            if (iconVisible) Icon(
                AppIcons.INFO,
                modifier = Modifier
                    .padding(end = AppPadding.MD_PADDING)
                    .size(AppSizes.LG_ICON),
                contentDescription = stringResource(R.string.information),
            )

            Text(text, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Start)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Stable
fun InfoChip(modifier: Modifier = Modifier, label: String, imageVector: ImageVector, color: Color? = null) {
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .shadow(4.dp, shape)
            .clip(shape)
            .background(color = MaterialTheme.colorScheme.background)
            .widthIn(max = 160.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector,
            modifier = Modifier
                .padding(start = AppPadding.SM_PADDING, top = AppPadding.XSM_PADDING, bottom = AppPadding.XSM_PADDING)
                .size(AppSizes.SM_ICON),
            contentDescription = label,
            tint = color ?: MaterialTheme.colorScheme.secondary
        )
        Text(
            label,
            modifier = Modifier.padding(vertical = AppPadding.XSM_PADDING, horizontal = AppPadding.SM_PADDING),
            style = MaterialTheme.typography.bodySmall.copy(color = color ?: MaterialTheme.colorScheme.secondary),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}