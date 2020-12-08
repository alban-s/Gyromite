package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import modele.deplacements.ColonneManager;
import modele.deplacements.Controle4Directions;
import modele.deplacements.Direction;
import modele.deplacements.color;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 */
public class VueControleurGyromite extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoSol;
    private ImageIcon icoColonneRouge;
    private ImageIcon icoColonneBleu;
    private ImageIcon icoTNT;
    private ImageIcon icoRope;


    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleurGyromite(Jeu _jeu) {
        sizeX = _jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : Controle4Directions.getInstance().setDirectionCourante(Direction.droite); break;
                    case KeyEvent.VK_DOWN : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_S : ColonneManager.getInstance().trigger(color.rouge); break;
                    case KeyEvent.VK_D : ColonneManager.getInstance().trigger(color.bleu); break;

                }
            }
        });
    }


    private void chargerLesIcones() {
        icoHero = chargerIcone("Images/Pacman.png");
        icoVide = chargerIcone("Images/bg_64.png");
        icoColonneRouge = chargerIcone("Images/pipe-middle_64.png");
        icoColonneBleu = chargerIcone("Images/pipe_blue.png");
        icoMur = chargerIcone("Images/wall_64.png");
        icoSol = chargerIcone("Images/ground_64.png");
        icoRope = chargerIcone("Images/rope_64.png");
        icoTNT =  chargerIcone("Images/bricksx64.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setSize(64*jeu.SIZE_X, 64*(jeu.SIZE_Y+1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY+1, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY+1];

        for (int y = 0; y < sizeY+1; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Heros) {
                    tabJLabel[x][y+1].setIcon(icoHero);
                } else if (jeu.getGrille()[x][y] instanceof Enemy) {
                    tabJLabel[x][y+1].setIcon(icoHero);
                } else if (jeu.getGrille()[x][y] instanceof Sol) {
                    tabJLabel[x][y+1].setIcon(icoSol);
                } else if (jeu.getGrille()[x][y] instanceof Mur) {
                    tabJLabel[x][y+1].setIcon(icoMur);
                } else if (jeu.getGrille()[x][y] instanceof Colonne) {
                    if (((Colonne) jeu.getGrille()[x][y]).col == color.rouge){
                        tabJLabel[x][y+1].setIcon(icoColonneRouge);
                    }
                    else {
                        tabJLabel[x][y+1].setIcon(icoColonneBleu);
                    }
                } else if (jeu.getGrille()[x][y] instanceof Corde) {
                    tabJLabel[x][y+1].setIcon(icoRope);
                } else if (jeu.getGrille()[x][y] instanceof Tnt) {
                    tabJLabel[x][y+1].setIcon(icoTNT);
                } else {
                    tabJLabel[x][y+1].setIcon(icoVide);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
