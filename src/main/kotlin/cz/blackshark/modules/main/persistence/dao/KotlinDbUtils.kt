package cz.blackshark.modules.main.persistence.dao

import io.agroal.api.AgroalDataSource
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.sql.*
import java.text.MessageFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.Date
import java.util.regex.Pattern
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject


/**
 * Created 31.10.17 11:00
 *
 * @author Jiri Pejsa
 *
 */
@ApplicationScoped
class KotlinDbUtils() {

	@Inject
	lateinit var datasource: AgroalDataSource

	private val p = Pattern.compile(":\\w+", Pattern.CASE_INSENSITIVE)

	companion object {
		/**
		 * Configured logger for class [KotlinDbUtils]
		 */
		val logger: Logger = LoggerFactory.getLogger(KotlinDbUtils::class.java)

		@Throws(SQLException::class)
		fun setLocalDateTime(statement: PreparedStatement, index: Int, localDateTime: LocalDateTime?) {
			if (localDateTime != null && index > 0) {
				statement.setTimestamp(index, Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
			}
		}

		@Throws(SQLException::class)
		fun setLocalDate(statement: PreparedStatement, index: Int, date: LocalDate?) {
			if (date != null && index>0) {
				statement.setDate(index, java.sql.Date(Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).time))
			}
		}

		@Throws(SQLException::class)
		fun getLocalDateTime(rs: ResultSet, columnName: String): LocalDateTime? {
			val ts = rs.getTimestamp(columnName)
			return if (rs.wasNull()) {
				null
			} else {
				LocalDateTime.from(ts.toInstant().atZone(ZoneId.systemDefault()))
			}
		}

		@Throws(SQLException::class)
		fun getBoolean(rs: ResultSet, columnName: String): Boolean? {
			val b = rs.getBoolean(columnName)
			return if (rs.wasNull()) {
				null
			} else {
				b
			}
		}

		@Throws(SQLException::class)
		fun getBoolean(rs: ResultSet, columnName: String, converter: (String) -> Boolean?): Boolean? {
			val s = rs.getString(columnName)
			return if (rs.wasNull()) {
				null
			} else {
				converter(s)
			}
		}

		@Throws(SQLException::class)
		fun getDate(rs: ResultSet, columnName: String): Date? {
			val timestamp = rs.getTimestamp(columnName)
			return if (timestamp != null) {
				Date(timestamp.time)
			} else {
				null
			}
		}

		@Throws(SQLException::class)
		fun getLong(rs: ResultSet, columnName: String): Long? {
			val aLong = rs.getLong(columnName)
			return if (rs.wasNull()) null; else aLong
		}

		@Throws(SQLException::class)
		fun getString(rs: ResultSet, columnName: String): String? {
			val str = rs.getString(columnName)
			return if (rs.wasNull()) null; else str

		}

		@Throws(SQLException::class)
		fun getInt(rs: ResultSet, columnName: String): Int? {
			return try {
				val value = rs.getInt(columnName)
				return if (rs.wasNull()) null; else value
			} catch (e: SQLException) {
				logger.error("Can not get column value Type: Integer from column name $columnName")
				null
			}
		}

		@Throws(SQLException::class)
		fun blobToString(blob: Blob?): String? {
			return if (blob != null) {
				try {
					String(IOUtils.toByteArray(blob.binaryStream))
				} catch (e: IOException) {
					null
				}
			} else {
				null
			}
		}

		@Throws(SQLException::class)
		fun setLong(statement: PreparedStatement?, i: Int, longVal: Long?) {
			if (statement != null) {
				if (longVal != null) {
					statement.setLong(i, longVal)
				} else {
					statement.setNull(i, Types.INTEGER)
				}
			}
		}

		@Throws(SQLException::class)
		fun setString(statement: PreparedStatement?, i: Int, strVal: String?) {
			if (statement != null) {
				if (strVal != null) {
					statement.setString(i, strVal)
				} else {
					statement.setNull(i, Types.VARCHAR)
				}
			}
		}


	}

	fun processData(query: String, params: Map<String, Any?>, processor: (ResultSet) -> Unit): Unit {
		val connection = datasource.connection
		try {
			buildPreparedStatement(connection, query, params).use { pstm ->
				val resultSet = pstm.executeQuery()
				if (resultSet != null) {
					while (resultSet.next()) {
						processor.invoke(resultSet)
					}
				}
			}
		} catch (e: Exception) {
			logger.error("Can not process resultSet for query {} ", query, e)
		} finally {
			connection.close()
		}
	}

