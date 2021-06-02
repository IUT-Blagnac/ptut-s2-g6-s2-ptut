package view.app;

import model.data.*;
import model.orm.AccessClient;
import model.orm.AccessCompetence;
import model.orm.AccessNiveau;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Fenetre d'édition d'un employe : Create Update Delete  
 */

@SuppressWarnings("serial")
public class ProjetEditor extends JDialog {
	
	public enum ModeEdition {
		CREATION, MODIFICATION, VISUALISATION,ACTIF
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
    private JLabel descriptionLabel ;
    private JLabel dateDebutLabel ;
    private JLabel dateFinEstimeeLabel ;
    private JLabel dateFinReelLabel ;
    private JLabel estActifLabel;
    private JLabel clientLabel;

    // Zones de saisie
    private JTextField idText ;
    private JTextField nomText ;
    private JTextField descriptionText ;
    private JTextField dateDebutText ;
    private JTextField dateFinEstimeeText ;
    private JTextField dateFinReelText ;

    private JCheckBox estActifTB;
    
    private JComboBox<String> comboBoxClient ;
    
 // Liste de valeurs des ComboBox
    private String[] allStringClient ;
    
 // Acces en BD
    private AccessClient ac = new AccessClient();

    // données en BD
    private ArrayList<Client> alClientBD;

    // Employe qui utilise l'application
	private Employe employueUtilisateur;
	
	// Employé modifié ou visualisé
	private Projet projetEdite;
	
	// Employé résultat (saisi ou modifié), null si annulation
	private Projet projetResult;
	
	/**
	 * Ouverture de la boite de dialogue d'édition d'un employé
	 *
	 * @param owner   fenêtre  mère de la boite de dialogue
	 * @param employeUtilisateur Employ" connecté à l'application
	 * @param projetEdite  Objet de type Employé à éditer (éventuellement null en création).
	 * @param mode    Mode d'ouverture (CREATION, MODIFICATION, VISUALISATION)
	 * 
	 * @return un objet Employe si l'action est validée / null sinon
	 */
	public static Projet showProjetEditor(Window owner, Employe employeUtilisateur, Projet projetEdite, ProjetEditor.ModeEdition mode) {
		
		if (mode == ProjetEditor.ModeEdition.CREATION) {
			projetEdite = new Projet();
		} else {
			projetEdite = new Projet(projetEdite);
		}
		
		ProjetEditor dial = new ProjetEditor(projetEdite, employeUtilisateur, owner, mode);
        dial.projetResult = null;
		dial.setModal(true);
		dial.setVisible(true);
		// le programme appelant est bloqué jusqu'au masquage de la JDialog.
		dial.dispose();
		return dial.projetResult;
	}

