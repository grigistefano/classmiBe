package it.classmi.be.service.mapper;

import static it.classmi.be.domain.AuthorAsserts.*;
import static it.classmi.be.domain.AuthorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorMapperTest {

    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAuthorSample1();
        var actual = authorMapper.toEntity(authorMapper.toDto(expected));
        assertAuthorAllPropertiesEquals(expected, actual);
    }
}
