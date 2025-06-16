package it.classmi.be.repository;

import it.classmi.be.domain.BookSell;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookSell entity.
 */
@Repository
public interface BookSellRepository extends JpaRepository<BookSell, UUID>, JpaSpecificationExecutor<BookSell> {
    default Optional<BookSell> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookSell> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookSell> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookSell from BookSell bookSell left join fetch bookSell.book",
        countQuery = "select count(bookSell) from BookSell bookSell"
    )
    Page<BookSell> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bookSell from BookSell bookSell left join fetch bookSell.book")
    List<BookSell> findAllWithToOneRelationships();

    @Query("select bookSell from BookSell bookSell left join fetch bookSell.book where bookSell.id =:id")
    Optional<BookSell> findOneWithToOneRelationships(@Param("id") UUID id);
}
