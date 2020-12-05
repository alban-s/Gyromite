package modele.plateau;

public class Enemy extends EntiteDynamique{
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
}
