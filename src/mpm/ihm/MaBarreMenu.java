package mpm.ihm;

import java.awt.event.*;
import javax.swing.*;
import mpm.Controleur;

/**
 * La classe {@code MaBarreMenu} représente la barre de menus principale de l'application.
 * Elle gère la création, l'organisation et les actions des menus (Fichier, Edition, Ajouter, Supprimer).
 *
 * Fonctionnalités principales :
 *   Création et organisation des menus et de leurs items
 *   Gestion des raccourcis clavier pour les actions courantes
 *   Traitement des actions utilisateur sur les menus (nouveau, ouvrir, enregistrer, annuler, copier, coller, etc.)
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */
public class MaBarreMenu extends JMenuBar implements ActionListener
{
	
	/*------------------*/
	/*    Attributs     */
	/*------------------*/

	private JMenuItem     menuiFichierNouveau;
	private JMenuItem     menuiFichierOuvrir;
	private JMenuItem     menuiFichierRafraichir;
	private JMenuItem     menuiFichierQuitter;
	private JMenuItem     menuiFichierEnregistrer;
	private JMenuItem     menuiFichierEnregistrerSous;

	private JMenuItem     menuiEditionAnnuler;
	private JMenuItem     menuiEditionRefaire;
	private JMenuItem     menuiEditionCopier;
	private JMenuItem     menuiEditionColler;

	private JMenuItem     menuiAjouterNouvelleTache;
	private JMenuItem     menuiSupprimerSupprimerTache;

	private Controleur    ctrl;
	
	private Object[] tacheCopiee;

