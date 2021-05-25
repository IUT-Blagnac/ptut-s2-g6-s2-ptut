package model.orm;

import model.data.Competence;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe réalisant le lien entre le programme Java et les competences d'un employe décrit dans la classe Fromation et Competence de la base de données Oracle
 */
public class AccessCompetence {
    /**
     * Constructeur par default
     */
    public AccessCompetence(){}

    /**
     * Permet de récuperer toutes les competences enregistrées
     * @return Une ArrayList de Competence
     * @throws DataAccessException 
     * @throws DatabaseConnexionException 
     */
    public ArrayList<Competence> getAllCompetence() throws DataAccessException, DatabaseConnexionException{
        ArrayList<Competence> al = new ArrayList<>();

        try {
            // Connexion a la base de données
            Connection con = LogToDatabase.getConnexion();

            // Requete
            String query = "Select  *" +
                    "FROM Competence " ;

            PreparedStatement pst = con.prepareStatement(query);

            // Exécution de la requete
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                // On crée la competence à partir de la chaque ligne du select de la requete d'avant
                al.add(new Competence(rs.getInt("IDCOMPETENCE"), rs.getString("NOM"))) ;
            }

            // on ferme la requete
            rs.close();
            pst.close();
            return al;

        } catch (SQLException e) {
        	throw new DataAccessException(Table.Competence, Order.SELECT, "Erreur accès", e);
        }
    }

}
