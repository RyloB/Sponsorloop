public class Sponsor {
	private String naam, criterium;
	private double bedrag;

	public Sponsor(String naam, String criterium, double bedrag) {
		this.naam = naam;
		this.criterium = criterium;
		this.bedrag = bedrag;
	}

	public double getBedrag() {
		return bedrag;
	}

	public String getNaam() {
		return naam;
	}

	public String getCriterium() {
		return criterium;
	}

}
