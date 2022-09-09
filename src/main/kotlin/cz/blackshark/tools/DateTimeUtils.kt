package cz.blackshark.tools

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

object DateTimeUtils {

    fun getDateDiffInMinutes(from: ZonedDateTime, to: ZonedDateTime) = from.until(to, ChronoUnit.MINUTES)

    fun formatDateDiff(from: ZonedDateTime, to: ZonedDateTime, pause: Int = 0): String {
        val minutes = getDateDiffInMinutes(from, to) - pause
        return (minutes / 60).toString().padStart(2, '0') + ":" + (minutes % 60).toString().padStart(2, '0')
    }

    fun overlapping(from1: LocalDateTime, to1: LocalDateTime, from2: LocalDateTime, to2: LocalDateTime) =
        ((from1.isAfter(from2) || from1.isEqual(from2)) && from1.isBefore(to2)) ||
                (to1.isAfter(from2) && (to1.isBefore(to2) || to1.isEqual(to2)))

    fun getTimeDiffInHours(from: ZonedDateTime, to: ZonedDateTime, pause: Int): BigDecimal {
        val duration = Duration.between(from, to)
            .minusMinutes(pause.toLong())
        return BigDecimal(duration.toMinutes() / 60.0)
    }
}
