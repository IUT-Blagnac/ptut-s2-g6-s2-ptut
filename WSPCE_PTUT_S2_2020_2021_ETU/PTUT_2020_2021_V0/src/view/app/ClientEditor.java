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
    private JLabel emailLabel ;
    private JLabel numeroLabel ;
    private JLabel estActifLabel;
    private JLabel competenceLabel ;
    private JLabel niveauLabel ;

    // Zones de saisie
    private JTextField idText ;
    private JTextField nomText ;
    private JTextField prenomText ;
    private JTextField entrepriseText ;
    private JTextField emailText ;
    private JTextField numeroText ;

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
    private Client employeEdite;

    // Employé résultat (saisi ou modifié), null si annulation
    private Client clientResult;
    private String employeResultMDP;

    /**
     * Ouverture de la boite de dialogue d'édition d'un employé
     *
     * @param owner   fenêtre  mère de la boite de dialogue
     * @param employeUtilisateur Employ" connecté à l'application
     * @param clientEdite  Objet de type Employé à éditer (éventuellement null en création).
     * @param mode    Mode d'ouverture (CREATION, MODIFICATION, VISUALISATION)
     *
     * @return un objet Client si l'action est validée / null sinon
     */
    public static Client showClientEditor(Window owner, Employe employeUtilisateur, Client clientEdite, ClientEditor.ModeEdition mode)
    {

        if (mode == ClientEditor.ModeEdition.CREATION)
        {
            clientEdite = new Client();
        }
        else
        {
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
    ClientEditor(Client pfEmployeEdite, Employe pfEmployeUtilisateur, Window owner, ClientEditor.ModeEdition pfMode) {

        super(owner);
        this.employueUtilisateur = pfEmployeUtilisateur ;
        this.employeEdite = pfEmployeEdite;
        this.clientResult = null;
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

        // Entreprise
        entrepriseLabel = new JLabel("Entreprise") ;
        entrepriseLabel.setHorizontalAlignment(0);
        entrepriseLabel.setPreferredSize(dimensionLabel);
        entrepriseLabel.setFont(normalFont);
        champsPanel.add(entrepriseLabel);

        entrepriseText = new JTextField();
        entrepriseText.setPreferredSize(dimensionText);
        champsPanel.add(entrepriseText);

        // Email
        emailLabel = new JLabel("Email") ;
        emailLabel.setHorizontalAlignment(0);
        emailLabel.setPreferredSize(dimensionLabel);
        emailLabel.setFont(normalFont);
        champsPanel.add(entrepriseLabel);

        emailText = new JTextField();
        emailText.setPreferredSize(dimensionText);
        champsPanel.add(emailText);

        // Numéro
        numeroLabel = new JLabel("Email") ;
        numeroLabel.setHorizontalAlignment(0);
        numeroLabel.setPreferredSize(dimensionLabel);
        numeroLabel.setFont(normalFont);
        champsPanel.add(numeroLabel);

        numeroText = new JTextField();
        numeroText.setPreferredSize(dimensionText);
        champsPanel.add(numeroText);


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
                prenomText.setEnabled(true);
                entrepriseText.setEnabled(true);
                estActifTB.setEnabled(true);
                numeroText.setEnabled(true);
                emailText.setEnabled(true);
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
                entrepriseText.setEnabled(true);
                numeroText.setEnabled(true);
                emailText.setEnabled(true);
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
                entrepriseText.setEnabled(false);
                numeroText.setEnabled(false);
                emailText.setEnabled(false);
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

        if (modeActuel != ClientEditor.ModeEdition.CREATION) {
            // on remplit les champs
            idText.setText(Integer.toString(employeEdite.getId()));
            nomText.setText(employeEdite.getNom());
            prenomText.setText(employeEdite.getPrenom());
            estActifTB.setSelected ( (employeEdite.getEstActif() == Employe.EST_ACTIF) );

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
    private Client generateEmploye(){
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
        Client emp ;
        if (modeActuel == ClientEditor.ModeEdition.CREATION){
            emp = new Client( -1 , nomText.getText().trim() , prenomText.getText().trim() , entrepriseText.getText().trim() , emailText.getText().trim(), numeroText.getText().trim(), 1 ) ;
        }else {
            emp = new Client( Integer.parseInt(idText.getText()) , nomText.getText() , prenomText.getText() , entrepriseText.getText() ,emailText.getText().trim(), numeroText.getText().trim(), 1 ) ;
        }

       // public Client(int idC, String nomC, String prenomC, String entrepriseC, String emailC, String numeroC, int estActifC)
       //public Employe(int idE, String nomE, String prenomE, String loginE, String mdpE, int estActifE, int idRoleE, int idCompetenceE, int idNiveauE){

        return emp ;
    }

    private void actionOK() {
        if (verifChamps()){
            if (modeActuel == ClientEditor.ModeEdition.CREATION) {
                employeResultMDP = PasswordEditor.showPassWordEditor(this, entrepriseText.getText().trim(), PasswordEditor.ModeEdition.CREATION);
                if (employeResultMDP == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un mot de pase pour créer un employé", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return ;
                }
            }
            this.clientResult = generateEmploye() ;
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
        if (nomText.getText().trim().isEmpty() || prenomText.getText().trim().isEmpty() || entrepriseText.getText().trim().isEmpty() || numeroText.getText().trim().isEmpty() || emailText.getText().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    private void actionAnnuler() {
        this.clientResult = null;
        this.setVisible(false);

    }
}
