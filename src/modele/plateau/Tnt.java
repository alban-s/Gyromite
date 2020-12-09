package modele.plateau;

public class Tnt extends EntiteStatique implements IKillable {
    public Tnt(Jeu _jeu) { super(_jeu);}

    @Override
    public void kill() {
        jeu.removeEntite(this);
        //TODO Score
    }
}
