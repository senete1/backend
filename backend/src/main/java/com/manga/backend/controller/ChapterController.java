package com.manga.backend.controller;

import com.manga.backend.model.Chapter;
import com.manga.backend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/mangas")
@CrossOrigin(origins = "*") 
public class ChapterController {

    @Autowired
    private ChapterService service;

    @GetMapping
    public ResponseEntity<List<Chapter>> listAll() {
        return ResponseEntity.ok(service.getAllChapters());
    }

    @GetMapping("/{title}")
    public ResponseEntity<List<Chapter>> getChaptersByTitle(@PathVariable("title") String title) {
        List<Chapter> chapters = service.getChaptersByManga(title);
        if (chapters.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chapters);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadChapter(
            @RequestParam("mangaTitle") String mangaTitle,
            @RequestParam("chapterNumber") Short chapterNumber,
            @RequestParam(value = "chapterTitle", required = false) String chapterTitle,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("coverFile") MultipartFile coverFile,
            @RequestParam("cbzFile") MultipartFile cbzFile) {

        try {
            Chapter newChapter = service.saveFullChapter(
                    mangaTitle, chapterNumber, chapterTitle, description, coverFile, cbzFile
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(newChapter);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }
    }
}
