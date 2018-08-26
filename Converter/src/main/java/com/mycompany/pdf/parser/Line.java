/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pdf.parser;

/**
 *
 * @author Iustina
 */
public class Line {
    Double fontSize;
    Double lineHeight;
    Double topCoord, leftCoord;
    Double areaHeight;
    String text;

    public Line(Double fontSize, Double lineHeight, Double topCoord, Double leftCoord, String text) {

        this.fontSize = fontSize;
        this.lineHeight = lineHeight;
        this.topCoord = topCoord;
        this.leftCoord = leftCoord;
        this.text = text;
        this.areaHeight = lineHeight;
    }
    
    public Double getFontSize() {
        return fontSize;
    }

    public void setFontSize(Double fontSize) {
        this.fontSize = fontSize;
    }

    public Double getTopCoord() {
        return topCoord;
    }

    public void setTopCoord(Double topCoord) {
        this.topCoord = topCoord;
    }

    public Double getLeftCoord() {
        return leftCoord;
    }

    public void setLeftCoord(Double leftCoord) {
        this.leftCoord = leftCoord;
    }

    public Double getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(Double lineHeight) {
        this.lineHeight = lineHeight;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
