/**
 * La classe {@code Mpm} gère la planification de tâches selon la méthode du chemin critique (MPM).
 * Elle permet de charger des tâches depuis un fichier, de calculer les dates au plus tôt/au plus tard,
 * ainsi que les marges, et d'afficher les résultats.
 *
 * Fonctionnalités principales :
 *   Chargement des tâches et de leurs dépendances depuis un fichier texte
 *   Calcul des dates au plus tôt et au plus tard pour chaque tâche
 *   Calcul de la marge de chaque tâche
 *   Affichage détaillé des tâches, de leurs dates et dépendances
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 *
 */

package mpm.metier;

import java.io.*;
import java.util.*;


public class Mpm
{
	/*------------------*/
	/*    Attributs     */
	/*------------------*/

	// attribut d'association
	private List<Tache>        lstTaches;
	private GrilleDonneesModel grilleDonneesModel;

	private CheminCritique cheminCritique;

	// attribut d'instance
	private int niveauCourantAuPlusTot;
	private int niveauCourantAuPlusTard;

	private List<Integer> lstNiveau;
	private List<Tache>   lstTachesCritiques;

	/*---------------*/
	/* Constructeur  */
	/*---------------*/

	/**Constructeur de la classe Mpm.
	 */
	public Mpm()
	{
		this.lstTaches          = new ArrayList<>();
		this.lstNiveau          = new ArrayList<>();
		this.lstTachesCritiques = new ArrayList<>();

		this.grilleDonneesModel = new GrilleDonneesModel();

		this.cheminCritique     = new CheminCritique();

		this.niveauCourantAuPlusTot  = 0;
		this.niveauCourantAuPlusTard = 0;
	}

	/*---------------*/
	/* Accesseurs    */
	/*---------------*/

	/**Récupère la liste des tâches.
	 * @return Liste de Tache.
	 */
	public List<Tache>        getTaches                 ()  { return this.lstTaches;                       }

	/**Récupère la liste des tâches critiques.
	 * @return List Tache.
	 */
	public List<Tache>        getTachesCritiques        ()  { return this.lstTachesCritiques;              }

	/**Récupère la grilleDonneesModel
	 * @return La grille.
	 */
	public GrilleDonneesModel getGrilleDonneesModel     ()  { return this.grilleDonneesModel;              }

	/**Récupère le fichier qui est utilisé dans GrilleDonneesModel
	 * @return Fichier en String.
	 */
	public String             getFichier                ()  { return this.grilleDonneesModel.getFichier(); }

	/**Récupère le niveau courant pour les calculer des dates au plus tard.
	 * @return Entier qui représente le dit niveau.
	 */
	public int                getNiveauCourantAuPlusTard()  { return this.niveauCourantAuPlusTard;         }

	/**Récupère le nombre de niveau.
	 * @return Entier qui représente la taille des niveaux.
	 */
	public int                getNbNiveaux              ()  { return this.lstNiveau.size();                }
	
	/*------------------*/
	/* Modificateurs    */
	/*------------------*/

	/**Modifie le fichier dans grille donnees model.
	 * @param fichier Nouveau fichier qui sera lu.
	 */
	public void setFichier(String fc) { this.grilleDonneesModel.setFichier(fc); }

	/*---------------------------------*/
	/*    Fonctionnalités de taches    */
	/*---------------------------------*/

	/**Permet de charger les tâches en fonction du fichier entré en paramètre.
	 * @param fichier String qui représente le nom du fichier à lire.
	 */
	public void chargerTaches(String fichier)
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/
		
		Map<String, Tache> tachesMap               ;

		String[]           deps                    ;
		String[]           parties                 ;

		String             ligne                   ;
		String             nom, x, y               ;

		int                duree, niveau           ; 

		Tache              tache , debut, fin, pred;
		
		tachesMap = new HashMap<>();
		this.lstTaches.clear();
		this.lstNiveau.clear();
		this.niveauCourantAuPlusTot = 0;

		// Ajoute Début
		debut = new Tache("Début", 0, null, "196", "162");
		this.lstTaches.add(debut);
		tachesMap.put("Début", debut);

