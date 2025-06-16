package it.classmi.be.service;

import it.classmi.be.domain.*; // for static metamodels
import it.classmi.be.domain.BookSell;
import it.classmi.be.repository.BookSellRepository;
import it.classmi.be.service.criteria.BookSellCriteria;
import it.classmi.be.service.dto.BookSellDTO;
import it.classmi.be.service.mapper.BookSellMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BookSell} entities in the database.
 * The main input is a {@link BookSellCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BookSellDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookSellQueryService extends QueryService<BookSell> {

    private static final Logger LOG = LoggerFactory.getLogger(BookSellQueryService.class);

    private final BookSellRepository bookSellRepository;

    private final BookSellMapper bookSellMapper;

    public BookSellQueryService(BookSellRepository bookSellRepository, BookSellMapper bookSellMapper) {
        this.bookSellRepository = bookSellRepository;
        this.bookSellMapper = bookSellMapper;
    }

    /**
     * Return a {@link Page} of {@link BookSellDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookSellDTO> findByCriteria(BookSellCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BookSell> specification = createSpecification(criteria);
        return bookSellRepository.findAll(specification, page).map(bookSellMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookSellCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<BookSell> specification = createSpecification(criteria);
        return bookSellRepository.count(specification);
    }

    /**
     * Function to convert {@link BookSellCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BookSell> createSpecification(BookSellCriteria criteria) {
        Specification<BookSell> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), BookSell_.id),
                buildStringSpecification(criteria.getUsername(), BookSell_.username),
                buildRangeSpecification(criteria.getDate(), BookSell_.date),
                buildStringSpecification(criteria.getCountry(), BookSell_.country),
                buildSpecification(criteria.getBookState(), BookSell_.bookState),
                buildRangeSpecification(criteria.getQuantity(), BookSell_.quantity),
                buildRangeSpecification(criteria.getPrice(), BookSell_.price),
                buildStringSpecification(criteria.getCreatedBy(), BookSell_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), BookSell_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), BookSell_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), BookSell_.lastModifiedDate),
                buildSpecification(criteria.getBookId(), root -> root.join(BookSell_.book, JoinType.LEFT).get(Book_.id))
            );
        }
        return specification;
    }
}
