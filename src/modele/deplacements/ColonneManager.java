package modele.deplacements;

import modele.plateau.*;

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
            System.out.print("trigger red : current dir = ");
            System.out.print(dirRouge);
            System.out.print('\n');
        } else if (col == color.bleu) {
            bMoveBleu = true;
            System.out.print("trigger blue" + '\n');
        }
    }

    protected boolean realiserDeplacement() {
        if(dirRouge == Direction.haut){
            boolean canStillMove=true;
            for (int p = 0; p < lstEntitesDynamiques.size(); p++) {
                Entite enHaut= lstEntitesDynamiques.get(p).regarderDansLaDirection(Direction.haut);
                if(bMoveRouge && ((Colonne) lstEntitesDynamiques.get(p)).col == color.rouge){
                    if (enHaut instanceof IKillable){
                        ((EntiteDynamique) enHaut).avancerDirectionChoisie(Direction.haut);
                        if (((EntiteDynamique) enHaut).regarderDansLaDirection(Direction.haut) instanceof Sol){
                            ((IKillable) enHaut).kill();
                            // TODO la mort de l'Entite dyn
                        }
                    }
                    canStillMove = lstEntitesDynamiques.get(p).avancerDirectionChoisie(dirRouge);

                }
            }
            if (!canStillMove) {
                bMoveRouge =false;
                if (dirRouge==Direction.haut){
                    dirRouge = Direction.bas;
                }
                else dirRouge = Direction.haut;
            }
        }
        else if(dirRouge == Direction.bas){
            boolean canStillMove=true;
            for (int p = lstEntitesDynamiques.size()-1; p >=0; p--) {

                Entite enBas= lstEntitesDynamiques.get(p).regarderDansLaDirection(Direction.bas);
                if(bMoveRouge && ((Colonne) lstEntitesDynamiques.get(p)).col == color.rouge){
                    if (enBas instanceof IKillable){
                        ((EntiteDynamique) enBas).avancerDirectionChoisie(Direction.bas);
                        if (((EntiteDynamique) enBas).regarderDansLaDirection(Direction.bas) instanceof Sol){
                            ((IKillable) enBas).kill();
                            // TODO la mort de l'Entite dyn
                        }
                    }
                    canStillMove = lstEntitesDynamiques.get(p).avancerDirectionChoisie(dirRouge);
                }
            }
            if (!canStillMove) {
                bMoveRouge =false;
                if (dirRouge==Direction.haut){
                    dirRouge = Direction.bas;
                }
                else dirRouge = Direction.haut;
            }
        }

        if(dirBleu == Direction.haut){
            boolean canStillMove=true;
            for (int p = 0; p < lstEntitesDynamiques.size(); p++) {
                Entite enHaut= lstEntitesDynamiques.get(p).regarderDansLaDirection(Direction.haut);
                if(bMoveBleu && ((Colonne) lstEntitesDynamiques.get(p)).col == color.bleu){
                    if (enHaut instanceof IKillable){
                        ((EntiteDynamique) enHaut).avancerDirectionChoisie(Direction.haut);
                        if (((EntiteDynamique) enHaut).regarderDansLaDirection(Direction.haut) instanceof Sol){
                            ((IKillable) enHaut).kill();
                            // TODO la mort de l'Entite dyn
                        }
                    }
                    canStillMove = lstEntitesDynamiques.get(p).avancerDirectionChoisie(dirBleu);
                }
            }
            if (!canStillMove) {
                bMoveBleu =false;
                if (dirBleu==Direction.haut){
                    dirBleu = Direction.bas;
                }
                else dirBleu = Direction.haut;
            }
        }
        else if(dirBleu == Direction.bas){
            boolean canStillMove=true;
            for (int p = lstEntitesDynamiques.size()-1; p >=0; p--) {
                Entite enBas= lstEntitesDynamiques.get(p).regarderDansLaDirection(Direction.bas);
                if(bMoveBleu && ((Colonne) lstEntitesDynamiques.get(p)).col == color.bleu){
                    if (enBas instanceof IKillable){
                        ((EntiteDynamique) enBas).avancerDirectionChoisie(Direction.bas);
                        if (((EntiteDynamique) enBas).regarderDansLaDirection(Direction.bas) instanceof Sol){
                            ((IKillable) enBas).kill();
                            // TODO la mort de l'Entite dyn
                        }
                    }
                    canStillMove = lstEntitesDynamiques.get(p).avancerDirectionChoisie(dirBleu);
                }
            }
            if (!canStillMove) {
                bMoveBleu =false;
                if (dirBleu==Direction.bas){
                    dirBleu = Direction.haut;
                }
                else dirBleu = Direction.bas;
            }
        }



        return true;
    } // TODO
}
