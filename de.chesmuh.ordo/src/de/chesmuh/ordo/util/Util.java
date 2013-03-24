package de.chesmuh.ordo.util;

import java.util.Collection;

import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.entitys.Tag;

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

	public static String getTags(Collection<Tag> tags) {
		String text = "";
		for (Tag tagName : tags) {
			if (!text.isEmpty()) {
				text += ", " + tagName.getName();
			} else {
				text += tagName.getName();
			}
		}
		return text;
	}
}
