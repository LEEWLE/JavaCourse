package com.loucong.week_3.gateway.outbound.netty;

import com.loucong.week_3.gateway.filter.HttpRequestFilter;
import com.loucong.week_3.gateway.outbound.OutboundHandler;
import com.loucong.week_3.gateway.router.HttpEndpointRouter;
import com.loucong.week_3.gateway.router.RandomHttpEndpointRouter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NettyClient implements OutboundHandler {

    private List<String> backendUrls;

    // 路由选择器
    private HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public NettyClient(List<String> backendUrls) {
        this.backendUrls = backendUrls;
    }

    @Override
    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext serverCtx, HttpRequestFilter requestFilter) throws InterruptedException, MalformedURLException {
        EventLoopGroup group = new NioEventLoopGroup();
        String backendUrl = router.route(backendUrls);
        URL url = new URL(backendUrl);
        String host = url.getHost();
        int port = url.getPort();
        String path = url.getPath();
        try {
            // 创建 Bootstrap
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 长链接
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 适用于 NIO 传输的 Channel 类型
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 自动聚合，将最大的消息大小为 512K 的 HttpObjectAggregator 添加到 Pipeline
                            pipeline
                                    // .addLast(new HttpResponseDecoder())
                                    // .addLast(new HttpRequestEncoder())
                                    // http 加解码器，相当于上面两行代码
                                    .addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(512 * 1024))
                                    // 添加自定义 Handler
                                    .addLast(new NettyOutboundHandler(path, fullRequest, serverCtx, requestFilter));
                        }
                    });
            // 连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            // 阻塞，直到 Channel 关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭线程池并且释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
