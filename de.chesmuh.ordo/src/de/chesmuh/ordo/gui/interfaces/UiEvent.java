package de.chesmuh.ordo.gui.interfaces;



public class UiEvent {
	
	private Object data;
	private UiEventType type;
	
	public UiEvent(Object data, UiEventType type) { 
		this.data = data;
		this.type = type;
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
