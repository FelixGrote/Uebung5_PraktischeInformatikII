package a1;

import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.*;
import java.util.*;

public class Adressbuch extends AbsAdressbuch {

	public Adressbuch(AdrGUI parent) {
		super(parent);
		// leeres Dokument anlegen, falls neue Adressen erzeugt werden
		Element r = new Element("adressbuch");
		Element bez = new Element("bezeichnung");
		bez.addContent("Neues Adressbuch");
		r.addContent(bez);
		DocType doctype = new DocType("adressbuch");
		this.doc = new Document(r, doctype);
		this.adressen = this.doc.getRootElement().getChildren("adresse"); // leere Liste
		current = -1; // aktuelles Adresselement auf -1, da Liste leer
	}

	/*
	 * laedt neues XML-Dokument und speichert eine Referenz dazu in doc Attribut
	 * Dazu muss die Liste adressen mit allen Adresselementen initialisiert werden
	 */
	public boolean loadDocument(File file) {
		try {
			this.doc = new SAXBuilder().build(file.getPath());
			if (this.doc != null) {
				adressen = this.doc.getRootElement().getChildren("adresse");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		} catch (JDOMException jde) {
			jde.printStackTrace();
			return false;
		}
		return true;
	}

	/* schreibt XML-Dokument in eine Datei file */
	public void saveDocument(File file) {
		if ((doc != null) && (file.getPath() != null)) {
			try {
				XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
				FileWriter fw = new FileWriter(file);
				out.output(doc, fw);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/* liefert erste Adresse aus geladen em Adressbuch, sonst null */
	public Adresse getFirstEntry() {
		Adresse a = null;
		if ((this.adressen != null) && (this.adressen.size() > 0)) {
			current = 0;
			Element e = adressen.get(current); // erstes Element
			a = getAdresse(e);
		}
		return a;
	}

	/* liefert aktuelle Adresse ueber current Index (Attribut in der Oberklasse) */
	public Adresse getCurrent() {
		Adresse a = null;
		if ((adressen != null) && (adressen.size() > 0)) {
			Element e = adressen.get(current);
			a = getAdresse(e);
		}
		return a;
	}

	/*
	 * liefert nächste Adresse bzw. die aktuelle Adresse, wenn es kein
	 * nachfolgendes Element gibt bzw. null, wenn die Liste leer ist
	 */
	public Adresse getNext() {
		Adresse a = null;
		if (adressen != null) {
			if (current < (adressen.size() - 1)) {
				Element e = adressen.get(++current);
				a = getAdresse(e);
			} else if (adressen.size() > 0) {
				Element e = adressen.get(current);
				a = getAdresse(e);
			}
		}
		return a;
	}

	/*
	 * liefert vorhergehende Adresse oder die aktuelle Adresse, wenn es kein
	 * vorhergehendes Element gibt bzw. null, wenn die Liste leer ist
	 */
	public Adresse getPrev() {
		Adresse a = null;
		if (adressen != null) {
			if (current > 0) {
				Element e = adressen.get(--current);
				a = getAdresse(e);
			} else if (adressen.size() > 0) {
				Element e = adressen.get(current);
				a = getAdresse(e);
			}
		}
		return a;
	}

	/*
	 * speichert die Daten aus der Instanz der Klasse Adresse in dem aktuellen
	 * Adress-Datensatz (Index current)
	 */
	public void save(Adresse a) {
		if (adressen != null) {
			if (current >= 0) {
				Element e = adressen.get(current);
				Element c;
				if (e != null) {
					String vorname = a.getVorname();
					String nachname = a.getNachname();
					String email = a.getEmail();
					String land = a.getLand();
					String vorwahl = a.getVorwahl();
					String telefon = a.getNummer();
					String notiz = a.getNotiz();

					c = e.getChild("vorname");
					if (c != null) {
						c.setText(vorname);
					}
					c = e.getChild("nachname");
					if (c != null) {
						c.setText(nachname);
					}
					c = e.getChild("email");
					if (c != null) {
						c.setText(email);
					}
					c = e.getChild("telefon");
					if (c != null) {
						c.setText(telefon);
						c.setAttribute("land", land);
						c.setAttribute("vorwahl", vorwahl);
					}
					c = e.getChild("notiz");
					if (c != null) {
						c.setText(notiz);
					}
				}
			} else { // erstes Element in Liste (in neues Dokument) einfügen
				addNeueAdresse(a);
			}
		}
	}

	/*
	 * holt aus Adresselement die Daten und erzeugt eine Instanz der Klasse Adresse
	 */
	protected Adresse getAdresse(Element e) {
		Adresse a = null;
		Element c;
		Attribute att;
		String vorname = "", nachname = "", email = "", land = "", vorwahl = "", telefon = "", notiz = "";
		if ((e != null) && (e.getName().equals("adresse"))) {
			c = e.getChild("vorname");
			if (c != null) {
				vorname = c.getText();
			}

			c = e.getChild("nachname");
			if (c != null) {
				nachname = c.getText();
			}

			c = e.getChild("email");
			if (c != null) {
				email = c.getText();
			}

			c = e.getChild("telefon");
			if (c != null) {
				telefon = c.getText();
				att = c.getAttribute("land");
				if (att != null) {
					land = att.getValue();
				}
				att = c.getAttribute("vorwahl");
				if (att != null) {
					vorwahl = att.getValue();
				}
			}

			c = e.getChild("notiz");
			if (c != null) {
				notiz = c.getText();
			} else {
				Element n = new Element("notiz");
				n.setText(notiz);
				e.addContent(n);
			}
			a = new Adresse(vorname, nachname, email, land, vorwahl, telefon, notiz);

		}
		return a;
	}

	/*
	 * aus einer Instanz der Klasse Adresse ein neues Adress-Element erzeugen und in
	 * das Dokument an der nächsten Position (++current) einfuegen
	 */
	public Adresse addNeueAdresse(Adresse a) {
		// neues Element Adresse mit Inhalt erzeugen
		Element vn = new Element("vorname");
		vn.addContent(a.getVorname());
		Element nn = new Element("nachname");
		nn.addContent(a.getNachname());
		Element email = new Element("email");
		email.addContent(a.getEmail());
		Element telefon = new Element("telefon");
		telefon.addContent(a.getNummer());
		telefon.setAttribute("land", a.getLand());
		telefon.setAttribute("vorwahl", a.getVorwahl());
		Element notiz = new Element("notiz");
		notiz.addContent(a.getNotiz());

		Element e = new Element("adresse");
		e.addContent(vn);
		e.addContent(nn);
		e.addContent(email);
		e.addContent(telefon);
		e.addContent(notiz);

		return addAdressElementAt(++current, e);
	}

	private Adresse addAdressElementAt(int index, Element ele) {
		Element r = doc.getRootElement();
		r.addContent(index, ele); // neues Element einfügen
		return this.getAdresse(adressen.get(index));
	}

	public Adresse sort(int compBy) {
		if (adressen.size() > 1) {
			// Adressliste kopieren (Elemente ohne Parent)
			List<Element> cpAdressen = new ArrayList<Element>();
			for (Element e : adressen)
				cpAdressen.add(e.clone());

			Adresse.COMP_BY = compBy;
			final Element e = adressen.get(current);
			Comparator<Element> comparator = new Comparator<Element>() {
				public int compare(Element e1, Element e2) {
					return Adressbuch.this.getAdresse(e1).compareTo(Adressbuch.this.getAdresse(e2));
				}

				public boolean equals(Object o2) {
					return Adressbuch.this.getAdresse(e).equals(Adressbuch.this.getAdresse((Element) o2));
				}
			};
			Collections.sort(cpAdressen, comparator);
			// Content (Adress-Elemente) ersetzen
			// Content besteht aus Bezeichner und Adressen
			Element r = doc.getRootElement();
			r.removeChildren("adresse"); // alte Adressen entfernen
			r.addContent(cpAdressen); // sortierte Liste einfügen
			adressen = r.getChildren("adresse");
		}
		return getFirstEntry();
	}

}
