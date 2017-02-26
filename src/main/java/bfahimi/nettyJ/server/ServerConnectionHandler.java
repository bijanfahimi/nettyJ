package bfahimi.nettyJ.server;

import bfahimi.nettyJ.commons.ConnectionHandler;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class ServerConnectionHandler implements ConnectionHandler {


    @Override
    public void activate(Channel channel) {
        log.info("HostAndPort on local address {}  established connection." + channel.localAddress());

    }

    @Override
    public void exception(Channel channel, Throwable cause) {
        log.warn("Exception raised during netty handling, closing connection to {}", channel.localAddress(), cause);

    }

    @Override
    public void shutdown(Channel channel) {
        log.warn("Closing connection to {}", channel.localAddress());
    }
}
