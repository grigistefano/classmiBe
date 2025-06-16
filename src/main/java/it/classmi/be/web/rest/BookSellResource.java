package it.classmi.be.web.rest;

import it.classmi.be.repository.BookSellRepository;
import it.classmi.be.service.BookSellQueryService;
import it.classmi.be.service.BookSellService;
import it.classmi.be.service.criteria.BookSellCriteria;
import it.classmi.be.service.dto.BookSellDTO;
import it.classmi.be.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.classmi.be.domain.BookSell}.
 */
@RestController
@RequestMapping("/api/book-sells")
public class BookSellResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookSellResource.class);

    private static final String ENTITY_NAME = "bookSell";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookSellService bookSellService;

    private final BookSellRepository bookSellRepository;

    private final BookSellQueryService bookSellQueryService;

    public BookSellResource(
        BookSellService bookSellService,
        BookSellRepository bookSellRepository,
        BookSellQueryService bookSellQueryService
    ) {
        this.bookSellService = bookSellService;
        this.bookSellRepository = bookSellRepository;
        this.bookSellQueryService = bookSellQueryService;
    }

    /**
     * {@code POST  /book-sells} : Create a new bookSell.
     *
     * @param bookSellDTO the bookSellDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookSellDTO, or with status {@code 400 (Bad Request)} if the bookSell has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookSellDTO> createBookSell(@Valid @RequestBody BookSellDTO bookSellDTO) throws URISyntaxException {
        LOG.debug("REST request to save BookSell : {}", bookSellDTO);
        if (bookSellDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookSell cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookSellDTO = bookSellService.save(bookSellDTO);
        return ResponseEntity.created(new URI("/api/book-sells/" + bookSellDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bookSellDTO.getId().toString()))
            .body(bookSellDTO);
    }

    /**
     * {@code PUT  /book-sells/:id} : Updates an existing bookSell.
     *
     * @param id the id of the bookSellDTO to save.
     * @param bookSellDTO the bookSellDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookSellDTO,
     * or with status {@code 400 (Bad Request)} if the bookSellDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookSellDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookSellDTO> updateBookSell(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody BookSellDTO bookSellDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookSell : {}, {}", id, bookSellDTO);
        if (bookSellDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookSellDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookSellRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookSellDTO = bookSellService.update(bookSellDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookSellDTO.getId().toString()))
            .body(bookSellDTO);
    }

    /**
     * {@code PATCH  /book-sells/:id} : Partial updates given fields of an existing bookSell, field will ignore if it is null
     *
     * @param id the id of the bookSellDTO to save.
     * @param bookSellDTO the bookSellDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookSellDTO,
     * or with status {@code 400 (Bad Request)} if the bookSellDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookSellDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookSellDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookSellDTO> partialUpdateBookSell(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody BookSellDTO bookSellDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookSell partially : {}, {}", id, bookSellDTO);
        if (bookSellDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookSellDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookSellRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookSellDTO> result = bookSellService.partialUpdate(bookSellDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookSellDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /book-sells} : get all the bookSells.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookSells in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookSellDTO>> getAllBookSells(
        BookSellCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get BookSells by criteria: {}", criteria);

        Page<BookSellDTO> page = bookSellQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-sells/count} : count all the bookSells.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBookSells(BookSellCriteria criteria) {
        LOG.debug("REST request to count BookSells by criteria: {}", criteria);
        return ResponseEntity.ok().body(bookSellQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /book-sells/:id} : get the "id" bookSell.
     *
     * @param id the id of the bookSellDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookSellDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookSellDTO> getBookSell(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get BookSell : {}", id);
        Optional<BookSellDTO> bookSellDTO = bookSellService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookSellDTO);
    }

    /**
     * {@code DELETE  /book-sells/:id} : delete the "id" bookSell.
     *
     * @param id the id of the bookSellDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookSell(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete BookSell : {}", id);
        bookSellService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
