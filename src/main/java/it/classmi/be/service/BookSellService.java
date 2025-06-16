package it.classmi.be.service;

import it.classmi.be.domain.BookSell;
import it.classmi.be.repository.BookSellRepository;
import it.classmi.be.service.dto.BookSellDTO;
import it.classmi.be.service.mapper.BookSellMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link it.classmi.be.domain.BookSell}.
 */
@Service
@Transactional
public class BookSellService {

    private static final Logger LOG = LoggerFactory.getLogger(BookSellService.class);

    private final BookSellRepository bookSellRepository;

    private final BookSellMapper bookSellMapper;

    public BookSellService(BookSellRepository bookSellRepository, BookSellMapper bookSellMapper) {
        this.bookSellRepository = bookSellRepository;
        this.bookSellMapper = bookSellMapper;
    }

    /**
     * Save a bookSell.
     *
     * @param bookSellDTO the entity to save.
     * @return the persisted entity.
     */
    public BookSellDTO save(BookSellDTO bookSellDTO) {
        LOG.debug("Request to save BookSell : {}", bookSellDTO);
        BookSell bookSell = bookSellMapper.toEntity(bookSellDTO);
        bookSell = bookSellRepository.save(bookSell);
        return bookSellMapper.toDto(bookSell);
    }

    /**
     * Update a bookSell.
     *
     * @param bookSellDTO the entity to save.
     * @return the persisted entity.
     */
    public BookSellDTO update(BookSellDTO bookSellDTO) {
        LOG.debug("Request to update BookSell : {}", bookSellDTO);
        BookSell bookSell = bookSellMapper.toEntity(bookSellDTO);
        bookSell = bookSellRepository.save(bookSell);
        return bookSellMapper.toDto(bookSell);
    }

    /**
     * Partially update a bookSell.
     *
     * @param bookSellDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookSellDTO> partialUpdate(BookSellDTO bookSellDTO) {
        LOG.debug("Request to partially update BookSell : {}", bookSellDTO);

        return bookSellRepository
            .findById(bookSellDTO.getId())
            .map(existingBookSell -> {
                bookSellMapper.partialUpdate(existingBookSell, bookSellDTO);

                return existingBookSell;
            })
            .map(bookSellRepository::save)
            .map(bookSellMapper::toDto);
    }

    /**
     * Get all the bookSells with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookSellDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookSellRepository.findAllWithEagerRelationships(pageable).map(bookSellMapper::toDto);
    }

    /**
     * Get one bookSell by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookSellDTO> findOne(UUID id) {
        LOG.debug("Request to get BookSell : {}", id);
        return bookSellRepository.findOneWithEagerRelationships(id).map(bookSellMapper::toDto);
    }

    /**
     * Delete the bookSell by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete BookSell : {}", id);
        bookSellRepository.deleteById(id);
    }
}
