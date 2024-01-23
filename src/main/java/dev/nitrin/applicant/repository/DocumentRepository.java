package dev.nitrin.applicant.repository;

import dev.nitrin.applicant.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByApplicantId(Long applicantId);
}
