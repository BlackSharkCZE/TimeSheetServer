package cz.blackshark.modules.main.persistence

object Sql {

	object Invoice {
		val SELECT_INVOICE_HEADER = """
			select sum(income) as amount, ord.cust_number, ord.note
			from (
			         select EXTRACT(epoch FROM work_time) / 3600 * (array_agg(r.rate order by valid_since desc))[1] / 8.0 as income,
			                vt.*
			         from v_timeline_agg vt
			                  join rate r on vt.company_id = r.company_id
			         where r.valid_since < from_time
			           and vt.from_time >= :fromDate
			           and vt.to_time < :toDate
			           and vt.company_id = :companyID
			           and vt.invoice_item_id is null
			         group by vt.company_id, vt.company_name, vt.project_id, vt.name, vt.timeline_id, vt.from_time, vt.invoice_item_id,
			                  vt.to_time, vt.work_time
			         order by vt.from_time
			     ) xxx
			         join (
			    select cust_number, note, company_id from requisition where company_id = 2 order by start_date desc limit 1
			) ord on ord.company_id = xxx.company_id
			group by ord.cust_number, ord.note
		""".trimIndent()

		/**
		 * Params:
		 * :companyID
		 * :fromTime
		 * :toTime
		 */
		val SELECT_INVOICE_SOURCE = """select max(r.rate) as rate,
       tl.timeline_id as timeline_id,
       tl.name as project_name,
       EXTRACT(epoch FROM work_time) / 3600 as work_time,
       tl.company_id,
       tl.project_id,
       cast(tl.from_time as date) as work_day,
       cast(tl.from_time as time) as work_since,
       cast(tl.to_time as time) as work_until


from v_timeline_agg tl
join rate r on r.company_id = tl.company_id and r.valid_since < from_time
where tl.company_id = :companyID
  and cast(from_time as date) >= cast(:fromDate as date)
  and cast(from_time as date) <= cast(:toDate as date)
and invoice_item_id is null
group by tl.timeline_id, work_time, tl.company_id, tl.project_id,tl.from_time,
         tl.to_time,tl.name
order by tl.from_time, to_time""".trimIndent()

	}

	object WorkTime {
		val EARNING = """select sum(income) as price,
       xxx.company_id,
       xxx.company_name,
       EXTRACT(epoch FROM sum(xxx.work_time)) / 3600 as work_time
from (
         select EXTRACT(epoch FROM work_time) / 3600 * (array_agg(r.rate order by valid_since desc))[1] / 8.0 as income,
                vt.*
         from v_timeline_agg vt
                  join rate r on vt.company_id = r.company_id
         where r.valid_since < from_time
           and cast(vt.from_time as date) >= :fromTime
           and cast(vt.to_time as date) <= :toTime
		   and vt.subject_id = :subject
         group by vt.company_id, vt.company_name, vt.project_id, vt.name, vt.timeline_id, vt.from_time, vt.invoice_item_id,
                  vt.to_time, vt.work_time, vt.subject_id
         order by vt.from_time
     ) xxx
group by xxx.company_id, xxx.company_name;""".trimIndent()

		//TODO this was EARNING_BY_INVOICE - it is little different case
		val EARNING_BY_INVOICE_ITEM = """select sum(income) as price,
       xxx.company_id,
       xxx.company_name,
       EXTRACT(epoch FROM sum(xxx.work_time)) / 3600 as work_time
from (
         select EXTRACT(epoch FROM work_time) / 3600 * (array_agg(r.rate order by valid_since desc))[1] / 8.0 as income,
                vt.*
         from v_timeline_agg vt
                  join rate r on vt.company_id = r.company_id
         where r.valid_since < from_time
           and vt.invoice_item_id = :invoiceNumber
         group by vt.company_id, vt.company_name, vt.project_id, vt.name, vt.timeline_id, vt.from_time, vt.invoice_item_id,
                  vt.to_time, vt.work_time
         order by vt.from_time
     ) xxx
group by xxx.company_id, xxx.company_name""".trimIndent()


		const val WORK_TIME = "SELECT EXTRACT(epoch FROM (select sum(to_time - from_time - make_interval(mins=>pause)) as work_time_sum\n" +
			"from timeline\n join project p on timeline.project_id = p.id\n " +
			"where from_time >= :fromTime\n" +
			"and from_time < :toTime)) / 3600"

		const val WORK_TIME_FOR_COMPANY = """
			SELECT EXTRACT(epoch FROM (
    select sum(to_time - from_time - make_interval(mins=>pause)) as work_time_sum
    from timeline
             join project p on timeline.project_id = p.id
    where from_time >= :fromTime
      and from_time < :toTime
      and p.company_id = :companyId
)) / 3600
		"""

	}


	object Timeline {
		const val MIN_YEAR_IN_TIMELINE = "select to_char(min(from_time),'YYYY') as min_year from timeline"

		//		const val MAX_TIME_IN_DATE = "select to_char(max(to_time), 'HH24:MI') as max_time from timeline where cast(to_time as date) = :date"
		const val MAX_TIME_IN_DATE = "select max(to_time) as max_time from timeline where cast(to_time as date) = :date"
		val DAILY_EARNING = """select sum(x.income) as income, sum(x.work_time) as work_time
from (
         select max(r.rate) / 8 * EXTRACT(epoch FROM work_time) / 3600 as income,
                EXTRACT(epoch FROM work_time) / 3600                   as work_time
         from v_timeline_agg tl
                  join rate r on r.company_id = tl.company_id and r.valid_since < from_time
         where cast(from_time as date) = cast(:day as date)
         group by tl.timeline_id, work_time, tl.company_id, tl.project_id, tl.from_time,
                  tl.to_time, tl.name) x""".trimIndent()
	}

}
