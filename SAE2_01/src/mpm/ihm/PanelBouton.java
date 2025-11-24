package mpm.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import mpm.Controleur;

/**
 * La classe {@code PanelBouton} représente le panneau des boutons de contrôle de l'application.
 * Elle regroupe les boutons pour le calcul des dates au plus tôt, au plus tard, l'affichage du chemin critique,
 * ainsi que les options d'affichage des dates en jours ou en format réel.
 *
 * Fonctionnalités principales :
 *   Gestion des actions sur les boutons de calcul et d'affichage
 *   Activation/désactivation dynamique des boutons selon l'état du projet
 *   Interaction avec le contrôleur pour déclencher les calculs et mises à jour de l'affichage
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */
public class PanelBouton extends JPanel implements ActionListener, ItemListener 
{

	/*------------------*/
	/* Constantes */
	/*------------------*/

	private final Color COULEUR_R = new Color(159, 168, 218);
	private final Color COULEUR_V = new Color(255, 205, 210);
	private final Color COULEUR_B = new Color(178, 223, 219);

	/*------------------*/
	/* Attributs */
	/*------------------*/

	private Controleur ctrl;

	private JButton btnDteDeb;
	private JButton btnDteFin;
	private JButton btnCheminCrtique;

	private JRadioButton rbJour;
	private JRadioButton rbDate;

	private JPanel panelBtn;
	private JPanel panelRb;

	/**Constructeur de PanelBouton.
	 * @param ctrl Permet de faire le lien avec Controleur et donc la class MPM.
	 */
	public PanelBouton(Controleur ctrl) 
	{
		this.ctrl = ctrl;
		this.setLayout(new FlowLayout());

		ButtonGroup bg ;

		bg = new ButtonGroup();

		/* ----------------------- */
		/* Création des composants */
		/* ----------------------- */
		this.setLayout(new BorderLayout());

		this.panelBtn = new JPanel((new FlowLayout()));
		this.panelRb = new JPanel((new FlowLayout()));

		this.btnDteDeb        = new JButton("Date + tot"     );
		this.btnDteFin        = new JButton("Date + tard"    );
		this.btnCheminCrtique = new JButton("Chemin critique");

		this.btnDteDeb       .setBackground(COULEUR_V);
		this.btnDteFin       .setBackground(COULEUR_R);
		this.btnCheminCrtique.setBackground(COULEUR_B);

		this.rbJour           = new JRadioButton("Jour"       );
		this.rbDate           = new JRadioButton("Date "      );

		bg                   .add(this.rbJour);
		bg                   .add(this.rbDate);

		/* ----------------------------- */
		/* Positionnement des composants */
		/* ----------------------------- */
		this                 .add( this.panelBtn, BorderLayout.CENTER );
		this                 .add( this.panelRb, BorderLayout.EAST    );

		this.panelBtn        .add( this.btnDteDeb        );
		this.panelBtn        .add( this.btnDteFin        );
		this.panelBtn        .add( this.btnCheminCrtique );

		this.panelRb         .add(this.rbJour);
		this.panelRb         .add(this.rbDate);

		this.btnDteDeb       .setEnabled( false );
		this.btnDteFin       .setEnabled( false );
		this.btnCheminCrtique.setEnabled( false );

		/*------------------------------*/
		/*   Activation des composants  */
		/*------------------------------*/

		this.btnDteDeb       .addActionListener( this );
		this.btnDteFin       .addActionListener( this );
		this.btnCheminCrtique.addActionListener( this );

		this.rbJour          .addItemListener( this   );
		this.rbDate          .addItemListener( this   );

	}

	/**Gère les actions des différents boutons.
	 * Selon la source de l'événement, cette méthode effectue l'action appropriée .
	 * @param e l'événement d'action déclenché lorsque l'on clique sur un bouton.
	*/
	public void actionPerformed(ActionEvent e) 
	{
		/*-------------------------------*/
		/*         Variable              */
		/*-------------------------------*/

		boolean encore;
		
		if (e.getSource() == this.btnDteDeb) 
		{
			encore = this.ctrl.calculerDatesAuPlusTotNiveauSuivant();

			// Rafraîchit juste l'affichage, sans recharger les données
			this.ctrl.sauvegarderEtatAvantModification();
			this.ctrl.rafraichirAffichage();

			if (!encore) 
			{
				this.btnDteDeb.setEnabled(false);
				this.btnDteFin.setEnabled(true);
			}
		}

		if (e.getSource() == this.btnDteFin) {
			encore = this.ctrl.calculerDatesAuPlusTardNiveauSuivant();

			// Rafraîchit juste l'affichage, sans recharger les données
			this.ctrl.sauvegarderEtatAvantModification();
			this.ctrl.rafraichirAffichage();

			if ( !encore ) 
			{
				this.btnDteDeb.setEnabled(false);
				this.btnDteFin.setEnabled(false);
				this.btnCheminCrtique.setEnabled(true);
			}
		}

		if ( e.getSource() == this.btnCheminCrtique ) 
		{
			this.ctrl            .afficherCheminCritiqueEtRafraichir();
			this.ctrl            .sauvegarderEtatAvantModification();

			this.ctrl            .getVue().getPanelGraphe().setModeCheminCritique( true  );
			this.btnDteDeb       .setEnabled( false                                      );
			this.btnDteFin       .setEnabled( false                                      );
			this.btnCheminCrtique.setEnabled( false                                      );
		}
	}

	public void itemStateChanged(ItemEvent e) 
	{
		if ( this.rbDate.isSelected() ) 
		{
			this.ctrl.sauvegarderEtatAvantModification();
			this.ctrl.getVue().getPanelGraphe().setModeAffichageDateReelle( true );
		}

		else if ( this.rbJour.isSelected() ) 
		{
			this.ctrl.sauvegarderEtatAvantModification();
			this.ctrl.getVue().getPanelGraphe().setModeAffichageDateReelle( false );
		}
	}

	public void resetBoutons() 
	{
		this.btnDteDeb       .setEnabled( true  );
		this.btnDteFin       .setEnabled( false );
		this.btnCheminCrtique.setEnabled( false );
	}
}
