package bfahimi.nettyJ.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import utils.serverlist.HostAndPort;

/**
 *
 */
@Data
@RequiredArgsConstructor
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class Request extends Message implements Serializable{

    @NonNull
    public Object data;

    @NonNull
    public HostAndPort sender;

}
