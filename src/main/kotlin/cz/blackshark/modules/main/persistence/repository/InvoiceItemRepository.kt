package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.CompanyEntity
import cz.blackshark.modules.main.persistence.entity.InvoiceItemEntity
import cz.blackshark.modules.main.persistence.dao.KotlinDbUtils
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
open class InvoiceItemRepository : PanacheRepositoryBase<InvoiceItemEntity, Long> {

	@Inject
	protected lateinit var kotlinDbUtils: KotlinDbUtils

	fun updatePriceFromTimeLine(requisitionItem: InvoiceItemEntity, issuerEntity: CompanyEntity) {

		val priceQuery = "select sum(income) as price\n" +
				"from (\n" +
				"         select EXTRACT(epoch FROM work_time) / 3600 * (array_agg(r.rate order by valid_since desc))[1] / 8.0 as income,\n" +
				"                vt.*\n" +
				"         from v_timeline_agg vt\n" +
				"                  join rate r on vt.company_id = r.company_id\n" +
				"         where r.valid_since < from_time\n" +
				"           and vt.invoice_item_id = :invoiceItemID\n" +
				"         group by vt.company_id, vt.company_name, vt.project_id, vt.name, vt.timeline_id, vt.from_time,\n" +
				"                  vt.invoice_item_id,\n" +
				"                  vt.to_time, vt.work_time\n" +
				"         order by vt.from_time\n" +
				"     ) xxx\n" +
				"group by xxx.company_id, xxx.company_name"

		val price:BigDecimal = kotlinDbUtils.loadData(priceQuery, mapOf("invoiceItemID" to requisitionItem.id!!)) {
			it.getBigDecimal("price")
		}.getOrElse(0) { _ -> BigDecimal.ZERO }

		requisitionItem.price = price
		if (issuerEntity.platceDph) {
			requisitionItem.totalPrice = price.times(requisitionItem.vat)
		} else {
			requisitionItem.totalPrice = price
		}

		persistAndFlush(requisitionItem)

	}


}
