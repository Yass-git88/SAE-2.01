/**
 * La classe {@code Controleur} fait le lien entre la vue (interface graphique) et le modèle métier (MPM).
 * Elle centralise la gestion des tâches, des calculs de chemin critique, des dates au plus tôt/au plus tard,
 * ainsi que la synchronisation des données entre la vue et le modèle.
 *
 * Fonctionnalités principales :
 * <ul>
 *   <li>Gestion de l'historique pour l'annulation/rétablissement des modifications (undo/redo)</li>
 *   <li>Chargement, ajout, suppression et modification des tâches</li>
 *   <li>Calcul et affichage du chemin critique</li>
 *   <li>Calcul progressif des dates au plus tôt et au plus tard</li>
 *   <li>Synchronisation des données entre la grille, le graphe et les fichiers</li>
 *   <li>Sauvegarde des tâches dans un fichier</li>
 * </ul>
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */

package mpm;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mpm.ihm.FramePrincipale;
import mpm.ihm.PanelGrille;
import mpm.metier.GrilleDonneesModel;
import mpm.metier.Mpm;
import mpm.metier.Tache;
import mpm.metier.figure.Rectangle;


public class Controleur
{
	/*-----------------*/
	/*    Attributs    */
	/*-----------------*/
	
	private FramePrincipale vue;
	private Mpm             metier;

	private List<String>   historique;
	private int            indexHistorique;

	/**
	 * Constructeur de la classe Controleur.
	 */
	public Controleur()
	{
		this.metier = new Mpm();
		this.vue = new FramePrincipale(this);

		this.historique  = new ArrayList<>();
		this.indexHistorique = -1;

		this.sauvegarderEtatAvantModification();
	}

	/*-----------------*/
	/*   Accesseurs    */
	/*-----------------*/

	/**
	 * Retourne la liste des tâches du projet.
	 * @return la liste des tâches
	 */
	public List<Tache> getTaches() { return this.metier.getTaches(); }

	/**
	 * Retourne la liste des tâches critiques du projet.
	 * @return la liste des tâches critiques
	 */
	public List<Tache> getTachesCritiques() { return this.metier.getTachesCritiques(); }

	/**
	 * Retourne le chemin du fichier de données actuellement utilisé.
	 * @return le nom du fichier de données
	 */
	public String getFichier() { return this.metier.getFichier(); }

	/**
	 * Retourne le nombre de niveaux dans le graphe des tâches.
	 * @return le nombre de niveaux
	 */
	public int getNbNiveaux() { return this.metier.getNbNiveaux(); }

	/**
	 * Retourne l'indice du niveau actuellement traité pour le calcul des dates au plus tard.
	 * @return l'indice du niveau courant au plus tard
	 */
	public int getNiveauCourantAuPlusTard() { return this.metier.getNiveauCourantAuPlusTard(); }

	/**
	 * Retourne le modèle de données de la grille des tâches.
	 * @return le modèle de la grille des tâches
	 */
	public GrilleDonneesModel getGrilleDonneesModel() { return this.metier.getGrilleDonneesModel(); }

	/**
	 * Retourne la fenêtre principale de l'application.
	 * @return la vue principale
	 */
	public FramePrincipale getVue() { return this.vue; }

	/*------------------------------------------*/
	/*    Fonctionnalités de chemin critique    */
	/*------------------------------------------*/
	
	/**
	 * Calcule et retourne tous les chemins critiques du projet.
	 * @return la liste des chemins critiques, chaque chemin étant une liste de tâches
	 */
	public List<List<Tache>> calculerCheminCritique()
	{
		this.metier.calculerCheminCritique();
		return this.metier.calculerCheminsCritiques();
	}

	/*------------------------------------------*/
	/*    Fonctionnalités dates au plus tot     */
	/*------------------------------------------*/

	/**
	 * Réinitialise l'indice du niveau courant pour le calcul des dates au plus tôt.
	 */
	public void resetNiveauCourantAuPlusTot() { this.metier.resetNiveauCourantAuPlusTot(); }

	/**
	 * Calcule les dates au plus tôt pour toutes les tâches, niveau par niveau.
	 */
	public void calculerDatesAuPlusTotParNiveau() { this.metier.calculerDatesAuPlusTotParNiveau(); }

	/**
	 * Calcule les dates au plus tôt pour le niveau suivant.
	 * @return true si un niveau supplémentaire a été calculé, false sinon
	 */
	public boolean calculerDatesAuPlusTotNiveauSuivant() { return this.metier.calculerDatesAuPlusTotNiveauSuivant(); }

	/*-------------------------------------------*/
	/*    Fonctionnalités dates au plus tard     */
	/*-------------------------------------------*/

