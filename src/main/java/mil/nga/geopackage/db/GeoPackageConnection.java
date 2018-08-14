package mil.nga.geopackage.db;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.j256.ormlite.support.ConnectionSource;

/**
 * GeoPackage Connection wrapper
 * 
 * @author osbornb
 */
public class GeoPackageConnection extends GeoPackageCoreConnection {

	/**
	 * Logger
	 */
	private static final Logger log = Logger
			.getLogger(GeoPackageConnection.class.getName());

	/**
	 * Name column
	 */
	private static final String NAME_COLUMN = "name";

	/**
	 * GeoPackage file
	 */
	private final File file;

	/**
	 * Connection
	 */
	private final Connection connection;

	/**
	 * Connection source
	 */
	private final ConnectionSource connectionSource;

	/**
	 * Constructor
	 *
	 * @param file
	 *            file
	 * @param connection
	 *            connection
	 * @param connectionSource
	 *            connection source
	 */
	public GeoPackageConnection(File file, Connection connection,
			ConnectionSource connectionSource) {
		this.file = file;
		this.connection = connection;
		this.connectionSource = connectionSource;
	}

	/**
	 * Get the connection
	 *
	 * @return connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execSQL(String sql) {
		SQLUtils.execSQL(connection, sql);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(String table, String whereClause, String[] whereArgs) {
		return SQLUtils.delete(connection, table, whereClause, whereArgs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(String table, String where, String[] args) {
		return SQLUtils.count(connection, table, where, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer min(String table, String column, String where, String[] args) {
		return SQLUtils.min(connection, table, column, where, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer max(String table, String column, String where, String[] args) {
		return SQLUtils.max(connection, table, column, where, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		connectionSource.closeQuietly();
		try {
			connection.close();
		} catch (SQLException e) {
			log.log(Level.WARNING, "Failed to close GeoPackage connection to: "
					+ file.getAbsolutePath(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean columnExists(String tableName, String columnName) {

		boolean exists = false;

		ResultSet result = query(
				"PRAGMA table_info(" + CoreSQLUtils.quoteWrap(tableName) + ")",
				null);
		try {
			while (result.next()) {
				String name = result.getString(NAME_COLUMN);
				if (columnName.equals(name)) {
					exists = true;
					break;
				}
			}
		} catch (SQLException e) {
			log.log(Level.WARNING, "Failed to search table info: " + tableName
					+ ", looking for column: " + columnName, e);
		} finally {
			try {
				result.close();
			} catch (SQLException e) {
				log.log(Level.WARNING,
						"Failed to close result set to table info: "
								+ tableName, e);
			}
		}

		return exists;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String querySingleStringResult(String sql, String[] args) {
		return SQLUtils.querySingleStringResult(connection, sql, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer querySingleIntResult(String sql, String[] args) {
		return SQLUtils.querySingleIntResult(connection, sql, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> querySingleColumnStringResults(String sql, String[] args) {
		return SQLUtils.querySingleColumnStringResults(connection, sql, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String[]> queryStringResults(String sql, String[] args) {
		return SQLUtils.queryStringResults(connection, sql, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] querySingleRowStringResults(String sql, String[] args) {
		return SQLUtils.querySingleRowStringResults(connection, sql, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String[]> queryStringResults(String sql, String[] args,
			Integer limit) {
		return SQLUtils.queryStringResults(connection, sql, args, limit);
	}

	/**
	 * Query for a single result object
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single result object
	 * @since 3.0.2
	 */
	public Object querySingleObjectResult(String sql, String[] args) {
		return SQLUtils.querySingleObjectResult(connection, sql, args);
	}

	/**
	 * Query for values from a single column
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            sql arguments
	 * @return single column values
	 * @since 3.0.2
	 */
	public List<Object> querySingleColumnResults(String sql, String[] args) {
		return SQLUtils.querySingleColumnResults(connection, sql, args);
	}

	/**
	 * Perform a database query
	 * 
	 * @param sql
	 *            sql statement
	 * @param args
	 *            arguments
	 * @return result set
	 * @since 1.1.2
	 */
	public ResultSet query(String sql, String[] args) {
		return SQLUtils.query(connection, sql, args);
	}

}
