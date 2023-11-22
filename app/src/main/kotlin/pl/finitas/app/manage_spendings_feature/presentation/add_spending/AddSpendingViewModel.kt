package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddSpendingViewModel: ViewModel() {

    private val _isDialogOpen = mutableStateOf(false)
    val isDialogOpen: State<Boolean> = _isDialogOpen

    fun openDialog() { _isDialogOpen.value = true }
    fun closeDialog() { _isDialogOpen.value = false }
}