package com.astieoce.divinewhisper.util;

public class BrewColorHandling {
    private static final int FAILED_BREW_COLOR = 0xFF262433;

    public static int applyImpurityToColor(int baseColor, int impurity) {
        // Extract base color components
        int alpha = (baseColor >> 24) & 0xFF;
        int red = (baseColor >> 16) & 0xFF;
        int green = (baseColor >> 8) & 0xFF;
        int blue = baseColor & 0xFF;

        // Extract failed brew color components
        int failedRed = (FAILED_BREW_COLOR >> 16) & 0xFF;
        int failedGreen = (FAILED_BREW_COLOR >> 8) & 0xFF;
        int failedBlue = FAILED_BREW_COLOR & 0xFF;

        // Calculate impurity effect (blend between base color and failed brew color)
        float impurityFactor = impurity / 100.0f;
        red = blendColorComponent(red, failedRed, impurityFactor);
        green = blendColorComponent(green, failedGreen, impurityFactor);
        blue = blendColorComponent(blue, failedBlue, impurityFactor);

        // Combine adjusted components back into ARGB color
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private static int blendColorComponent(int baseComponent, int failedComponent, float factor) {
        return (int) (baseComponent * (1 - factor) + failedComponent * factor);
    }
}
