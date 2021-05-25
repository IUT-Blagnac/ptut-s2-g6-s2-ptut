package view;

import java.awt.EventQueue;

import view.app.LoginDialog;

/**
 * Main qui permet d'exécuter l'application
 * commentaire
 */
public class Main {
    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginDialog frame = new LoginDialog();
			        frame.setLocationRelativeTo(null);
			        frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
