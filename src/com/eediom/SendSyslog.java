package com.eediom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendSyslog {

    public static void main(String[] args) {
        if (args == null || args.length != 4) {
            System.err.println("USAGE: java -jar SendSyslog.jar <tcp|tcp-multi|udp> host <port> <filename>\n\n" +
                    "Options : \n" +
                    "==================================================================================================\n" +
                    "  - tcp : sends messages over tcp, messages delimited by new lines\n" +
                    "  - tcp-multi : sends each 1000 messages over a new connection\n" +
                    "  - udp : sends messages over udp\n" +
                    "  - host : the host to send to\n" +
                    "  - port : the port to send to\n" +
                    "  - filename : filename to use as a line-by-line message\n");
            System.exit(1);
        }

        final String type = args[0];
        final String host = args[1];
        final Integer port = Integer.parseInt(args[2]);
        final String fileName = args[3];
        final SendSyslog sender = new SendSyslog();
        try {
            if ("tcp".equals(type)) {
                sender.sendTcp(host, port, fileName);
            } else if ("tcp-multi".equals(type)) {
                sender.sendTcpMulti(host, port, fileName);
            } else {
                sender.sendUdp(host, port, fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendUdp(String host, int port, String fileName) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(1048576);
        final InetSocketAddress address = new InetSocketAddress(host, port);
        final BufferedReader in = new BufferedReader(new FileReader(fileName));
        int i = 0;
        String s;

        DatagramChannel channel = DatagramChannel.open();
        while ((s = in.readLine()) != null) {
            long time = System.currentTimeMillis();
            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            s = dayTime.format(new Date(time)) + " logpresso [UDP] : " + s;
            byte[] message = s.getBytes();
            buf.clear();
            buf.put(message);
            buf.flip();
            channel.send(buf, address);
            i++;

            if (i % 10000 == 0) {
                System.out.println("Sent " + i);
            }
        }
        in.close();
        System.out.println("DONE!  " + i);
    }

    private void sendTcp(String host, int port, String fileName) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(1048576);
        final InetSocketAddress address = new InetSocketAddress(host, port);
        final BufferedReader in = new BufferedReader(new FileReader(fileName));
        String s;
        int i = 0;
        SocketChannel channel = SocketChannel.open();
        channel.connect(address);

        while ((s = in.readLine()) != null) {
            long time = System.currentTimeMillis();
            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            s = dayTime.format(new Date(time)) + " logpresso [TCP] : " + s;
            byte[] message = s.getBytes();
            buf.clear();
            buf.put(message);
            buf.put((byte) '\n');
            buf.flip();

            while (buf.hasRemaining()) {
                channel.write(buf);
            }
            i++;

            if (i % 10000 == 0) {
                System.out.println("Sent " + i);
            }
        }
        in.close();
        System.out.println("DONE!  " + i);
    }

    private void sendTcpMulti(String host, int port, String fileName) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(1048576);
        final InetSocketAddress address = new InetSocketAddress(host, port);
        final BufferedReader in = new BufferedReader(new FileReader(fileName));
        String s;
        int i = 0; int channel_count = 0;
        SocketChannel channel = SocketChannel.open();
        channel.connect(address);

        while ((s = in.readLine()) != null) {
            if (i % 1000 == 999) {
                channel_count++;
                channel.close();
                channel = SocketChannel.open();
                channel.connect(address);
            }

            long time = System.currentTimeMillis();
            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            s = dayTime.format(new Date(time)) + " logpresso [TCP : connection-" + channel_count + " ] : " + s;
            byte[] message = s.getBytes();
            buf.clear();
            buf.put(message);
            buf.put((byte) '\n');
            buf.flip();

            while (buf.hasRemaining()) {
                channel.write(buf);
            }
            i++;

            if (i % 10000 == 0) {
                System.out.println("Sent " + i);
            }
        }
        in.close();
        System.out.println("DONE!  " + i);
    }
}