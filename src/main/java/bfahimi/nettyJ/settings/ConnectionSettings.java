package bfahimi.nettyJ.settings;

import lombok.Data;

/**
 *
 */
@Data
public class ConnectionSettings {

    private int lowWaterMarkInBytes = 2 * 1024 * 1024;
    private int highWaterMarkInBytes = 4 * 1024 * 1024;

    private int fromPort = 50_000;
    private int toPort = 60_000;

    /**
     * Seconds in which the server must respond with an okay, or an exception is raised.
     */
    private long serverRequestAckTimeoutInSec = 10;

    /**
     * Max. number of messages that may pend before flushing the channel.
     */
    private int maxPendingMessages = 256;

    /**
     * Max nano seconds that the flusher may wait until it flushes its buffer.
     */
    private long maxDelayInNanos = 100;

}
