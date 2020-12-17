package a1;


public class Adresse implements Comparable<Adresse>  {
	private String vorname;
	private String nachname;
	private String email;
	private String[] tel;
	private StringBuffer notiz;

	public final static int COMP_BY_NACHNAME = 0;
	public final static int COMP_BY_VORNAME = 1;
	public static int COMP_BY = COMP_BY_NACHNAME; // Vergleichsattribut


	public Adresse(String vorname,String nachname,String email, String land, String vorwahl, String nummer, String notiz) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.tel = new String[] {land,vorwahl,nummer};
		this.notiz = new StringBuffer(notiz);
	}

	public String getVorname(){
		return this.vorname;
	}

	public void setVorname(String vorname){
		this.vorname = vorname;
	}


	public String getNachname(){
		return this.nachname;
	}

	public void setNachname(String nachname){
		this.nachname = nachname;
	}


	public String getEmail(){
		return this.email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getLand(){
		return this.tel[0];
	}

	public void setLand(String land){
		this.tel[0] = land;
	}

	public String getVorwahl(){
		return this.tel[1];
	}

	public void setVorwahl(String vorwahl){
		this.tel[1] = vorwahl;
	}

	public String getNummer(){
		return this.tel[2];
	}

	public void setNummer(String nummer){
		this.tel[2] = nummer;
	}

	public String getNotiz(){
		return this.notiz.toString();
	}

	public void setNotiz(String notiz){
		this.notiz = new StringBuffer(notiz);
	}

	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append(vorname + nachname +  "\n");
		buf.append("Tel.:  " + tel[0] + " " + tel[1] + " " + tel[2] + "\n");
		buf.append("Email: " + email +  "\n");
		buf.append(" -->   " + notiz + "\n");
		return buf.toString();
	}

	public int compareTo(Adresse a2){
		switch(COMP_BY){
		case COMP_BY_NACHNAME: return nachname.compareTo(a2.getNachname());
		case COMP_BY_VORNAME: return vorname.compareTo(a2.getVorname());
		default: return vorname.compareTo(a2.getVorname());
		}
	}

	public boolean equals(Object o){
		switch(COMP_BY){ 
		case COMP_BY_NACHNAME: return nachname.equals(((Adresse)o).getNachname());
		case COMP_BY_VORNAME: return vorname.equals(((Adresse)o).getVorname());
		default: return vorname.equals(((Adresse)o).getVorname());
		}
	}


}
