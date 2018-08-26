/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pdf.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Iustina
 */
public class Page {
    int number;
    List<Line> lines = new ArrayList<>();
    List <Line> lastLines;
    
    
    public Page(int number){
        this.number = number;
        lastLines = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLastLines() {
        return lastLines;
    }

    public void setLastLines(List<Line> lastLines) {
        this.lastLines = lastLines;
    }
}
