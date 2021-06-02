package view.app;

import model.data.*;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;


/**
 * Fenetre d'édition d'un client : Create Update Delete  
 */

@SuppressWarnings("serial")
public class ClientEditor extends JDialog {
	
	public enum ModeEdition {
		CREATION, MODIFICATION, VISUALISATION
		// pas de suppression car fonctionnellement impossible
	};

	
	private ModeEdition modeActuel;
	
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
    
    // Zones de texte titres des saisies
    private JLabel idLabel ;
    private JLabel nomLabel ;
    private JLabel prenomLabel ;
    private JLabel entrepriseLabel ;
    private JLabel estActifLabel;
    private JLabel emailLabel ;
    private JLabel telLabel ;

    // Zones de saisie
    private JTextField idText ;
    private JTextField nomText ;
    private JTextField prenomText ;
    private JTextField entreprise ;
    private JTextField email ;
    private JTextField tel ;
    
    private JCheckBox estActifTB;


    // Employe qui utilise l'application
	@SuppressWarnings("unused")
	private Employe employueUtilisateur;
	
	// Employé modifié ou visualisé
	private Client clientEdite;
	
	// Employé résultat (saisi ou modifié), null si annulation
	private Client clientResult;
	
	/**
	 * Ouverture de la boite de dialogue d'�dition d'un client
	 *
	 * @param owner   fenêtre  mère de la boite de dialogue
	 * @param employeUtilisateur Employ" connecté à l'application
	 * @param clientEdite  Objet de type Client à éditer (éventuellement null en création).
	 * @param mode    Mode d'ouverture (CREATION, MODIFICATION, VISUALISATION)
	 * 
	 * @return un objet Client si l'action est validée / null sinon
	 */
	public static Client showClientEditor(Window owner, Employe employeUtilisateur, Client clientEdite, ClientEditor.ModeEdition mode) {
		
		if (mode == ClientEditor.ModeEdition.CREATION) {
			clientEdite = new Client();
		} else {
			clientEdite = new Client(clientEdite);
		}
		
		ClientEditor dial = new ClientEditor(clientEdite, employeUtilisateur, owner, mode);
        dial.clientResult = null;
		dial.setModal(true);
		dial.setVisible(true);
		// le programme appelant est bloqué jusqu'au masquage de la JDialog.
		dial.dispose();
		return dial.clientResult;
	}

