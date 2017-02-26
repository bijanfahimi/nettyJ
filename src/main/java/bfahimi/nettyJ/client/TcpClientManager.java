package bfahimi.nettyJ.client;


import bfahimi.nettyJ.commons.ConnectionHandler;
import bfahimi.nettyJ.commons.MessageEmitter;
import bfahimi.nettyJ.settings.ConnectionSettings;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.serverlist.HostAndPort;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class TcpClientManager {


    private final ConnectionSettings connectionSettings;

    private final ConnectionHandler connectionHandler;

    private final MessageEmitter messageSender;

    private final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    public void connect(HostAndPort server) {
        TcpClient tcpClientThread = new TcpClient(eventLoopGroup, connectionHandler, messageSender, server, connectionSettings);
        tcpClientThread.run();
    }

}
