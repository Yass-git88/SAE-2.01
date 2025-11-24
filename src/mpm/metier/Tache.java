/**
 * Représente une tâche dans un projet, avec ses dépendances, sa durée, ses dates au plus tôt et au plus tard, et sa marge.
 * Permet de gérer les relations de précédence et de succession entre tâches, ainsi que le calcul de la marge.
 * 
 * Attributs principaux :
 * nom : le nom de la tâche
 * duree : la durée de la tâche en jours
 * dateMin : date au plus tôt (début possible)
 * dateMax : date au plus tard (fin possible)
 * marge : marge de la tâche (dateMax - dateMin)
 * lstPrc : liste des tâches précédentes (prédécesseurs)
 * lstSvt : liste des tâches suivantes (successeurs)
 * lstCheminCritique : liste des tâches du chemin critique
 * predecesseurString : représentation textuelle des prédécesseurs
 * 
 * Méthodes principales :
 * Accesseurs et mutateurs pour tous les attributs
 * Ajout de prédécesseurs et successeurs
 * Calcul de la marge
 * Affichage détaillé de la tâche via {@link #toString()}
 * 
 * @author Dumont Enzo, El-Maati Yassine, Gricourt Paul, Sefil-Amouret Matys
 * @version 1.0
 */
package mpm.metier;

import java.util.ArrayList;
import java.util.List;


public class Tache 
{
	/*---------------*/
	/* Attributs     */
	/*---------------*/
	
	// attributs d'associations
	private ArrayList<Tache>    lstSvt;
	private ArrayList<Tache>    lstPrc;
	private List<Tache>         lstTaches;
	private DateFr              dateDebutProjet;

	// attributs d'instance
	private String nom;
	private String predecesseurString;
	private int    duree;
	private int    dateMin;
	private int    dateMax;
	private int    marge;
	private int    niveauTache;

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/

	/**
	 * Constructeur de la classe Tache.
	 * @param nom          Nom de la tâche qui sera créée en String.
	 * @param duree        Durée de la tâche en int.
	 * @param predecesseur Predecesseurs de la tâche en String sous la forme : nom|durée|prédecesseurs|coordX|coordY 
	 * @param coordX       Coordonnée X de la tâche en <strong> String </strong>.
	 * @param coordY       Coordonnée Y de la tâche en <strong> String </strong>.
	 */
	public Tache ( String nom ,int duree, String predecesseurString, String x, String y )
	{
		this.nom     = nom;
		this.duree   = duree;
		this.dateMin = 0;
		this.dateMax = 0;
		this.marge   = 0;

		this.predecesseurString = predecesseurString;

		this.lstSvt    = new ArrayList<Tache>();
		this.lstPrc    = new ArrayList<Tache>();
		this.lstTaches = new ArrayList<Tache>();

		this.dateDebutProjet = new DateFr();
	}


	/*-------------------------------*/
	/*          Accesseurs           */
	/*-------------------------------*/
	
	/**Retourne le nom de la tâche.
	 * @return le nom de la tâche
	 */
	public String getNom() { return this.nom; }

	/**Retourne la durée de la tâche en jours.
	 * @return la durée de la tâche
	 */
	public int getDuree() { return this.duree; }

	/**Retourne la marge de la tâche (dateMax - dateMin).
	 * @return la marge de la tâche
	 */
	public int getMarge() { return this.marge; }

	/**Retourne la date au plus tôt (début possible) de la tâche.
	 * @return la date au plus tôt
	 */
	public int getDateMin() { return this.dateMin; }

	/**Retourne la date au plus tard (fin possible) de la tâche.
	 * @return la date au plus tard
	 */
	public int getDateMax() { return this.dateMax; }

	/**Retourne la liste des tâches précédentes (prédécesseurs).
	 * @return la liste des prédécesseurs
	 */
	public List<Tache> getPredecesseurs() { return this.lstPrc; }

	/**Retourne la liste des tâches suivantes (successeurs).
	 * @return la liste des successeurs
	 */
	public List<Tache> getSuccesseurs() { return this.lstSvt; }

	/**Retourne la représentation textuelle des prédécesseurs.
	 * @return la chaîne des prédécesseurs
	 */
	public String getPredecesseurString() { return this.predecesseurString; }

	/**Retourne le niveau de la tâche dans le graphe.
	 * @return le niveau de la tâche
	 */
	public int getNiveauTache() { return this.niveauTache; }

