package bfahimi.nettyJ.loadbalancer;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class RoundRobinTcpLoadBalancer extends AbstractTcpLoadBalancer {

    private final AtomicInteger index = new AtomicInteger();

    @Override
    public Optional<Channel> next() {
        if(!hasServer()){
            log.warn("No connection established yet. Try again after a connection has been established! ");
            return Optional.empty();
        }
        return Optional.of(channels.get(index.getAndIncrement() % channels.size()));
    }

}
