package com.wannacall.rs;

import com.wannacall.rs.client.Client;
import com.wannacall.rs.server.net.TcpServer;
import com.wannacall.utils.logging.Logger;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main {
    public static void main(String[] args) throws Exception {
        var options = (new VertxOptions())
                .setEventLoopPoolSize(Runtime.getRuntime().availableProcessors() * 50)
                .setWorkerPoolSize(Runtime.getRuntime().availableProcessors() * 50)
                .setPreferNativeTransport(true);
        var vertx = Vertx.vertx(options);
        vertx.deployVerticle(new TcpServer());
        Logger.info("Server started");
        new Client();
    }
}
