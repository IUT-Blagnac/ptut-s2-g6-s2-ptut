package model.orm;

import model.data.Niveau;
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
 * Classe réalisant le lien entre le programme Java et les Niveaux disponibles de la base de données Oracle
 */
public class AccessNiveau {

    /**
     * Permet de récuperer toutes les niveaux enregistrées dans la BD
     * @return Une ArrayList de Niveaux
     * @throws DatabaseConnexionException 
     * @throws DataAccessException 
     */
    public ArrayList<Niveau> getAllNiveaux() throws DatabaseConnexionException, DataAccessException{
        ArrayList<Niveau> al = new ArrayList<>();

        try {
            // Connexion a la base de données
            Connection con = LogToDatabase.getConnexion();

            // Requete
            String query = "Select  *" +
                    "FROM NIVEAU " +
                    "ORDER BY IDNIVEAU ";

            PreparedStatement pst = con.prepareStatement(query);

            // Exécution de la requete
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                // On crée la competence à partir de la chaque ligne du select de la requete d'avant
                al.add(new Niveau(rs.getInt("idNiveau"), rs.getString("INTITULE"))) ;
            }

            // on ferme la requete
            rs.close();
            pst.close();
            return al;

        } catch (SQLException e) {
        	 throw new DataAccessException(Table.Niveau, Order.SELECT, "Erreur accès", e);
        }
    }
}
