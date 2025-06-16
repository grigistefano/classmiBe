package it.classmi.be.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link it.classmi.be.domain.Book} entity. This class is used
 * in {@link it.classmi.be.web.rest.BookResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /books?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter title;

    private StringFilter titleSearch;

    private StringFilter isbn;

    private StringFilter publisher;

    private IntegerFilter publishedYear;

    private BooleanFilter isVerified;

    private StringFilter verifyUrl;

    private StringFilter viewAuthors;

    private StringFilter frontImageLink;

    private StringFilter backImageLink;

    private IntegerFilter pagesNumber;

    private StringFilter language;

    private StringFilter description;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private UUIDFilter bookSellsId;

    private UUIDFilter authorsId;

    private Boolean distinct;

    public BookCriteria() {}

    public BookCriteria(BookCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.titleSearch = other.optionalTitleSearch().map(StringFilter::copy).orElse(null);
        this.isbn = other.optionalIsbn().map(StringFilter::copy).orElse(null);
        this.publisher = other.optionalPublisher().map(StringFilter::copy).orElse(null);
        this.publishedYear = other.optionalPublishedYear().map(IntegerFilter::copy).orElse(null);
        this.isVerified = other.optionalIsVerified().map(BooleanFilter::copy).orElse(null);
        this.verifyUrl = other.optionalVerifyUrl().map(StringFilter::copy).orElse(null);
        this.viewAuthors = other.optionalViewAuthors().map(StringFilter::copy).orElse(null);
        this.frontImageLink = other.optionalFrontImageLink().map(StringFilter::copy).orElse(null);
        this.backImageLink = other.optionalBackImageLink().map(StringFilter::copy).orElse(null);
        this.pagesNumber = other.optionalPagesNumber().map(IntegerFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.bookSellsId = other.optionalBookSellsId().map(UUIDFilter::copy).orElse(null);
        this.authorsId = other.optionalAuthorsId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BookCriteria copy() {
        return new BookCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getTitleSearch() {
        return titleSearch;
    }

    public Optional<StringFilter> optionalTitleSearch() {
        return Optional.ofNullable(titleSearch);
    }

    public StringFilter titleSearch() {
        if (titleSearch == null) {
            setTitleSearch(new StringFilter());
        }
        return titleSearch;
    }

    public void setTitleSearch(StringFilter titleSearch) {
        this.titleSearch = titleSearch;
    }

    public StringFilter getIsbn() {
        return isbn;
    }

    public Optional<StringFilter> optionalIsbn() {
        return Optional.ofNullable(isbn);
    }

    public StringFilter isbn() {
        if (isbn == null) {
            setIsbn(new StringFilter());
        }
        return isbn;
    }

    public void setIsbn(StringFilter isbn) {
        this.isbn = isbn;
    }

    public StringFilter getPublisher() {
        return publisher;
    }

    public Optional<StringFilter> optionalPublisher() {
        return Optional.ofNullable(publisher);
    }

    public StringFilter publisher() {
        if (publisher == null) {
            setPublisher(new StringFilter());
        }
        return publisher;
    }

    public void setPublisher(StringFilter publisher) {
        this.publisher = publisher;
    }

    public IntegerFilter getPublishedYear() {
        return publishedYear;
    }

    public Optional<IntegerFilter> optionalPublishedYear() {
        return Optional.ofNullable(publishedYear);
    }

    public IntegerFilter publishedYear() {
        if (publishedYear == null) {
            setPublishedYear(new IntegerFilter());
        }
        return publishedYear;
    }

    public void setPublishedYear(IntegerFilter publishedYear) {
        this.publishedYear = publishedYear;
    }

    public BooleanFilter getIsVerified() {
        return isVerified;
    }

    public Optional<BooleanFilter> optionalIsVerified() {
        return Optional.ofNullable(isVerified);
    }

    public BooleanFilter isVerified() {
        if (isVerified == null) {
            setIsVerified(new BooleanFilter());
        }
        return isVerified;
    }

    public void setIsVerified(BooleanFilter isVerified) {
        this.isVerified = isVerified;
    }

    public StringFilter getVerifyUrl() {
        return verifyUrl;
    }

    public Optional<StringFilter> optionalVerifyUrl() {
        return Optional.ofNullable(verifyUrl);
    }

    public StringFilter verifyUrl() {
        if (verifyUrl == null) {
            setVerifyUrl(new StringFilter());
        }
        return verifyUrl;
    }

    public void setVerifyUrl(StringFilter verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public StringFilter getViewAuthors() {
        return viewAuthors;
    }

    public Optional<StringFilter> optionalViewAuthors() {
        return Optional.ofNullable(viewAuthors);
    }

    public StringFilter viewAuthors() {
        if (viewAuthors == null) {
            setViewAuthors(new StringFilter());
        }
        return viewAuthors;
    }

    public void setViewAuthors(StringFilter viewAuthors) {
        this.viewAuthors = viewAuthors;
    }

    public StringFilter getFrontImageLink() {
        return frontImageLink;
    }

    public Optional<StringFilter> optionalFrontImageLink() {
        return Optional.ofNullable(frontImageLink);
    }

    public StringFilter frontImageLink() {
        if (frontImageLink == null) {
            setFrontImageLink(new StringFilter());
        }
        return frontImageLink;
    }

    public void setFrontImageLink(StringFilter frontImageLink) {
        this.frontImageLink = frontImageLink;
    }

    public StringFilter getBackImageLink() {
        return backImageLink;
    }

    public Optional<StringFilter> optionalBackImageLink() {
        return Optional.ofNullable(backImageLink);
    }

    public StringFilter backImageLink() {
        if (backImageLink == null) {
            setBackImageLink(new StringFilter());
        }
        return backImageLink;
    }

    public void setBackImageLink(StringFilter backImageLink) {
        this.backImageLink = backImageLink;
    }

    public IntegerFilter getPagesNumber() {
        return pagesNumber;
    }

    public Optional<IntegerFilter> optionalPagesNumber() {
        return Optional.ofNullable(pagesNumber);
    }

    public IntegerFilter pagesNumber() {
        if (pagesNumber == null) {
            setPagesNumber(new IntegerFilter());
        }
        return pagesNumber;
    }

    public void setPagesNumber(IntegerFilter pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public Optional<StringFilter> optionalLanguage() {
        return Optional.ofNullable(language);
    }

    public StringFilter language() {
        if (language == null) {
            setLanguage(new StringFilter());
        }
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public UUIDFilter getBookSellsId() {
        return bookSellsId;
    }

    public Optional<UUIDFilter> optionalBookSellsId() {
        return Optional.ofNullable(bookSellsId);
    }

    public UUIDFilter bookSellsId() {
        if (bookSellsId == null) {
            setBookSellsId(new UUIDFilter());
        }
        return bookSellsId;
    }

    public void setBookSellsId(UUIDFilter bookSellsId) {
        this.bookSellsId = bookSellsId;
    }

    public UUIDFilter getAuthorsId() {
        return authorsId;
    }

    public Optional<UUIDFilter> optionalAuthorsId() {
        return Optional.ofNullable(authorsId);
    }

    public UUIDFilter authorsId() {
        if (authorsId == null) {
            setAuthorsId(new UUIDFilter());
        }
        return authorsId;
    }

    public void setAuthorsId(UUIDFilter authorsId) {
        this.authorsId = authorsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookCriteria that = (BookCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(titleSearch, that.titleSearch) &&
            Objects.equals(isbn, that.isbn) &&
            Objects.equals(publisher, that.publisher) &&
            Objects.equals(publishedYear, that.publishedYear) &&
            Objects.equals(isVerified, that.isVerified) &&
            Objects.equals(verifyUrl, that.verifyUrl) &&
            Objects.equals(viewAuthors, that.viewAuthors) &&
            Objects.equals(frontImageLink, that.frontImageLink) &&
            Objects.equals(backImageLink, that.backImageLink) &&
            Objects.equals(pagesNumber, that.pagesNumber) &&
            Objects.equals(language, that.language) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(bookSellsId, that.bookSellsId) &&
            Objects.equals(authorsId, that.authorsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            titleSearch,
            isbn,
            publisher,
            publishedYear,
            isVerified,
            verifyUrl,
            viewAuthors,
            frontImageLink,
            backImageLink,
            pagesNumber,
            language,
            description,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            bookSellsId,
            authorsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalTitleSearch().map(f -> "titleSearch=" + f + ", ").orElse("") +
            optionalIsbn().map(f -> "isbn=" + f + ", ").orElse("") +
            optionalPublisher().map(f -> "publisher=" + f + ", ").orElse("") +
            optionalPublishedYear().map(f -> "publishedYear=" + f + ", ").orElse("") +
            optionalIsVerified().map(f -> "isVerified=" + f + ", ").orElse("") +
            optionalVerifyUrl().map(f -> "verifyUrl=" + f + ", ").orElse("") +
            optionalViewAuthors().map(f -> "viewAuthors=" + f + ", ").orElse("") +
            optionalFrontImageLink().map(f -> "frontImageLink=" + f + ", ").orElse("") +
            optionalBackImageLink().map(f -> "backImageLink=" + f + ", ").orElse("") +
            optionalPagesNumber().map(f -> "pagesNumber=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalBookSellsId().map(f -> "bookSellsId=" + f + ", ").orElse("") +
            optionalAuthorsId().map(f -> "authorsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
