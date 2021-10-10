package com.loucong.week_3.gateway.inbound;
import com.loucong.week_3.gateway.filter.HeaderHttpRequestFilter;
import com.loucong.week_3.gateway.filter.HttpRequestFilter;
import com.loucong.week_3.gateway.outbound.OutboundHandler;
import com.loucong.week_3.gateway.outbound.netty.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private String configPath = "/Users/loucong/java/src/loucong.com/taylor/src/main/resources/client.properties";

    private final List<String> proxyServer;

    // 转发器：HttpClient 或者 Netty 实现
    private OutboundHandler client;

    private HttpRequestFilter filter = new HeaderHttpRequestFilter();

    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer =  proxyServer;
        // 获取配置文件
//        Properties properties = new Properties();
        try {
//            properties.load(new FileInputStream(configPath));
//            String clientClass = properties.getProperty("client");
//            Class<?> aClass = Class.forName(clientClass);
//            Constructor<?> constructor = aClass.getConstructor(List.class);
//            this.client = (OutboundHandler) constructor.newInstance(proxyServer);
            this.client = new NettyClient(proxyServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 通知 ChannelInboundHandler 最后一次对 channelRead() 的调用是当前批量读取中的最后一条消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    /**
     * 对于每个传入的消息都要调用
     * @param serverCtx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext serverCtx, Object msg) throws Exception {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;

            client.handle(fullRequest, serverCtx, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
