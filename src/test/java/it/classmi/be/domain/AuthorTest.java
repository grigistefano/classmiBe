package it.classmi.be.domain;

import static it.classmi.be.domain.AuthorTestSamples.*;
import static it.classmi.be.domain.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.classmi.be.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = getAuthorSample1();
        Author author2 = new Author();
        assertThat(author1).isNotEqualTo(author2);

        author2.setId(author1.getId());
        assertThat(author1).isEqualTo(author2);

        author2 = getAuthorSample2();
        assertThat(author1).isNotEqualTo(author2);
    }

    @Test
    void booksTest() {
        Author author = getAuthorRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        author.addBooks(bookBack);
        assertThat(author.getBooks()).containsOnly(bookBack);

        author.removeBooks(bookBack);
        assertThat(author.getBooks()).doesNotContain(bookBack);

        author.books(new HashSet<>(Set.of(bookBack)));
        assertThat(author.getBooks()).containsOnly(bookBack);

        author.setBooks(new HashSet<>());
        assertThat(author.getBooks()).doesNotContain(bookBack);
    }
}
