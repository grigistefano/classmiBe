package it.classmi.be.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * to edit: extends AbstractAuditingEntity<UUID>
 * elimina audit fields
 */
@Entity
@Table(name = "book")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private UUID id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "title_search")
    private String titleSearch;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "verify_url")
    private String verifyUrl;

    @Column(name = "view_authors")
    private String viewAuthors;

    @Column(name = "front_image_link")
    private String frontImageLink;

    @Column(name = "back_image_link")
    private String backImageLink;

    @Column(name = "pages_number")
    private Integer pagesNumber;

    @Column(name = "language")
    private String language;

    /**
     * VARCHAR(1000)
     */
    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonIgnoreProperties(value = { "book" }, allowSetters = true)
    private Set<BookSell> bookSells = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "books")
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private Set<Author> authors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Book id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Book title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleSearch() {
        return this.titleSearch;
    }

    public Book titleSearch(String titleSearch) {
        this.setTitleSearch(titleSearch);
        return this;
    }

    public void setTitleSearch(String titleSearch) {
        this.titleSearch = titleSearch;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public Book isbn(String isbn) {
        this.setIsbn(isbn);
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public Book publisher(String publisher) {
        this.setPublisher(publisher);
        return this;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublishedYear() {
        return this.publishedYear;
    }

    public Book publishedYear(Integer publishedYear) {
        this.setPublishedYear(publishedYear);
        return this;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Boolean getIsVerified() {
        return this.isVerified;
    }

    public Book isVerified(Boolean isVerified) {
        this.setIsVerified(isVerified);
        return this;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getVerifyUrl() {
        return this.verifyUrl;
    }

    public Book verifyUrl(String verifyUrl) {
        this.setVerifyUrl(verifyUrl);
        return this;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public String getViewAuthors() {
        return this.viewAuthors;
    }

    public Book viewAuthors(String viewAuthors) {
        this.setViewAuthors(viewAuthors);
        return this;
    }

    public void setViewAuthors(String viewAuthors) {
        this.viewAuthors = viewAuthors;
    }

    public String getFrontImageLink() {
        return this.frontImageLink;
    }

    public Book frontImageLink(String frontImageLink) {
        this.setFrontImageLink(frontImageLink);
        return this;
    }

    public void setFrontImageLink(String frontImageLink) {
        this.frontImageLink = frontImageLink;
    }

    public String getBackImageLink() {
        return this.backImageLink;
    }

    public Book backImageLink(String backImageLink) {
        this.setBackImageLink(backImageLink);
        return this;
    }

    public void setBackImageLink(String backImageLink) {
        this.backImageLink = backImageLink;
    }

    public Integer getPagesNumber() {
        return this.pagesNumber;
    }

    public Book pagesNumber(Integer pagesNumber) {
        this.setPagesNumber(pagesNumber);
        return this;
    }

    public void setPagesNumber(Integer pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public String getLanguage() {
        return this.language;
    }

    public Book language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return this.description;
    }

    public Book description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Book createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Book createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Book lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Book lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<BookSell> getBookSells() {
        return this.bookSells;
    }

    public void setBookSells(Set<BookSell> bookSells) {
        if (this.bookSells != null) {
            this.bookSells.forEach(i -> i.setBook(null));
        }
        if (bookSells != null) {
            bookSells.forEach(i -> i.setBook(this));
        }
        this.bookSells = bookSells;
    }

    public Book bookSells(Set<BookSell> bookSells) {
        this.setBookSells(bookSells);
        return this;
    }

    public Book addBookSells(BookSell bookSell) {
        this.bookSells.add(bookSell);
        bookSell.setBook(this);
        return this;
    }

    public Book removeBookSells(BookSell bookSell) {
        this.bookSells.remove(bookSell);
        bookSell.setBook(null);
        return this;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public void setAuthors(Set<Author> authors) {
        if (this.authors != null) {
            this.authors.forEach(i -> i.removeBooks(this));
        }
        if (authors != null) {
            authors.forEach(i -> i.addBooks(this));
        }
        this.authors = authors;
    }

    public Book authors(Set<Author> authors) {
        this.setAuthors(authors);
        return this;
    }

    public Book addAuthors(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
        return this;
    }

    public Book removeAuthors(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return getId() != null && getId().equals(((Book) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
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
            "}";
    }
}
