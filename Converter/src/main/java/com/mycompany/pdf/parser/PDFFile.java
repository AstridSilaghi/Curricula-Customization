/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pdf.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Iustina
 */
public class PDFFile {
   
    String title;
    List <String> authors;
    String content;
    List <String> properties;
    
    public PDFFile(String title, List<String> authors, String content){
        this.title = title;
        this.authors = authors;
        this.content = content;
        
        /* The order of the added properties is very IMPORTANT*/
        this.properties = new ArrayList<>(Arrays.asList("title", "authors", "content"));
    }
    
    public List<String> getProperties(){
        return properties;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }  
}
