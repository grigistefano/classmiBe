package it.classmi.be.domain;

import static it.classmi.be.domain.BookSellTestSamples.*;
import static it.classmi.be.domain.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.classmi.be.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookSellTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookSell.class);
        BookSell bookSell1 = getBookSellSample1();
        BookSell bookSell2 = new BookSell();
        assertThat(bookSell1).isNotEqualTo(bookSell2);

        bookSell2.setId(bookSell1.getId());
        assertThat(bookSell1).isEqualTo(bookSell2);

        bookSell2 = getBookSellSample2();
        assertThat(bookSell1).isNotEqualTo(bookSell2);
    }

    @Test
    void bookTest() {
        BookSell bookSell = getBookSellRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        bookSell.setBook(bookBack);
        assertThat(bookSell.getBook()).isEqualTo(bookBack);

        bookSell.book(null);
        assertThat(bookSell.getBook()).isNull();
    }
}
