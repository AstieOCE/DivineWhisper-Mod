package com.astieoce.divinewhisper.camera;

import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CameraPath {
    private final List<CameraFrame> frames = new ArrayList<>();
    private Map<String, Object> settings;

    public void addFrame(Vec3d position, float yaw, float pitch, long timestamp) {
        frames.add(new CameraFrame(position, yaw, pitch, timestamp));
    }

    public List<CameraFrame> getFrames() {
        return frames;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public static class CameraFrame {
        private final Vec3d position;
        private final float yaw;
        private final float pitch;
        private final long timestamp;

        public CameraFrame(Vec3d position, float yaw, float pitch, long timestamp) {
            this.position = position;
            this.yaw = yaw;
            this.pitch = pitch;
            this.timestamp = timestamp;
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
    }
}
