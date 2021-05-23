package demo;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
	final int totalCount = 150_000_000;
	final int threadCount = 4;
	final ExecutorService executor = Executors.newCachedThreadPool();
	Supplier<Connection> connectionSupplier;

	public static void main(String[] args) throws Exception {
		new CopyTest().run();
	}

	private void run() throws Exception {
		connectionSupplier = new SimpleConnectionProvider(properties);
		try (Connection connection = connectionSupplier.get()) {
			TableHelper.createTable(connection, "/create-table-only.sql");
		}

		BenchmarkMeter.start();
		List<Future> futures = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			boolean print = i == 0;
			futures.add(executor.submit(() -> insertData(print)));
		}
		futures.forEach(this::resolveFuture);
		BenchmarkMeter.end(totalCount, threadCount);
		System.exit(0);
	}

	private void resolveFuture(Future future) {
		try {
			future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long insertData(boolean print) throws Exception {
		LOGGER.info("Thread started");
		final long start = System.currentTimeMillis();
		long batchStart = start;
		Connection connection = connectionSupplier.get();
		connection.setAutoCommit(false);

		PgConnection unwrapped = connection.unwrap(PgConnection.class);
		CopyManager copyManager = unwrapped.getCopyAPI();
		StringBuilder sb = new StringBuilder();

		for (int i = 1; i <= totalCount / threadCount; i++) {
			TableHelper.addRow(sb, i);
			if (i % batchSize == 0) {
				ByteArrayInputStream is = new ByteArrayInputStream(sb.toString().getBytes());
				copyManager.copyIn(TableHelper.COPY, is);
				connection.commit();
				sb.setLength(0);

				long now = System.currentTimeMillis();
				long batchTime = (now - batchStart);
				long totalTime = (now - start) / 1000;
				batchStart = now;
				if (print) {
					System.out.println(i * threadCount + "," + batchTime + "," + totalTime);
				}
			}
		}

		if (sb.length() > 0) {
			ByteArrayInputStream is = new ByteArrayInputStream(sb.toString().getBytes());
			copyManager.copyIn(TableHelper.COPY, is);
			connection.commit();
		}

		connection.close();
		LOGGER.info("Thread stopped");
		return totalCount;
	}
}
