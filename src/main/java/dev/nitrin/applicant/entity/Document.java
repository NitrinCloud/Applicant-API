package dev.nitrin.applicant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentName;

    @Lob
    private byte[] documentData;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    public Document() {
    }

    public Document(String documentName, byte[] documentData, Applicant applicant) {
        this.documentName = documentName;
        this.documentData = documentData;
        this.applicant = applicant;
    }

    public Long getId() {
        return id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @JsonIgnore
    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    @JsonIgnore
    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }
}

