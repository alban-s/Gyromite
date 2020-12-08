package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class Gravite extends RealisateurDeDeplacement {
    private static Gravite self;
    public static Gravite getInstance() {
        if (self == null) {
            self = new Gravite();
        }
        return self;
    }


    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            if (eBas == null || (!eBas.peutServirDeSupport())) {
                if (e.avancerDirectionChoisie(Direction.bas))
                    ret = true;
            }
        }

        return ret;
    }
}
