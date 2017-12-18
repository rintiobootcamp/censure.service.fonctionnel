package com.bootcamp.Unitaire.Service;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.services.CensureService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

//import org.junit.Test;
//import org.testng.annotations.Test;

/**
 * Created by darextossa on 12/9/17.
 */

@RunWith(PowerMockRunner.class)
@WebMvcTest(value = CensureService.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(CensureCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class CensureServiceTest {

    @InjectMocks
    private CensureService censureService;


    @Test
    public void create() throws Exception{
        List<Censure> censures = loadDataCensureFromJsonFile();
        for (Censure censure : censures) {
//            preference = preferences.get(1);
             PowerMockito.mockStatic(CensureCRUD.class);
        Mockito.
                when(CensureCRUD.create(censure)).thenReturn(true);
        }
             
    }

    @Test
    public void delete() throws Exception{
        List<Censure> censures = loadDataCensureFromJsonFile();
        Censure censure = censures.get(1);

        PowerMockito.mockStatic(CensureCRUD.class);
        Mockito.
                when(CensureCRUD.delete(censure)).thenReturn(true);
    }

    @Test
    public void update() throws Exception{
        List<Censure> censures = loadDataCensureFromJsonFile();
        Censure censure = censures.get(1);

        PowerMockito.mockStatic(CensureCRUD.class);
        Mockito.
                when(CensureCRUD.update(censure)).thenReturn(true);
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

}