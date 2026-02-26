package az.edu.ada.wm2.lab5.controller;

import az.edu.ada.wm2.lab5.model.Event;
import az.edu.ada.wm2.lab5.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/filter/date")
    public ResponseEntity<List<Event>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            List<Event> events = eventService.getEventsByDateRange(start, end);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/filter/price")
    public ResponseEntity<List<Event>> getEventsByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        try {
            List<Event> events = eventService.getEventsByPriceRange(min, max);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/filter/tag")
    public ResponseEntity<List<Event>> getEventsByTag(@RequestParam String tag) {
        try {
            List<Event> events = eventService.getEventsByTag(tag);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        try {
            List<Event> events = eventService.getUpcomingEvents();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<Event> updateEventPrice(@PathVariable UUID id, @RequestParam BigDecimal price) {
        try {
            Event updatedEvent = eventService.updateEventPrice(id, price);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            // Check message for "not found" to return 404
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}