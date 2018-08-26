/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.client;

import com.readerbench.coreservices.data.AbstractDocumentTemplate;
import com.readerbench.coreservices.data.document.Document;
import com.readerbench.coreservices.semanticmodels.SemanticModel;
import com.readerbench.datasourceprovider.pojo.Lang;
import com.readerbench.processingservice.Annotators;
import com.readerbench.processingservice.document.DocumentProcessingPipeline;

import java.util.Arrays;
import java.util.List;
import org.omg.SendingContext.RunTime;

/**
 *
 * @author Iustina
 */

public class ProcessingDocument {
    protected Lang lang;
    protected List<SemanticModel> models;
    protected List<Annotators> annotators;

    public ProcessingDocument() {
        lang = Lang.en;
        models = SemanticModel.loadModels("tasa", lang);
        annotators = Arrays.asList(Annotators.NLP_PREPROCESSING, Annotators.DIALOGISM, Annotators.TEXTUAL_COMPLEXITY);
    }

    public void createDocumentText(String doc) {
        try{
            System.out.println("1");
            DocumentProcessingPipeline pipeline = new DocumentProcessingPipeline(lang, models, annotators);
            
            System.out.println("2");
            Document d = pipeline.createDocumentFromTemplate(AbstractDocumentTemplate.getDocumentModel(doc)); 
            
            System.out.println("3");
            pipeline.processDocument(d);
            //Assert.assertEquals(2, d.getNoBlocks());
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public static void main(String args[]){
        int mb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        
        System.out.println("runtime max memory = " + rt.maxMemory() / mb);
        
        
        System.out.println("BEGIN");
        ProcessingDocument pd = new ProcessingDocument();
        
        System.out.println("AFTER INITIALIZATION");
        String doc = "ABSTRACT\n" + "On 21 May 1981 the WHO International Code of\n" +
                            "Marketing Breast Milk Substitutes (hereafter referred\n" +
                            "to as the Code) was passed by 118 votes to 1, the US\n" +
                            "casting the sole negative vote. The Code arose out\n" +
                            "of concern that the dramatic increase in mortality,\n" +
                            "malnutrition and diarrhoea in very young infants in\n" +
                            "the developing world was associated with aggressive\n" +
                            "marketing of formula. The Code prohibited any\n" +
                            "advertising of baby formula, bottles or teats and gifts\n" +
                            "to mothers or ‘bribery’ of health workers. Despite\n" +
                            "successes, it has been weakened over the years by\n" +
                            "the seemingly inexhaustible resources of the global\n" +
                            "pharmaceutical industry. This article reviews the long\n" +
                            "and tortuous history of the Code through the Convention\n" +
                            "on the Rights of the Child, the HIV pandemic and the rare\n" +
                            "instances when substitute feeding is clearly essential.\n" +
                            "Currently, suboptimal breastfeeding is associated with\n" +
                            "over a million deaths each year and 10% of the global\n" +
                            "disease burden in children. All health workers need to\n" +
                            "recognise inappropriate advertising of formula, to report\n" +
                            "violations of the Code and to support efforts to promote\n" +
                            "breastfeeding: the most effective way of preventing\n" +
                            "child mortality throughout the world";
        
        pd.createDocumentText(doc);
        System.out.println("THE END");
    }
}
