package com.lczarny.lsnplanner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lczarny.lsnplanner.presentation.ui.home.HomeScreen
import com.lczarny.lsnplanner.presentation.ui.lessonplan.LessonPlanScreen
import com.lczarny.lsnplanner.presentation.ui.planclass.PlanClassScreen
import com.lczarny.lsnplanner.presentation.ui.start.StartScreen
import com.lczarny.lsnplanner.presentation.ui.todo.ToDoScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any = StartRoute,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<StartRoute> { StartScreen(navController) }

        composable<HomeRoute> { backStackEntry ->
            val home: HomeRoute = backStackEntry.toRoute()
            HomeScreen(navController, home.firstLaunch)
        }

        composable<LessonPlanRoute> { backStackEntry ->
            val lessonPlan: LessonPlanRoute = backStackEntry.toRoute()
            LessonPlanScreen(navController, lessonPlan.firstLaunch, lessonPlan.lessonPlanId)
        }

        composable<PlanClassRoute> { backStackEntry ->
            val planClass: PlanClassRoute = backStackEntry.toRoute()
            PlanClassScreen(
                navController,
                planClass.lessonPlanId,
                planClass.lessonPlanType,
                planClass.defaultWeekDay,
                planClass.planClassId
            )
        }

        composable<ToDoRoute> { backStackEntry ->
            val toDo: ToDoRoute = backStackEntry.toRoute()
            ToDoScreen(navController, lessonPlanId = toDo.lessonPlanId, classId = toDo.classId, toDoId = toDo.toDoId)
        }
    }
}