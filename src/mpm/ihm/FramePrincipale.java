package mpm.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.*;
import mpm.Controleur;

/**
 * La classe {@code FramePrincipale} représente la fenêtre principale de l'application de gestion de projet.
 * Elle organise et affiche les différents composants graphiques : barre de menu, boutons, grille des tâches et graphe.
 *
 * Fonctionnalités principales :
 *   Affichage et organisation des composants principaux de l'interface
 *   Centralisation des interactions entre la vue et le contrôleur
 *   Rafraîchissement de l'affichage lors des modifications
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */
public class FramePrincipale extends JFrame
{
	
	/*------------------*/
	/*    Attributs     */
	/*------------------*/
	
	private PanelGrille panelGrille  ;
	private PanelGraphe panelGraphe  ;
	private PanelBouton panelBouton  ;

	private Controleur  ctrl         ;

	/**Constructeur de FramePrincipale.
	 * @param ctrl Permet de faire le lien avec Controleur et donc la class MPM.
	 */
	public FramePrincipale(Controleur ctrl)
	{
		/*-------------------------------*/
		/*         Variables             */
		/*-------------------------------*/


		Dimension   tailleEcran  ;

		int         largeurEcran ;
		int         hauteurEcran ;
  
		Point       p            ;

		JScrollPane sp           ;
		
		this.ctrl = ctrl;

		tailleEcran = getToolkit().getScreenSize();
		largeurEcran = (int)tailleEcran.getWidth();
		hauteurEcran = (int)tailleEcran.getHeight();

		p = new Point( (largeurEcran - 1400) / 2, (hauteurEcran - 800) / 2 );

		this.setTitle("Gestion de Projet");
		this.setLocation(p);
		this.setLayout(new BorderLayout());
		this.setSize(1400, 800);

		/* ----------------------  */
		/* Création des composants */
		/* ----------------------  */

		this.setJMenuBar( new MaBarreMenu(this.ctrl));
		this.panelBouton = new PanelBouton(this.ctrl);

		this.panelGrille = new PanelGrille(this.ctrl);
		this.panelGrille.setPreferredSize(new Dimension(1400, 325));

		this.panelGraphe = new PanelGraphe(this.ctrl);
		this.panelGraphe.setPreferredSize(new Dimension(2800, 500));

		sp = new JScrollPane( this.panelGraphe );

		/* ----------------------------  */
		/* Positionnement des composants */
		/* ----------------------------  */

		this.add( this.panelBouton, BorderLayout.NORTH  );
		this.add( this.panelGrille, BorderLayout.SOUTH  );
		this.add( sp, BorderLayout.CENTER          );

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible( true );
	}

	/*-----------------*/
	/*   Accesseurs    */
	/*-----------------*/

	/**Méthode qui sert à récupérer le panelBouton.
	 * @return Retourne le panelBouton de la FramePrincipale.
	 */
	public PanelBouton getPanelBouton() { return this.panelBouton; }

	/**Méthode qui sert à récupérer le panelGraphe.
	 * @return Retourne le panelGraphe de la FramePrincipale.
	 */
	public PanelGraphe getPanelGraphe() { return this.panelGraphe; }

	/**Méthode qui sert à récupérer le panelGrille.
	 * @return Retourne le panelGrille de la FramePrincipale.
	 */
	public PanelGrille getPanelGrille() { return this.panelGrille; }

	/**Méthode qui sert à rafraichir la FramePrincipale.
	 */
	public void rafraichirAffichage()
	{
		this.panelGrille.rafraichirModele();
		this.panelGrille.repaint();
		this.panelGraphe.recalculerPositions();
		this.panelGraphe.repaint();
	}



}
