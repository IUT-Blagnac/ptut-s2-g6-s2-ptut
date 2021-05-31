package model.orm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.data.Projet;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

public class AccessProjet {
	
	public ArrayList<Projet> getProjet(String pNom) throws DataAccessException, DatabaseConnexionException, RowNotFoundOrTooManyRowsException {
		ArrayList<Projet> alProjet =  new ArrayList<>();
		
		try {
			Connection con = LogToDatabase.getConnexion();
			
			pNom = "%" + pNom.toUpperCase() + "%";
			
			String query = "Select p.* "
					+ "From Projet p "
					+ "Where (upper(p.nom) like ?)";
			
			PreparedStatement pst = con.prepareStatement(query);	
			pst.setString(1, pNom);
			System.err.println(query);
			ResultSet rs = pst.executeQuery();
			
			 while (rs.next()) {
				 Projet pro = new Projet(rs.getInt("idNom"), rs.getString("nom"), rs.getString("descriptions"), rs.getDate("dateDebut"), rs.getDate("dateFinEstimee"), rs.getDate("dateFinReel"), rs.getInt("estActif"), rs.getInt("idNomCli"));
				 alProjet.add(pro);
			 }

			return alProjet;
			
		}catch (SQLException e) {
			throw new DataAccessException(Table.Projet, Order.SELECT, "Erreur accés", e);
        }
		
	}
	
	public ArrayList<Projet> getProjet(int pIdCli) throws DatabaseConnexionException, DataAccessException{
		ArrayList<Projet> alProjet =  new ArrayList<>();
		
		try {
			Connection con = LogToDatabase.getConnexion();
			
			String query = "Select p.*"
					+ "From Projet p"
					+ "Where p.idNomCli = ?";
			
			PreparedStatement pst = con.prepareStatement(query);	
			pst.setInt(1, pIdCli);
			System.err.println(query);
			ResultSet rs = pst.executeQuery();
			
			 while (rs.next()) {
				 Projet pro = new Projet(rs.getInt("id"), rs.getString("nom"), rs.getString("description"), rs.getDate("dateDebut"), rs.getDate("dateFinEstimee"), rs.getDate("dateFinReel"), rs.getInt("estActif"), rs.getInt("estActif"));
				 alProjet.add(pro);
			 }

			return alProjet;
			
		}catch (SQLException e) {
			throw new DataAccessException(Table.Projet, Order.SELECT, "Erreur accés", e);
        }	
	}
	
	public void insertProjet (Projet pProjet) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		 try {
			 Connection con = LogToDatabase.getConnexion();
			 CallableStatement call;
			 
			 String query = "Execute create_projet(?, ?, ?, ?, ?, ?)";
			 call = con.prepareCall(query);
			 
			 call.setString(1, pProjet.getNom());
			 call.setString(2, pProjet.getDescription());
			 call.setDate(3, (Date) pProjet.getDateDebut());
			 call.setDate(4, (Date) pProjet.getDateFinEstimee());
			 call.setInt(5, pProjet.getEstActif());
			 call.setInt(6, pProjet.getIdCli());
			 
			 call.registerOutParameter(7, java.sql.Types.INTEGER);
			 
			 call.execute();
			 
			 int retour = call.getInt(7);
			 
			 if (retour==0) {
				 System.out.println("Ajoutée");
			 }else {
				 System.out.println("erreur");
			 }
			 
		 }catch (SQLException e) {
				throw new DataAccessException(Table.Projet, Order.SELECT, "Erreur accés", e);
	     }
	}
	
	public void updateProjet (Projet pProjet) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		 try {
	            Connection con = LogToDatabase.getConnexion();

	            String query = "UPDATE PROJET SET "
	                    + "NOM = " + "?" + " , "
	                    + "DESCRIPTION = " + "?" + " , "
	                    + "DATEDEBUT = " + "?" + " , "
	                    + "DATEFINESTIMEE = " + "?"  + " , "
	                    + "ESTACTIF = " + "?"  + " , "
	                    + "IDNOMCLI = "  + "?" + " , "
	                    + "WHERE IDNOM = ? ";

	            PreparedStatement pst = con.prepareStatement(query);
	            pst.setString(1, pProjet.getNom());
	            pst.setString(2, pProjet.getDescription()) ;
	            pst.setDate(3, (Date) pProjet.getDateDebut());
	            pst.setDate(4, (Date) pProjet.getDateFinEstimee());
	            pst.setInt(5, pProjet.getEstActif());
	            pst.setInt (6, pProjet.getIdCli());
	            pst.setInt (7, pProjet.getId());

	            System.err.println(query);

	            int result = pst.executeUpdate();
	            pst.close();

	            if (result != 1) {
	                con.rollback();
	                throw new  RowNotFoundOrTooManyRowsException(Table.Projet, Order.UPDATE, "Update anormal (update de moins ou plus d'une ligne)", null, result);
	            }
	            con.commit();

	        } catch (SQLException e) {
	        	throw new DataAccessException(Table.Projet, Order.UPDATE, "Erreur accés", e);
	        }
	}
}
