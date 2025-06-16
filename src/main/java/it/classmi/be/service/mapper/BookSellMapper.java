package it.classmi.be.service.mapper;

import it.classmi.be.domain.Book;
import it.classmi.be.domain.BookSell;
import it.classmi.be.service.dto.BookDTO;
import it.classmi.be.service.dto.BookSellDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookSell} and its DTO {@link BookSellDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookSellMapper extends EntityMapper<BookSellDTO, BookSell> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookTitle")
    BookSellDTO toDto(BookSell s);

    @Named("bookTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    BookDTO toDtoBookTitle(Book book);
}
