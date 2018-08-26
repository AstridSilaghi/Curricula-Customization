/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pdf.parser;

import com.mycompany.template.TemplatesManagement;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 *
 * @author Iustina
 */
public class PDFParser {
    
    
    public File f;
    public List<String> lines = new ArrayList<>();
    public static HashMap<Double, String> map = new HashMap<>();
    public int pagesNumber;
    public static Page[] pages;
    public static TemplatesManagement templatesMngmt = new TemplatesManagement();
    public static Double SPACING_ERROR = 2.0;
    public static String pdfsDirName = System.getProperty("user.dir") + "/src/main/resources/articles";
    
    public static final String DELIMITATOR = ",";
    
    public static String HTML_FILENAME = System.getProperty("user.dir") + "htmlFileConverted.txt"; 
    
    public PDFParser(){
    }
    
    public static int getPagesNumber(String filename) throws IOException{
        int count = PDDocument.load(new File(filename)).getNumberOfPages();
    
        return count;
    } 
    
    public static void generateHTMLFromPDF(String filename) throws IOException {
        PDDocument pdf = PDDocument.load(new File(filename));
       
        try (Writer output = new PrintWriter(HTML_FILENAME, "utf-8")) {
            try {
                new PDFDomTree().writeText(pdf, output);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(PDFParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ioEx){
            System.err.println(ioEx);
        }
    }

    public static Double getFontSize(String style){
        String[] tmps = style.split(";");
        String[] tmp2;
        int i;
        for(String tmp : tmps){
            tmp2 = tmp.split(":");
             if(tmp2[0].compareTo("font-size") == 0){
                 
                tmp2[1] = tmp2[1].replace("pt", "");
                return Double.valueOf(tmp2[1]);
            }
        }
        
        return 0.0;        
    }

    public static Double getHeight(String style){
        String[] tmps = style.split(";");
        String[] tmp2;
        int i;
        for(String tmp : tmps){
            tmp2 = tmp.split(":");
             if(tmp2[0].compareTo("line-height") == 0){
                 
                tmp2[1] = tmp2[1].replace("pt", "");
                return Double.valueOf(tmp2[1]);
            }
        }
        
        return 0.0;        
    }

    public static Double getTopCoord(String style){
        String[] tmps = style.split(";");
        String[] tmp2;
        int i;
        for(String tmp : tmps){
            tmp2 = tmp.split(":");
             if(tmp2[0].compareTo("top") == 0){
                 
                tmp2[1] = tmp2[1].replace("pt", "");
                return Double.valueOf(tmp2[1]);
            }
        }
        
        return 0.0;        
    }
    
    public static Double getLeftCoord(String style){
        String[] tmps = style.split(";");
        String[] tmp2;
        int i;
        for(String tmp : tmps){
            tmp2 = tmp.split(":");
             if(tmp2[0].compareTo("left") == 0){
                 
                tmp2[1] = tmp2[1].replace("pt", "");
                return Double.valueOf(tmp2[1]);
            }
        }
        
        return 0.0;        
    }
    
    public static Double getWidth(String style){
        String[] tmps = style.split(";");
        String[] tmp2;
        int i;
        for(String tmp : tmps){
            tmp2 = tmp.split(":");
             if(tmp2[0].compareTo("width") == 0){
                 
                tmp2[1] = tmp2[1].replace("pt", "");
                return Double.valueOf(tmp2[1]);
            }
        }
        
        return 0.0;        
    }
    
    public static void addTextPutMap(Double fontSize, String text){
        String tmp;
        if((tmp = map.get(fontSize)) != null){
            if(text.compareTo("`") != 0){
                tmp = tmp.concat(" ");
            }
            tmp = tmp.concat(text);
            map.put(fontSize, tmp);
        }else{
            map.put(fontSize, text);
        }
    }
    
    public static boolean isInNeighbourhood(Double areaHeight, Double lineTopCoord, 
                    Double newTopCoord, Double newLineHeight){
        
        Double diff = newTopCoord - lineTopCoord - areaHeight;
        return lineTopCoord.equals(newTopCoord) || diff.equals(newLineHeight) 
                                    || diff.compareTo(5.0) < 0;
    } 
    
    public static void addLine(int pageNumber, Double fontSize, Double lineHeight, 
                    Double topCoord, Double leftCoord, String text){
        
        Page page = pages[pageNumber];
        String tmp;
        Double diff;
        
        for(Line l : page.getLines()){
            if((l.fontSize.equals(fontSize) && l.lineHeight.equals(lineHeight)
                    && isInNeighbourhood(l.areaHeight, l.topCoord, topCoord, lineHeight))){
                
                tmp = l.getText();
                tmp = tmp.concat(" " + text);
                l.setText(tmp);
                
                diff = topCoord - l.topCoord - l.areaHeight;
                //System.out.println("areaH = " +  l.areaHeight + " lineTopC = " + l.topCoord + " newTopC = " + topCoord + " lineH = " + lineHeight + " diff = " + diff + "   " + l.text);
                if(l.topCoord.equals(topCoord) == false ||  diff.equals(lineHeight) == false ){
                    l.areaHeight = topCoord - l.topCoord + lineHeight;
                }
                
                return ;
            }
        }
        
        page.getLines().add(new Line(fontSize, lineHeight, topCoord, leftCoord, text));
    }
    
    public static void extractCoordsFromHtml() throws IOException{
        Document doc = Jsoup.parse(new File(HTML_FILENAME), "UTF-8", "");
        String style;
        int pageNumber;
        
        for(Element link : doc.getElementsByClass("page")){
            pageNumber = Integer.valueOf(link.attr("id").replace("page_", ""));
            
            for(Element link_p : link.getElementsByClass("p")){
                
                style = link_p.attr("style");
                addLine(pageNumber, getFontSize(style), getHeight(style), 
                        getTopCoord(style), getLeftCoord(style), link_p.text());
            }
        }
    }
    
    public static int removeHeaderFooter(Line line, Double x, Double y, Double height){
        if(line.leftCoord >= y && line.topCoord >= x && line.topCoord < height){
            return 0;
        }
            
        return 1;
    }

    public static com.mycompany.template.Template detectTemplate(){
        List <Line> firstPageLines = pages[0].getLines();
        List <Line> sndPageLines = pages[1].getLines();
        
        Line fstLinePage0 = firstPageLines.get(0);
        Line sndLinePage0 = firstPageLines.get(1);
        Line lastLinePage0 = firstPageLines.get(firstPageLines.size() - 1);
        Line fstLinePage1 = sndPageLines.get(0);
        
        return templatesMngmt.getTemplate(fstLinePage0.getTopCoord(), fstLinePage0.getLeftCoord(),
                sndLinePage0.getTopCoord(), sndLinePage0.getLeftCoord(), lastLinePage0.getTopCoord(),
                lastLinePage0.getLeftCoord(), fstLinePage1.getTopCoord(), fstLinePage1.getLeftCoord());
    }
    
    public static void extractInformation(){
        com.mycompany.template.Template t;
        if((t = detectTemplate()) != null){
            System.out.println(t.getClass());
        } else {
            for(int i = 0; i < pages.length; i++){
                System.out.println("\n      PAGE = " + i);
                for(Line l : pages[i].getLines()){
                    System.out.println("-- TC = " + l.getTopCoord() + "    LC = " + l.getLeftCoord() + "   " + l.getFontSize() + "  " + l.getLineHeight() + "  " + l.getText());
                }
            }
            System.out.println("Nu s-a identificat niciun template");
            return ;
        }    
        
        for(Page p : pages){
            
            for(int i = 0; i < p.getLines().size(); i++){
                if(p.number == 0){
                    if(removeHeaderFooter(p.getLines().get(i), t.xPage1, t.yPage1, t.heightPage1) == 1){
                         p.getLines().remove(i--);
                    }
                }else{
                    if(removeHeaderFooter(p.getLines().get(i), t.xPage2, t.yPage2, t.heightPage2) == 1){
                         p.getLines().remove(i--);
                    }
                }
            }
        }
    }
    
    public static void sortLinesAfterTopCoord (int pageNumber){

        for(int i = 0; i < pageNumber; i++){

            Collections.sort(pages[i].getLines(), new Comparator<Line>() {
                @Override
                public int compare(Line o1, Line o2) {
                    return o1.getTopCoord().compareTo(o2.getTopCoord());
                }
            });
        }
    }
    
    public static void initPages(int pagesNumber){
        pages = new Page[pagesNumber];
        for(int i = 0; i < pagesNumber; i++){
            pages[i] = new Page(i);
        }
    }
    
    public static String getCleanedContent(String text){
        String charsSequence = "1234567890.'*()-/`’";
        
        for(int i = 0; i < charsSequence.length(); i++){
            text = text.replace(String.valueOf(charsSequence.charAt(i)), "");
        }

        /* For special characters present when referring names */
        text = text.replace("a ", "");
        text = text.replace("b ", "");
        
        return text.contains("  ") || text.length() == 1 ? null : text;
    }

    public static boolean abstractSubtitleFound(String text){
        List<String> abstractSubtile = new ArrayList(Arrays.asList("Abstract", "ABSTRACT", "abstract", "Summary"));
        
        for(String abstr : abstractSubtile){
            if(text.contains(abstr)){
                return true;
            }
        }
        
        return false;
    }

    
    public static List<PDFFile> getParsedFiles() throws IOException{
        List<PDFFile> pdfFiles = new ArrayList<>();
        
        int pagesNumber;
        int i, j;
        int abstractFound;
        String title, content, authorsName;
        List <String> authors = new ArrayList<>();
        File directory = new File(pdfsDirName);
        
        for(File file : directory.listFiles()){
            abstractFound = 0;
            content = ""; title = ""; authorsName = "";
            pagesNumber = getPagesNumber(file.getAbsolutePath());
            
            initPages(pagesNumber);

            generateHTMLFromPDF(file.getAbsolutePath());
            extractCoordsFromHtml();
            sortLinesAfterTopCoord(pagesNumber);

            extractInformation();

            for(i = 0; i < pages.length; i++){

                for(j = 0; j < pages[i].getLines().size(); j++){

                    if(abstractSubtitleFound(pages[i].getLines().get(j).getText()) ){
                            abstractFound = 1;
                    }

                    if(i == 0 && j == 0){
                        title = pages[i].getLines().get(j).getText();
                    }else if ((i == 0 && j == 1) || (i == 0 && abstractFound == 0)){
                        if((authorsName = getCleanedContent(pages[i].getLines().get(j).getText())) != null){
                            authors.addAll(Arrays.asList(authorsName.split(",")));
                        }
                    }else{
                        content = content.concat(pages[i].getLines().get(j).getText());
                    }
                }
            }
            pdfFiles.add(new PDFFile(title, authors, content));
        }
        
        return pdfFiles;
    }
    
    public static void main(String args[]) throws IOException{
     
        for(PDFFile pf : getParsedFiles()){
            

            System.out.println("title = " + pf.getTitle());
            System.out.print("authors = ");
            for(String a : pf.getAuthors()){
                System.out.print(a + "  ");
            }
            System.out.println("\ncontent = " + pf.getContent());
        
        }
    }
}

