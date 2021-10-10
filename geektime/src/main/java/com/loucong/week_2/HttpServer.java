package com.loucong.week_2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author loucong
 */
public class HttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8080);
        while (true) {
            Socket accept = socket.accept();
            service(accept);
        }
    }

    private static void service(Socket socket) throws IOException {
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            inputStream = socket.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            System.out.println(new String(baos.toByteArray()));
            System.out.println("------------------");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream.close();
        }

        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,nio";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.flush();
            Thread.sleep(2000);
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
