package com.projeto.maedopedro.Controller;
import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import com.projeto.maedopedro.Service.FileStorageService;
import com.projeto.maedopedro.Service.LoyaltyUserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/anamnese")
@RequiredArgsConstructor
public class FileStorageController {
    private final FileStorageService fileStorageService;
    private final LoyaltyUserService loyaltyUserService;

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file, @PathVariable Long id) throws IOException {
        byte[] optmizedPdfBytes = fileStorageService.optimizeFile(file);
        byte[] encryptedAndOptmizedPdfBytes = fileStorageService.encryptFile(optmizedPdfBytes);
        String anamnesePath = fileStorageService.saveFile(encryptedAndOptmizedPdfBytes, id);
        loyaltyUserService.saveAnamnesePathToLoyaltyUser(anamnesePath,id);
        return ResponseEntity.ok(anamnesePath);
    }

    //FORMA DE DEIXAR O PDF CARREGAR RAPIDO
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> loadFile(@PathVariable Long id) throws IOException {
        Resource downloadedFile = loyaltyUserService.getAnamense(id);
        byte[] encryptedByes = downloadedFile.getContentAsByteArray();
        byte[] decryptedByes = fileStorageService.decryptFile(encryptedByes);
        ByteArrayResource resource = new ByteArrayResource(decryptedByes);
        long fileSize = decryptedByes.length;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION
                , "inline; filename=\""+ downloadedFile.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(fileSize)
                .body(resource);
    }
}
