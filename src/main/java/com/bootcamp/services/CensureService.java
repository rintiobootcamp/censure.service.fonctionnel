package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.entities.Censure;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class CensureService implements DatabaseConstants {

    public Censure create(Censure censure) throws SQLException {
        censure.setDateCreation(System.currentTimeMillis());
        censure.setDateMiseAJour(System.currentTimeMillis());
        CensureCRUD.create(censure);
        return censure;
    }

    public Censure update(Censure censure) throws SQLException {
        CensureCRUD.update(censure);
        censure.setDateMiseAJour(System.currentTimeMillis());
        return censure;
    }

    public Censure delete(int id) throws SQLException {
        Censure censure = read(id);
        CensureCRUD.delete(censure);
        return censure;
    }

    public Censure read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Censure> censures = CensureCRUD.read(criterias);
        return censures.get(0);
    }
    
    public List<Censure> getByEntity(int entityId, EntityType entityType) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        return CensureCRUD.read(criterias);
    }
}