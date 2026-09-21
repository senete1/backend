package com.manga.backend.repository;

import com.manga.backend.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, UUID> {
    
    // Busca capítulos de um mangá específico e ordena por número do capítulo
    List<Chapter> findByMangaTitleOrderByChapterNumberAsc(String mangaTitle);
}