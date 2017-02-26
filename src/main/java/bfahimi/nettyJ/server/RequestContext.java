package bfahimi.nettyJ.server;

import bfahimi.nettyJ.model.Response;

public interface RequestContext {
  void reply(Response reply);
}