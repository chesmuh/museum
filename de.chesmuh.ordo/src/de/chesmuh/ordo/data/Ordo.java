package de.chesmuh.ordo.data;

public class Ordo {

	private Ordo() {
		
	}
	
	public static class Museum {
		
		private Museum() {
			
		}
		
		public static final String TABLE_NAME = "museum";
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String DELETED = "deleted";
				
	}
	
	public static class Section {
		
		private Section() {
			
		}
		
		public static final String TABLE_NAME = "section";
		public static final String ID = "id";
		public static final String PARENT_ID = "parent_id";
		public static final String MUSEUM_ID = "museum_id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String DELETED = "deleted";
		
	}
}
