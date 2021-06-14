package org.acme.resteasyjackson

import java.util.*

data class EntitySummaryDTO(val id: String = UUID.randomUUID().toString()) {
    constructor(entity: Entity) : this(id = entity.id)
}

fun Entity.toSummaryDTO() = EntitySummaryDTO(this)