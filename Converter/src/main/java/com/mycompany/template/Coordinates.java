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
public class Coordinates {
    public Double topCoordPage1Header1;
    public Double leftCoordPage1Header1;
    public Double topCoordPage1Header2;
    public Double leftCoordPage1Header2;
    public Double topCoordPage1Footer;
    public Double leftCoordPage1Footer;
    
    public Double topCoordPage2Header;
    public Double leftCoordPage2Header;

    public Coordinates(){}
    
    public Coordinates(Double topCoordPage1Header1, Double leftCoordPage1Header1, 
            Double topCoordPage1Header2, Double leftCoordPage1Header2, 
            Double topCoordPage1Footer, Double leftCoordPage1Footer, 
            Double topCoordPage2Header, Double leftCoordPage2Header) {
    
        this.topCoordPage1Header1 = topCoordPage1Header1;
        this.leftCoordPage1Header1 = leftCoordPage1Header1;
        this.topCoordPage1Header2 = topCoordPage1Header2;
        this.leftCoordPage1Header2 = leftCoordPage1Header2;
        this.topCoordPage1Footer = topCoordPage1Footer;
        this.leftCoordPage1Footer = leftCoordPage1Footer;
        this.topCoordPage2Header = topCoordPage2Header;
        this.leftCoordPage2Header = leftCoordPage2Header;
    }
}
