package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.data.Tache;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

public class AccessTache {
	
	public ArrayList<Tache> getTache(String pNom) throws DataAccessException, DatabaseConnexionException{
		ArrayList<Tache> alTache = new ArrayList<>();
		
		try {
			Connection con = LogToDatabase.getConnexion();
			
			pNom = "%" + pNom.toUpperCase() + "%";
			
			String query = "Select t.* "
					+ "From Tache t "
					+ "Where (upper(t.nom) like ?)";
			
			PreparedStatement pst = con.prepareStatement(query);	
			pst.setString(1, pNom);
			System.err.println(query);
			ResultSet rs = pst.executeQuery();
			
			 while (rs.next()) {
				 Tache tache = new Tache(rs.getInt("idTache"), rs.getString("nom"), rs.getString("descriptions"), rs.getDate("dateDebutReal"),rs.getDate("dateFinReal"),rs.getInt("dureeEstimeeReal"),rs.getInt("dureeReelleReal"),rs.getInt("idProjet"),rs.getInt("idCompetence"),rs.getInt("idNiveau"),rs.getInt("idEmploye"));
				 alTache.add(tache);
			 }
		}catch (SQLException e) {
			throw new DataAccessException(Table.Tache, Order.SELECT, "Erreur accés", e);
        }
		return alTache;
	}
	
	public void insertTache (Tache pTache) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
            Connection con = LogToDatabase.getConnexion();

            String query = "INSERT INTO TACHE VALUES ("
            		+ "seq_id_tache.NEXTVAL"
            		+ ", " + "?"
            		+ ", " + "?"
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
            pst.setString(1, pTache.getNom());
            pst.setString(2, pTache.getDescription());
            pst.setDate(3, pTache.getDateDebut());
            pst.setDate(4, pTache.getDateFin());
            pst.setInt (5, pTache.getDureeEstimee());
            pst.setInt (6, pTache.getDureeReelle());
            pst.setInt (7, pTache.getIdProjet());
            pst.setInt (8, pTache.getIdCompetence());
            pst.setInt (9, pTache.getIdNiveau());
            pst.setInt (10, pTache.getIdEmploye());

            System.err.println(query);
            
            int result = pst.executeUpdate();
            pst.close();

            if (result != 1) {
            	con.rollback();
            	throw new RowNotFoundOrTooManyRowsException(Table.Tache, Order.INSERT, "Insert anormal (insert de moins ou plus d'une ligne)", null, result);
            }
            con.commit();
        } catch (SQLException e) {
        	throw new DataAccessException(Table.Tache, Order.INSERT, "Erreur accÃ©s", e);
        }
	}
	
	public void updateTache (Tache pTache) {
		
	}
}
