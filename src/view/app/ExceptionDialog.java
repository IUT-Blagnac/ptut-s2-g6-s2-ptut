package view.app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import model.orm.exception.ApplicationException;

@SuppressWarnings("serial")
public class ExceptionDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtException;
	private JTextField txtTable;
	private JTextField txtOperation;
	private JLabel lblMessage;
	private JTextArea txtDetails;
	private JLabel lblOperation;
	private JLabel lblException;
	private JLabel lblTable;
	private JPanel panelLabels;
	private JPanel panelTxts;

	/**
	 * Les constructeurs paramétrés avec une exception initialisent la boite de dialogue de manièe modale et l'affiche immédiatement
	 */
	public ExceptionDialog(Window owner, ApplicationException ae) {
		this(owner);
		this.getLblMessage().setText(ae.getMessage());
		this.getTxtTable().setText(ae.getTableName().toString());
		this.getTxtOperation().setText(ae.getOrder().toString());
		this.getTxtException().setText(ae.getClass().getName());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ae.printStackTrace(pw);
		this.getTxtDetails().setText(sw.toString());
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	public ExceptionDialog(Window owner, SQLException sqle) {
		this(owner);
		this.getLblMessage().setText(sqle.getMessage());
		getPanelLabels().setPreferredSize(new Dimension(160, 40));
		getPanelLabels().remove(this.getLblTable());
		getPanelLabels().remove(this.getLblOperation());
		getPanelTxts().remove(this.getTxtTable());
		getPanelTxts().remove(this.getTxtOperation());
		this.getTxtException().setText(sqle.getClass().getName());
		this.pack();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		sqle.printStackTrace(pw);
		this.getTxtDetails().setText(sw.toString());
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
	/**
	 * Le constructeur par défaut ne fait que l'initialisation des éléments swing de la boite de dialogue
	 */
	public ExceptionDialog() {
		this(null);
	}
	public ExceptionDialog(Window owner) {
		super(owner);
		setModal(true);
		setTitle("Op\u00E9ration impossible");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), new EmptyBorder(10, 10, 10, 10))));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), new EmptyBorder(10, 10, 10, 10))));
		contentPanel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		lblMessage = new JLabel("Message");
		lblMessage.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblMessage, BorderLayout.NORTH);

		panelLabels = new JPanel();
		panelLabels.setPreferredSize(new Dimension(160, 120));
		contentPanel.add(panelLabels, BorderLayout.WEST);
		panelLabels.setLayout(new GridLayout(0, 1, 0, 0));

		lblTable = new JLabel("Table");
		lblTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelLabels.add(lblTable);

		lblOperation = new JLabel("Op\u00E9ration");
		lblOperation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelLabels.add(lblOperation);


		lblException = new JLabel("Exception");
		lblException.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelLabels.add(lblException);

		panelTxts = new JPanel();
		panelTxts.setPreferredSize(new Dimension(640, 10));
		panelTxts.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPanel.add(panelTxts);
		panelTxts.setLayout(new GridLayout(0, 1, 0, 0));

		txtTable = new JTextField();
		txtTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtTable.setText("Table");
		panelTxts.add(txtTable);
		txtTable.setColumns(10);

		txtOperation = new JTextField();
		txtOperation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtOperation.setText("Operation");
		panelTxts.add(txtOperation);
		txtOperation.setColumns(10);

		txtException = new JTextField();
		txtException.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtException.setText("Exception");
		panelTxts.add(txtException);
		txtException.setColumns(10);

		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		JLabel lblDtailsTechniques = new JLabel("D\u00E9tails techniques");
		lblDtailsTechniques.setVerticalAlignment(SwingConstants.TOP);
		lblDtailsTechniques.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDtailsTechniques.setPreferredSize(new Dimension(160, 120));
		panel.add(lblDtailsTechniques, BorderLayout.WEST);
		txtDetails = new JTextArea();
		txtDetails.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txtDetails.setText("D\u00E9tails");
		JScrollPane scrollPane = new JScrollPane(txtDetails);
		scrollPane.setPreferredSize(new Dimension(640, 120));
		panel.add(scrollPane, BorderLayout.CENTER);


		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		okButton.addActionListener( e-> ExceptionDialog.this.dispose() );
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		this.pack();
	}

	public JLabel getLblMessage() {
		return lblMessage;
	}
	public JTextField getTxtTable() {
		return txtTable;
	}
	public JTextField getTxtOperation() {
		return txtOperation;
	}
	public JTextField getTxtException() {
		return txtException;
	}
	public JTextArea getTxtDetails() {
		return txtDetails;
	}
	public JLabel getLblOperation() {
		return lblOperation;
	}
	public JLabel getLblTable() {
		return lblTable;
	}
	public JPanel getPanelLabels() {
		return panelLabels;
	}
	public JPanel getPanelTxts() {
		return panelTxts;
	}
}
