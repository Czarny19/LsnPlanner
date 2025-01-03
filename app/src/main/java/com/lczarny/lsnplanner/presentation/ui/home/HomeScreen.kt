package com.lczarny.lsnplanner.presentation.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.LessonPlanWithClassesModel
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.SuccessSnackbar
import com.lczarny.lsnplanner.presentation.navigation.ToDoRoute
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.home.model.HomeState
import com.lczarny.lsnplanner.presentation.ui.home.more.MoreTab
import com.lczarny.lsnplanner.presentation.ui.home.todos.ToDosTab
import com.lczarny.lsnplanner.utils.getDayOfWeekDisplayValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class TabBarItem(
    val id: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

enum class HomeScreenSnackbar {
    FirstLaunch,
    DeleteHistoricalToDos,
}

@Composable
fun HomeScreen(navController: NavController, firstLaunch: Boolean, viewModel: HomeViewModel = hiltViewModel()) {
    val screenState by viewModel.screenState.collectAsState()
    val lessonPlan by viewModel.lessonPlan.collectAsState()

    AppTheme(
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                content = {
                    when (screenState) {
                        HomeState.Loading -> FullScreenLoading()
                        HomeState.Ready -> HomeTabs(navController, firstLaunch, lessonPlan!!, viewModel)
                    }
                }
            )
        }
    )
}

@Composable
fun HomeTabs(navController: NavController, firstLaunch: Boolean, lessonPlan: LessonPlanWithClassesModel, viewModel: HomeViewModel) {
    val context = LocalContext.current

    val firstLaunchDone by viewModel.firstLaunchDone.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarChannel = remember { Channel<HomeScreenSnackbar>(Channel.CONFLATED) }

    LaunchedEffect(snackbarChannel) {
        snackbarChannel.receiveAsFlow().collect {
            when (it) {
                HomeScreenSnackbar.FirstLaunch -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_create_first_plan),
                    withDismissAction = true
                )

                HomeScreenSnackbar.DeleteHistoricalToDos -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.todo_delete_all_historical_snackbar),
                    withDismissAction = true
                )
            }
        }
    }

    if (firstLaunch && firstLaunchDone.not()) {
        LaunchedEffect(true) {
            snackbarChannel.trySend(HomeScreenSnackbar.FirstLaunch)
            viewModel.setFirstLaunchDone()
        }
    }

    val classesTab = TabBarItem(
        id = "Classes",
        label = stringResource(R.string.home_tab_classes),
        selectedIcon = Icons.Filled.Class,
        unselectedIcon = Icons.Outlined.Class,
    )
    val calendarTab = TabBarItem(
        id = "Calendar",
        label = stringResource(R.string.home_tab_calendar),
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth
    )
    val toDoTab = TabBarItem(
        id = "ToDo",
        label = stringResource(R.string.home_tab_to_dos),
        selectedIcon = Icons.Filled.NoteAlt,
        unselectedIcon = Icons.Outlined.NoteAlt
    )
    val moreTab = TabBarItem(
        id = "More",
        label = stringResource(R.string.home_tab_more),
        selectedIcon = Icons.Filled.Menu,
        unselectedIcon = Icons.Outlined.Menu
    )

    val tabBarItems = listOf(classesTab, calendarTab, toDoTab, moreTab)
    val bottomBarNavController = rememberNavController()

    val showHistoricalToDos by viewModel.showHistoricalToDos.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { SuccessSnackbar(it) } },
        topBar = {
            val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            AppNavBar(
                title = when (currentRoute) {
                    classesTab.id -> "${getDayOfWeekDisplayValue(context)} (${lessonPlan.plan.name})"
                    calendarTab.id -> stringResource(R.string.home_tab_calendar)
                    toDoTab.id -> stringResource(R.string.home_tab_to_dos)
                    else -> stringResource(R.string.home_tab_more)
                },
                actions = {
                    when (currentRoute) {
                        toDoTab.id -> Row {
                            IconButton(onClick = { viewModel.switchShowHistoricalToDos() }) {
                                Icon(
                                    imageVector = if (showHistoricalToDos) Icons.Filled.CheckBox else Icons.Outlined.CheckBox,
                                    contentDescription = stringResource(R.string.todo_show_done)
                                )
                            }
                        }

                        else -> null
                    }
                }
            )
        },
        bottomBar = { TabView(tabBarItems, bottomBarNavController) },
        floatingActionButton = {
            val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute == toDoTab.id) {
                FloatingActionButton(
                    onClick = { navController.navigate(ToDoRoute(lessonPlan.plan.id!!)) },
                    content = { Icon(Icons.Filled.Add, stringResource(R.string.todo_add)) }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            NavHost(navController = bottomBarNavController, startDestination = classesTab.id) {
                composable(classesTab.id) {
                    Text(classesTab.id)
                }
                composable(calendarTab.id) {
                    Text(calendarTab.id)
                }
                composable(toDoTab.id) {
                    val toDos by viewModel.toDos.collectAsState()
                    ToDosTab(padding, viewModel, navController, lessonPlan.plan.id!!, toDos, showHistoricalToDos)
                }
                composable(moreTab.id) {
                    MoreTab(padding, viewModel, snackbarChannel)
                }
            }
        }
    )
}

@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    unselectedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                ),
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.id)
                },
                label = { Text(text = tabBarItem.label) },
                icon = {
                    Icon(
                        imageVector = if (selectedTabIndex == index) tabBarItem.selectedIcon else tabBarItem.unselectedIcon,
                        contentDescription = tabBarItem.id
                    )
                }
            )
        }
    }
}