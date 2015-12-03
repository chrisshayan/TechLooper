package com.techlooper.aspect;

import com.itextpdf.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by phuonghqh on 11/24/15.
 */
public class Main2 {

  public static void main(String[] args) throws Exception {

    String content = String.join("\n", Files.readAllLines(Paths.get("./src/main/webapp/assets/report.html")));

    //Set PDF target output file
    String outputFile = "firstdoc.pdf";

    OutputStream os =  new FileOutputStream(outputFile);

    ITextRenderer renderer = new ITextRenderer();
    renderer.getFontResolver().addFont("font/verdana.ttf",  BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    renderer.setDocumentFromString(content);
    renderer.layout();
    renderer.createPDF(os);

    os.close();
  }
}
