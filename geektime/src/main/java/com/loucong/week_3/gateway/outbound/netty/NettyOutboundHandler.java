package com.loucong.week_3.gateway.outbound.netty;

import com.loucong.week_3.gateway.filter.HeaderHttpResponseFilter;
import com.loucong.week_3.gateway.filter.HttpRequestFilter;
import com.loucong.week_3.gateway.filter.HttpResponseFilter;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyOutboundHandler extends ChannelInboundHandlerAdapter {

    private String uri;

    // 响应过滤器
    private HttpResponseFilter responseFilter = new HeaderHttpResponseFilter();

    // 请求过滤器
    private HttpRequestFilter requestFilter;

    private FullHttpRequest request;

    private FullHttpResponse response;

    private ChannelHandlerContext serverCtx;

    public NettyOutboundHandler(String uri, FullHttpRequest fullHttpRequest, ChannelHandlerContext serverCtx, HttpRequestFilter requestFilter) {
        this.uri = uri;
        this.request = fullHttpRequest;
        this.serverCtx = serverCtx;
        this.requestFilter = requestFilter;
    }

    @Override
    public void channelActive(ChannelHandlerContext clientCtx) {
        request.setUri(uri);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        requestFilter.filter(request, serverCtx);
        clientCtx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext clientCtx, Object msg) throws Exception {
        try {
            response = (FullHttpResponse) msg;
            System.out.println(response.content().toString(CharsetUtil.UTF_8));
            responseFilter.filter(response);
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(serverCtx, e);
        } finally {
            if (request != null) {
                if (!HttpUtil.isKeepAlive(request)) {
                    serverCtx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    serverCtx.write(response);
                }
            }
            serverCtx.flush();
            clientCtx.close();
        }
    }

    /**
     * 在处理过程中引发异常时被调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 在发生异常时，记录错误并关闭 Channel
        cause.printStackTrace();
        ctx.close();
    }
}
