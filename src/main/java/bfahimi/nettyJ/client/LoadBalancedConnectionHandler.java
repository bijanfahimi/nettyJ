package bfahimi.nettyJ.client;

import bfahimi.nettyJ.commons.ConnectionHandler;
import bfahimi.nettyJ.loadbalancer.AbstractTcpLoadBalancer;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class LoadBalancedConnectionHandler implements ConnectionHandler {

    private final AbstractTcpLoadBalancer loadBalancer;


    @Override
    public void activate(Channel channel) {
        log.info("HostAndPort connection established with client on {}" + channel.remoteAddress());
        loadBalancer.addServer(channel);
    }

    @Override
    public void exception(Channel channel, Throwable cause) {
        log.warn("Exception raised during netty handling, closing connection to {}", channel.remoteAddress(), cause);
        loadBalancer.removeServer(channel);
    }

    @Override
    public void shutdown(Channel channel) {
        log.warn("Closing connection to {}", channel.remoteAddress());
        loadBalancer.removeServer(channel);
    }
}
