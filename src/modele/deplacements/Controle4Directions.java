package modele.deplacements;

import modele.plateau.Corde;
import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.Tnt;

/**
 * Controle4Directions permet d'appliquer une direction (connexion avec le clavier) à un ensemble d'entités dynamiques
 */
public class Controle4Directions extends RealisateurDeDeplacement {
    private Direction directionCourante;
    // Design pattern singleton
    private static Controle4Directions c3d;

    public static Controle4Directions getInstance() {
        if (c3d == null) {
            c3d = new Controle4Directions();
        }
        return c3d;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null) {
                Entite eBas;
                Entite eTnt;
                boolean active = true;
                switch (directionCourante) {
                    case gauche, droite:
                        eTnt =e.regarderDansLaDirection(directionCourante);
                        if(eTnt instanceof Tnt){
                            if(!(e.jeu.nbr_tnt > 1)){
                                active = false;
                            }
                            ((Tnt) eTnt).kill();


                        }
                        if (active){
                            if (e.avancerDirectionChoisie(directionCourante))
                                ret = true;
                        }
                        break;
                    case haut:
                        // on ne peut pas sauter sans prendre appui
                        // (attention, test d'appui réalisé à partir de la position courante, si la gravité à été appliquée, il ne s'agit pas de la position affichée, amélioration possible)
                        eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas != null && eBas.peutServirDeSupport()) {
                            if (e.avancerDirectionChoisie(Direction.haut))
                                ret = true;
                        }
                        break;
                    case bas:
                        eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas instanceof Corde){
                            e.avancerDirectionChoisie(Direction.bas);
                            ret = true;
                        }
                        break;
                }
            }
        }

        return ret;
    }

    public void resetDirection() {
        directionCourante = null;
    }
}
