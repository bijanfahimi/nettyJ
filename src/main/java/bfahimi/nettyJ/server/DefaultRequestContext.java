package bfahimi.nettyJ.server;

import bfahimi.nettyJ.model.Response;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@RequiredArgsConstructor
public class DefaultRequestContext implements RequestContext {

    private final Channel channel;

    @Override
    public void reply(Response response) {
        channel.writeAndFlush(response);
    }
}
