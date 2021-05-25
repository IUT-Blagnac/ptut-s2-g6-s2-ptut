package model.data;

/**
 * Role attribué à un employe sur l'application (ex : Chef de Projet OU Employé)
 */
public class Role {
	
	public static final int ID_ROLE_CHEF_PROJET = 1;
	public static final String LIB_ROLE_CHEF_PROJET = "Chef de Projet";
	
	public static final int ID_ROLE_EMPLOYE = 2;
	public static final String LIB_ROLE_EMPLOYE = "Employé";
	
    // Attributs
    private int idRole ;
    private String nom ;

    /**
     * Constructeur avec paramètre
     * @param idRole id du Role
     * @param nom nom du Role
     */
    public Role(int idRole, String nom) {
        this.idRole = idRole;
        this.nom = nom;
    }


    /*GETTER SETTER*/

    public int getIdRole() {
        return idRole;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "Role{" +
                "idRole=" + idRole +
                ", nom='" + nom + '\'' +
                '}';
    }
}
