package com.lczarny.lsnplanner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lczarny.lsnplanner.presentation.ui.classdetails.ClassDetailsScreen
import com.lczarny.lsnplanner.presentation.ui.classlist.ClassListScreen
import com.lczarny.lsnplanner.presentation.ui.home.HomeScreen
import com.lczarny.lsnplanner.presentation.ui.lessonplan.LessonPlanScreen
import com.lczarny.lsnplanner.presentation.ui.lessonplanlist.LessonPlanListScreen
import com.lczarny.lsnplanner.presentation.ui.note.NoteScreen
import com.lczarny.lsnplanner.presentation.ui.signin.SignInScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any = SignInRoute,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<SignInRoute> {
            SignInScreen(navController)
        }

        composable<HomeRoute> { backStackEntry ->
            val homeRoute: HomeRoute = backStackEntry.toRoute()
            HomeScreen(navController, homeRoute.firstLaunch)
        }

        composable<LessonPlanListRoute> {
            LessonPlanListScreen(navController)
        }

        composable<LessonPlanRoute> { backStackEntry ->
            val lessonPlanRoute: LessonPlanRoute = backStackEntry.toRoute()
            LessonPlanScreen(navController, lessonPlanRoute.firstLaunch, lessonPlanRoute.lessonPlanId)
        }

        composable<ClassDetailsRoute> { backStackEntry ->
            val classRoute: ClassDetailsRoute = backStackEntry.toRoute()
            ClassDetailsScreen(navController, classRoute.defaultWeekDay, classRoute.classInfoId)
        }

        composable<ClassListRoute> { backStackEntry ->
            ClassListScreen(navController)
        }

        composable<NoteRoute> { backStackEntry ->
            val noteRoute: NoteRoute = backStackEntry.toRoute()
            NoteScreen(navController, noteRoute.noteId)
        }
    }
}