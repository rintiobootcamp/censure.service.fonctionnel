package com.bootcamp.controllers;

import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.entities.Censure;
import com.bootcamp.services.CensureService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bello
 */
@RestController("CensureController")
@RequestMapping("/censures")
@Api(value = "Censure API", description = "Censure API")
@CrossOrigin(origins = "*")
public class CensureController {

    @Autowired
    CensureService censureService;

    @Autowired
    HttpServletRequest request;

    /**
     * Create a censor in the database
     *
     * @param censure
     * @return the created censor
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Create a new censure", notes = "Create a new censure")
    public ResponseEntity<Censure> create(@RequestBody @Valid Censure censure) {

        HttpStatus httpStatus = null;

        try {
            censure = censureService.create(censure);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CensureController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<Censure>(censure, httpStatus);
    }

    /**
     * Get a censor knowing its id
     *
     * @param id
     * @return censure
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a censures", notes = "Read a censures")
    public ResponseEntity<Censure> read(@PathVariable(name = "id") int id) {

        Censure censure = new Censure();
        HttpStatus httpStatus = null;

        try {
            censure = censureService.read(id);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CensureController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Censure>(censure, httpStatus);
    }

    /**
     * Get all the censors of a given entity
     *
     * @param entityId
     * @param entityType
     * @return censors list
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{entityType}/{entityId}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a censures", notes = "Read a censures")
    public ResponseEntity<List<Censure>> readByEntity(@PathVariable("entityId") int entityId, @PathVariable("entityType") String entityType) {
        EntityType entite = EntityType.valueOf(entityType);
        List<Censure> censure = new ArrayList<Censure>();
        HttpStatus httpStatus = null;

        try {
            censure = censureService.getByEntity(entityId, entite);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CensureController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<List<Censure>>(censure, httpStatus);
    }
    
        /**
     * Get all the censors of the database
     *
     * @return censures list
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get list of censure by request", notes = "Get list of censures by request")
    public ResponseEntity<List<Censure>> findAll() throws Exception {
        HttpStatus httpStatus = null;
        List<Censure> censures = censureService.readAll(request);
        httpStatus = HttpStatus.OK;
        return new ResponseEntity<List<Censure>>(censures, httpStatus);
    }

    /**
     * Delete a comment by its id
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "delete censure ", notes = "delete censure by id")
    public ResponseEntity<Censure> delete(@PathVariable int id) throws Exception {
        HttpStatus httpStatus = null;
        Censure censure = censureService.delete(id);
        httpStatus = HttpStatus.OK;
        return new ResponseEntity<Censure>(censure, httpStatus);
    }
}
