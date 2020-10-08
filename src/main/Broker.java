package main;

import java.io.InputStream;
import java.net.*;

public class Broker {

    private int port;

    public Broker(int port) { this.port = port; }

    public static void main(String[] args) throws Exception {

        // port
        int port = 8088;
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        ServerSocket server = new ServerSocket(port);
        Socket socket = server.accept();
        InputStream inputStream = socket.getInputStream();

        byte[] bytes = new byte[65530];
        int len;

        while((len = inputStream.read(bytes)) != -1) {
            // 服务器处理过程
            handler(bytes, len);
        }

        inputStream.close();
        socket.close();
        server.close();


    }

    // 服务器处理
    public static void handler(byte[] bytes, int len) {
        System.out.printf(bytes.toString());
    }
}
