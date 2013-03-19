package de.chesmuh.ordo.data;

import java.util.Collection;

import de.chesmuh.ordo.data.manager.ExhibitManager;
import de.chesmuh.ordo.data.manager.MuseumManager;
import de.chesmuh.ordo.data.manager.SectionManager;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;

/**
 * 
 * @author Chesmuh
 *
 */
public class DataAccess {

	private static DataAccess instance;
	
	private MuseumManager museumManager;
	private SectionManager sectionManager;
	private ExhibitManager exhibitManager;
	
	private DataAccess() {
		museumManager = new MuseumManager();
		museumManager.loadAll();
		sectionManager = new SectionManager();
		sectionManager.loadAll();
		exhibitManager = new ExhibitManager();
		exhibitManager.loadAll();
	}
	
	public static DataAccess getInstance() {
		if(instance == null) {
			instance = new DataAccess();
		}
		return instance;
	}
	
	public Collection<Museum> getAllMuseum() {
		return museumManager.getAll();
	}
	
	public Museum getMuseumById(Long id) {
		return museumManager.getbyId(id);
	}

	public Collection<Section> getAllSection() {
		return sectionManager.getAll();
	}
	
	public Collection<Section> getAllSectionsByMuseum(Museum museum) {
		return sectionManager.getByMuseum(museum);
	}

	public Collection<Section> getAllSectionBySection(Section section) {
		return sectionManager.getBySection(section);
	}

	public Collection<Section> getAllSectionByMuseumWithNoParent(Museum museum) {
		return sectionManager.getByMuseumWithParentNull(museum);
	}

}
