package Renderers;

public final class GameSettings {

    private static int brightnessPercent = 100;

    private GameSettings()
    {
    }

    public static int getBrightnessPercent()
    {
        return brightnessPercent;
    }

    public static void setBrightnessPercent(int brightness)
    {
        brightnessPercent = Math.max(0, Math.min(100, brightness));
    }

    public static int getDarknessAlpha()
    {
        return Math.round((100 - brightnessPercent) * 2.55f);
    }
}
