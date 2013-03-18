package de.chesmuh.ordo.gui.resources;

import java.io.InputStream;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

public class ResourceManager {
	
	private ResourceManager() {
		
	}
	
	public static Image getImage(Device device, String path) {
		InputStream inputStream = ResourceManager.class.getResourceAsStream(path);
		if(inputStream == null)
			return null;
		return new Image(device, inputStream);
	}
}
