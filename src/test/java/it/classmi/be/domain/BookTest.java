package it.classmi.be.domain;

import static it.classmi.be.domain.AuthorTestSamples.*;
import static it.classmi.be.domain.BookSellTestSamples.*;
import static it.classmi.be.domain.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.classmi.be.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void bookSellsTest() {
        Book book = getBookRandomSampleGenerator();
        BookSell bookSellBack = getBookSellRandomSampleGenerator();

        book.addBookSells(bookSellBack);
        assertThat(book.getBookSells()).containsOnly(bookSellBack);
        assertThat(bookSellBack.getBook()).isEqualTo(book);

        book.removeBookSells(bookSellBack);
        assertThat(book.getBookSells()).doesNotContain(bookSellBack);
        assertThat(bookSellBack.getBook()).isNull();

        book.bookSells(new HashSet<>(Set.of(bookSellBack)));
        assertThat(book.getBookSells()).containsOnly(bookSellBack);
        assertThat(bookSellBack.getBook()).isEqualTo(book);

        book.setBookSells(new HashSet<>());
        assertThat(book.getBookSells()).doesNotContain(bookSellBack);
        assertThat(bookSellBack.getBook()).isNull();
    }

    @Test
    void authorsTest() {
        Book book = getBookRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        book.addAuthors(authorBack);
        assertThat(book.getAuthors()).containsOnly(authorBack);
        assertThat(authorBack.getBooks()).containsOnly(book);

        book.removeAuthors(authorBack);
        assertThat(book.getAuthors()).doesNotContain(authorBack);
        assertThat(authorBack.getBooks()).doesNotContain(book);

        book.authors(new HashSet<>(Set.of(authorBack)));
        assertThat(book.getAuthors()).containsOnly(authorBack);
        assertThat(authorBack.getBooks()).containsOnly(book);

        book.setAuthors(new HashSet<>());
        assertThat(book.getAuthors()).doesNotContain(authorBack);
        assertThat(authorBack.getBooks()).doesNotContain(book);
    }
}
