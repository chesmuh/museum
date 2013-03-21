package de.chesmuh.ordo.gui.resources;

import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

import de.chesmuh.ordo.config.Config;

public class ResourceManager {
	
	private ResourceManager() {
		
	}
	
	public static Image getImage(Device device, String path) {
		InputStream inputStream = ResourceManager.class.getResourceAsStream(path);
		if(inputStream == null)
			return null;
		return new Image(device, inputStream);
	}
	
	public static String getText(String key) {
		ResourceBundle bundle = Config.getInstance().getUIBundle();
		try {
			return bundle.getString(key);
		} catch(MissingResourceException exn) { // Key not found.
			return key;
		}
	}
}
