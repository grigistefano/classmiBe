package it.classmi.be.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BookCriteriaTest {

    @Test
    void newBookCriteriaHasAllFiltersNullTest() {
        var bookCriteria = new BookCriteria();
        assertThat(bookCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void bookCriteriaFluentMethodsCreatesFiltersTest() {
        var bookCriteria = new BookCriteria();

        setAllFilters(bookCriteria);

        assertThat(bookCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void bookCriteriaCopyCreatesNullFilterTest() {
        var bookCriteria = new BookCriteria();
        var copy = bookCriteria.copy();

        assertThat(bookCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(bookCriteria)
        );
    }

    @Test
    void bookCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bookCriteria = new BookCriteria();
        setAllFilters(bookCriteria);

        var copy = bookCriteria.copy();

        assertThat(bookCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(bookCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bookCriteria = new BookCriteria();

        assertThat(bookCriteria).hasToString("BookCriteria{}");
    }

    private static void setAllFilters(BookCriteria bookCriteria) {
        bookCriteria.id();
        bookCriteria.title();
        bookCriteria.titleSearch();
        bookCriteria.isbn();
        bookCriteria.publisher();
        bookCriteria.publishedYear();
        bookCriteria.isVerified();
        bookCriteria.verifyUrl();
        bookCriteria.viewAuthors();
        bookCriteria.frontImageLink();
        bookCriteria.backImageLink();
        bookCriteria.pagesNumber();
        bookCriteria.language();
        bookCriteria.description();
        bookCriteria.createdBy();
        bookCriteria.createdDate();
        bookCriteria.lastModifiedBy();
        bookCriteria.lastModifiedDate();
        bookCriteria.bookSellsId();
        bookCriteria.authorsId();
        bookCriteria.distinct();
    }

    private static Condition<BookCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getTitleSearch()) &&
                condition.apply(criteria.getIsbn()) &&
                condition.apply(criteria.getPublisher()) &&
                condition.apply(criteria.getPublishedYear()) &&
                condition.apply(criteria.getIsVerified()) &&
                condition.apply(criteria.getVerifyUrl()) &&
                condition.apply(criteria.getViewAuthors()) &&
                condition.apply(criteria.getFrontImageLink()) &&
                condition.apply(criteria.getBackImageLink()) &&
                condition.apply(criteria.getPagesNumber()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getBookSellsId()) &&
                condition.apply(criteria.getAuthorsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BookCriteria> copyFiltersAre(BookCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getTitleSearch(), copy.getTitleSearch()) &&
                condition.apply(criteria.getIsbn(), copy.getIsbn()) &&
                condition.apply(criteria.getPublisher(), copy.getPublisher()) &&
                condition.apply(criteria.getPublishedYear(), copy.getPublishedYear()) &&
                condition.apply(criteria.getIsVerified(), copy.getIsVerified()) &&
                condition.apply(criteria.getVerifyUrl(), copy.getVerifyUrl()) &&
                condition.apply(criteria.getViewAuthors(), copy.getViewAuthors()) &&
                condition.apply(criteria.getFrontImageLink(), copy.getFrontImageLink()) &&
                condition.apply(criteria.getBackImageLink(), copy.getBackImageLink()) &&
                condition.apply(criteria.getPagesNumber(), copy.getPagesNumber()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getBookSellsId(), copy.getBookSellsId()) &&
                condition.apply(criteria.getAuthorsId(), copy.getAuthorsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
