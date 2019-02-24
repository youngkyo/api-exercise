package me.youngkyo.apiexercise.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.youngkyo.apiexercise.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 2, 22, 22, 0, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 2, 22, 20, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 2, 20, 10, 0, 0))
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("seoul")
                .build();


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(jsonPath("id").value(Matchers.not(100)));
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 2, 24, 22, 0, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 2, 22, 20, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 2, 20, 10, 0, 0))
                .endEventDateTime(LocalDateTime.of(2019, 2, 21, 1, 1))
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("seoul")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").exists());
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번재 페이지 조회하기")
    public void queryEvent() throws Exception {
        // given
        IntStream.range(0, 30).forEach(this::generateEvent);
        // when
        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
        .andExpect(jsonPath("page").exists());

        // then
    }

    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception {
        Event event = this.generateEvent(100);

        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
        ;

    }

    @Test
    @TestDescription("없는 이벤트는 조회했을 때 404응답받기")
    public void getEvent404() throws Exception {
        this.mockMvc.perform(get("/api/events/119883"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEvent() throws Exception {
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setName("update");

        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("update"));

    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .build();

       return this.eventRepository.save(event);
    }
}
