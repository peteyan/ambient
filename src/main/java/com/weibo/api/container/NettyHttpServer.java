package main.java.com.weibo.api.container;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new NettyHttpServerInitializer());
            Channel channel = serverBootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new NettyHttpServer(port).start();
    }

}
