package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.ares.http.client.AresClientV2
import cz.blackshark.modules.main.converter.CompanyMapper
import cz.blackshark.timesheet.commons.domain.CompanyVo
import org.eclipse.microprofile.rest.client.inject.RestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("ares")
class AresController(
    @RestClient val aresClient2: AresClientV2,
) {

    @GET
    @Path("{ico}")
    fun searchByIco(@PathParam("ico") ico: String): CompanyVo {
        return CompanyMapper.convert(aresClient2.vratEkonomickySubjekt(ico))
//        return CompanyMapper.convert(aresClient.basicQuery(ico))
    }
}