		try (BufferedReader reader = new BufferedReader(new FileReader(fichier)))
		{
			while ((ligne = reader.readLine()) != null)
			{
				if (ligne.isBlank()) continue;
				parties = ligne.split("\\|");
				if (parties.length < 2) continue;

				nom   = parties[0].trim();
				duree = Integer.parseInt(parties[1].trim());
				x     = parties.length > 3 ? parties[3].trim() : "0";
				y     = parties.length > 4 ? parties[4].trim() : "0";

				tache    = new Tache(nom, duree, parties.length > 2 ? parties[2].trim() : null, x, y);

				tachesMap.put(nom, tache);

				lstTaches.add(tache);
			}
		}
		catch (IOException | NumberFormatException e) 
		{
			System.err.println("Erreur lors du chargement des tâches : " + e.getMessage());
			e.printStackTrace();
		}

		// Ajoute Fin
		fin = new Tache("Fin", 0, null, "0", "0");
		this.lstTaches.add(fin);
		tachesMap.put("Fin", fin);

		// Résolution des dépendances
		for (Tache t : lstTaches)
		{
			if (t.getPredecesseurString() != null)
			{
				deps = t.getPredecesseurString().split(",");
				for (String depNom : deps)
				{
					pred = tachesMap.get(depNom.trim());
					if (pred != null)
					{
						t.addPredecesseur(pred);
						pred.addSuccesseur(t);
					}
				}
			}
		}

		// Relie à "Fin" uniquement les tâches sans successeur (hors Fin et Début)
		for (Tache t : lstTaches)
		{
			if ( ! t.getNom().equals("Fin") && ! t.getNom().equals("Début") && t.getSuccesseurs().isEmpty())
			{
				t.addSuccesseur(fin);
				fin.addPredecesseur(t);
			}
		}

		// Relie les tâches de niveau 1 à "Début" (optionnel)
		for (Tache t : lstTaches)
		{
			if (t != debut && t.getPredecesseurs().isEmpty())
			{
				t.addPredecesseur(debut);
				debut.addSuccesseur(t);
			}
		}

		// Calcul des niveaux
		this.calculerNiveau();

		// Mets à jour lstNiveau
		this.lstNiveau.clear();
		for (Tache t : lstTaches)
		{
			niveau = t.getNiveauTache();
			if (!this.lstNiveau.contains(niveau))
				this.lstNiveau.add(niveau);
		}
		Collections.sort(this.lstNiveau);

		// Sécurité : Début ne doit jamais être reliée à Fin
		debut.getSuccesseurs().remove(fin);
		fin.getPredecesseurs().remove(debut);

