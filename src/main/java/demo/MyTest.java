package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.shared.BenchmarkMeter;

public class MyTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkMeter.class);

	public static void main(String[] args) throws Exception {
		int[] data = new int[] {
				1000000, 44693,
				2000000, 56972,
				4000000, 82571,
				2000000, 39850,

				1000000, 12142,
				2000000, 15452,
				4000000, 22860,
				8000000, 51520};

		for (int i = 0; i < data.length; i += 2) {
			double rows = data[i];
			double millis = data[i + 1];

			double assetSec = rows / 10 * 1000 / millis;
			double assetHour = assetSec * 60 * 60 / 1_000_000;

			LOGGER.info("rows {} millis {} MAssets/h {}", rows, millis, (int) assetHour);
		}
	}


}
