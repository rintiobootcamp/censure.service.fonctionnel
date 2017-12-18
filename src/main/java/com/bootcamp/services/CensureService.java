package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.entities.Censure;
import java.lang.reflect.InvocationTargetException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class CensureService implements DatabaseConstants {

    /**
     * Insert the censor in the database
     *
     * @param censure
     * @return the censor
     * @throws SQLException
     */
    public Censure create(Censure censure) throws SQLException {
        censure.setDateCreation(System.currentTimeMillis());
        censure.setDateMiseAJour(System.currentTimeMillis());
        CensureCRUD.create(censure);
        return censure;
    }

    /**
     * Update the censor in the database
     *
     * @param censure
     * @return the censor
     * @throws SQLException
     */
    public Censure update(Censure censure) throws SQLException {
        CensureCRUD.update(censure);
        censure.setDateMiseAJour(System.currentTimeMillis());
        return censure;
    }

    /**
     * Delete the censor in the database
     *
     * @param id
     * @return the censor
     * @throws SQLException
     */
    public boolean delete(int id) throws SQLException {
        Censure censure = read(id);
        return CensureCRUD.delete(censure);
    }

    /**
     * Get a censor by its id
     *
     * @param id
     * @return censor
     * @throws SQLException
     */
    public Censure read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Censure> censures = CensureCRUD.read(criterias);
        return censures.get(0);
    }

    /**
     * Get all the censors of a given entity
     *
     * @param entityId
     * @param entityType
     * @return censors list
     * @throws SQLException
     */
    public List<Censure> getByEntity(int entityId, EntityType entityType) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        return CensureCRUD.read(criterias);
    }
    
        /**
     * Get all the censors of the database matching the given request
     *
     * @param request
     * @return censures list
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws DatabaseException
     * @throws InvocationTargetException
     */
    public List<Censure> readAll(HttpServletRequest request) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Censure> censures = null;
        if (criterias == null && fields == null) {
            censures = CensureCRUD.read();
        } else if (criterias != null && fields == null) {
            censures = CensureCRUD.read(criterias);
        } else if (criterias == null && fields != null) {
            censures = CensureCRUD.read(fields);
        } else {
            censures = CensureCRUD.read(criterias, fields);
        }

        return censures;
    }

    /**
     * Check if a program exist in the database
     *
     * @param id
     * @return
     * @throws Exception
     */
    public boolean exist(int id) throws Exception {
        if (read(id) != null) {
            return true;
        }
        return false;
    }

}
