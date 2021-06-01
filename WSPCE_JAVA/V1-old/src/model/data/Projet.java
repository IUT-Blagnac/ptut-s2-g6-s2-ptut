package model.data;

import java.sql.Date;

public class Projet {
	
	public static final int ID_PROJET_INEXISTANT = -1000;

	public static final int EST_ACTIF = 1;
	public static final int EST_INACTIF = 0;

	
	private int id;
	private String nom;
	private String description;
	private Date dateDebut;
	private Date dateFinEstimee;
	private Date dateFinReel;
	private int estActif ;
	private int idCli;
	
	public Projet() {
		this(Projet.ID_PROJET_INEXISTANT, null, null, null, null, null, -1, Client.ID_CLIENT_INEXISTANT);
	}

	public Projet(int pId, String pNom, String pDescr, Date pDateDebut, Date pDateFinEstimee, Date pDateFinReel, int pEstActif, int pIdCli) {
		id = pId;
		nom = pNom;
		description = pDescr;
		dateDebut = pDateDebut;
		dateFinEstimee = pDateFinEstimee;
		dateFinReel =pDateFinReel;
		estActif = pEstActif;
		idCli = pIdCli;
	}
	
	public Projet (Projet p) {
		id = p.id;
		nom = p.nom;
		description = p.description;
		dateDebut = p.dateDebut;
		dateFinEstimee = p.dateFinEstimee;
		dateFinReel =p.dateFinReel;
		estActif = p.estActif;
		idCli = p.idCli;
	}
	
	public void recopieProjet(Projet p){
		id = p.id;
		nom = p.nom;
		description = p.description;
		dateDebut = p.dateDebut;
		dateFinEstimee = p.dateFinEstimee;
		dateFinReel =p.dateFinReel;
		estActif = p.estActif;
		idCli = p.idCli;
    }
	
	public int getId() {
		return id;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Date getDateDebut() {
		return dateDebut;
	}
	
	public Date getDateFinEstimee() {
		return dateFinEstimee;
	}
	
	public Date getDateFinReel() {
		return dateFinReel;
	}
	
	public int getEstActif() {
		return estActif;
	}
	
	public int getIdCli() {
		return idCli;
	}
	
	@Override
    public String toString() {
		String libelleActif = (estActif == 1 ? "Actif" : "Inactif") ;
		return "[id="+id+"] "+nom+" "+dateDebut+" "+libelleActif;
    }
}