		this.grilleDonneesModel = new GrilleDonneesModel(fichier); 
	}


	/*------------------------------------------*/
	/*    Fonctionnalités dates au plus tot     */
	/*------------------------------------------*/

	/**Permet de calculer les dates au plut tôt en fonction du niveau de la tâche.
	 */
	public void calculerDatesAuPlusTotParNiveau()
	{
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		int max, val;

		for (Tache t : lstTaches) 
			t.setDateMin(0);

		for (int niveau : this.lstNiveau)
		{
			for (Tache t : lstTaches)
			{
				if (t.getNiveauTache() == niveau)
				{
					max = 0;
					for (Tache pred : t.getPredecesseurs())
					{
						val = pred.getDateMin() + pred.getDuree();
						if (val > max) max = val;
					}
					t.setDateMin(max);
				}
			}
		}
	}

	/**Permet de calculer la date au plus tôt du niveau suivant de la tâche.
	 * @return boolean qui représente si le calcul s'est bien passé.
	 */
	public boolean calculerDatesAuPlusTotNiveauSuivant()
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		int niveau, max, val;
		
		this.niveauCourantAuPlusTot++;
		if (this.niveauCourantAuPlusTot >= lstNiveau.size()) return false;

		niveau = lstNiveau.get(this.niveauCourantAuPlusTot);
		for (Tache t : lstTaches)
		{
			if (t.getNiveauTache() == niveau)
			{
				max = 0;
				for (Tache pred : t.getPredecesseurs())
				{
					val = pred.getDateMin() + pred.getDuree();
					if (val > max) max = val;
				}
				t.setDateMin(max);
			}
		}
		return this.niveauCourantAuPlusTot < lstNiveau.size() - 1;

	}

	/**Permet de réinitialiser le niveau courant pour les dates au plus tôt.
	 * Si ce n'est pas le début on met à -1.
	 */
	public void resetNiveauCourantAuPlusTot()
	{
		this.niveauCourantAuPlusTot = 0;

		for (Tache t : lstTaches)
		{
			if (t.getNom().equals("Début"))
				t.setDateMin(0);
			else
				t.setDateMin(-1);
		}
	}

	/*-------------------------------------------*/
	/*    Fonctionnalités dates au plus tard     */
	/*-------------------------------------------*/

	/**Permet de calculer les dates au plus tard par niveau.
	 */
	public void calculerDatesAuPlusTardParNiveau()
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		List<Integer> niveauxDesc             ;

		int           finProjet, datePossible ;
		
		// Trouve la date de fin du projet
		finProjet = 0;
		for (Tache t : lstTaches)
			finProjet = Math.max(finProjet, t.getDateMin() + t.getDuree());

		// Initialise les dates au plus tard
		for (Tache t : lstTaches)
			t.setDateMax(finProjet - t.getDuree());

		// Parcours les niveaux dans l'ordre décroissant
		niveauxDesc = new ArrayList<>(this.lstNiveau);
		Collections.reverse(niveauxDesc);

		for (int niveau : niveauxDesc)
		{
			for (Tache t : lstTaches)
			{
				if (t.getNiveauTache() == niveau)
				{
					for (Tache succ : t.getSuccesseurs())
					{
						datePossible = succ.getDateMax() - t.getDuree();

						if (datePossible < t.getDateMax())
							t.setDateMax(datePossible);
					}
				}
			}
		}
	}

	/**Permet de calculer la date au plus tard du niveau suivant de la tâche.
	 * Comme nous parcourons à l'envers alors le suivant est le précédent de la tâche.
	 * @return boolean qui représente si le calcul s'est bien passé.
	 */
	public boolean calculerDatesAuPlusTardNiveauSuivant()
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		int niveau, min, val;

		// On utilise un index pour parcourir lstNiveau du dernier au premier
		if (this.niveauCourantAuPlusTard >= lstNiveau.size()) return false; // Plus de niveaux

		niveau = lstNiveau.get(lstNiveau.size() - 1 - this.niveauCourantAuPlusTard);
		for (Tache t : lstTaches)
		{
			if (t.getNiveauTache() == niveau)
			{
				if (t.getSuccesseurs().isEmpty())
					t.setDateMax(t.getDateMin());  // Pour "Fin" ou tâche terminale
				
				else
				{
					min = Integer.MAX_VALUE;
					for (Tache succ : t.getSuccesseurs())
					{
						val = succ.getDateMax() - t.getDuree();
						if (val < min) min = val;
					}
					t.setDateMax(min);
				}
			}
		}
		this.niveauCourantAuPlusTard++; // On passe au niveau précédent (plus petit)
		return this.niveauCourantAuPlusTard < lstNiveau.size();
	}

	/**Permet de réinitialiser le niveau courant pour les dates au plus tard.
	 */
	public void resetNiveauCourantAuPlusTard()
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		int finProjet;
		
		this.niveauCourantAuPlusTard = 0;
		finProjet = 0;
		for (Tache t : lstTaches)
			if (t.getNom().equals("Fin"))
				finProjet = t.getDateMin();

		if (finProjet == 0)
			System.out.println("Attention : calcule d'abord les dates au plus tôt !");
		

		for (Tache t : lstTaches)
			t.setDateMax(finProjet);
	}

	/*-------------------------------*/
	/*    Fonctionnalités taches     */
	/*-------------------------------*/

	/**Permet de modifier une Tache.
	 * @param ancienNom             Ancien nom de la tâche.
	 * @param nouvelleDuree         Nouvelle durée de la tâche.
	 * @param nouveauxPredecesseurs Nouveaux prédécesseurs de la tâche.
	 */
	public void modifierTache(String ancienNom, int nouvelleDuree, String nouveauxPredecesseurs)
	{
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		GrilleDonneesModel model   ;
		int                lig     ;
		Object             nomCell ;

		model = this.getGrilleDonneesModel();
		lig = -1;
		for (int i = 0; i < model.getRowCount(); i++)
		{
			nomCell = model.getValueAt(i, 0);

			if (nomCell != null && nomCell.toString().equals(ancienNom))
			{
				lig= i;
				break;
			}
		}
		if (lig== -1) return; // tâche non trouvée

		model.setValueAt(ancienNom, lig, 0); // le nom ne change pas
		model.setValueAt(String.valueOf(nouvelleDuree), lig, 1);
		model.setValueAt(nouveauxPredecesseurs, lig, 2);
	}

	/**Permet de trier les tâches.
	 */
	public void trierTaches()  { Collections.sort(lstTaches, Comparator.comparing(Tache::getNom));}

	/**Permet d'ajouter une tâche à la liste des tâches.
	 * @param nom           Nom de la tâche à ajouter.
	 * @param duree         Duree de la tâche qui est ajouté.
	 * @param predecesseurs Predecesseurs de la tâche ajouté.
	 * @param coordX        Coordonnée X de la tâche en <strong> String </strong>.
	 * @param coordY        Coordonnée Y de la tache en <strong> String </strong>.
	 */
	public void ajouterTache(String nom, int duree, String predecesseurs, String x, String y)
	{
		this.lstTaches.add(new Tache(nom, duree, predecesseurs, x, y));
		this.trierTaches();
	}
	
	/**Sert à supprimer la tâche dont le nom est entré en paramètre.
	 * @param nom Nom de la tâche à supprimer.
	 */
	public void supprimerTache(String nom)
	{
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/
		
		Iterator<Tache> itTache;
		Tache t;
		
		itTache = lstTaches.iterator();

		while (itTache.hasNext())
		{
			t = itTache.next();
			if (t.getNom().equals(nom))
			{
				for (Tache succ : t.getSuccesseurs())
					succ.getPredecesseurs().remove(t);

				for (Tache pred : t.getPredecesseurs())
					pred.getSuccesseurs().remove(t);

				itTache.remove();
			}
		}
	}

	/**Permet de sauvegarder les tâches dans un autre fichier.
	 * @param nouveauFIchier Nom du fichier dans lequel les tâches seront enregistrés.
	 */
	public void sauvegarderTachesDansFichier(String nouveauFichier)  {this.grilleDonneesModel.sauvegarderTachesDansFichier(nouveauFichier);}

	/**Permet de calculer le niveau de chaque tâche.
	 */
	public void calculerNiveau()
	{
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/
		
		boolean modif;

		do
		{
			modif = false;
			for (Tache t : this.lstTaches)
			{
				int niveau = 1;
				for (Tache pred : t.getPredecesseurs())
				{
					niveau = Math.max(niveau, pred.getNiveauTache() + 1);
				}
				if (t.getNiveauTache() != niveau)
				{
					t.setNiveauTache(niveau);
					modif = true;
				}
			}
		} while (modif);
	}


	/*------------------------------------------*/
	/*    Fonctionnalités chemin critique       */
	/*------------------------------------------*/

	/**Permet de calculer le chemin critique.
	 * @return Une liste de liste de tâches qui sont tous les chemins critiques.
	 */
	public List<List<Tache>> calculerCheminsCritiques()
	{
		this.cheminCritique.setTaches(this.lstTaches); 
		return this.cheminCritique.calculerLesCheminsCritiques();
	}

	/**Permet de calculer un chemin critique.
	 * @return Une liste de tâches qui sont dans le chemin critique.
	 */
	public List<Tache>        calculerCheminCritique    ()  { return this.cheminCritique.calculerCheminCritique();             }
	
}