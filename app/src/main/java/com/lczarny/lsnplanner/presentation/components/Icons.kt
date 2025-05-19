package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.ArrowDropDownCircle
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SensorDoor
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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

object AppIcons {
    val WELCOME = Icons.Outlined.WavingHand

    val BACK = Icons.AutoMirrored.Filled.ArrowBack
    val NEXT = Icons.AutoMirrored.Filled.ArrowForward

    val SAVE = Icons.Filled.Save
    val ADD = Icons.Filled.Add
    val DELETE = Icons.Outlined.Delete
    val INFO = Icons.Outlined.Info
    val ERROR = Icons.Filled.ErrorOutline
    val OPTIONS = Icons.Filled.MoreVert
    val TUTORIAL = Icons.Outlined.QuestionMark
    val USER = Icons.Filled.Person
    val RESET = Icons.Filled.SettingsBackupRestore
    val SIGN_OUT = Icons.AutoMirrored.Filled.Logout

    val PASS_VISIBLE = Icons.Outlined.Visibility
    val PASS_HIDDEN = Icons.Outlined.VisibilityOff

    val ACTIVE = Icons.Filled.Check
    val INACTIVE = Icons.Filled.Close
    val SELECTED = Icons.Filled.CheckCircle

    val LIST = Icons.AutoMirrored.Filled.List
    val FAB_MENU = Icons.Filled.MoreHoriz
    val DROP_DOWN = Icons.Outlined.ArrowDropDownCircle
    val DROP_DOWN_NOT_EXPANDED = Icons.Filled.KeyboardArrowDown
    val DROP_DOWN_EXPANDED = Icons.Filled.KeyboardArrowUp

    val DATE = Icons.Outlined.CalendarToday
    val EDIT_DATE = Icons.Filled.EditCalendar
    val TIME = Icons.Outlined.Schedule
    val DURATION = Icons.Outlined.Timer
    val TIMELINE = Icons.Outlined.Timeline

    val LOCATION = Icons.Outlined.LocationOn
    val CLASSROOM = Icons.Outlined.SensorDoor

    val PLAN = Icons.Filled.School
    val CLASS = Icons.Filled.Class

    val HOME_CLASSES = Icons.Outlined.Class
    val HOME_CLASSES_SELECTED = Icons.Filled.Class
    val HOME_EVENTS = Icons.Outlined.Event
    val HOME_EVENTS_SELECTED = Icons.Filled.Event
    val HOME_NOTES = Icons.Outlined.NoteAlt
    val HOME_NOTES_SELECTED = Icons.Filled.NoteAlt
    val HOME_MORE = Icons.Outlined.Menu
    val HOME_MORE_SELECTED = Icons.Filled.Menu


    val CLASS_TYPE_CLASS = Icons.Filled.Class
    val CLASS_TYPE_EXTRA = Icons.Filled.PlusOne
    val CLASS_TYPE_LECTURE = Icons.AutoMirrored.Filled.LibraryBooks
    val CLASS_TYPE_PRACTICAL = Icons.Filled.DesignServices
    val CLASS_TYPE_LAB = Icons.Filled.Science
    val CLASS_TYPE_SEMINAR = Icons.Filled.School
    val CLASS_TYPE_WORKSHOP = Icons.Filled.Handyman
    val CLASS_TYPE_OTHER = Icons.Filled.Schedule

    val IMPORTANCE_NORMAL = Icons.Filled.LowPriority
    val IMPORTANCE_HIGH = Icons.Filled.WarningAmber
    val IMPORTANCE_VERY_HIGH = Icons.Filled.Warning
}

@Composable
fun SaveIcon(modifier: Modifier = Modifier) =
    Icon(AppIcons.SAVE, modifier = modifier, contentDescription = stringResource(R.string.save))

@Composable
fun BackIcon(modifier: Modifier = Modifier, contentDescription: String = stringResource(R.string.back)) =
    Icon(AppIcons.BACK, modifier = modifier, contentDescription = contentDescription)

@Composable
fun NextIcon(modifier: Modifier = Modifier, contentDescription: String = stringResource(R.string.next)) =
    Icon(AppIcons.NEXT, modifier = modifier, contentDescription = contentDescription)

@Composable
fun OptionsMenuIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) =
    Icon(AppIcons.OPTIONS, modifier = modifier, contentDescription = stringResource(R.string.options), tint = tint)

@Composable
fun DropDownIcon(modifier: Modifier = Modifier, color: Color? = null) = Icon(
    AppIcons.DROP_DOWN,
    modifier = modifier,
    contentDescription = stringResource(R.string.drop_down_arrow),
    tint = color ?: LocalContentColor.current
)

@Composable
fun SelectDateIcon(modifier: Modifier = Modifier) =
    Icon(AppIcons.DATE, modifier = modifier, contentDescription = stringResource(R.string.select_date))

@Composable
fun SelectTimeIcon(modifier: Modifier = Modifier) =
    Icon(AppIcons.TIME, modifier = modifier, contentDescription = stringResource(R.string.select_time))

@Composable
fun DeleteIcon(modifier: Modifier = Modifier, contentDescription: String = stringResource(R.string.delete)) =
    Icon(AppIcons.DELETE, modifier = modifier, contentDescription = contentDescription)

@Composable
fun AddIcon(modifier: Modifier = Modifier, contentDescription: String = stringResource(R.string.add_new)) =
    Icon(AppIcons.ADD, modifier = modifier, contentDescription = contentDescription)

@Composable
fun AddFirstItemIcon(modifier: Modifier = Modifier) = Icon(
    AppIcons.ADD,
    modifier = modifier.size(AppSizes.XL_ICON),
    contentDescription = stringResource(R.string.add_first_item),
    tint = MaterialTheme.colorScheme.primary
)