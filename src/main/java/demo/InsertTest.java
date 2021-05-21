package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
	final int totalCount = 60_000_000;
	final int threadCount = 1;
	final ExecutorService executor = Executors.newCachedThreadPool();
	Supplier<Connection> connectionSupplier;

	public static void main(String[] args) throws Exception {
		new InsertTest().run();
	}

	private void run() throws Exception {
		connectionSupplier = new SimpleConnectionProvider(properties);
		try (Connection connection = connectionSupplier.get()) {
//			TableHelper.createTable(connection, "/create-table-full.sql");
//			TableHelper.createTable(connection, "/create-table-short.sql");
			TableHelper.createTable(connection, "/create-table-only.sql");
		}

		BenchmarkMeter.start();
		List<Future> futures = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			futures.add(executor.submit(this::insertData));
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

	private long insertData() throws Exception {
		LOGGER.info("Thread started");
		long start = System.currentTimeMillis();
		Connection connection = connectionSupplier.get();
		connection.setAutoCommit(false);
//		PreparedStatement pstmt = connection.prepareStatement(TableHelper.INSERT_SHORT);
		PreparedStatement pstmt = connection.prepareStatement(TableHelper.INSERT);
		for (int i = 1; i <= totalCount / threadCount; i++) {
//			TableHelper.setParametersShort(pstmt, i);
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
		pstmt.executeBatch();
		connection.commit();
		pstmt.close();
		connection.close();
		LOGGER.info("Thread stopped");
		return totalCount;
	}
}
