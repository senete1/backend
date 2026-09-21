package com.manga.backend.service;

import com.manga.backend.model.Chapter;
import com.manga.backend.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository repository;

    @Autowired
    private SupabaseStorageService storageService;

    @Transactional(readOnly = true)
    public List<Chapter> getAllChapters() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Chapter> getChaptersByManga(String mangaTitle) {
        return repository.findByMangaTitleOrderByChapterNumberAsc(mangaTitle);
    }

    @Transactional
    public Chapter saveFullChapter(String mangaTitle, Short chapterNumber, 
                                   String chapterTitle, String description, 
                                   MultipartFile coverFile, MultipartFile cbzFile) throws Exception {
        
        String coverUrl = storageService.uploadFile(coverFile, "covers");
        String fileUrl = storageService.uploadFile(cbzFile, "files");

        Chapter chapter = new Chapter();
        chapter.setMangaTitle(mangaTitle);
        chapter.setChapterNumber(chapterNumber);
        chapter.setChapterTitle(chapterTitle);
        chapter.setDescription(description);
        chapter.setCoverUrl(coverUrl); 
        chapter.setFileUrl(fileUrl); 

        return repository.save(chapter);
    }
}