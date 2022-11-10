package cz.blackshark.modules.main.persistence.repository

import cz.blackshark.modules.main.persistence.entity.InvoiceNumberEntity
import cz.blackshark.modules.main.persistence.dao.KotlinDbUtils
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.ResultSet
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
open class InvoiceNumberRepository @Inject constructor(
	val kotlinDbUtils: KotlinDbUtils

) : PanacheRepositoryBase<InvoiceNumberEntity, Long> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(InvoiceNumberRepository::class.java)
	}

	/**
	 * Return new invoice number for the company
	 */
	fun nextInvoiceNumber(companyID: Long): InvoiceNumberEntity? {
		val connection = kotlinDbUtils.datasource.connection

		try {
//			connection.autoCommit = false
			val pstm = connection.prepareStatement(
				"select prefix, company_id, current_val from invoice_number where company_id=? for update",
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE
			)
			pstm.setLong(1, companyID)
			var i: InvoiceNumberEntity? = null
			val res = pstm.executeQuery()
			if (res.next()) {
				i = InvoiceNumberEntity()
				i.prefix = res.getLong("prefix")
				i.currentValue = res.getLong("current_val")
				i.companyID = res.getLong("company_id")
				res.updateLong("current_val", i.currentValue + 1)
				res.updateRow()
			}
//			connection.commit()
			pstm.close()
			res.close()
			return i
		} catch (e: Exception) {
			logger.error("Can not read new invoice number!", e)
			connection.rollback()
		} finally {
			connection.close()
		}
		return null;
	}

}
