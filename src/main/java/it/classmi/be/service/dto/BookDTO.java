package it.classmi.be.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link it.classmi.be.domain.Book} entity.
 */
@Schema(description = "to edit: extends AbstractAuditingEntity<UUID>\nelimina audit fields")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookDTO implements Serializable {

    private UUID id;

    @NotNull
    private String title;

    private String titleSearch;

    private String isbn;

    private String publisher;

    private Integer publishedYear;

    private Boolean isVerified;

    private String verifyUrl;

    private String viewAuthors;

    private String frontImageLink;

    private String backImageLink;

    private Integer pagesNumber;

    private String language;

    @Schema(description = "VARCHAR(1000)")
    private String description;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<AuthorDTO> authors = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleSearch() {
        return titleSearch;
    }

    public void setTitleSearch(String titleSearch) {
        this.titleSearch = titleSearch;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public String getViewAuthors() {
        return viewAuthors;
    }

    public void setViewAuthors(String viewAuthors) {
        this.viewAuthors = viewAuthors;
    }

    public String getFrontImageLink() {
        return frontImageLink;
    }

    public void setFrontImageLink(String frontImageLink) {
        this.frontImageLink = frontImageLink;
    }

    public String getBackImageLink() {
        return backImageLink;
    }

    public void setBackImageLink(String backImageLink) {
        this.backImageLink = backImageLink;
    }

    public Integer getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(Integer pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookDTO)) {
            return false;
        }

        BookDTO bookDTO = (BookDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", titleSearch='" + getTitleSearch() + "'" +
            ", isbn='" + getIsbn() + "'" +
            ", publisher='" + getPublisher() + "'" +
            ", publishedYear=" + getPublishedYear() +
            ", isVerified='" + getIsVerified() + "'" +
            ", verifyUrl='" + getVerifyUrl() + "'" +
            ", viewAuthors='" + getViewAuthors() + "'" +
            ", frontImageLink='" + getFrontImageLink() + "'" +
            ", backImageLink='" + getBackImageLink() + "'" +
            ", pagesNumber=" + getPagesNumber() +
            ", language='" + getLanguage() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", authors=" + getAuthors() +
            "}";
    }
}
