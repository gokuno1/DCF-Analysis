package com.example.Excel.DCFAnalysis.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ReadPdfToExcel {
	
	public void readAndWrite() throws DocumentException, IOException, COSVisitorException
	{
		File input = new File("simplepdf.pdf");
		PDDocument docx = PDDocument.load(input);
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        String text = "This is an example of adding text to a page in the pdf document. we can add as many lines as we want like this using the draw string method of the ContentStream class"; 
        document.add(new Paragraph(text));
        docx.save(new File("Sample.txt"));
        document.close();
        
        
	}
	
	public static void main(String[] args) throws Exception {
		
		new ReadPdfToExcel().readAndWrite();
		
		

		
		 

 }
}