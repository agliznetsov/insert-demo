package demo.shared;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkMeter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkMeter.class);

    public static void meter(Supplier<Long> a) {
        long before = System.currentTimeMillis();
        Long lines = a.get();
        long after = System.currentTimeMillis();
        long millis = (after - before);
        long rate = millis > 0 ? lines/millis : 0;
        LOGGER.info("Rows: {} Time: {} Rows/sec: {}", lines, millis, rate * 1000);
    }
}
