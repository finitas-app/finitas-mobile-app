package pl.finitas.app.manage_spendings_feature.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingDialog
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartConstructorViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartDisplayViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.components.ChartConstructor
import pl.finitas.app.manage_spendings_feature.presentation.charts.components.ChartPanel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.FinishedSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.FinishedSpendingsPanel

@Composable
fun HomeScreen(
    navController: NavController,
) {
    val finishedSpendingViewModel: FinishedSpendingViewModel = koinViewModel()
    val addSpendingViewModel: AddSpendingViewModel = koinViewModel()
    val chartDisplayViewModel: ChartDisplayViewModel = koinViewModel()
    val chartConstructorViewModel: ChartConstructorViewModel = koinViewModel()
    val authorities by finishedSpendingViewModel.authorities.collectAsState(initial = null)
    val idsUser = remember { finishedSpendingViewModel.idsUser }
    var hasModifyAuthority by remember { mutableStateOf(false) }
    LaunchedEffect(authorities) {
        if (authorities != null) {
            if (idsUser.isNotEmpty() && Authority.READ_USERS_DATA !in authorities!!) {
                navController.popBackStack()
            }
            if (idsUser.isNotEmpty() && Authority.MODIFY_USERS_DATA !in authorities!!) {
                addSpendingViewModel.closeDialog()
            }
            hasModifyAuthority = idsUser.isEmpty() || Authority.MODIFY_USERS_DATA in authorities!!
        }
    }

    PrimaryBackground(
        isDialogOpen = addSpendingViewModel.isDialogOpen
                || chartConstructorViewModel.isChartConstructorDialogOpen
    ) {
        ChartPanel(
            chartDisplayViewModel = chartDisplayViewModel,
            chartConstructorViewModel = chartConstructorViewModel
        )
        FinishedSpendingsPanel(
            viewModel = finishedSpendingViewModel,
            onAddSpendingClick = addSpendingViewModel::openDialog,
            context = finishedSpendingViewModel.context,
            hasModifyAuthority = hasModifyAuthority,
        )
        if (finishedSpendingViewModel.context.isClear) {
            NavBar(navController = navController, backgroundColor = Colors.backgroundDark)
        } else {
            BackToRoomButton(navController = navController, backgroundColor = Colors.backgroundDark)
        }

    }
    AddSpendingDialog(addSpendingViewModel)
    ChartConstructor(constructorViewModel = chartConstructorViewModel)
}

@Composable
private fun BoxScope.BackToRoomButton(
    navController: NavController,
    backgroundColor: Color = Color(16, 16, 16),
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(140.dp)
            .align(Alignment.BottomCenter)
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, backgroundColor),
                    endY = with(LocalDensity.current) { 100.dp.toPx() }
                )
            )
    ) {
        ClickableIcon(
            imageVector = Icons.Rounded.ArrowBackIos,
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }

}