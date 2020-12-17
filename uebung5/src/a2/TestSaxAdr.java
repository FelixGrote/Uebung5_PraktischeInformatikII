package a2;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
public class TestSaxAdr{
	public final static String resourcePath = 
			System.getProperty("user.dir") + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator");

	public static void main(String[] args) {
		try {
			// XMLReader erzeugen
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();

			// Pfad zur XML Datei
			FileReader reader = new FileReader(resourcePath + "adressen.xml");
			InputSource inputSource = new InputSource(reader);

			// DTD kann optional uebergeben werden
			// inputSource.setSystemId(resourcePath + "adressen.dtd");

			// PersonenContentHandler wird uebergeben
			xmlReader.setContentHandler(new AdressContentHandler());
			// Parsen wird gestartet
			xmlReader.parse(inputSource);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (SAXException e){
			e.printStackTrace();
		}
	}
}
