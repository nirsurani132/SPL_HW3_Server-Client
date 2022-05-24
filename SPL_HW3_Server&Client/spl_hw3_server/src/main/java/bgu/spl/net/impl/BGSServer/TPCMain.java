package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.Utils;
import bgu.spl.net.api.BGUSocialEncoderDecoder;
import bgu.spl.net.api.BGUSocialProtocol;
import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        if (args.length > 1)
            Utils.forbiddenWords = args[1].split(",");
        BGUSocialConnections connections = BGUSocialConnections.getInstance();
        BaseServer server = new BaseServer(Integer.parseInt(args[0]), () -> new BGUSocialProtocol(), () -> new BGUSocialEncoderDecoder(), connections) {
            @Override
            protected void execute(BlockingConnectionHandler handler) {
                new Thread(handler).start();
            }
        };
        server.serve();
    }
}
