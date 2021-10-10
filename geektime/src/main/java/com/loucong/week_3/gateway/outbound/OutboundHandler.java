package com.loucong.week_3.gateway.outbound;

import com.loucong.week_3.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.MalformedURLException;

public interface OutboundHandler {

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter) throws InterruptedException, MalformedURLException;
}
