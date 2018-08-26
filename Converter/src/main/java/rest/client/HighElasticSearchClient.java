/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.client;

import com.mycompany.pdf.parser.PDFFile;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 *
 * @author Iustina
 */
public class HighElasticSearchClient {
    
    public static Integer ELASTICSEARCH_PORT = 9200;
    public static String ELASTICSEARCH_ADDR = "127.0.0.1";
    public static String ELASTICSEARCH_CLUSTER_NAME = "elasticsearch";
    public static String ELASTICSEARCH_INDEX = "articles2";
    public static String ELASTICSEARCH_INDEX_PROCESSED_CONTENT = "processed_articles";
    public static String ELASTICSEARCH_TYPE = "pdf";
    public static String ELASTICSEARCH_METHOD_REQUEST = "POST";
    
    //public static RestClient restClient;
    public static Log log = LogFactory.getLog(HighElasticSearchClient.class);
    
    
    public static ArrayList<PDFFile> getPDFFilesList() throws IOException{

        return(ArrayList<PDFFile>) com.mycompany.pdf.parser.PDFParser.getParsedFiles();
    }
    
    public static void index() throws IOException{
        CreateIndexRequest request = new CreateIndexRequest(ELASTICSEARCH_INDEX);
        
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject(ELASTICSEARCH_TYPE);
            {
                builder.startObject("properties");
                {
                    builder.startObject("title");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();
                    builder.startObject("content");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.mapping(ELASTICSEARCH_INDEX, builder);
        System.out.println("success");
    }
    
    public static void initializeClient(){
       RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
            new HttpHost(ELASTICSEARCH_ADDR, ELASTICSEARCH_PORT, "http")));
    }
    
    public static void main(String args[]) throws IOException{
        
        PropertyConfigurator.configure("src\\main\\java\\rest\\client\\log4j.properties");
        
        initializeClient();
        index();
    }
    
}
