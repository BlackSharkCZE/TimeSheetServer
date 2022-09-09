package cz.blackshark.modules.main.beans

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import javax.inject.Singleton

@Singleton
class WorkTimeUtils {

    /**
     * Get number of working days in the month of the given date
     * @return number of working days of the month
     */
    fun getWorkTimeByMonth(date: LocalDate): Int {
        var days = 0
        var current = date.withDayOfMonth(1)
        while (current.month == date.month) {
            if (current.dayOfWeek != DayOfWeek.SATURDAY && current.dayOfWeek != DayOfWeek.SUNDAY) {
                if (!isHoliday(current))
                    days++
            }
            current = current.plusDays(1)
        }
        return days
    }

    private fun isHoliday(date: LocalDate): Boolean {
        val easter = easterSunDay(date)
        return when {
            date.dayOfMonth == 1 && date.month == Month.JANUARY -> true
            date.dayOfMonth == 1 && date.month == Month.MAY -> true
            date.dayOfMonth == 8 && date.month == Month.MAY -> true
            date.dayOfMonth == 5 && date.month == Month.JULY -> true
            date.dayOfMonth == 6 && date.month == Month.JULY -> true
            date.dayOfMonth == 28 && date.month == Month.SEPTEMBER -> true
            date.dayOfMonth == 28 && date.month == Month.OCTOBER -> true
            date.dayOfMonth == 17 && date.month == Month.NOVEMBER -> true
            date.dayOfMonth == 24 && date.month == Month.DECEMBER -> true
            date.dayOfMonth == 25 && date.month == Month.DECEMBER -> true
            date.dayOfMonth == 26 && date.month == Month.DECEMBER -> true
            date == easter.plusDays(1) -> true
            date == easter.minusDays(2) -> true
            else -> false
        }
    }

    /**
     * Get easter sunday for the given year.
     * https://kalendar.beda.cz/vypocet-velikonocni-nedele?year=2029
     */
    private fun easterSunDay(date: LocalDate): LocalDate {
        val goldNumber = (date.year % 19) + 1
        val epakta = (goldNumber * 11) % 30
        val century = date.year / 100 + 1
        val sunCorrelation = ((century - 16) * 3) / 4
        val moonCorrelation = ((century - 15) * 8) / 25

        val step1 = epakta - 10 - sunCorrelation + moonCorrelation
        val gepakta = if (step1 < 0) step1 + 30; else if (step1 > 29) step1 - 30; else step1

        val pfm = when (gepakta) {
            in 0..23 -> 44 - gepakta
            24 -> 49
            25 -> if (goldNumber < 12) 49; else 48
            in 26..29 -> 74 - gepakta
            else -> throw RuntimeException("Invalid Gregorian epakta")
        }

        val cyclicFullMoon = if (pfm < 32) {
            LocalDate.of(date.year, Month.MARCH, pfm)
        } else {
            if (pfm > 31) {
                LocalDate.of(date.year, Month.APRIL, pfm - 31)
            } else {
                throw RuntimeException("Can not calculate Cyclic Full Moon")
            }
        }

//        val gfix = 10 + sunCorrelation
//        val day = (date.year + (date.year / 4) - gfix + pfm) % 7


        var calcMonn = cyclicFullMoon.plusDays(1)
        while (calcMonn.dayOfWeek != DayOfWeek.SUNDAY) {
            calcMonn = calcMonn.plusDays(1)
        }

        return calcMonn
    }

}
