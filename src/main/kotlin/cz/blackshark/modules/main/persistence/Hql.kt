package cz.blackshark.modules.main.persistence

object Hql {

	const val FIND_TIMELINE_WITH_DATE = "SELECT e FROM TimelineEntity e WHERE e.fromTime>=:fromDate AND e.toTime < :toDate ORDER BY e.fromTime DESC"
	const val FIND_TIMELINE_BETWEEN = "select tl from TimelineEntity tl where (tl.fromTime between :fromTime AND :toTime) or (tl.toTime between :fromTime AND :toTime)"
	const val COUNT_BY_USED_TIME = "fromTime < :time AND toTime > :time"
	const val COUNT_BY_USED_TIME_WITH_ID_EXCEPTION = "fromTime < :time AND toTime > :time AND id != :id"

}
