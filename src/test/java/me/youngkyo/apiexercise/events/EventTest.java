package me.youngkyo.apiexercise.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }


    @Test
    public void testFree() {
        Event event = Event.builder().basePrice(0)
                .maxPrice(0).build();

        assertThat(event.isFree()).isTrue();
    }
}