	/**Constructeur de MaBarreMenu.
	 * @param ctrl Permet de faire le lien avec Controleur et donc la class MPM.
	 */
	public MaBarreMenu(Controleur ctrl)
	{
		this.ctrl = ctrl;

		/*----------------------------*/
		/* Création des composants    */
		/*----------------------------*/

		// les JMenu
		JMenu menuFichier   = new JMenu("Fichier");
		JMenu menuEdition   = new JMenu("Edition");
		JMenu menuAjouter   = new JMenu("Ajouter");
		JMenu menuSupprimer = new JMenu("Supprimer");

		this.menuiFichierNouveau         = new JMenuItem ("Nouveau"         );
		this.menuiFichierOuvrir          = new JMenuItem ("Ouvrir"          );
		this.menuiFichierRafraichir      = new JMenuItem ("Rafraîchir"      );
		this.menuiFichierEnregistrer     = new JMenuItem ("Enregistrer"     );
		this.menuiFichierEnregistrerSous = new JMenuItem ("Enregistrer sous");
		this.menuiFichierQuitter         = new JMenuItem ("Quitter"         );

		// les items du menu edition
		this.menuiEditionAnnuler = new JMenuItem ("Annuler" );
		this.menuiEditionRefaire = new JMenuItem ("Refaire" );
		this.menuiEditionCopier  = new JMenuItem ("Copier"  );
		this.menuiEditionColler  = new JMenuItem ("Coller"  );

		// les items du menu recherche
		this.menuiAjouterNouvelleTache    = new JMenuItem ("Ajouter une tâche"   );
		this.menuiSupprimerSupprimerTache = new JMenuItem ("Supprimer une tâche" );



		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/

		// des JItemMenu dans les JMenu
		menuFichier.add( this.menuiFichierNouveau    );
		menuFichier.add( this.menuiFichierOuvrir     );
		menuFichier.add( this.menuiFichierRafraichir );
		menuFichier.addSeparator();
		menuFichier.add( this.menuiFichierEnregistrer     );
		menuFichier.add( this.menuiFichierEnregistrerSous );
		menuFichier.addSeparator();
		menuFichier.add( this.menuiFichierQuitter );

		menuEdition.add( this.menuiEditionAnnuler );
		menuEdition.add( this.menuiEditionRefaire );
		menuEdition.addSeparator();
		menuEdition.add( this.menuiEditionCopier );
		menuEdition.add( this.menuiEditionColler );

		menuAjouter.add  ( this.menuiAjouterNouvelleTache    );
		menuSupprimer.add( this.menuiSupprimerSupprimerTache );

		// Des JMenu dans la JMenuBar
		this.add( menuFichier );
		this.add( menuEdition );
		this.add( menuAjouter );
		this.add( menuSupprimer );

		/*-------------------------------*/
		/* Activation des composants     */
		/*-------------------------------*/

		// Création des raccourcis clavier
		this.menuiFichierNouveau          .addActionListener ( this );
		this.menuiFichierOuvrir           .addActionListener ( this );
		this.menuiFichierRafraichir       .addActionListener ( this );
		this.menuiFichierEnregistrer      .addActionListener ( this );
		this.menuiFichierEnregistrerSous  .addActionListener ( this );
		this.menuiFichierQuitter          .addActionListener ( this );
		this.menuiEditionAnnuler          .addActionListener ( this );
		this.menuiEditionColler           .addActionListener ( this );
		this.menuiEditionCopier           .addActionListener ( this );
		this.menuiEditionRefaire          .addActionListener ( this );
		this.menuiAjouterNouvelleTache    .addActionListener ( this );
		this.menuiSupprimerSupprimerTache .addActionListener ( this );

		this.menuiFichierEnregistrer      .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK                              ));  // pour CTRL+S
		this.menuiFichierEnregistrerSous  .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK ));  // pour CTRL+SHIFT+S
		
		this.menuiFichierRafraichir       .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK ));  // pour CTRL+R
		this.menuiFichierQuitter          .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_F4,InputEvent.ALT_DOWN_MASK  ));  // pour ALT+F4
		this.menuiFichierNouveau          .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK ));  // pour CTRL+N
		this.menuiFichierOuvrir           .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK ));  // pour CTRL+O

		this.menuiEditionAnnuler          .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK ));  // pour CTRL+K
		this.menuiEditionRefaire          .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK ));  // pour CTRL+Y
		this.menuiEditionCopier           .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK ));  // pour CTRL+C
		this.menuiEditionColler           .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK ));  // pour CTRL+P

		this.menuiAjouterNouvelleTache    .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK ));  // pour CTRL+SHIFT+N
		this.menuiSupprimerSupprimerTache .setAccelerator ( KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK                         ));  // pour CTRL+SUPPR

	}

	/**Gère les actions des différents éléments de la barre de menu.
	 * Selon la source de l'événement (JMenuItem), cette méthode effectue l'action appropriée .
	 * @param e l'événement d'action déclenché par un élément du menu
	*/
	public void actionPerformed ( ActionEvent e )
	{
		
		/*-------------------------------*/
		/*         Variables             */
		/*-------------------------------*/

		JFileChooser fichierChoisi, fichierASauvegarder ;
		
		String       choix, nomOriginal, nouveauNom, nouveauFichier, fichier ;
		String       prec, dureeStr, nvlDuree, nomASupprimer                 ;   
		String       nvX, nom, nvY, nomTache, x, y, predecesseurs            ; 
 
		int          retour,yOriginal,duree                                  ;
		int          ligneSelectionne,ligneInseree                           ;

		boolean      existe                                                  ;

		JTable       table                                                   ;

		String[]     preds                                                   ;
		
		if ( e.getSource() instanceof JMenuItem )
		{
			switch (( (JMenuItem) e.getSource() ).getText())
			{
				case "Nouveau" :
					// ouvrir une nouvelle fenêtre
					new Controleur();
					break;

				case "Ouvrir" :
					// ouvrir un fichier existant
					try
					{
						fichierChoisi = new JFileChooser("../data");
						fichierChoisi.showOpenDialog(this);
						choix = (fichierChoisi.getSelectedFile() != null) ? fichierChoisi.getSelectedFile().getAbsolutePath() : null;

						if (choix == null || choix.isBlank())
							throw new Exception("Aucun fichier sélectionné.");

						this.ctrl.chargerTachesDepuisFichier(choix);
						this.ctrl.getGrilleDonneesModel().setFichier(choix);
						this.ctrl.rafraichirAffichage();
						JOptionPane.showMessageDialog(this, "Données chargées depuis : " + choix, "Ouvrir", JOptionPane.INFORMATION_MESSAGE);
					} 
					catch (Exception ex) 
					{
						JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
					}
				break;

				case "Rafraîchir" :
					// Rafraîchir la fenêtre actuelle
					this.ctrl.rafraichirAffichage();
					JOptionPane.showMessageDialog(this, "Rafraîchir la fenêtre actuelle.");
					break;

				case "Enregistrer" :
					// Enregistre dans le fichier courant, ou demande un fichier si aucun n'est défini
					try
					{
						fichier = this.ctrl.getFichier();
						if (fichier == null || fichier.isBlank()) 
						{
							fichierASauvegarder = new JFileChooser("./");
							retour = fichierASauvegarder.showSaveDialog(this);
							if (retour == JFileChooser.APPROVE_OPTION) {
								fichier = fichierASauvegarder.getSelectedFile().getAbsolutePath();
								this.ctrl.getGrilleDonneesModel().setFichier(fichier);
							}
							else
							{
								break; // Annulé
							}
						}
						this.ctrl.getGrilleDonneesModel().sauvegarderDansFichier();
						this.ctrl.getGrilleDonneesModel().fireTableDataChanged();
						JOptionPane.showMessageDialog(this, "Données enregistrées dans : " + fichier, "Enregistrer", JOptionPane.INFORMATION_MESSAGE);
					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
					}
					break;

				case "Enregistrer sous" :
					// Ouvre une boîte de dialogue pour choisir un nouveau fichier

					fichierASauvegarder = new JFileChooser("../data");

					retour = fichierASauvegarder.showSaveDialog(this);
					if (retour == JFileChooser.APPROVE_OPTION)
					{
						nouveauFichier = fichierASauvegarder.getSelectedFile().getAbsolutePath();
						this.ctrl.getGrilleDonneesModel().setFichier(nouveauFichier);
						try
						{
							this.ctrl.sauvegarderTachesDansFichier(nouveauFichier); // <-- Ajout de la sauvegarde réelle
							this.ctrl.getGrilleDonneesModel().sauvegarderDansFichier();
							this.ctrl.getGrilleDonneesModel().fireTableDataChanged();
							JOptionPane.showMessageDialog(this, "Données enregistrées sous : " + nouveauFichier, "Enregistrer sous", JOptionPane.INFORMATION_MESSAGE);
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
						}
					}
					break;

				case "Quitter" :
					System.exit(0);
					break;

				case "Annuler" :
					// annuler la dernière action
					this.ctrl.annuler();
					JOptionPane.showMessageDialog(this, "Annuler la dernière action.");
					break;

				case "Refaire" :
					// rétablir la dernière action annulée
					this.ctrl.refaire();
					JOptionPane.showMessageDialog(this, "Rétablir la dernière action annulée.");
					break;

				case "Copier":
					this.ctrl.sauvegarderEtatAvantModification();
					table = this.ctrl.getVue().getPanelGrille().getTable();
					ligneSelectionne = table.getSelectedRow();
					if (ligneSelectionne != -1)
					{
						tacheCopiee = new Object[table.getColumnCount()];
						for (int col = 0; col < table.getColumnCount(); col++)
						{
							tacheCopiee[col] = table.getValueAt(ligneSelectionne, col);
						}
						JOptionPane.showMessageDialog(this, "Tâche copiée !");
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Veuillez sélectionner une tâche à copier.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
					break;

				case "Coller" :
					this.ctrl.sauvegarderEtatAvantModification();
					// coller le contenu du presse-papiers
					table = this.ctrl.getVue().getPanelGrille().getTable();
					ligneInseree = table.getSelectedRow();
					if (tacheCopiee == null)
					{
						JOptionPane.showMessageDialog(this, "Aucune tâche copiée.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
						break;
					}
					if (ligneInseree == -1)
					{
						JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne où coller.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
						break;
					}

					// Crée une copie avec un nom unique
					nomOriginal = tacheCopiee[0].toString();
					nouveauNom = nomOriginal + "_copie";

					// Vérifie l'unicité du nom
					existe = false;
					for (int i = 0; i < table.getRowCount(); i++)
					{
						if (table.getValueAt(i, 0).toString().equals(nouveauNom))
						{
							existe = true;
							break;
						}
					}
					if (existe)
					{
						nouveauNom = nomOriginal + "_copie" + System.currentTimeMillis();
					}

					nvlDuree = tacheCopiee[1] != null ? tacheCopiee[1].toString() : "1";
					prec     = tacheCopiee[2] != null ? tacheCopiee[2].toString() : "";
					nvX      = tacheCopiee[3] != null ? tacheCopiee[3].toString() : "0";
					yOriginal = 0;
					try
					{
						yOriginal = tacheCopiee[4] != null ? Integer.parseInt(tacheCopiee[4].toString()) : 0;
					}
					catch (Exception ex)
					{
						yOriginal = 0;
					}
					nvY = Integer.toString(yOriginal + 150);

					this.ctrl.ajouterTache(nouveauNom, Integer.parseInt(nvlDuree), prec, nvX, nvY);
					this.ctrl.rafraichirAffichage();
					this.ctrl.getGrilleDonneesModel().fireTableDataChanged();
					JOptionPane.showMessageDialog(this, "Tâche collée sous le nom : " + nouveauNom);
					break;

				case "Ajouter une tâche":
					nom = JOptionPane.showInputDialog(this, "Nom de la tâche :");
					if (nom == null || nom.isBlank()) return;

					// Vérification que le nom n'est pas déjà utilisé par une autre tâche
					for (int i = 0; i < this.ctrl.getGrilleDonneesModel().getRowCount(); i++)
					{
						nomTache = this.ctrl.getGrilleDonneesModel().getValueAt(i, 0).toString();

						if (nomTache.equals(nom))
						{
							JOptionPane.showMessageDialog(this, "Une tâche avec ce nom existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}

					dureeStr = JOptionPane.showInputDialog(this, "Durée de la tâche (en jours) :");
					if (dureeStr == null || dureeStr.isBlank()) return;

					try 
					{
						duree = Integer.parseInt(dureeStr);
						if (duree < 0) 
						{
							JOptionPane.showMessageDialog(this, "La durée d'une tâche ne peut pas être négative.", "Erreur", JOptionPane.ERROR_MESSAGE);
							return;
						}
					} 
					catch (NumberFormatException ex) 
					{
						JOptionPane.showMessageDialog(this, "La durée doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					x = JOptionPane.showInputDialog(this, "Position X de la tâche :", "0");
					if (x == null || !x.matches("\\d+")) x = "0";

					y = JOptionPane.showInputDialog(this, "Position Y de la tâche :", "0");
					if (y == null || !y.matches("\\d+")) y = "0";

					predecesseurs = JOptionPane.showInputDialog(this, "Prédécesseurs (séparés par des virgules) :", "");
					if (predecesseurs != null && !predecesseurs.isBlank()) 
					{
						preds = predecesseurs.split(",");
						for (String pred : preds) 
						{
							if (nom.equals(pred.trim())) 
							{
								JOptionPane.showMessageDialog(this, "Une tâche ne peut pas être son propre prédécesseur.", "Erreur", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					}

					this.ctrl.ajouterTache(nom, duree, predecesseurs == null ? "" : predecesseurs, x, y);
					this.ctrl.rafraichirAffichage();
					this.ctrl.getGrilleDonneesModel().fireTableDataChanged();
					break;

				case "Supprimer une tâche":
					nomASupprimer = JOptionPane.showInputDialog(this, "Nom de la tâche à supprimer :");
					if (nomASupprimer == null || nomASupprimer.isBlank()) break;

					if ( nomASupprimer.equals("Debut") || nomASupprimer.equals("Fin") )
					{
						JOptionPane.showMessageDialog(this, "Vous ne pouvez pas supprimer les tâches 'Début' ou 'Fin'.", "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					this.ctrl.supprimerTache(nomASupprimer);
					this.ctrl.rafraichirDepuisFichier();
					this.ctrl.rafraichirAffichage();
					break;
			}
		}

	}
}