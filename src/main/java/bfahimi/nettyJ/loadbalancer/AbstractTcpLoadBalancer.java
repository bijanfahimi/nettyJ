package bfahimi.nettyJ.loadbalancer;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public abstract class AbstractTcpLoadBalancer {

    protected CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<>();


    public void addServer(Channel channel) {

        if (channels.stream()
                .noneMatch(isChannelAlreadyInLoadBalancer(channel))) {

            channels.add(channel);
            log.info("Add connection to {} to loadbalancer. ", channel.remoteAddress());
        }else {
            log.info("Ignoring the connection,  a connection pointing to the same remote address {} is already in the loadbalancer. ", channel.remoteAddress());
        }

    }

    public boolean removeServer(Channel channel) {
        return channels.removeIf(isChannelAlreadyInLoadBalancer(channel));
    }

    private Predicate<Channel> isChannelAlreadyInLoadBalancer(Channel channel) {
        return channelInList -> channelInList.remoteAddress().equals(channel.remoteAddress());
    }

    public boolean hasServer(){
        return !channels.isEmpty();
    }

    public abstract Optional<Channel> next();


    public Stream<Channel> all() {
        return channels.stream().filter(channel -> channel.isActive() && channel.isOpen())
                .collect(Collectors.toList())
                .stream();
    }

}
