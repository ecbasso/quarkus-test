package org.acme.resteasyjackson

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import java.util.*

@JsonTypeInfo(use = NAME)
@JsonSubTypes(
    Type(value = FooEntityDTO::class, name = "FooEntityDTO"),
    Type(value = BarEntityDTO::class, name = "BarEntityDTO"),
)
sealed class EntityDTO(open val id: String = UUID.randomUUID().toString()) {

    abstract fun toDomain(): Entity

    companion object {
        operator fun invoke(entity: Entity) = when (entity) {
            is FooEntity -> FooEntityDTO(entity)
            is BarEntity -> BarEntityDTO(entity)
        }
    }
}

data class FooEntityDTO(
    override val id: String = UUID.randomUUID().toString(),
    var name: String = ""
) : EntityDTO() {
    constructor(entity: FooEntity) : this(id = entity.id, name = entity.name)

    override fun toDomain() = FooEntity(id = id, name = name)
}

class BarEntityDTO(
    override val id: String = UUID.randomUUID().toString(),
    var code: Long = 0L,
) : EntityDTO() {
    constructor(entity: BarEntity) : this(id = entity.id, code = entity.code)

    override fun toDomain() = BarEntity(id = id, code = code)
}

fun Entity.toDTO() = EntityDTO(this)