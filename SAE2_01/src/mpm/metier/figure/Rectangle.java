package mpm.metier.figure;


/**
 * La classe {@code Rectangle} représente une figure rectangulaire centrée sur un point donné.
 * Elle hérite de {@code Figure} et implémente la méthode d'appartenance pour déterminer si un point est à l'intérieur du rectangle.
 *
 * Fonctionnalités principales :
 *   Construction d'un rectangle à partir de son centre et de ses dimensions
 *   Détermination de l'appartenance d'un point au rectangle
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */
public class Rectangle extends Figure
{
	/**Constructeur de la classe Rectangle.
	 * @param centreX centre dans l'axe X.
	 * @param centrY  centre dans l'axe Y.
	 * @param tailleX longueur du rectangle.
	 * @param tailleY largeur du rectangle.
	 */
	public Rectangle ( int centreX, int centreY, int tailleX, int tailleY )
	{
		super(centreX, centreY, tailleX, tailleY);
	}

	/**Sert à savoir si un point (x,y) est dans le rectangle.
	 * @param x coordonnée X du point.
	 * @param y coordonnée Y du point.
	 * 
	 * @return true si le point est dans le rectangle, sinon false.
	 */
	public boolean possede( int x, int y )
	{
		int demiLargeur = this.getTailleX() / 2, demiHauteur = this.getTailleY() / 2;

		return  x >= this.getCentreX()-demiLargeur  &&  x <= this.getCentreX()+demiLargeur     &&
				y >= this.getCentreY()-demiHauteur  &&  y <= this.getCentreY()+demiHauteur;
	}
}