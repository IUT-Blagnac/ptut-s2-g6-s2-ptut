package view.app;

import model.data.*;
import model.orm.*;
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
 * Fenetre d'édition d'une tache : Create Update Delete
 */

@SuppressWarnings("serial")
public class TacheEditor extends JDialog {

    public enum ModeEdition {
        CREATION, MODIFICATION, VISUALISATION,
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
    private JLabel dateFinLabel ;
    private JLabel dateDebutLabel;
    private JLabel dureeEstimeeLabel ;
    private JLabel dureeReelLabel ;
    private JLabel competenceLabel ;
    private JLabel niveauLabel ;
    private JLabel employeLabel ;

    private int idProjet;
    // Zones de saisie
    private JTextField idText ;
    private JTextField nomText ;
    private JTextField descriptionText ;
    private JTextField dateFinText ;
    private JTextField dateDebutText;
    private JTextField dureeEstimeeText ;
    private JTextField dureeReelText ;


    private JComboBox<String> comboBoxCompetence ;
    private JComboBox<String> comboBoxNiveau ;
    private JComboBox<String> comboBoxEmploye ;

    // Liste de valeurs des ComboBox
    private String[] allStringComp ;
    private String[] allStringNiveau ;
    private String[] allStringEmploye ;

    // Acces en BD
    private AccessEmploye ae = new AccessEmploye();
    private AccessCompetence ac = new AccessCompetence();
    private AccessNiveau an = new AccessNiveau();

    // données en BD
    private ArrayList<Employe> alEmployeBD;
    private ArrayList<Competence> alCompetenceBD;
    private ArrayList<Niveau> alNiveauBD;

    // Employe qui utilise l'application
    @SuppressWarnings("unused")
	private Employe employueUtilisateur;

    // Tache modifié ou visualisé
    private Tache tacheEdite;

    // Tache résultat (saisi ou modifié), null si annulation
    private Tache tacheResult;

    /**
     * Ouverture de la boite de dialogue d'édition d'une tache
     *
     * @param owner   fenêtre  mère de la boite de dialogue
     * @param employeUtilisateur Employ" connecté à l'application
     * @param tacheEdite  Objet de type Employé à éditer (éventuellement null en création).
     * @param mode    Mode d'ouverture (CREATION, MODIFICATION, VISUALISATION)
     *
     * @return un objet Employe si l'action est validée / null sinon
     * @throws Exception 
     */
    public static Tache showTacheEditor(Window owner, Employe employeUtilisateur, Tache tacheEdite, TacheEditor.ModeEdition mode) throws Exception {
        int idProjet = 0;

        if (mode == TacheEditor.ModeEdition.CREATION) {
            tacheEdite = new Tache();
            idProjet = tacheEdite.getIdProjet();
        } else {
            tacheEdite = new Tache(tacheEdite);
            idProjet = tacheEdite.getIdProjet();
        }

        TacheEditor dial = new TacheEditor(tacheEdite, employeUtilisateur, owner, mode, idProjet);
        dial.tacheResult = null;
        dial.setModal(true);
        dial.setVisible(true);
        // le programme appelant est bloqué jusqu'au masquage de la JDialog.
        dial.dispose();
        return dial.tacheResult;
    }