	/**
	 * Réinitialise l'indice du niveau courant pour le calcul des dates au plus tard.
	 */
	public void resetNiveauCourantAuPlusTard() { this.metier.resetNiveauCourantAuPlusTard(); }

	/**
	 * Calcule les dates au plus tard pour toutes les tâches, niveau par niveau.
	 */
	public void calculerDatesAuPlusTardParNiveau() { this.metier.calculerDatesAuPlusTardParNiveau(); }

	/**
	 * Calcule les dates au plus tard pour le niveau suivant.
	 * @return true si un niveau supplémentaire a été calculé, false sinon
	 */
	public boolean calculerDatesAuPlusTardNiveauSuivant() { return this.metier.calculerDatesAuPlusTardNiveauSuivant(); }

	/*-------------------------------*/
	/*    Fonctionnalités taches     */
	/*-------------------------------*/

	/**
	 * Recharge les tâches depuis le fichier actuellement utilisé.
	 */
	public void chargerTaches() { this.metier.chargerTaches(this.getFichier()); }

	/**
	 * Ajoute une nouvelle tâche au projet et à la grille.
	 * @param nom le nom de la tâche
	 * @param duree la durée de la tâche
	 * @param predecesseurs la liste des prédécesseurs (sous forme de chaîne)
	 * @param x la position x de la tâche
	 * @param y la position y de la tâche
	 */
	public void ajouterTache(String nom, int duree, String predecesseurs, String x, String y)
	{
		this.sauvegarderEtatAvantModification();
		this.metier.ajouterTache(nom, duree, predecesseurs, x, y);
		this.getGrilleDonneesModel().ajouterTache(nom, duree, predecesseurs, x, y);
	}

	/**
	 * Supprime une tâche du projet et de la grille.
	 * @param nom le nom de la tâche à supprimer
	 */
	public void supprimerTache(String nom)
	{
		this.sauvegarderEtatAvantModification();
		this.metier.supprimerTache(nom);
		this.getGrilleDonneesModel().supprimerTache(nom);
	}

	/**
	 * Charge les tâches depuis un fichier texte, réinitialise les niveaux pour les calculs au plus tôt/au plus tard,
	 * synchronise les positions des tâches entre le graphe et la grille, et réinitialise l'affichage.
	 *
	 * @param chemin le chemin du fichier à charger
	 */
	public void chargerTachesDepuisFichier(String chemin)
	{
		this.metier.chargerTaches(chemin);
		this.metier.resetNiveauCourantAuPlusTot();
		this.metier.resetNiveauCourantAuPlusTard();
		this.rafraichirAffichage();

		// Synchronise les positions calculées avec la grille
		Map<Tache, Point> mapPos = this.getVue().getPanelGraphe().getMap();
		var model = this.getGrilleDonneesModel();
		for (Tache t : this.getTaches())
		{
			if (t.getNom().equals("Début") || t.getNom().equals("Fin"))
				continue;
			Point p = mapPos.get(t);
			if (p == null)
				continue;
			for (int i = 0; i < model.getRowCount(); i++)
			{
				if (t.getNom().equals(model.getValueAt(i, 0)))
				{
					model.setValueAt(String.valueOf(p.x), i, 3); // colonne x
					model.setValueAt(String.valueOf(p.y), i, 4); // colonne y
					break;
				}
			}
		}

		if (this.vue != null)
		{
			this.vue.getPanelBouton().resetBoutons();
			this.vue.getPanelGraphe().setModeCheminCritique(false);
		}
	}

	/**
	 * Recharge les tâches depuis le fichier courant et met à jour l'affichage.
	 */
	public void rafraichirDepuisFichier()
	{
		this.metier.chargerTaches(this.getFichier());
		this.metier.calculerDatesAuPlusTotParNiveau();
		this.metier.calculerDatesAuPlusTardParNiveau();
		this.getGrilleDonneesModel().fireTableDataChanged();

		if (this.vue != null)
			this.vue.rafraichirAffichage();
	}

	/**
	 * Modifie une tâche existante dans le projet.
	 * @param nom le nom de la tâche à modifier
	 * @param duree la nouvelle durée
	 * @param predecesseurs la nouvelle liste de prédécesseurs (sous forme de chaîne)
	 */
	public void modifierTache(String nom, int duree, String predecesseurs)
	{
		this.sauvegarderEtatAvantModification();
		this.metier.modifierTache(nom, duree, predecesseurs);
		this.rafraichirDepuisFichier();
	}

	/*------------------------------------------*/
	/*    Fonctionnalités de Figure             */
	/*------------------------------------------*/

