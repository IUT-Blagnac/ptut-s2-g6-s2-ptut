package view.app;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import view.encryption.HachageSHA;

import java.awt.*;

/**
 * Fenetre d'édition d'un employe : Create Update Delete  
 */

@SuppressWarnings("serial")
public class PasswordEditor extends JDialog {
	
	public enum ModeEdition {
		CREATION, MODIFICATION
	};

	
    // Panels
    private JPanel contentPane;
    private JPanel champsPanel ;
    private JPanel boutonsPanel ;

    // Label titre
    private JLabel titreLabel ;

    // Police d'écriture
    private Font titreFont = new Font("Arial", Font.BOLD, 22) ;
    private Font normalFont = new Font("Arial", Font.PLAIN, 16) ;

    // Boutons
    private JButton enregistrerBouton ;
    private JButton annulerBouton ;

    // Dimension
    private Dimension dimensionBouton = new Dimension(140,35) ;
    private Dimension dimensionLabel = new Dimension(200,40) ;
    private Dimension dimensionText = new Dimension(135,25) ;
    
    private JLabel loginLabel ;
    private JLabel mdpLabel ;
    private JLabel reMDPLabel ;

    // Zones de saisie
    private JTextField loginText ;
    private JPasswordField mdpText ;
    private JPasswordField reMDPText ;
    
	private String loginUserMisAJour;
	private String passwordResult;
	
	private PasswordEditor.ModeEdition modeActuel;
	/**
	 * Ouverture de la boite de dialogue de saisie d'un mot de passe
	 *
	 * @param owner   fenêtre  mère de la boite de dialogue
	 * 
	 * @return un objet String qui ets le mot de passe saisi / null sinon
	 */
	public static String showPassWordEditor(Window owner, String loginUser, PasswordEditor.ModeEdition mode) {
		
		PasswordEditor dial = new PasswordEditor(owner, loginUser, mode);
        dial.passwordResult = null;
		dial.setModal(true);
		dial.setVisible(true);
		// le programme appelant est bloqué jusqu'au masquage de la JDialog.
		dial.dispose();
		return dial.passwordResult;
	}

	// =======================================================================
	// Les constructeurs de la classe sont privés
	// Pour créer un éditeur, Il faut utiliser la méthode statique 
	// == > showPassWordEditor() 
	// =======================================================================
	private PasswordEditor(Window owner, String loginUSer, PasswordEditor.ModeEdition mode) {
        
        super(owner);
        this.passwordResult = null;
        this.loginUserMisAJour = loginUSer;
        this.modeActuel = mode;
        
        setTitle("Saisie d'un mot de passe");
        setSize(450, 340) ;
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                
        // Border
        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder title = BorderFactory.createTitledBorder(
                blackline, "Veuillez saisir un MOT DE PASSE VALIDE sur votre système",
                TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.ITALIC, 15) );

        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border compound = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);

        // Toute la fenetre
        contentPane = new JPanel(new BorderLayout()) ;
        setContentPane(contentPane);

        // Titre label
        titreLabel = new JLabel("Créer mot de passe");
        if (modeActuel == PasswordEditor.ModeEdition.MODIFICATION) {
        	titreLabel.setText("Changer mot de passe");
        }
        
        titreLabel.setHorizontalAlignment(0);
        titreLabel.setPreferredSize(new Dimension(400, 80));
        titreLabel.setFont(titreFont);
        titreLabel.setBorder(compound);

        contentPane.add(titreLabel, BorderLayout.NORTH);

        // Champs Panel
        champsPanel = new JPanel(new FlowLayout()) ;
        contentPane.add(champsPanel,  BorderLayout.CENTER);
        champsPanel.setBorder(title);

        // Boutons Panel
        boutonsPanel = new JPanel(new FlowLayout());
        boutonsPanel.setPreferredSize(new Dimension(150,50));
        contentPane.add(boutonsPanel, BorderLayout.SOUTH);

        // Boutons
        enregistrerBouton = new JButton("Créer MP");
        if (modeActuel == PasswordEditor.ModeEdition.MODIFICATION) {
        	enregistrerBouton.setText("Changer MP");
        }
        enregistrerBouton.setBackground(new Color(104, 177, 255)) ;
        enregistrerBouton.setPreferredSize(dimensionBouton);
        enregistrerBouton.addActionListener(e -> actionOK());

        boutonsPanel.add(enregistrerBouton);

        annulerBouton = new JButton("Annuler");
        annulerBouton.setBackground(new Color(104, 177, 255)) ;
        annulerBouton.setPreferredSize(dimensionBouton);
        annulerBouton.addActionListener(e -> actionAnnuler());
        annulerBouton.setActionCommand("quitter");

        boutonsPanel.add(annulerBouton,BorderLayout.SOUTH);
        
	    
        // login
        loginLabel = new JLabel("Login") ;
        loginLabel.setHorizontalAlignment(0);
        loginLabel.setPreferredSize(dimensionLabel);
        loginLabel.setFont(normalFont);
        champsPanel.add(loginLabel);
        
        loginText = new JTextField();
        loginText.setPreferredSize(dimensionText);
        loginText.setText(loginUserMisAJour);
        loginText.setEnabled(false);
        champsPanel.add(loginText);

        // Mot de passe
        mdpLabel = new JLabel("Mot de passe") ;
        mdpLabel.setHorizontalAlignment(0);
        mdpLabel.setPreferredSize(dimensionLabel);
        mdpLabel.setFont(normalFont);
        champsPanel.add(mdpLabel);

        mdpText = new JPasswordField();
        mdpText.setPreferredSize(dimensionText);
        champsPanel.add(mdpText);

     // Mot de passe
        reMDPLabel = new JLabel("Resaisie") ;
        reMDPLabel.setHorizontalAlignment(0);
        reMDPLabel.setPreferredSize(dimensionLabel);
        reMDPLabel.setFont(normalFont);
        champsPanel.add(reMDPLabel);

        reMDPText = new JPasswordField();
        reMDPText.setPreferredSize(dimensionText);
        champsPanel.add(reMDPText);
    }

    private void actionOK() {
    	String mdp = new String(mdpText.getPassword());
    	String reMDP =  new String(reMDPText.getPassword());
    	
    	if (mdp.isEmpty()) {
    		JOptionPane.showMessageDialog(this, "Saisir un mot de passe", "Attention", JOptionPane.ERROR_MESSAGE);
    		mdpText.setText("");
    		reMDPText.setText("");
    		return ;
    	}
    	if (mdp.contains(" ") || mdp.contains("\t") || mdp.contains("\n") || mdp.contains("\b")) {
    		JOptionPane.showMessageDialog(this, "Ne pas saisir de caractère(s) invalide(s)", "ERREUR", JOptionPane.ERROR_MESSAGE);
    		mdpText.setText("");
    		reMDPText.setText("");
    		return ;
    	}
    	if (mdp.compareTo(reMDP) != 0) {
    		JOptionPane.showMessageDialog(this, "Saisir deux fois le même mot de passe", "ERREUR", JOptionPane.ERROR_MESSAGE);
    		mdpText.setText("");
    		reMDPText.setText("");
    		return ;
    	}
    	
    	this.passwordResult = HachageSHA.calculerHashCode(mdp);
        this.setVisible(false);
    }

    private void actionAnnuler() {
    	this.passwordResult = null;
        this.setVisible(false);

    }
}
