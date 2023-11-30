package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.DelayedRequest
import pl.finitas.app.core.data.model.RequestMethod

data class RequestMethodToDelayedRequest(
    @Embedded
    val requestMethod: RequestMethod,
    @Relation(
        parentColumn = "idRequestMethod",
        entityColumn = "idRequestMethod"
    )
    val delayedRequests: List<DelayedRequest>,
)