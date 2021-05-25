package model.data;


/**
 * Classe Niveau qui d�crit un niveau (logique)
 */
public class Niveau {

    private int idNiveau ;
    private String intitule ;

    /**
     * Constructeur avec param�tre de la classe Niveau
     * @param idNiveau id du niveau
     * @param intitule intitulé du niveau
     */
    public Niveau(int idNiveau, String intitule) {
        this.idNiveau = idNiveau;
        this.intitule = intitule;
    }

    /*GETTER SETTER*/

    public int getIdNiveau() {
        return idNiveau;
    }

    public String getIntitule() {
        return intitule;
    }

    @Override
    public String toString() {
        return "Niveau{" +
                "idNiveau=" + idNiveau +
                ", intitule='" + intitule + '\'' +
                '}';
    }
}
