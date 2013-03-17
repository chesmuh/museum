package de.chesmuh.ordo.data;

public class SQLStatements {

	private SQLStatements() {
		
	}
	
	public static class Museum {
	
		private Museum() {
			
		}
		
		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + Ordo.Museum.TABLE_NAME + " (\n\t" + Ordo.Museum.ID + " int unsigned primary key auto_increment,\n\t" + Ordo.Museum.NAME + " varchar(255) not null,\n\t" + Ordo.Museum.DESCRIPTION + " text null,\n\t" + Ordo.Museum.DELETED + " timestamp null\n);";

	}
	
	public static class Section {
		
		private Section() {
			
		}
		
		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + Ordo.Section.TABLE_NAME + " (\n\t" + Ordo.Section.ID + " int unsigned primary key auto_increment,\n\t" + Ordo.Section.PARENT_ID + " int unsigned null,\n\t" + Ordo.Section.MUSEUM_ID + " int unsigned null,\n\t" + Ordo.Section.NAME + " varchar(255) not null,\n\t" + Ordo.Section.DESCRIPTION + " text null,\n\t" + Ordo.Section.DELETED + " timestamp null\n);";
	}

}
