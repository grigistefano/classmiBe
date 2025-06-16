package it.classmi.be.web.rest;

import static it.classmi.be.domain.AuthorAsserts.*;
import static it.classmi.be.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.classmi.be.IntegrationTest;
import it.classmi.be.domain.Author;
import it.classmi.be.domain.Book;
import it.classmi.be.repository.AuthorRepository;
import it.classmi.be.service.AuthorService;
import it.classmi.be.service.dto.AuthorDTO;
import it.classmi.be.service.mapper.AuthorMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AuthorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AuthorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/authors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuthorRepository authorRepository;

    @Mock
    private AuthorRepository authorRepositoryMock;

    @Autowired
    private AuthorMapper authorMapper;

    @Mock
    private AuthorService authorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthorMockMvc;

    private Author author;

    private Author insertedAuthor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Author createEntity() {
        return new Author()
            .name(DEFAULT_NAME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Author createUpdatedEntity() {
        return new Author()
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        author = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAuthor != null) {
            authorRepository.delete(insertedAuthor);
            insertedAuthor = null;
        }
    }

    @Test
    @Transactional
    void createAuthor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Author
        AuthorDTO authorDTO = authorMapper.toDto(author);
        var returnedAuthorDTO = om.readValue(
            restAuthorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AuthorDTO.class
        );

        // Validate the Author in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAuthor = authorMapper.toEntity(returnedAuthorDTO);
        assertAuthorUpdatableFieldsEquals(returnedAuthor, getPersistedAuthor(returnedAuthor));

        insertedAuthor = returnedAuthor;
    }

    @Test
    @Transactional
    void createAuthorWithExistingId() throws Exception {
        // Create the Author with an existing ID
        insertedAuthor = authorRepository.saveAndFlush(author);
        AuthorDTO authorDTO = authorMapper.toDto(author);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Author in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        author.setName(null);

        // Create the Author, which fails.
        AuthorDTO authorDTO = authorMapper.toDto(author);

        restAuthorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuthors() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList
        restAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(author.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAuthorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(authorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAuthorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(authorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAuthorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(authorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAuthorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(authorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAuthor() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get the author
        restAuthorMockMvc
            .perform(get(ENTITY_API_URL_ID, author.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(author.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAuthorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        UUID id = author.getId();

        defaultAuthorFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAuthorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where name equals to
        defaultAuthorFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAuthorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where name in
        defaultAuthorFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAuthorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where name is not null
        defaultAuthorFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthorsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where name contains
        defaultAuthorFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAuthorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where name does not contain
        defaultAuthorFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAuthorsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where createdBy equals to
        defaultAuthorFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAuthorsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where createdBy in
        defaultAuthorFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAuthorsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where createdBy is not null
        defaultAuthorFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthorsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where createdBy contains
        defaultAuthorFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAuthorsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where createdBy does not contain
        defaultAuthorFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAuthorsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where createdDate equals to
        defaultAuthorFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAuthorsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where createdDate in
        defaultAuthorFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAuthorsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where createdDate is not null
        defaultAuthorFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthorsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where lastModifiedBy equals to
        defaultAuthorFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAuthorsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where lastModifiedBy in
        defaultAuthorFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAuthorsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where lastModifiedBy is not null
        defaultAuthorFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthorsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where lastModifiedBy contains
        defaultAuthorFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAuthorsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where lastModifiedBy does not contain
        defaultAuthorFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAuthorsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where lastModifiedDate equals to
        defaultAuthorFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAuthorsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where lastModifiedDate in
        defaultAuthorFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAuthorsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        // Get all the authorList where lastModifiedDate is not null
        defaultAuthorFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthorsByBooksIsEqualToSomething() throws Exception {
        Book books;
        if (TestUtil.findAll(em, Book.class).isEmpty()) {
            authorRepository.saveAndFlush(author);
            books = BookResourceIT.createEntity();
        } else {
            books = TestUtil.findAll(em, Book.class).get(0);
        }
        em.persist(books);
        em.flush();
        author.addBooks(books);
        authorRepository.saveAndFlush(author);
        UUID booksId = books.getId();
        // Get all the authorList where books equals to booksId
        defaultAuthorShouldBeFound("booksId.equals=" + booksId);

        // Get all the authorList where books equals to UUID.randomUUID()
        defaultAuthorShouldNotBeFound("booksId.equals=" + UUID.randomUUID());
    }

    private void defaultAuthorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAuthorShouldBeFound(shouldBeFound);
        defaultAuthorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuthorShouldBeFound(String filter) throws Exception {
        restAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(author.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restAuthorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuthorShouldNotBeFound(String filter) throws Exception {
        restAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuthorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAuthor() throws Exception {
        // Get the author
        restAuthorMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuthor() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the author
        Author updatedAuthor = authorRepository.findById(author.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAuthor are not directly saved in db
        em.detach(updatedAuthor);
        updatedAuthor
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        AuthorDTO authorDTO = authorMapper.toDto(updatedAuthor);

        restAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Author in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAuthorToMatchAllProperties(updatedAuthor);
    }

    @Test
    @Transactional
    void putNonExistingAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        author.setId(UUID.randomUUID());

        // Create the Author
        AuthorDTO authorDTO = authorMapper.toDto(author);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Author in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        author.setId(UUID.randomUUID());

        // Create the Author
        AuthorDTO authorDTO = authorMapper.toDto(author);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Author in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        author.setId(UUID.randomUUID());

        // Create the Author
        AuthorDTO authorDTO = authorMapper.toDto(author);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Author in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuthorWithPatch() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the author using partial update
        Author partialUpdatedAuthor = new Author();
        partialUpdatedAuthor.setId(author.getId());

        partialUpdatedAuthor.name(UPDATED_NAME).createdBy(UPDATED_CREATED_BY).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuthor))
            )
            .andExpect(status().isOk());

        // Validate the Author in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAuthor, author), getPersistedAuthor(author));
    }

    @Test
    @Transactional
    void fullUpdateAuthorWithPatch() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the author using partial update
        Author partialUpdatedAuthor = new Author();
        partialUpdatedAuthor.setId(author.getId());

        partialUpdatedAuthor
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuthor))
            )
            .andExpect(status().isOk());

        // Validate the Author in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthorUpdatableFieldsEquals(partialUpdatedAuthor, getPersistedAuthor(partialUpdatedAuthor));
    }

    @Test
    @Transactional
    void patchNonExistingAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        author.setId(UUID.randomUUID());

        // Create the Author
        AuthorDTO authorDTO = authorMapper.toDto(author);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, authorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(authorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Author in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        author.setId(UUID.randomUUID());

        // Create the Author
        AuthorDTO authorDTO = authorMapper.toDto(author);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(authorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Author in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuthor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        author.setId(UUID.randomUUID());

        // Create the Author
        AuthorDTO authorDTO = authorMapper.toDto(author);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(authorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Author in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuthor() throws Exception {
        // Initialize the database
        insertedAuthor = authorRepository.saveAndFlush(author);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the author
        restAuthorMockMvc
            .perform(delete(ENTITY_API_URL_ID, author.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return authorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Author getPersistedAuthor(Author author) {
        return authorRepository.findById(author.getId()).orElseThrow();
    }

    protected void assertPersistedAuthorToMatchAllProperties(Author expectedAuthor) {
        assertAuthorAllPropertiesEquals(expectedAuthor, getPersistedAuthor(expectedAuthor));
    }

    protected void assertPersistedAuthorToMatchUpdatableProperties(Author expectedAuthor) {
        assertAuthorAllUpdatablePropertiesEquals(expectedAuthor, getPersistedAuthor(expectedAuthor));
    }
}
