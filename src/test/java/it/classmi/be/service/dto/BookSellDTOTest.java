package it.classmi.be.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.classmi.be.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BookSellDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookSellDTO.class);
        BookSellDTO bookSellDTO1 = new BookSellDTO();
        bookSellDTO1.setId(UUID.randomUUID());
        BookSellDTO bookSellDTO2 = new BookSellDTO();
        assertThat(bookSellDTO1).isNotEqualTo(bookSellDTO2);
        bookSellDTO2.setId(bookSellDTO1.getId());
        assertThat(bookSellDTO1).isEqualTo(bookSellDTO2);
        bookSellDTO2.setId(UUID.randomUUID());
        assertThat(bookSellDTO1).isNotEqualTo(bookSellDTO2);
        bookSellDTO1.setId(null);
        assertThat(bookSellDTO1).isNotEqualTo(bookSellDTO2);
    }
}
