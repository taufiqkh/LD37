package net.buddat.ludumdare.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.buddat.ludumdare.ConstantsKt;
import net.buddat.ludumdare.LD37;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = ConstantsKt.getWidth();
		config.height = ConstantsKt.getHeight();
		config.resizable = false;
		config.title = ConstantsKt.getTitle();
		config.x = -1;
		config.y = -1;
		config.foregroundFPS = ConstantsKt.getFPS();
		
		new LwjglApplication(new LD37(), config);
	}
	
}
