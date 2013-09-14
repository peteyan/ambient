package main.java.com.weibo.api.handler;

import com.google.common.base.Preconditions;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

/**
 * 基于 Netty 的 HTTP 服务端的处理逻辑。
 * User: Aiden S. Zouliu
 * Date: 9/13/13
 * Time: 4:34 PM
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(NettyHttpServerHandler.class);

    private final Servlet servlet;
    private final ServletContext servletContext;

    /**
     * 指定一个实际处理的 {@link Servlet}，创建一个实例。
     * @param servlet 实际处理请求的 {@link Servlet}。
     */
    public NettyHttpServerHandler(Servlet servlet) {
        this.servlet = Preconditions.checkNotNull(servlet, "servlet must not be null");
        this.servletContext = this.servlet.getServletConfig().getServletContext();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
        // TODO 转化成 Servlet 形式，交给指定的 servlet 处理。
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("{} caught exception", NettyHttpServerHandler.class.getSimpleName(), cause);
        if (ctx.channel().isActive()) {
            LOGGER.debug("sendError with status {}", HttpResponseStatus.INTERNAL_SERVER_ERROR);
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 发送错误信息。
     */
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().add(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    }

}
