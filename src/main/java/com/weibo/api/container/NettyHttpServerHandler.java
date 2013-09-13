package main.java.com.weibo.api.container;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 基于 Netty 的 HTTP 服务端的处理逻辑。
 * User: Aiden S. Zouliu
 * Date: 9/13/13
 * Time: 4:34 PM
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // TODO 完成读的逻辑。
    }

}
