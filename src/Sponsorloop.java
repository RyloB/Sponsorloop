import java.util.ArrayList;

public class Sponsorloop {
	private String naam;
	private ArrayList<Loper> lopers;
	private ArrayList<Integer> nummers;
	private int loperTeller;

	public Sponsorloop(String naam) {
		this.naam = naam;
		loperTeller = 0;
		lopers = new ArrayList<Loper>();
		nummers = new ArrayList<Integer>();
		System.out.println("Sponsorloop " + naam + " is aangemaakt.");
	}

	public ArrayList<Loper> getLopers() {
		return lopers;
	}

	public int voegLoperToe(String naam) {
		int nummer = 0;
		if (nummers.size() > 0) {
			nummer = getLaagste();
		} else {
			loperTeller++;
			nummer = loperTeller;
		}
		Loper l = new Loper(nummer, naam);
		lopers.add(l);
		System.out.println("Loper " + naam + " met nummer " + nummer
				+ " is toegevoegd aan " + this.naam + ".");
		return nummer;
	}

	public int getLaagste() {
		int laagste = 0;
		int positie = 0;
		for (int i = 0; i < nummers.size(); i++) {
			if (i < laagste || laagste == 0) {
				laagste = nummers.get(i);
				positie = i;
			}
		}
		nummers.remove(positie);
		return laagste;
	}

	public void verwijderLoper(int nummer) {
		nummers.add(nummer);
		System.out.println("Loper " + getLoper(nummer).getNaam()
				+ " met nummer " + nummer + " is verwijderd.");
		lopers.remove(getLoper(nummer));
	}

	public Loper getLoper(int nummer) {
		Loper deLoper = null;
		for (Loper l : lopers) {
			if (l.getNummer() == nummer) {
				deLoper = l;
				break;
			}
		}
		return deLoper;
	}

	public int getTeller() {
		return loperTeller;
	}

	public double getSponsorgelden() {
		double totaal = 0;
		for (Loper l : lopers) {
			totaal += l.getSponsorgelden();
		}
		return totaal;
	}

	public String getNaam() {
		return naam;
	}

}
