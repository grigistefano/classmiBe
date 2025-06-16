package it.classmi.be.service;

import it.classmi.be.domain.*; // for static metamodels
import it.classmi.be.domain.Author;
import it.classmi.be.repository.AuthorRepository;
import it.classmi.be.service.criteria.AuthorCriteria;
import it.classmi.be.service.dto.AuthorDTO;
import it.classmi.be.service.mapper.AuthorMapper;
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
 * Service for executing complex queries for {@link Author} entities in the database.
 * The main input is a {@link AuthorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AuthorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuthorQueryService extends QueryService<Author> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorQueryService.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    public AuthorQueryService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    /**
     * Return a {@link Page} of {@link AuthorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthorDTO> findByCriteria(AuthorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Author> specification = createSpecification(criteria);
        return authorRepository.fetchBagRelationships(authorRepository.findAll(specification, page)).map(authorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuthorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Author> specification = createSpecification(criteria);
        return authorRepository.count(specification);
    }

    /**
     * Function to convert {@link AuthorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Author> createSpecification(AuthorCriteria criteria) {
        Specification<Author> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Author_.id),
                buildStringSpecification(criteria.getName(), Author_.name),
                buildStringSpecification(criteria.getCreatedBy(), Author_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), Author_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), Author_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), Author_.lastModifiedDate),
                buildSpecification(criteria.getBooksId(), root -> root.join(Author_.books, JoinType.LEFT).get(Book_.id))
            );
        }
        return specification;
    }
}
