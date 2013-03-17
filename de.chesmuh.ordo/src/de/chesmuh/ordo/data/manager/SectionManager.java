package de.chesmuh.ordo.data.manager;

import java.util.Collection;
import java.util.HashSet;

import de.chesmuh.ordo.data.sql.SQLQuerySection;
import de.chesmuh.ordo.entity.Museum;
import de.chesmuh.ordo.entity.Section;

public class SectionManager extends AbstractManager<Section>
{

    public SectionManager()
    {
        super(SQLQuerySection.class);
    }

    /**
     * Gets all sections that do belong to the museum with given id
     *
     * @param id the museum-id
     * @return the sections of the museum
     */
    public Collection<Section> getByMuseumId(Long id)
    {
        HashSet<Section> ret = new HashSet<>();
        for (Section s : this.getAll())
        {
            if (s.getMuseum_id() == id)
            {
                ret.add(s);
            }
        }
        return ret;
    }

    /**
     * Gets all sections that do belong to the given museum
     *
     * @param museum the museum
     * @return the sections of the museum
     */
    public Collection<Section> getByMuseum(Museum museum)
    {
        return this.getByMuseumId(museum.getId());
    }

    /**
     * Gets all sections that have given parent-section-id
     *
     * @param id the parent-section-id
     * @return the sub-sections
     */
    public Collection<Section> getByParentSectionId(Long id)
    {
        HashSet<Section> ret = new HashSet<>();
        for (Section s : this.getAll())
        {
            if (s.getParent_id() == id)
            {
                ret.add(s);
            }
        }
        return ret;
    }

    /**
     * Gets all sections that have given parent-section
     *
     * @param section the parent-section
     * @return the sub-sections
     */
    public Collection<Section> getByParentSection(Section section)
    {
        return this.getByParentSectionId(section.getId());
    }

    public Collection<Section> getByName(String name)
    {
        HashSet<Section> result = new HashSet<>();
        for (Section step : this.getAll())
        {
            if (step.getName().toLowerCase().contains(name.toLowerCase()))
            {
                result.add(step);
            }
        }
        return result;
    }

    public Collection<Section> getAllSubSections(long id)
    {
        HashSet<Section> result = new HashSet<>();
        result.add(this.getbyId(id));
        return this.getAllSubSections(result, id);
    }

    private Collection<Section> getAllSubSections(HashSet<Section> result, long id)
    {
        Collection<Section> temp = this.getByParentSectionId(id);
        result.addAll(temp);
        for (Section section : temp)
        {
            this.getAllSubSections(result, section.getId());
        }
        return result;
    }
}
