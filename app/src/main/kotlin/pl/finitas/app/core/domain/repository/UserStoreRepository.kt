package pl.finitas.app.core.domain.repository

import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.IdUserWithVisibleName
import pl.finitas.app.core.domain.dto.store.RegularSpendingDto
import pl.finitas.app.core.domain.dto.store.UserDto

interface UserStoreRepository {
    suspend fun addRegularSpendings(idUser: String, regularSpendings: List<RegularSpendingDto>)
    suspend fun upsertUser(dto: UserDto)
    suspend fun getNicknames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName>
    suspend fun getRegularSpendings(idUser: String): List<RegularSpendingDto>
}