	/**
	 * Load data from database by given sql query and parameters
	 * @param query SQL query with optionals parameter (defined as :paramName)
	 * @param params map of parameters for query
	 * @param mapper closure mapping loaded result set to required object
	 */
	fun <T> loadData(query: String, params: Map<String, Any?>, mapper: (ResultSet) -> T): List<T> {
		val connection = datasource.connection

		try {
			buildPreparedStatement(connection, query, params).use { pstm ->
				val resultSet = pstm.executeQuery()
				if (resultSet != null) {
					val res = mutableListOf<T>()
					try {
						while (resultSet.next()) {
							res.add(mapper.invoke(resultSet))
						}
					} catch (e: SQLException) {
						logger.error("Can not read data by query $query and params $params", e)
					} finally {
						return res
					}
				}
				return listOf()
			}
		} catch (e: Exception) {
			logger.error("Can not read data by query $query and params $params", e)
			return listOf()
		} finally {
			connection.close()
		}


	}

	fun execute(command: String, params: Map<String, Any?>, connection: Connection): Int {
		var res: Int
		buildPreparedStatement(connection, command, params).use { pstm ->
			res = pstm.executeUpdate()
		}
		return res
	}

	fun execute(command: String, params: Map<String, Any?>): Int {
		return execute(command, params, datasource.connection)
	}

	fun <T> withTransaction(block: (KotlinDbUtils, Connection) -> T, params: Map<String, Any?>): T {
		val con = datasource.connection
		con.autoCommit = false
		try {
			val x: T = block.invoke(this, con)
			con.commit()
			return x
		} catch (e: Exception) {
			logger.error("Error processing block with transaction", e)
			if (!con.isClosed) {
				con.rollback()
			}
			throw e
		} finally {
			con.close()
		}
	}


	@Throws(SQLException::class)
	private fun buildPreparedStatement(conn: Connection, query: String, params: Map<String, Any?>): PreparedStatement {

		val keyIndexMap = HashMap<Int, String>(5)
		var i = 1
		var matcher = p.matcher(query)

		while (matcher.find()) {
			val key = matcher.group().substring(1)
			val obj: Any? = params[key];
			if (params.containsKey(key) && obj != null) {
				when (obj) {
					is List<*> -> {
						matcher = p.matcher(query.replace(":$key", "?,".repeat(obj.size).removeSuffix(",")))
						keyIndexMap[i++] = key
						i += obj.size - 1
					}
					else -> keyIndexMap[i++] = key
				}
			}

		}
		val jdbcQuery = matcher.replaceAll("?")

		val pstm = conn.prepareStatement(jdbcQuery)

		if (params.isNotEmpty()) {
			keyIndexMap.entries.forEach { e ->
				val paramValue = params[e.value]
				val paramIndex = e.key
				if (paramValue != null) {
					if (paramValue is Date) {
						try {
							pstm.setTimestamp(paramIndex, Timestamp(paramValue.time))
						} catch (e1: SQLException) {
							logger.error(MessageFormat.format("Can not set parameter {0} on index {1} with value {2}", e.value, paramIndex, paramValue), e)
						}

					} else {

						when (paramValue) {
							is LocalDate -> {
								try {
									setLocalDate(pstm, paramIndex, paramValue)
								} catch (e1: SQLException) {
									logger.error("Can not set LocalDate into parameter {} fro key {}", paramIndex, e.key)
								}
							}
							is LocalDateTime -> {
								try {
									setLocalDateTime(pstm, paramIndex, paramValue as LocalDateTime?)
								} catch (e1: SQLException) {
									logger.error("Can not set LocalDateTime into parameter {} for key {}", paramIndex, e.key)
								}
							}
							is List<*> -> {
								paramValue.forEachIndexed { index, value ->
									pstm.setObject(paramIndex + index, value)
								}
							}
							else -> {
								try {
									pstm.setObject(paramIndex, paramValue)
								} catch (e1: SQLException) {
									logger.error(MessageFormat.format("Can not set parameter {0} on index {1} with value {2}", e.value, paramIndex, paramValue), e)
								}
							}
						}


					}
				} else {
					try {
						pstm.setNull(paramIndex, Types.NULL)
					} catch (e1: SQLException) {
						logger.error(MessageFormat.format("Can not set parameter {0} to NULL on index {1}", e.value, paramIndex), e)
					}

				}
			}
		}

		return pstm
	}

}
