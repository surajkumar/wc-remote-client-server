package com.wannacall.rs.server.screen;

import com.wannacall.utils.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.FileCacheImageOutputStream;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScreenRecorder implements Runnable {
    private List<ScreenWatcher> watchers = new ArrayList<>();
    private Robot robot;
    private Rectangle rectangle;

    public ScreenRecorder() throws AWTException {
        this.robot = new Robot();
        this.rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    @Override
    public void run() {
        while(true) {
            try {
                var buffers = new ByteArrayOutputStream[3];
                for(int i = 0; i < buffers.length; i++) {
                    var buffer = new ByteArrayOutputStream();
                    var screenshot = robot.createScreenCapture(rectangle);

                    if (!ImageIO.write(screenshot, "tiff", buffer)) {
                        Logger.error("Failed to convert screenshot to image: No image writer on system found.");
                    }

                    watchers.stream().forEach(watcher -> watcher.receiveScreenshot(buffer.toByteArray()));
                }

            } catch (Exception e) {
                Logger.error("Error capturing screenshot: " + e.getMessage());
            }
        }
    }

    public boolean registerWatcher(ScreenWatcher watcher) {
       return watchers.add(watcher);
    }

    public boolean removeWatcher(ScreenWatcher watcher) {
        return watchers.remove(watcher);
    }

    public List<ScreenWatcher> getWatchers() {
        return Collections.unmodifiableList(watchers);
    }
}