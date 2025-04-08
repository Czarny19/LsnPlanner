package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WavingHand
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppSizes

@Composable
fun WelcomeIcon(modifier: Modifier = Modifier) = Icon(
    Icons.Outlined.WavingHand,
    modifier = modifier.size(AppSizes.XL_ICON),
    contentDescription = stringResource(R.string.first_launch_welcome),
    tint = MaterialTheme.colorScheme.primary,
)

@Composable
fun SaveIcon() = Icon(Icons.Filled.Save, contentDescription = stringResource(R.string.save))

@Composable
fun BackIcon() = Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.go_back))

@Composable
fun OptionsMenuIcon(tint: Color = LocalContentColor.current) =
    Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.options), tint = tint)

@Composable
fun DropDownIcon(modifier: Modifier = Modifier, color: Color? = null) = Icon(
    Icons.Outlined.ArrowDropDown,
    modifier = modifier,
    contentDescription = stringResource(R.string.drop_down_arrow),
    tint = color ?: LocalContentColor.current
)

@Composable
fun InfoIcon(modifier: Modifier = Modifier) = Icon(
    Icons.Outlined.Info,
    contentDescription = stringResource(R.string.information),
    modifier = modifier.size(AppSizes.LG_ICON),
)

@Composable
fun ErrorIcon(modifier: Modifier = Modifier, color: Color? = null) = Icon(
    Icons.Filled.ErrorOutline,
    modifier = modifier,
    contentDescription = stringResource(R.string.error),
    tint = color ?: MaterialTheme.colorScheme.error
)

@Composable
fun SelectDateIcon() = Icon(Icons.Outlined.CalendarToday, stringResource(R.string.select_date))

@Composable
fun SelectTimeIcon() = Icon(Icons.Outlined.Schedule, stringResource(R.string.select_time))

@Composable
fun InputDropDownIcon(modifier: Modifier = Modifier, expanded: Boolean) = Icon(
    if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
    contentDescription = stringResource(R.string.show_options),
    modifier = modifier
)

@Composable
fun SetActiveIcon(contentDescription: String) = Icon(Icons.Outlined.CheckCircle, contentDescription = contentDescription)

@Composable
fun DeleteIcon(contentDescription: String = stringResource(R.string.delete)) = Icon(Icons.Outlined.Delete, contentDescription = contentDescription)

@Composable
fun AddIcon(contentDescription: String = stringResource(R.string.add_new)) = Icon(Icons.Filled.Add, contentDescription = contentDescription)

@Composable
fun AddFirstItemIcon() = Icon(
    Icons.Outlined.Add,
    contentDescription = stringResource(R.string.add_first_item),
    modifier = Modifier.size(AppSizes.XL_ICON),
    tint = MaterialTheme.colorScheme.primary
)

@Composable
fun PlanTypeIcon(modifier: Modifier = Modifier) =
    Icon(Icons.Filled.School, modifier = modifier, contentDescription = stringResource(R.string.plan_type))

@Composable
fun PlanCreateDateIcon(modifier: Modifier = Modifier) =
    Icon(Icons.Filled.EditCalendar, modifier = modifier, contentDescription = stringResource(R.string.plan_create_date))


@Composable
fun PlanActiveIcon(modifier: Modifier = Modifier, active: Boolean) = Icon(
    if (active) Icons.Filled.Check else Icons.Filled.Close,
    modifier = modifier,
    contentDescription = stringResource(if (active) R.string.plan_is_active else R.string.plan_is_not_active)
)

@Composable
fun PlanSelectedIcon(active: Boolean) = Icon(
    Icons.Filled.CheckCircle,
    contentDescription = stringResource(R.string.plan_active),
    tint = if (active) MaterialTheme.colorScheme.tertiary else Color.Transparent
)

@Composable
fun ClassIcon(contentDescription: String) = Icon(Icons.Filled.Class, contentDescription = contentDescription)