package main.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DateType;


public class Req3 {
	
	public static String array2HTML(Object[][] array){
		StringBuilder html = new StringBuilder("<table>");
		
		for(Object elem:array[0]){
			html.append("<th>" + elem.toString() + "</th>");
		}
		for(int i = 1; i < array.length; i++){
			Object[] row = array[i];
			html.append("<tr>");
			for(Object elem:row){
				html.append("<td>" + elem.toString() + "</td>");
			}
			html.append("</tr>");
		}
		html.append("</table>");
		return html.toString();
	}
	
	public static String getTitle(File f) throws AnalysisException, IOException {
		ContentExtractor extractor = new ContentExtractor();
    	InputStream inputStream = new FileInputStream(f);
    	extractor.setPDF(inputStream);
    	
    	String path = f.getAbsolutePath();
    	String title = extractor.getMetadata().getTitle();
    	
    	return "<a href=\""+ path + "\">" + title + "</a>";
	}
	
	public static String getJournal(File f) throws AnalysisException, IOException {
		ContentExtractor extractor = new ContentExtractor();
    	InputStream inputStream = new FileInputStream(f);
    	extractor.setPDF(inputStream);
    	
    	return extractor.getMetadata().getJournal();
	}
	
	public static String getYear(File f) throws AnalysisException, IOException {
		ContentExtractor extractor = new ContentExtractor();
    	InputStream inputStream = new FileInputStream(f);
    	extractor.setPDF(inputStream);
    	
    	return extractor.getMetadata().getDate(DateType.PUBLISHED).getYear();
	}
	
	public static String getAuthors(File f) throws AnalysisException, IOException {
		ContentExtractor extractor = new ContentExtractor();
    	InputStream inputStream = new FileInputStream(f);
    	extractor.setPDF(inputStream);
    	
    	String result = "";
    	
    	int length = extractor.getMetadata().getAuthors().size();
    	
    	for(int i = 0; i<length; i++) {
    		if(i==(length-1))
    			result = result + extractor.getMetadata().getAuthors().get(i).getName();
    		else
    			result = result + extractor.getMetadata().getAuthors().get(i).getName() + ", ";
    	}
    	return result;
	}
	
    public static void main( String[] args ) throws IOException, AnalysisException {
    	File f1 = new File("documentos\\1.pdf");
		File f2 = new File("documentos\\2.pdf");
		File f3 = new File("documentos\\3.pdf");
		File f4 = new File("documentos\\4.pdf");
		
		Object[][] ints = {{"Article Title","Journal Name","Publication Year","Authors"},{getTitle(f1),getJournal(f1),getYear(f1),getAuthors(f1)},{getTitle(f2),getJournal(f2),getYear(f2),getAuthors(f1)},{getTitle(f3),getJournal(f3),getYear(f3),getAuthors(f3)},{getTitle(f4),getJournal(f4),getYear(f4),getAuthors(f4)}};
		System.out.println(array2HTML(ints));
    }
}