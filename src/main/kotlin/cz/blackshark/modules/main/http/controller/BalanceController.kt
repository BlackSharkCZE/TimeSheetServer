package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.BalanceBean
import cz.blackshark.modules.main.persistence.entity.BalanceView
import io.quarkus.security.Authenticated
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("balance")
@Authenticated
class BalanceController : AbstractBaseController() {

    @Inject
    private lateinit var balanceBean: BalanceBean

    @GET
    fun getAllBalanceForPrimaryCompany(): RestResponse<List<BalanceView>> {
        return RestResponse(true, null, balanceBean.getBalance())
    }

}
