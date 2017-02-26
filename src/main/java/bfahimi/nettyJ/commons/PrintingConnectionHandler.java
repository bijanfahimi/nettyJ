package bfahimi.nettyJ.commons;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class PrintingConnectionHandler implements ConnectionHandler {


    @Override
    public void activate(Channel channel) {
        log.info("HostAndPort connection established with client on {}" + channel.localAddress());
    }

    @Override
    public void exception(Channel channel, Throwable cause) {
        log.warn("Exception raised during netty handling closing connection to {}", channel.remoteAddress(), cause);
    }

    @Override
    public void shutdown(Channel channel) {
        log.warn("Closing connection to {}", channel.remoteAddress());
    }
}
