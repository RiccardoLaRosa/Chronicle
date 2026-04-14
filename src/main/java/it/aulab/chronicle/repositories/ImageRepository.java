package it.aulab.chronicle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.chronicle.models.Image;

public interface ImageRepository extends JpaRepository<Image,Long> {
    //Cancella dal database l’immagine che ha quel percorso (path)
    void deleteByPath(String path);
}
