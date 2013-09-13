package main.java.com.weibo.api.container;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;

/**
 * 使用 Netty 作为 HTTP 容器的服务端。
 * User: Aiden S. Zouliu
 * Date: 9/13/13
 * Time: 3:57 PM
 */
public class NettyHttpServer {

    private static final Range<Integer> allowingPortRange = Range.closed(1000, 65535); // 允许监听的端口范围。

    private final int port; // 监听的端口。

    /**
     * 指定监听的端口号，创建一个服务端实例。
     * @param port 监听的端口号。
     * @throws IllegalArgumentException 如果指定的端口号不在闭区间 [1000, 65535]。
     */
    public NettyHttpServer(int port) throws IllegalArgumentException {
        Preconditions.checkArgument(allowingPortRange.contains(port), "port(%s) is out of range %s", port, allowingPortRange);
        this.port = port;
    }

    /**
     * 启动服务端。
     */
    public void start() {

    }

    /**
     * 停止服务器端。
     */
    public void shutdown() {

    }

}
