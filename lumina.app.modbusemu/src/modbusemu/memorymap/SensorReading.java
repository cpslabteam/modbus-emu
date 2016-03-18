package modbusemu.memorymap;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * The Class SensorReading.
 */
public class SensorReading
        implements Delayed {

    /**
     * Delay time.
     */
    private final long delayTime;
    /**
     * The reading value.
     */
    private final long reading;

    public SensorReading(long timestamp, long reading) {
        this.delayTime = timestamp;
        this.reading = reading;
    }

    public long getReading() {
        return reading;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (delayTime > ((SensorReading) o).delayTime) {
            return 1;
        } else if (delayTime == ((SensorReading) o).delayTime) {
            return 0;
        } else {
            return -1;
        }
    }
}
