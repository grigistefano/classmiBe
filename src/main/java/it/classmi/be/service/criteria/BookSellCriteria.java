package it.classmi.be.service.criteria;

import it.classmi.be.domain.enumeration.BookState;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link it.classmi.be.domain.BookSell} entity. This class is used
 * in {@link it.classmi.be.web.rest.BookSellResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /book-sells?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookSellCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BookState
     */
    public static class BookStateFilter extends Filter<BookState> {

        public BookStateFilter() {}

        public BookStateFilter(BookStateFilter filter) {
            super(filter);
        }

        @Override
        public BookStateFilter copy() {
            return new BookStateFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter username;

    private ZonedDateTimeFilter date;

    private StringFilter country;

    private BookStateFilter bookState;

    private IntegerFilter quantity;

    private DoubleFilter price;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private UUIDFilter bookId;

    private Boolean distinct;

    public BookSellCriteria() {}

    public BookSellCriteria(BookSellCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.country = other.optionalCountry().map(StringFilter::copy).orElse(null);
        this.bookState = other.optionalBookState().map(BookStateFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(DoubleFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.bookId = other.optionalBookId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BookSellCriteria copy() {
        return new BookSellCriteria(this);
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

    public StringFilter getUsername() {
        return username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(username);
    }

    public StringFilter username() {
        if (username == null) {
            setUsername(new StringFilter());
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public Optional<ZonedDateTimeFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public ZonedDateTimeFilter date() {
        if (date == null) {
            setDate(new ZonedDateTimeFilter());
        }
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public StringFilter getCountry() {
        return country;
    }

    public Optional<StringFilter> optionalCountry() {
        return Optional.ofNullable(country);
    }

    public StringFilter country() {
        if (country == null) {
            setCountry(new StringFilter());
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public BookStateFilter getBookState() {
        return bookState;
    }

    public Optional<BookStateFilter> optionalBookState() {
        return Optional.ofNullable(bookState);
    }

    public BookStateFilter bookState() {
        if (bookState == null) {
            setBookState(new BookStateFilter());
        }
        return bookState;
    }

    public void setBookState(BookStateFilter bookState) {
        this.bookState = bookState;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public Optional<DoubleFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public DoubleFilter price() {
        if (price == null) {
            setPrice(new DoubleFilter());
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
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

    public UUIDFilter getBookId() {
        return bookId;
    }

    public Optional<UUIDFilter> optionalBookId() {
        return Optional.ofNullable(bookId);
    }

    public UUIDFilter bookId() {
        if (bookId == null) {
            setBookId(new UUIDFilter());
        }
        return bookId;
    }

    public void setBookId(UUIDFilter bookId) {
        this.bookId = bookId;
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
        final BookSellCriteria that = (BookSellCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(date, that.date) &&
            Objects.equals(country, that.country) &&
            Objects.equals(bookState, that.bookState) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(price, that.price) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(bookId, that.bookId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            username,
            date,
            country,
            bookState,
            quantity,
            price,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            bookId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookSellCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalCountry().map(f -> "country=" + f + ", ").orElse("") +
            optionalBookState().map(f -> "bookState=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalBookId().map(f -> "bookId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
