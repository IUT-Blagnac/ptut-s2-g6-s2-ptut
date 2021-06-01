package model.orm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.data.Client;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

public class AccessClient {
	
	public ArrayList<Client> getClient() throws DataAccessException, DatabaseConnexionException{
		ArrayList<Client> alClient=  new ArrayList<>();		
		try {
			Connection con = LogToDatabase.getConnexion();
			
			String pNom = "%%";
			
			String query = "Select c.* "
					+ "From Client c "
					+ "Where (upper(c.nom) like ?)";
			
			PreparedStatement pst = con.prepareStatement(query);	
			pst.setString(1, pNom);
			System.err.println(query);
			ResultSet rs = pst.executeQuery();
			
			 while (rs.next()) {
				 Client pro = new Client(rs.getInt("idNomCli"), rs.getString("nom"), rs.getString("prenom"),  rs.getString("entreprise"), rs.getString("email"), rs.getString("telephone"), rs.getInt("estActif"));
				 alClient.add(pro);
			 }

			return alClient;
			
		}catch (SQLException e) {
			throw new DataAccessException(Table.Client, Order.SELECT, "Erreur accés", e);
        }
	}
	
	public void insertClient (Client pClient) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		 try {
			 Connection con = LogToDatabase.getConnexion();
			 CallableStatement call;
			 
			 String query = "{Execute create_projet(?, ?, ?, ?, ?, ?, ?)}";
			 call = con.prepareCall(query);
			 
			 call.setString(1, pClient.getNom());
			 call.setString(2, pClient.getPrenom());
			 call.setString(3, pClient.getEntreprise());
			 call.setString(4, pClient.getEmail());
			 call.setString(5, pClient.getNumero());
			 call.setInt(6, pClient.getEstActif());
			 
			 call.registerOutParameter(7, java.sql.Types.INTEGER);
			 
			 call.execute();
			 
			 int retour = call.getInt(7);
			 
			 if (retour==0) {
				 System.out.println("Ajoutée");
			 }else {
				 System.out.println("erreur");
			 }
			 
		 }catch (SQLException e) {
				throw new DataAccessException(Table.Client, Order.SELECT, "Erreur accï¿½s", e);
	     }
	}
	public void updateProjet (Client pClient) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();

	        String query = "UPDATE CLIENT SET "
	                    + "NOM = " + "?" + " , "
	                    + "DESCRIPTION = " + "?" + " , "
	                    + "DATEDEBUT = " + "?" + " , "
	                    + "DATEFINESTIMEE = " + "?"  + " , "
	                    + "ESTACTIF = " + "?"  + " , "
	                    + "IDNOMCLI = "  + "?" + " , "
	                    + "WHERE IDNOM = ? ";

	        PreparedStatement pst = con.prepareStatement(query);
	        pst.setString(1, pClient.getNom());
	        pst.setString(2, pClient.getPrenom());
	        pst.setString(3, pClient.getEntreprise());
	        pst.setString(4, pClient.getEmail());
	        pst.setString(5, pClient.getNumero());
	        pst.setInt(6, pClient.getEstActif());
	        pst.setInt (7, pClient.getId());
	        System.err.println(query);

	        int result = pst.executeUpdate();
	        pst.close();

	        if (result != 1) {
	        	con.rollback();
	            throw new  RowNotFoundOrTooManyRowsException(Table.Client, Order.UPDATE, "Update anormal (update de moins ou plus d'une ligne)", null, result);
	       }
	       con.commit();

		} catch (SQLException e) {
	        throw new DataAccessException(Table.Client, Order.UPDATE, "Erreur accï¿½s", e);
	    }
	}
}
