package bfahimi.nettyJ.server;

import bfahimi.nettyJ.model.MessageId;
import bfahimi.nettyJ.model.Request;
import bfahimi.nettyJ.model.Response;
import bfahimi.nettyJ.model.TransferState;

/**
 *
 */
public class DefaultRequestHandler implements RequestHandler {

    @Override
    public void handleRequest(Request request, RequestContext context) {
        System.out.println("HostAndPort-> Request Accepted: " + request);


        // ack the message
        MessageId messageId = new MessageId(request.getMessageId().getCorrelationId(), System.currentTimeMillis());
        context.reply(new Response(messageId, TransferState.OK));
    }
}
