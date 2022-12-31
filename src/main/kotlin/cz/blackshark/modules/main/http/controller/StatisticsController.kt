package cz.blackshark.modules.main.http.controller

import cz.blackshark.modules.main.beans.StatisticsBean
import cz.blackshark.modules.main.exceptions.TsException
import cz.blackshark.modules.main.persistence.entity.EarnHistoryView
import io.quarkus.security.Authenticated
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("statistics")
@Authenticated
class StatisticsController : AbstractBaseController() {

    @Inject
    private lateinit var statisticsBean: StatisticsBean

    @Throws(TsException::class)
    @GET
    fun getEarnStats(): List<EarnHistoryView> = statisticsBean.findForPrimaryCompany(retrieveSubject())

}
