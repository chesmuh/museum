package de.chesmuh.ordo.util;

import de.chesmuh.ordo.entitys.Section;

public class Util {

	private Util() {

	}

	public static String getPath(Section section) {
		String path = section.getName();
		while (section.getParent() != null) {
			path = section.getParent().getName() + "\\" + path;
			section = section.getParent();
		}
		path = section.getMuseum().getName() + "\\" + path;
		return path;
	}
}
