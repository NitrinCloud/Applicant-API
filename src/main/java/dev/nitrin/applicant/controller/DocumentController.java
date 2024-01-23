package dev.nitrin.applicant.controller;

import dev.nitrin.applicant.entity.Applicant;
import dev.nitrin.applicant.entity.Document;
import dev.nitrin.applicant.repository.ApplicantRepository;
import dev.nitrin.applicant.repository.DocumentRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/{applicantId}/documents")
public class DocumentController {

    private final ApplicantRepository applicantRepository;
    private final DocumentRepository documentRepository;

    public DocumentController(ApplicantRepository applicantRepository, DocumentRepository documentRepository) {
        this.applicantRepository = applicantRepository;
        this.documentRepository = documentRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @PathVariable Long applicantId,
            @RequestParam("file") MultipartFile file) {
        try {
            Optional<Applicant> optionalApplicant = applicantRepository.findById(applicantId);

            if (optionalApplicant.isPresent()) {
                Applicant applicant = optionalApplicant.get();

                Document document = new Document();
                document.setDocumentName(file.getOriginalFilename());
                document.setDocumentData(file.getBytes());
                document.setApplicant(applicant);

                documentRepository.save(document);

                return ResponseEntity.ok("Document uploaded successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading document");
        }
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long applicantId,
            @PathVariable Long documentId) {
        Optional<Document> optionalDocument = documentRepository.findById(documentId);

        if (optionalDocument.isPresent() && optionalDocument.get().getApplicant().getId().equals(applicantId)) {
            Document document = optionalDocument.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", document.getDocumentName());

            return new ResponseEntity<>(document.getDocumentData(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
