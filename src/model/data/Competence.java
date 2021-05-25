package model.data;

/**
 * Classe compétence qui réference les compétence acquis d'un employé
 */
public class Competence {
	
    private int idCompetence;
    private String nom ;

    /**
     * Constructeur avec paramètre
     * @param idEmploye id de l'employe
     * @param nom nom de la compétance
     */
    public Competence(int idEmploye, String nom ) {
        this.idCompetence = idEmploye;
        this.nom = nom;
    }

    /*GETTER SETTER*/

    public int getIdCompetence() {
        return idCompetence;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "Competence{" +
                "idCompetence=" + idCompetence +
                ", nom='" + nom + '\'' +
                '}';
    }
}
