package it.classmi.be.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.classmi.be.domain.enumeration.BookState;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link it.classmi.be.domain.BookSell} entity.
 */
@Schema(description = "to edit: extends AbstractAuditingEntity<UUID>\nelimina audit fields")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookSellDTO implements Serializable {

    private UUID id;

    @NotNull
    private String username;

    private ZonedDateTime date;

    @NotNull
    private String country;

    private BookState bookState;

    private Integer quantity;

    private Double price;

    @Lob
    private String note;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private BookDTO book;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BookState getBookState() {
        return bookState;
    }

    public void setBookState(BookState bookState) {
        this.bookState = bookState;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookSellDTO)) {
            return false;
        }

        BookSellDTO bookSellDTO = (BookSellDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookSellDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookSellDTO{" +
            "id='" + getId() + "'" +
            ", username='" + getUsername() + "'" +
            ", date='" + getDate() + "'" +
            ", country='" + getCountry() + "'" +
            ", bookState='" + getBookState() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", note='" + getNote() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", book=" + getBook() +
            "}";
    }
}