	// =======================================================================
	// Les constructeurs de la classe sont privés
	// Pour créer un éditeur, Il faut utiliser la méthode statique 
	// == > showEmployeEditor() 
	// =======================================================================
	private ProjetEditor(Projet pfProjetEdite, Employe pfEmployeUtilisateur, Window owner, ProjetEditor.ModeEdition pfMode) {
        
        super(owner);
        this.employueUtilisateur = pfEmployeUtilisateur ;
        this.projetEdite = pfProjetEdite;
        this.projetResult = null;
        this.modeActuel = pfMode;
        
        try {
			alClientBD = ac.getClient("") ;
        } catch (DatabaseConnexionException | DataAccessException e1) {
			new ExceptionDialog(this, e1);
			JOptionPane.showMessageDialog(this, 
				"Impossible de continuer !\nMise à jour annulée.", "ERREUR", JOptionPane.ERROR_MESSAGE);
			actionAnnuler();
		} 

        
        setTitle("Gestion d'un Projet");
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
        enregistrerBouton.addActionListener(e -> {
			try {
				actionOK();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

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

        // Description
        descriptionLabel = new JLabel ("Description") ;
        descriptionLabel.setHorizontalAlignment(0);
        descriptionLabel.setPreferredSize(dimensionLabel);
        descriptionLabel.setFont(normalFont);
        champsPanel.add(descriptionLabel);

        descriptionText = new JTextField();
        descriptionText.setPreferredSize(dimensionText);
        champsPanel.add(descriptionText);

        // Date début
        dateDebutLabel = new JLabel("Date de début") ;
        dateDebutLabel.setHorizontalAlignment(0);
        dateDebutLabel.setPreferredSize(dimensionLabel);
        dateDebutLabel.setFont(normalFont);
        champsPanel.add(dateDebutLabel);

        dateDebutText = new JTextField();
        dateDebutText.setPreferredSize(dimensionText);
        champsPanel.add(dateDebutText);

        // Date de fin estimée
        dateFinEstimeeLabel = new JLabel("Date fin estimée") ;
        dateFinEstimeeLabel.setHorizontalAlignment(0);
        dateFinEstimeeLabel.setPreferredSize(dimensionLabel);
        dateFinEstimeeLabel.setFont(normalFont);
        champsPanel.add(dateFinEstimeeLabel);

        dateFinEstimeeText = new JTextField();
        dateFinEstimeeText.setPreferredSize(dimensionText);
        champsPanel.add(dateFinEstimeeText);

        // Date de fin réelle
        dateFinReelLabel = new JLabel("Date fin réelle") ;
        dateFinReelLabel.setHorizontalAlignment(0);
        dateFinReelLabel.setPreferredSize(dimensionLabel);
        dateFinReelLabel.setFont(normalFont);
        champsPanel.add(dateFinReelLabel);

        dateFinReelText = new JTextField();
        dateFinReelText.setPreferredSize(dimensionText);
        champsPanel.add(dateFinReelText);
        

        // estActif
        estActifLabel = new JLabel("estActif ?") ;
        estActifLabel.setHorizontalAlignment(0);
        estActifLabel.setPreferredSize(dimensionLabel);
        estActifLabel.setFont(normalFont);
        champsPanel.add(estActifLabel);

        estActifTB = new JCheckBox();
        estActifTB.setSelected(true);
        champsPanel.add(estActifTB);
        
     // Clients
        clientLabel = new JLabel("Client") ;
        clientLabel.setHorizontalAlignment(0);
        clientLabel.setPreferredSize(dimensionLabel);
        clientLabel.setFont(normalFont);
        champsPanel.add(clientLabel);

        allStringClient = new String[alClientBD.size()] ;

        for (int i = 0; i < alClientBD.size(); i++) {
        	allStringClient[i] = alClientBD.get(i).getNom();
        }

        comboBoxClient = new JComboBox<String>(allStringClient) ;
        comboBoxClient.setPreferredSize(new Dimension(280,30) );
        champsPanel.add(comboBoxClient);
        
        this.setLocationRelativeTo(this.getParent());
		
        changeEtatSaisie ();
    }

    private void changeEtatSaisie() {
		switch (modeActuel) {
			case CREATION :
				idText.setEnabled(false); 
			    nomText.setEnabled(true); 
			    descriptionText.setEnabled(true); 
			    dateDebutText.setEnabled(true); 
			    dateFinEstimeeText.setEnabled(true);
			    dateFinReelText.setEnabled(true);
			    estActifTB.setEnabled(true);
			    comboBoxClient.setEnabled(true);
			    
			    titreLabel.setText("Creer Projet");

			    enregistrerBouton.setText("Enregister");
		        annulerBouton.setText("Annuler");
			break;
			case MODIFICATION:
				idText.setEnabled(false); 
			    nomText.setEnabled(true); 
			    descriptionText.setEnabled(true); 
			    dateDebutText.setEnabled(true); 
			    dateFinEstimeeText.setEnabled(true);
			    dateFinReelText.setEnabled(true);
			    estActifTB.setEnabled(true);
			    comboBoxClient.setEnabled(true);
			    
			    titreLabel.setText("Modifier Projet");

			    enregistrerBouton.setText("Modifier");
		        annulerBouton.setText("Annuler");
				break;
			case VISUALISATION:
				idText.setEnabled(false); 
			    nomText.setEnabled(false); 
			    descriptionText.setEnabled(false); 
			    dateDebutText.setEnabled(false); 
			    dateFinEstimeeText.setEnabled(false);
			    dateFinReelText.setEnabled(false);
			    estActifTB.setEnabled(false);
			    comboBoxClient.setEnabled(false);
			    
			    
			    titreLabel.setText("Voir Employé");

			    enregistrerBouton.setText("");
			    enregistrerBouton.setEnabled(false);
		        annulerBouton.setText("Retour");
				break;
			 case ACTIF:
	                idText.setEnabled(false);
	                nomText.setEnabled(false);
	                descriptionText.setEnabled(false);
	                dateDebutText.setEnabled(false);
	                dateFinReelText.setEnabled(false);
	                dateFinEstimeeText.setEnabled(false);
	                estActifTB.setEnabled(true);


	                titreLabel.setText("Rendre actif");

	                enregistrerBouton.setText("");
	                enregistrerBouton.setEnabled(false);
	                annulerBouton.setText("Retour");
	                break;
		}
		
        if (modeActuel != ProjetEditor.ModeEdition.CREATION) {
        	// on remplit les champs 
	        idText.setText(Integer.toString(projetEdite.getId()));
	        nomText.setText(projetEdite.getNom());
	        descriptionText.setText(projetEdite.getDescription());
	        dateDebutText.setText(projetEdite.getDateDebut().toString());
	        dateFinEstimeeText.setText(projetEdite.getDateFinEstimee().toString());
	        dateFinReelText.setText(projetEdite.getDateFinReel().toString());
		    estActifTB.setSelected ( (projetEdite.getEstActif() == Employe.EST_ACTIF) );
		    
		    comboBoxClient.setSelectedIndex(clientValueToIndex(projetEdite.getIdCli()));

        }
	}

    
    /**
     * Genere l'employe avec la valeurs des champs remplis
     * @return un employe
     * @throws ParseException 
     * @throws NumberFormatException 
     */
    private Projet generateProjet() throws ParseException{
        // On génére le role de l'employe
    	int estActifP;
    	int indexCli = comboBoxClient.getSelectedIndex() ;
    	int compId = alClientBD.get(indexCli).getId(); 
        estActifP = (estActifTB.isSelected() ? Projet.EST_ACTIF : Projet.EST_INACTIF);
        // On récupere tous les elements pour créer l'employé
        Projet pjt ;
        if (modeActuel == ProjetEditor.ModeEdition.CREATION){
            pjt = new Projet( -1 , nomText.getText().trim() , descriptionText.getText().trim() ,stringToDate(dateDebutText.getText()), stringToDate(dateFinEstimeeText.getText()), stringToDate(dateFinReelText.getText()), estActifP, compId) ;
        }else {
        	pjt = new Projet( Integer.parseInt(idText.getText()) , nomText.getText() , descriptionText.getText() , stringToDate(dateDebutText.getText()), stringToDate(dateFinEstimeeText.getText()), stringToDate(dateFinReelText.getText()), estActifP, compId ) ;        }

        return pjt ;
    }

    private void actionOK() throws ParseException  {
    	 if (verifChamps()){
        	 this.projetResult = generateProjet() ;
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
    	if (nomText.getText().trim().isEmpty() || descriptionText.getText().trim().isEmpty() || dateDebutText.getText().trim().isEmpty())
        {
            return false;
        }
        return true;
    }

    private void actionAnnuler() {
    	this.projetResult = null;
        this.setVisible(false);

    }
    public Date stringToDate(String str) throws ParseException
    {
        String jour, mois, annee;
        Date date;
       if (str.length() == 8)
       {
           String[] dates = str.split("");
           jour = dates[0] + dates[1];
           mois = dates[2] + dates[3];
           annee = dates[4] + dates[5] + dates[6] + dates[7];

           date = new Date(Integer.parseInt(annee), Integer.parseInt(mois), Integer.parseInt(jour));
       }
       else
       {
           date = null;
       }

        return date;
    }
    private int clientValueToIndex (int idClient) {
    	for (int i=0; i<alClientBD.size(); i++) {
    		if (alClientBD.get(i).getId() == idClient) {
    			return i;
    		}
    	}
    	return -1; // Fin anormale
    }
}
