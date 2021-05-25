package view.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe utilitaire de gestion des algorithmes de hachage SHA
 *  
 * @author Fabrice Pelleau
 */
public class HachageSHA {
	private static final String ALGORITHME_DEFAUT = "SHA-256";

	/**
	 * Calculer le code de Hachage (Hexadecimal) d'une chaine de caractères
	 * en utilisant l'algorithme par défaut Cf. ALGORITHME_DEFAUT.
	 * 
	 * @param message    le texte que l'on souhaite hacher
	 * @return           le HashCode Hexadecimal correspondant
	 */
	public static String calculerHashCode(String message) {
		return calculerHashCode(message, ALGORITHME_DEFAUT);
	}
	/**
	 * Calculer le code de Hachage (Hexadecimal) d'une chaîne de caractères.
	 * 
	 * @param message    le texte que l'on souhaite hacher
	 * @param algorithme l'algorithme à utiliser (Optionnel)
	 * @return           le HashCode Hexadecimal correspondant ou null si l'algorithme demandé n'est pas disponible
	 */
	private static String calculerHashCode(String message, String algorithme) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(algorithme);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("HachageSHA : librairie "+algorithme+" non disponible");
			return null;
		}
		// Calcul du code de hachage
		md.update(message.getBytes());
		byte byteData[] = md.digest();
		// Conversion en hexadécimal
		StringBuffer hexString = new StringBuffer(64);
		for ( byte bit : byteData ) {
			String hex=Integer.toHexString(0xff & bit);
			hexString.append( hex.length()==1 ? 0 : hex  );
		}
		return hexString.toString();
	}

}
