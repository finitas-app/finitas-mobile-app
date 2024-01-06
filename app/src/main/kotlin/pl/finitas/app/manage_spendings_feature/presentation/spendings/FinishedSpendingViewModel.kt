package pl.finitas.app.manage_spendings_feature.presentation.spendings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.domain.services.AuthorizedUserService
import pl.finitas.app.core.domain.services.FinishedSpendingView
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService
import pl.finitas.app.manage_spendings_feature.presentation.utils.extractIdRoomAndIdsUsers
import java.time.LocalDate
import java.util.UUID


class FinishedSpendingViewModel(
    private val finishedSpendingService: FinishedSpendingService,
    private val savedStateHandle: SavedStateHandle,
    private val authorizedUserService: AuthorizedUserService,
) : ViewModel() {
    val totalSpendings: Flow<List<Pair<LocalDate, List<FinishedSpendingView>>>>
    val idsUser: List<UUID>
    val idRoom: UUID?
    val authorities: Flow<Set<Authority>>

    val context get() = FinishedSpendingContext(idRoom, idsUser)

    init {
        val queryParams = savedStateHandle.extractIdRoomAndIdsUsers()
        idRoom = queryParams.first
        idsUser = queryParams.second

        authorities = idRoom
            ?.let { authorizedUserService.getAuthorityOfCurrentUser(it) }
            ?: flowOf(setOf())
        totalSpendings = finishedSpendingService.getTotalSpendings(idsUser)
    }
}

data class FinishedSpendingContext(
    val idRoom: UUID?,
    val idsUser: List<UUID>,
) {
    val isClear get() = idRoom == null && idsUser.isEmpty()
}
