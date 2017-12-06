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

@RestController("CensureController")
@RequestMapping("/censures")
@Api(value = "Censure API", description = "Censure API")
@CrossOrigin(origins = "*")
public class CensureController {

    @Autowired
    CensureService censureService;

    @Autowired
    HttpServletRequest request;

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Create a new censure", notes = "Create a new censure")
    public ResponseEntity<Censure> create(@RequestBody @Valid Censure comment) {

        HttpStatus httpStatus = null;

        try {
            comment = censureService.create(comment);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(CensureController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<Censure>(comment, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a comments", notes = "Read a comments")
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

    @RequestMapping(method = RequestMethod.GET, value = "/{entityType}/{entityId}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a comments", notes = "Read a comments")
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
}
