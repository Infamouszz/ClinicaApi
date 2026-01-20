package com.projeto.clinicaapi.Service;
import com.projeto.clinicaapi.ExceptionHandler.Exception.ResourceNotFoundException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final Path rootLocation = Paths.get("uploads");
    @Value("${file.encryption.key}")
    private String encryptionKey;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH_BYTES = 16;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            throw new RuntimeException("Error creating root location", e);
        }
    }
    public byte[] encryptFile(byte[] fileBytes) throws IOException {
        try {
            byte[] initializationVector = new byte[IV_LENGTH_BYTES];
            new SecureRandom().nextBytes(initializationVector);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            byte[] encryptedFile = cipher.doFinal(fileBytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(initializationVector);
            outputStream.write(encryptedFile);
            return outputStream.toByteArray();
        }catch (Exception e){
            throw new RuntimeException("Error encrypting file", e);
        }

    }
    public byte[] decryptFile(byte[] fileBytes) throws IOException {
        try{
            byte[] initializationVector = new byte[IV_LENGTH_BYTES];
            System.arraycopy(fileBytes, 0, initializationVector, 0, IV_LENGTH_BYTES);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);
            int encryptedFileLength = fileBytes.length - IV_LENGTH_BYTES;
            byte[] encryptedFileBytes = new byte[encryptedFileLength];
            System.arraycopy(fileBytes, IV_LENGTH_BYTES, encryptedFileBytes, 0, encryptedFileLength);
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE,key,ivParameterSpec);
            return cipher.doFinal(encryptedFileBytes);
        }catch (Exception e){
            throw new RuntimeException("Error decrypting file", e);
        }
    }
    public byte[] optimizeFile(MultipartFile file) throws IOException {
        try (
                PDDocument document = Loader.loadPDF(file.getBytes());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ){
            document.save(outputStream);
            return outputStream.toByteArray();
        }catch (Exception e) {
            throw new RuntimeException("Error optimizing file", e);
        }
    }

    public String saveFile(byte[] fileBytes, Long loyaltyUserId) {
        try{
            if (fileBytes.length == 0) {
               throw new RuntimeException("File is empty");
            }
            String newFileName = "paciente_"+ loyaltyUserId+"_anamnese.pdf";
            Path destinationFile = this.rootLocation.resolve(newFileName)
                    .toAbsolutePath();
            Files.write(destinationFile, fileBytes);
            return destinationFile.toString();
        }catch (IOException e){
            throw new RuntimeException("Error saving file", e);
        }
   }
   public Resource loadFile(String fileName) {
        try{
            Path file = rootLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()){
                return resource;
            }else {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException e){
            throw new RuntimeException("ERROR: ", e);
        }
   }
}
