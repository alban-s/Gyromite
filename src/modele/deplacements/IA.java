package modele.deplacements;

import modele.plateau.*;

import java.util.Currency;
import java.util.HashMap;

public class IA extends RealisateurDeDeplacement {
    private static IA self;
    public static IA getInstance() {
        if (self == null) {
            self = new IA();
        }
        return self;
    }

    enum EState {
        wandering,
        chasing,
    }

    class Status {
        Status() {
            state = EState.wandering;
            if (randomEvenWithRegen()){
                currentDir = Direction.gauche;
            }
            else {
                currentDir = Direction.droite;
            }
        }
        public EState state;
        public Direction currentDir;
    }

    private HashMap<EntiteDynamique, Status> IAState = new HashMap<EntiteDynamique, Status>();

    private boolean randomEvenWithRegen(){
        return Math.random()>0.5;
    }

    protected boolean realiserDeplacement() {
        for (EntiteDynamique e : lstEntitesDynamiques) {
            Status currentAIState = IAState.get(e);
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            Entite eGauche = e.regarderDansLaDirection(Direction.gauche);
            Entite eDroite = e.regarderDansLaDirection(Direction.droite);
            Entite eHaut = e.regarderDansLaDirection(Direction.haut);
            Entite eBasG = e.regarderDansLaDirection(Direction.basGauche);
            Entite eBasD = e.regarderDansLaDirection(Direction.basDroite);

            if (currentAIState.currentDir == Direction.haut && (eHaut instanceof Corde)){
                if (randomEvenWithRegen()) {
                    if (randomEvenWithRegen()){
                        if (eGauche == null && eBasG instanceof Sol) {
                            e.avancerDirectionChoisie(Direction.gauche);
                            currentAIState.currentDir = Direction.gauche;
                        }
                        else if (eDroite == null && eBasD instanceof Sol){
                            e.avancerDirectionChoisie(Direction.droite);
                            currentAIState.currentDir = Direction.droite;
                        }
                    }
                    else {
                        if (eDroite == null && eBasD instanceof Sol){
                            e.avancerDirectionChoisie(Direction.droite);
                            currentAIState.currentDir = Direction.droite;
                        }
                        else if (eGauche == null && eBasG instanceof Sol) {
                            e.avancerDirectionChoisie(Direction.gauche);
                            currentAIState.currentDir = Direction.gauche;
                        }
                    }
                }
                else {
                    e.avancerDirectionChoisie(Direction.haut);
                }
            }
            else if (eHaut instanceof Corde && currentAIState.currentDir!=Direction.bas && randomEvenWithRegen()){
                e.avancerDirectionChoisie(Direction.haut);
                currentAIState.currentDir=Direction.haut;
            }

            else if (currentAIState.currentDir==Direction.gauche){
                if (eBasG !=null && (eGauche ==null || eGauche instanceof Corde)){
                    e.avancerDirectionChoisie(Direction.gauche);
                }
                else {
                    e.avancerDirectionChoisie(Direction.droite);
                    currentAIState.currentDir=Direction.droite;
                }
            }
            else if (currentAIState.currentDir==Direction.droite){
                if (eBasD !=null&& (eDroite ==null || eDroite instanceof Corde)){
                    e.avancerDirectionChoisie(Direction.droite);
                }
                else {
                    e.avancerDirectionChoisie(Direction.gauche);
                    currentAIState.currentDir=Direction.gauche;
                }
            }
            else if (currentAIState.currentDir == Direction.haut && (eHaut instanceof EntiteStatique && !(eHaut instanceof Corde))){
                e.avancerDirectionChoisie(Direction.bas);
                currentAIState.currentDir=Direction.bas;
            }
            else if (currentAIState.currentDir ==Direction.bas && !(eBas instanceof Sol )){
                e.avancerDirectionChoisie(Direction.bas);
            }
            else {
                if (randomEvenWithRegen()){
                    currentAIState.currentDir=Direction.gauche;
                }
                else {
                    currentAIState.currentDir=Direction.droite;
                }
            }



            //System.out.print("IA MOVEMENT on " + e + '\n');
        }
        return false;
    } // TODO

    @Override
    public void addEntiteDynamique(EntiteDynamique ed) {
        IAState.put(ed, new Status());
        super.addEntiteDynamique(ed);
    }
}
