package com.astieoce.divinewhisper.camera;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CameraPath {
    private final List<CameraFrame> frames = new ArrayList<>();
    private Map<String, Object> settings;

    public static class CameraFrame {
        private final Vec3d position;
        private final float yaw;
        private final float pitch;
        private final long timestamp;
        private final Map<String, Boolean> keyStates;
        private final boolean leftClick;
        private final boolean rightClick;

        public CameraFrame(Vec3d position, float yaw, float pitch, long timestamp, Map<String, Boolean> keyStates, boolean leftClick, boolean rightClick) {
            this.position = position;
            this.yaw = yaw;
            this.pitch = pitch;
            this.timestamp = timestamp;
            this.keyStates = keyStates;
            this.leftClick = leftClick;
            this.rightClick = rightClick;
        }

        public Vec3d getPosition() {
            return position;
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public Map<String, Boolean> getKeyStates() {
            return keyStates;
        }

        public boolean isLeftClick() {
            return leftClick;
        }

        public boolean isRightClick() {
            return rightClick;
        }
    }

    public void addFrame(Vec3d position, float yaw, float pitch, long timestamp, Map<String, Boolean> keyStates, boolean leftClick, boolean rightClick) {
        frames.add(new CameraFrame(position, yaw, pitch, timestamp, keyStates, leftClick, rightClick));
    }

    public List<CameraFrame> getFrames() {
        return frames;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }
}
