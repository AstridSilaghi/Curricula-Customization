/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.client;

import com.mycompany.pdf.parser.PDFFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 *
 * @author Iustina
 */
public class ElasticSearchRestClient {
    
    public static Integer ELASTICSEARCH_PORT = 9200;
    public static String ELASTICSEARCH_ADDR = "127.0.0.1";
    public static String ELASTICSEARCH_CLUSTER_NAME = "elasticsearch";
    public static String ELASTICSEARCH_INDEX = "articles";
    public static String ELASTICSEARCH_INDEX_PROCESSED_CONTENT = "processed_articles";
    public static String ELASTICSEARCH_TYPE = "pdf";
    
    public static String ELASTICSEARCH_METHOD_REQUEST_POST = "POST";
    public static String ELASTICSEARCH_METHOD_REQUEST_PUT = "PUT";
    public static String ELASTICSEARCH_METHOD_REQUEST_GET = "GET";
    public static String ELASTICSEARCH_METHOD_REQUEST_DELETE = "DELETE";
    
    public static RestClient restClient;
    public static Log log = LogFactory.getLog(ElasticSearchRestClient.class);
    
    public static Map<String, Object> generateMap(List<PDFFile> pdfArray){
        Map<String, Object> map = new HashMap<>();
        int i;

        for(i = 0; i < pdfArray.size(); i++){
            map.put(Integer.toString(i), pdfArray.get(i));
        }
        
        return map;
    }
    
    public static ArrayList<PDFFile> getPDFFilesList() throws IOException{

        return(ArrayList<PDFFile>) com.mycompany.pdf.parser.PDFParser.getParsedFiles();
    }
    
    public static void deleteIndex(String index) throws IOException{
        
        Response response = restClient.performRequest(ELASTICSEARCH_METHOD_REQUEST_DELETE, index);
        HttpEntity entity = response.getEntity();
    }
    
    public static void initClient(){
        restClient = RestClient.builder(new HttpHost(ELASTICSEARCH_ADDR, ELASTICSEARCH_PORT, "http")).build();
    }
    
    public static void mappingProperties(String index) throws IOException{
        
        HttpEntity entity = new NStringEntity(
            "{\n" +
            "  \"mappings\": {\n" +
            "    \"pdf\": {\n" +
            "      \"properties\": {\n" +
            "        \"title\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"content\" : {\n" +
            "          \"type\" : \"text\"\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}"
           , ContentType.APPLICATION_JSON);

        Response indexResponse = restClient.performRequest(ELASTICSEARCH_METHOD_REQUEST_PUT,
            index,
            Collections.<String, String>emptyMap(),
            entity);

        System.out.println(EntityUtils.toString(indexResponse.getEntity()));
    }
    
    public static void indexPDFAttributes(String index, List<PDFFile> pdfFilesList) throws IOException{
        Response indexResponse;
        int i;
        
        /* Prepare params for HttpEntity*/
        List<Map<String, Object>> files = new LinkedList<>();
        files.add(generateMap(pdfFilesList));
        String index_type_uri;
        
        for(i = 0; i < pdfFilesList.size(); i++){
            
            HttpEntity entity = new NStringEntity(
                generateJsonContent(pdfFilesList.get(i))    
               , ContentType.APPLICATION_JSON);
            
            index_type_uri = "/" + index + "/" +  ELASTICSEARCH_TYPE + "/";

            indexResponse = restClient.performRequest(ELASTICSEARCH_METHOD_REQUEST_PUT,
                index_type_uri,
                Collections.<String, String>emptyMap(),
                entity);
            
            System.out.println(EntityUtils.toString(indexResponse.getEntity()));
        }
    }
    
    public static String generateJsonContent(PDFFile pdfFile){
        int i = 0, authorsListSize = 0;
        String jsonContent = "";
        
        jsonContent = jsonContent.concat("{\n");
        jsonContent = jsonContent.concat("\"" + pdfFile.getProperties().get(i++) + "\" : \"" + pdfFile.getTitle() + "\",\n");
        
        /* If the size of the list of authors is 1, behave like a normal property */
        if(pdfFile.getAuthors().size() == 1){
            jsonContent = jsonContent.concat("\"" + pdfFile.getProperties().get(i++) + "\" : \"" + pdfFile.getAuthors()+ "\",\n");
       /* If there is more tha one author, behave like a list*/
        }else{
            jsonContent = jsonContent.concat("\"" + pdfFile.getProperties().get(i++) + "\" :");
            jsonContent = jsonContent.concat("[ ");
            authorsListSize = pdfFile.getAuthors().size();
            for(int j = 0; j < authorsListSize; j++){
                jsonContent = jsonContent.concat(" \"" + pdfFile.getAuthors().get(j) + "\"");
                
                /* If it's not the last element, add comma */
                if(j < authorsListSize - 1){
                    jsonContent = jsonContent.concat(",");
                }
            }
            jsonContent = jsonContent.concat(" ],\n");
        }
            
        jsonContent = jsonContent.concat("\"" + pdfFile.getProperties().get(i++) + "\" : \"" + pdfFile.getContent()+ "\"\n");
        jsonContent = jsonContent.concat("}");
        
        return jsonContent;
    }
    
