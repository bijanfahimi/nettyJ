package bfahimi.nettyJ.server;

import bfahimi.nettyJ.commons.ConnectionHandler;
import bfahimi.nettyJ.model.Request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles a server-side channel.
 */
@Slf4j
@RequiredArgsConstructor
public class TcpServerHandler extends SimpleChannelInboundHandler<Request> {

    private final RequestHandler requestHandler;

    private final ConnectionHandler connectionHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connectionHandler.activate(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request msg) {
        requestHandler.handleRequest(msg, new DefaultRequestContext(ctx.channel()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        connectionHandler.exception(ctx.channel(), cause);
        ctx.close();
    }

}