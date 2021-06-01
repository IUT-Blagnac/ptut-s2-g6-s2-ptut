package model.data;

import java.util.Date;

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
    private Projet projet;
    private Competence competence;
    private Niveau niveau;
    private Employe employe;

    /**
     * Constructeur par default
     */
    public Tache()
    {
        this(Tache.ID_TACHE_INEXISTANTE, null, null, null, null,null, 0, 0, null, null, null);
    }

    public Tache(int idT, String nomT, String descT, Projet projetT, Date dateDebutT, Date dateFinT, int durEstT, int durRelT, Competence compT, Niveau nivT, Employe empT)
    {
        this.id = idT;
        this.nom = nomT;
        this.description = descT;
        this.dateDebut = dateDebutT;
        this.dateFin = dateFinT;
        this.dureeReelle = durRelT;
        this.dureeEstimee = durEstT;
        this.competence = compT;
        this.niveau = nivT;
        this.employe = empT;
        this.projet = projetT;
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
        this.competence = t.competence;
        this.niveau = t.niveau;
        this.employe = t.employe;
        this.projet = t.projet;
    }


    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
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

    public Competence getCompetence() {
        return competence;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Projet getProjet() {
        return projet;
    }



    @Override
    public String toString() {

        return "[" +
                "projet=" + projet.getNom() + "]" +
                "  " + nom +
                "  " + competence.toString() +
                "  " + niveau.toString() +
                "  " + employe.toString();
    }

}
