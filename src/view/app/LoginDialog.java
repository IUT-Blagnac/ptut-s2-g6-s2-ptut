package view.app;

import model.data.Employe;
import model.orm.AccessEmploye;
import model.orm.LogToDatabase;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import view.encryption.HachageSHA;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenetre de connexion à l'application (obligatoire pour acceder a l'accueil)
 */

@SuppressWarnings("serial")
public class LoginDialog extends JFrame {

    private Employe employe;

    private AccessEmploye ae = new AccessEmploye();
    
    private JPanel contentPane;
    private JPanel centerPane;
    private JPanel buttonGroup;

    private JLabel titreLabel;
    private JLabel loginLabel;
    private JLabel mdpLabel;
    private JTextField loginField;
    private JPasswordField mdpField;
    private JButton connexionButton;
    private JButton quitterButton;

    /**
     * Constructeur par default
     */
    public LoginDialog(){
        setSize(new Dimension(500,250));
        setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				actionQuitter();
			}
		});
        setTitle("Connexion RHESN");
        setResizable(false);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        setContentPane(contentPane);

        titreLabel = new JLabel("Application RHESN");
        titreLabel.setHorizontalAlignment(0);
        titreLabel.setPreferredSize(new Dimension(400,40));
        titreLabel.setFont(new Font("Arial", Font.BOLD, 25));
        contentPane.add(titreLabel, "North");

        centerPane = new JPanel(new GridLayout(4,1));
        centerPane.setBorder(BorderFactory.createTitledBorder("Connexion"));
        contentPane.add(centerPane,"Center");
        Border bordure = BorderFactory.createEmptyBorder(0,0,0,0);

        //Label Login
        loginLabel = new JLabel("Login :");
        centerPane.add(loginLabel);
        loginLabel.setBorder(bordure);

        //TextField Login
        JPanel logText = new JPanel(new FlowLayout());
        loginField = new JTextField();
        loginField.setPreferredSize(new Dimension(450, 20));
        logText.add(loginField);
        centerPane.add(logText);
        loginField.setBorder(bordure);

        //Label mot de passe
        mdpLabel = new JLabel("Mot de Passe");
        centerPane.add(mdpLabel);
        mdpLabel.setBorder(bordure);

        //TextField mot de passe
        JPanel mdpText = new JPanel();
        mdpField = new JPasswordField();
        mdpField.setPreferredSize(new Dimension(450, 20));
        mdpText.add(mdpField);
        centerPane.add(mdpText);
        mdpField.setBorder(bordure);

        buttonGroup = new JPanel(new FlowLayout());
        buttonGroup.setPreferredSize(new Dimension(500,40));
        contentPane.add(buttonGroup,"South");

        connexionButton = new JButton("Connexion");
        connexionButton.addActionListener(e -> actionConnexion());
        connexionButton.setBackground(new Color(104, 177, 255));
        connexionButton.setPreferredSize(new Dimension(100, 30));
        buttonGroup.add(connexionButton);

        quitterButton = new JButton("Quitter");
        quitterButton.setPreferredSize(connexionButton.getPreferredSize());
        quitterButton.addActionListener(e -> actionQuitter());
        quitterButton.setBackground(new Color(104, 177, 255));
        buttonGroup.add(quitterButton);

    }

    /**
     * Action réalisé des lors que l'on appuie sur le bouton connexion
     */
    private void actionConnexion() {
        loginLabel.setForeground(Color.BLACK);
        mdpLabel.setForeground(Color.BLACK);
        
        // Petite vérification de saisie
        if (loginField.getText().isEmpty() || mdpField.getPassword().length == 0){
        	String message = "";
        	if (loginField.getText().isEmpty()) { 
        		loginLabel.setForeground(Color.RED);
        		message = message + "votre login";
        	}
            if (mdpField.getPassword().length == 0 ){
                mdpLabel.setForeground(Color.RED);
                message = (message.length() == 0 ? message : message+ " et ");
                message = message + "votre mdp";
            }
            message = "Veuillez saisir " + message + ".";
            JOptionPane.showMessageDialog(this, message, "ERREUR", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int codeRetour=-1;
        try {
        	String passwd = HachageSHA.calculerHashCode(new String(mdpField.getPassword()));
        	employe = new Employe();
        	codeRetour = ae.getEmployeParLogin(loginField.getText(), passwd, employe);
		} catch (DatabaseConnexionException e) {
			new ExceptionDialog(this, e);
			dispose();
		} catch (DataAccessException e) {
			new ExceptionDialog(this, e);
			dispose();
		} 

        if (codeRetour == AccessEmploye.LOGIN_TROUVE) { // succès
            RHESNMainFrame root = new RHESNMainFrame(employe);
            root.setVisible(true);
            this.dispose();
        } else {
        	if (codeRetour == AccessEmploye.LOGIN_TROUVE_INACTIF) { // Trouvé mais inactif
        		JOptionPane.showMessageDialog(this, "Vous êtes reconnu mais l'accès est impossible (inactif).\nContactez l'administrateur.", "ATTENTION", JOptionPane.WARNING_MESSAGE);
        	} else if (codeRetour == AccessEmploye.LOGIN_INEXISTANT_OU_PLUSIEURS) { // Non trouvé
        		JOptionPane.showMessageDialog(this, "Login/mpd incorrect.\n0 ou plusieurs personnes existent pour ce login ...\n Réessayez ...", "ERREUR", JOptionPane.ERROR_MESSAGE);
        	}
        	mdpField.setText("");;
        	loginField.setText("");
        }
    }

    /**
     * Action réalisé des lors que l'on appuis sur le bouton deconnexion
     */
    private void actionQuitter() {
    	int input;
    	
    	input = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment quitter l'application", "Avertissement",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        // Si on valide la confirmation (bouton oui)
        if (input == JOptionPane.OK_OPTION){
			try {
				LogToDatabase.closeConnexion();
			} catch (DatabaseConnexionException e) {
				new ExceptionDialog(this, e);
			}
	        this.dispose();
        }
    }
}