	// =======================================================================
	// Les constructeurs de la classe sont privés
	// Pour créer un éditeur, Il faut utiliser la méthode statique 
	// == > showEmployeEditor() 
	// =======================================================================
	private ClientEditor(Client pfClientEdite, Employe pfEmployeUtilisateur, Window owner, ClientEditor.ModeEdition pfMode) {
        
        super(owner);
        this.employueUtilisateur = pfEmployeUtilisateur ;
        this.clientEdite = pfClientEdite;
        this.clientResult = null;
        this.modeActuel = pfMode;
        
       /* try {
	        alProjetBD = ap.getAllNiveaux() ;
        } catch (DatabaseConnexionException | DataAccessException e1) {
			new ExceptionDialog(this, e1);
			JOptionPane.showMessageDialog(this, 
				"Impossible de continuer !\nMise à jour annulée.", "ERREUR", JOptionPane.ERROR_MESSAGE);
			actionAnnuler();
		} 
        */

        
        setTitle("Gestion d'un client");
        setSize(400, 620) ;
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        // Border
        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder title = BorderFactory.createTitledBorder(
                blackline, "Veuillez remplir tous les champs possibles",
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
        titreLabel = new JLabel("tempo");
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
        enregistrerBouton = new JButton("Enregister");
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

        // ID
        idLabel = new JLabel("ID") ;
        idLabel.setHorizontalAlignment(0);
        idLabel.setPreferredSize(dimensionLabel);
        idLabel.setFont(normalFont);

        champsPanel.add(idLabel);

        idText = new JTextField("Généré automatiquement");
        idText.setPreferredSize(dimensionText);
        idText.setEnabled(false);
        champsPanel.add(idText);

        // Nom
        nomLabel = new JLabel("Nom") ;
        nomLabel.setHorizontalAlignment(0);
        nomLabel.setPreferredSize(dimensionLabel);
        nomLabel.setFont(normalFont);
        champsPanel.add(nomLabel);
        
        nomText = new JTextField();
        nomText.setPreferredSize(dimensionText);
        champsPanel.add(nomText);

        // Prenom
        prenomLabel = new JLabel ("Prénom") ;
        prenomLabel.setHorizontalAlignment(0);
        prenomLabel.setPreferredSize(dimensionLabel);
        prenomLabel.setFont(normalFont);
        champsPanel.add(prenomLabel);
        
        prenomText = new JTextField();
        prenomText.setPreferredSize(dimensionText);
        champsPanel.add(prenomText);

        // Entreprise
        entrepriseLabel = new JLabel("Entreprise") ;
        entrepriseLabel.setHorizontalAlignment(0);
        entrepriseLabel.setPreferredSize(dimensionLabel);
        entrepriseLabel.setFont(normalFont);
        champsPanel.add(entrepriseLabel);

        entreprise = new JTextField();
        entreprise.setPreferredSize(dimensionText);
        champsPanel.add(entreprise);
        
     // Email
        emailLabel = new JLabel("Email") ;
        emailLabel.setHorizontalAlignment(0);
        emailLabel.setPreferredSize(dimensionLabel);
        emailLabel.setFont(normalFont);
        champsPanel.add(emailLabel);

        email = new JTextField();
        email.setPreferredSize(dimensionText);
        champsPanel.add(email);
        
     // Tel
        telLabel = new JLabel("T�l�phone") ;
        telLabel.setHorizontalAlignment(0);
        telLabel.setPreferredSize(dimensionLabel);
        telLabel.setFont(normalFont);
        champsPanel.add(telLabel);

        tel = new JTextField();
        tel.setPreferredSize(dimensionText);
        champsPanel.add(tel);
        
        // estActif
        estActifLabel = new JLabel("estActif ?") ;
        estActifLabel.setHorizontalAlignment(0);
        estActifLabel.setPreferredSize(dimensionLabel);
        estActifLabel.setFont(normalFont);
        champsPanel.add(estActifLabel);

        estActifTB = new JCheckBox();
        estActifTB.setSelected(true);
        champsPanel.add(estActifTB);


        /* Niveau
        niveauLabel = new JLabel("Niveau") ;
        niveauLabel.setHorizontalAlignment(0);
        niveauLabel.setPreferredSize(dimensionLabel);
        niveauLabel.setFont(normalFont);
        champsPanel.add(niveauLabel);
        
        allStringNiveau = new String[alNiveauBD.size()] ;

        for (int i = 0; i < alNiveauBD.size(); i++) {
            allStringNiveau[i] = alNiveauBD.get(i).getIntitule();
        }

        comboBoxNiveau = new JComboBox<String>(allStringNiveau) ;
        comboBoxNiveau.setPreferredSize(new Dimension(280,30) );
        champsPanel.add(comboBoxNiveau);
*/
        this.setLocationRelativeTo(this.getParent());
		
        changeEtatSaisie ();
    }

    private void changeEtatSaisie() {
		switch (modeActuel) {
			case CREATION :
				idText.setEnabled(false); 
			    nomText.setEnabled(true); 
			    prenomText.setEnabled(true); 
			    entreprise.setEnabled(true); 
			    email.setEnabled(true); 
			    tel.setEnabled(true); 
			    estActifTB.setEnabled(true);
			   // comboBoxNiveau.setEnabled(true); 

			    
			    titreLabel.setText("Cr�er Client�");

			    enregistrerBouton.setText("Enregister");
		        annulerBouton.setText("Annuler");
			break;
			case MODIFICATION:
				idText.setEnabled(false); 
			    nomText.setEnabled(true); 
			    prenomText.setEnabled(true); 
			    entreprise.setEnabled(true); 
			    email.setEnabled(true); 
			    tel.setEnabled(true); 
			    estActifTB.setEnabled(true);
			   // comboBoxNiveau.setEnabled(true); 
			    
			    titreLabel.setText("Modifier Client");

			    enregistrerBouton.setText("Modifier");
		        annulerBouton.setText("Annuler");
				break;
			case VISUALISATION:
				idText.setEnabled(false); 
			    nomText.setEnabled(false); 
			    prenomText.setEnabled(false); 
			    entreprise.setEnabled(false); 
			    email.setEnabled(false); 
			    tel.setEnabled(false); 
			    estActifTB.setEnabled(false); 
			   // comboBoxNiveau.setEnabled(false); 
			  
			    
			    titreLabel.setText("Voir Client");

			    enregistrerBouton.setText("");
			    enregistrerBouton.setEnabled(false);
		        annulerBouton.setText("Retour");
				break;
		}
		
        if (modeActuel != ClientEditor.ModeEdition.CREATION) {
        	// on remplit les champs 
	        idText.setText(Integer.toString(clientEdite.getId()));
	        nomText.setText(clientEdite.getNom());
	        prenomText.setText(clientEdite.getPrenom());
	        entreprise.setText(clientEdite.getEntreprise());
	        email.setText(clientEdite.getEmail());
	        tel.setText(clientEdite.getNumero());
		    estActifTB.setSelected ( (clientEdite.getEstActif() == Client.EST_ACTIF) );
        }
    }
	        //comboBoxNiveau.setSelectedIndex(niveauValueToIndex(employeEdite.getIdNiveau()));


    
    /*private int niveauValueToIndex (int idNiveau) {
    	for (int i=0; i<alNiveauBD.size(); i++) {
    		if (alNiveauBD.get(i).getIdNiveau() == idNiveau) {
    			return i;
    		}
    	}
    	return -1; // Fin anormale
    }
    */
    /**
     * Genere l'employe avec la valeurs des champs remplis
     * @return un employe
     */
    private Client generateClient(){
        
        int estActifE;
        estActifE = (estActifTB.isSelected() ? Client.EST_ACTIF : Client.EST_INACTIF);
        // On récupere tous les elements pour créer l'employé
        Client client ;
        if (modeActuel == ClientEditor.ModeEdition.CREATION){
            client = new Client( -1 , nomText.getText().trim() , prenomText.getText().trim() , entreprise.getText().trim() , email.getText().trim(),tel.getText().trim(), estActifE) ;
        }else {
            client = new Client( Integer.parseInt(idText.getText()) , nomText.getText() , prenomText.getText() ,entreprise.getText() , email.getText(),tel.getText(), estActifE) ;
        }

        return client ;
    }

    private void actionOK() {
        if (verifChamps()){
        	 this.clientResult = generateClient() ;
            this.setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "Veuillez saisir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    /**
     * Vérifier si tous les champs ont été saisis
     * @return vrai ou faux
     */
    private boolean verifChamps() {
        if (nomText.getText().trim().isEmpty() || prenomText.getText().trim().isEmpty() || entreprise.getText().trim().isEmpty() || email.getText().trim().isEmpty() || tel.getText().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    private void actionAnnuler() {
    	this.clientResult = null;
        this.setVisible(false);

    }
}
