package cz.blackshark.modules.main.http.resource

import cz.blackshark.modules.main.beans.RemoteWriterBean
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/remote-writer")
class RemoteWritersResource @Inject constructor(private val remoteWriterBean: RemoteWriterBean) {

    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllSupportedTypes(): List<String> = remoteWriterBean.getSupportedWriters()

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllConfigs(): List<RemoteWriteSettingsEntity> = remoteWriterBean.findAll()

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun upsertConfig(item: RemoteWriteSettingsEntity): RemoteWriteSettingsEntity {
        return remoteWriterBean.upsert(item)
    }

}
