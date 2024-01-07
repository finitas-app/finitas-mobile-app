package pl.finitas.app.manage_spendings_feature.presentation.add_spending

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.repository.SettingsRepository
import pl.finitas.app.core.domain.services.AuthorizedUserService
import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.domain.services.SpendingRecordView
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService
import pl.finitas.app.manage_spendings_feature.domain.service.ScanReceiptService
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.time.LocalDate
import java.util.UUID

class AddSpendingViewModel(
    private val spendingCategoryService: SpendingCategoryService,
    private val finishedSpendingService: FinishedSpendingService,
    private val scanReceiptService: ScanReceiptService,
    private val authorizedUserService: AuthorizedUserService,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    var isDialogOpen by mutableStateOf(false)
        private set

    var finishedSpendingState by mutableStateOf(FinishedSpendingState.emptyState)
        private set

    var titleErrors by mutableStateOf<List<String>?>(null)
        private set

    var errors by mutableStateOf<List<String>?>(null)
        private set

    var isImageParsing by mutableStateOf(false)
        private set

    fun openDialog(finishedSpendingView: FinishedSpendingView?, idUser: UUID?) {
        viewModelScope.launch {
            finishedSpendingState =
                if (finishedSpendingView == null) {
                    val categories =
                        spendingCategoryService.getSpendingCategoriesByIdUserFlat(idUser)
                    FinishedSpendingState(
                        categories = categories,
                        idUser,
                        settingsRepository.getDefaultCurrency().first()
                    )
                } else {
                    val categories =
                        spendingCategoryService.getSpendingCategoriesByIdUserFlat(idUser)
                    FinishedSpendingState(categories = categories, finishedSpendingView)
                }
            if (finishedSpendingState.categories.isEmpty()) {
                errors = if (finishedSpendingState.idUser == null) {
                    listOf("To add a spending, you must first create a category.")
                } else {
                    listOf(
                        "The user you are trying to add the spending to has no categories, you will" +
                                " only be able to do this after the user has added their first category."
                    )
                }
            }
            isDialogOpen = true
        }
    }

    fun closeDialog() {
        finishedSpendingState = FinishedSpendingState.emptyState
        isDialogOpen = false
    }

    fun setTitle(title: String) {
        finishedSpendingState = finishedSpendingState.copy(title = title)
    }

    fun setDate(date: LocalDate) {
        finishedSpendingState = finishedSpendingState.copy(date = date)
    }

    fun addSpending(spendingRecord: SpendingRecordView) {
        finishedSpendingState = finishedSpendingState.addSpending(spendingRecord)
    }

    fun removeSpending(spendingRecord: SpendingRecordView) {
        finishedSpendingState = finishedSpendingState.removeSpending(spendingRecord)
    }

    fun onSave() {
        viewModelScope.launch {
            try {
                finishedSpendingService.upsertFinishedSpending(finishedSpendingState)
                closeDialog()
            } catch (e: InputValidationException) {
                titleErrors = e.errors["title"]
                errors = e.errors[null]
            } catch (e: Exception) {
                errors = listOf("There's been a fatal error.")
            }
        }
    }

    fun setCurrency(currencyValue: CurrencyValue) {
        finishedSpendingState = finishedSpendingState.copy(currencyValue = currencyValue)
    }

    fun deleteFinishedSpending(idFinishedSpending: UUID) {
        viewModelScope.launch {
            try {
                finishedSpendingService.deleteFinishedSpending(idFinishedSpending)
                closeDialog()
            } catch (e: InputValidationException) {
                titleErrors = e.errors["title"]
                errors = e.errors[null]
            } catch (e: Exception) {
                errors = listOf("There's been a fatal error.")
            }
        }
    }

    fun processImage(file: ByteArray) {
        if (file.isNotEmpty()) {
            viewModelScope.launch {
                if (authorizedUserService.getAuthorizedIdUser().first() == null) {
                    errors = listOf("Check scanning is available only for authorized users.")
                    return@launch
                }

                isImageParsing = true
                try {
                    val currentCategories = finishedSpendingState.categories.toMutableList()
                    val firstCategory = currentCategories.removeFirst()
                    val firstCategoryWithParsedElements = firstCategory.copy(
                        elements = firstCategory.elements + scanReceiptService.scanReceipt(
                            file,
                            firstCategory.idCategory
                        )
                    )
                    finishedSpendingState = finishedSpendingState.copy(
                        categories = currentCategories.apply {
                            add(0, firstCategoryWithParsedElements)
                        }
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    errors = listOf("There's been a fatal error.")
                }
                isImageParsing = false
            }
        }
    }
}