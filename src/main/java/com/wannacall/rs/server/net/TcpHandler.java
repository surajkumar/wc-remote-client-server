package com.wannacall.rs.server.net;

import com.wannacall.rs.server.screen.ScreenRecorder;
import com.wannacall.rs.server.screen.ScreenWatcher;
import com.wannacall.utils.logging.Logger;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

import java.awt.*;
import java.awt.event.InputEvent;
import java.nio.ByteBuffer;

public class TcpHandler implements Handler<NetSocket> {
    private final ScreenRecorder recorder = new ScreenRecorder();
    private final Vertx vertx;

    public TcpHandler(Vertx vertx) throws AWTException {
        this.vertx = vertx;
        new Thread(recorder).start();
    }

    public boolean containsSocket(NetSocket socket) {
        for (ScreenWatcher watcher : recorder.getWatchers()) {
            if(watcher.getSocket() == socket) {
                return true;
            }
        }
        return false;
    }

    public ScreenWatcher getWatcher(NetSocket socket) {
        for (ScreenWatcher watcher : recorder.getWatchers()) {
            if(watcher.getSocket() == socket) {
                return watcher;
            }
        }
        return null;
    }

    private Robot robot = new Robot();


    @Override
    public void handle(NetSocket socket) {
        socket.closeHandler(handler -> {
            recorder.removeWatcher(getWatcher(socket));
            Logger.info(socket.localAddress().host() + " disconnected.");
        });

        socket.handler(buffer -> vertx.executeBlocking(x -> {
            if (buffer.length() <= 0) {
                Logger.warning("Received 0 bytes from " + socket.remoteAddress().host());
                return;
            }

            if(!containsSocket(socket)) {
                recorder.registerWatcher(new ScreenWatcher(socket));
                Logger.info("Registered watcher");
            }

            try {
                var buf = ByteBuffer.wrap(buffer.getBytes());
                var action = buf.getInt();
                switch(action) {
                    case 1:
                        var xPos = buf.getInt();
                        var yPos = buf.getInt();
                        var button = buf.getInt();
                        robot.mouseMove(xPos, yPos);
                        robot.mousePress(InputEvent.getMaskForButton(button));
                        break;
                    case 2:
                        robot.mouseRelease(InputEvent.getMaskForButton(buf.getInt()));
                        break;
                }

            } catch (Exception e) {
                /* Ignore */
                System.out.println(e.getMessage());
            }



        }, res -> {
            if (res.failed()) {
                Logger.warning("Failed to handle a TCP request");
                Logger.warning("Reason: " + res.cause().getMessage());
            } else {
                Logger.info("Handled TCP request successfully");
            }
        }));
    }
}
