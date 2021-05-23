package demo.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkMeter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkMeter.class);

    static long start;

    public static void start() {
        start = System.currentTimeMillis();
    }

    public static void end(long rowCount, int threadCount) {
        long end = System.currentTimeMillis();
        long millis = (end - start);
        long rate = millis > 0 ? rowCount / millis : 0;
        LOGGER.info("Threads: {} Rows: {} Time: {} Rows/sec: {}",
                threadCount, rowCount, millis, rate * 1000);
    }
}
