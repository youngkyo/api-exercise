package me.youngkyo.apiexercise.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
//@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 2, 22, 22, 0, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 2, 22, 20, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 2, 20, 10, 0, 0))
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("seoul")
                .build();

        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                   .contentType(MediaType.APPLICATION_JSON_UTF8)
                   .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated()).andExpect(jsonPath("id").exists());
    }

//    @Test
//    public void createEvent_Bad_Request_Empty_Input() throws Exception {
//        EventDto eventDto = EventDto.builder()
//                .name("Spring")
//                .description("REST API Development with Spring")
//                .beginEnrollmentDateTime(LocalDateTime.of(2019, 2, 24, 22, 0, 0))
//                .closeEnrollmentDateTime(LocalDateTime.of(2019, 2, 22, 20, 0, 0))
//                .beginEventDateTime(LocalDateTime.of(2019, 2, 20, 10, 0, 0))
//                .maxPrice(200)
//                .limitOfEnrollment(100)
//                .location("seoul")
//                .build();
//
//        mockMvc.perform(post("/api/events")
//                    .contentType(MediaType.APPLICATION_JSON_UTF8)
//                    .accept(MediaType.APPLICATION_JSON_UTF8)
//                    .contentType(objectMapper.writeValueAsString(eventDto)))
//                .andExpect(status().isBadRequest());
//    }
}
