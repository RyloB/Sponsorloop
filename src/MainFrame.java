import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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

public class MainFrame extends JFrame implements ActionListener {
	private JButton b1, b2;
	private JList list;
	private DefaultListModel listModel;
	private ArrayList<Sponsorloop> sponsorlopen;
	private JLabel lab1, lab2;
	public LoperFrame lf;

	public MainFrame() {
		lf = null;
		sponsorlopen = new ArrayList<Sponsorloop>();
		setSize(200, 200);
		lab1 = new JLabel("Sponsorlopen", JLabel.CENTER);
		lab2 = new JLabel();
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(5);
		b1 = new JButton("Nieuwe Sponsorloop");
		b2 = new JButton("Verwijder Sponsorloop");
		JScrollPane listScrollPane = new JScrollPane(list);
		JPanel lowerPane = new JPanel();
		// lowerPane.setLayout(new BoxLayout(lowerPane, BoxLayout.Y_AXIS));
		lowerPane.setLayout(new BorderLayout());

		//Wat er gebeurt bij veranderen van selectie in het menu
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (lf != null) {
						lf.dispose();
						if (lf.getSf() != null) {
							lf.getSf().dispose();
						}
					}
					if (list.getSelectedValue() != null) {
						lf = new LoperFrame(vindLoop((String) list
								.getSelectedValue()));
						updateAantal();
						b2.setEnabled(true);;
						geefDoor();
					}
					if (list.getSelectedValue() == null) {
						b2.setEnabled(false);
					}

				}
			}
		});

		add(lab1, BorderLayout.NORTH);
		lowerPane.add(lab2, BorderLayout.NORTH);
		lowerPane.add(b1, BorderLayout.CENTER);
		lowerPane.add(b2, BorderLayout.SOUTH);
		b2.setEnabled(false);
		add(listScrollPane, BorderLayout.CENTER);
		add(lowerPane, BorderLayout.SOUTH);

		b1.addActionListener(this);
		b2.addActionListener(this);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(250, dim.height / 2 - this.getSize().height / 2);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			String naam = JOptionPane.showInputDialog(null,
					"Voer sponsorloop naam in: ", "Naam invoeren",
					JOptionPane.PLAIN_MESSAGE);
			if ((naam != null) && (naam.length() > 0)) {
				if (bestaatAl(naam)) {
					JOptionPane.showMessageDialog(null,
							"Er bestaat al een sponsorloop met deze naam",
							"Fout", JOptionPane.INFORMATION_MESSAGE);
				} else {
					listModel.addElement(naam);
					sponsorlopen.add(new Sponsorloop(naam));
				}
			}
		}
		if (e.getSource() == b2) {
			if (list.getSelectedIndex() >= 0) {
				String[] options = new String[2];
				options[0] = new String("Ja");
				options[1] = new String("Nee");
				int reply = JOptionPane.showOptionDialog(
						null,
						"Weet je zeker dat je sponsorloop '"
								+ (String) list.getSelectedValue()
								+ "' wilt verwijderen?", "Verwijderen?", 0,
						JOptionPane.INFORMATION_MESSAGE, null, options, null);
				if (reply == 0) {
					String naam = (String) list.getSelectedValue();
					int selectedIndex = list.getSelectedIndex();
					if (selectedIndex >= 0) {
						if (lf.getSf() != null) {
							lf.getSf().dispose();
						}

						verwijderLoop(naam);
						listModel.removeElementAt(selectedIndex);
						lf.dispose();
						updateAantal();
					}

				}
			}
		}
	}

	public boolean bestaatAl(String naam) {
		for (Sponsorloop s : sponsorlopen) {
			if (s.getNaam().equals(naam)) {
				return true;
			}
		}
		return false;
	}

	public void verwijderLoop(String naam) {
		for (int i = 0; i < sponsorlopen.size(); i++) {
			if (sponsorlopen.get(i).getNaam() == naam) {
				sponsorlopen.remove(i);
				System.out.println("Sponsorloop " + naam + " is verwijderd.");
				break;
			}
		}
	}

	public Sponsorloop vindLoop(String naam) {
		for (Sponsorloop s : sponsorlopen) {
			if (s.getNaam().equals(naam)) {
				return s;
			}
		}
		return null;
	}

	public void updateAantal() {
		if (list.getSelectedValue() == null) {
			lab2.setText(" Sponsorgeld: 0.0");
		} else {
			double geld = vindLoop((String) list.getSelectedValue())
					.getSponsorgelden();
			int decimalPlaces = 2;
			BigDecimal bd = new BigDecimal(geld);
			bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
			geld = bd.doubleValue();
			lab2.setText(" Sponsorgeld: " + geld);
		}
	}
	
	public void geefDoor() {
		lf.ontvang(this);
	}
	
}
