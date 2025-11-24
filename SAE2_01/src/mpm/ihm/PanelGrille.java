package mpm.ihm;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import mpm.Controleur;


/**
 * La classe {@code PanelGrille} représente le panneau affichant la grille des tâches du projet.
 * Elle permet la visualisation, la sélection et la modification des tâches via un tableau interactif.
 *
 * Fonctionnalités principales :
 *   Affichage et édition des tâches dans une grille
 *   Sélection et modification des propriétés d'une tâche
 *   Synchronisation des modifications avec le contrôleur et le graphe
 *   Vérification de la cohérence des données saisies par l'utilisateur
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */
public class PanelGrille extends JPanel implements ActionListener
{
	
	/*------------------*/
	/*    Attributs     */
	/*------------------*/
	
	//le JTable pour les données et sa scrollPane
	private JTable      tblGrilleDonneesModel;
	private JScrollPane scrollPane;

	//les différents panel pour les différentes interactions
	private JPanel      panelModifier;
	private JPanel      panelNomTache;
	private JPanel      panelDureeTache;
	private JPanel      panelPredecesseurs;
	private JPanel      panelButton;
	private JPanel      panelCoord;

	//les JTextField pour créer les tâches 
	private JTextField  txtNomTache;
	private JTextField  txtDureeTache;
	private JTextField  txtPredecesseurs;
	private JTextField  txtX;
	private JTextField  txtY;

	//Le JButton pour interagir avec la modification
	private JButton     btnModifier;

	//le controleur pour le modele MCV
	private Controleur  ctrl;

	/**Constructeur de PanelGrille.
	 * @param ctrl Permet de faire le lien avec Controleur et donc la class MPM.
	 */
	public PanelGrille(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setLayout(new GridLayout(2, 1) );

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/

		this.panelModifier      = new JPanel();
		this.panelNomTache      = new JPanel(new FlowLayout(FlowLayout.LEFT   ));
		this.panelDureeTache    = new JPanel(new FlowLayout(FlowLayout.LEFT   ));
		this.panelPredecesseurs = new JPanel(new FlowLayout(FlowLayout.LEFT   ));
		this.panelButton        = new JPanel(new FlowLayout(FlowLayout.CENTER ));
		this.panelCoord         = new JPanel(new FlowLayout(FlowLayout.LEFT   ));
		

		this.panelModifier.setLayout( new GridLayout(5, 1) );

		this.txtNomTache        = new JTextField( 20 );
		this.txtDureeTache      = new JTextField( 5  );
		this.txtPredecesseurs   = new JTextField( 20 );

		this.txtX               = new JTextField( 5  );
		this.txtY               = new JTextField( 5  );

		this.btnModifier        = new JButton("Modifier Tâche" );
		
		// Modèle vide au démarrage
		this.tblGrilleDonneesModel = new JTable(new DefaultTableModel(
			new Object[][]{},
			new String[] { "Nom", "Durée", "Tâches Précédentes", "x", "y" }
		));
		this.scrollPane = new JScrollPane(this.tblGrilleDonneesModel);

		// Ajout de l'écouteur de sélection de ligne
		this.tblGrilleDonneesModel.getSelectionModel().addListSelectionListener(new SelectionRemplissageListener());

		/*-----------------------------------*/
		/*   Positionnement des composants   */
		/*-----------------------------------*/

		this.panelNomTache      .add( new JLabel("Nom de la Tâche    : "), FlowLayout.LEFT );
		this.panelNomTache      .add(this.txtNomTache                                      );

		this.panelDureeTache    .add( new JLabel("Durée de la Tâche : "), FlowLayout.LEFT  );
		this.panelDureeTache    .add(this.txtDureeTache                                    );

		this.panelPredecesseurs .add( new JLabel("Prédécesseurs      : "), FlowLayout.LEFT );
		this.panelPredecesseurs .add(this.txtPredecesseurs                                 );

		this.panelCoord         .add( new JLabel("X : "                 ), FlowLayout.LEFT);
		this.panelCoord         .add(this.txtX                                             );
		this.panelCoord         .add( new JLabel("Y : "                 )                  );
		this.panelCoord         .add(this.txtY                                             );

		this.panelButton        .add(this.btnModifier                                      );

		this.panelModifier      .add(this.panelNomTache                                    );
		this.panelModifier      .add(this.panelDureeTache                                  );
		this.panelModifier      .add(this.panelPredecesseurs                               );
		this.panelModifier      .add(this.panelCoord                                       );
		this.panelModifier      .add(this.panelButton                                      );

		this                    .add(this.panelModifier                                    );
		this                    .add(this.scrollPane                                       );

		/*-------------------------------*/
		/*   Activation des composants   */
		/*-------------------------------*/

		this.btnModifier.addActionListener(this);
	}

