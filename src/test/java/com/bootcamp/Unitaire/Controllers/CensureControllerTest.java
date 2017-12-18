package com.bootcamp.Unitaire.Controllers;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.controllers.CensureController;
import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.entities.Programme;
import com.bootcamp.services.CensureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.LogManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Archange on 17/12/2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = CensureController.class, secure = false)
@ContextConfiguration(classes = {Application.class})
public class CensureControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CensureService censureService;

    private static org.apache.log4j.Logger LOG= LogManager.getLogger(CensureControllerTest.class);

    /**
     * get censure by id
     * @throws Exception
     */

    @Test
    public void getCensureByEntity() throws Exception{
        List<Censure> censures =  loadDataCensureFromJsonFile();

        EntityType pro = EntityType.SECTEUR;
        int id = 2;
        System.out.println(censures.size());
        List<Censure> resultcomm = getCensureByEnity(pro.toString(),id);
        Mockito.
                when(censureService.getByEntity(id,pro)).thenReturn(resultcomm);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/censures/{entityType}/{entityId}",pro,id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

    }


    /**
     *create censure
     * @throws Exception
     */
    @Test
    public void createCensure() throws Exception {
        List<Censure> censures = loadDataCensureFromJsonFile();
        Censure censure = censures.get(0);

        when(censureService.read(0)).thenReturn(censure);
        when(censureService.create(censure)).thenReturn(censure);
        RequestBuilder requestBuilder
                = post("/censures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(censure));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        System.out.println(response.getContentAsString());

        System.out.println("*********************************Test for create censure controller done *******************");
    }


    /**
     * *get all censure
     * @throws Exception
     */
    @Test
    public void getAllCensure() throws Exception{
        LOG.info("Testing get all censure test method ");
        List<Censure> censures =  loadDataCensureFromJsonFile();
        System.out.println(censures.size());
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.
                when(censureService.readAll(Mockito.any(HttpServletRequest.class))).thenReturn(censures);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/censures")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        LOG.info(response.getContentAsString());
        System.out.println(response.getContentAsString());
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

    }

    /**
     * *get censure by id
     * @throws Exception
     */
    @Test
    public void getCensureByIdForController() throws Exception{
        int id = 1;
        Censure censure = getCensureById(id);
        when(censureService.read(id)).thenReturn(censure);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/censures/{id}",id)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getContentAsString());
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for get a censure by id in censure controller done *******************");

    }

    /**
     * *delete censure
     * @throws Exception
     */

    @Test
    public void deleteCensure() throws Exception {
        int id = 1;
        when(censureService.exist(id)).thenReturn(true);
        when(censureService.delete(id)).thenReturn(true);
        RequestBuilder requestBuilder
                = delete("/censures/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getContentAsString());
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for delete censure  controller done *******************");
    }



    private List<Censure> getCensureByEnity(String entityType,int entityId) throws Exception {
        List<Censure> censures = loadDataCensureFromJsonFile();
        List<Censure> result = censures.stream().filter(item -> item.getEntityType().equals(entityType) && item.getEntityId()==entityId).collect(Collectors.toList());
        return result;
    }

    private Censure getCensureById(int id) throws Exception {
        List<Censure> censures = loadDataCensureFromJsonFile();
        Censure censure = censures.stream().filter(item -> item.getId() == id).findFirst().get();
        return censure;
    }


    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    public List<Censure> loadDataCensureFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "censures.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Censure>>() {
        }.getType();
        List<Censure> censures = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return censures;
    }
/*
    @Test
    public void getAllCensure() throws Exception {
        List<Censure> censures = loadDataCensureFromJsonFile();
        PowerMockito.mockStatic(CensureCRUD.class);
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.
                when(CensureCRUD.read()).thenReturn(censures);
        List<Censure> resultCensure = censureService.readAll(mockRequest);
        Assert.assertNotNull(resultCensure);
    }
  */
    private static String objectToJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
