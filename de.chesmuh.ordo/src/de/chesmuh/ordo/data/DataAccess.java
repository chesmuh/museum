package de.chesmuh.ordo.data;

import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.manager.ExhibitManager;
import de.chesmuh.ordo.data.manager.MuseumManager;
import de.chesmuh.ordo.data.manager.SectionManager;
import de.chesmuh.ordo.data.manager.TagManager;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.entitys.Tag;

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
	private TagManager tagManager;
	
	private DataAccess() {
		museumManager = new MuseumManager();
		museumManager.loadAll();
		sectionManager = new SectionManager();
		sectionManager.loadAll();
		exhibitManager = new ExhibitManager();
		exhibitManager.loadAll();
		tagManager = new TagManager();
		tagManager.loadAll();
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

	public Collection<Exhibit> getExhibitsByMuseumWithSectionNull(Museum museum) {
		return exhibitManager.getByMuseumWithSectionNull(museum);
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

	public Collection<Tag> getAllLabels() {
		return tagManager.getAll();
	}

	public Exhibit getExhibitById(Long id) {
		return exhibitManager.getbyId(id);
	}

	public void deleteExhibits(Collection<Exhibit> toDelete) {
		for(Exhibit e : toDelete) { 
			exhibitManager.markAsDeleted(e);
		}
	}

	public Tag getTagById(Long id) {
		return tagManager.getbyId(id);
	}

	public void saveTag(Tag tag) {
		tagManager.store(tag);
	}

	public void addExhibitToTag(Tag tag, Exhibit exhibit) {
		tag.getExhibit_ids().add(exhibit.getId());
		tagManager.update(tag);
	}

	public void deleteTags(ArrayList<Tag> toDelete) {
		for(Tag tag : toDelete) {
			tagManager.markAsDeleted(tag);
		}
	}

	public void deleteSection(Section section) {
		sectionManager.markAsDeleted(section);
	}

	public void deleteMuseum(Museum museum) {
		museumManager.markAsDeleted(museum);
	}

	public ArrayList<Exhibit> getExhibitBySectionWithSubSections(Section section) {
		ArrayList<Exhibit> ret = new ArrayList<Exhibit>();
		Collection<Section> sectionBySection = DataAccess.getInstance().getSectionBySection(section);
		for(Section subSection : sectionBySection) {
			ret.addAll(DataAccess.getInstance().getExhibitBySectionWithSubSections(subSection));
		}
		ret.addAll(DataAccess.getInstance().getExhibitBySection(section));
		return ret;
	}

	public Collection<Section> getSectionBySectionWithSubSections(
			Section section) {
		ArrayList<Section> ret = new ArrayList<Section>();
		Collection<Section> sectionBySection = DataAccess.getInstance().getSectionBySection(section);
		ret.add(section);
		for(Section subSection : sectionBySection) {
			ret.addAll(DataAccess.getInstance().getSectionBySectionWithSubSections(subSection));
		}
		return ret;
	}

	public void saveMuseum(Museum museum) {
		museumManager.store(museum);
	}

	public void updateMuseum(Museum museum) {
		museumManager.update(museum);
	}

	public void updateTag(Tag tag) {
		tagManager.update(tag);
	}

}
