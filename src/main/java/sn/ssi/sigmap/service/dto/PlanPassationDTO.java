package sn.ssi.sigmap.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link sn.ssi.sigmap.domain.PlanPassation} entity.
 */
public class PlanPassationDTO implements Serializable {
    
    private Long id;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String commentaire;

    @NotNull
    private Integer annee;

    @NotNull
    private LocalDate dateCreation;

    private LocalDate dateMiseValidation;

    private LocalDate dateValidation;

    private String statut;

    private String commentaireMiseEnValidationAC;

    private String referenceMiseValidationAC;

    @Lob
    private byte[] fichierMiseValidationAC;

    private String fichierMiseValidationACContentType;
    private LocalDate dateMiseEnValidationCcmp;

    @Lob
    private byte[] fichierMiseValidationCcmp;

    private String fichierMiseValidationCcmpContentType;
    private String referenceMiseValidationCcmp;

    private LocalDate dateValidation1;

    private String commentaireValidation;

    private String referenceValidation;

    @Lob
    private byte[] fichierValidation;

    private String fichierValidationContentType;
    private LocalDate dateValidation2;

    private LocalDate dateRejet;

    private LocalDate datePublication;

    private String commentairePublication;

    private String numPlan;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateMiseValidation() {
        return dateMiseValidation;
    }

    public void setDateMiseValidation(LocalDate dateMiseValidation) {
        this.dateMiseValidation = dateMiseValidation;
    }

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommentaireMiseEnValidationAC() {
        return commentaireMiseEnValidationAC;
    }

    public void setCommentaireMiseEnValidationAC(String commentaireMiseEnValidationAC) {
        this.commentaireMiseEnValidationAC = commentaireMiseEnValidationAC;
    }

    public String getReferenceMiseValidationAC() {
        return referenceMiseValidationAC;
    }

    public void setReferenceMiseValidationAC(String referenceMiseValidationAC) {
        this.referenceMiseValidationAC = referenceMiseValidationAC;
    }

    public byte[] getFichierMiseValidationAC() {
        return fichierMiseValidationAC;
    }

    public void setFichierMiseValidationAC(byte[] fichierMiseValidationAC) {
        this.fichierMiseValidationAC = fichierMiseValidationAC;
    }

    public String getFichierMiseValidationACContentType() {
        return fichierMiseValidationACContentType;
    }

    public void setFichierMiseValidationACContentType(String fichierMiseValidationACContentType) {
        this.fichierMiseValidationACContentType = fichierMiseValidationACContentType;
    }

    public LocalDate getDateMiseEnValidationCcmp() {
        return dateMiseEnValidationCcmp;
    }

    public void setDateMiseEnValidationCcmp(LocalDate dateMiseEnValidationCcmp) {
        this.dateMiseEnValidationCcmp = dateMiseEnValidationCcmp;
    }

    public byte[] getFichierMiseValidationCcmp() {
        return fichierMiseValidationCcmp;
    }

    public void setFichierMiseValidationCcmp(byte[] fichierMiseValidationCcmp) {
        this.fichierMiseValidationCcmp = fichierMiseValidationCcmp;
    }

    public String getFichierMiseValidationCcmpContentType() {
        return fichierMiseValidationCcmpContentType;
    }

    public void setFichierMiseValidationCcmpContentType(String fichierMiseValidationCcmpContentType) {
        this.fichierMiseValidationCcmpContentType = fichierMiseValidationCcmpContentType;
    }

    public String getReferenceMiseValidationCcmp() {
        return referenceMiseValidationCcmp;
    }

    public void setReferenceMiseValidationCcmp(String referenceMiseValidationCcmp) {
        this.referenceMiseValidationCcmp = referenceMiseValidationCcmp;
    }

    public LocalDate getDateValidation1() {
        return dateValidation1;
    }

    public void setDateValidation1(LocalDate dateValidation1) {
        this.dateValidation1 = dateValidation1;
    }

    public String getCommentaireValidation() {
        return commentaireValidation;
    }

    public void setCommentaireValidation(String commentaireValidation) {
        this.commentaireValidation = commentaireValidation;
    }

    public String getReferenceValidation() {
        return referenceValidation;
    }

    public void setReferenceValidation(String referenceValidation) {
        this.referenceValidation = referenceValidation;
    }

    public byte[] getFichierValidation() {
        return fichierValidation;
    }

    public void setFichierValidation(byte[] fichierValidation) {
        this.fichierValidation = fichierValidation;
    }

    public String getFichierValidationContentType() {
        return fichierValidationContentType;
    }

    public void setFichierValidationContentType(String fichierValidationContentType) {
        this.fichierValidationContentType = fichierValidationContentType;
    }

    public LocalDate getDateValidation2() {
        return dateValidation2;
    }

    public void setDateValidation2(LocalDate dateValidation2) {
        this.dateValidation2 = dateValidation2;
    }

    public LocalDate getDateRejet() {
        return dateRejet;
    }

    public void setDateRejet(LocalDate dateRejet) {
        this.dateRejet = dateRejet;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public String getCommentairePublication() {
        return commentairePublication;
    }

    public void setCommentairePublication(String commentairePublication) {
        this.commentairePublication = commentairePublication;
    }

    public String getNumPlan() {
        return numPlan;
    }

    public void setNumPlan(String numPlan) {
        this.numPlan = numPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanPassationDTO)) {
            return false;
        }

        return id != null && id.equals(((PlanPassationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanPassationDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", annee=" + getAnnee() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateMiseValidation='" + getDateMiseValidation() + "'" +
            ", dateValidation='" + getDateValidation() + "'" +
            ", statut='" + getStatut() + "'" +
            ", commentaireMiseEnValidationAC='" + getCommentaireMiseEnValidationAC() + "'" +
            ", referenceMiseValidationAC='" + getReferenceMiseValidationAC() + "'" +
            ", fichierMiseValidationAC='" + getFichierMiseValidationAC() + "'" +
            ", dateMiseEnValidationCcmp='" + getDateMiseEnValidationCcmp() + "'" +
            ", fichierMiseValidationCcmp='" + getFichierMiseValidationCcmp() + "'" +
            ", referenceMiseValidationCcmp='" + getReferenceMiseValidationCcmp() + "'" +
            ", dateValidation1='" + getDateValidation1() + "'" +
            ", commentaireValidation='" + getCommentaireValidation() + "'" +
            ", referenceValidation='" + getReferenceValidation() + "'" +
            ", fichierValidation='" + getFichierValidation() + "'" +
            ", dateValidation2='" + getDateValidation2() + "'" +
            ", dateRejet='" + getDateRejet() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            ", commentairePublication='" + getCommentairePublication() + "'" +
            ", numPlan='" + getNumPlan() + "'" +
            "}";
    }
}
