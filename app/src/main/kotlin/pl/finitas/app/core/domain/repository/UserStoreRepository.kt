package pl.finitas.app.core.domain.repository

import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.IdRegularSpending
import pl.finitas.app.core.domain.dto.store.IdUserWithVisibleName
import pl.finitas.app.core.domain.dto.store.RegularSpendingDto
import pl.finitas.app.core.domain.dto.store.UserDto
import pl.finitas.app.core.domain.dto.store.VisibleName

interface UserStoreRepository {
    suspend fun addRegularSpendings(idUser: String, regularSpendings: List<RegularSpendingDto>)
    suspend fun getVisibleNames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName>
    suspend fun patchVisibleName(visibleName: VisibleName)
    suspend fun getRegularSpendings(idUser: String): List<RegularSpendingDto>
    suspend fun deleteRegularSpending(idRegularSpending: IdRegularSpending)
}