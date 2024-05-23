package com.astieoce.divinewhisper.camera;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class CameraPath {
    private final List<CameraFrame> frames = new ArrayList<>();

    public void addFrame(Vec3d position, float yaw, float pitch, long timestamp) {
        frames.add(new CameraFrame(position, yaw, pitch, timestamp));
    }

    public List<CameraFrame> getFrames() {
        return frames;
    }

    public record CameraFrame(Vec3d position, float yaw, float pitch, long timestamp) {
    }
}
