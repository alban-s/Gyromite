package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.IKillable;
import modele.plateau.Sol;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des colonnes se déplacent dans la direction définie
 * (vérifier "collisions" avec le héros)
 */
public class ColonneManager extends RealisateurDeDeplacement {
    private static ColonneManager colController;
    private boolean bMoveRouge;
    private boolean bMoveBleu;
    private Direction dirRouge = Direction.haut;
    private Direction dirBleu = Direction.haut;

    public static ColonneManager getInstance() {
        if (colController == null) {
            colController = new ColonneManager();
        }
        return colController;
    }

    public void trigger(color col) {
        if (col == color.rouge) {
            bMoveRouge = true;
            System.out.print("trigger red" + '\n');
            System.out.print('\n');
        } else if (col == color.bleu) {
            bMoveBleu = true;
            System.out.print("trigger blue" + '\n');
        }
    }

    protected boolean realiserDeplacement() {
        if(dirRouge == Direction.haut){
            for (int p = 0; p < lstEntitesDynamiques.size(); p++) {
                Entite enHaut= lstEntitesDynamiques.get(p).regarderDansLaDirection(Direction.haut);
                if(bMoveRouge && ((modele.plateau.Colonne) lstEntitesDynamiques.get(p)).col == color.rouge){
                    if (enHaut instanceof IKillable){
                        ((EntiteDynamique) enHaut).avancerDirectionChoisie(Direction.haut);
                        if (((EntiteDynamique) enHaut).regarderDansLaDirection(Direction.haut) instanceof Sol){
                            ((IKillable) enHaut).kill();
                            // TODO la mort de l'Entite dyn
                        }
                    }
                    boolean canStillMove = lstEntitesDynamiques.get(p).avancerDirectionChoisie(dirRouge);
                    if (!canStillMove) {
                        bMoveRouge =false;
                        if (dirRouge==Direction.haut){
                            dirRouge = Direction.bas;
                        }
                        else dirRouge = Direction.haut;
                    }
                }
            }
        }
        else if(dirRouge == Direction.bas){
            for (int p = lstEntitesDynamiques.size()-1; p >=0; p--) {
                Entite enBas= lstEntitesDynamiques.get(p).regarderDansLaDirection(Direction.bas);
                if(bMoveRouge && ((modele.plateau.Colonne) lstEntitesDynamiques.get(p)).col == color.rouge){
                    boolean canStillMove = lstEntitesDynamiques.get(p).avancerDirectionChoisie(dirRouge);
                    if (!canStillMove) {
                        bMoveRouge =false;
                        if (dirRouge==Direction.haut){
                            dirRouge = Direction.bas;
                        }
                        else dirRouge = Direction.haut;
                    }
                }
            }
        }

        if(dirBleu == Direction.haut){
            for (int p = 0; p < lstEntitesDynamiques.size(); p++) {
                Entite enHaut= lstEntitesDynamiques.get(p).regarderDansLaDirection(Direction.haut);
                if(bMoveBleu && ((modele.plateau.Colonne) lstEntitesDynamiques.get(p)).col == color.bleu){
                    boolean canStillMove = lstEntitesDynamiques.get(p).avancerDirectionChoisie(dirBleu);
                    if (!canStillMove) {
                        bMoveBleu =false;
                        if (dirBleu==Direction.haut){
                            dirBleu = Direction.bas;
                        }
                        else dirBleu = Direction.haut;
                    }
                }
            }
        }
        else if(dirBleu == Direction.bas){
            for (int p = lstEntitesDynamiques.size()-1; p >=0; p--) {
                Entite enBas= lstEntitesDynamiques.get(p).regarderDansLaDirection(Direction.bas);
                if(bMoveBleu && ((modele.plateau.Colonne) lstEntitesDynamiques.get(p)).col == color.bleu){
                    boolean canStillMove = lstEntitesDynamiques.get(p).avancerDirectionChoisie(dirBleu);
                    if (!canStillMove) {
                        bMoveBleu =false;
                        if (dirBleu==Direction.haut){
                            dirBleu = Direction.bas;
                        }
                        else dirBleu = Direction.haut;
                    }
                }
            }
        }



        return true;
    } // TODO
}
