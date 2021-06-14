package org.acme.resteasyjackson

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.random.Random

@Path("/entities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EntitiesResource {

    private val entities = mutableListOf(
        FooEntity(id = "123", name = "Foo"),
        BarEntity(id = "654", code = Random.nextLong(until = 500)),
        FooEntity(id = "456", name = "Another Foo"),
        BarEntity(id = "987", code = Random.nextLong(from = 500, until = 1000)),
    )

    @GET
    @Path("/{$ID_PATH_PARAM}")
    fun find(
        @PathParam(ID_PATH_PARAM) id: String,
        @QueryParam(SUMMARY_QUERY_PARAM) summary: Boolean = false
    ): Any? = entities.find { entity -> entity.id == id }?.map(summary = summary) ?: throw NotFoundException()

    @GET
    fun list(@QueryParam(SUMMARY_QUERY_PARAM) summary: Boolean = true): Response =
        Response.ok(if (summary) entities.map { it.toSummaryDTO() }.toTypedArray() else entities.map { it.toDTO() }.toTypedArray()).build()

    @POST
    fun add(entity: EntityDTO): List<Any> {
        entities.add(entity.unmap())
        return entities.mappedAsDTO()
    }

    @DELETE
    @Path("/{$ID_PATH_PARAM}")
    fun delete(@PathParam(ID_PATH_PARAM) id: String): List<Any> {
        entities.removeIf { existingEntity: Entity -> existingEntity.id == id }
        return entities.mappedAsDTO(summary = false)
    }

    private fun Entity.map(summary: Boolean = false): Any = if (summary) toSummaryDTO() else toDTO()

    private fun EntityDTO.unmap() = toDomain()

    private fun Collection<Entity>.mappedAsDTO(summary: Boolean = true) =
        this.map { entity -> entity.map(summary) }

    companion object {
        private const val ID_PATH_PARAM = "id"
        private const val SUMMARY_QUERY_PARAM = "summary"
    }

}


