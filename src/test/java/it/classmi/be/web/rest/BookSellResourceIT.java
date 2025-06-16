package it.classmi.be.web.rest;

import static it.classmi.be.domain.BookSellAsserts.*;
import static it.classmi.be.web.rest.TestUtil.createUpdateProxyForBean;
import static it.classmi.be.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.classmi.be.IntegrationTest;
import it.classmi.be.domain.Book;
import it.classmi.be.domain.BookSell;
import it.classmi.be.domain.enumeration.BookState;
import it.classmi.be.repository.BookSellRepository;
import it.classmi.be.service.BookSellService;
import it.classmi.be.service.dto.BookSellDTO;
import it.classmi.be.service.mapper.BookSellMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link BookSellResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookSellResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final BookState DEFAULT_BOOK_STATE = BookState.PESSIMO;
    private static final BookState UPDATED_BOOK_STATE = BookState.BUONO;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/book-sells";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookSellRepository bookSellRepository;

    @Mock
    private BookSellRepository bookSellRepositoryMock;

    @Autowired
    private BookSellMapper bookSellMapper;

    @Mock
    private BookSellService bookSellServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookSellMockMvc;

    private BookSell bookSell;

    private BookSell insertedBookSell;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookSell createEntity() {
        return new BookSell()
            .username(DEFAULT_USERNAME)
            .date(DEFAULT_DATE)
            .country(DEFAULT_COUNTRY)
            .bookState(DEFAULT_BOOK_STATE)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .note(DEFAULT_NOTE)
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
    public static BookSell createUpdatedEntity() {
        return new BookSell()
            .username(UPDATED_USERNAME)
            .date(UPDATED_DATE)
            .country(UPDATED_COUNTRY)
            .bookState(UPDATED_BOOK_STATE)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .note(UPDATED_NOTE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        bookSell = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBookSell != null) {
            bookSellRepository.delete(insertedBookSell);
            insertedBookSell = null;
        }
    }

    @Test
    @Transactional
    void createBookSell() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookSell
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);
        var returnedBookSellDTO = om.readValue(
            restBookSellMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookSellDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookSellDTO.class
        );

        // Validate the BookSell in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookSell = bookSellMapper.toEntity(returnedBookSellDTO);
        assertBookSellUpdatableFieldsEquals(returnedBookSell, getPersistedBookSell(returnedBookSell));

        insertedBookSell = returnedBookSell;
    }

    @Test
    @Transactional
    void createBookSellWithExistingId() throws Exception {
        // Create the BookSell with an existing ID
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookSellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookSellDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookSell in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookSell.setUsername(null);

        // Create the BookSell, which fails.
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        restBookSellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookSellDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookSell.setCountry(null);

        // Create the BookSell, which fails.
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        restBookSellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookSellDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookSells() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList
        restBookSellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookSell.getId().toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].bookState").value(hasItem(DEFAULT_BOOK_STATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookSellsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookSellServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookSellMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookSellServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookSellsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookSellServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookSellMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookSellRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookSell() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get the bookSell
        restBookSellMockMvc
            .perform(get(ENTITY_API_URL_ID, bookSell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookSell.getId().toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.bookState").value(DEFAULT_BOOK_STATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getBookSellsByIdFiltering() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        UUID id = bookSell.getId();

        defaultBookSellFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllBookSellsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where username equals to
        defaultBookSellFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllBookSellsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where username in
        defaultBookSellFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllBookSellsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where username is not null
        defaultBookSellFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where username contains
        defaultBookSellFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllBookSellsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where username does not contain
        defaultBookSellFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllBookSellsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where date equals to
        defaultBookSellFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where date in
        defaultBookSellFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where date is not null
        defaultBookSellFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where date is greater than or equal to
        defaultBookSellFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where date is less than or equal to
        defaultBookSellFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where date is less than
        defaultBookSellFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where date is greater than
        defaultBookSellFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where country equals to
        defaultBookSellFiltering("country.equals=" + DEFAULT_COUNTRY, "country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllBookSellsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where country in
        defaultBookSellFiltering("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY, "country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllBookSellsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where country is not null
        defaultBookSellFiltering("country.specified=true", "country.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByCountryContainsSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where country contains
        defaultBookSellFiltering("country.contains=" + DEFAULT_COUNTRY, "country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllBookSellsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where country does not contain
        defaultBookSellFiltering("country.doesNotContain=" + UPDATED_COUNTRY, "country.doesNotContain=" + DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void getAllBookSellsByBookStateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where bookState equals to
        defaultBookSellFiltering("bookState.equals=" + DEFAULT_BOOK_STATE, "bookState.equals=" + UPDATED_BOOK_STATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByBookStateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where bookState in
        defaultBookSellFiltering("bookState.in=" + DEFAULT_BOOK_STATE + "," + UPDATED_BOOK_STATE, "bookState.in=" + UPDATED_BOOK_STATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByBookStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where bookState is not null
        defaultBookSellFiltering("bookState.specified=true", "bookState.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where quantity equals to
        defaultBookSellFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBookSellsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where quantity in
        defaultBookSellFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBookSellsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where quantity is not null
        defaultBookSellFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where quantity is greater than or equal to
        defaultBookSellFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBookSellsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where quantity is less than or equal to
        defaultBookSellFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBookSellsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where quantity is less than
        defaultBookSellFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBookSellsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where quantity is greater than
        defaultBookSellFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllBookSellsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where price equals to
        defaultBookSellFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookSellsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where price in
        defaultBookSellFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookSellsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where price is not null
        defaultBookSellFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where price is greater than or equal to
        defaultBookSellFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookSellsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where price is less than or equal to
        defaultBookSellFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBookSellsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where price is less than
        defaultBookSellFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllBookSellsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where price is greater than
        defaultBookSellFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllBookSellsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where createdBy equals to
        defaultBookSellFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBookSellsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where createdBy in
        defaultBookSellFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBookSellsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where createdBy is not null
        defaultBookSellFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where createdBy contains
        defaultBookSellFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBookSellsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where createdBy does not contain
        defaultBookSellFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBookSellsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where createdDate equals to
        defaultBookSellFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBookSellsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where createdDate in
        defaultBookSellFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBookSellsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where createdDate is not null
        defaultBookSellFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where lastModifiedBy equals to
        defaultBookSellFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllBookSellsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where lastModifiedBy in
        defaultBookSellFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBookSellsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where lastModifiedBy is not null
        defaultBookSellFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where lastModifiedBy contains
        defaultBookSellFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBookSellsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where lastModifiedBy does not contain
        defaultBookSellFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBookSellsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where lastModifiedDate equals to
        defaultBookSellFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBookSellsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where lastModifiedDate in
        defaultBookSellFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBookSellsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        // Get all the bookSellList where lastModifiedDate is not null
        defaultBookSellFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookSellsByBookIsEqualToSomething() throws Exception {
        Book book;
        if (TestUtil.findAll(em, Book.class).isEmpty()) {
            bookSellRepository.saveAndFlush(bookSell);
            book = BookResourceIT.createEntity();
        } else {
            book = TestUtil.findAll(em, Book.class).get(0);
        }
        em.persist(book);
        em.flush();
        bookSell.setBook(book);
        bookSellRepository.saveAndFlush(bookSell);
        UUID bookId = book.getId();
        // Get all the bookSellList where book equals to bookId
        defaultBookSellShouldBeFound("bookId.equals=" + bookId);

        // Get all the bookSellList where book equals to UUID.randomUUID()
        defaultBookSellShouldNotBeFound("bookId.equals=" + UUID.randomUUID());
    }

    private void defaultBookSellFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBookSellShouldBeFound(shouldBeFound);
        defaultBookSellShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookSellShouldBeFound(String filter) throws Exception {
        restBookSellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookSell.getId().toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].bookState").value(hasItem(DEFAULT_BOOK_STATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restBookSellMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookSellShouldNotBeFound(String filter) throws Exception {
        restBookSellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookSellMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBookSell() throws Exception {
        // Get the bookSell
        restBookSellMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookSell() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookSell
        BookSell updatedBookSell = bookSellRepository.findById(bookSell.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookSell are not directly saved in db
        em.detach(updatedBookSell);
        updatedBookSell
            .username(UPDATED_USERNAME)
            .date(UPDATED_DATE)
            .country(UPDATED_COUNTRY)
            .bookState(UPDATED_BOOK_STATE)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .note(UPDATED_NOTE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        BookSellDTO bookSellDTO = bookSellMapper.toDto(updatedBookSell);

        restBookSellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookSellDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookSellDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookSell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookSellToMatchAllProperties(updatedBookSell);
    }

    @Test
    @Transactional
    void putNonExistingBookSell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookSell.setId(UUID.randomUUID());

        // Create the BookSell
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookSellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookSellDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookSellDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookSell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookSell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookSell.setId(UUID.randomUUID());

        // Create the BookSell
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookSellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookSellDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookSell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookSell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookSell.setId(UUID.randomUUID());

        // Create the BookSell
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookSellMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookSellDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookSell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookSellWithPatch() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookSell using partial update
        BookSell partialUpdatedBookSell = new BookSell();
        partialUpdatedBookSell.setId(bookSell.getId());

        partialUpdatedBookSell
            .username(UPDATED_USERNAME)
            .date(UPDATED_DATE)
            .country(UPDATED_COUNTRY)
            .quantity(UPDATED_QUANTITY)
            .note(UPDATED_NOTE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restBookSellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookSell.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookSell))
            )
            .andExpect(status().isOk());

        // Validate the BookSell in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookSellUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBookSell, bookSell), getPersistedBookSell(bookSell));
    }

    @Test
    @Transactional
    void fullUpdateBookSellWithPatch() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookSell using partial update
        BookSell partialUpdatedBookSell = new BookSell();
        partialUpdatedBookSell.setId(bookSell.getId());

        partialUpdatedBookSell
            .username(UPDATED_USERNAME)
            .date(UPDATED_DATE)
            .country(UPDATED_COUNTRY)
            .bookState(UPDATED_BOOK_STATE)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .note(UPDATED_NOTE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restBookSellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookSell.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookSell))
            )
            .andExpect(status().isOk());

        // Validate the BookSell in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookSellUpdatableFieldsEquals(partialUpdatedBookSell, getPersistedBookSell(partialUpdatedBookSell));
    }

    @Test
    @Transactional
    void patchNonExistingBookSell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookSell.setId(UUID.randomUUID());

        // Create the BookSell
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookSellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookSellDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookSellDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookSell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookSell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookSell.setId(UUID.randomUUID());

        // Create the BookSell
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookSellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookSellDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookSell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookSell() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookSell.setId(UUID.randomUUID());

        // Create the BookSell
        BookSellDTO bookSellDTO = bookSellMapper.toDto(bookSell);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookSellMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookSellDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookSell in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookSell() throws Exception {
        // Initialize the database
        insertedBookSell = bookSellRepository.saveAndFlush(bookSell);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookSell
        restBookSellMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookSell.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookSellRepository.count();
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

    protected BookSell getPersistedBookSell(BookSell bookSell) {
        return bookSellRepository.findById(bookSell.getId()).orElseThrow();
    }

    protected void assertPersistedBookSellToMatchAllProperties(BookSell expectedBookSell) {
        assertBookSellAllPropertiesEquals(expectedBookSell, getPersistedBookSell(expectedBookSell));
    }

    protected void assertPersistedBookSellToMatchUpdatableProperties(BookSell expectedBookSell) {
        assertBookSellAllUpdatablePropertiesEquals(expectedBookSell, getPersistedBookSell(expectedBookSell));
    }
}
