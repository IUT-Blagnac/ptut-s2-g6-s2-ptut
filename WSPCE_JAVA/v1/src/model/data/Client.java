package model.data;

public class Client
{

    public static final int ID_CLIENT_INEXISTANT = -1000;

    public static final int EST_ACTIF = 1;
    public static final int EST_INACTIF = 0;

    private int id;
    private String nom;
    private String prenom;
    private String entreprise;
    private String email;
    private String numero;
    private int estActif ;

    /**
     * Constructeur par default
     */
    public Client(){
        this(Client.ID_CLIENT_INEXISTANT, null, null, null,null, null, Employe.EST_INACTIF);
    }

    /**
     * Constructeur avec parametre
     * @param idC id de l'employe
     * @param nomC nom de l'employe
     * @param prenomC prénom de l'employe
     * @param entrepriseC login de l'employe
     * @param estActifC actif (1) ou pas (0)
     */
    public Client(int idC, String nomC, String prenomC, String entrepriseC, String emailC, String numeroC, int estActifC)
    {
        id = idC;
        nom = nomC;
        prenom = prenomC;
        estActif = estActifC;
        entreprise = entrepriseC ;
        email = emailC;
        numero = numeroC;
    }

    /**
     * Constructeur par recopie
     * @param e	Client à copier
     */

    public Client(Client e)
    {
        id = e.id;
        nom = e.nom;
        prenom = e.prenom;
        entreprise = e.entreprise;
        estActif = e.estActif;
        email = e.email ;
        numero = e.numero;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEntreprise() {
        return entreprise;
    }


    public int getEstActif() {
        return estActif;
    }

    public String getEmail() {
        return email;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        String libelleActif = (estActif == Employe.EST_ACTIF ? "Actif" : "Inactif") ;
        return "[" +
                "id=" + id + "]" +
                "  " + nom +
                "  " + prenom +
                "  " + entreprise +
                "  " + email +
                "  " + numero +
                "  " + libelleActif;
    }
}