	/*--------------------------------*/
	/*         Modificateurs          */
	/*--------------------------------*/

	/**Modifie la marge de la tâche.
	 * @param marge la nouvelle marge
	 */
	public void setMarge(int marge) { this.marge = marge; }

	/**Modifie la date au plus tôt de la tâche.
	 * @param dateMin la nouvelle date au plus tôt
	 */
	public void setDateMin(int dateMin) { this.dateMin = dateMin; }

	/**Modifie la date au plus tard de la tâche.
	 * @param dateMax la nouvelle date au plus tard
	 */
	public void setDateMax(int dateMax) { this.dateMax = dateMax; }

	/**Modifie la date au plus tôt de la tâche (alias de setDateMin).
	 * @param val la nouvelle date au plus tôt
	 */
	public void setDateAuPlusTot(int val) { this.dateMin = val; }

	/**Modifie la date au plus tard de la tâche (alias de setDateMax).
	 * @param val la nouvelle date au plus tard
	 */
	public void setDateAuPlusTard(int val) { this.dateMax = val; }

	/**Modifie la représentation textuelle des prédécesseurs.
	 * @param s la nouvelle chaîne des prédécesseurs
	 */
	public void setPredecesseurString(String s) { this.predecesseurString = s; }

	/**Modifie le niveau de la tâche dans le graphe.
	 * @param niveau le nouveau niveau
	 */
	public void setNiveauTache(int niveau) { this.niveauTache = niveau; }

	/*-------------------------------*/
	/*        Autre méthodes         */
	/*-------------------------------*/

	/**Ajoute une tâche à la liste des prédécesseurs.
	 * @param t la tâche à ajouter comme prédécesseur
	 */
	public void addPredecesseur(Tache t) { this.lstPrc.add(t); }

	/**Ajoute une tâche à la liste des successeurs.
	 * @param t la tâche à ajouter comme successeur
	 */
	public void addSuccesseur(Tache t) { this.lstSvt.add(t); }

	/**Calcule et met à jour la marge de la tâche (dateMax - dateMin).
	 */
	public void calculerMarge() { this.marge = this.dateMax - this.dateMin; }

	/**Formate une date à partir du nombre de jours écoulés depuis le début du projet.
	 * @param nbJours le nombre de jours depuis le début du projet
	 * @return la date formatée en "j/m/a"
	 */
	public String formatDateFr(int nbJours)
	{
		DateFr date = new DateFr(dateDebutProjet);
		date.add(DateFr.DAY_OF_MONTH, nbJours);
		return date.toString("j/m");
	}

	/**Retourne une représentation détaillée de la tâche et de ses dépendances.
	 * @return une chaîne décrivant la tâche, ses dates, sa marge, ses prédécesseurs et successeurs
	 */
	public String toString() 
	{
		
		/*-------------------------------*/
		/*           Variable            */
		/*-------------------------------*/
		String sRet;

		sRet = "";

		for (Tache t : lstTaches)
		{

			sRet += t.getNom() + " : " + t.getDuree() + " jour" + (t.getDuree() > 1 ? "s" : "") + "\n";
			sRet += "  date au plus tôt : " + formatDateFr(t.getDateMin()) + "\n";
			sRet += "  date au plus tard : " + formatDateFr(t.getDateMax()) + "\n";
			sRet += "  marge : " + t.getMarge() + " jour" + (t.getMarge() > 1 ? "s" : "") + "\n";

			if (t.getPredecesseurs().isEmpty())
			{
				sRet += "  pas de tâche précédente\n";
			}
			else
			{
				sRet += "  liste des tâches précédentes : \n    ";
				List<Tache> preds = t.getPredecesseurs();
				for (int i = 0; i < preds.size(); i++)
				{
					sRet += preds.get(i).getNom();
					if (i < preds.size() - 1)
						sRet += ", ";
				}
				sRet += "\n";
			}

			if (t.getSuccesseurs().isEmpty())
			{
				sRet += "  pas de tâche suivante\n";
			}
			else
			{
				sRet += "  liste des tâches suivantes   :\n    ";
				List<Tache> succs = t.getSuccesseurs();
				for (int i = 0; i < succs.size(); i++)
				{
					sRet += succs.get(i).getNom();
					if (i < succs.size() - 1)
						sRet += ", ";
				}
				sRet += "\n";
			}
		}
		return sRet;
	}
}
