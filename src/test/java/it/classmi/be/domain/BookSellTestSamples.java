package it.classmi.be.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BookSellTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BookSell getBookSellSample1() {
        return new BookSell()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .username("username1")
            .country("country1")
            .quantity(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static BookSell getBookSellSample2() {
        return new BookSell()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .username("username2")
            .country("country2")
            .quantity(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static BookSell getBookSellRandomSampleGenerator() {
        return new BookSell()
            .id(UUID.randomUUID())
            .username(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
