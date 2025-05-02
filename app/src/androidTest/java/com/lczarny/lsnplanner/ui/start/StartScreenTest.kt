package com.lczarny.lsnplanner.ui.start

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.repository.DataStoreRepository
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.presentation.model.SignInScreenState
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.signin.SignInScreen
import com.lczarny.lsnplanner.presentation.ui.signin.SignInViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class StartScreenTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    private lateinit var lessonPlanRepository: LessonPlanRepository

    @MockK
    private lateinit var dataStoreRepository: DataStoreRepository

    @Inject
    lateinit var lessonPlanDao: LessonPlanDao

    @Before
    fun init() {
        hiltRule.inject()
        MockKAnnotations.init(this)
        lessonPlanRepository = spyk<LessonPlanRepository>(LessonPlanRepository(lessonPlanDao))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testNormalLaunch() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = SignInViewModel(dispatcher, lessonPlanRepository, dataStoreRepository)

        coEvery { lessonPlanRepository.checkIfActivePlanExists() } returns true

        var context: Context? = null

        composeTestRule.setContent {
            context = LocalContext.current

            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            AppTheme {
                SignInScreen(navController, viewModel)
            }
        }

        advanceUntilIdle()

        composeTestRule.waitUntil { viewModel.screenState.value == SignInScreenState.StartApp }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testFirstLanch() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = SignInViewModel(dispatcher, lessonPlanRepository, dataStoreRepository)

        coEvery { dataStoreRepository.setUserName("UserName") } just Runs
        coEvery { lessonPlanRepository.checkIfActivePlanExists() } returns false

        var context: Context? = null

        launch {
            composeTestRule.setContent {
                context = LocalContext.current

                navController = TestNavHostController(LocalContext.current).apply {
                    navigatorProvider.addNavigator(ComposeNavigator())
                }

                AppTheme {
                    SignInScreen(navController, viewModel)
                }
            }
        }

        advanceUntilIdle()

        val welcomeMsg = composeTestRule.onNodeWithText(context!!.getString(R.string.first_launch_welcome))
        val userNameInput = composeTestRule.onNodeWithText(context.getString(R.string.user_name))
        val startButton = composeTestRule.onNodeWithText(context.getString(R.string.first_launch_create_plan))

        welcomeMsg.assertIsDisplayed()
        userNameInput.assertIsDisplayed()
        startButton.assertIsNotEnabled()

        userNameInput.performTextInput("UserName")

        composeTestRule.waitUntil { viewModel.startEnabled.value == true }

        startButton.assertIsEnabled()
        startButton.performClick()

        advanceUntilIdle()

        composeTestRule.waitUntil { viewModel.screenState.value == SignInScreenState.UserNameSaved }
    }
}