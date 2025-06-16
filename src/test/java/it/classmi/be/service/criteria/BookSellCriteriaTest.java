package it.classmi.be.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BookSellCriteriaTest {

    @Test
    void newBookSellCriteriaHasAllFiltersNullTest() {
        var bookSellCriteria = new BookSellCriteria();
        assertThat(bookSellCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void bookSellCriteriaFluentMethodsCreatesFiltersTest() {
        var bookSellCriteria = new BookSellCriteria();

        setAllFilters(bookSellCriteria);

        assertThat(bookSellCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void bookSellCriteriaCopyCreatesNullFilterTest() {
        var bookSellCriteria = new BookSellCriteria();
        var copy = bookSellCriteria.copy();

        assertThat(bookSellCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(bookSellCriteria)
        );
    }

    @Test
    void bookSellCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bookSellCriteria = new BookSellCriteria();
        setAllFilters(bookSellCriteria);

        var copy = bookSellCriteria.copy();

        assertThat(bookSellCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(bookSellCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bookSellCriteria = new BookSellCriteria();

        assertThat(bookSellCriteria).hasToString("BookSellCriteria{}");
    }

    private static void setAllFilters(BookSellCriteria bookSellCriteria) {
        bookSellCriteria.id();
        bookSellCriteria.username();
        bookSellCriteria.date();
        bookSellCriteria.country();
        bookSellCriteria.bookState();
        bookSellCriteria.quantity();
        bookSellCriteria.price();
        bookSellCriteria.createdBy();
        bookSellCriteria.createdDate();
        bookSellCriteria.lastModifiedBy();
        bookSellCriteria.lastModifiedDate();
        bookSellCriteria.bookId();
        bookSellCriteria.distinct();
    }

    private static Condition<BookSellCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getCountry()) &&
                condition.apply(criteria.getBookState()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getBookId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BookSellCriteria> copyFiltersAre(BookSellCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getCountry(), copy.getCountry()) &&
                condition.apply(criteria.getBookState(), copy.getBookState()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getBookId(), copy.getBookId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
