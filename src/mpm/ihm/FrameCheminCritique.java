package mpm.ihm;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import mpm.Controleur;
import mpm.metier.Tache;

/**
 * La classe {@code FrameCheminCritique} affiche dans une fenêtre les différents chemins critiques calculés pour le projet.
 * Elle permet de visualiser, sous forme textuelle, tous les chemins critiques trouvés par l'application.
 *
 * Fonctionnalités principales :
 *   Affichage des chemins critiques dans une fenêtre dédiée
 *   Présentation des tâches composant chaque chemin critique
 *   Gestion de l'absence de chemin critique
 *
 * @author Dumont Enzo, El-Maaddi Yassine, Gricourt Paul, Sefil-Amouret Matys
 */
public class FrameCheminCritique extends JFrame
{
	/*------------------*/
	/*    Attributs     */
	/*------------------*/

	private Controleur ctrl;

	/**Constructeur de FrameCheminCritique.
	 * @param ctrl Permet de faire le lien avec Controleur et donc la class MPM.
	 */
	public FrameCheminCritique(Controleur ctrl) 
	{
		
		/*-------------------------------*/
		/*         Variables             */
		/*-------------------------------*/

		List<List<Tache>> chemins   ;
		String            affichage ;
		int               i         ;
		
		this.ctrl = ctrl;

		this.setTitle("Chemin critique");
		this.setSize(300, 300);

		chemins = this.ctrl.calculerCheminCritique();

		affichage = "<html>";

		if (chemins.isEmpty())
			affichage += "Aucun chemin critique trouvé.";
		else {
			i = 1;
			for (List<Tache> chemin : chemins) {
				affichage += "Chemin critique " + i + " : " + "{ ";
				for (int j = 0; j < chemin.size(); j++) {
					Tache t = chemin.get(j);

					if (!t.getNom().equals("Début") && !t.getNom().equals("Fin"))
						affichage += t.getNom() + " ; ";
				}
				affichage += " } " + "<br>";
				i++;
			}
		}
		affichage += "</html>";

		this.add(new JLabel(affichage));

		this.setVisible(true);
	}

}
