package bfahimi.nettyJ.server;

import bfahimi.nettyJ.model.Request;

public interface RequestHandler {
    void handleRequest(Request request, RequestContext context);
}