package it.classmi.be.repository;

import it.classmi.be.domain.Author;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AuthorRepositoryWithBagRelationships {
    Optional<Author> fetchBagRelationships(Optional<Author> author);

    List<Author> fetchBagRelationships(List<Author> authors);

    Page<Author> fetchBagRelationships(Page<Author> authors);
}
