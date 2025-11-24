package mpm.metier.figure;

/**
 * La classe abstraite {@code Figure} définit la structure de base pour représenter des figures géométriques.
 * Elle gère la position, la taille et les opérations de déplacement des figures, et impose l'implémentation d'une méthode de test d'appartenance.
 *
 * Fonctionnalités principales :
 *   Gestion de la position centrale et des dimensions de la figure
 *   Accès et modification des coordonnées et tailles
 *   Déplacement horizontal et vertical de la figure
 *   Définition abstraite de la méthode d'appartenance à la figure (possede)
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */

public abstract class Figure
{
	
	/*------------------*/
	/*    Attributs     */
	/*------------------*/
	
	private int centreX;
	private int centreY;
	private int tailleX;
	private int tailleY;

	/*------------------------------------------*/
	/*              Constructeur                */
	/*------------------------------------------*/

	/**Construit une figure avec une position centrale et des dimensions données.
	 *
	 * @param centreX la coordonnée x du centre de la figure
	 * @param centreY la coordonnée y du centre de la figure
	 * @param tailleX la longueur (dimension horizontale) de la figure
	 * @param tailleY la largeur (dimension verticale) de la figure
	 */
	public Figure(int centreX, int centreY, int tailleX, int tailleY) 
	{
		this.centreX = centreX;
		this.centreY = centreY;
		this.tailleX = tailleX;
		this.tailleY = tailleY;
	}

	/*------------------------------------------*/
	/*               Accesseurs                 */
	/*------------------------------------------*/

	/**Retourne la coordonnée x du centre de la figure.
	 * @return la position du centre sur l'axe X
	 */
	public int getCentreX() { return this.centreX; }

	/**Retourne la coordonnée y du centre de la figure.
	 * @return la position du centre sur l'axe Y
	 */
	public int getCentreY() { return this.centreY; }

	/**Retourne la longueur (dimension horizontale) de la figure.
	 * @return la longueur de la figure (axe X)
	 */
	public int getTailleX() { return this.tailleX; }

	/**Retourne la largeur (dimension verticale) de la figure.
	 * @return la largeur de la figure (axe Y)
	 */
	public int getTailleY() { return this.tailleY; }

	/*------------------------------------------*/
	/*               Modificateurs              */
	/*------------------------------------------*/

	/**Modifie la longueur (dimension horizontale) de la figure.
	 * @param x la nouvelle valeur de la longueur (axe X) de la figure
	 */
	void setTailleX      (int x) { this.tailleX = x; }

	/**Modifie la largeur (dimension verticale) de la figure.
	 * @param y la nouvelle valeur de la largeur (axe Y) de la figure
	 */
	void setTailleY      (int y) { this.tailleY = y;  }

	/*------------------------------------------*/
	/*    Fonctionnalités de déplacer           */
	/*------------------------------------------*/

	/**Déplace la figure horizontalement de la valeur spécifiée.
	 * @param x le déplacement à appliquer sur l'axe X (positif vers la droite, négatif vers la gauche)
	 */
	void deplacerX       (int x)  { this.centreX += x; }

	/**Déplace la figure verticalement de la valeur spécifiée.
	 * @param y le déplacement à appliquer sur l'axe Y (positif vers le bas, négatif vers le haut)
	 */
	void deplacerY       (int y)  { this.centreY += y; }

	/**
	 * Détermine si le point de coordonnées (x, y) appartient à la figure.
	 *
	 * @param x abscisse du point à tester
	 * @param y ordonnée du point à tester
	 * @return true si le point appartient à la figure, false sinon
	 */
	abstract boolean possede(int x, int y);
}
