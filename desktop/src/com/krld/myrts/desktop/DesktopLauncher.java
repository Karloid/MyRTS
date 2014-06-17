package com.krld.myrts.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.krld.myrts.WorldView;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 768;
        WorldView listener = new WorldView();
        LwjglApplication lwjglApplication = new LwjglApplication(listener, config);
        listener.setWidth(config.width);
        listener.setHeigth(config.height);
    }
}
