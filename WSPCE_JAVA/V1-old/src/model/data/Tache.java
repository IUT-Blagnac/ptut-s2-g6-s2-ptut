package model.data;

import java.sql.Date;

public class Tache
{

    public static final int ID_TACHE_INEXISTANTE = -1000;

    private int id;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private int dureeEstimee;
    private int dureeReelle;
    private int idProjet;
    private int idCompetence;
    private int idNiveau;
    private int idEmploye;

    /**
     * Constructeur par default
     */
    public Tache()
    {
        this(Tache.ID_TACHE_INEXISTANTE, null, null, null,null, 0, 0, Tache.ID_TACHE_INEXISTANTE, Tache.ID_TACHE_INEXISTANTE, Tache.ID_TACHE_INEXISTANTE,Tache.ID_TACHE_INEXISTANTE);
    }

    public Tache(int idT, String nomT, String descT, Date dateDebutT, Date dateFinT, int durEstT, int durRelT, int idPro, int idComp, int idNiv, int idEmp)
    {
        this.id = idT;
        this.nom = nomT;
        this.description = descT;
        this.dateDebut = dateDebutT;
        this.dateFin = dateFinT;
        this.dureeReelle = durRelT;
        this.dureeEstimee = durEstT;
        this.idProjet = idPro;
        this.idCompetence = idComp;
        this.idNiveau = idNiv;
        this.idEmploye = idEmp;
    }

    /**
     * Constructeur par recopie
     * @param t	Tâche à copier
     */
    public Tache(Tache t)
    {
        this.id = t.id;
        this.nom = t.nom;
        this.description = t.description;
        this.dateDebut = t.dateDebut;
        this.dateFin = t.dateFin;
        this.dureeReelle = t.dureeReelle;
        this.dureeEstimee = t.dureeEstimee;
        this.idCompetence = t.idCompetence;
        this.idNiveau = t.idNiveau;
        this.idEmploye = t.idEmploye;
        this.idProjet = t.idProjet;
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

    public Date getDateFin() {
        return dateFin;
    }

    public int getDureeEstimee() {
        return dureeEstimee;
    }

    public int  getDureeReelle() {
        return dureeReelle;
    }

    public int getIdCompetence() {
        return idCompetence;
    }

    public int getIdNiveau() {
        return idNiveau;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public int getIdProjet() {
        return idProjet;
    }



    @Override
    public String toString() {
        return "[" +id+"] "+nom + " : "+description;    
        
    }

}
