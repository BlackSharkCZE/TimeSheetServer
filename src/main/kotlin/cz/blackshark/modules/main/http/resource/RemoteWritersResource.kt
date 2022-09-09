package cz.blackshark.modules.main.http.resource

import cz.blackshark.modules.main.beans.RemoteWriterBean
import cz.blackshark.modules.main.persistence.entity.RemoteWriteSettingsEntity
import org.eclipse.microprofile.graphql.Description
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query
import javax.inject.Inject

@GraphQLApi
class RemoteWritersResource @Inject constructor(private val remoteWriterBean: RemoteWriterBean) {


    @Query("allConfigs")
    @Description("Get all saved remote writer configs")
    fun getAllConfigs(): List<RemoteWriteSettingsEntity> = remoteWriterBean.findAll()

    @Mutation("upsertConfig")
    @Description("Create new config")
    fun upsertConfig(item: RemoteWriteSettingsEntity): RemoteWriteSettingsEntity {
        return remoteWriterBean.upsert(item)
    }

}
