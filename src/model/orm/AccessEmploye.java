package model.orm;

import model.data.*;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe réalisant le lien entre le programme Java et les employés de la base de données Oracle
 */
public class AccessEmploye {
	
	public static final int LOGIN_TROUVE = 1;
	public static final int LOGIN_TROUVE_INACTIF = 2;
	public static final int LOGIN_INEXISTANT_OU_PLUSIEURS = 0;
	
    public AccessEmploye() {
    }

    /**
     * Permet de récupérer un employé depuis la BD via son login et son mdp
     * L'Employe doit être actif pour pouvoir de sonnecter
     * 
     * @param pfLogin login de l'employé
     * @param pfMdp   mdp de l'employé
     * @param pEmploye employé trouvé (si code return == 1) 
     * @return	code de retour : 
     * 		- 1 : employé trouvé (pEmpTrouve renseigné), 
     * 		- 2 : employé trouve MAIS inactif, 
     * 		- 0 : employé
     * @throws DatabaseConnexionException 
     * @throws DataAccessException 
     */
    public int getEmployeParLogin (String pfLogin, String pfMdp, Employe pEmploye) throws DatabaseConnexionException, DataAccessException {
		try {
			Connection con = LogToDatabase.getConnexion();
			CallableStatement call;

			String q = "{call Authentifier (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
				// les ? correspondent aux paramètres : cf. déf sql procédure (10 paramètres)
			call = con.prepareCall(q);

			// Paramètres in
			call.setString(1, pfLogin);
				// 1 -> valeur du premier paramètre, cf. déf sql procédure 
			call.setString(2, pfMdp);

			// Paramètres out
			call.registerOutParameter(3, java.sql.Types.INTEGER);
				// 3 -> type du 3° paramètre qui est déclaré en OUT, cf. déf sql procédure 
			call.registerOutParameter(4, java.sql.Types.INTEGER);
			call.registerOutParameter(5, java.sql.Types.VARCHAR);
			call.registerOutParameter(6, java.sql.Types.VARCHAR);
			call.registerOutParameter(7, java.sql.Types.INTEGER);
			call.registerOutParameter(8, java.sql.Types.INTEGER);
			call.registerOutParameter(9, java.sql.Types.INTEGER);
			call.registerOutParameter(10, java.sql.Types.INTEGER);
			
			call.execute();

			// Récupérer les paramètres de sortie de la procédure, cf. déf sql procédure 
			int codeRetour = call.getInt(3);
			int idE = call.getInt(4);
			String nomE = call.getString(5);
			String prenomE = call.getString(6);
			int estActifE = call.getInt(7);
			int idRoleE= call.getInt(8);
			int idCompetenceE = call.getInt(9);
			int idNiveauE= call.getInt(10);

			Employe eResult;
			if (codeRetour == 1) { 
				eResult = new Employe(idE, nomE, prenomE, pfLogin, pfMdp, estActifE, idRoleE, idCompetenceE, idNiveauE);
			} else { 
				eResult = new Employe();
			}
			pEmploye.recopieEmploye(eResult);
			
			return codeRetour;
			
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.SELECT, "Erreur accès", e);
		}
    }

    /**
     * Permet de récuperer les employes de la base de données.
     * dont le nom ou le prénom commencent par nomOuPrenom
     * @param	nomOuPrenom	début du nom ou du prénom recherchés
     * @return une ArrayList d' Employe
     * @throws DataAccessException 
     * @throws DatabaseConnexionException 
     * @throws RowNotFoundOrTooManyRowsException 
     * 
     */
    public ArrayList<Employe> getEmployes(String nomOuPrenom) throws DataAccessException, DatabaseConnexionException, RowNotFoundOrTooManyRowsException {
        // Initialisation
        ArrayList<Employe> alEmploye = new ArrayList<>();

        try {
            // Connexion a la base de données
            Connection con = LogToDatabase.getConnexion();
            
            nomOuPrenom = "%"+nomOuPrenom.toUpperCase()+"%";

            // Requete
            String query = "Select e.* "
            		+ " FROM Employe e "
            		+ " WHERE (upper(e.nom) like ? OR upper(e.prenom) like ?)"
            		+ " ORDER BY e.nom";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, nomOuPrenom);
            pst.setString(2, nomOuPrenom);

            System.err.println(query);
            
            // Exécution de la requete
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // On crée l'employe

            	Employe emp = new Employe(rs.getInt("idemploye"), rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("motdepasse"), rs.getInt("estACtif"), rs.getInt("idRole"), rs.getInt("idCompetence"), rs.getInt("idNiveau"));
                // puis on recupere aussi ses competences et on les ajoute
                // On ajoute l'employe a l'arrayList
                alEmploye.add(emp) ;
            }

            // on ferme la requete
            rs.close();
            pst.close();
            return alEmploye;

        } catch (SQLException e) {
        	throw new DataAccessException(Table.Employe, Order.SELECT, "Erreur accés", e);
        }
    }


    /**
     * Permet d'inserer un employé dans la base de donnée
     * @param pfEmploye Un employé a insérer
     * @throws DataAccessException 
     * @throws DatabaseConnexionException 
     */
    public void insertEmploye(Employe pfEmploye) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
        try {
            Connection con = LogToDatabase.getConnexion();

            String query = "INSERT INTO EMPLOYE VALUES ("
                    + "seq_id_employe.NEXTVAL"
                    + ", " + "?"
                    + ", " + "?"
                    + ", " + "?"
                    + ", " + "?"
                    + ", " + "?"
                    + ", " + "?"
                    + ", " + "?"
                    + ", " + "?"
                    +")";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, pfEmploye.getNom());
            pst.setString(2, pfEmploye.getPrenom());
            pst.setString(3, pfEmploye.getLogin());
            pst.setString(4, pfEmploye.getMdp());
            pst.setInt (5, pfEmploye.getEstActif());
            pst.setInt (6, pfEmploye.getIdRole());
            pst.setInt (7, pfEmploye.getIdCompetence());
            pst.setInt (8, pfEmploye.getIdNiveau());

            System.err.println(query);

            int result = pst.executeUpdate();
            pst.close();

            if (result != 1) {
                con.rollback();
                throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.INSERT, "Insert anormal (insert de moins ou plus d'une ligne)", null, result);
            }
            con.commit();

        } catch (SQLException e) {
        	throw new DataAccessException(Table.Employe, Order.INSERT, "Erreur accés", e);
        }

    }

    /**
     * Permet de mettre à jour les attributs d'un employé passée en paramètre
     * @param pfEmploye un Employe en mettre a jour
     * @throws DataAccessException 
     * @throws DatabaseConnexionException 
     * @throws RowNotFoundOrTooManyRowsException 
     */
    public void updateEmploye(Employe pfEmploye) throws DataAccessException, DatabaseConnexionException, RowNotFoundOrTooManyRowsException{
        try {
            Connection con = LogToDatabase.getConnexion();

            String query = "UPDATE EMPLOYE SET "
                    + "NOM = " + "?" + " , "
                    + "PRENOM = " + "?" + " , "
                    + "LOGIN = " + "?" + " , "
                    + "MOTDEPASSE = " + "?"  + " , "
                    + "ESTACTIF = " + "?"  + " , "
                    + "IDROLE = "  + "?" + " , "
                    + "IDCOMPETENCE = " + "?"  + " , "
                    + "IDNIVEAU = "  + "?"
                    + " " + "WHERE IDEMPLOYE = ? ";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, pfEmploye.getNom());
            pst.setString(2, pfEmploye.getPrenom()) ;
            pst.setString(3, pfEmploye.getLogin());
            pst.setString(4, pfEmploye.getMdp());
            pst.setInt(5, pfEmploye.getEstActif());
            pst.setInt (6, pfEmploye.getIdRole());
            pst.setInt (7, pfEmploye.getIdCompetence());
            pst.setInt (8, pfEmploye.getIdNiveau());

            pst.setInt (9, pfEmploye.getId());

            System.err.println(query);

            int result = pst.executeUpdate();
            pst.close();

            if (result != 1) {
                con.rollback();
                throw new  RowNotFoundOrTooManyRowsException(Table.Employe, Order.UPDATE, "Update anormal (update de moins ou plus d'une ligne)", null, result);
            }
            con.commit();

        } catch (SQLException e) {
        	throw new DataAccessException(Table.Employe, Order.UPDATE, "Erreur accés", e);
        }


    }
}

