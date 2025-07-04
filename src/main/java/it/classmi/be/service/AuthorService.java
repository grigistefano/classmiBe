package it.classmi.be.service;

import it.classmi.be.domain.Author;
import it.classmi.be.repository.AuthorRepository;
import it.classmi.be.service.dto.AuthorDTO;
import it.classmi.be.service.mapper.AuthorMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link it.classmi.be.domain.Author}.
 */
@Service
@Transactional
public class AuthorService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    /**
     * Save a author.
     *
     * @param authorDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorDTO save(AuthorDTO authorDTO) {
        LOG.debug("Request to save Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    /**
     * Update a author.
     *
     * @param authorDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorDTO update(AuthorDTO authorDTO) {
        LOG.debug("Request to update Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    /**
     * Partially update a author.
     *
     * @param authorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AuthorDTO> partialUpdate(AuthorDTO authorDTO) {
        LOG.debug("Request to partially update Author : {}", authorDTO);

        return authorRepository
            .findById(authorDTO.getId())
            .map(existingAuthor -> {
                authorMapper.partialUpdate(existingAuthor, authorDTO);

                return existingAuthor;
            })
            .map(authorRepository::save)
            .map(authorMapper::toDto);
    }

    /**
     * Get all the authors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AuthorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return authorRepository.findAllWithEagerRelationships(pageable).map(authorMapper::toDto);
    }

    /**
     * Get one author by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findOne(UUID id) {
        LOG.debug("Request to get Author : {}", id);
        return authorRepository.findOneWithEagerRelationships(id).map(authorMapper::toDto);
    }

    /**
     * Delete the author by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete Author : {}", id);
        authorRepository.deleteById(id);
    }
}
