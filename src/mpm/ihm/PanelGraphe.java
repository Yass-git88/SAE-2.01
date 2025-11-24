package mpm.ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import mpm.Controleur;
import mpm.metier.Tache;
import mpm.metier.figure.Rectangle;

/**
 * Classe PanelGraphe.
 * Sert à afficher le graphe MPM.
 */
public class PanelGraphe extends JPanel 
{

	/*------------------*/
	/* Attributs */
	/*------------------*/

	private Controleur ctrl;
	private Map<Tache, Point> mapTachePos;

	private boolean modeCheminCritique = false;

	private JPopupMenu popupTache;
	private Tache tacheSurvolee;

	private Tache tacheSelectionnee;
	private Point decalage;

	private boolean modeAffichageDateReelle;

	/**
	 * Constructeur de PanelGraphe.
	 * 
	 * @param ctrl Permet de faire le lien avec Controleur et donc la class MPM.
	 */
	public PanelGraphe(Controleur ctrl) 
	{
		GereSouris gereSouris;

		this.ctrl = ctrl;
		this.mapTachePos = new HashMap<>();

		this.modeAffichageDateReelle = false;

		this.popupTache = new JPopupMenu();
		this.popupTache.setEnabled( false );
		this.popupTache.setBackground(this.getBackground());

		this.tacheSurvolee = null;

		this.recalculerPositions();

		this.setBackground(Color.LIGHT_GRAY);

		gereSouris = new GereSouris();
		this.addMouseListener(gereSouris);
		this.addMouseMotionListener(gereSouris);
		this.addMouseMotionListener(new GereSurvolTache());
	}

	/*-----------------*/
	/* Accesseurs      */
	/*-----------------*/

	/**
	 * Méthodes qui récupère les arcs qui font partie du chemin critique pour les
	 * colorier.
	 * 
	 * @return Retroune une liste de nom d'arcs comme suivant : <br>
	 *         "nom de l'arc -> nom de l'arc+1".
	 */
	private List<String> getArcsCritiques() 
	{
		List<String> arcsCritiques;
		List<List<Tache>> chemins;
		Tache t1, t2;

		arcsCritiques = new ArrayList<>();

		chemins = ctrl.calculerCheminCritique();

		for (List<Tache> chemin : chemins) 
		{
			for (int i = 0; i < chemin.size() - 1; i++) 
			{
				t1 = chemin.get(i);
				t2 = chemin.get(i + 1);
				arcsCritiques.add(t1.getNom() + "->" + t2.getNom());
			}
		}
		return arcsCritiques;
	}

	public Map<Tache, Point> getMap() 
	{
		return this.mapTachePos;
	}

	/*-------------------*/
	/* Modificateurs */
	/*-------------------*/

	/**
	 * Sert à modifier le mode de chemin critique.
	 * Si l'affichage du graphe doit mettre en évidence le chemin critique alors
	 * modeCheminCritique sera <em>true</em>.
	 * 
	 * @param actif si false alors modeCheminCritique sera à false.
	 */
	public void setModeCheminCritique(boolean actif) 
	{
		this.modeCheminCritique = actif;
		repaint();
	}

	/**
	 * Sert à positionner les tâches en fonction de leur coordonnées.
	 * 
	 * @param nomTache Le nom de la tâche de type String.
	 * @param x        Coordonnée de x en int.
	 * @param y        Coordonnée de y en int.
	 */
	public void setPositionTache(String nomTache, int x, int y) 
	{
		for (Tache t : this.ctrl.getTaches()) 
		{
			if (t.getNom().equals(nomTache)) 
			{
				this.mapTachePos.put(t, new Point(x, y));
				break;
			}
		}
		this.repaint();
	}

	/**
	 * Sert à dire si nous devons afficher les dates réelles ou des jours fictifs.
	 * 
	 * @param b si true alors modeAffichageDateReelle est true.
	 */
	public void setModeAffichageDateReelle(boolean b) 
	{
		this.modeAffichageDateReelle = b;
		this.repaint(); // Ajout pour forcer la mise à jour immédiate
	}

