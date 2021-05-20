package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.shared.BenchmarkMeter;
import demo.shared.SimpleConnectionProvider;
import demo.shared.TableHelper;

public class InsertTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkMeter.class);

	final String properties = "src/main/resources/postgres.properties";
	final int batchSize = 50_000;
	final int totalCount = 5_000_000;

	public static void main(String[] args) throws Exception {
		new InsertTest().run();
	}

	private void run() throws Exception {
		Supplier<Connection> connectionSuplier = new SimpleConnectionProvider(properties);
		try (Connection connection = connectionSuplier.get()) {
			TableHelper.createTable(connection);
			connection.setAutoCommit(false);
			BenchmarkMeter.meter(() -> insertData(connection));
		}
	}

	private long insertData(Connection connection) {
		long start = System.currentTimeMillis();
		try (PreparedStatement pstmt = connection.prepareStatement(TableHelper.INSERT)) {
			for (int i = 1; i <= totalCount; i++) {
				TableHelper.setParameters(pstmt, i);
				pstmt.addBatch();
				if (i % batchSize == 0) {
					pstmt.executeBatch();
					connection.commit();
					long end = System.currentTimeMillis();
					LOGGER.info("Rows {} batch time: {}", i, (end - start));
					start = end;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return totalCount;
	}
}
