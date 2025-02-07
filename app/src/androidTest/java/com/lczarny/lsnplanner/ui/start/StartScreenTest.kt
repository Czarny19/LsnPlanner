package com.lczarny.lsnplanner.ui.start

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.start.StartScreen
import com.lczarny.lsnplanner.presentation.ui.start.StartViewModel
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock

class StartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    val mockNavController: NavController = mock(NavController::class.java)

    @Mock
    val mockLessonPlanRepository: LessonPlanRepository = mock(LessonPlanRepository::class.java)

    @Test
    fun testLoading() {
        val viewModel = StartViewModel(mockLessonPlanRepository)

        composeTestRule.setContent {
            AppTheme {
                StartScreen(mockNavController, viewModel)
            }
        }

        composeTestRule.onNodeWithText("Please wait…").assertIsDisplayed()
    }

    @Test
    fun testFirstLanch() {
        fun checkIfDefaultFlow() = flow {
            emit(false)
        }

        Mockito.`when`(mockLessonPlanRepository.checkIfDefaultPlanExists()).thenReturn(checkIfDefaultFlow())

        val viewModel = StartViewModel(mockLessonPlanRepository)

        composeTestRule.setContent {
            AppTheme {
                StartScreen(mockNavController, viewModel)
            }
        }

        composeTestRule.onNodeWithText("Create your first plan").assertIsDisplayed()
    }
}