    // =======================================================================
    // Les constructeurs de la classe sont privés
    // Pour créer un éditeur, Il faut utiliser la méthode statique
    // == > showEmployeEditor()
    // =======================================================================
    private TacheEditor(Tache pfTacheEdite, Employe pfEmployeUtilisateur, Window owner, TacheEditor.ModeEdition pfMode, int idProjet) throws Exception {

        super(owner);
        this.employueUtilisateur = pfEmployeUtilisateur ;
        this.tacheEdite = pfTacheEdite;
        this.tacheResult = null;
        this.modeActuel = pfMode;

        try {
			alCompetenceBD = ac.getAllCompetence() ;
	        alNiveauBD = an.getAllNiveaux() ;
            alEmployeBD = ae.getEmployes("");
        } catch (DatabaseConnexionException | DataAccessException e1) {
			new ExceptionDialog(this, e1);
			JOptionPane.showMessageDialog(this, 
				"Impossible de continuer !\nMise à jour annulée.", "ERREUR", JOptionPane.ERROR_MESSAGE);
			actionAnnuler();
		}


        setTitle("Gestion d'une tâche");
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

        // Date fin
        dateFinLabel = new JLabel("Date de fin") ;
        dateFinLabel.setHorizontalAlignment(0);
        dateFinLabel.setPreferredSize(dimensionLabel);
        dateFinLabel.setFont(normalFont);
        champsPanel.add(dateFinLabel);

        dateFinText = new JTextField();
        dateFinText.setPreferredSize(dimensionText);
        champsPanel.add(dateFinText);

        // Durée estimée
        dureeEstimeeLabel = new JLabel("Durée estimée") ;
        dureeEstimeeLabel.setHorizontalAlignment(0);
        dureeEstimeeLabel.setPreferredSize(dimensionLabel);
        dureeEstimeeLabel.setFont(normalFont);
        champsPanel.add(dureeEstimeeLabel);

        dureeEstimeeText = new JTextField();
        dureeEstimeeText.setPreferredSize(dimensionText);
        champsPanel.add(dureeEstimeeText);

        //Durée réelle
        dureeReelLabel = new JLabel("Durée réelle") ;
        dureeReelLabel.setHorizontalAlignment(0);
        dureeReelLabel.setPreferredSize(dimensionLabel);
        dureeReelLabel.setFont(normalFont);
        champsPanel.add(dureeReelLabel);

        dureeReelText = new JTextField();
        dureeReelText.setPreferredSize(dimensionText);
        champsPanel.add(dureeReelText);



        // Employe
        employeLabel = new JLabel("Client") ;
        employeLabel.setHorizontalAlignment(0);
        employeLabel.setPreferredSize(dimensionLabel);
        employeLabel.setFont(normalFont);
        champsPanel.add(employeLabel);

        allStringComp = new String[alEmployeBD.size()] ;

        for (int i = 0; i < alEmployeBD.size(); i++) {
            allStringEmploye[i] = alEmployeBD.get(i).getNom();
        }

        comboBoxEmploye = new JComboBox<String>(allStringEmploye) ;
        comboBoxEmploye.setPreferredSize(new Dimension(280,30) );
        champsPanel.add(comboBoxEmploye);


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

        this.setLocationRelativeTo(this.getParent());

        changeEtatSaisie ();
    }

