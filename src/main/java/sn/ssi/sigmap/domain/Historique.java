package sn.ssi.sigmap.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Historique. by
 */
@Entity
@Table(name = "historique")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Historique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_rejet")
    private LocalDate dateRejet;

    @Column(name = "date_rjet_2")
    private LocalDate dateRjet2;

    @Column(name = "date_mise_validation")
    private LocalDate dateMiseValidation;

    @Column(name = "commentaire_rejet")
    private String commentaireRejet;

    @Column(name = "commentaire_mise_validation")
    private String commentaireMiseValidation;

    @Lob
    @Column(name = "fichier_mise_validation")
    private byte[] fichierMiseValidation;

    @Column(name = "fichier_mise_validation_content_type")
    private String fichierMiseValidationContentType;

    @Lob
    @Column(name = "fichier_rejet")
    private byte[] fichierRejet;

    @Column(name = "fichier_rejet_content_type")
    private String fichierRejetContentType;

    @Column(name = "etat")
    private String etat;

    @Column(name = "etat_2")
    private String etat2;

    @Column(name = "commentaire_rejet_2")
    private String commentaireRejet2;

    @Lob
    @Column(name = "fichier_rejet_2")
    private byte[] fichierRejet2;

    @Column(name = "fichier_rejet_2_content_type")
    private String fichierRejet2ContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "historiques", allowSetters = true)
    private PlanPassation planPassation;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateRejet() {
        return dateRejet;
    }

    public Historique dateRejet(LocalDate dateRejet) {
        this.dateRejet = dateRejet;
        return this;
    }

    public void setDateRejet(LocalDate dateRejet) {
        this.dateRejet = dateRejet;
    }

    public LocalDate getDateRjet2() {
        return dateRjet2;
    }

    public Historique dateRjet2(LocalDate dateRjet2) {
        this.dateRjet2 = dateRjet2;
        return this;
    }

    public void setDateRjet2(LocalDate dateRjet2) {
        this.dateRjet2 = dateRjet2;
    }

    public LocalDate getDateMiseValidation() {
        return dateMiseValidation;
    }

    public Historique dateMiseValidation(LocalDate dateMiseValidation) {
        this.dateMiseValidation = dateMiseValidation;
        return this;
    }

    public void setDateMiseValidation(LocalDate dateMiseValidation) {
        this.dateMiseValidation = dateMiseValidation;
    }

    public String getCommentaireRejet() {
        return commentaireRejet;
    }

    public Historique commentaireRejet(String commentaireRejet) {
        this.commentaireRejet = commentaireRejet;
        return this;
    }

    public void setCommentaireRejet(String commentaireRejet) {
        this.commentaireRejet = commentaireRejet;
    }

    public String getCommentaireMiseValidation() {
        return commentaireMiseValidation;
    }

    public Historique commentaireMiseValidation(String commentaireMiseValidation) {
        this.commentaireMiseValidation = commentaireMiseValidation;
        return this;
    }

    public void setCommentaireMiseValidation(String commentaireMiseValidation) {
        this.commentaireMiseValidation = commentaireMiseValidation;
    }

    public byte[] getFichierMiseValidation() {
        return fichierMiseValidation;
    }

    public Historique fichierMiseValidation(byte[] fichierMiseValidation) {
        this.fichierMiseValidation = fichierMiseValidation;
        return this;
    }

    public void setFichierMiseValidation(byte[] fichierMiseValidation) {
        this.fichierMiseValidation = fichierMiseValidation;
    }

    public String getFichierMiseValidationContentType() {
        return fichierMiseValidationContentType;
    }

    public Historique fichierMiseValidationContentType(String fichierMiseValidationContentType) {
        this.fichierMiseValidationContentType = fichierMiseValidationContentType;
        return this;
    }

    public void setFichierMiseValidationContentType(String fichierMiseValidationContentType) {
        this.fichierMiseValidationContentType = fichierMiseValidationContentType;
    }

    public byte[] getFichierRejet() {
        return fichierRejet;
    }

    public Historique fichierRejet(byte[] fichierRejet) {
        this.fichierRejet = fichierRejet;
        return this;
    }

    public void setFichierRejet(byte[] fichierRejet) {
        this.fichierRejet = fichierRejet;
    }

    public String getFichierRejetContentType() {
        return fichierRejetContentType;
    }

    public Historique fichierRejetContentType(String fichierRejetContentType) {
        this.fichierRejetContentType = fichierRejetContentType;
        return this;
    }

    public void setFichierRejetContentType(String fichierRejetContentType) {
        this.fichierRejetContentType = fichierRejetContentType;
    }

    public String getEtat() {
        return etat;
    }

    public Historique etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getEtat2() {
        return etat2;
    }

    public Historique etat2(String etat2) {
        this.etat2 = etat2;
        return this;
    }

    public void setEtat2(String etat2) {
        this.etat2 = etat2;
    }

    public String getCommentaireRejet2() {
        return commentaireRejet2;
    }

    public Historique commentaireRejet2(String commentaireRejet2) {
        this.commentaireRejet2 = commentaireRejet2;
        return this;
    }

    public void setCommentaireRejet2(String commentaireRejet2) {
        this.commentaireRejet2 = commentaireRejet2;
    }

    public byte[] getFichierRejet2() {
        return fichierRejet2;
    }

    public Historique fichierRejet2(byte[] fichierRejet2) {
        this.fichierRejet2 = fichierRejet2;
        return this;
    }

    public void setFichierRejet2(byte[] fichierRejet2) {
        this.fichierRejet2 = fichierRejet2;
    }

    public String getFichierRejet2ContentType() {
        return fichierRejet2ContentType;
    }

    public Historique fichierRejet2ContentType(String fichierRejet2ContentType) {
        this.fichierRejet2ContentType = fichierRejet2ContentType;
        return this;
    }

    public void setFichierRejet2ContentType(String fichierRejet2ContentType) {
        this.fichierRejet2ContentType = fichierRejet2ContentType;
    }

    public PlanPassation getPlanPassation() {
        return planPassation;
    }

    public Historique planPassation(PlanPassation planPassation) {
        this.planPassation = planPassation;
        return this;
    }

    public void setPlanPassation(PlanPassation planPassation) {
        this.planPassation = planPassation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Historique)) {
            return false;
        }
        return id != null && id.equals(((Historique) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Historique{" +
            "id=" + getId() +
            ", dateRejet='" + getDateRejet() + "'" +
            ", dateRjet2='" + getDateRjet2() + "'" +
            ", dateMiseValidation='" + getDateMiseValidation() + "'" +
            ", commentaireRejet='" + getCommentaireRejet() + "'" +
            ", commentaireMiseValidation='" + getCommentaireMiseValidation() + "'" +
            ", fichierMiseValidation='" + getFichierMiseValidation() + "'" +
            ", fichierMiseValidationContentType='" + getFichierMiseValidationContentType() + "'" +
            ", fichierRejet='" + getFichierRejet() + "'" +
            ", fichierRejetContentType='" + getFichierRejetContentType() + "'" +
            ", etat='" + getEtat() + "'" +
            ", etat2='" + getEtat2() + "'" +
            ", commentaireRejet2='" + getCommentaireRejet2() + "'" +
            ", fichierRejet2='" + getFichierRejet2() + "'" +
            ", fichierRejet2ContentType='" + getFichierRejet2ContentType() + "'" +
            "}";
    }
}
