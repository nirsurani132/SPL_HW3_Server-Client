package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Connections<T> connections;
    private final int connectionId;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder reader, BidiMessagingProtocol protocol, Connections connections) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connections = connections;
        this.connectionId = connections.register(this);
    }

    @Override
    public void run() {
        protocol.start(connectionId,connections);
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    System.out.println(nextMessage);
                    protocol.process(nextMessage);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void send(T msg) {
        try {
            out = new BufferedOutputStream(sock.getOutputStream());
            if(msg!=null) {
                out.write(encdec.encode(msg));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        protocol.closeConnection();
        connected = false;
        sock.close();
    }
}
