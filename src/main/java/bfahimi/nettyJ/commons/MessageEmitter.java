package bfahimi.nettyJ.commons;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import bfahimi.nettyJ.model.MessageId;
import com.github.benmanes.caffeine.cache.RemovalCause;
import bfahimi.nettyJ.loadbalancer.AbstractTcpLoadBalancer;
import bfahimi.nettyJ.model.Request;
import bfahimi.nettyJ.settings.ConnectionSettings;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class MessageEmitter {


    private final AbstractTcpLoadBalancer loadBalancer;
    private final ConnectionSettings settings;

    private TimeEvictedMap<Long, Request> cache;



    public MessageEmitter(AbstractTcpLoadBalancer loadBalancer, BiConsumer<Request, RemovalCause> timeoutHandler, ConnectionSettings settings) {
        this.settings = settings;
        this.loadBalancer = loadBalancer;

        cache = new TimeEvictedMap<>(settings.getServerRequestAckTimeoutInSec(), TimeUnit.SECONDS,
                (messageId) -> {
                    log.warn("Unknown messageId {}. The message has already been evicted.", messageId);
                    return null;
                },
                (key, value, cause) -> {
                    if (cause.wasEvicted()) {
                        timeoutHandler.accept(value, cause);
                    }
                });
    }

    public void emit(Request request) {

        Channel channel = loadBalancer.next().orElseThrow(() -> new IllegalAccessError("Could not emit request " + request.getMessageId() + " no communication channel active."));

        cache.put(request.getMessageId().getCorrelationId(), request);
        channel.writeAndFlush(request);
    }

    public void ack(MessageId messageId) {
        cache.evict(messageId.getCorrelationId());
    }
}
