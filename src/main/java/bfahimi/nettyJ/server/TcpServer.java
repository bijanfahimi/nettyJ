package bfahimi.nettyJ.server;


import bfahimi.nettyJ.commons.ConnectionHandler;
import bfahimi.nettyJ.settings.ConnectionSettings;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.flush.FlushConsolidationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.inet.SocketUtil;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class TcpServer {


    private final ConnectionSettings settings;

    private final ConnectionHandler connectionHandler;

    public void run() {

        int port = SocketUtil.findFreePortInRange(settings.getFromPort(), settings.getToPort());


        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("EE", new ObjectEncoder())
                                .addLast("decoder", new ObjectDecoder(ClassResolvers.weakCachingResolver(ClassLoader.getSystemClassLoader())))
                                .addLast(new TcpServerHandler(new DefaultRequestHandler(), connectionHandler))
                                .addFirst(new FlushConsolidationHandler(settings.getMaxPendingMessages(), true));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(settings.getLowWaterMarkInBytes(), settings.getHighWaterMarkInBytes()));

        log.info("Bind on port {} and start to accept incoming connections.", port);
        Channel channel = b.bind(port).channel();


        channel.closeFuture().addListener((ChannelFutureListener) future -> {
            connectionHandler.shutdown(future.channel());

            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        });

    }

}
