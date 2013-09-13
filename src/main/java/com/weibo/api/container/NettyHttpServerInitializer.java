package main.java.com.weibo.api.container;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 基于 Netty 的 HTTP 容器的启动器。
 * User: Aiden S. Zouliu
 * Date: 9/13/13
 * Time: 4:20 PM
 */
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        // 创建一个默认的 pipeline 实例。
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 如果需要 HTTPS 支持，解开下面的一系列注释。
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("decoder", new HttpRequestDecoder());
        // 如果不需要处理 HttpChunks，解开下面这行注释。
        //pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        // 如果不需要自动压缩内容，去掉下面这行代码。
        //pipeline.addLast("deflater", new HttpContentCompressor());
        pipeline.addLast("handler", new NettyHttpServerHandler());
    }

}
