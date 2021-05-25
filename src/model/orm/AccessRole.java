package model.orm;


import model.data.Role;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe réalisant le lien entre le programme Java et les role décrit dans la classe Role de la base de données Oracle
 */
public class AccessRole {
    /**
     * Constructeur par défault
     */
    public AccessRole(){}

    /**
     * Permet de récuperer le role de l'employe selon son id
     * @param pfId identifiant de l'employé
     * @return un role ou null
     * @throws DataAccessException 
     * @throws DatabaseConnexionException 
     * @throws RowNotFoundOrTooManyRowsException 
     */
    public Role getRoleById(int pfId ) throws DataAccessException, DatabaseConnexionException, RowNotFoundOrTooManyRowsException{
        Role roleTrouve ;

        try {
            Connection con = LogToDatabase.getConnexion();
            String query = "Select r.IDrole, r.nom FROM Role r , Employe e WHERE e.idemploye = ? AND e.idrole = r.idrole";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1,pfId);

            ResultSet rs = pst.executeQuery();

            //Si trouvé
            if(rs.next()){
                int idRole = rs.getInt("idRole");
                String nom = rs.getString("nom");

                roleTrouve = new Role(idRole, nom);
            }else{
                //Si non trouvé
                rs.close();
                pst.close();
                return null;
            }

            //Si on trouve plus d'une personne
            if(rs.next()){
                rs.close();
                pst.close();
                throw new RowNotFoundOrTooManyRowsException(Table.Role, Order.SELECT, "Recherche anormale (en trouve au moins 2)", null, 2);
            }

            rs.close();
            pst.close();
            return roleTrouve;
        } catch (SQLException e) {
        	throw new DataAccessException(Table.Role, Order.SELECT, "Erreur accès", e);
        }
    }
}
