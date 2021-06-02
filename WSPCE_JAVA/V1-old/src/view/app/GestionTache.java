package view.app;


import model.data.Employe;

import model.data.Projet;
import model.orm.AccessProjet;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.RowNotFoundOrTooManyRowsException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.util.ArrayList;

/**
 * Fenetre avec tous les employes et les actions possibles sur eux
 */

@SuppressWarnings("serial")
public class GestionTache extends JDialog
{

    // L'employé qui utilise l'application
    private Employe employeUtilisateur;

    private Projet projetConcerne;

    private AccessProjet ap = new AccessProjet() ;

    private DefaultListModel<Projet> model = new DefaultListModel<Projet>();

    private JButton createButton;
    private JButton voirButton;
    private JButton modifierButton;
    private JButton rendreInactifButton;
    private JButton rechercherButton;
    private JButton tacheButton;
    private JButton retourButton;
    private JList<Projet> selectionProjet;
    private JScrollPane scroll;
    private JTextField researchBar;

    /**
     * Constructeur
     */
    public GestionTache(Window owner, Employe employeU, Projet projetC ){
        super(owner);
        this.employeUtilisateur = employeU;
        this.projetConcerne = projetC;

        setModal(true);

        setTitle("Gestion des tâches du projet" + projetC.getNom());
        setResizable(true);
        setSize(600,450);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        JPanel contentHead = new JPanel(new BorderLayout());
        JPanel contentButtons = new JPanel(new FlowLayout());
        JPanel contentList = new JPanel();


        createButton = new JButton("Créer");
        createButton.addActionListener(e -> actionCreer());
        createButton.setBackground(new Color(104, 177, 255)) ;

        rendreInactifButton = new JButton("Rendre actif/inactif");
        rendreInactifButton.addActionListener(e -> actionRendreInactif());
        rendreInactifButton.setBackground(new Color(104, 177, 255)) ;

        voirButton = new JButton("Voir");
        voirButton.addActionListener(e -> actionVoir());
        voirButton.setBackground(new Color(104, 177, 255)) ;

        modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(e -> actionModifier());
        modifierButton.setBackground(new Color(104, 177, 255)) ;

        retourButton = new JButton("Retour");
        retourButton.addActionListener(e -> actionRetour());
        retourButton.setBackground(new Color(104, 177, 255)) ;

        tacheButton = new JButton("Tâches");
        tacheButton.addActionListener(e -> actionTaches());
        tacheButton.setBackground(new Color(104, 177, 255)) ;

        createButton.setPreferredSize(new Dimension(200,40));
        voirButton.setPreferredSize(new Dimension(200,40));
        modifierButton.setPreferredSize(new Dimension(200,40));
        retourButton.setPreferredSize(new Dimension(200,40));
        rendreInactifButton.setPreferredSize(new Dimension(200,40));
        tacheButton.setPreferredSize(new Dimension(200,40));



        contentButtons.add(createButton);
        contentButtons.add(voirButton);
        contentButtons.add(modifierButton);
        contentButtons.add(rendreInactifButton);
        contentButtons.add(tacheButton);

        JLabel espace = new JLabel();
        espace.setPreferredSize(new Dimension(200,20));
        contentButtons.add(espace);
        contentButtons.add(retourButton);
        espace = new JLabel();
        espace.setPreferredSize(new Dimension(200,20));
        contentButtons.add(espace);
        contentButtons.setPreferredSize(new Dimension(250,300));
        contentButtons.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));

        model = new DefaultListModel<>();

        selectionProjet = new JList<>(model);
        selectionProjet.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg0) {
                verifierEtatComposants();
            }
        });
        scroll = new JScrollPane(selectionProjet);
        scroll.setPreferredSize(new Dimension(270, 270));
        scroll.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        contentList.add(scroll);
        scroll.setBorder(BorderFactory.createTitledBorder("Liste des projets"));
        contentList.setBorder(BorderFactory.createEmptyBorder(25,30,0,0));

        JLabel titre = new JLabel("Gestion des projets");
        titre.setFont(new Font("Arial",Font.BOLD,22));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        rechercherButton = new JButton("Rechercher");

        rechercherButton.addActionListener(e -> actionRechercherProjets());


        researchBar = new JTextField("");
        researchBar.getFont().deriveFont(Font.ITALIC);
        researchBar.setForeground(Color.gray);
        researchBar.setPreferredSize(new Dimension(400,24));
        researchBar.addActionListener(e -> actionResearchBar());

        contentHead.add(titre, BorderLayout.NORTH);
        contentHead.add(rechercherButton, BorderLayout.WEST);
        contentHead.add(researchBar, BorderLayout.EAST);
        contentHead.setBorder(BorderFactory.createEmptyBorder(0,35,0,25));

        contentPane.add(contentHead, BorderLayout.NORTH);
        contentPane.add(contentButtons, BorderLayout.EAST);
        contentPane.add(contentList, BorderLayout.WEST);

        add(contentPane);

        this.setLocationRelativeTo(this.getParent());

        actionRechercherProjets();
        verifierEtatComposants();
    }


    /**
     * Permet de vrifier si l'utilisateur a saisi un projet avant de cliquer sur les boutons
     */

    private void verifierEtatComposants(){
        if (selectionProjet.getSelectedIndex()<0) {
            voirButton.setEnabled(false);
            modifierButton.setEnabled(false);
        } else {
            voirButton.setEnabled(true);
            modifierButton.setEnabled(true);
        }
    }

    private void actionRechercherProjets() {

        String debutNom = this.researchBar.getText();

        ArrayList<Projet> listeProjets = new ArrayList<>();

        try {
            listeProjets = ap.getProjet(debutNom);
        } catch (DatabaseConnexionException e) {
            new ExceptionDialog(this, e);
            dispose();
        } catch (DataAccessException e) {
            new ExceptionDialog(this, e);
        } catch (RowNotFoundOrTooManyRowsException e) {
            new ExceptionDialog(this, e);
        }

        model.clear() ;
        for (Projet projet : listeProjets) {
            model.addElement(projet);
        }

        if (model.size() > 0) {
            selectionProjet.ensureIndexIsVisible(0);
        }
        selectionProjet.setSelectedIndex(-1);
        verifierEtatComposants();
    }

    private void actionResearchBar() {
        this.actionRechercherProjets();
    }

    private void actionRetour() {
        this.setVisible(false);
    }

    private void actionVoir() {
        Projet projetEdite = model.get(selectionProjet.getSelectedIndex());
        ProjetEditor.showProjetEditor(this,
                employeUtilisateur, projetEdite,
                ProjetEditor.ModeEdition.VISUALISATION);
    }


    // mettre en commentaire à partir d'ici si problème
    private void actionModifier() {
        Projet projetEdite = model.get(selectionProjet.getSelectedIndex());
        Projet result ;
        result = ProjetEditor.showProjetEditor(this,
                employeUtilisateur, projetEdite,
                ProjetEditor.ModeEdition.MODIFICATION);

        if (result != null) { // modif validée
            try {
                ap.updateProjet(result);
            } catch (RowNotFoundOrTooManyRowsException e) {
                new ExceptionDialog(this, e);
            } catch (DataAccessException e) {
                new ExceptionDialog(this, e);
            } catch (DatabaseConnexionException e) {
                new ExceptionDialog(this, e);
                this.dispose();;
            }

            actionRechercherProjets ();
        }
    }






    private void actionCreer()
    {
        Projet result ;
        result = ProjetEditor.showProjetEditor(this,
                employeUtilisateur, null,
                ProjetEditor.ModeEdition.CREATION);





        if (result != null) { // saisie validée
            try {
                ap.insertProjet(result);
            } catch (RowNotFoundOrTooManyRowsException e) {
                new ExceptionDialog(this, e);
            } catch (DataAccessException e) {
                new ExceptionDialog(this, e);
            } catch (DatabaseConnexionException e) {
                new ExceptionDialog(this, e);
                this.dispose();;
            }

            actionRechercherProjets ();
        }


    }



    private void actionRendreInactif()
    {
        Projet projetEdite = model.get(selectionProjet.getSelectedIndex());
        ProjetEditor.showProjetEditor(this,
                employeUtilisateur, projetEdite,
                ProjetEditor.ModeEdition.MODIFICATION);
    }


    private void actionTaches()
    {

    }

}
