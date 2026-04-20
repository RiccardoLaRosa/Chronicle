package it.aulab.chronicle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import it.aulab.chronicle.models.Image;

public interface ImageRepository extends JpaRepository<Image,Long> {
    //Cancella dal database l’immagine che ha quel percorso (path)

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.path = :path")
    void deleteByPath(String path);
}
