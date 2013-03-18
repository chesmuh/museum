package de.chesmuh.ordo.gui.interfaces;

import org.eclipse.swt.widgets.Composite;

import de.chesmuh.ordo.gui.UiEventType;

public class UiEvent {
	
	private Composite sender;
	private Object data;
	private UiEventType type;
	
	public UiEvent(Composite sender, Object data, UiEventType type) { 
		this.sender = sender;
		this.data = data;
		this.type = type;
	}

	public Composite getSender() {
		return sender;
	}

	public void setSender(Composite sender) {
		this.sender = sender;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public UiEventType getType() {
		return type;
	}

	public void setType(UiEventType type) {
		this.type = type;
	}
	
}
