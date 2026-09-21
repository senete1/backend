package com.manga.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public String uploadFile(MultipartFile file, String folder) throws Exception {
        String originalName = file.getOriginalFilename();
        String extension = originalName != null ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String uniqueFileName = folder + "/" + UUID.randomUUID().toString() + extension;

        String endpoint = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + uniqueFileName;

        Supplier<InputStream> streamSupplier = () -> {
            try {
                return file.getInputStream();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };

        String mimeType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + supabaseKey)
                .header("Content-Type", mimeType)
                .POST(HttpRequest.BodyPublishers.ofInputStream(streamSupplier))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + uniqueFileName;
        } else {
            throw new Exception("Upload failed. Status: " + response.statusCode() + " Error: " + response.body());
        }
    }
}