package de.chesmuh.ordo.data;

import java.util.Collection;

import de.chesmuh.ordo.data.manager.ExhibitManager;
import de.chesmuh.ordo.data.manager.LabelManager;
import de.chesmuh.ordo.data.manager.MuseumManager;
import de.chesmuh.ordo.data.manager.SectionManager;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Label;
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
	private LabelManager labelManager;
	
	private DataAccess() {
		museumManager = new MuseumManager();
		museumManager.loadAll();
		sectionManager = new SectionManager();
		sectionManager.loadAll();
		exhibitManager = new ExhibitManager();
		exhibitManager.loadAll();
		labelManager = new LabelManager();
		labelManager.loadAll();
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
	
	public Museum getMuseumById(long id) {
		return museumManager.getbyId(id);
	}

	public Collection<Exhibit> getExhibitBySection(Section section) {
		return exhibitManager.getBySection(section);
	}

	public Section getSectionById(long sectionId) {
		return sectionManager.getbyId(sectionId);
	}

	public Collection<Section> getAllSection() {
		return sectionManager.getAll();
	}
	
	public Collection<Section> getSectionsByMuseum(Museum museum) {
		return sectionManager.getByMuseum(museum);
	}
	
	public Collection<Section> getSectionBySection(Section section) {
		return sectionManager.getBySection(section);
	}
	
	public Collection<Section> getSectionByMuseumWithNoParent(Museum museum) {
		return sectionManager.getByMuseumWithParentNull(museum);
	}

	public void saveSection(Section section) {
		sectionManager.store(section);
	}

	public void saveExhibit(Exhibit exhibit) {
		exhibitManager.store(exhibit);
	}

	public Collection<Label> getAllLabels() {
		return labelManager.getAll();
	}

}
