package it.classmi.be.service.mapper;

import it.classmi.be.domain.Author;
import it.classmi.be.domain.Book;
import it.classmi.be.service.dto.AuthorDTO;
import it.classmi.be.service.dto.BookDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Mapping(target = "books", source = "books", qualifiedByName = "bookTitleSet")
    AuthorDTO toDto(Author s);

    @Mapping(target = "removeBooks", ignore = true)
    Author toEntity(AuthorDTO authorDTO);

    @Named("bookTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    BookDTO toDtoBookTitle(Book book);

    @Named("bookTitleSet")
    default Set<BookDTO> toDtoBookTitleSet(Set<Book> book) {
        return book.stream().map(this::toDtoBookTitle).collect(Collectors.toSet());
    }
}
