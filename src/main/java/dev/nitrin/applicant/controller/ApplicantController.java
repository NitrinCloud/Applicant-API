package dev.nitrin.applicant.controller;

import dev.nitrin.applicant.entity.Applicant;
import dev.nitrin.applicant.repository.ApplicantRepository;
import dev.nitrin.applicant.repository.DocumentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applicants")
public class ApplicantController {

    private final ApplicantRepository applicantRepository;
    private final DocumentRepository documentRepository;

    public ApplicantController(ApplicantRepository applicantRepository, DocumentRepository documentRepository) {
        this.applicantRepository = applicantRepository;
        this.documentRepository = documentRepository;
    }

    @PostMapping
    public Applicant addApplicant(@RequestBody Applicant applicant) {
        return applicantRepository.save(applicant);
    }

    @GetMapping
    public List<Applicant> getAllApplicants() {
        return applicantRepository.findAll();
    }

    @GetMapping("/{applicantId}")
    public ResponseEntity<Applicant> getApplicantById(@PathVariable Long applicantId) {
        Optional<Applicant> optionalApplicant = applicantRepository.findById(applicantId);

        return optionalApplicant
                .map(applicant -> ResponseEntity.ok().body(applicant))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{applicantId}")
    public ResponseEntity<String> deleteApplicantById(@PathVariable Long applicantId) {
        Optional<Applicant> optionalApplicant = applicantRepository.findById(applicantId);
        if (optionalApplicant.isPresent()) {
            documentRepository.deleteAll(optionalApplicant.get().getDocuments());

            applicantRepository.deleteById(applicantId);

            return ResponseEntity.ok("Applicant deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
