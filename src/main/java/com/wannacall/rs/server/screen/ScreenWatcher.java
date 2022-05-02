package com.wannacall.rs.server.screen;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class ScreenWatcher {
    private NetSocket socket;

    public ScreenWatcher(NetSocket socket) {
        this.socket = socket;
    }

    public void receiveScreenshot(byte[] buffer) {
        socket.write(Buffer.buffer().appendBytes(buffer));
    }

    public NetSocket getSocket() {
        return socket;
    }
}
