package view.app;

import model.data.*;
import model.orm.AccessCompetence;
import model.orm.AccessNiveau;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.util.ArrayList;

/**
 * Fenetre d'édition d'un employe : Create Update Delete  
 */

@SuppressWarnings("serial")
public class EmployeEditor extends JDialog {
	
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
    private JLabel loginLabel ;
    private JLabel estActifLabel;
    private JLabel competenceLabel ;
    private JLabel niveauLabel ;

    // Zones de saisie
    private JTextField idText ;
    private JTextField nomText ;
    private JTextField prenomText ;
    private JTextField loginText ;
    
    private JCheckBox estActifTB;

    private JComboBox<String> comboBoxCompetence ;
    private JComboBox<String> comboBoxNiveau ;
    
    // Liste de valeurs des ComboBox
    private String[] allStringComp ;
    private String[] allStringNiveau ;

    // Radio bouton (pour le role)
    private ButtonGroup boutonGroup ;
    private JRadioButton chefBouton ;
    private JRadioButton employeBouton ;

    // Acces en BD
    private AccessCompetence ac = new AccessCompetence();
    private AccessNiveau an = new AccessNiveau();

    // données en BD
    private ArrayList<Competence> alCompetenceBD;
    private ArrayList<Niveau> alNiveauBD;

    // Employe qui utilise l'application
	private Employe employueUtilisateur;
	
	// Employé modifié ou visualisé
	private Employe employeEdite;
	
	// Employé résultat (saisi ou modifié), null si annulation
	private Employe employeResult;
	private String employeResultMDP;
	
	/**
	 * Ouverture de la boite de dialogue d'édition d'un employé
	 *
	 * @param owner   fenêtre  mère de la boite de dialogue
	 * @param employeUtilisateur Employ" connecté à l'application
	 * @param employeEdite  Objet de type Employé à éditer (éventuellement null en création).
	 * @param mode    Mode d'ouverture (CREATION, MODIFICATION, VISUALISATION)
	 * 
	 * @return un objet Employe si l'action est validée / null sinon
	 */
	public static Employe showEmployeEditor(Window owner, Employe employeUtilisateur, Employe employeEdite, EmployeEditor.ModeEdition mode) {
		
		if (mode == EmployeEditor.ModeEdition.CREATION) {
			employeEdite = new Employe();
		} else {
			employeEdite = new Employe(employeEdite);
		}
		
		EmployeEditor dial = new EmployeEditor(employeEdite, employeUtilisateur, owner, mode);
        dial.employeResult = null;
		dial.setModal(true);
		dial.setVisible(true);
		// le programme appelant est bloqué jusqu'au masquage de la JDialog.
		dial.dispose();
		return dial.employeResult;
	}

	// =======================================================================
	// Les constructeurs de la classe sont privés
	// Pour créer un éditeur, Il faut utiliser la méthode statique 
	// == > showEmployeEditor() 
	// =======================================================================
	private EmployeEditor(Employe pfEmployeEdite, Employe pfEmployeUtilisateur, Window owner, EmployeEditor.ModeEdition pfMode) {
        
        super(owner);
        this.employueUtilisateur = pfEmployeUtilisateur ;
        this.employeEdite = pfEmployeEdite;
        this.employeResult = null;
        this.modeActuel = pfMode;
        
        try {
			alCompetenceBD = ac.getAllCompetence() ;
	        alNiveauBD = an.getAllNiveaux() ;
        } catch (DatabaseConnexionException | DataAccessException e1) {
			new ExceptionDialog(this, e1);
			JOptionPane.showMessageDialog(this, 
				"Impossible de continuer !\nMise à jour annulée.", "ERREUR", JOptionPane.ERROR_MESSAGE);
			actionAnnuler();
		} 
        

        
        setTitle("Gestion d'un Employé");
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

        // Login
        loginLabel = new JLabel("Login") ;
        loginLabel.setHorizontalAlignment(0);
        loginLabel.setPreferredSize(dimensionLabel);
        loginLabel.setFont(normalFont);
        champsPanel.add(loginLabel);

        loginText = new JTextField();
        loginText.setPreferredSize(dimensionText);
        champsPanel.add(loginText);
        
        // estActif
        estActifLabel = new JLabel("estActif ?") ;
        estActifLabel.setHorizontalAlignment(0);
        estActifLabel.setPreferredSize(dimensionLabel);
        estActifLabel.setFont(normalFont);
        champsPanel.add(estActifLabel);

        estActifTB = new JCheckBox();
        estActifTB.setSelected(true);
        champsPanel.add(estActifTB);

        // Competences
        competenceLabel = new JLabel("Competence") ;
        competenceLabel.setHorizontalAlignment(0);
        competenceLabel.setPreferredSize(dimensionLabel);
        competenceLabel.setFont(normalFont);
        champsPanel.add(competenceLabel);

        allStringComp = new String[alCompetenceBD.size()] ;

        for (int i = 0; i < alCompetenceBD.size(); i++) {
            allStringComp[i] = alCompetenceBD.get(i).getNom();
        }

        comboBoxCompetence = new JComboBox<String>(allStringComp) ;
        comboBoxCompetence.setPreferredSize(new Dimension(280,30) );
        champsPanel.add(comboBoxCompetence);

        // Niveau
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

        // Radio boutons
        boutonGroup = new ButtonGroup();

        chefBouton = new JRadioButton("Chef de projet");
        chefBouton.setFont(normalFont);

        employeBouton = new JRadioButton("Employé");
        employeBouton.setSelected(true);
        employeBouton.setFont(normalFont);

        boutonGroup.add(chefBouton);
        boutonGroup.add(employeBouton);

        champsPanel.add(chefBouton);
        champsPanel.add(employeBouton);
        
        this.setLocationRelativeTo(this.getParent());
		
        changeEtatSaisie ();
    }

