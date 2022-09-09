package cz.blackshark.modules.main.dto

import java.math.BigDecimal
import java.time.LocalDate

data class WorkTimeDashboardVo(
    val date: LocalDate, // date of the data
    val hoursInMonth: Int, // number of work hours in the given month
    val hoursDone: BigDecimal // number of hours

)
