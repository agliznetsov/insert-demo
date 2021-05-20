package demo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.function.Supplier;

import org.postgresql.copy.CopyManager;
import org.postgresql.jdbc.PgConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.shared.BenchmarkMeter;
import demo.shared.SimpleConnectionProvider;
import demo.shared.TableHelper;

public class CopyTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkMeter.class);

	final String properties = "src/main/resources/postgres.properties";
	final int batchSize = 50_000;
	final int totalCount = 5_000_000;

	public static void main(String[] args) throws Exception {
		new CopyTest().run();
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
		try {
			PgConnection unwrapped = connection.unwrap(PgConnection.class);
			CopyManager copyManager = unwrapped.getCopyAPI();
			StringBuilder sb = new StringBuilder();

			for (int i = 1; i <= totalCount; i++) {
				TableHelper.addRow(sb, i);
				if (i % batchSize == 0) {
					InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
					copyManager.copyIn(TableHelper.COPY, is);
					connection.commit();

					sb.setLength(0);
					long end = System.currentTimeMillis();
					LOGGER.info("Rows {} batch time: {}", i, (end - start));
					start = end;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return totalCount;
	}
}
