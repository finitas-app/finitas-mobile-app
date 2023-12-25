package pl.finitas.app.profile_feature.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.constructors.ConstructorBox
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.profile_feature.presentation.components.CurrencyInput
import pl.finitas.app.profile_feature.presentation.components.LogoutButton
import pl.finitas.app.profile_feature.presentation.components.ReminderSettings
import pl.finitas.app.profile_feature.presentation.components.RepeatingSpendingsSetting
import pl.finitas.app.profile_feature.presentation.components.SeparatorLine
import pl.finitas.app.profile_feature.presentation.components.SignInButton
import pl.finitas.app.profile_feature.presentation.components.UsernameInput

@Composable
fun ProfileScreen(
    navController: NavController,
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val isAuthorized by viewModel.isAuthorize.collectAsState(initial = false)

    PrimaryBackground {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 40.dp)
                .padding(top = 50.dp, bottom = 100.dp)
                .align(Alignment.Center)
                .fillMaxSize()
        ) {
            Column {
                Fonts.heading1.Text(text = "Profile")
                ConstructorBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    postModifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Column {
                        val modifier = Modifier.padding(vertical = 20.dp)

                        if (isAuthorized) {
                            UsernameInput(viewModel = viewModel, modifier = modifier)
                        } else {
                            SignInButton(navController = navController, modifier = modifier)
                        }

                        SeparatorLine()
                        CurrencyInput(viewModel = viewModel, modifier = modifier)
                        SeparatorLine()
                        ReminderSettings(viewModel = viewModel, modifier = modifier)
                        SeparatorLine()
                        RepeatingSpendingsSetting(viewModel = viewModel, modifier = modifier)

                        if (isAuthorized) {
                            SeparatorLine()
                            LogoutButton(viewModel = viewModel, modifier = modifier)
                        }
                    }
                }
            }
        }
        NavBar(navController = navController)
    }
}


// todo: align logout button to right
// todo: review time input functionalities
// todo: align currency dropdown to right

// todo: Dania - configure currency dropdown
// todo: Dania - add return button to auth screen
// todo: Dania - improve currency dropdown appearance
