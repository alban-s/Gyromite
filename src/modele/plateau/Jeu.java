/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.*;

import java.awt.Point;
import java.util.HashMap;

/**
 * Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu {

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;
    int nbr_tnt=0;

    private HashMap<Entite, Point> map = new HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    private Entite[][] grilleOriginal = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu() {
        initialisationDesEntites();
    }

    public void victoire(){
        System.out.print("gagné!!");
        //recommencer();
    }
    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void removeEntite(Entite e){
        int x=map.get(e).x;
        int y=map.get(e).y;
        grilleEntites[x][y] = null;
        if (e instanceof EntiteStatique) {
            grilleOriginal[x][y] = null;
        }
        if(e instanceof Tnt)
            nbr_tnt=nbr_tnt - 1;
        map.remove(e);
        if(nbr_tnt == 0)
            victoire();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }

    public void genererNiveauRandom() {
        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addEntite(new Sol(this), x, 0);
            addEntite(new Sol(this), x, 9);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), 19, y);
        }

        // 3 étages : y = 9, 6 ,3;
        // étage 2 :
        double f1Ran = (Math.random() * 15) + 1;
        for (int i = 0; i < f1Ran - 1; i++) {
            addEntite(new Sol(this), i, 6);
        }
        double f1MiddleHole = (Math.random() * 0) + 3;
        for (int i = (int) (f1Ran + f1MiddleHole); i < 20; i++) {
            addEntite(new Sol(this), i, 6);
        }


        //étage 3
        double f2Ran = (Math.random() * 8) + 1;
        for (int i = 0; i < f2Ran - 1; i++) {
            addEntite(new Sol(this), i, 3);
        }
        double f2MiddleHole1 = (Math.random() * 3) + 2;
        double f2MiddleHole2 = (Math.random() * 6) + 2;
        for (int i = (int) (f2Ran + f2MiddleHole1); i < 20 - f2MiddleHole2; i++) {
            addEntite(new Sol(this), i, 3);
        }

        if (f2Ran > 3){
            addEntite(new Tnt(this),1,2);
            nbr_tnt= nbr_tnt + 1;

        }
        if ((int) (f2Ran + f2MiddleHole1 + f2MiddleHole2) <20 &&
                (grilleOriginal[(int) (f2Ran + f2MiddleHole1 + f2MiddleHole2)][3] instanceof Sol)){
            addEntite(new Tnt(this),(int) (f2Ran + f2MiddleHole1 + f2MiddleHole2),2);
            nbr_tnt= nbr_tnt + 1;
        }


        if (grilleOriginal[(int)(f2Ran)][6] instanceof Sol){
            for (int i = 0; i < 5; i++) {
                addEntite(new Corde(this), (int)(f2Ran), 1+i);
            }
        }
        else {
            color sel;
            if (Math.random() > 0.5){
                sel = color.rouge;
            }
            else {
                sel =color.bleu;
            }
            for (int i = 0; i < 3; i++) {
                Colonne col = new Colonne(this,sel);
                addEntite(col, (int)(f2Ran), 1+i);
                ColonneManager.getInstance().addEntiteDynamique(col);
            }
        }

        int minH=4;
        if (!(grilleOriginal[(int)(f1Ran)][3] instanceof Sol)){
            minH =1;
        }
        for (int i = 0; i < 5 + (4-minH); i++) {
            addEntite(new Corde(this), (int)(f1Ran), minH+i);
        }

        if (!(grilleOriginal[(int)(f1Ran + f1MiddleHole)][3] instanceof Sol)){
            minH =1;
        }
        for (int i = 0; i < 5 + (4-minH); i++) {
            addEntite(new Corde(this), (int)((f1Ran + f1MiddleHole)), minH+i);
        }

        if (f1Ran > 5 && f2Ran > 3) {
            for (int i = 0; i < 5; i++) {
                addEntite(new Corde(this), 3, 4+i);
            }
        }

        if (grilleOriginal[(int)(f2Ran+ f2MiddleHole1)][6] instanceof Sol){
            for (int i = 0; i < 5; i++) {
                addEntite(new Corde(this), (int)(f2Ran+ f2MiddleHole1), 1+i);
            }
        }
        else {
            color sel;
            if (Math.random() > 0.5){
                sel = color.rouge;
            }
            else {
                sel =color.bleu;
            }
            for (int i = 0; i < 3; i++) {
                Colonne col = new Colonne(this,sel);
                addEntite(col, (int)(f2Ran+ f2MiddleHole1), 1+i);
                ColonneManager.getInstance().addEntiteDynamique(col);
            }
        }



        hector = new Heros(this);
        addEntite(hector, 2, 2);

        Gravite.getInstance().addEntiteDynamique(hector);
        Controle4Directions.getInstance().addEntiteDynamique(hector);

        addEntite(new Tnt(this),18,8);
        nbr_tnt= nbr_tnt + 1;


        double max = Math.random() * 3 + 1;
        Enemy[] enemyArray = new Enemy[5];
        for (int i = 0; i < 2; i++) {
            enemyArray[i] = new Enemy(this);
            addEntite(enemyArray[i], (int) (Math.random() * 16 + 2), 8);
            Gravite.getInstance().addEntiteDynamique(enemyArray[i]);
            IA.getInstance().addEntiteDynamique(enemyArray[i]);
        }

        color sel;
        if (Math.random() > 0.5){
            sel = color.rouge;
        }
        else {
            sel =color.bleu;
        }
        int offset = (int) (Math.random()*2) +2;
        grilleOriginal[(int)(f1Ran-offset)][6] = null;
        if ((int)(f1Ran-offset) >2){
            for (int i = 0; i < 3; i++) {
                Colonne col = new Colonne(this,sel);
                addEntite(col, (int)(f1Ran-offset), 6+i);
                ColonneManager.getInstance().addEntiteDynamique(col);
            }
        }



        if (Math.random() > 0.5){
            sel = color.rouge;
        }
        else {
            sel =color.bleu;
        }
        int offset2 = (int) (Math.random()*2) +2;
        grilleOriginal[(int)(f1Ran-offset2)][3] = null;
        for (int i = 0; i < 3; i++) {
            Colonne col = new Colonne(this,sel);
            addEntite(col, (int)(f2Ran+ f2MiddleHole1+ f2MiddleHole2+1), 3+i);
            ColonneManager.getInstance().addEntiteDynamique(col);
        }


    }


    public void genererNiveau() {
        hector = new Heros(this);
        addEntite(hector, 2, 1);

        Gravite.getInstance().addEntiteDynamique(hector);
        Controle4Directions.getInstance().addEntiteDynamique(hector);

        Enemy[] enemyArray = new Enemy[1];
        enemyArray[0] = new Enemy(this);
        addEntite(enemyArray[0], 8, 6);
        Gravite.getInstance().addEntiteDynamique(enemyArray[0]);
        IA.getInstance().addEntiteDynamique(enemyArray[0]);

        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addEntite(new Sol(this), x, 0);
            addEntite(new Sol(this), x, 9);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), 19, y);
        }

        addEntite(new Sol(this), 2, 6);
        addEntite(new Sol(this), 3, 6);
        addEntite(new Sol(this), 2, 3);
        addEntite(new Sol(this), 1, 3);

        //Colonne col = new Colonne(this,color.rouge);
        Colonne[] colT = new Colonne[3];
        Colonne[] colTB = new Colonne[3];
        for (int i = 0; i < 3; i++) {
            colT[i] = new Colonne(this, color.rouge);
            addEntite(colT[i], 1, 6 + i);
            ColonneManager.getInstance().addEntiteDynamique(colT[i]);
        }

        for (int i = 0; i < 3; i++) {
            colTB[i] = new Colonne(this, color.bleu);
            addEntite(colTB[i], 7, 6 + i);
            ColonneManager.getInstance().addEntiteDynamique(colTB[i]);
        }

        //addEntite(col,1,7);
        //addEntite(col,1,8);
        //addEntite(new Colonne(this, color.rouge),1,6);
        //addEntite(new Colonne(this, color.rouge),1,5);
        //addEntite(new Colonne(this, color.rouge),1,4);

        for (int i = 1; i < 9; i++) {
            addEntite(new Corde(this), 4, i);
        }
    }


    public void recommencer() {
        HashMap<Entite, Point> map = new HashMap<Entite, Point>();
        grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
        grilleOriginal = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
        //Ordonnanceur ordonnanceur = new Ordonnanceur(this);
        ColonneManager.getInstance().lstEntitesDynamiques.clear();
        Gravite.getInstance().lstEntitesDynamiques.clear();
        Controle4Directions.getInstance().lstEntitesDynamiques.clear();
        IA.getInstance().lstEntitesDynamiques.clear();
        map.clear();
        genererNiveauRandom();
        //start(300);
    }

    public Entite[][] getGrille() {
        return grilleEntites;
    }

    public Heros getHector() {
        return hector;
    }

    private void initialisationDesEntites() {
        ordonnanceur.add(Gravite.getInstance());
        ordonnanceur.add(Controle4Directions.getInstance());
        ordonnanceur.add(IA.getInstance());
        //modele.deplacements.Colonne pipeManager = new modele.deplacements.Colonne();
        ordonnanceur.add(ColonneManager.getInstance());

        genererNiveauRandom();
    }

    private void addEntite(Entite e, int x, int y) {
        grilleEntites[x][y] = null;
        grilleOriginal[x][y] = null;
        grilleEntites[x][y] = e;
        if (e instanceof EntiteStatique) {
            grilleOriginal[x][y] = e;
        }
        map.put(e, new Point(x, y));
    }


    /**
     * Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }

    /**
     * Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;

        Point pCourant = map.get(e);

        Point pCible = calculerPointCible(pCourant, d);

        if (contenuDansGrille(pCible) && (objetALaPosition(pCible) == null || objetALaPosition(pCible) instanceof Corde || objetALaPosition(pCible) instanceof Tnt)) { // a adapter (collisions murs, etc.)
            // compter le déplacement : 1 deplacement horizontal et vertical max par pas de temps par entité
            switch (d) {
                case bas, haut:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);
                        retour = true;
                    }
                    break;
                case gauche, droite:
                    if (cmptDeplH.get(e) == null && cmptDeplV.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;
                    }
                    break;
            }
        }
        if (retour) {
            deplacerEntite(pCourant, pCible, e);
            if(e instanceof Tnt)
                removeEntite(e);

        }

        return retour;
    }


    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;

        switch (d) {
            case haut:
                pCible = new Point(pCourant.x, pCourant.y - 1);
                break;
            case bas:
                pCible = new Point(pCourant.x, pCourant.y + 1);
                break;
            case gauche:
                pCible = new Point(pCourant.x - 1, pCourant.y);
                break;
            case droite:
                pCible = new Point(pCourant.x + 1, pCourant.y);
                break;
            case basGauche:
                pCible = new Point(pCourant.x - 1, pCourant.y + 1);
                break;
            case basDroite:
                pCible = new Point(pCourant.x + 1, pCourant.y + 1);
                break;

        }

        return pCible;
    }

    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = grilleOriginal[pCourant.x][pCourant.y];
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }

    /**
     * Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Entite objetALaPosition(Point p) {
        Entite retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }

        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
}