	/*-------------------------------*/
	/*          Accesseurs           */
	/*-------------------------------*/

	/**Récupère la table de grille données model.
	 * @return Table de la grille donnée model.
	 */
	public JTable getTable() { return this.tblGrilleDonneesModel; }

	/**Gère les actions des différents éléments de la grille.
	 * Selon la source de l'événement, cette méthode effectue l'action appropriée .
	 * @param e l'événement d'action déclenché par bouton le bouton modifier.
	*/
	public void actionPerformed(ActionEvent e)
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		String[] preds        ;
 
		String   nom          ; 
		String   dureeStr     ; 
		String   predecesseurs; 
		String   x, y         ;    
		String   nomTache     ;
		String   ancNom       ;
  
		boolean  existe       ;
  
		int      selectedRow  ;
		int      duree        ;
		int      xInt, yInt   ;

		if (e.getSource() == this.btnModifier)
		{
			selectedRow = this.tblGrilleDonneesModel.getSelectedRow() ;
			if (selectedRow == -1) 
			{
				JOptionPane.showMessageDialog(this, "Veuillez sélectionner une tâche dans le tableau.", "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			ancNom        = String.valueOf(this.ctrl.getGrilleDonneesModel().getValueAt(selectedRow, 0));

			nom           = this.txtNomTache        .getText().trim() ;
			dureeStr      = this.txtDureeTache      .getText().trim() ;

			predecesseurs = this.txtPredecesseurs   .getText().trim() ;
			x             = this.txtX               .getText().trim() ;
			y             = this.txtY               .getText().trim() ;

			// Vérification de la redondance des nom de tache
			for (int i = 0; i < this.ctrl.getGrilleDonneesModel().getRowCount(); i++) 
			{
				if (i != selectedRow) // Ignore la ligne actuellement modifiée
				{
					nomTache = String.valueOf(this.ctrl.getGrilleDonneesModel().getValueAt(i, 0));
					if (nomTache.equals(nom)) 
					{
						JOptionPane.showMessageDialog(this, "Le nom de la tâche existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}

			if (x.isEmpty() || Integer.parseInt(x) < 0)
				x = "0";

			if (y.isEmpty() || Integer.parseInt(y) < 0)
				y = "0";

			if (nom.isEmpty() || dureeStr.isEmpty()) 
			{
				JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Vérification des prédécesseurs
			if (!predecesseurs.isEmpty()) 
			{
				preds = predecesseurs.split(",");
				for (String pred : preds) 
				{
					pred = pred.trim();
					// Vérifie que le prédécesseur existe
					existe = false;
					for (int i = 0; i < this.ctrl.getGrilleDonneesModel().getRowCount(); i++) 
					{
						nomTache = this.ctrl.getGrilleDonneesModel().getValueAt(i, 0).toString();
						if (nomTache.equals(pred)) 
						{
							existe = true;
							break;
						}
					}
					if (!existe) 
					{
						JOptionPane.showMessageDialog(this, "Le prédécesseur \"" + pred + "\" n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					// Vérifie que la tâche n'est pas son propre prédécesseur
					if (nom.equals(pred)) 
					{
						JOptionPane.showMessageDialog(this, "Une tâche ne peut pas être son propre prédécesseur.", "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}

			
			try 
			{
				duree = Integer.parseInt(dureeStr);
			} 
			catch (NumberFormatException ex) 
			{
				JOptionPane.showMessageDialog(this, "La durée doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// --- MODIFICATION POUR METTRE À JOUR LES PRÉDÉCESSEURS ---
			// Si le nom change, on met à jour tous les prédécesseurs qui pointaient vers l'ancien nom
			if (!ancNom.equals(nom)) {
				for (int i = 0; i < this.ctrl.getGrilleDonneesModel().getRowCount(); i++) 
				{
					if (i == selectedRow) continue;
					String precs = String.valueOf(this.ctrl.getGrilleDonneesModel().getValueAt(i, 2));
					if (precs != null && !precs.isEmpty()) 
					{
						String[] arr = precs.split(",");
						boolean modif = false;
						for (int j = 0; j < arr.length; j++) 
						{
							if (arr[j].trim().equals(ancNom)) 
							{
								arr[j] = nom;
								modif = true;
							}
						}
						if (modif) 
						{
							// Reformate la chaîne proprement
							StringBuilder sb = new StringBuilder();
							for (int j = 0; j < arr.length; j++) 
							{
								if (j > 0) sb.append(",");
								sb.append(arr[j].trim());
							}
							this.ctrl.getGrilleDonneesModel().setValueAt(sb.toString(), i, 2);
						}
					}
				}
			}

			this.ctrl.getGrilleDonneesModel().setValueAt( nom, selectedRow, 0                   );
			this.ctrl.getGrilleDonneesModel().setValueAt( String.valueOf(duree), selectedRow, 1 );
			this.ctrl.getGrilleDonneesModel().setValueAt( predecesseurs, selectedRow, 2         );
			this.ctrl.getGrilleDonneesModel().setValueAt( x, selectedRow, 3                     );
			this.ctrl.getGrilleDonneesModel().setValueAt( y, selectedRow, 4                     );

			try
			{
				xInt = Integer.parseInt(x);
				yInt = Integer.parseInt(y);
				this.ctrl.getVue().getPanelGraphe().setPositionTache(nom, xInt, yInt);
			} 
			catch (NumberFormatException ex)
			{
				// Ignore si x ou y ne sont pas des entiers
				ex.printStackTrace();
			}

			this.txtNomTache     .setText("");
			this.txtDureeTache   .setText("");
			this.txtPredecesseurs.setText("");

			// Rafraîchit le modèle de la table
				this.ctrl.getGrilleDonneesModel().fireTableDataChanged();
				this.ctrl.rafraichirDepuisFichier();
				this.ctrl.rafraichirAffichage();

				// Optionnel : afficher un message de succès
				JOptionPane.showMessageDialog(this, "Tâche modifiée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**Raffraichit la grille.
	 */
	public void rafraichirModele() {this.tblGrilleDonneesModel.setModel(this.ctrl.getGrilleDonneesModel());}

	/**Remplit les champs de saisie du panneau de modification avec les valeurs de la tâche sélectionnée dans la grille.
	 * Si aucune ligne n'est sélectionnée, les champs sont vidés.
	 */
	private void remplirChampsDepuisSelection() 
	{
		
		/*-------------------------------*/
		/*           Variables           */
		/*-------------------------------*/

		String nom           ; 
		String duree         ;
		String predecesseurs ;
		String x             ;
		String y             ;

		int selectedRow = this.tblGrilleDonneesModel.getSelectedRow();

		if (selectedRow == -1) 
		{
			this.txtNomTache     .setText("");
			this.txtDureeTache   .setText("");
			this.txtPredecesseurs.setText("");
			return;
		}

		nom           = String.valueOf( this.ctrl.getGrilleDonneesModel().getValueAt(selectedRow, 0) );
		duree         = String.valueOf( this.ctrl.getGrilleDonneesModel().getValueAt(selectedRow, 1) );
		predecesseurs = String.valueOf( this.ctrl.getGrilleDonneesModel().getValueAt(selectedRow, 2) );
		x             = String.valueOf( this.ctrl.getGrilleDonneesModel().getValueAt(selectedRow, 3) );
		y             = String.valueOf( this.ctrl.getGrilleDonneesModel().getValueAt(selectedRow, 4) );

		this.txtNomTache         .setText( nom           );
		this.txtDureeTache       .setText( duree         );
		this.txtPredecesseurs    .setText( predecesseurs );
		this.txtX                .setText( x             );
		this.txtY                .setText( y             );
	}

	/**La classe {@code SelectionRemplissageListener} écoute la sélection de lignes dans la grille des tâches.
	 * Elle permet de remplir automatiquement les champs de saisie avec les informations de la tâche sélectionnée.
	 *
	 * Fonctionnalités principales :
	 *   Détection du changement de sélection dans la JTable
	 *   Remplissage automatique des champs de modification avec les données de la ligne sélectionnée
	 */
	private class SelectionRemplissageListener implements ListSelectionListener 
	{
		public void valueChanged(ListSelectionEvent e) 
		{
			if (!e.getValueIsAdjusting()) 
				remplirChampsDepuisSelection();
		}
	}
	
}