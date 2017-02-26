package bfahimi.nettyJ.server;


import bfahimi.nettyJ.commons.ConnectionHandler;
import bfahimi.nettyJ.settings.ConnectionSettings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class TcpServerManager {

    private final ConnectionSettings settings;

    private final ConnectionHandler connectionHandler;


    public void start() {

        TcpServer tcpStandardServerThread = new TcpServer(settings, connectionHandler);
        tcpStandardServerThread.run();
    }


}
