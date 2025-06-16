package it.classmi.be.service.mapper;

import static it.classmi.be.domain.BookSellAsserts.*;
import static it.classmi.be.domain.BookSellTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookSellMapperTest {

    private BookSellMapper bookSellMapper;

    @BeforeEach
    void setUp() {
        bookSellMapper = new BookSellMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookSellSample1();
        var actual = bookSellMapper.toEntity(bookSellMapper.toDto(expected));
        assertBookSellAllPropertiesEquals(expected, actual);
    }
}
