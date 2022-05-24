package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.Utils;
import bgu.spl.net.api.BGUSocialEncoderDecoder;
import bgu.spl.net.api.BGUSocialProtocol;
import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        if (args.length > 2)
            Utils.forbiddenWords = args[2].split(",");
        BGUSocialConnections connections = BGUSocialConnections.getInstance();
        Reactor server = new Reactor(Integer.parseInt(args[1]),Integer.parseInt(args[0]),() -> new BGUSocialProtocol(), () -> new BGUSocialEncoderDecoder(),connections);
        server.serve();
    }
}