    public static void searchDocument(String index, String property, String data) throws IOException{

        HttpEntity entity = new NStringEntity(
            "{\n" +
            "  \"query\" : {\n" +
            "    \"bool\" : {\n" +
            "      \"must\" : {\n" +
            "        \"term\" : { \n" +
            "          \"" + property + "\" : \"" + data + "\"\n" +
            "          \n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}"
           , ContentType.APPLICATION_JSON);
       
        Response response = restClient.performRequest(ELASTICSEARCH_METHOD_REQUEST_GET,
            index + "/_search",
            Collections.<String, String>emptyMap(),
            entity);

        System.out.println("--------------------------------------");
        System.out.println(EntityUtils.toString(response.getEntity()));
        System.out.println("Host : " +  response.getHost());
        System.out.println("Request Line : " +  response.getRequestLine());
    }
    
    public static void searchDocumentListKeywords(String index, String property, List<String> listKeywords) throws IOException{
        String keywords = "";
        for(int i = 0; i < listKeywords.size(); i++){
            keywords = keywords.concat("\"" + listKeywords.get(i) + "\"");
            if(i != listKeywords.size() - 1){
                keywords = keywords.concat(",");
            }
        }
        
        HttpEntity entity = new NStringEntity(
           "{\n" +
            "  \"query\" :{\n" +
            "    \"terms\": {\n" +
            "      \"" + property + "\": [\n" +
                    keywords + 
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}"
           , ContentType.APPLICATION_JSON);
       
        Response response = restClient.performRequest(ELASTICSEARCH_METHOD_REQUEST_GET,
            index + "/_search",
            Collections.<String, String>emptyMap(),
            entity);

        System.out.println("--------------------------------------");
        System.out.println(EntityUtils.toString(response.getEntity()));
        System.out.println("Host : " +  response.getHost());
        System.out.println("Request Line : " +  response.getRequestLine());
    }
    
    public static void indexExists(String index) throws IOException{
        Map <String, String> paramMap = new HashMap<>();
        
        try{
            Response response = restClient.performRequest(ELASTICSEARCH_METHOD_REQUEST_GET, "/" + index + "/_search", paramMap);
            response.getEntity().isChunked();
        }catch(IOException e){
            System.out.println("Index exists and it has already the mapping configured");
        }
    }
    
    public static void main(String args[]) throws IOException, InterruptedException{
      
        PropertyConfigurator.configure("src\\main\\java\\rest\\client\\log4j.properties");
        int deleteIndex = 0;
        List<String> listKeywords = new ArrayList<>(Arrays.asList("ethnographic", "salivary", "oxytocin"));
        
        initClient();
        try{
            indexExists(ELASTICSEARCH_INDEX);
            mappingProperties(ELASTICSEARCH_INDEX);
        } catch (IOException ioe){
        }
        
        //List<PDFFile> pdfFilesList = getPDFFilesList();
        //indexPDFAttributes(ELASTICSEARCH_INDEX, pdfFilesList);
        
        String data = "Contradictions and conflict: A meta-ethnographic study of migrant women s experiences of breastfeeding in a new country";
        searchDocument(ELASTICSEARCH_INDEX, "title", data); 
        searchDocumentListKeywords(ELASTICSEARCH_INDEX, "content", listKeywords);
        /* Process content
            TO DO
        */
        
        //indexPDFAttributes(ELASTICSEARCH_INDEX_PROCESSED_CONTENT, pdfFilesList);
        
        try{
            if(deleteIndex == 1){
                deleteIndex(ELASTICSEARCH_INDEX);
                System.out.println("Index was successfully deleted");
            }
        } catch(IOException e) {
            System.out.println("Index does not exist");
        }
        
        restClient.close();
    }
}
