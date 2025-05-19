package com.lczarny.lsnplanner.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.model.BasicScreenState
import com.lczarny.lsnplanner.model.TabBarItemWithIcon
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.SuccessSnackbar
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanRoute
import com.lczarny.lsnplanner.presentation.navigation.SignInRoute
import com.lczarny.lsnplanner.presentation.ui.home.components.HomeScreenSnackbar
import com.lczarny.lsnplanner.presentation.ui.home.components.HomeSnackbar
import com.lczarny.lsnplanner.presentation.ui.home.tab.classes.ClassesTab
import com.lczarny.lsnplanner.presentation.ui.home.tab.classes.ClassesTabActions
import com.lczarny.lsnplanner.presentation.ui.home.tab.classes.ClassesTabFab
import com.lczarny.lsnplanner.presentation.ui.home.tab.more.MoreTab
import com.lczarny.lsnplanner.presentation.ui.home.tab.notes.NotesTab
import com.lczarny.lsnplanner.presentation.ui.home.tab.notes.NotesTabFab
import com.lczarny.lsnplanner.utils.getMonthName
import kotlinx.coroutines.channels.Channel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        when (screenState) {
            BasicScreenState.Loading -> FullScreenLoading(stringResource(R.string.please_wait))
            BasicScreenState.Ready -> HomeTabs(navController, viewModel)
        }
    }
}

@Composable
private fun HomeTabs(navController: NavController, viewModel: HomeViewModel) {
    val context = LocalContext.current

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()

    if (lessonPlan == null) {
        navigateToLessonPlanCreation(navController)
        return
    }

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val sessionActive by viewModel.sessionActive.collectAsStateWithLifecycle()

    if (sessionActive.not() || userProfile == null) {
        navigateToSignIn(navController)
        return
    }

    val classesCurrentDate by viewModel.classesCurrentDate.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarChannel = remember { Channel<HomeScreenSnackbar>(Channel.CONFLATED) }

    HomeSnackbar(snackbarHostState, snackbarChannel)

    val classesTab = TabBarItemWithIcon(
        id = "Classes",
        label = stringResource(R.string.home_tab_classes),
        selectedIcon = AppIcons.HOME_CLASSES_SELECTED,
        unselectedIcon = AppIcons.HOME_CLASSES,
    )
    val eventsTab = TabBarItemWithIcon(
        id = "Events",
        label = stringResource(R.string.home_tab_events),
        selectedIcon = AppIcons.HOME_EVENTS_SELECTED,
        unselectedIcon = AppIcons.HOME_EVENTS
    )
    val notesTab = TabBarItemWithIcon(
        id = "Notes",
        label = stringResource(R.string.home_tab_notes),
        selectedIcon = AppIcons.HOME_NOTES_SELECTED,
        unselectedIcon = AppIcons.HOME_NOTES
    )
    val moreTab = TabBarItemWithIcon(
        id = "More",
        label = stringResource(R.string.home_tab_more),
        selectedIcon = AppIcons.HOME_MORE_SELECTED,
        unselectedIcon = AppIcons.HOME_MORE
    )

    val tabBarItems by lazy { listOf(classesTab, eventsTab, notesTab, moreTab) }

    val bottomBarNavController = rememberNavController()
    val classesPagerState = rememberPagerState(initialPage = classesCurrentDate.dayOfWeek.value - 1) { 7 }

    val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { SuccessSnackbar(it) } },
        topBar = {
            AppNavBar(
                title = when (currentRoute) {
                    classesTab.id -> "${classesCurrentDate.month.getMonthName(context)} ${classesCurrentDate.year}"
                    eventsTab.id -> stringResource(R.string.home_tab_events)
                    notesTab.id -> stringResource(R.string.home_tab_notes)
                    moreTab.id -> stringResource(R.string.home_tab_more)
                    else -> ""
                },
                navIconVisible = false,
                actions = {
                    when (currentRoute) {
                        classesTab.id -> ClassesTabActions(viewModel)
                    }
                }
            )
        },
        bottomBar = { TabView(tabBarItems, bottomBarNavController) },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            when (currentRoute) {
                notesTab.id -> NotesTabFab(navController)
                classesTab.id -> ClassesTabFab(viewModel, navController, classesPagerState)
            }
        }
    ) { padding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(navController = bottomBarNavController, startDestination = classesTab.id) {
                composable(classesTab.id) { ClassesTab(padding, viewModel, lessonPlan!!, classesPagerState) }
                composable(eventsTab.id) { Text(eventsTab.id) }
                composable(notesTab.id) { NotesTab(padding, navController, viewModel) }
                composable(moreTab.id) { MoreTab(padding, navController, viewModel, lessonPlan!!, userProfile!!, snackbarChannel) }
            }
        }
    }
}

@Composable
private fun TabView(tabBarItems: List<TabBarItemWithIcon>, navController: NavController) {
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
                label = { Text(tabBarItem.label) },
                icon = {
                    Icon(
                        if (selectedTabIndex == index) tabBarItem.selectedIcon else tabBarItem.unselectedIcon,
                        contentDescription = tabBarItem.label
                    )
                }
            )
        }
    }
}

private fun navigateToLessonPlanCreation(navController: NavController) {
    navController.navigate(LessonPlanRoute(firstPlan = true))
}

private fun navigateToSignIn(navController: NavController) {
    navController.navigate(SignInRoute) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}