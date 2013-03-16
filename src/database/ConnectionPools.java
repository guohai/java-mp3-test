package database;

import java.sql.SQLException;

import javax.sql.PooledConnection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionPools {

	private final static ComboPooledDataSource dataSource = new ComboPooledDataSource(
			); // "nimble_database_0x0001"

	public static PooledConnection getPooledConnection() {

		PooledConnection connection = null;
		try {
			connection = dataSource.getConnectionPoolDataSource()
					.getPooledConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
