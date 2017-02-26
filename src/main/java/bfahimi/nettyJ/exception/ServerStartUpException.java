package bfahimi.nettyJ.exception;

/**
 *
 */
public class ServerStartUpException extends RuntimeException {

    public ServerStartUpException(Throwable t){
        super("Could not start server.", t);
    }
}