    private void changeEtatSaisie() {
        switch (modeActuel) {
            case CREATION :
                idText.setEnabled(false);
                nomText.setEnabled(true);
                descriptionText.setEnabled(true);
                dateFinText.setEnabled(true);
                dureeEstimeeText.setEnabled(true);
                dureeReelText.setEnabled(true);
                comboBoxCompetence.setEnabled(true);
                comboBoxNiveau.setEnabled(false);
                comboBoxEmploye.setEnabled(false);

                titreLabel.setText("Creer Tache");

                enregistrerBouton.setText("Enregister");
                annulerBouton.setText("Annuler");
                break;
            case MODIFICATION:
                idText.setEnabled(false);
                nomText.setEnabled(true);
                descriptionText.setEnabled(true);
                dateFinText.setEnabled(true);
                dureeEstimeeText.setEnabled(true);
                dureeReelText.setEnabled(true);
                comboBoxCompetence.setEnabled(false);
                comboBoxNiveau.setEnabled(false);
                comboBoxEmploye.setEnabled(false);

                titreLabel.setText("Modifier Tache");

                enregistrerBouton.setText("Modifier");
                annulerBouton.setText("Annuler");
                break;
            case VISUALISATION:
                idText.setEnabled(false);
                nomText.setEnabled(false);
                descriptionText.setEnabled(false);
                dateFinText.setEnabled(false);
                dureeEstimeeText.setEnabled(false);
                dureeReelText.setEnabled(false);
                comboBoxCompetence.setEnabled(false);
                comboBoxNiveau.setEnabled(false);
                comboBoxEmploye.setEnabled(false);


                titreLabel.setText("Voir Tache");

                enregistrerBouton.setText("");
                enregistrerBouton.setEnabled(false);
                annulerBouton.setText("Retour");
                break;

        }

        if (modeActuel != TacheEditor.ModeEdition.CREATION) {
            // on remplit les champs
            idText.setText(Integer.toString(tacheEdite.getId()));
            nomText.setText(tacheEdite.getNom());
            descriptionText.setText(tacheEdite.getDescription());
            dateFinText.setText(tacheEdite.getDateFin().toString());
            dateDebutText.setText(tacheEdite.getDateDebut().toString());
            dureeEstimeeText.setText("" +tacheEdite.getDureeEstimee());
            dureeEstimeeText.setText("" +tacheEdite.getDureeReelle());
            

			comboBoxCompetence.setSelectedIndex(employeValueToIndex(tacheEdite.getIdEmploye()));
            comboBoxCompetence.setSelectedIndex(comptenceValueToIndex(tacheEdite.getIdCompetence()));
	        comboBoxNiveau.setSelectedIndex(niveauValueToIndex(tacheEdite.getIdNiveau()));
        }
    }


    /**
     * Genere la tache avec la valeurs des champs remplis
     * @return une tache
     * @throws ParseException
     * @throws NumberFormatException
     */
    private Tache generateTache() throws ParseException{
        // On génére le role de l'employe
        int indexEmp = comboBoxEmploye.getSelectedIndex() ;
        int empId = alEmployeBD.get(indexEmp).getId();
        int indexComp = comboBoxCompetence.getSelectedIndex() ;
        int compId = alCompetenceBD.get(indexComp).getIdCompetence(); 
        int indexNiv = comboBoxNiveau.getSelectedIndex() ;
        int nivId = alNiveauBD.get(indexNiv).getIdNiveau();
        // On récupere tous les elements pour créer l'employé


        Tache tache ;
        if (modeActuel == TacheEditor.ModeEdition.CREATION)
        {
            tache = new Tache(-1, nomText.getText().trim(), descriptionText.getText().trim(), stringToDate(dateDebutText.getText()), stringToDate(dateFinText.getText()), Integer.parseInt(dureeEstimeeText.getText()), Integer.parseInt(dureeReelText.getText()), idProjet, compId,nivId,empId) ;
        }
        else {
            tache = new Tache(Integer.parseInt(idText.getText()), nomText.getText().trim(), descriptionText.getText().trim(), stringToDate(dateDebutText.getText()), stringToDate(dateFinText.getText()),Integer.parseInt(dureeEstimeeText.getText()), Integer.parseInt(dureeReelText.getText()), idProjet, compId,nivId,empId) ;
        }
        return tache ;
    }

    private void actionOK() throws ParseException  {
        if (verifChamps()){
            this.tacheResult = generateTache() ;
            this.setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "Veuillez saisir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
public static Date stringToDate(String pfString){
	Date SQLdate = Date.valueOf(pfString) ;
	return SQLdate ;
}

    /**
     * Vérifier si tous les champs ont été saisis
     * @return vrai ou faux
     */
    private boolean verifChamps() {
        if (nomText.getText().trim().isEmpty() || descriptionText.getText().trim().isEmpty() || dateFinText.getText().trim().isEmpty())
        {
            return false;
        }
        return true;
    }

    private void actionAnnuler() {
        this.tacheResult = null;
        this.setVisible(false);

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

    private int employeValueToIndex (int idEmploye)
    {
        for (int i=0; i<alEmployeBD.size(); i++) {
            if (alEmployeBD.get(i).getId() == idEmploye) {
                return i;
            }
        }
        return -1; // Fin anormale
    }
}
