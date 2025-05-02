package com.lczarny.lsnplanner.presentation.ui.signin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.model.SignInScreenState
import com.lczarny.lsnplanner.presentation.navigation.HomeRoute
import com.lczarny.lsnplanner.presentation.ui.signin.components.SignInForm

@Composable
fun SignInScreen(navController: NavController, viewModel: SignInViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        when (screenState) {
            SignInScreenState.Loading -> FullScreenLoading()
            SignInScreenState.SignIn -> SignInForm(viewModel)
            SignInScreenState.Done -> navigateToHome(navController)
        }
    }
}

private fun navigateToHome(navController: NavController) {
    navController.navigate(HomeRoute) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}