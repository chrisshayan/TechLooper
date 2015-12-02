package com.techlooper.aspect;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by phuonghqh on 11/24/15.
 */
public class Main2 {

  public static void main(String[] args) throws Exception {

//    ChartUtilities.saveChartAsJPEG(new java.io.File("chart.jpg"), Main.generatePieChart(), 500, 500);

//    WebClient webClient = new WebClient(BrowserVersion.CHROME);
//    JavaScriptEngine engine = new JavaScriptEngine(webClient);
//    engine.setJavaScriptTimeout(15000);
//    webClient.setJavaScriptEngine(engine);
//    HtmlPage page = webClient.getPage("http://localhost:8080/abc.html");

//    HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
////    WebDriver driver = new HtmlUnitDriver();
//    driver.setJavascriptEnabled(true);
////    driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
//    driver.get("http://localhost:8080/abc.html");
//    String address = "http://www.w3.org";
//    BufferedImage buff = Graphics2DRenderer.renderToImageAutoSize(address, 1024);
//    ImageIO.write(buff, "png", new File("screenshot.png"));


//    BufferedImage image = new Robot().createScreenCapture(newRectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//    ImageIO.write(image, "png", new File("/screenshot.png"));

//    WebDriver driver = new FirefoxDriver();
//    driver.get("http://www.google.com/");
//    java.io.File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//// Now you can do whatever you need to do with it, for example copy somewhere
//    FileUtils.copyFile(scrFile, new File("screenshot.png"));

//    WebDriverWait myWaitVar = new WebDriverWait(driver, 30);
//    myWaitVar.
//    myWaitVar.until(ExpectedConditions.visibilityOfElementLocated(
//      By.id("container")));

//    String content=driver.getPageSource();
//    System.out.println(content);
//    driver.close();
//    Object page = driver.get("http://localhost:8080/abc.html");

//    java.io.File destination = new java.io.File("file.txt");
// This line isn't needed, but is really useful
// if you're a beginner and don't know where your file is going to end up.
//    System.out.println(destination.getAbsolutePath());
//    PrintWriter out = new PrintWriter("filename.txt");
//    out.println(content);
//    out.close();
    //

    String content = String.join("\n", Files.readAllLines(Paths.get("/Users/phuonghqh/Documents/working/TechLooper/src/main/webapp/assets/report.html")));

//    String k = "<html><body> This is my Project </body></html>";
    OutputStream file = new FileOutputStream(new java.io.File("Test.pdf"));
    Document document = new Document();
    PdfWriter writer = PdfWriter.getInstance(document, file);
    document.open();
    java.io.InputStream is = new ByteArrayInputStream(content.getBytes());
    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
    document.close();
    file.close();

    //Set PDF target output file
//    String outputFile = "firstdoc.pdf";
//    OutputStream os = new FileOutputStream(outputFile);
//
////Set up flying-saucer IText based renderer
//    ITextRenderer renderer = new ITextRenderer();
//
////Create PDF
//    renderer.setDocumentFromString(content);
//    renderer.layout();
//    renderer.createPDF(os);
//
//    os.close();

//    page.save(new File("def.html"));
//      Assert.assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());
//
//      final String pageAsXml = page.asXml();
//      Assert.assertTrue(pageAsXml.contains("<body class=\"composite\">"));
//
//      final String pageAsText = page.asText();
//      Assert.assertTrue(pageAsText.contains("Support for the HTTP and HTTPS protocols"));
  }
}
