package com.bootcamp.integration;

import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.entities.Censure;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jayway.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * <h2> The integration test for Censure controller</h2>
 * <p>
 * In this test class, the methods :
 * <ul>
 * <li>create a censure </li>
 * <li>get one censure by it's id</li>
 * <li>get all censure</li>
 * <li>And update a censure have been implemented</li>
 * </ul>
 * before getting started , make sure , the censure fonctionnel service is
 * deploy and running as well. you can also test it will the online ruuning
 * service As this test interact directly with the local database, make sure
 * that the specific database has been created and all it's tables. If you have
 * data in the table,make sure that before creating a data with it's id, do not
 * use an existing id.
 * </p>
 */
public class CensureControllerIntegrationTest {

    private static Logger logger = LogManager.getLogger(CensureControllerIntegrationTest.class);

    /**
     * The Base URI of censure fonctionnal service, it can be change with
     * the online URIof this service.
     */
    private String BASE_URI = "http://localhost:8089/censure";

    /**
     * The path of the Censure controller, according to this controller
     * implementation
     */
    private String CENSURE_PATH = "/censures";

    /**
     * This ID is initialize for create , getById, and update method, you have
     * to change it if you have a save data on this ID otherwise a error or
     * conflit will be note by your test.
     */
    private int censureId = 0;


    /* @BeforeTest
    public void count() throws Exception{
       int totalData = new CensureService().getCountCensures();
       censureId=totalData;
       logger.info( censureId );
   }*/
    /**
     * This method create a new censure with the given id
     *
     * @see Censure#id
     * <b>you have to chenge the name of the censure if this name already
     * exists in the database
     * @see Censure#getMessage() else, the censure will be created but not
     * wiht the given ID. and this will accure an error in the getById and
     * update method</b>
     * Note that this method will be the first to execute If every done , it
     * will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 0, groups = {"CensureTest"})
    public void createCensure() throws Exception {
        String createURI = BASE_URI + CENSURE_PATH;
        Censure censure = getCensureById(1);
        censure.setId(censureId);
        censure.setMessage("censure test after the doc");
        Gson gson = new Gson();
        String censureData = gson.toJson(censure);
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(censureData)
                .expect()
                .when()
                .post(createURI);

        censureId = gson.fromJson(response.getBody().print(), Censure.class).getId();

        logger.debug(censureId);
        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * This method get a censure with the given id
     *
     * @see Censure#id
     * <b>
     * If the given ID doesn't exist it will log an error
     * </b>
     * Note that this method will be the second to execute If every done , it
     * will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 1, groups = {"CensureTest"})
    public void getCensureById() throws Exception {

        String getCensureById = BASE_URI + CENSURE_PATH + "/" + censureId;

        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getCensureById);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Get All the censures in the database If every done , it will return a
     * 200 httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 2, groups = {"CensureTest"})
    public void getAllCensures() throws Exception {
        String getAllCensureURI = BASE_URI + CENSURE_PATH;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getAllCensureURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Delete a censure for the given ID will return a 200 httpStatus code
     * if OK
     *
     * @throws Exception
     */
    @Test(priority = 3, groups = {"CensureTest"})
    public void deleteCensure() throws Exception {

        String deleteCensureUI = BASE_URI + CENSURE_PATH + "/" + censureId;

        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .delete(deleteCensureUI);

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Get All the censures related to a specify entity in the database If
     * every done , it will return a 200 httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 4, groups = {"CensureTest"})
    public void getCensuresByEntity() throws Exception {
        String getCensuresByEntityURI = BASE_URI + CENSURE_PATH + "/PROJET/7";
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getCensuresByEntityURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Convert a relative path file into a File Object type
     *
     * @param relativePath
     * @return File
     * @throws Exception
     */
    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    /**
     * Get on censure by a given ID from the List of censures
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Censure getCensureById(int id) throws Exception {
        List<Censure> censures = loadDataCensureFromJsonFile();
        Censure censure = censures.stream().filter(item -> item.getId() == id).findFirst().get();

        return censure;
    }

    /**
     * Convert a censures json data to a censure objet list this json
     * file is in resources
     *
     * @return a list of censure in this json file
     * @throws Exception
     */
    public List<Censure> loadDataCensureFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "censures.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Censure>>() {
        }.getType();
        List<Censure> censures = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return censures;
    }

}
