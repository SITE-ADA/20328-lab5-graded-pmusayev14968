package az.edu.ada.wm2.lab5.service;

import az.edu.ada.wm2.lab5.model.Event;
import az.edu.ada.wm2.lab5.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event createEvent(Event event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event updateEvent(UUID id, Event event) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        event.setId(id);
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    @Override
    public Event partialUpdateEvent(UUID id, Event partialEvent) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        if (partialEvent.getEventName() != null) existingEvent.setEventName(partialEvent.getEventName());
        if (partialEvent.getTags() != null && !partialEvent.getTags().isEmpty()) existingEvent.setTags(partialEvent.getTags());
        if (partialEvent.getTicketPrice() != null) existingEvent.setTicketPrice(partialEvent.getTicketPrice());
        if (partialEvent.getEventDateTime() != null) existingEvent.setEventDateTime(partialEvent.getEventDateTime());
        if (partialEvent.getDurationMinutes() > 0) existingEvent.setDurationMinutes(partialEvent.getDurationMinutes());

        return eventRepository.save(existingEvent);
    }

    @Override
    public List<Event> getEventsByTag(String tag) {
        List<Event> allEvents = eventRepository.findAll();
        List<Event> results = new ArrayList<>();
        if (tag == null || tag.isEmpty()) return results;

        for (Event e : allEvents) {
            if (e.getTags() != null && e.getTags().contains(tag)) {
                results.add(e);
            }
        }
        return results;
    }

    @Override
    public List<Event> getUpcomingEvents() {
        List<Event> all = eventRepository.findAll();
        List<Event> upcoming = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Event event : all) {
            if (event.getEventDateTime() != null && event.getEventDateTime().isAfter(now)) {
                upcoming.add(event);
            }
        }
        return upcoming;
    }

    @Override
    public List<Event> getEventsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) { return List.of(); }

    @Override
    public List<Event> getEventsByDateRange(LocalDateTime start, LocalDateTime end) { return List.of(); }

    @Override
    public Event updateEventPrice(UUID id, BigDecimal newPrice) { return null; }
}