    private void changeEtatSaisie() {
		switch (modeActuel) {
			case CREATION :
				idText.setEnabled(false); 
			    nomText.setEnabled(true); 
			    prenomText.setEnabled(true); 
			    loginText.setEnabled(true); 
			    estActifTB.setEnabled(true);
			    comboBoxCompetence.setEnabled(true); 
			    comboBoxNiveau.setEnabled(true); 
			    chefBouton.setEnabled(true); 
			    employeBouton.setEnabled(true);
			    
			    titreLabel.setText("Créer Employé");

			    enregistrerBouton.setText("Enregister");
		        annulerBouton.setText("Annuler");
			break;
			case MODIFICATION:
				idText.setEnabled(false); 
			    nomText.setEnabled(true); 
			    prenomText.setEnabled(true); 
			    loginText.setEnabled(true); 
			    estActifTB.setEnabled(true);
			    comboBoxCompetence.setEnabled(true); 
			    comboBoxNiveau.setEnabled(true); 
			    chefBouton.setEnabled(true); 
			    employeBouton.setEnabled(true);
			    
			    titreLabel.setText("Modifier Employé");

			    enregistrerBouton.setText("Modifier");
		        annulerBouton.setText("Annuler");
				break;
			case VISUALISATION:
				idText.setEnabled(false); 
			    nomText.setEnabled(false); 
			    prenomText.setEnabled(false); 
			    loginText.setEnabled(false); 
			    estActifTB.setEnabled(false);
			    comboBoxCompetence.setEnabled(false); 
			    comboBoxNiveau.setEnabled(false); 
			    chefBouton.setEnabled(false); 
			    employeBouton.setEnabled(false);
			    
			    titreLabel.setText("Voir Employé");

			    enregistrerBouton.setText("");
			    enregistrerBouton.setEnabled(false);
		        annulerBouton.setText("Retour");
				break;
		}
		
        if (modeActuel != EmployeEditor.ModeEdition.CREATION) {
        	// on remplit les champs 
	        idText.setText(Integer.toString(employeEdite.getId()));
	        nomText.setText(employeEdite.getNom());
	        prenomText.setText(employeEdite.getPrenom());
	        loginText.setText(employeEdite.getLogin());
		    estActifTB.setSelected ( (employeEdite.getEstActif() == Employe.EST_ACTIF) );
		    
		    employeResultMDP = employeEdite.getMdp();
	
	        comboBoxCompetence.setSelectedIndex(comptenceValueToIndex(employeEdite.getIdCompetence()));
	        comboBoxNiveau.setSelectedIndex(niveauValueToIndex(employeEdite.getIdNiveau()));

		    // Si l'employe est chef de projet
	        if (employeEdite.getIdRole() == Role.ID_ROLE_CHEF_PROJET){
	            chefBouton.setSelected(true);
	        } else {
	            employeBouton.setSelected(true);
	        }
        }
	}

    private int comptenceValueToIndex (int idCompetence) {
    	for (int i=0; i<alCompetenceBD.size(); i++) {
    		if (alCompetenceBD.get(i).getIdCompetence() == idCompetence) {
    			return i;
    		}
    	}
    	return -1; // Fin anormale
    }
    
    private int niveauValueToIndex (int idNiveau) {
    	for (int i=0; i<alNiveauBD.size(); i++) {
    		if (alNiveauBD.get(i).getIdNiveau() == idNiveau) {
    			return i;
    		}
    	}
    	return -1; // Fin anormale
    }
    
    /**
     * Genere l'employe avec la valeurs des champs remplis
     * @return un employe
     */
    private Employe generateEmploye(){
        // On génére le role de l'employe
        int roleId ;
        if (chefBouton.isSelected()){
            roleId = Role.ID_ROLE_CHEF_PROJET ;
        } else {
            roleId = Role.ID_ROLE_EMPLOYE ;
        }

        int indexComp = comboBoxCompetence.getSelectedIndex() ;
        int compId = alCompetenceBD.get(indexComp).getIdCompetence(); 
        int indexNiv = comboBoxNiveau.getSelectedIndex() ;
        int nivId = alNiveauBD.get(indexNiv).getIdNiveau();
        int estActifE;
        estActifE = (estActifTB.isSelected() ? Employe.EST_ACTIF : Employe.EST_INACTIF);
        // On récupere tous les elements pour créer l'employé
        Employe emp ;
        if (modeActuel == EmployeEditor.ModeEdition.CREATION){
            emp = new Employe( -1 , nomText.getText().trim() , prenomText.getText().trim() , loginText.getText().trim() , employeResultMDP, estActifE, roleId, compId, nivId) ;
        }else {
            emp = new Employe( Integer.parseInt(idText.getText()) , nomText.getText() , prenomText.getText() , loginText.getText() , employeResultMDP, estActifE, roleId, compId, nivId) ;
        }

        return emp ;
    }

    private void actionOK() {
        if (verifChamps()){
        	if (modeActuel == EmployeEditor.ModeEdition.CREATION) {
        		employeResultMDP = PasswordEditor.showPassWordEditor(this, loginText.getText().trim(), PasswordEditor.ModeEdition.CREATION);
        		if (employeResultMDP == null) {
        			JOptionPane.showMessageDialog(this, "Veuillez saisir un mot de pase pour créer un employé", "Erreur", JOptionPane.ERROR_MESSAGE);
        			return ;
        		}
        	}
            this.employeResult = generateEmploye() ;
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
        if (nomText.getText().trim().isEmpty() || prenomText.getText().trim().isEmpty() || loginText.getText().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    private void actionAnnuler() {
    	this.employeResult = null;
        this.setVisible(false);

    }
}
