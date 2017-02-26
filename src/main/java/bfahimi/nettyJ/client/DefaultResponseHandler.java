package bfahimi.nettyJ.client;

import bfahimi.nettyJ.commons.MessageEmitter;
import bfahimi.nettyJ.model.Response;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultResponseHandler implements ResponseHandler {

    private final MessageEmitter messageSender;

    @Override
    public void handleResponse(Response response, Channel channel) {
        log.info("Client->Respone: {}", response);
        messageSender.ack(response.getMessageId());

    }
}
