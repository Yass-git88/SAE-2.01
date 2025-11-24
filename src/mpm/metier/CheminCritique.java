package mpm.metier;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code CheminCritique} gère le calcul du ou des chemins critiques dans un projet.
 * Elle permet de déterminer les tâches critiques, de calculer tous les chemins critiques possibles,
 * et de mettre à jour les marges des tâches.
 *
 * Fonctionnalités principales :
 *   Calcul du chemin critique principal à partir des dépendances et marges
 *   Recherche de tous les chemins critiques possibles entre "Début" et "Fin"
 *   Mise à jour des marges pour toutes les tâches du projet
 *   Gestion de la liste des tâches du projet pour l'analyse du chemin critique
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */
public class CheminCritique
{
	
	/*------------------*/
	/*    Attributs     */
	/*------------------*/
	
	private List<Tache> lstTachesCritiques;
	private List<Tache> lstTaches;
	
	/**Constructeur de CheminCritique.
	 */
	public CheminCritique()
	{
		this.lstTachesCritiques = new ArrayList<>();
		this.lstTaches = new ArrayList<>();
	}

	/*----------------------*/
	/*    Modificateurs     */
	/*----------------------*/

	/**Sert à modifier la liste de tâches par celle entrée en paramètre.
	 * @param taches Liste de tâches
	 */
	public void setTaches(List<Tache> taches) {this.lstTaches = taches;}

	/*------------------------------------------*/
	/*    Fonctionnalités de chemin critique    */
	/*------------------------------------------*/

	/**Sert à trouver UN chemin critique.
	 * @return Une liste de tâche qui font partie du chemin critique.
	 */
	public List<Tache> calculerCheminCritique()
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		Tache tacheCourante,suivant;
		int   minNiveau            ;
		
		this.lstTachesCritiques.clear();

		// Cherche la tâche "Fin"
		tacheCourante = null;
		for (Tache t : this.lstTaches)
		{
			if (t.getNom().equals("Fin"))
			{
				tacheCourante = t;
				break;
			}
		}
		if (tacheCourante == null)
			return this.lstTachesCritiques;
		

		// Remonte le chemin critique en prenant à chaque fois le prédécesseur critique avec le niveau le plus bas
		while (tacheCourante != null && !tacheCourante.getNom().equals("Début"))
		{
			this.lstTachesCritiques.add(0, tacheCourante);

			suivant = null;
			minNiveau = Integer.MAX_VALUE;
			for (Tache pred : tacheCourante.getPredecesseurs())
			{
				if (pred.getMarge() == 0 && pred.getNiveauTache() < minNiveau)
				{
					minNiveau = pred.getNiveauTache();
					suivant = pred;
				}
			}
			tacheCourante = suivant;
		}
		// Ajoute "Début" si présent
		if (tacheCourante != null && tacheCourante.getNom().equals("Début"))
			this.lstTachesCritiques.add(0, tacheCourante);
		

		// Enlève "Début" et "Fin" si tu ne veux pas les afficher
		if (!this.lstTachesCritiques.isEmpty() && this.lstTachesCritiques.get(0).getNom().equals("Début"))
			this.lstTachesCritiques.remove(0);
		
		if (!this.lstTachesCritiques.isEmpty() && this.lstTachesCritiques.get(this.lstTachesCritiques.size() - 1).getNom().equals("Fin"))
			this.lstTachesCritiques.remove(this.lstTachesCritiques.size() - 1);
		

		return this.lstTachesCritiques;
	}

	/**Permet de trouver LES chemins critiques.
	 * @return Une liste de liste de tâches qui sont les tâches faisaient partie d'un chemin critique.
	 */
	public List<List<Tache>> calculerLesCheminsCritiques()
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/
		
		List<List<Tache>> cheminsCritiques  ;
		List<Tache>       cheminCourant     ;

		Tache             debut, fin        ;

		cheminsCritiques = new ArrayList<>();
		cheminCourant    = new ArrayList<>();


		// Recherche des tâches "Début" et "Fin"
		debut = null;
		fin   = null;
		for (Tache t : this.lstTaches)
		{
			calculerMarge();
			if (t.getNom().equals("Début"))
				debut = t;
			
			if (t.getNom().equals("Fin"))
				fin = t;
			
		}

		if (debut == null || fin == null)
			return cheminsCritiques;
		
		trouverCheminsCritiquesRec(debut, fin, cheminCourant, cheminsCritiques);

		return cheminsCritiques;
	}

	/**Vérifie que le chemin critique est bon.
	 * @param courant tache courante regargé.
	 * @param fin     tache "Fin".
	 * @param chemin  Liste de tâches.
	 * @param result  Liste de liste de tâches qui sont les tâches faisaient partie d'un chemin critique.
	 */
	private void trouverCheminsCritiquesRec(Tache courant, Tache fin, List<Tache> chemin, List<List<Tache>> result)
	{
		
		/*-------------------------------*/
		/*           Variable            */
		/*-------------------------------*/
		boolean estCritique;
		
		// On ajoute au chemin que si marge=0 ou Début/Fin
		if (courant.getNom().equals("Début") || courant.getNom().equals("Fin") || courant.getMarge() == 0)
		{
			chemin.add(courant);

			if (courant == fin)
			{
				// Vérifie que toutes les tâches du chemin (hors Début et Fin) ont bien marge=0
				estCritique = true;
				for (Tache t : chemin)
				{
					if (!t.getNom().equals("Début") && !t.getNom().equals("Fin") && t.getMarge() != 0)
					{
						estCritique = false;
						break;
					}
				}
				if (estCritique)
					result.add(new ArrayList<>(chemin));
				
			}
			else
			{
				for (Tache succ : courant.getSuccesseurs())
				{
					if (!chemin.contains(succ))
						trouverCheminsCritiquesRec(succ, fin, chemin, result);
					
				}
			}
			chemin.remove(chemin.size() - 1);
		}
	}

	/**Sert à calculer la marge de chaque tâche.
	 * Si une tâche a dteDebut = dteFin alors marge 0.
	 */
	public void calculerMarge()
	{
		for (Tache t : lstTaches)
			t.calculerMarge();
	}

	
}
