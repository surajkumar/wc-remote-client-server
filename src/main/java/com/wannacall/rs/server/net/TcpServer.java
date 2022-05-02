package com.wannacall.rs.server.net;

import com.wannacall.utils.logging.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.net.NetServerOptions;

public class TcpServer extends AbstractVerticle {
    public void start(Promise<Void> startPromise) throws Exception {
        var server = vertx.createNetServer(new NetServerOptions().setPort(43594));
        server.connectHandler(new TcpHandler(vertx));
        server.listen(res -> {
            if (res.failed()) {
                Logger.error(res.cause().getMessage());
                System.exit(1);
            }
        });
    }
}
