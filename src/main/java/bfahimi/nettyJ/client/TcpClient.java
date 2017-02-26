package bfahimi.nettyJ.client;


import bfahimi.nettyJ.commons.ConnectionHandler;
import bfahimi.nettyJ.commons.MessageEmitter;
import bfahimi.nettyJ.settings.ConnectionSettings;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.flush.FlushConsolidationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.serverlist.HostAndPort;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class TcpClient {

    private final EventLoopGroup workerGroup;

    private final ConnectionHandler connectionHandler;

    private final MessageEmitter messageSender;

    private final HostAndPort server;

    private final ConnectionSettings connectionSettings;


    public void run() {

        Bootstrap b = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(connectionSettings.getLowWaterMarkInBytes(), connectionSettings.getHighWaterMarkInBytes()))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("encoder", new ObjectEncoder())
                                .addLast("decoder", new ObjectDecoder(ClassResolvers.weakCachingResolver(ClassLoader.getSystemClassLoader())))
                                .addLast(new TcpClientHandler(new DefaultResponseHandler(messageSender), connectionHandler))
                                .addFirst(new FlushConsolidationHandler(connectionSettings.getMaxPendingMessages(), true));
                    }
                });

        log.info("Trying to connect tcp client on  {}", server);
        Channel channel = b.connect(server.getHost(), server.getPort()).channel();


        log.info("Wait until the connection {} is closed.", server);
        channel.closeFuture().addListener((ChannelFutureListener) future -> connectionHandler.shutdown(future.channel()));

    }

}
