package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.RateBean
import cz.blackshark.modules.main.dto.RateVo
import cz.blackshark.modules.main.exceptions.TsException
import io.quarkus.security.Authenticated
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.logging.Logger
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext

@Path("rate")
@Authenticated
@RequestScoped
class RateController @Inject constructor(
    private val logger: Logger,
    private val rateBean: RateBean,

    ) {

    @Inject
    private lateinit var jwt: JsonWebToken

    @POST
    @Path("/create")
    @Throws(TsException::class)
    @Transactional
    fun creteRate(@Context context: SecurityContext, rateVo: RateVo): RateVo {
        return rateBean.createRate(rateVo, jwt.subject!!)
    }

    @GET
    @Path("/list")
    @Throws(TsException::class)
    fun getAllUserRates(): List<RateVo> {
        return rateBean.findForSubject(jwt.subject!!)
    }


}
