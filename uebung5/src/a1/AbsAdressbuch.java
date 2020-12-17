package a1;

import java.io.*;
import java.util.*;
import org.jdom2.*;

public abstract class AbsAdressbuch {
	protected AdrGUI gui;  // Viewer, an den ggf. neue Adressdaten geschickt werden
	protected Document doc;  // eingeladenes XML-Dokument
	protected List<Element> adressen = null; // Liste der Adresselemente
	protected int current = 0; // Index der aktuell angezeigten Adresse

	public AbsAdressbuch(AdrGUI parent){
		gui = parent;
		// leeres Dokument anlegen, falls neue Adressen erzeugt werden
		Element r = new Element("adressbuch");
		// Bezeichnungselement einfuegen
		Element bez = new Element("bezeichnung");
		bez.addContent("Neues Adressbuch");
		r.addContent(bez);
		// doc type einfuegen
		DocType doctype = new DocType("adressbuch");
		this.doc = new Document(r,doctype);
		// adressen mit leeree Adressliste initialisieren
		this.adressen = this.doc.getRootElement().getChildren("adresse"); 
		current = -1; // aktuelles Adresselement auf -1, da Liste leer
	}

	// laedt neues XML-Dokument und speichert es ueber doc-Attribut
	// liefert true zurueck, falls erfolgreich, false sonst
	public abstract boolean loadDocument(File file);

	// schreibt aktuelles  XML-Dokument doc in Datei raus
	public abstract void saveDocument(File file);

	// liefert erste Adresse aus geladenem Adressbuch, falls dieses
	// nicht leer ist, sonst null
	// Adress-Element wird dabei in Instanz der Klasse Adresse gewandelt
	// current  wird ggf. aktualisiert
	public abstract Adresse getFirstEntry();

	// liefert naechste Adresse aus dem Dokument oder nullrrent aktualisiert
	// Adress-Element wird dabei in Instanz der Klasse Adresse gewandelt
	// current wird ggf. aktualisiert
	public abstract Adresse getPrev();

	// liefert vorherige Adresse oder null
	// Adress-Element wird dabei in Instanz der Klasse Adresse gewandelt
	// current wird ggf. aktualisiert
	public abstract Adresse getNext();

	// liefert aktuelle Adresse (ueber current Zeiger) oder null,
	// falls Adressbuch leer
	// Adress-Element wird dabei in Instanz der Klasse Adresse gewandelt
	// wenn Adressbuch leer
	public abstract Adresse getCurrent();

	// speichern der Daten aus der Instanz der Klasse Adresse  
	// in den aktuellen Adress-Datensatz (an Position current)
	// bzw. in eine leere Liste neu einfuegen
	public abstract void save(Adresse a);

	// aus einer Instanz der Klasse Adresse ein neues Adress-Element
	// erzeugen und in das Dokument an der Position current+1 
	// einfuegen
	public abstract Adresse addNeueAdresse(Adresse a);

	// Hilfsmethode
	// holt aus Adresselement die Daten und erzeugt eine Instanz der 
	// Klasse Adresse
	protected abstract Adresse getAdresse(Element e);

}
