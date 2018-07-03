package com.bootcamp;

import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.crud.PhaseCRUD;
import com.bootcamp.crud.ProjetCRUD;
import com.bootcamp.crud.RegionCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.entities.Phase;
import com.bootcamp.entities.Projet;
import com.bootcamp.entities.Region;
import com.rintio.elastic.client.ElasticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

public class ElasticTest {
    private final Logger LOG = LoggerFactory.getLogger(ElasticTest.class);


    @Test
    public void createIndexCensure()throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Censure> censures = CensureCRUD.read();
        for (Censure censure : censures){
            elasticClient.creerIndexObjectNative("censures","censure",censure,censure.getId());
            LOG.info("censure "+censure.getId()+" created");
        }
    }
}
