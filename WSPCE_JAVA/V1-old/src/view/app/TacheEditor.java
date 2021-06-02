package view.app;

import model.data.*;
import model.orm.AccessClient;
import model.orm.AccessCompetence;
import model.orm.AccessEmploye;
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
public class TacheEditor extends JDialog {

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
    private JLabel dateFinLabel ;
    private JLabel dateDebutLabel;
    private JLabel dureeEstimeeLabel ;
    private JLabel dureeReelLabel ;
    private JLabel estActifLabel;
    private JLabel clientLabel;
    private JLabel competenceLabel ;
    private JLabel niveauLabel ;

    // Zones de saisie
    private JTextField idText ;
    private JTextField nomText ;
    private JTextField descriptionText ;
    private JTextField dateFinText ;
    private JTextField dateDebutText;
    private JTextField dureeEstimeeText ;
    private JTextField dureeReelText ;

    private JCheckBox estActifTB;


    private JComboBox<String> comboBoxClient ;
    private JComboBox<String> comboBoxCompetence ;
    private JComboBox<String> comboBoxNiveau ;

    // Liste de valeurs des ComboBox
    private String[] allStringComp ;
    private String[] allStringNiveau ;

    // Liste de valeurs des ComboBox
    private String[] allStringClient ;

    // Acces en BD
    private AccessClient ac = new AccessClient();
    private AccessEmploye ap = new AccessEmploye();

    // données en BD
    private ArrayList<Client> alClientBD;
    private ArrayList<Employe> BDalEmployeBD;
    private ArrayList<Competence> alCompetenceBD;
    private ArrayList<Niveau> alNiveauBD;

    // Employe qui utilise l'application
    private Employe employueUtilisateur;

    // Employé modifié ou visualisé
    private Tache tacheEdite;

    // Employé résultat (saisi ou modifié), null si annulation
    private Tache tacheResult;

    /**
     * Ouverture de la boite de dialogue d'édition d'un employé
     *
     * @param owner   fenêtre  mère de la boite de dialogue
     * @param employeUtilisateur Employ" connecté à l'application
     * @param tacheEdite  Objet de type Employé à éditer (éventuellement null en création).
     * @param mode    Mode d'ouverture (CREATION, MODIFICATION, VISUALISATION)
     *
     * @return un objet Employe si l'action est validée / null sinon
     */
    public static Tache showTacheEditor(Window owner, Employe employeUtilisateur, Tache tacheEdite, TacheEditor.ModeEdition mode) {

        if (mode == TacheEditor.ModeEdition.CREATION) {
            tacheEdite = new Tache();
        } else {
            tacheEdite = new Tache(tacheEdite);
        }

        TacheEditor dial = new TacheEditor(tacheEdite, employeUtilisateur, owner, mode);
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
    private TacheEditor(Tache pfTacheEdite, Employe pfEmployeUtilisateur, Window owner, TacheEditor.ModeEdition pfMode) {

        super(owner);
        this.employueUtilisateur = pfEmployeUtilisateur ;
        this.tacheEdite = pfTacheEdite;
        this.tacheResult = null;
        this.modeActuel = pfMode;

        try {
            alClientBD = ac.getClient("") ;
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
                dateFinText.setEnabled(true);
                dureeEstimeeText.setEnabled(true);
                dureeReelText.setEnabled(true);
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
                dateFinText.setEnabled(false);
                dureeEstimeeText.setEnabled(false);
                dureeReelText.setEnabled(false);
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
                dateFinText.setEnabled(false);
                dureeReelText.setEnabled(false);
                dureeEstimeeText.setEnabled(false);
                estActifTB.setEnabled(true);


                titreLabel.setText("Rendre actif");

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
            dureeEstimeeText.setText(tacheEdite.getDureeEstimee());


            dureeEstimeeText.setText(tacheEdite.getDateFinEstimee().toString());
            dureeReelText.setText(tacheEdite.getDateFinReel().toString());
            estActifTB.setSelected ( (tacheEdite.getEstActif() == Employe.EST_ACTIF) );

            comboBoxClient.setSelectedIndex(clientValueToIndex(tacheEdite.getIdCli()));

        }
    }


    /**
     * Genere l'employe avec la valeurs des champs remplis
     * @return un employe
     * @throws ParseException
     * @throws NumberFormatException
     */
    private Tache generateTache() throws ParseException{
        // On génére le role de l'employe
        int estActifP;
        int indexCli = comboBoxClient.getSelectedIndex() ;
        int compId = alClientBD.get(indexCli).getId();
        estActifP = (estActifTB.isSelected() ? Projet.EST_ACTIF : Projet.EST_INACTIF);
        // On récupere tous les elements pour créer l'employé
        Tache tache ;
        if (modeActuel == TacheEditor.ModeEdition.CREATION){
            tache = new Projet( -1 , nomText.getText().trim() , descriptionText.getText().trim() ,stringToDate(dateFinText.getText()), stringToDate(dureeEstimeeText.getText()), stringToDate(dureeReelText.getText()), estActifP, compId) ;
        }else {
            tache = new Projet( Integer.parseInt(idText.getText()) , nomText.getText() , descriptionText.getText() , stringToDate(dateFinText.getText()), stringToDate(dureeEstimeeText.getText()), stringToDate(dureeReelText.getText()), estActifP, compId ) ;        }

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
