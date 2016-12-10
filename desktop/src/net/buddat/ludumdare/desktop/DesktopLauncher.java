package net.buddat.ludumdare.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.buddat.ludumdare.Constants;
import net.buddat.ludumdare.LD37;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = Constants.INSTANCE.getWidth();
		config.height = Constants.INSTANCE.getHeight();
		config.resizable = false;
		config.title = Constants.INSTANCE.getTitle();
		config.x = -1;
		config.y = -1;
		config.foregroundFPS = Constants.INSTANCE.getFPS();
		
		new LwjglApplication(new LD37(), config);
	}
	
}
