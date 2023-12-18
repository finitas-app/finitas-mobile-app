package pl.finitas.app.sync_feature.data.data_source

import pl.finitas.app.core.data.data_source.dao.VersionsDao
import pl.finitas.app.sync_feature.domain.repository.VersionsRepository

class VersionRepositoryImpl(
    private val versionsDao: VersionsDao,
): VersionsRepository by versionsDao