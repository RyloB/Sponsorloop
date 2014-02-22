import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

public class LoperFrame extends JFrame implements ActionListener {
	private JButton b1, b2;
	private JList list;
	private DefaultListModel listModel;
	private JLabel lab1, lab2, lab3;
	private SponsorFrame sf;
	private Sponsorloop loop;
	private MainFrame mf;

	public LoperFrame(final Sponsorloop loop) {
		this.loop = loop;
		sf = null;

		setSize(200, 200);
		lab1 = new JLabel("Lopers", JLabel.CENTER);
		lab2 = new JLabel();
		updateAantal();
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		deselect();
		list.setVisibleRowCount(5);
		b1 = new JButton("Nieuwe Loper");
		b2 = new JButton("Verwijder Loper");
		JScrollPane listScrollPane = new JScrollPane(list);
		JPanel lowerPane = new JPanel();
		lowerPane.setLayout(new BorderLayout());

		//Wat er gebeurt bij veranderen van selectie in het menu
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (sf != null) {
						sf.dispose();
					}
					if (list.getSelectedValue() != null) {
						sf = new SponsorFrame(loop.getLoper(getLoper()));
						updateAantal();
						b2.setEnabled(true);
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

		for (Loper l : loop.getLopers()) {
			listModel.addElement(l.getNummer() + ". " + l.getNaam());
		}

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(450, dim.height / 2 - this.getSize().height / 2);
		setVisible(true);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				mf.deselect();
				if (sf != null) {
					sf.dispose();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			String naam = JOptionPane.showInputDialog(null,
					"Voer loper naam in: ", "Naam invoeren",
					JOptionPane.PLAIN_MESSAGE);
			if ((naam != null) && (naam.length() > 1)) {
				listModel.addElement(loop.voegLoperToe(naam) + ". " + naam);
				updateAantal();
			}
		}
		if (e.getSource() == b2) {
			if (list.getSelectedIndex() >= 0) {
				String[] options = new String[2];
				options[0] = new String("Ja");
				options[1] = new String("Nee");
				int reply = JOptionPane.showOptionDialog(
						null,
						"Weet je zeker dat je loper '"
								+ (String) list.getSelectedValue()
								+ "' wilt verwijderen?", "Verwijderen?", 0,
						JOptionPane.INFORMATION_MESSAGE, null, options, null);
				if (reply == 0) {
					String naam = (String) list.getSelectedValue();
					int selectedIndex = list.getSelectedIndex();
					if (selectedIndex >= 0) {
						loop.verwijderLoper(getLoper());
						listModel.removeElementAt(selectedIndex);
						sf.dispose();
						updateAantal();
					}
				}
			}
		}
	}

	public Sponsorloop getLoop() {
		return loop;
	}

	public void updateAantal() {
		lab2.setText(" Aantal lopers: " + loop.getLopers().size());
	}

	public int getLoper() {
		String s = (String) list.getSelectedValue();
		String[] cijfer = s.split("\\.");
		int nummer = Integer.parseInt(cijfer[0]);
		return nummer;
	}

	public JFrame getSf() {
		return sf;
	}
	
	public void ontvang(MainFrame mf) {
		this.mf = mf;
	}
	
	public void geefDoor() {
		sf.ontvang(mf);
	}
	
	public void deselect() {
		list.clearSelection();
	}

}
