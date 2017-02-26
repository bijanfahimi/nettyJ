package bfahimi.nettyJ.model;

import java.io.Serializable;
import java.util.Random;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Data
@RequiredArgsConstructor
@SuppressWarnings("serial")
public class Message implements Serializable {

    private static final Random rand = new Random();

    private final MessageId messageId;

    public Message() {
        messageId = new MessageId(rand.nextLong(), System.currentTimeMillis());
    }
}
