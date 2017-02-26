package bfahimi.nettyJ.commons;

import io.netty.channel.Channel;

/**
 *
 */
public interface ConnectionHandler {

    void activate(Channel channel);

    void exception(Channel channel, Throwable cause);

    void shutdown(Channel channel);
}
