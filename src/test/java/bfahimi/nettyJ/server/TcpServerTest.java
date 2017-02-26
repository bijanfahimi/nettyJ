package bfahimi.nettyJ.server;

import java.net.UnknownHostException;

import bfahimi.nettyJ.commons.MessageEmitter;
import org.junit.Test;

import bfahimi.nettyJ.client.LoadBalancedConnectionHandler;
import bfahimi.nettyJ.client.TcpClientManager;
import bfahimi.nettyJ.commons.PrintingConnectionHandler;
import bfahimi.nettyJ.loadbalancer.RoundRobinTcpLoadBalancer;
import bfahimi.nettyJ.model.Request;
import bfahimi.nettyJ.settings.ConnectionSettings;

import utils.inet.HostNameUtil;
import utils.serverlist.HostAndPort;
import utils.sleeper.ThreadSleeper;

/**
 *
 */
public class TcpServerTest {

    @Test
    public void tcpServer() throws InterruptedException, UnknownHostException {


        ConnectionSettings connectionSettings = new ConnectionSettings();

        TcpServerManager server = new TcpServerManager(connectionSettings, new PrintingConnectionHandler());

        server.start();

        RoundRobinTcpLoadBalancer roundRobinTcpLoadBalancer = new RoundRobinTcpLoadBalancer();
        MessageEmitter sender = new MessageEmitter(roundRobinTcpLoadBalancer, (request, cause) -> {
            throw new RuntimeException("dfisjf: " + request);
        }, connectionSettings);


        LoadBalancedConnectionHandler loadBalancedConnectionHandler = new LoadBalancedConnectionHandler(roundRobinTcpLoadBalancer);
        TcpClientManager client = new TcpClientManager(connectionSettings, loadBalancedConnectionHandler, sender);

        HostAndPort server1 = new HostAndPort("localhost", 50_000);
        client.connect(server1);

        HostAndPort hostAndPort = new HostAndPort(HostNameUtil.getLocalHostAddress(), 50_000);


        ThreadSleeper.sleepQuietly(1000);

        while (true) {
            ThreadSleeper.sleepQuietly(100);
            Request request = new Request("Hello world!!", hostAndPort);
            sender.emit(request);
        }
    }
}
