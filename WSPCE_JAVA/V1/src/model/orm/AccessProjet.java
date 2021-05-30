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
	public ArrayList<Projet> getProjet() throws DataAccessException, DatabaseConnexionException, RowNotFoundOrTooManyRowsException {
		ArrayList<Projet> alProjet =  new ArrayList<>();
		
		try {
			Connection con = LogToDatabase.getConnexion();
			
			String query = "Select p.*"
					+ "From Projet p"
					+ "Order by p.nom";
			
			PreparedStatement pst = con.prepareStatement(query);	
			System.err.println(query);
			ResultSet rs = pst.executeQuery();
			
			 while (rs.next()) {
				 Projet pro = new Projet(rs.getInt("id"), rs.getString("nom"), rs.getString("description"), rs.getDate("dateDebut"), rs.getDate("dateFinEstimee"), rs.getDate("dateFinReel"), rs.getInt("estActif"));
				 alProjet.add(pro);
			 }

			return alProjet;
			
		}catch (SQLException e) {
			throw new DataAccessException(Table.Projet, Order.SELECT, "Erreur accés", e);
        }
	}
	
	public ArrayList<Projet> getProjet(String pNom) throws DataAccessException, DatabaseConnexionException, RowNotFoundOrTooManyRowsException {
		ArrayList<Projet> alProjet =  new ArrayList<>();
		
		try {
			Connection con = LogToDatabase.getConnexion();
			
			String query = "Select p.*"
					+ "From Projet p"
					+ "Where nom = p.nom";
			
			PreparedStatement pst = con.prepareStatement(query);	
			System.err.println(query);
			ResultSet rs = pst.executeQuery();
			
			 while (rs.next()) {
				 Projet pro = new Projet(rs.getInt("id"), rs.getString("nom"), rs.getString("description"), rs.getDate("dateDebut"), rs.getDate("dateFinEstimee"), rs.getDate("dateFinReel"), rs.getInt("estActif"));
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
			 
			 call.registerOutParameter(6, java.sql.Types.INTEGER);
			 
			 call.execute();
			 
			 int retour = call.getInt(6);
			 
			 if (retour==0) {
				 System.out.println("Ajoutée");
			 }else {
				 System.out.println("erreur");
			 }
			 
		 }catch (SQLException e) {
				throw new DataAccessException(Table.Projet, Order.SELECT, "Erreur accés", e);
	     }
	}

	//vide pour l'instant pour les tests, pour Nathan
	public void updateProjet(Projet pfProjet)
	{

	}
	
	
}
