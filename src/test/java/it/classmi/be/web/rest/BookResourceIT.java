package it.classmi.be.web.rest;

import static it.classmi.be.domain.BookAsserts.*;
import static it.classmi.be.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.classmi.be.IntegrationTest;
import it.classmi.be.domain.Author;
import it.classmi.be.domain.Book;
import it.classmi.be.repository.BookRepository;
import it.classmi.be.service.dto.BookDTO;
import it.classmi.be.service.mapper.BookMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE_SEARCH = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_SEARCH = "BBBBBBBBBB";

    private static final String DEFAULT_ISBN = "AAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHER = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHER = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUBLISHED_YEAR = 1;
    private static final Integer UPDATED_PUBLISHED_YEAR = 2;
    private static final Integer SMALLER_PUBLISHED_YEAR = 1 - 1;

    private static final Boolean DEFAULT_IS_VERIFIED = false;
    private static final Boolean UPDATED_IS_VERIFIED = true;

    private static final String DEFAULT_VERIFY_URL = "AAAAAAAAAA";
    private static final String UPDATED_VERIFY_URL = "BBBBBBBBBB";

    private static final String DEFAULT_VIEW_AUTHORS = "AAAAAAAAAA";
    private static final String UPDATED_VIEW_AUTHORS = "BBBBBBBBBB";

    private static final String DEFAULT_FRONT_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_FRONT_IMAGE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_BACK_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_BACK_IMAGE_LINK = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGES_NUMBER = 1;
    private static final Integer UPDATED_PAGES_NUMBER = 2;
    private static final Integer SMALLER_PAGES_NUMBER = 1 - 1;

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookMockMvc;

    private Book book;

    private Book insertedBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createEntity() {
        return new Book()
            .title(DEFAULT_TITLE)
            .titleSearch(DEFAULT_TITLE_SEARCH)
            .isbn(DEFAULT_ISBN)
            .publisher(DEFAULT_PUBLISHER)
            .publishedYear(DEFAULT_PUBLISHED_YEAR)
            .isVerified(DEFAULT_IS_VERIFIED)
            .verifyUrl(DEFAULT_VERIFY_URL)
            .viewAuthors(DEFAULT_VIEW_AUTHORS)
            .frontImageLink(DEFAULT_FRONT_IMAGE_LINK)
            .backImageLink(DEFAULT_BACK_IMAGE_LINK)
            .pagesNumber(DEFAULT_PAGES_NUMBER)
            .language(DEFAULT_LANGUAGE)
            .description(DEFAULT_DESCRIPTION)
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
    public static Book createUpdatedEntity() {
        return new Book()
            .title(UPDATED_TITLE)
            .titleSearch(UPDATED_TITLE_SEARCH)
            .isbn(UPDATED_ISBN)
            .publisher(UPDATED_PUBLISHER)
            .publishedYear(UPDATED_PUBLISHED_YEAR)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifyUrl(UPDATED_VERIFY_URL)
            .viewAuthors(UPDATED_VIEW_AUTHORS)
            .frontImageLink(UPDATED_FRONT_IMAGE_LINK)
            .backImageLink(UPDATED_BACK_IMAGE_LINK)
            .pagesNumber(UPDATED_PAGES_NUMBER)
            .language(UPDATED_LANGUAGE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        book = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBook != null) {
            bookRepository.delete(insertedBook);
            insertedBook = null;
        }
    }

    @Test
    @Transactional
    void createBook() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);
        var returnedBookDTO = om.readValue(
            restBookMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookDTO.class
        );

        // Validate the Book in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBook = bookMapper.toEntity(returnedBookDTO);
        assertBookUpdatableFieldsEquals(returnedBook, getPersistedBook(returnedBook));

        insertedBook = returnedBook;
    }

    @Test
    @Transactional
    void createBookWithExistingId() throws Exception {
        // Create the Book with an existing ID
        insertedBook = bookRepository.saveAndFlush(book);
        BookDTO bookDTO = bookMapper.toDto(book);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        book.setTitle(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.toDto(book);

        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBooks() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].titleSearch").value(hasItem(DEFAULT_TITLE_SEARCH)))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER)))
            .andExpect(jsonPath("$.[*].publishedYear").value(hasItem(DEFAULT_PUBLISHED_YEAR)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].verifyUrl").value(hasItem(DEFAULT_VERIFY_URL)))
            .andExpect(jsonPath("$.[*].viewAuthors").value(hasItem(DEFAULT_VIEW_AUTHORS)))
            .andExpect(jsonPath("$.[*].frontImageLink").value(hasItem(DEFAULT_FRONT_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].backImageLink").value(hasItem(DEFAULT_BACK_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].pagesNumber").value(hasItem(DEFAULT_PAGES_NUMBER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBook() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc
            .perform(get(ENTITY_API_URL_ID, book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.titleSearch").value(DEFAULT_TITLE_SEARCH))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER))
            .andExpect(jsonPath("$.publishedYear").value(DEFAULT_PUBLISHED_YEAR))
            .andExpect(jsonPath("$.isVerified").value(DEFAULT_IS_VERIFIED))
            .andExpect(jsonPath("$.verifyUrl").value(DEFAULT_VERIFY_URL))
            .andExpect(jsonPath("$.viewAuthors").value(DEFAULT_VIEW_AUTHORS))
            .andExpect(jsonPath("$.frontImageLink").value(DEFAULT_FRONT_IMAGE_LINK))
            .andExpect(jsonPath("$.backImageLink").value(DEFAULT_BACK_IMAGE_LINK))
            .andExpect(jsonPath("$.pagesNumber").value(DEFAULT_PAGES_NUMBER))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getBooksByIdFiltering() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        UUID id = book.getId();

        defaultBookFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where title equals to
        defaultBookFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where title in
        defaultBookFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where title is not null
        defaultBookFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where title contains
        defaultBookFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where title does not contain
        defaultBookFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleSearchIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where titleSearch equals to
        defaultBookFiltering("titleSearch.equals=" + DEFAULT_TITLE_SEARCH, "titleSearch.equals=" + UPDATED_TITLE_SEARCH);
    }

    @Test
    @Transactional
    void getAllBooksByTitleSearchIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where titleSearch in
        defaultBookFiltering(
            "titleSearch.in=" + DEFAULT_TITLE_SEARCH + "," + UPDATED_TITLE_SEARCH,
            "titleSearch.in=" + UPDATED_TITLE_SEARCH
        );
    }

    @Test
    @Transactional
    void getAllBooksByTitleSearchIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where titleSearch is not null
        defaultBookFiltering("titleSearch.specified=true", "titleSearch.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByTitleSearchContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where titleSearch contains
        defaultBookFiltering("titleSearch.contains=" + DEFAULT_TITLE_SEARCH, "titleSearch.contains=" + UPDATED_TITLE_SEARCH);
    }

    @Test
    @Transactional
    void getAllBooksByTitleSearchNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where titleSearch does not contain
        defaultBookFiltering("titleSearch.doesNotContain=" + UPDATED_TITLE_SEARCH, "titleSearch.doesNotContain=" + DEFAULT_TITLE_SEARCH);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn equals to
        defaultBookFiltering("isbn.equals=" + DEFAULT_ISBN, "isbn.equals=" + UPDATED_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn in
        defaultBookFiltering("isbn.in=" + DEFAULT_ISBN + "," + UPDATED_ISBN, "isbn.in=" + UPDATED_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn is not null
        defaultBookFiltering("isbn.specified=true", "isbn.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByIsbnContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn contains
        defaultBookFiltering("isbn.contains=" + DEFAULT_ISBN, "isbn.contains=" + UPDATED_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn does not contain
        defaultBookFiltering("isbn.doesNotContain=" + UPDATED_ISBN, "isbn.doesNotContain=" + DEFAULT_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByPublisherIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publisher equals to
        defaultBookFiltering("publisher.equals=" + DEFAULT_PUBLISHER, "publisher.equals=" + UPDATED_PUBLISHER);
    }

    @Test
    @Transactional
    void getAllBooksByPublisherIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publisher in
        defaultBookFiltering("publisher.in=" + DEFAULT_PUBLISHER + "," + UPDATED_PUBLISHER, "publisher.in=" + UPDATED_PUBLISHER);
    }

    @Test
    @Transactional
    void getAllBooksByPublisherIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publisher is not null
        defaultBookFiltering("publisher.specified=true", "publisher.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByPublisherContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publisher contains
        defaultBookFiltering("publisher.contains=" + DEFAULT_PUBLISHER, "publisher.contains=" + UPDATED_PUBLISHER);
    }

    @Test
    @Transactional
    void getAllBooksByPublisherNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publisher does not contain
        defaultBookFiltering("publisher.doesNotContain=" + UPDATED_PUBLISHER, "publisher.doesNotContain=" + DEFAULT_PUBLISHER);
    }

    @Test
    @Transactional
    void getAllBooksByPublishedYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publishedYear equals to
        defaultBookFiltering("publishedYear.equals=" + DEFAULT_PUBLISHED_YEAR, "publishedYear.equals=" + UPDATED_PUBLISHED_YEAR);
    }

    @Test
    @Transactional
    void getAllBooksByPublishedYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publishedYear in
        defaultBookFiltering(
            "publishedYear.in=" + DEFAULT_PUBLISHED_YEAR + "," + UPDATED_PUBLISHED_YEAR,
            "publishedYear.in=" + UPDATED_PUBLISHED_YEAR
        );
    }

    @Test
    @Transactional
    void getAllBooksByPublishedYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publishedYear is not null
        defaultBookFiltering("publishedYear.specified=true", "publishedYear.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByPublishedYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publishedYear is greater than or equal to
        defaultBookFiltering(
            "publishedYear.greaterThanOrEqual=" + DEFAULT_PUBLISHED_YEAR,
            "publishedYear.greaterThanOrEqual=" + UPDATED_PUBLISHED_YEAR
        );
    }

    @Test
    @Transactional
    void getAllBooksByPublishedYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publishedYear is less than or equal to
        defaultBookFiltering(
            "publishedYear.lessThanOrEqual=" + DEFAULT_PUBLISHED_YEAR,
            "publishedYear.lessThanOrEqual=" + SMALLER_PUBLISHED_YEAR
        );
    }

    @Test
    @Transactional
    void getAllBooksByPublishedYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publishedYear is less than
        defaultBookFiltering("publishedYear.lessThan=" + UPDATED_PUBLISHED_YEAR, "publishedYear.lessThan=" + DEFAULT_PUBLISHED_YEAR);
    }

    @Test
    @Transactional
    void getAllBooksByPublishedYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where publishedYear is greater than
        defaultBookFiltering("publishedYear.greaterThan=" + SMALLER_PUBLISHED_YEAR, "publishedYear.greaterThan=" + DEFAULT_PUBLISHED_YEAR);
    }

    @Test
    @Transactional
    void getAllBooksByIsVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where isVerified equals to
        defaultBookFiltering("isVerified.equals=" + DEFAULT_IS_VERIFIED, "isVerified.equals=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllBooksByIsVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where isVerified in
        defaultBookFiltering("isVerified.in=" + DEFAULT_IS_VERIFIED + "," + UPDATED_IS_VERIFIED, "isVerified.in=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllBooksByIsVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where isVerified is not null
        defaultBookFiltering("isVerified.specified=true", "isVerified.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByVerifyUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where verifyUrl equals to
        defaultBookFiltering("verifyUrl.equals=" + DEFAULT_VERIFY_URL, "verifyUrl.equals=" + UPDATED_VERIFY_URL);
    }

    @Test
    @Transactional
    void getAllBooksByVerifyUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where verifyUrl in
        defaultBookFiltering("verifyUrl.in=" + DEFAULT_VERIFY_URL + "," + UPDATED_VERIFY_URL, "verifyUrl.in=" + UPDATED_VERIFY_URL);
    }

    @Test
    @Transactional
    void getAllBooksByVerifyUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where verifyUrl is not null
        defaultBookFiltering("verifyUrl.specified=true", "verifyUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByVerifyUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where verifyUrl contains
        defaultBookFiltering("verifyUrl.contains=" + DEFAULT_VERIFY_URL, "verifyUrl.contains=" + UPDATED_VERIFY_URL);
    }

    @Test
    @Transactional
    void getAllBooksByVerifyUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where verifyUrl does not contain
        defaultBookFiltering("verifyUrl.doesNotContain=" + UPDATED_VERIFY_URL, "verifyUrl.doesNotContain=" + DEFAULT_VERIFY_URL);
    }

    @Test
    @Transactional
    void getAllBooksByViewAuthorsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where viewAuthors equals to
        defaultBookFiltering("viewAuthors.equals=" + DEFAULT_VIEW_AUTHORS, "viewAuthors.equals=" + UPDATED_VIEW_AUTHORS);
    }

    @Test
    @Transactional
    void getAllBooksByViewAuthorsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where viewAuthors in
        defaultBookFiltering(
            "viewAuthors.in=" + DEFAULT_VIEW_AUTHORS + "," + UPDATED_VIEW_AUTHORS,
            "viewAuthors.in=" + UPDATED_VIEW_AUTHORS
        );
    }

    @Test
    @Transactional
    void getAllBooksByViewAuthorsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where viewAuthors is not null
        defaultBookFiltering("viewAuthors.specified=true", "viewAuthors.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByViewAuthorsContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where viewAuthors contains
        defaultBookFiltering("viewAuthors.contains=" + DEFAULT_VIEW_AUTHORS, "viewAuthors.contains=" + UPDATED_VIEW_AUTHORS);
    }

    @Test
    @Transactional
    void getAllBooksByViewAuthorsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where viewAuthors does not contain
        defaultBookFiltering("viewAuthors.doesNotContain=" + UPDATED_VIEW_AUTHORS, "viewAuthors.doesNotContain=" + DEFAULT_VIEW_AUTHORS);
    }

    @Test
    @Transactional
    void getAllBooksByFrontImageLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where frontImageLink equals to
        defaultBookFiltering("frontImageLink.equals=" + DEFAULT_FRONT_IMAGE_LINK, "frontImageLink.equals=" + UPDATED_FRONT_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllBooksByFrontImageLinkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where frontImageLink in
        defaultBookFiltering(
            "frontImageLink.in=" + DEFAULT_FRONT_IMAGE_LINK + "," + UPDATED_FRONT_IMAGE_LINK,
            "frontImageLink.in=" + UPDATED_FRONT_IMAGE_LINK
        );
    }

    @Test
    @Transactional
    void getAllBooksByFrontImageLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where frontImageLink is not null
        defaultBookFiltering("frontImageLink.specified=true", "frontImageLink.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByFrontImageLinkContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where frontImageLink contains
        defaultBookFiltering("frontImageLink.contains=" + DEFAULT_FRONT_IMAGE_LINK, "frontImageLink.contains=" + UPDATED_FRONT_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllBooksByFrontImageLinkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where frontImageLink does not contain
        defaultBookFiltering(
            "frontImageLink.doesNotContain=" + UPDATED_FRONT_IMAGE_LINK,
            "frontImageLink.doesNotContain=" + DEFAULT_FRONT_IMAGE_LINK
        );
    }

    @Test
    @Transactional
    void getAllBooksByBackImageLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where backImageLink equals to
        defaultBookFiltering("backImageLink.equals=" + DEFAULT_BACK_IMAGE_LINK, "backImageLink.equals=" + UPDATED_BACK_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllBooksByBackImageLinkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where backImageLink in
        defaultBookFiltering(
            "backImageLink.in=" + DEFAULT_BACK_IMAGE_LINK + "," + UPDATED_BACK_IMAGE_LINK,
            "backImageLink.in=" + UPDATED_BACK_IMAGE_LINK
        );
    }

    @Test
    @Transactional
    void getAllBooksByBackImageLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where backImageLink is not null
        defaultBookFiltering("backImageLink.specified=true", "backImageLink.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByBackImageLinkContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where backImageLink contains
        defaultBookFiltering("backImageLink.contains=" + DEFAULT_BACK_IMAGE_LINK, "backImageLink.contains=" + UPDATED_BACK_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllBooksByBackImageLinkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where backImageLink does not contain
        defaultBookFiltering(
            "backImageLink.doesNotContain=" + UPDATED_BACK_IMAGE_LINK,
            "backImageLink.doesNotContain=" + DEFAULT_BACK_IMAGE_LINK
        );
    }

    @Test
    @Transactional
    void getAllBooksByPagesNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where pagesNumber equals to
        defaultBookFiltering("pagesNumber.equals=" + DEFAULT_PAGES_NUMBER, "pagesNumber.equals=" + UPDATED_PAGES_NUMBER);
    }

    @Test
    @Transactional
    void getAllBooksByPagesNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where pagesNumber in
        defaultBookFiltering(
            "pagesNumber.in=" + DEFAULT_PAGES_NUMBER + "," + UPDATED_PAGES_NUMBER,
            "pagesNumber.in=" + UPDATED_PAGES_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBooksByPagesNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where pagesNumber is not null
        defaultBookFiltering("pagesNumber.specified=true", "pagesNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByPagesNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where pagesNumber is greater than or equal to
        defaultBookFiltering(
            "pagesNumber.greaterThanOrEqual=" + DEFAULT_PAGES_NUMBER,
            "pagesNumber.greaterThanOrEqual=" + UPDATED_PAGES_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBooksByPagesNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where pagesNumber is less than or equal to
        defaultBookFiltering("pagesNumber.lessThanOrEqual=" + DEFAULT_PAGES_NUMBER, "pagesNumber.lessThanOrEqual=" + SMALLER_PAGES_NUMBER);
    }

    @Test
    @Transactional
    void getAllBooksByPagesNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where pagesNumber is less than
        defaultBookFiltering("pagesNumber.lessThan=" + UPDATED_PAGES_NUMBER, "pagesNumber.lessThan=" + DEFAULT_PAGES_NUMBER);
    }

    @Test
    @Transactional
    void getAllBooksByPagesNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where pagesNumber is greater than
        defaultBookFiltering("pagesNumber.greaterThan=" + SMALLER_PAGES_NUMBER, "pagesNumber.greaterThan=" + DEFAULT_PAGES_NUMBER);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where language equals to
        defaultBookFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where language in
        defaultBookFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where language is not null
        defaultBookFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where language contains
        defaultBookFiltering("language.contains=" + DEFAULT_LANGUAGE, "language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where language does not contain
        defaultBookFiltering("language.doesNotContain=" + UPDATED_LANGUAGE, "language.doesNotContain=" + DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where description equals to
        defaultBookFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where description in
        defaultBookFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where description is not null
        defaultBookFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where description contains
        defaultBookFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where description does not contain
        defaultBookFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where createdBy equals to
        defaultBookFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBooksByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where createdBy in
        defaultBookFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBooksByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where createdBy is not null
        defaultBookFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where createdBy contains
        defaultBookFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBooksByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where createdBy does not contain
        defaultBookFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBooksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where createdDate equals to
        defaultBookFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBooksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where createdDate in
        defaultBookFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBooksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where createdDate is not null
        defaultBookFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedBy equals to
        defaultBookFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllBooksByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedBy in
        defaultBookFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBooksByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedBy is not null
        defaultBookFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedBy contains
        defaultBookFiltering("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllBooksByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedBy does not contain
        defaultBookFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBooksByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedDate equals to
        defaultBookFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBooksByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedDate in
        defaultBookFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBooksByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedDate is not null
        defaultBookFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByAuthorsIsEqualToSomething() throws Exception {
        Author authors;
        if (TestUtil.findAll(em, Author.class).isEmpty()) {
            bookRepository.saveAndFlush(book);
            authors = AuthorResourceIT.createEntity();
        } else {
            authors = TestUtil.findAll(em, Author.class).get(0);
        }
        em.persist(authors);
        em.flush();
        book.addAuthors(authors);
        bookRepository.saveAndFlush(book);
        UUID authorsId = authors.getId();
        // Get all the bookList where authors equals to authorsId
        defaultBookShouldBeFound("authorsId.equals=" + authorsId);

        // Get all the bookList where authors equals to UUID.randomUUID()
        defaultBookShouldNotBeFound("authorsId.equals=" + UUID.randomUUID());
    }

    private void defaultBookFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBookShouldBeFound(shouldBeFound);
        defaultBookShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookShouldBeFound(String filter) throws Exception {
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].titleSearch").value(hasItem(DEFAULT_TITLE_SEARCH)))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER)))
            .andExpect(jsonPath("$.[*].publishedYear").value(hasItem(DEFAULT_PUBLISHED_YEAR)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].verifyUrl").value(hasItem(DEFAULT_VERIFY_URL)))
            .andExpect(jsonPath("$.[*].viewAuthors").value(hasItem(DEFAULT_VIEW_AUTHORS)))
            .andExpect(jsonPath("$.[*].frontImageLink").value(hasItem(DEFAULT_FRONT_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].backImageLink").value(hasItem(DEFAULT_BACK_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].pagesNumber").value(hasItem(DEFAULT_PAGES_NUMBER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookShouldNotBeFound(String filter) throws Exception {
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBook() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the book
        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBook are not directly saved in db
        em.detach(updatedBook);
        updatedBook
            .title(UPDATED_TITLE)
            .titleSearch(UPDATED_TITLE_SEARCH)
            .isbn(UPDATED_ISBN)
            .publisher(UPDATED_PUBLISHER)
            .publishedYear(UPDATED_PUBLISHED_YEAR)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifyUrl(UPDATED_VERIFY_URL)
            .viewAuthors(UPDATED_VIEW_AUTHORS)
            .frontImageLink(UPDATED_FRONT_IMAGE_LINK)
            .backImageLink(UPDATED_BACK_IMAGE_LINK)
            .pagesNumber(UPDATED_PAGES_NUMBER)
            .language(UPDATED_LANGUAGE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        BookDTO bookDTO = bookMapper.toDto(updatedBook);

        restBookMockMvc
            .perform(put(ENTITY_API_URL_ID, bookDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isOk());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookToMatchAllProperties(updatedBook);
    }

    @Test
    @Transactional
    void putNonExistingBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(UUID.randomUUID());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(put(ENTITY_API_URL_ID, bookDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(UUID.randomUUID());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(UUID.randomUUID());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookWithPatch() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the book using partial update
        Book partialUpdatedBook = new Book();
        partialUpdatedBook.setId(book.getId());

        partialUpdatedBook
            .titleSearch(UPDATED_TITLE_SEARCH)
            .isbn(UPDATED_ISBN)
            .publisher(UPDATED_PUBLISHER)
            .isVerified(UPDATED_IS_VERIFIED)
            .viewAuthors(UPDATED_VIEW_AUTHORS)
            .frontImageLink(UPDATED_FRONT_IMAGE_LINK)
            .pagesNumber(UPDATED_PAGES_NUMBER)
            .language(UPDATED_LANGUAGE)
            .description(UPDATED_DESCRIPTION);

        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBook))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBook, book), getPersistedBook(book));
    }

    @Test
    @Transactional
    void fullUpdateBookWithPatch() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the book using partial update
        Book partialUpdatedBook = new Book();
        partialUpdatedBook.setId(book.getId());

        partialUpdatedBook
            .title(UPDATED_TITLE)
            .titleSearch(UPDATED_TITLE_SEARCH)
            .isbn(UPDATED_ISBN)
            .publisher(UPDATED_PUBLISHER)
            .publishedYear(UPDATED_PUBLISHED_YEAR)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifyUrl(UPDATED_VERIFY_URL)
            .viewAuthors(UPDATED_VIEW_AUTHORS)
            .frontImageLink(UPDATED_FRONT_IMAGE_LINK)
            .backImageLink(UPDATED_BACK_IMAGE_LINK)
            .pagesNumber(UPDATED_PAGES_NUMBER)
            .language(UPDATED_LANGUAGE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBook))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookUpdatableFieldsEquals(partialUpdatedBook, getPersistedBook(partialUpdatedBook));
    }

    @Test
    @Transactional
    void patchNonExistingBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(UUID.randomUUID());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(UUID.randomUUID());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(UUID.randomUUID());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBook() throws Exception {
        // Initialize the database
        insertedBook = bookRepository.saveAndFlush(book);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the book
        restBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, book.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookRepository.count();
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

    protected Book getPersistedBook(Book book) {
        return bookRepository.findById(book.getId()).orElseThrow();
    }

    protected void assertPersistedBookToMatchAllProperties(Book expectedBook) {
        assertBookAllPropertiesEquals(expectedBook, getPersistedBook(expectedBook));
    }

    protected void assertPersistedBookToMatchUpdatableProperties(Book expectedBook) {
        assertBookAllUpdatablePropertiesEquals(expectedBook, getPersistedBook(expectedBook));
    }
}
