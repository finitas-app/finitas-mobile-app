package pl.finitas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending.RegularSpendingActualizationViewModel
import pl.finitas.app.navigation.AppNavigation
import pl.finitas.app.sync_feature.SynchronizationViewModel

@OptIn(KoinExperimentalAPI::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoinAndroidContext {
                koinViewModel<SynchronizationViewModel>()
                koinViewModel<RegularSpendingActualizationViewModel>()
                AppNavigation()
            }
        }
    }
}
