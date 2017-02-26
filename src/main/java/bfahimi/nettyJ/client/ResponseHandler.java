package bfahimi.nettyJ.client;

import bfahimi.nettyJ.model.Response;

import io.netty.channel.Channel;

/**
 *
 */
public interface ResponseHandler {

    void handleResponse(Response response, Channel channel);
}
