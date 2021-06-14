package org.acme.resteasyjackson

import java.util.*

sealed class Entity(open val id: String = UUID.randomUUID().toString())

data class FooEntity(
    override val id: String = UUID.randomUUID().toString(),
    var name: String = ""
) : Entity()

class BarEntity(
    override val id: String = UUID.randomUUID().toString(),
    var code: Long = 0L,
) : Entity()