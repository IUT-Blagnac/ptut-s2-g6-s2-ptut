package model.data;

/**
 * Classe Employé, qui décrit une personne travaillant sur un projet
 */
public class Employe {

	public static final int ID_EMPLOYE_INEXISTANT = -1000;
	
	public static final int EST_ACTIF = 1;
	public static final int EST_INACTIF = 0;
	
    private int id;
    private String nom;
    private String prenom;
    private String login;
    private String mdp;
    private int estActif ;
    private int idRole ;
    private int idCompetence;
    private int idNiveau;


	/**
     * Constructeur par default
     */
    public Employe(){
        this(Employe.ID_EMPLOYE_INEXISTANT, null, null, null,null, Employe.EST_INACTIF, -1, -1, -1);
    }

    /**
     * Constructeur avec parametre
     * @param idE id de l'employe
     * @param nomE nom de l'employe
     * @param prenomE prénom de l'employe
     * @param loginE login de l'employe
     * @param mdpE mot de passe de l'employe
     * @param estActifE actif (1) ou pas (0) 
     * @param idRoleE role
     * @param idCompetenceE competence
     * @param idNiveauE niveau
     */
    public Employe(int idE, String nomE, String prenomE, String loginE, String mdpE, int estActifE, int idRoleE, int idCompetenceE, int idNiveauE){
        id = idE;
        nom = nomE;
        prenom = prenomE;
        login = loginE;
        mdp = mdpE;
        estActif = estActifE;
        idRole = idRoleE ;
        idCompetence = idCompetenceE;
        idNiveau = idNiveauE;
    }
    
    /**
     * Constructeur par recopie
     * @param e	Employe à copier
     */
    
    public Employe(Employe e){
        id = e.id;
        nom = e.nom;
        prenom = e.prenom;
        login = e.login;
        mdp = e.mdp;
        estActif = e.estActif;
        idRole = e.idRole ;
        idCompetence = e.idCompetence;
        idNiveau = e.idNiveau;
    }
    
    public void recopieEmploye(Employe e){
        id = e.id;
        nom = e.nom;
        prenom = e.prenom;
        login = e.login;
        mdp = e.mdp;
        estActif = e.estActif;
        idRole = e.idRole ;
        idCompetence = e.idCompetence;
        idNiveau = e.idNiveau;
    }


    /*GETTER SETTER*/

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getLogin() {
        return login;
    }

    public String getMdp() {
        return mdp;
    }
    
    public int getIdRole() {
        return idRole;
    }
    
    public int getEstActif() {
		return estActif;
	}

    public int getIdCompetence() {
        return idCompetence;
    }

    public int getIdNiveau() {
		return idNiveau;
	}
    
    @Override
    public String toString() {
    	String libelleActif = (estActif == Employe.EST_ACTIF ? "Actif" : "Inactif") ;
        return "[" +
                "id=" + id + "]" +
                "  " + nom + 
                "  " + prenom + 
                "   (" + login + " - "
                + libelleActif + ")";
    }

}
