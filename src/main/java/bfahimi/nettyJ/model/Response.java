package bfahimi.nettyJ.model;

import java.io.Serializable;

import lombok.ToString;

/**
 *
 */
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class Response extends Message implements Serializable{

    private TransferState state;

    public Response(MessageId messageId, TransferState state) {
        super(messageId);
        this.state = state;
    }
}
