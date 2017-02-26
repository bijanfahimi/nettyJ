package bfahimi.nettyJ.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 */
@Data
@AllArgsConstructor
@SuppressWarnings("serial")
public class MessageId implements Serializable{
    private long correlationId;
    private long timestamp;

}
