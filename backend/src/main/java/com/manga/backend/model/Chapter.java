package com.manga.backend.model;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "chapters", indexes = {
    @Index(name = "idx_manga_title", columnList = "manga_title")
})
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "manga_title", nullable = false)
    private String mangaTitle;

    @Column(name = "chapter_number", nullable = false)
    private Short chapterNumber;

    @Column(name = "chapter_title")
    private String chapterTitle;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getMangaTitle() { return mangaTitle; }
    public void setMangaTitle(String mangaTitle) { this.mangaTitle = mangaTitle; }

    public Short getChapterNumber() { return chapterNumber; }
    public void setChapterNumber(Short chapterNumber) { this.chapterNumber = chapterNumber; }

    public String getChapterTitle() { return chapterTitle; }
    public void setChapterTitle(String chapterTitle) { this.chapterTitle = chapterTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
}