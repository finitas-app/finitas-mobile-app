package pl.finitas.app.manage_spendings_feature.presentation.charts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import pl.finitas.app.core.domain.services.AuthorizedUserService
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartWithCategoriesDto
import pl.finitas.app.manage_spendings_feature.domain.service.ChartService
import pl.finitas.app.manage_spendings_feature.presentation.utils.extractIdRoomAndIdsUsers
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ChartDisplayViewModel(
    private val service: ChartService,
    private val savedStateHandle: SavedStateHandle,
    private val authorizedUserService: AuthorizedUserService,
) : ViewModel() {
    val charts: Flow<List<ChartWithCategoriesDto>>
    val idRoom: UUID?
    val idsUser: List<UUID>
    init {
        val queryParams = savedStateHandle.extractIdRoomAndIdsUsers()
        idRoom = queryParams.first
        idsUser = queryParams.second
        charts = when(idsUser.size) {
            0 -> service.getChartsWithCategoriesFlow(null, null)
            1 -> {
                authorizedUserService.getAuthorizedIdUser().flatMapLatest {
                    val idUser = if (idsUser[0] == it) null else idsUser[0]
                    service.getChartsWithCategoriesFlow(null, idUser)
                }
            }
            else -> service.getChartsWithCategoriesFlow(idRoom, null)
        }
    }
}