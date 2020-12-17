package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.io.*;

public class AdrGUI extends JFrame{
	private static final long serialVersionUID = 1L;  
	//private static final Color panelColor = new Color(212,212,255);
	Font textFieldFont  = new Font("Times new roman",Font.PLAIN,20);
	Font labelFont  = new Font("Times new roman",Font.BOLD,16);
	private JTextField vorname,nachname,land,vorwahl,telefon,email;
	private JTextArea notiz;
	private JButton vor, zurueck, speichern,neueAdresse;
	private JMenuItem load, save, byNachname, byVorname;
	private Adressbuch adressen=null;
	private File currentDir = new File("./");  // aktuelles Verzeichnis fuer JFileChooser merken

	private boolean neueAdresseEingabe = false;

	public AdrGUI(){
		super("Adressbuch");
		this.adressen = new Adressbuch(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setBounds(200,200,500,400); // Groesse und Position von GUI setzen
		this.setResizable(false); // Groesse des Fensters unveraenderbar machen
		this.createContent();
		this.pack();
	}


	private void createContent(){
		JPanel p1 = createAdressPanel();
		JPanel p2 = createButtonPanel();
		createMenu();
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.X_AXIS));
		this.getContentPane().add(p1);this.getContentPane().add(p2);	

	}


	private JPanel createAdressPanel(){
		vorname = new JTextField(20);
		vorname.setFont(this.textFieldFont);
		nachname = new JTextField(20);
		nachname.setFont(this.textFieldFont);
		land = new JTextField(4);
		land.setFont(this.textFieldFont);
		vorwahl = new JTextField(7);
		vorwahl.setFont(this.textFieldFont);
		telefon = new JTextField(11);
		telefon.setFont(this.textFieldFont);
		email = new JTextField(25);
		email.setFont(this.textFieldFont);
		notiz = new JTextArea(2,60);
		notiz.setBorder(BorderFactory.createEtchedBorder());
		useTextFields(false); // disable textfields


		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel,BoxLayout.X_AXIS));
		JLabel n = new JLabel("Name: ");
		n.setFont(this.labelFont);
		namePanel.add(n);
		namePanel.add(vorname);namePanel.add(nachname);

		JPanel telPanel = new JPanel();
		telPanel.setLayout(new BoxLayout(telPanel,BoxLayout.X_AXIS));
		JLabel t = new JLabel("Tel.:    ");
		t.setFont(this.labelFont);
		telPanel.add(t);
		telPanel.add(land); telPanel.add(vorwahl);telPanel.add(telefon);

		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(new BoxLayout(emailPanel,BoxLayout.X_AXIS));
		JLabel e = new JLabel("Email:  ");
		e.setFont(this.labelFont);
		emailPanel.add(e);
		emailPanel.add(email);

		JPanel notizPanel = new JPanel();
		notizPanel.setLayout(new BoxLayout(notizPanel,BoxLayout.Y_AXIS));
		JLabel z = new JLabel("Notiz:");
		z.setFont(this.labelFont);
		z.setAlignmentX(0.0f);
		notiz.setAlignmentX(0.0f);
		notizPanel.add(z);
		notizPanel.add(notiz);
		notizPanel.setPreferredSize(new Dimension(500,100));
		notizPanel.setAlignmentX(0.0f);

		JPanel adrPanel = new JPanel();
		adrPanel.setLayout(new BoxLayout(adrPanel, BoxLayout.Y_AXIS));
		adrPanel.setAlignmentX(0.0f);
		adrPanel.add(namePanel);
		adrPanel.add(telPanel);
		adrPanel.add(emailPanel);

		JPanel labelAdrPanel = new JPanel();
		labelAdrPanel.setLayout(new BoxLayout(labelAdrPanel, BoxLayout.Y_AXIS));
		labelAdrPanel.add(adrPanel);
		notizPanel.add(Box.createRigidArea(new Dimension(0,20)));

		labelAdrPanel.add(notizPanel);


		labelAdrPanel.setPreferredSize(new Dimension(300,170));
		return labelAdrPanel;
	}

	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();

		vor = new JButton(">");
		zurueck = new JButton("<");
		vor.setEnabled(false);zurueck.setEnabled(false);
		JPanel mPanel = new JPanel();
		mPanel.setLayout(new BoxLayout(mPanel,BoxLayout.X_AXIS));
		mPanel.add(zurueck);mPanel.add(vor);
		mPanel.setAlignmentX(0.5f);

		neueAdresse = new JButton("NEU");
		neueAdresse.setAlignmentX(0.5f);

		speichern = new JButton("SPEICHERN");
		speichern.setAlignmentX(0.5f);

		ButtonListener lis = new ButtonListener();
		vor.addActionListener(lis);
		zurueck.addActionListener(lis);
		speichern.addActionListener(lis);
		neueAdresse.addActionListener(lis);

		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		panel.add(mPanel);
		panel.add(Box.createRigidArea(new Dimension(0,20)));
		panel.add(neueAdresse);
		panel.add(Box.createRigidArea(new Dimension(0,20)));
		panel.add(speichern);

		return panel;
	}

	private void createMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenu menu2 = new JMenu("Data");
		menuBar.add(menu);
		menuBar.add(menu2);

		load = new JMenuItem("XML Datei laden");
		save = new JMenuItem("XML Datei speichern");
		menu.add(load);
		menu.add(save);

		JMenu sortMenu = new JMenu("sortiere");
		byNachname = new JMenuItem("nach Nachnamen");
		byVorname = new JMenuItem("nach Vornamen");
		byNachname.setEnabled(false);
		byVorname.setEnabled(false);

		DataListener dataListener = new DataListener();
		byNachname.addActionListener(dataListener);
		byVorname.addActionListener(dataListener);
		sortMenu.add(byNachname);
		sortMenu.add(byVorname);
		menu2.add(sortMenu);

		this.setJMenuBar(menuBar);
		XMLFileListener lis = new XMLFileListener();
		load.addActionListener(lis);
		save.addActionListener(lis);
	}

	private class XMLFileListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JMenuItem mi = (JMenuItem) e.getSource();
			String command = mi.getText();
			File file;

			if (command.equals("XML Datei laden")){
				file = chooseFile("load");
				if (file != null){
					boolean success = adressen.loadDocument(file);
					if (success){
						Adresse a = adressen.getFirstEntry();
						if (a != null){
							AdrGUI.this.setAdresse(a);
						}
						vor.setEnabled(true);zurueck.setEnabled(true);
						byNachname.setEnabled(true);
						byVorname.setEnabled(true);
						useTextFields(true); // enable textfields

					}
				}
			}
			else if (command.equals("XML Datei speichern")){
				file = chooseFile("close");
				if (file != null){
					adressen.saveDocument(file);
				}
			}
		}

		private File chooseFile(String action){
			File file = null;

			JFileChooser chooser = new JFileChooser(currentDir);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
			chooser.setFileFilter(filter);
			int returnVal = 0;
			switch(action){
			case "load":
				returnVal = chooser.showOpenDialog(AdrGUI.this);
				break;
			case "save":
				returnVal = chooser.showSaveDialog(AdrGUI.this);
				break;
			default:	
				break;
			}
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				AdrGUI.this.currentDir = chooser.getCurrentDirectory();
			}
			return file;
		}
	}

	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JButton b = (JButton) e.getSource();
			String command = b.getText();

			if (command.equals("<")){
				Adresse a = AdrGUI.this.adressen.getPrev();
				if (a != null) {
					AdrGUI.this.setAdresse(a);
				}
			}
			else if (command.equals(">")){
				Adresse a = AdrGUI.this.adressen.getNext();
				if (a != null) {
					AdrGUI.this.setAdresse(a);
				}
			}
			else if (command.equals("SPEICHERN")){
				if (!AdrGUI.this.neueAdresseEingabe){
					AdrGUI.this.adressen.save(AdrGUI.this.getAdresse());
				}
				else { // bestehende Adresse uebeschreiben
					Adresse a = AdrGUI.this.adressen.addNeueAdresse(AdrGUI.this.getAdresse());
					// weiter im Anzeigemodus
					neueAdresse.setText("NEU");
					vor.setEnabled(true);
					zurueck.setEnabled(true);
					AdrGUI.this.neueAdresseEingabe = false;
					AdrGUI.this.setAdresse(a);	    
				}
			}
			else if (command.equals("NEU")){
				// Textfelder leeren
				// JButton Text umaendern
				b.setText("VERWERFEN");
				// Adress-Instanz anlegen
				AdrGUI.this.neueAdresseEingabe = true;
				vor.setEnabled(false);
				zurueck.setEnabled(false);
				useTextFields(true); // enable textfields
				AdrGUI.this.clearAll();
			}
			else if (command.equals("VERWERFEN")){
				// JButton Text umaendern
				b.setText("NEU");
				vor.setEnabled(true);
				zurueck.setEnabled(true);
				AdrGUI.this.neueAdresseEingabe = false;
				Adresse a;
				if ((a = AdrGUI.this.adressen.getCurrent()) != null){
					AdrGUI.this.setAdresse(a);	
				}
				else {
					useTextFields(false); // enable textfields
					clearAll();
				}
			}
		}
	}

	private class DataListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			String test = ((JMenuItem)e.getSource()).getText();
			if (test.equals("nach Nachnamen")){
				Adresse a = AdrGUI.this.adressen.sort(Adresse.COMP_BY_NACHNAME);
				AdrGUI.this.setAdresse(a);
			}
			else if (test.equals("nach Vornamen")){
				Adresse a = AdrGUI.this.adressen.sort(Adresse.COMP_BY_VORNAME);
				AdrGUI.this.setAdresse(a);
			}
		}
	}

	// Adress Eintraege in Feldern loeschen
	private void clearAll(){
		vorname.setText("");
		nachname.setText("");
		land.setText("");
		vorwahl.setText("");
		telefon.setText("");
		email.setText("");
		notiz.setText("");
		repaint();
	}

	public void setAdresse(Adresse a){
		vorname.setText(a.getVorname());
		nachname.setText(a.getNachname());
		land.setText(a.getLand());
		vorwahl.setText(a.getVorwahl());
		telefon.setText(a.getNummer());
		email.setText(a.getEmail());
		notiz.setText(a.getNotiz());
		repaint();
	}


	private Adresse getAdresse(){
		return new Adresse(vorname.getText().trim(),
				nachname.getText().trim(),
				email.getText().trim(),
				land.getText().trim(),
				vorwahl.getText().trim(),
				telefon.getText().trim(),
				notiz.getText().trim());
	}

	public static void main(String[] args){
		AdrGUI gui = new AdrGUI();
		gui.setVisible(true);
	}

	// enable or disable editing of all textfields
	private void useTextFields(boolean enableP){
		vorname.setEditable(enableP);
		nachname.setEditable(enableP);
		email.setEditable(enableP);
		land.setEditable(enableP);
		vorwahl.setEditable(enableP);
		telefon.setEditable(enableP);
		notiz.setEditable(enableP);
	}

}
