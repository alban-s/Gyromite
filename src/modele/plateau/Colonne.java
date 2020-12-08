package modele.plateau;

import modele.deplacements.color;

public class Colonne extends EntiteDynamique {

    public color col;

    public color getCol() {
        return col;
    }

    public Colonne(Jeu _jeu) {
        super(_jeu);
    }

    public Colonne(Jeu _jeu, color _col) {
        super(_jeu);
        col=_col;
        System.out.print(col);
    }

    @Override
    public boolean peutEtreEcrase() {
        return false;
    }

    @Override
    public boolean peutServirDeSupport() {
        return true;
    }

    @Override
    public boolean peutPermettreDeMonterDescendre() {
        return false;
    }
}
