package view.app;

import com.sun.security.ntlm.Client;
import model.data.Employe;
import model.orm.AccessEmploye;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.RowNotFoundOrTooManyRowsException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class GestionProjet extends JDialog
{

    // L'employé qui utilise l'application
    private Employe employeUtilisateur;

    private AccessEmploye ae = new AccessEmploye() ;


    //        clientButton.setEnabled(false);


    private DefaultListModel<Client> model = new DefaultListModel<Client>();


    private JButton createButton;
    private JButton voirButton;
    private JButton modifierButton;
    private JButton rechercherButton;
    private JButton retourButton;
    private JList<Employe> selectionClient;
    private JScrollPane scroll;
    private JTextField researchBar;


    public GestionProjet(Window owner, Employe employeU )
    {
        super(owner);
        this.employeUtilisateur = employeU;

        setModal(true);

        setTitle("Gestion des projets");
        setResizable(true);
        setSize(600,450);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        JPanel contentHead = new JPanel(new BorderLayout());
        JPanel contentButtons = new JPanel(new FlowLayout());
        JPanel contentList = new JPanel();


        createButton = new JButton("Créer");
        //createButton.addActionListener(e -> actionCreer());
        createButton.setBackground(new Color(104, 177, 255)) ;

        voirButton = new JButton("Voir");
        //voirButton.addActionListener(e -> actionVoir());
        voirButton.setBackground(new Color(104, 177, 255)) ;

        modifierButton = new JButton("Modifier");
        //modifierButton.addActionListener(e -> actionModifier());
        modifierButton.setBackground(new Color(104, 177, 255)) ;

        retourButton = new JButton("Retour");
        retourButton.addActionListener(e -> actionRetour());
        retourButton.setBackground(new Color(104, 177, 255)) ;

        createButton.setPreferredSize(new Dimension(200,40));
        voirButton.setPreferredSize(new Dimension(200,40));
        modifierButton.setPreferredSize(new Dimension(200,40));
        retourButton.setPreferredSize(new Dimension(200,40));

        contentButtons.add(createButton);
        contentButtons.add(voirButton);
        contentButtons.add(modifierButton);
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


        /*
        selectionEmploye = new JList<>(model);
        selectionEmploye.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg0) {
                verifierEtatComposants();
            }
        });

         */
        scroll = new JScrollPane(selectionClient);
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

        //rechercherButton.addActionListener(e -> actionRechercheEmployes());


        researchBar = new JTextField("");
        researchBar.getFont().deriveFont(Font.ITALIC);
        researchBar.setForeground(Color.gray);
        researchBar.setPreferredSize(new Dimension(400,24));
        //researchBar.addActionListener(e -> actionResearchBar());

        contentHead.add(titre, BorderLayout.NORTH);
        contentHead.add(rechercherButton, BorderLayout.WEST);
        contentHead.add(researchBar, BorderLayout.EAST);
        contentHead.setBorder(BorderFactory.createEmptyBorder(0,35,0,25));

        contentPane.add(contentHead, BorderLayout.NORTH);
        contentPane.add(contentButtons, BorderLayout.EAST);
        contentPane.add(contentList, BorderLayout.WEST);

        add(contentPane);

        this.setLocationRelativeTo(this.getParent());

        //actionRechercheEmployes();
        //verifierEtatComposants();
    }

    private void actionRetour() { this.setVisible(false); }
}
