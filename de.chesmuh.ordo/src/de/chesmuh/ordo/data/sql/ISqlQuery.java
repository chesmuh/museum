package de.chesmuh.ordo.data.sql;

import java.sql.SQLException;
import java.util.Collection;

import de.chesmuh.ordo.entity.DatabaseElement;

/**
 * 
 * @author Chesmuh
 *
 * @param <D> {@link DatabaseElement}
 */
public interface ISqlQuery<D extends DatabaseElement>
{

    /**
     * Loads all models M from database
     *
     * @param local true for using local-database
     * @return a collection of the loaded models
     * @throws SQLException
     */
    Collection<D> loadAll() throws SQLException;

    /**
     * Updates the given model in the database
     *
     * @param local true for using local-database
     * @param model the model to update
     * @throws SQLException
     */
    void update(D element) throws SQLException;

    /**
     * Stores the given model into database and sets the model id Storing is
     * always in the local database
     *
     * @param model the model to store
     * @throws SQLException
     */
    void store(D element) throws SQLException;

    /**
     * Deletes the given model. Data cannot be retreived later!
     *
     * @param local true for using local-database
     * @param model the model to delete
     * @throws SQLException
     */
    void delete(D element) throws SQLException;

    /**
     * Gets a model directly from Database by Id
     *
     * @param local true for using local-database
     * @param id the model-id
     * @return the created model
     * @throws SQLException
     */
    D getById(Long id) throws SQLException;

    /**
     * Creates the tables
     * 
     * @throws SQLException 
     */
    public void createTables() throws SQLException;

    /**
     * Prepares the statements
     * @throws SQLException 
     */
    public void prepareStatements() throws SQLException;

    /**
     * Initializing the Queries
     * 
     * @throws SQLException 
     */
    public void init() throws SQLException;
}
