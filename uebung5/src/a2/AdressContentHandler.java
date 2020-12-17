package a2;


import org.xml.sax.*;

public class AdressContentHandler implements ContentHandler {

	private String currentValue;
	private String nachname, vorname;
	private static int counter = 0;

	// Aktuelle Zeichen die gelesen werden, werden in eine 
	// Zwischenvariable gespeichert (Aufr. der Meth. durch Parser)
	public void characters(char[] ch, int start, int length) throws SAXException {
		currentValue = new String(ch,start,length);
	}
	// Methode wird aufgerufen wenn der Parser zu einem Start Tag 
	// kommt
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
	}


	// Methode wird aufgerufen wenn der Parser zu einem End-Tag 
	// kommt
	public void endElement(String uri, String localName,String qName) throws SAXException {

		// Name setzen
		if (localName.equals("vorname")) {
			vorname = currentValue;
		}
		// Kategorie setzen
		if (localName.equals("nachname")) {
			nachname = currentValue;
		}
		if (localName.equals("adresse")) {
			System.out.println(++counter + ". " + vorname + " " +  nachname);
		}
	}

	// leere Implementierungen für alle weiteren Methoden
	// des Interfaces
	public void endDocument() throws SAXException {}
	public void endPrefixMapping(String prefix) throws SAXException {}
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
	public void processingInstruction(String target,String data) throws SAXException {}
	public void setDocumentLocator(Locator locator){}
	public void skippedEntity(String name) throws SAXException {}
	public void startDocument() throws SAXException {}
	public void startPrefixMapping(String prefix,String uri)throws SAXException{}
}