	/**
	 * Sert à recalculer les positions des tâches en fonction de leur niveau.
	 */
	public void recalculerPositions() 
	{
		Map<Integer, List<Tache>> tachesParNiveau;

		int niveauMax;
		int x, y;

		Tache debut, fin;

		String nom;
		String xStr, yStr;

		tachesParNiveau = new HashMap<>();

		niveauMax = 1;

		debut = null;
		fin = null;

		xStr = null;
		yStr = null;

		this.mapTachePos.clear();

		for (Tache t : this.ctrl.getTaches()) 
		{
			if (t.getNom().equals("Début"))
				debut = t;
			else if (t.getNom().equals("Fin"))
				fin = t;
			else 
			{
				int niveau = t.getNiveauTache();
				niveauMax = Math.max(niveauMax, niveau);
				tachesParNiveau.computeIfAbsent(niveau, k -> new ArrayList<>()).add(t);
			}
		}

		// Place "Début" tout à gauche
		if (debut != null)
			mapTachePos.put(debut, new Point(50, 50));

		// Place les autres tâches par niveau, mais utilise x / y du modèle
		for (int niveau = 1; niveau <= niveauMax; niveau++) 
		{
			List<Tache> tachesNiveau = tachesParNiveau.get(niveau);
			if (tachesNiveau == null)
				continue;

			for (Tache t : tachesNiveau) 
			{
				// Cherche la ligne correspondante dans le modèle pour récupérer x / y
				nom = t.getNom();
				for (int i = 0; i < ctrl.getGrilleDonneesModel().getRowCount(); i++) {
					if (nom.equals(ctrl.getGrilleDonneesModel().getValueAt(i, 0))) {
						xStr = String.valueOf(ctrl.getGrilleDonneesModel().getValueAt(i, 3));
						yStr = String.valueOf(ctrl.getGrilleDonneesModel().getValueAt(i, 4));
						break;
					}
				}

				try 
				{
					x = (xStr != null && !xStr.isBlank()) ? Integer.parseInt(xStr) : 50 + niveau * 180;
				} 
				catch (Exception e) 
				{
					x = 50 + niveau * 180;
				}

				try 
				{
					y = (yStr != null && !yStr.isBlank()) ? Integer.parseInt(yStr) : 50;
				} 
				catch (Exception e) 
				{
					y = 50;
				}
				mapTachePos.put(t, new Point(x, y));
			}
		}

		// Place "Fin" tout à droite
		if (fin != null)
			mapTachePos.put(fin, new Point(50 + (niveauMax + 1) * 180, 50));

	}

