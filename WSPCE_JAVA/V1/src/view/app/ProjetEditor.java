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

//copié collé et modifié de EmployeEditor, pas encore fini

@SuppressWarnings("serial")
public class ProjetEditor extends JDialog {

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
    private JLabel descriptionLabel ;
    private JLabel dateDebutLabel ;
    private JLabel dateFinEstimeeLabel ;
    private JLabel dateFinReelLabel ;
    private JLabel estActifLabel;


    // Zones de saisie
    private JTextField idText ;
    private JTextField nomText ;
    private JTextField descriptionText ;
    private JTextField dateDebutText ;
    private JTextField dateFinEstimeeText ;
    private JTextField dateFinReelText ;

    private JCheckBox estActifTB;



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

    // Projet modifié ou visualisé
    private Projet projetEdite;

    // Projet résultat (saisi ou modifié), null si annulation
    private Projet projetResult;

    /**
     * Ouverture de la boite de dialogue d'édition d'un employé
     *
     * @param owner   fenêtre  mère de la boite de dialogue
     * @param employeUtilisateur Employ" connecté à l'application
     * @param projetEdite  Objet de type Employé à éditer (éventuellement null en création).
     * @param mode    Mode d'ouverture (CREATION, MODIFICATION, VISUALISATION)
     *
     * @return un objet Client si l'action est validée / null sinon
     */
    public static Projet showProjetEditor(Window owner, Employe employeUtilisateur, Projet projetEdite, ProjetEditor.ModeEdition mode)
    {

        if (mode == ProjetEditor.ModeEdition.CREATION)
        {
            projetEdite = new Projet();
        }
        else
        {
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
    ProjetEditor(Projet pfProjetEdite, Employe pfEmployeUtilisateur, Window owner, ProjetEditor.ModeEdition pfMode) {

        super(owner);
        this.employueUtilisateur = pfEmployeUtilisateur ;
        this.projetEdite = pfProjetEdite;
        this.projetResult = null;
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

        dateDebutText = new JTextField();
        dateDebutText.setPreferredSize(dimensionText);
        champsPanel.add(dateDebutText);

        // Date de fin réelle
        dateFinReelLabel = new JLabel("Date de début") ;
        dateFinReelLabel.setHorizontalAlignment(0);
        dateFinReelLabel.setPreferredSize(dimensionLabel);
        dateFinReelLabel.setFont(normalFont);
        champsPanel.add(dateFinReelLabel);

        dateDebutText = new JTextField();
        dateDebutText.setPreferredSize(dimensionText);
        champsPanel.add(dateDebutText);
        

        // estActif
        estActifLabel = new JLabel("estActif ?") ;
        estActifLabel.setHorizontalAlignment(0);
        estActifLabel.setPreferredSize(dimensionLabel);
        estActifLabel.setFont(normalFont);
        champsPanel.add(estActifLabel);

        estActifTB = new JCheckBox();
        estActifTB.setSelected(true);
        champsPanel.add(estActifTB);


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
                dateFinReelText.setEnabled(true);
                dateFinEstimeeText.setEnabled(true);
                estActifTB.setEnabled(true);
                chefBouton.setEnabled(true);
                employeBouton.setEnabled(true);

                titreLabel.setText("Créer Employé");

                enregistrerBouton.setText("Enregister");
                annulerBouton.setText("Annuler");
                break;
            case MODIFICATION:
                idText.setEnabled(false);
                nomText.setEnabled(true);
                descriptionText.setEnabled(true);
                dateDebutText.setEnabled(true);
                dateFinReelText.setEnabled(true);
                dateFinEstimeeText.setEnabled(true);
                estActifTB.setEnabled(true);
                chefBouton.setEnabled(true);
                employeBouton.setEnabled(true);

                titreLabel.setText("Modifier Employé");

                enregistrerBouton.setText("Modifier");
                annulerBouton.setText("Annuler");
                break;
            case VISUALISATION:
                idText.setEnabled(false);
                nomText.setEnabled(false);
                descriptionText.setEnabled(false);
                dateDebutText.setEnabled(false);
                dateFinReelText.setEnabled(false);
                dateFinEstimeeText.setEnabled(false);
                estActifTB.setEnabled(false);
                chefBouton.setEnabled(false);
                employeBouton.setEnabled(false);

                titreLabel.setText("Voir Projet");

                enregistrerBouton.setText("");
                enregistrerBouton.setEnabled(false);
                annulerBouton.setText("Retour");
                break;
        }

        if (modeActuel != ProjetEditor.ModeEdition.CREATION) {
            // on remplit les champs
            idText.setText(Integer.toString(projetEdite.getId()));
            nomText.setText(projetEdite.getNom());
            dateDebutText.setText(projetEdite.getDateDebut().toString());
            dateFinEstimeeText.setText(projetEdite.getDateFinEstimee().toString());
            dateFinReelText.setText(projetEdite.getDateFinReel().toString());
            estActifTB.setSelected ( (projetEdite.getEstActif() == Employe.EST_ACTIF) );

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
    private Projet generateProjet(){
        // On génére le role de l'employe
        int roleId ;
        if (chefBouton.isSelected()){
            roleId = Role.ID_ROLE_CHEF_PROJET ;
        } else {
            roleId = Role.ID_ROLE_EMPLOYE ;
        }

        int estActifE;
        estActifE = (estActifTB.isSelected() ? Employe.EST_ACTIF : Employe.EST_INACTIF);
        // On récupere tous les elements pour créer l'employé
        Projet projet ;
        if (modeActuel == ProjetEditor.ModeEdition.CREATION){
            projet = new Projet( -1 , nomText.getText().trim() , descriptionText.getText().trim() , null, null, null, 1 ) ;
        }else {
            projet = new Projet( Integer.parseInt(idText.getText()) , nomText.getText() , descriptionText.getText() , null, null, null, 1 ) ;
        }

        // public Client(int idC, String nomC, String prenomC, String entrepriseC, String emailC, String numeroC, int estActifC)
        //public Employe(int idE, String nomE, String prenomE, String loginE, String mdpE, int estActifE, int idRoleE, int idCompetenceE, int idNiveauE){

        return projet ;
    }

    private void actionOK() {
        if (verifChamps())
        {

            this.projetResult = generateProjet() ;
            this.setVisible(false);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Veuillez saisir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    /**
     * Vérifier si tous les champs ont été saisis
     * @return vrai ou faux
     */
    private boolean verifChamps() {
        if (nomText.getText().trim().isEmpty() || descriptionText.getText().trim().isEmpty() || dateDebutText.getText().trim().isEmpty() || dateDebutText.getText().trim().isEmpty() || dateFinEstimeeText.getText().trim().isEmpty() || dateFinReelText.getText().trim().isEmpty() || !verifDate())
        {
            return false;
        }
        return true;
    }

    private boolean verifDate()
    {

        Integer d1 = Integer.parseInt(dateDebutText.getText());
        Integer d2 = Integer.parseInt(dateFinEstimeeText.getText());
        Integer d3 = Integer.parseInt(dateFinReelText.getText());
        


        return true;
    }


    private void actionAnnuler() {
        this.projetResult = null;
        this.setVisible(false);

    }
}
