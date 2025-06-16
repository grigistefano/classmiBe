package it.classmi.be.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BookTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Book getBookSample1() {
        return new Book()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .title("title1")
            .titleSearch("titleSearch1")
            .isbn("isbn1")
            .publisher("publisher1")
            .publishedYear(1)
            .verifyUrl("verifyUrl1")
            .viewAuthors("viewAuthors1")
            .frontImageLink("frontImageLink1")
            .backImageLink("backImageLink1")
            .pagesNumber(1)
            .language("language1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Book getBookSample2() {
        return new Book()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .title("title2")
            .titleSearch("titleSearch2")
            .isbn("isbn2")
            .publisher("publisher2")
            .publishedYear(2)
            .verifyUrl("verifyUrl2")
            .viewAuthors("viewAuthors2")
            .frontImageLink("frontImageLink2")
            .backImageLink("backImageLink2")
            .pagesNumber(2)
            .language("language2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Book getBookRandomSampleGenerator() {
        return new Book()
            .id(UUID.randomUUID())
            .title(UUID.randomUUID().toString())
            .titleSearch(UUID.randomUUID().toString())
            .isbn(UUID.randomUUID().toString())
            .publisher(UUID.randomUUID().toString())
            .publishedYear(intCount.incrementAndGet())
            .verifyUrl(UUID.randomUUID().toString())
            .viewAuthors(UUID.randomUUID().toString())
            .frontImageLink(UUID.randomUUID().toString())
            .backImageLink(UUID.randomUUID().toString())
            .pagesNumber(intCount.incrementAndGet())
            .language(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
