package de.chesmuh.ordo.data;

/**
 * 
 * @author Chesmuh
 * 
 */
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
		public static final String INSERTED = "inserted";
		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (\n\t" + ID
				+ " int unsigned primary key auto_increment,\n\t" + NAME
				+ " varchar(255) not null,\n\t" + DESCRIPTION
				+ " text null,\n\t" + INSERTED + " timestamp not null,\n\t"
				+ DELETED + " timestamp null\n);";

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
		public static final String INSERTED = "inserted";
		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (\n\t" + ID
				+ " int unsigned primary key auto_increment,\n\t" + PARENT_ID
				+ " int unsigned null,\n\t" + MUSEUM_ID
				+ " int unsigned not null,\n\t" + NAME
				+ " varchar(255) not null,\n\t" + DESCRIPTION
				+ " text null,\n\t" + INSERTED + " timestamp not null,\n\t"
				+ DELETED + " timestamp null\n);";

	}

	public static class Exhibit {

		private Exhibit() {

		}

		public static final String TABLE_NAME = "exhibit";
		public static final String ID = "id";
		public static final String SECTION_ID = "section_id";
		public static final String MUSEUM_ID = "museum_id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String DELETED = "deleted";
		public static final String INSERTED = "inserted";
		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "	+ TABLE_NAME + " (\n\t" 
				+ ID + " int unsigned primary key auto_increment,\n\t" 
				+ SECTION_ID + " int unsigned null,\n\t" 
				+ MUSEUM_ID + " int unsigned null,\n\t" 
				+ NAME + " varchar(255) not null,\n\t" 
				+ DESCRIPTION + " text null,\n\t" 
				+ INSERTED + " timestamp not null,\n\t"
				+ DELETED + " timestamp null\n);";
	}

	public static class Tag {
		
		private Tag() {
			
		}
		
		public static final String TABLE_NAME = "tag";
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String DELETED = "deleted";
		public static final String INSERTED = "inserted";
		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "	+ TABLE_NAME + " (\n\t" 
				+ ID + " int unsigned primary key auto_increment,\n\t" 
				+ NAME + " varchar(255) not null,\n\t" 
				+ INSERTED + " timestamp not null,\n\t"
				+ DELETED + " timestamp null\n);";
	}
	
	public static class Tagged {
		
		private Tagged() {
			
		}
		
		public static final String TABLE_NAME = "tagged";
		public static final String ID = "labeled_id";
		public static final String EXHIBIT_ID = "exhibit_id";
		public static final String LABEL_ID = "label_id";
		public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "	+ TABLE_NAME + " (\n\t" 
				+ ID + " int unsigned primary key auto_increment,\n\t"
				+ EXHIBIT_ID + " int unsigned not null,\n\t"
				+ LABEL_ID + " int unsigned not null\n);";
	}
}
