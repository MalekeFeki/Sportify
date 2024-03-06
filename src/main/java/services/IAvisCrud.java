package services;
import java.util.List;
import entities.Avis;
public interface IAvisCrud<T>  {




        void ajouterAvis(T avis);

        List<T> afficherAvis();

        void modifierAvis(T avis);

        void supprimerAvis(int id);

        Avis calculerAvisMoyen(List<Avis> avisList);
}
