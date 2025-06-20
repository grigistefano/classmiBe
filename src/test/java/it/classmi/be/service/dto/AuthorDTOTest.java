package it.classmi.be.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.classmi.be.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AuthorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorDTO.class);
        AuthorDTO authorDTO1 = new AuthorDTO();
        authorDTO1.setId(UUID.randomUUID());
        AuthorDTO authorDTO2 = new AuthorDTO();
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
        authorDTO2.setId(authorDTO1.getId());
        assertThat(authorDTO1).isEqualTo(authorDTO2);
        authorDTO2.setId(UUID.randomUUID());
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
        authorDTO1.setId(null);
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
    }
}
