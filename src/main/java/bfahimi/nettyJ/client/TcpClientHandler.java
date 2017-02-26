package bfahimi.nettyJ.client;

import bfahimi.nettyJ.commons.ConnectionHandler;
import bfahimi.nettyJ.model.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TcpClientHandler extends SimpleChannelInboundHandler<Response> {

    private final ResponseHandler responseHandler;

    private final ConnectionHandler connectionHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connectionHandler.activate(ctx.channel());
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        responseHandler.handleResponse(msg, ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("Exception in Client during netty response handling closing connection to {}", ctx.channel().remoteAddress(), cause);
        connectionHandler.exception(ctx.channel(), cause);
        ctx.close();
    }
}