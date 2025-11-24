package mpm.metier;

import iut.algo.Decomposeur;
import javax.swing.table.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


/**
 * La classe {@code GrilleDonneesModel} gère les données de la grille des tâches du projet sous forme de tableau.
 * Elle permet le chargement, l'affichage, la modification et la sauvegarde des tâches dans un fichier texte,
 * tout en assurant la synchronisation avec l'interface graphique.
 *
 * Fonctionnalités principales :
 *   Chargement des tâches depuis un fichier texte
 *   Affichage et édition des tâches dans un tableau (JTable)
 *   Ajout, suppression et modification des tâches
 *   Sauvegarde automatique des modifications dans le fichier associé
 *   Synchronisation des données avec le contrôleur et la vue
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */
public class GrilleDonneesModel extends AbstractTableModel
{

	/*------------------*/
	/*    Attributs     */
	/*------------------*/

	private String[]   tabEntetes;
	private Object[][] tabDonnees;

	private String fichier;

	/**Constructeur par défaut de GrilleDonnnesModel.
	 */
	public GrilleDonneesModel()
	{
		this.tabEntetes = new String[] { "Nom","Durée","Tâches Précédentes", "x", "y" };

		this.fichier = null;

		this.tabDonnees = new Object[0][5];
	}

	/**Constructeur de GrilleDonnneesModel avec un paramètre.
	 * @param fichier String qui représente le fichier à lire.
	 */
	public GrilleDonneesModel(String fichier) 
	{
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		Scanner  scFichier              ;
		          
		String   ligne                  ;
		
		int      nbTotalLigne, nbLigne  ;
		
		String[] parties                ;
		String   nom, duree, prec, x, y ;

		nbTotalLigne = 0;
		
		this.fichier = fichier;


		this.tabEntetes = new String[] { "Nom","Durée","Tâches Précédentes", "x", "y" };

		// Compter le nombre de lignes non vides dans le fichier
		try 
		{
			scFichier = new Scanner(new FileInputStream(this.fichier), StandardCharsets.UTF_8);

			while (scFichier.hasNextLine())
			{
				ligne = scFichier.nextLine().trim();
				if (!ligne.isEmpty()) nbTotalLigne++;
			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.tabDonnees = new Object[nbTotalLigne][5];

		nbLigne = 0;

		// Remplir le tableau avec les données du fichier
		try 
		{
			scFichier = new Scanner(new FileInputStream(this.fichier), StandardCharsets.UTF_8);

			while (scFichier.hasNextLine())
			{
				ligne = scFichier.nextLine().trim();
				if (ligne.isEmpty()) continue;

				parties = ligne.split("\\|");
				nom     = parties.length > 0 ? parties[0].trim() : "";
				duree   = parties.length > 1 ? parties[1].trim() : "";
				prec    = parties.length > 2 ? parties[2].trim() : "";
				x       = parties.length > 3 ? parties[3].trim() : "";
				y       = parties.length > 4 ? parties[4].trim() : "";

				this.tabDonnees[nbLigne++] = new Object[] { nom, duree, prec, x, y };
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*------------------------------------------*/
	/*               Accesseurs                 */
	/*------------------------------------------*/

	/**Récupère le fichier qui a été lu.
	 * @return Une chaîne de caractère qui représente le chemin du fichier.
	 */
	public String getFichier    ()                 { return this.fichier;                   }
	
	/**Récupère le nombre de ligne de la grille 
	 * @return Entier qui représente le nombre de ligne de la grille.
	 */
	public int    getRowCount   ()                 { return this.tabDonnees.length;            }

	/**Récupère le nombre de colonne de la grille.
	 * @return Entier qui représente le nombre de colonne de la grille.
	 */
	public int    getColumnCount()                 { return this.tabEntetes.length;            }

	/**Récupère le nom de la colonne de la colonne entrée en paramètre.
	 * @param col Entier qui représente la colonne que l'on veut récupérer son nom.
	 * @return Une chîne de caractères qui est le nom de la colonne entrée en paramètre.
	 */
	public String getColumnName (int col)          { return this.tabEntetes[col];              }

	/**Return la valeur à la ligne et la colonne entrée en paramètre.
	 * @param lig Entier qui représente la ligne.
	 * @param col Entier qui représente la colonne.
	 * 
	 * @return La valeur à la position ligne, colonne spécifié en paramètre.
	 */
	public Object getValueAt    (int lig, int col) { return this.tabDonnees[lig][col];         }
 
	/**Récupère la classe à la ligne 0 et la colonne spécifié en paramètre.
	 * @param colonne Entier qui représente la colonne.
	 * 
	 * @return La classe de la valeur à la ligne 0 et la colonnne entrée en paramètre.
	 */
	public Class  getColumnClass(int colonne)      { return getValueAt(0, colonne).getClass(); }

	/*------------------------------------------*/
	/*               Modificateurs              */
	/*------------------------------------------*/

	/**Permet de modifier le fichier à lire.
	 */
	public void setFichier(String fc) 	{this.fichier = fc;}

	/**Permet de modifer la valeur à la ligne et la colonne entrées en paramètre.
	 * @param valeur  La valeur de base sera remplacée par celle-ci.
	 * @param ligne   Coordonnée de la case pour sa ligne.
	 * @param colonne Coordonnée de la case pour sa colonne.
	 */
	public void setValueAt(Object val, int lig, int col)
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/
		
		String   nom, duree, prec, x, y ;

		// Met à jour la valeur dans le tableau
		if (col >= 0 && col < tabDonnees[lig].length) 
		{
			this.tabDonnees[lig][col] = val;
			this.fireTableCellUpdated(lig, col);

			// Met à jour le fichier texte
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(this.fichier, false), StandardCharsets.UTF_8))) 
			{
				for (int i = 0; i < this.tabDonnees.length; i++) 
				{
					nom   = this.tabDonnees[i][0] != null ? this.tabDonnees[i][0].toString() : "";
					duree = this.tabDonnees[i][1] != null ? this.tabDonnees[i][1].toString() : "";
					prec  = this.tabDonnees[i][2] != null ? this.tabDonnees[i][2].toString() : "";
					x     = this.tabDonnees[i][3] != null ? this.tabDonnees[i][3].toString() : "";
					y     = this.tabDonnees[i][4] != null ? this.tabDonnees[i][4].toString() : "";
					writer.println(nom + "|" + duree + "|" + prec + "|" + x + "|" + y);
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**Permet de dire si une case à la ligne et colonne entrées en paramètre est modifiable ou non.
	 * @param ligne   Coordonnées de la case.
	 * @param colonne Coordonnées de la case.
	 */
	public boolean isCellEditable(int row, int col) { return ( col == 0 || col == 1 || col == 2) || (col == 3 || col == 4);  }

	/*------------------------------------------*/
	/*    Fonctionnalités de taches             */
	/*------------------------------------------*/

	/**Permet d'ajouter une tâche dans la grille.
	 * @param nom           Nom de la tâche à ajouter en String.
	 * @param duree         Durée de la tâche à ajouter en entier.
	 * @param predecesseurs Predecesseurs de la tâche à ajouter sous la forme : tache1,tache2
	 * @param coordX        Coordonnée X de la tâche à ajouter en String.
	 * @param coordY        Coordonnée X de la tâche à ajouter en String.
	 */
	public void ajouterTache(String nom, int duree, String predecesseurs, String x, String y) 
	{

		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		int ind;
		Object[][] nvTab;

		ind = 0;

		while (ind < this.tabDonnees.length && nom.compareToIgnoreCase(this.tabDonnees[ind][0].toString()) > 0) 
			ind++;
		

		nvTab = new Object[this.tabDonnees.length + 1][5];

		for (int i = 0, j = 0; i < nvTab.length; i++) 
		{
			if (i == ind) 
				nvTab[i] = new Object[] { nom, String.valueOf(duree), predecesseurs, x, y };
			
			else 
				nvTab[i] = this.tabDonnees[j++];
		}

		this.tabDonnees = nvTab;
		fireTableDataChanged();
		sauvegarderDansFichier();
	}

	/**Sert à supprimer une tâche dans la grille.
	 * @param nom Nom de la tâche à supprimer.
	 */
	public void supprimerTache(String nom) 
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		Object[][] nvTab;
		int        ind  ;

		ind = -1;

		for (int i = 0; i < this.tabDonnees.length; i++) 
		{
			if (this.tabDonnees[i][0].equals(nom)) 
			{
				ind = i;
				break;
			}
		}
		if (ind == -1) return;

		nvTab = new Object[this.tabDonnees.length - 1][5];

		for (int i = 0, j = 0; i < this.tabDonnees.length; i++) 
		{
			if (i != ind) nvTab[j++] = this.tabDonnees[i];
		}
		this.tabDonnees = nvTab;

		fireTableDataChanged();
		sauvegarderDansFichier();
	}

	/*------------------------------------------*/
	/*    Fonctionnalités de fichier            */
	/*------------------------------------------*/

	/**Permet de sauvegarder les données dans un fichier.
	 * Elle écrit dans celui-ci sous la forme nom|durée|prédecesseurs|coordX|coordY .
	 */
	public void sauvegarderDansFichier() 
	{
		try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.fichier, false), StandardCharsets.UTF_8))) 
		{
			for (Object[] ligne : this.tabDonnees) 
			writer.println(ligne[0] + "|" + ligne[1] + "|" + ligne[2] + "|" + ligne[3] + "|" + ligne[4]);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**Permet de sauvegarder des tâches dans un nouveau fichier.
	 * @param nouveauFichier Nom du nouveau fichier en String.
	 */
	public void sauvegarderTachesDansFichier(String nouveauFichier)
	{

		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		String   nom, duree, prec, x, y ;

		try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(nouveauFichier, false), StandardCharsets.UTF_8)))
		{
			for (int i = 0; i < this.getRowCount(); i++)
			{
				nom   = this.getValueAt(i, 0) != null ? this.getValueAt(i, 0).toString() : "";
				duree = this.getValueAt(i, 1) != null ? this.getValueAt(i, 1).toString() : "";
				prec  = this.getValueAt(i, 2) != null ? this.getValueAt(i, 2).toString() : "";
				x     = this.getValueAt(i, 3) != null ? this.getValueAt(i, 3).toString() : "";
				y     = this.getValueAt(i, 4) != null ? this.getValueAt(i, 4).toString() : "";
				writer.println(nom + "|" + duree + "|" + prec + "|" + x + "|" + y);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}