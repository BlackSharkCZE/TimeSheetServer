package cz.blackshark.modules.main.beans

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

internal class WorkTimeUtilsTest {

    @Test
    fun getWorkTimeByMonth() {

        val utils = WorkTimeUtils()
        val now = LocalDate.now().withMonth(Month.APRIL.value)
        val workTimeByMonth = utils.getWorkTimeByMonth(now)
        println("Number of working days: $workTimeByMonth")
        println("Number of working hours: ${workTimeByMonth * 8}")

    }

}