	/**
	 * Retourne une map associant chaque tâche à son rectangle d'affichage.
	 * @param mapTachePos la map des positions des tâches
	 * @return la map des rectangles associés aux tâches
	 */
	public Map<Tache, Rectangle> getRectanglesTaches(Map<Tache, Point> mapTachePos)
	{
		Map<Tache, Rectangle> mapRect = new HashMap<>();
		
		for (Map.Entry<Tache, Point> entry : mapTachePos.entrySet()) 
		{
			Point p = entry.getValue();
			// Rectangle centré sur la tâche (mêmes dimensions que dans PanelGraphe)
			mapRect.put(entry.getKey(), new Rectangle(p.x + 60, p.y + 40, 120, 80));
		}
		return mapRect;
	}

	/*-------------------------------*/
	/*    Fonctionnalités vue        */
	/*-------------------------------*/

	/**
	 * Rafraîchit l'affichage de la vue principale.
	 */
	public void rafraichirAffichage()
	{
		if (this.vue != null)
			this.vue.rafraichirAffichage();
	}

	/**
	 * Affiche la fenêtre du chemin critique et rafraîchit l'affichage.
	 */
	public void afficherCheminCritiqueEtRafraichir()
	{
		this.metier.calculerCheminCritique(); // met à jour lstTachesCritiques
		new mpm.ihm.FrameCheminCritique(this);
		this.rafraichirAffichage();
	}

	/**
	 * Retourne le panel de la grille des tâches.
	 * @return le panel de la grille
	 */
	public PanelGrille getPanelGrille()
	{
		return this.vue.getPanelGrille();
	}

	/* ---------------------------------- */
	/* Fonctionnalité de sauvegarde       */
	/* ---------------------------------- */

	/**
	 * Sauvegarde les tâches dans un fichier texte.
	 * @param nouveauFichier le chemin du fichier de sauvegarde
	 */
	public void sauvegarderTachesDansFichier(String nouveauFichier)
	{
		this.metier.sauvegarderTachesDansFichier(nouveauFichier);
	}

	/**
	 * Sauvegarde l'état courant des tâches avant une modification (pour l'historique undo/redo).
	 */
	public void sauvegarderEtatAvantModification()
	{
		StringBuilder sb = new StringBuilder();
		var model = this.getGrilleDonneesModel();
		for (int i = 0; i < model.getRowCount(); i++)
		{
			String nom   = model.getValueAt(i, 0) != null ? model.getValueAt(i, 0).toString() : "";
			String duree = model.getValueAt(i, 1) != null ? model.getValueAt(i, 1).toString() : "";
			String prec  = model.getValueAt(i, 2) != null ? model.getValueAt(i, 2).toString() : "";
			String x     = model.getValueAt(i, 3) != null ? model.getValueAt(i, 3).toString() : "";
			String y     = model.getValueAt(i, 4) != null ? model.getValueAt(i, 4).toString() : "";
			
			sb.append(nom).append("|").append(duree).append("|").append(prec).append("|").append(x).append("|")
					.append(y).append("\n");
		}
		// Supprime les états futurs si on annule puis modifie
		while (historique.size() > indexHistorique + 1)
		{
			historique.remove(historique.size() - 1);
		}
		historique.add(sb.toString());
		indexHistorique++;
	}

	/**
	 * Annule la dernière modification (fonctionnalité undo).
	 */
	public void annuler()
	{
		if (indexHistorique > 0)
		{
			indexHistorique--;
			String etatPrecedent = historique.get(indexHistorique);

			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(this.getFichier(), false), StandardCharsets.UTF_8)))
			{
				writer.write(etatPrecedent);
				writer.flush();
				this.chargerTachesDepuisFichier(this.getFichier());
				this.getGrilleDonneesModel().fireTableDataChanged();

				if (this.vue != null)
					this.vue.rafraichirAffichage();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Rétablit la modification annulée précédemment (fonctionnalité redo).
	 */
	public void refaire()
	{
		if (indexHistorique < historique.size() - 1)
		{
			indexHistorique++;
			String etatSuivant = historique.get(indexHistorique);

			try (BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(this.getFichier(), false), StandardCharsets.UTF_8)))
			{
				writer.write(etatSuivant);
				writer.flush();
				this.chargerTachesDepuisFichier(this.getFichier());
				this.getGrilleDonneesModel().fireTableDataChanged();

				if (this.vue != null)
					this.vue.rafraichirAffichage();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}


	/**
	 * Point d'entrée principal de l'application.
	 * @param args les arguments de la ligne de commande (non utilisés)
	 */
	public static void main(String[] args)
	{
		Controleur ctrl = new Controleur();
		ctrl.vue.setVisible(true);
		ctrl.getTachesCritiques(); // force la mise à jour de la liste
		ctrl.calculerCheminCritique(); // calcule tous les chemins critiques (pour la fenêtre)
		ctrl.rafraichirAffichage();
	}
}
