/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.template;

import com.mycompany.template.templates.NHMTemplate;
import com.mycompany.template.templates.BMCPregnancyChilbirthTemplate;
import com.mycompany.template.templates.ReviewTemplate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Iustina
 */

public final class TemplatesManagement {
    List <Template> template;
        
    public TemplatesManagement(){
        template = new ArrayList<>();
        initExistingTemplates();
    }
    
    public void initExistingTemplates(){
        template.add(new NHMTemplate());
        template.add(new BMCPregnancyChilbirthTemplate());
        template.add(new ReviewTemplate());
    }
    
    public void addTemplate(Template t){
        template.add(t);
    }
    
    public Template getTemplate(Double tcPage1H1, Double lcPage1H1, 
                        Double tcPage1H2, Double lcPage1H2, Double tcPage1F, 
                        Double lcPage1F, Double tcPage2H, Double lcPage2H){
        
        for(Template t : template){
            if(t.matchedTemplate(tcPage1H1, lcPage1H1, tcPage1H2, lcPage1H2, 
                tcPage1F, lcPage1F, tcPage2H, lcPage2H) == 1){

                return t;
            }
        }
        
        return null;
    }
}
