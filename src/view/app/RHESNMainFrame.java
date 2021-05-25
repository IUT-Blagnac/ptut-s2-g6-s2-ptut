package view.app;

import model.data.Employe;
import model.data.Role;
import model.orm.LogToDatabase;
import model.orm.exception.DatabaseConnexionException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Frame d'accueil d'un employé connecté. Il peut voir les projets et les clients
 */

@SuppressWarnings("serial")
public class RHESNMainFrame extends JFrame {

    private JButton employeButton;
    private JButton projectButton;
    private JButton clientButton;
    private JButton deconnexionButton;
    private JLabel employeLabel;
    private Employe employeUtilisateur;
    
    private JPanel contentButton;
    private JPanel contentEmploye;
    
    /**
     * Constructeur
     * @param pfEmploye l'employe connecté juste avant
     */
    public RHESNMainFrame(Employe pfEmploye){
        setTitle("Accueil RHESN");
        setResizable(true);
        setSize(600,350);
        setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				actionQuitter();
			}
		});
        setResizable(false);

        this.employeUtilisateur = pfEmploye;

        JPanel contentPane = new JPanel(new BorderLayout());
        contentButton = new JPanel(new FlowLayout());
        contentButton.setPreferredSize(new Dimension(250, 200));
        contentButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));
        contentEmploye = new JPanel(new BorderLayout());
        contentEmploye.setPreferredSize(new Dimension(300, 250));
        contentEmploye.setBorder(BorderFactory.createEmptyBorder(10,30,60,0));

        JLabel accueilLabel = new JLabel("RHESN");
        accueilLabel.setPreferredSize(new Dimension(300,75));
        accueilLabel.setFont(new Font("Arial",Font.BOLD,36));
        accueilLabel.setHorizontalAlignment(SwingConstants.CENTER);


        projectButton = new JButton("Projet");
        projectButton.setPreferredSize(new Dimension(200,50));
        projectButton.setBackground(new Color(104, 177, 255)) ;
        projectButton.setEnabled(false);

        employeButton = new JButton("Employé");
        employeButton.setPreferredSize(new Dimension(200,50));
        employeButton.setBackground(new Color(104, 177, 255)) ;
        employeButton.addActionListener(e -> actionEmploye());

        clientButton = new JButton("Client");
        clientButton.setPreferredSize(new Dimension(200,50));
        clientButton.setBackground(new Color(104, 177, 255)) ;
        clientButton.setEnabled(false);

        String libRole = (employeUtilisateur.getIdRole() == Role.ID_ROLE_CHEF_PROJET ? Role.LIB_ROLE_CHEF_PROJET : Role.LIB_ROLE_EMPLOYE);
        employeLabel = new JLabel("<html><u>Employé connecté(e) :</u><br><br>Nom: <i>"+employeUtilisateur.getNom()+ "</i><br>Prénom: <i>"+ employeUtilisateur.getPrenom()+"</i><br>Role: <i>"+ libRole+"</i></html>\n");
        employeLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        employeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        deconnexionButton = new JButton("Deconnexion");
        deconnexionButton.setPreferredSize(new Dimension(120, 40));
        deconnexionButton.setBackground(new Color(104, 177, 255)) ;
        deconnexionButton.addActionListener(e -> actionDeconnexion());
        
        JPanel contentDecoButton = new JPanel(new FlowLayout());
        contentDecoButton.add(deconnexionButton);
        contentEmploye.add(employeLabel, BorderLayout.CENTER);
        contentEmploye.add(contentDecoButton, BorderLayout.SOUTH);

        contentPane.add(accueilLabel, BorderLayout.NORTH);
        contentPane.add(contentEmploye, BorderLayout.WEST);
        contentPane.add(contentButton, BorderLayout.EAST);

        verifierEtatComposants();
        
        add(contentPane);
        
        employeButton.requestFocusInWindow();
        
        setVisible(true);
    }

	private void verifierEtatComposants() {
        if(this.employeUtilisateur.getIdRole() == Role.ID_ROLE_CHEF_PROJET){
            contentButton.add(employeButton);
            contentButton.add(projectButton);
            contentButton.add(clientButton);
        } else if(this.employeUtilisateur.getIdRole() == Role.ID_ROLE_EMPLOYE){
            contentButton.add(projectButton);
            contentButton.add(clientButton);
        }
	}

    private void actionEmploye() {
    	GestionEmploye gestionEmploye= new GestionEmploye(this, employeUtilisateur);
    	gestionEmploye.setVisible(true);
    	gestionEmploye.dispose();
    }
    
    private void actionDeconnexion() {
    	employeUtilisateur = null;
    	LoginDialog root = new LoginDialog();
        root.setVisible(true);
        this.dispose();
	}
    
    private void actionQuitter() {
    	int input;
    	
    	input = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment quitter l'application", "Avertissement",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        // Si on valide la confirmation (bouton oui)
        if (input == JOptionPane.OK_OPTION){
			try {
				LogToDatabase.closeConnexion();
			} catch (DatabaseConnexionException e) {
				new ExceptionDialog(this, e);
			}
			this.dispose();
        }
	}
}

