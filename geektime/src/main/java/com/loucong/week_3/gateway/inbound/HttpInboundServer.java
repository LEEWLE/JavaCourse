package com.loucong.week_3.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.List;

public class HttpInboundServer {

    private int port;

    private List<String> proxyServers;

    public HttpInboundServer(int port, List<String> proxyServers) {
        this.port = port;
        this.proxyServers = proxyServers;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(16);

        try {
            ServerBootstrap b = new ServerBootstrap();
            // listen 函数：初始化服务端可连接队列
            b.option(ChannelOption.SO_BACKLOG, 128)
                    // 使用 Nagle 算法有关，Nagle算法是将小的数据包组装为更大的帧然后进行发送
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 允许重复使用本地地址和端口
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    // 接受缓冲区大小
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    // 发送缓冲区大小
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    // 两小时内没有数据通信，TCP 会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // ByteBuf的分配器(重用缓冲区)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new HttpInboundInitializer(this.proxyServers));

            Channel channel = b.bind(port).sync().channel();
            System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
