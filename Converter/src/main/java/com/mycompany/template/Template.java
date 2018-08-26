/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.template;

/**
 *
 * @author Iustina
 */
public class Template {
    Coordinates coords;
    
    public Double xPage1, yPage1, widthPage1, heightPage1;
    public Double xPage2, yPage2, widthPage2, heightPage2;
    
    public Template(){
        this.coords = new Coordinates();
    }
    
    public Template(Double tcPage1H1, Double lcPage1H1, Double tcPage1H2, 
                    Double lcPage1H2, Double tcPage1F, Double lcPage1F, 
                    Double tcPage2H, Double lcPage2H){
        this.coords = new Coordinates();             
        this.coords = new Coordinates(tcPage1H1, lcPage1H1, tcPage1H2, lcPage1H2, tcPage1F, lcPage1F, tcPage2H, lcPage2H);
    }
    
    public int matchedTemplate(Double tcPage1H1, Double lcPage1H1, 
                        Double tcPage1H2, Double lcPage1H2, Double tcPage1F, 
                        Double lcPage1F, Double tcPage2H, Double lcPage2H){
        
        if(coords.topCoordPage1Header1.compareTo(tcPage1H1) == 0 &&
            coords.leftCoordPage1Header1.compareTo(lcPage1H1)== 0 &&
            coords.topCoordPage1Header2.compareTo(tcPage1H2) == 0 &&
            coords.leftCoordPage1Header2.compareTo(lcPage1H2) == 0&&    
            //coords.topCoordPage1Footer.compareTo(tcPage1F) == 0 &&
            //coords.leftCoordPage1Footer.compareTo(lcPage1F) == 0 &&
            coords.topCoordPage2Header.compareTo(tcPage2H)  == 0 &&
            coords.leftCoordPage2Header.compareTo(lcPage2H) == 0){

            return 1;
        }    

        return 0;
    }
}
