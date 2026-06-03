package model;

import java.util.Date;

/**
 * The type Main.
 */
public class Main {
	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	@SuppressWarnings("java:S106")
	public static void main(String[] args) {
		Docente d = new Docente("prof_mario", "ciao000", "mariorossi@professore.it", "Mario", "Rossi", "SSN000");
		Studente s = new Studente("lolloz", "ciao123", "lorenzo@studente.it", "Lorenzo", "Mollo", "DE1086");
		Coordinatore c = new Coordinatore("coord_anna", "ciao321", "annaversi@professore.it", "Carolina", "Monaco", "SSN321", "Informatica");

		Tirocinio tirocinio1 = d.aggiungiTirocinio(1, "Esperienza Java");
		RichiestaTirocinio req = s.richiediTirocinio(d, tirocinio1, new Date());
		d.valutaRichiesta(req, true);

		SedutaDiLaurea sed = c.inserisciSeduta(new Date(), "09:00", "Aula Magna", "S01");
		c.aggiungiDocenteACommissione(d, sed);

		Tesi tesi = s.caricaTesi("Titolo Tesi", "Desktop/tesi.pdf");
		PrenotazioneLaurea prenotazione = s.prenotaSedutaLaurea(s,tesi, sed);

		d.valutaTesi(tesi, true);
		if (tesi.getStato() == StatoTesi.ACCETTATO) {
			prenotazione.setStato(StatoLaurea.ACCETTATO);
		}

		System.out.println("Stato finale prenotazione: " + prenotazione.getStato());
	}
	}
