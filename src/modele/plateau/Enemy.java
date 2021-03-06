package modele.plateau;

import modele.deplacements.Gravite;
import modele.deplacements.IA;

public class Enemy extends EntiteDynamique implements IKillable{
    public Enemy(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreEcrase() {
        return true;
    }

    @Override
    public boolean peutServirDeSupport() {
        return false;
    }

    @Override
    public boolean peutPermettreDeMonterDescendre() {
        return false;
    }

    @Override
    public void kill() {
        System.out.print("SMICK DIED" + '\n');
        jeu.addScore(250);
        IA.getInstance().lstEntitesDynamiques.remove(this);
        Gravite.getInstance().lstEntitesDynamiques.remove(this);
        jeu.removeEntite(this);

    }
}
