package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.entities.Censure;
import java.lang.reflect.InvocationTargetException;

import com.rintio.elastic.client.ElasticClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by rintio on 11/27/17.
 */
@Component
public class CensureService implements DatabaseConstants {
    ElasticClient elasticClient;
    public CensureService(){
        elasticClient = new ElasticClient();
    }
    /**
     * Insert the censor in the database
     *
     * @param censure
     * @return the censor
     * @throws SQLException
     */
    public Censure create(Censure censure) throws Exception {
        censure.setDateCreation(System.currentTimeMillis());
        censure.setDateMiseAJour(System.currentTimeMillis());
        CensureCRUD.create(censure);
        createAllIndexCensure();
        return censure;
    }

    /**
     * Update the censor in the database
     *
     * @param censure
     * @return the censor
     * @throws SQLException
     */
    public Censure update(Censure censure) throws Exception {
        CensureCRUD.update(censure);
        censure.setDateMiseAJour(System.currentTimeMillis());
        createAllIndexCensure();
        return censure;
    }

    /**
     * Delete the censor in the database
     *
     * @param id
     * @return the censor
     * @throws SQLException
     */
    public boolean delete(int id) throws Exception {
        Censure censure = read(id);
        if( CensureCRUD.delete(censure))
            createAllIndexCensure();
        return  true;
    }

    /**
     * Get a censor by its id
     *
     * @param id
     * @return censor
     * @throws SQLException
     */
    public Censure read(int id) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria("id", "=", id));
//        List<Censure> censures = CensureCRUD.read(criterias);
        Censure censure = getAllCensure().stream().filter(t->t.getId()==id).findFirst().get();
        return censure;
    }

    /**
     * Get all the censors of a given entity
     *
     * @param entityId
     * @param entityType
     * @return censors list
     * @throws SQLException
     */
    public List<Censure> getByEntity(int entityId, EntityType entityType) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
//        return CensureCRUD.read(criterias);
        return getAllCensure().stream().filter(t->t.getEntityId()==entityId && t.getEntityType().equals(entityType)).collect(Collectors.toList());
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
    public List<Censure> readAll(HttpServletRequest request) throws Exception, IllegalAccessException, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Censure> censures = null;
        if (criterias == null && fields == null) {
            censures = getAllCensure();
        } else if (criterias != null && fields == null) {
            censures = CensureCRUD.read(criterias);
        } else if (criterias == null && fields != null) {
            censures = CensureCRUD.read(fields);
        } else {
            censures = CensureCRUD.read(criterias, fields);
        }

        return censures;
    }

    public List<Censure> getAllCensure() throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Object> objects = elasticClient.getAllObject("censures");
        ModelMapper modelMapper = new ModelMapper();
        List<Censure> rest = new ArrayList<>();
        for(Object obj:objects){
            rest.add(modelMapper.map(obj,Censure.class));
        }
        return rest;
    }

    public boolean createIndexCensure()throws Exception{
//        ElasticClient elasticClient = new ElasticClient();
        List<Censure> censures = CensureCRUD.read();
        for (Censure censure : censures){
            elasticClient.creerIndexObjectNative("censures","censure",censure,censure.getId());
//            LOG.info("censure "+censure.getId()+" created");
        }
        return true;
    }

//    public void createCensureIndex(Censure censure) throws Exception{
//        ElasticClient elasticClient = new ElasticClient();
//        elasticClient.creerIndexObject("censures","censure",censure,censure.getId());
//
//    }

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

    public boolean createAllIndexCensure()throws Exception{
        List<Censure> censures = CensureCRUD.read();
        for (Censure censure : censures){
            elasticClient.creerIndexObjectNative("censures","censure",censure,censure.getId());
        }
        return true;
    }

}
