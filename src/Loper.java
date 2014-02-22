import java.util.ArrayList;

public class Loper {
	private int nummer;
	private String naam;
	private ArrayList<Sponsor> sponsors;
	private double totaal;

	public Loper(int nummer, String naam) {
		this.nummer = nummer;
		this.naam = naam;
		sponsors = new ArrayList<Sponsor>();
		totaal = 0;
	}

	public void voegSponsorToe(String naam, String criterium, double bedrag) {
		Sponsor s = new Sponsor(naam, criterium, bedrag);
		sponsors.add(s);
		System.out.println("Sponsor " + naam + " met sponsorbedrag " + bedrag
				+ " is toegevoegd.");
	}

	public double getSponsorgelden() {
		totaal = 0;
		for (Sponsor s : sponsors) {
			totaal += s.getBedrag();
		}
		return totaal;
	}

	public int getNummer() {
		return nummer;
	}

	public String getNaam() {
		return naam;
	}

	public Sponsor getSponsor(String naam) {
		Sponsor deSponsor = null;
		for (Sponsor s : sponsors) {
			if (s.getNaam() == naam) {
				deSponsor = s;
				break;
			}
		}
		return deSponsor;
	}

	public ArrayList<Sponsor> getSponsors() {
		return sponsors;
	}

	public void verwijderSponsor(String naam) {
		for (Sponsor s : sponsors) {
			if (s.getNaam() == naam) {
				sponsors.remove(s);
				break;
			}
		}
	}

}
