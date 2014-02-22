import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SponsorFrame extends JFrame implements ActionListener {
	private JButton b1, b2, b3;
	private JList list;
	private DefaultListModel listModel;
	private JLabel lab1, lab2;
	private Sponsorloop loop;
	private Loper loper;
	private JPanel labelPane, lowerPane, buttonPane;
	private MainFrame mf;

	public SponsorFrame(Loper loper) {
		this.loper = loper;
		setSize(200, 200);
		lab1 = new JLabel("Sponsors", JLabel.CENTER);
		lab2 = new JLabel();
		updateAantal();
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(5);
		b1 = new JButton("Nieuwe Sponsor");
		b2 = new JButton("Verwijder Sponsor");
		b3 = new JButton("Bekijk Criterium");
		JScrollPane listScrollPane = new JScrollPane(list);
		lowerPane = new JPanel();
		labelPane = new JPanel();
		buttonPane = new JPanel();
		lowerPane.setLayout(new BorderLayout());
		labelPane.setLayout(new BorderLayout());
		buttonPane.setLayout(new BorderLayout());

		//Wat er gebeurt bij veranderen van selectie in het menu
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (list.getSelectedValue() != null) {
						b2.setEnabled(true);
						b3.setEnabled(true);
					}
					if (list.getSelectedValue() == null) {
						b2.setEnabled(false);
						b3.setEnabled(false);
					}
				}
			}
		});

		add(lab1, BorderLayout.NORTH);
		labelPane.add(lab2, BorderLayout.NORTH);
		buttonPane.add(b3, BorderLayout.NORTH);
		buttonPane.add(b1, BorderLayout.CENTER);
		buttonPane.add(b2, BorderLayout.SOUTH);
		b2.setEnabled(false);
		b3.setEnabled(false);
		add(listScrollPane, BorderLayout.CENTER);
		add(lowerPane, BorderLayout.SOUTH);
		lowerPane.add(labelPane, BorderLayout.NORTH);
		lowerPane.add(buttonPane, BorderLayout.CENTER);

		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);

		for (Sponsor s : loper.getSponsors()) {
			listModel.addElement(s.getNaam());
		}

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(650, dim.height / 2 - this.getSize().height / 2);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b3) {
			if (list.getSelectedIndex() >= 0) {
				JOptionPane.showMessageDialog(null,
						loper.getSponsor((String) list.getSelectedValue())
								.getCriterium(), "Criterium",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

		if (e.getSource() == b1) {
			String naam = "";
			while (true) {
			while (true) {
				naam = JOptionPane.showInputDialog(null,
						"Voer sponsor naam in: ", "Naam invoeren",
						JOptionPane.PLAIN_MESSAGE);
				if (naam == null) {
					break;
				}
				if (naam.equals("") || naam.length() < 2) {
					JOptionPane.showMessageDialog(null,
							"Voer een correcte naam in.", "Fout",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					break;
				}
			}
			if (naam == null) {
				break;
			}
			
			String criterium = "";
			while (true) {
				criterium = JOptionPane.showInputDialog(null,
						"Voer criterium in: ", "Criterium invoeren",
						JOptionPane.PLAIN_MESSAGE);
				if (criterium == null) {
					break;
				}
				if (criterium.equals("") || criterium.length() < 2) {
					JOptionPane.showMessageDialog(null,
							"Voer een correct criterium in.", "Fout",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					break;
				}
			}
			if (criterium == null) {
				break;
			}
			String s = "";
			double bedrag = 0;
			while (true) {
				s = JOptionPane.showInputDialog(null, "Voer bedrag in: ",
						"Bedrag invoeren", JOptionPane.PLAIN_MESSAGE);
				if (s == null) {
					break;
				}
				
				if (s.equals("") || s.length() < 1 || !s.matches("[0-9.]+")
						|| s.matches("[.]+")) {
					JOptionPane
							.showMessageDialog(
									null,
									"Voer een correct bedrag in. \nTip: Gebruik een punt als scheidingsteken, geen komma",
									"Fout", JOptionPane.INFORMATION_MESSAGE);
				} else {
					bedrag = Double.parseDouble(s);
					break;
				}
			}
			if (s == null) {
				break;
			}

			listModel.addElement(naam);
			loper.voegSponsorToe(naam, criterium, bedrag);
			updateAantal();
			mf.updateAantal();
			break;
			}

		}
		if (e.getSource() == b2) {
			if (list.getSelectedIndex() >= 0) {
				String[] options = new String[2];
				options[0] = new String("Ja");
				options[1] = new String("Nee");
				int reply = JOptionPane.showOptionDialog(
						null,
						"Weet je zeker dat je sponsor '"
								+ (String) list.getSelectedValue()
								+ "' wilt verwijderen?", "Verwijderen?", 0,
						JOptionPane.INFORMATION_MESSAGE, null, options, null);
				if (reply == 0) {
					String naam = (String) list.getSelectedValue();
					int selectedIndex = list.getSelectedIndex();
					if (selectedIndex >= 0) {
						listModel.removeElementAt(selectedIndex);
						loper.verwijderSponsor(naam);
						updateAantal();
						mf.updateAantal();
					}
				}
			}
		}
	}

	public void updateAantal() {
		double geld = loper.getSponsorgelden();
		int decimalPlaces = 2;
		BigDecimal bd = new BigDecimal(geld);
		bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		geld = bd.doubleValue();
		lab2.setText(" Totaal sponsorgeld: " + geld);
	}
	
	public void ontvang(MainFrame mf) {
		this.mf = mf;
	}

}
