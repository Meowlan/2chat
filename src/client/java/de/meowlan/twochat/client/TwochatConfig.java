package de.meowlan.twochat.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.AutoConfig;

@Config(name = "twochat")
public class TwochatConfig implements ConfigData {
    int posX = 10;
    int posY = 10;
    int width = 320;
    int height = 180;
    int fadeTimeMs = 10000;
    int align = 1;
    boolean chatflowDown = false;
    boolean visible = true;
    float backgroundOpacity = 0.5f;

    public static TwochatConfig getConfig() {
        return AutoConfig.getConfigHolder(TwochatConfig.class).getConfig();
    }

    // getters
    int getPosX() { return posX; }
    int getPosY() { return posY; }
    int getWidth() { return width; }
    int getHeight() { return height; }
    int getFadeTimeMs() { return fadeTimeMs; }
    int getAlign() { return align; }
    boolean getChatflowDown() { return chatflowDown; }
    boolean getVisible() { return visible; }
    float getBackgroundOpacity() { return backgroundOpacity; }
}