	/**
	 * Sert à dessiner ou redessine le graphe entré en paramètre.
	 * Affiche les carrés, les flèches, les noms des tâches ainsi que leur durée.
	 * 
	 * @param g Graphe de type Graphics sur lequel nous allons dessiner.
	 */
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);

		/*-------------------------------*/
		/* Variables */
		/*-------------------------------*/

		Graphics2D g2;
		FontMetrics fm, fmDate, fmValeur;

		String tot, tard;
		String nom;
		String totDate, tardDate;
		String arcKey;
		String duree;

		int xTexte, yTexte;
		int largeurNom, largeurDate, largeurTexte;
		int x, y;
		int largeur, hauteur;
		int hauteurTitre, hauteurValeurs;
		int largeurCase, yValeurs, yValeur;
		int xTot;
		int xStart, yStart;
		int xEnd, yEnd;
		int xMilieu, yMilieu;

		Point point, point1, point2;
		Integer largeur1, largeur2;

		List<String> arcsCritiques;

		g2 = (Graphics2D) g;

		// calcul des largeurs des rectangles
		Map<Tache, Integer> largeurRectangles = new HashMap<>();
		for (Tache tache : this.ctrl.getTaches()) 
		{
			fm = g.getFontMetrics();
			largeurNom = fm.stringWidth(tache.getNom());
			largeurDate = 0;

			if (modeAffichageDateReelle) 
			{
				totDate = (tache.getDateMin() != -1) ? tache.formatDateFr(tache.getDateMin()) : "";
				tardDate = (tache.getDateMax() != -1) ? tache.formatDateFr(tache.getDateMax()) : "";
				fmDate = g.getFontMetrics(new Font("Serif", Font.PLAIN, 14));
				largeurDate = Math.max(fmDate.stringWidth(totDate), fmDate.stringWidth(tardDate));
			}
			largeur = Math.max(80, Math.max(largeurNom, largeurDate * 2) + 40);
			largeurRectangles.put(tache, largeur);
		}

		// Dessin des tâches (nœuds)
		for (Tache tache : this.ctrl.getTaches()) 
		{
			point = mapTachePos.get(tache);
			if (point == null)
				continue; // Sécurité

			fm = g2.getFontMetrics();
			largeurNom = fm.stringWidth(tache.getNom());

			largeurDate = 0;
			if (modeAffichageDateReelle) 
			{
				totDate = (tache.getDateMin() != -1) ? tache.formatDateFr(tache.getDateMin()) : "";
				tardDate = (tache.getDateMax() != -1) ? tache.formatDateFr(tache.getDateMax()) : "";

				fmDate = g2.getFontMetrics(new Font("Serif", Font.PLAIN, 14));

				largeurDate = Math.max(fmDate.stringWidth(totDate), fmDate.stringWidth(tardDate));
			}

			x = point.x;
			y = point.y;

			// Largeur adaptative : max entre nom et date, fois 2 pour les deux cases, plus
			// un peu de marge
			largeur = Math.max(80, Math.max(largeurNom, largeurDate * 2) + 40);
			hauteur = 80;
			hauteurTitre = 25;
			hauteurValeurs = hauteur - hauteurTitre;
			largeurCase = largeur / 2;
			yValeurs = y + hauteurTitre;

			if (modeAffichageDateReelle) 
			{
				tot = (tache.getDateMin() != -1) ? tache.formatDateFr(tache.getDateMin()) : "";
				tard = (tache.getDateMax() != -1) ? tache.formatDateFr(tache.getDateMax()) : "";
			} 
			else 
			{
				tot = (tache.getDateMin() != -1) ? String.valueOf(tache.getDateMin()) : "";
				tard = (tache.getDateMax() != -1) ? String.valueOf(tache.getDateMax()) : "";
			}

			// Rectangle principal
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(2.0f));
			g2.drawRect(x, y, largeur, hauteur);
			g2.drawLine(x, y + hauteurTitre, x + largeur, y + hauteurTitre);

			// Texte du nom, centré
			fm = g2.getFontMetrics();
			nom = tache.getNom();
			xTexte = x + (largeur - fm.stringWidth(nom)) / 2;
			yTexte = y + (hauteurTitre + fm.getAscent()) / 2 - 2;
			g2.setFont(new Font("Arial", Font.BOLD, 16));
			g2.setColor(Color.BLACK);
			g2.drawString(nom, xTexte, yTexte);

			// Sous-rectangles pour les deux valeurs
			g2.drawRect(x, yValeurs, largeurCase, hauteurValeurs);
			g2.drawRect(x + largeurCase, yValeurs, largeurCase, hauteurValeurs);

			g2.drawRect(x, yValeurs, largeurCase, hauteurValeurs);
			g2.drawRect(x + largeurCase, yValeurs, largeurCase, hauteurValeurs);

			// === AJOUT : Affichage des dates au plus tôt et au plus tard ===
			g2.setFont(new Font("Arial", Font.BOLD, 14));
			g2.setColor(Color.BLACK);

			// Centrage vertical dans la case
			fmValeur = g2.getFontMetrics();
			yValeur = yValeurs + (hauteurValeurs + fmValeur.getAscent()) / 2 - 2;

			// Au plus tôt (gauche)
			xTot = x + (largeurCase - fmValeur.stringWidth(tot)) / 2;
			g2.setColor(Color.BLUE);
			g2.drawString(tot, xTot, yValeur);

			// Au plus tard (droite)
			int xTard = x + largeurCase + (largeurCase - fmValeur.stringWidth(tard)) / 2;
			g2.setColor(Color.RED);
			g2.drawString(tard, xTard, yValeur);

			g2.setColor(Color.WHITE);

		}

		// Dessin des arcs (dépendances)
		arcsCritiques = getArcsCritiques();

		for (Tache tache : this.ctrl.getTaches()) 
		{
			point1 = mapTachePos.get(tache);
			largeur1 = largeurRectangles.get(tache);

			for (Tache successeur : tache.getSuccesseurs()) 
			{
				point2 = mapTachePos.get(successeur);
				largeur2 = largeurRectangles.get(successeur);

				if (point1 != null && point2 != null && largeur1 != null && largeur2 != null) 
				{
					arcKey = tache.getNom() + "->" + successeur.getNom();

					if (modeCheminCritique && arcsCritiques.contains(arcKey))
						g2.setColor(Color.RED);
					else
						g2.setColor(Color.BLUE);

					// Départ : bord droit du rectangle source
					xStart = point1.x + largeur1;
					yStart = point1.y + 40;
					// Arrivée : bord gauche du rectangle destination
					xEnd = point2.x;
					yEnd = point2.y + 40;

					dessineFleche(g2, xStart, yStart, xEnd, yEnd);

					// Affichage de la durée au milieu de l'arc
					xMilieu = (xStart + xEnd) / 2;
					yMilieu = (yStart + yEnd) / 2;

					duree = "" + tache.getDuree();
					fm = g2.getFontMetrics();
					largeurTexte = fm.stringWidth(duree);

					g2.setColor(this.getBackground());
					g2.fillRect(xMilieu - largeurTexte / 2, yMilieu - 10, largeurTexte + 4, largeurTexte + 4);
					g2.setColor(Color.BLACK);
					g2.drawString(duree, xMilieu - largeurTexte / 2, yMilieu);
				}
			}
		}
	}

	/**
	 * Sert à modifier la couleur du graphic en rouge si la tâche entrée en
	 * pramaètre est dans le chemin critique.
	 * 
	 * @param g2    graphic sur lequel nous allons modifier la couleur.
	 * @param tache De la classe Tache, nous vérifions si elle est dans le chemin
	 *              critique.
	 */
	public void ChangerCouleurCritique(Graphics2D g2, Tache tache) 
	{
		if (this.ctrl.getTachesCritiques().contains(tache))
			g2.setColor(Color.RED);
		else
			g2.setColor(Color.BLACK);
	}

	/**
	 * Sert à dessiner une flèche sur le graphic donné en paramètre.02d
	 * 
	 * @param g2 graphique dans lequel nous allons dessiner la flèche.
	 * 
	 *           o * @param x1 entier qui est le coor
	 * @param y1
	 * 
	 * @param x2
	 * @param y2
	 * 
	 */
	private void dessineFleche(Graphics2D g2, int x1, int y1, int x2, int y2) 
	{
		int tailleFleche = 10;

		double dx = x2 - x1;
		double dy = y2 - y1;
		double angle = Math.atan2(dy, dx);

		// Ligne principale
		g2.drawLine(x1, y1, x2, y2);

		// Calcul des points du triangle
		int xFleche1 = (int) (x2 - tailleFleche * Math.cos(angle - Math.PI / 6));
		int yFleche1 = (int) (y2 - tailleFleche * Math.sin(angle - Math.PI / 6));
		int xFleche2 = (int) (x2 - tailleFleche * Math.cos(angle + Math.PI / 6));
		int yFleche2 = (int) (y2 - tailleFleche * Math.sin(angle + Math.PI / 6));

		int[] xPoints = { x2, xFleche1, xFleche2 };
		int[] yPoints = { y2, yFleche1, yFleche2 };

		g2.setColor(Color.BLUE);
		g2.fillPolygon(xPoints, yPoints, 3);

	}

	public boolean estCheminCritique() 
	{
		return this.modeCheminCritique;
	}

	private Tache getTacheAt(int x, int y) 
	{
		for (Map.Entry<Tache, Point> entree : this.getMap().entrySet()) 
		{
			Point point = entree.getValue();
			// Rectangle centré comme dans paintComponent
			int largeur = Math.max(80, 120); // adapte si besoin
			int hauteur = 80;
			Rectangle rect = new Rectangle(point.x + largeur / 2, point.y + hauteur / 2, largeur, hauteur);
			if (rect.possede(x, y)) 
			{
				return entree.getKey();
			}
		}
		return null;
	}

	private void afficherPopupTache(Tache tache, int x, int y) 
	{
		this.popupTache.setVisible(false);
		this.popupTache.removeAll();
		this.popupTache.add(new JMenuItem("Nom : " + tache.getNom()));
		this.popupTache.addSeparator();
		this.popupTache.add(new JMenuItem("Durée : " + tache.getDuree()));
		this.popupTache.add(new JMenuItem("Prédécesseurs : " + (tache.getPredecesseurString() != null ? tache.getPredecesseurString() : "")));
		this.popupTache.addSeparator();
		this.popupTache.add(new JMenuItem("Date au plus tôt : " + tache.getDateMin()));
		this.popupTache.add(new JMenuItem("Date au plus tard : " + tache.getDateMax()));
		this.popupTache.show(this, x, y + 10);
	}

	private class GereSouris extends MouseAdapter 
	{
		public void mousePressed(MouseEvent e) 
		{
			for (Map.Entry<Tache, Point> entree : getMap().entrySet()) 
			{
				Point point = entree.getValue();

				Rectangle rect = new Rectangle(point.x + 60, point.y + 40, 120, 80);

				if (rect.possede(e.getX(), e.getY())) 
				{
					PanelGraphe.this.tacheSelectionnee = entree.getKey();

					decalage = new Point(e.getX() - point.x, e.getY() - point.y);

					break;
				}
			}
		}

		public void mouseReleased(MouseEvent e) 
		{
			PanelGraphe.this.tacheSelectionnee = null;
			decalage = null;

			PanelGraphe.this.ctrl.sauvegarderEtatAvantModification();
		}

		public void mouseDragged(MouseEvent e) 
		{
			if (PanelGraphe.this.tacheSelectionnee != null && decalage != null) 
			{
				int newX = e.getX() - decalage.x;
				int newY = e.getY() - decalage.y;
				getMap().get(PanelGraphe.this.tacheSelectionnee).setLocation(newX, newY);

				// Met à jour la grille et le fichier
				var model = ctrl.getGrilleDonneesModel();
				for (int i = 0; i < model.getRowCount(); i++) 
				{
					if (PanelGraphe.this.tacheSelectionnee.getNom().equals(model.getValueAt(i, 0))) 
					{
						model.setValueAt(String.valueOf(newX), i, 3); // colonne x
						model.setValueAt(String.valueOf(newY), i, 4); // colonne y
						break;
					}
				}
				repaint();
			}
		}
	}

	private class GereSurvolTache extends MouseMotionAdapter 
	{
		public void mouseMoved(MouseEvent e) 
		{
			Tache tacheTrouvee = getTacheAt(e.getX(), e.getY());
			if (tacheTrouvee != null && tacheTrouvee != tacheSurvolee) 
			{
				tacheSurvolee = tacheTrouvee;
				afficherPopupTache(tacheSurvolee, e.getX(), e.getY());
			} 
			else if (tacheTrouvee == null) 
			{
				popupTache.setVisible(false);
				tacheSurvolee = null;
			}
		}
	}
}