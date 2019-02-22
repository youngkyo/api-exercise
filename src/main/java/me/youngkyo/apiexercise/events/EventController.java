package me.youngkyo.apiexercise.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @PostMapping("/api/events")
    public ResponseEntity createEvent(@RequestBody Event event) {
        Event savedEvent = eventRepository.save(event);
        URI createdUri = linkTo(EventController.class)
                .slash("{id}")
                .toUri();

        return ResponseEntity.created(createdUri).body(savedEvent);

    }
}
