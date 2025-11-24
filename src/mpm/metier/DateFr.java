package mpm.metier;

import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * Classe DateFr qui étend GregorianCalendar pour gérer les dates au format européen.
 */
public class DateFr extends GregorianCalendar 
{
	/**
	 * Constructeur par défaut.
	 * Initialise la date à la date et l'heure courantes.
	 */
	public DateFr() 
	{
		super();
	}

	/**
	 * Constructeur avec jour, mois et année.
	 * @param jour Le jour du mois.
	 * @param mois Le mois (1-12).
	 * @param annee L'année.
	 */
	public DateFr(int jour, int mois, int annee) 
	{
		this(annee, mois - 1, jour, 0, 0, 0);
	}

	/**
	 * Constructeur avec jour, mois, année, heure et minute.
	 * @param jour Le jour du mois.
	 * @param mois Le mois (1-12).
	 * @param annee L'année.
	 * @param heure L'heure (0-23).
	 * @param minute Les minutes (0-59).
	 */
	public DateFr(int jour, int mois, int annee, int heure, int minute) 
	{
		super(annee, mois - 1, jour, heure, minute);
	}

	/**
	 * Constructeur avec jour, mois, année, heure, minute et seconde.
	 * @param jour Le jour du mois.
	 * @param mois Le mois (1-12).
	 * @param annee L'année.
	 * @param heure L'heure (0-23).
	 * @param minute Les minutes (0-59).
	 * @param seconde Les secondes (0-59).
	 */
	public DateFr(int jour, int mois, int annee, int heure, int minute, int seconde) 
	{
		super(annee, mois - 1, jour, heure, minute, seconde);
	}

	/**
	 * Constructeur avec une date au format jj/mm/aaaa.
	 * @param date La date sous forme de chaîne (ex : "jj/mm/aaaa").
	 */
	public DateFr(String date) 
	{
		super();
		StringTokenizer st = new StringTokenizer(date, "/");
		int jour = Integer.parseInt(st.nextToken());
		int mois = Integer.parseInt(st.nextToken());
		int annee = Integer.parseInt(st.nextToken());
		this.set(annee, mois - 1, jour);
	}

	/**
	 * Constructeur par recopie.
	 * @param autreDate L'autre date à copier.
	 */
	public DateFr(DateFr autreDate) 
	{
		super(autreDate.get(YEAR), autreDate.get(MONTH) - 1, autreDate.get(DAY_OF_MONTH),
			  autreDate.get(HOUR_OF_DAY), autreDate.get(MINUTE), autreDate.get(SECOND));
	}

	/**
	 * Renvoie la valeur d'un champ de date.
	 * @param field Le champ (ex : DAY_OF_MONTH, MONTH, YEAR).
	 * @return La valeur du champ.
	 */
	public int get(int field) 
	{
		// Ajustement des valeurs pour le format européen
		switch (field) 
		{
			case DAY_OF_MONTH, YEAR, MINUTE, SECOND -> {
				return super.get(field); // Valeurs inchangées
			}
			case MONTH -> {
				return super.get(field) + 1; // Mois de 1 à 12
			}
			case HOUR -> {
				return super.get(HOUR_OF_DAY); // Heure de 0 à 23
			}
			default -> {
				return super.get(field); // <-- Ajoute cette ligne pour gérer tous les autres champs
			}
		}
	}

	/**
	 * Renvoie la date sous forme de chaîne formatée.
	 * @param format Le format de la chaîne (ex : "jj/mm/aaaa").
	 * @return La date formatée.
	 */
	public String toString(String format) 
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < format.length(); i++) 
		{
			char c = format.charAt(i);
			switch (c) {
				case 'j' -> sb.append(String.format("%02d", get(DAY_OF_MONTH)));
				case 'm' -> sb.append(String.format("%02d", get(MONTH)));
				case 'a' -> sb.append(get(YEAR));
				default  -> sb.append(c);
			}
		}
		return sb.toString();
	}


	/**
	 * Vérifie si le jour courant est un jour férié en France.
	 * @return true si le jour est férié, false sinon.
	 */
	public boolean estFerie() 
	{
		int jour = get(DAY_OF_MONTH);
		int mois = get(MONTH);

		// Jours fixes
		if ((jour == 1 && mois == 1)  || // Jour de l'an
			(jour == 1 && mois == 5)  || // Fête du travail
			(jour == 8 && mois == 5)  || // Victoire 1945
			(jour == 14 && mois == 7) || // Fête Nationale
			(jour == 11 && mois == 11))  // Armistice 1918
			return true;

		// Jours variables (simplification)
		GregorianCalendar paques = calculerPaques(get(YEAR));

		if (equals(paques) || equals(addDays(paques, 1)) || // Dimanche et lundi de Pâques
			equals(addDays(paques, 39)) || // Ascension
			equals(addDays(paques, 49)) || equals(addDays(paques, 50)))  // Pentecôte
			return true;
		

		return false;
	}

	private GregorianCalendar calculerPaques(int annee)
	
	{
		int cycleMetonique = annee % 19;
		int siecle = annee / 100; 
		int resteSiecle = annee % 100;
		int divSieclePar4 = siecle / 4; 
		int resteDivSieclePar4 = siecle % 4; 
		int correctionSiecle = (siecle + 8) / 25; 
		int ajustementSiecle = (siecle - correctionSiecle + 1) / 3; 
		int epacte = (19 * cycleMetonique + siecle - divSieclePar4 - ajustementSiecle + 15) % 30; 
		int divResteSieclePar4 = resteSiecle / 4; 
		int resteDivResteSieclePar4 = resteSiecle % 4;
		int jourSemaine = (32 + 2 * resteDivSieclePar4 + 2 * divResteSieclePar4 - epacte - resteDivResteSieclePar4) % 7; 
		int correctionLunaire = (cycleMetonique + 11 * epacte + 22 * jourSemaine) / 451; 
		int mois = (epacte + jourSemaine - 7 * correctionLunaire + 114) / 31; 
		int jour = ((epacte + jourSemaine - 7 * correctionLunaire + 114) % 31) + 1;

		return new GregorianCalendar(annee, mois - 1, jour);
	}

	private GregorianCalendar addDays(GregorianCalendar date, int days) 
	{
		GregorianCalendar newDate = (GregorianCalendar) date.clone();
		newDate.add(DAY_OF_MONTH, days);
		return newDate;
	}

	/**
	 * Calcule la différence en jours entre la date courante et une autre date.
	 * @param autreDate La date à comparer.
	 * @return La différence en jours (positive si la date courante est après, négative sinon).
	 */
	public int differenceJour(DateFr autreDate) 
	{
		long diffMillis = this.getTimeInMillis() - autreDate.getTimeInMillis();
		return (int) (diffMillis / (1000 * 60 * 60 * 24));
	}
}