package com.example.ems.service;

import com.example.ems.model.Event;
import com.example.ems.model.Location;
import com.example.ems.model.Timing;
import com.example.ems.repository.EventRepository;
import com.example.ems.util.exception.DateAndTimeOverLapException;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final EventRepository eventRepository;

    public void validateTimings(List<Timing> timings) {
        for (int i = 0; i < timings.size(); i++) {
            Timing t1 = timings.get(i);

            if (t1.getEndTime().isBefore(t1.getStartTime())) {
                throw new DateAndTimeOverLapException("End time must be after start time.");
            }

            for (int j = i + 1; j < timings.size(); j++) {
                Timing t2 = timings.get(j);

                boolean isDuplicate = t1.getStartTime().isEqual(t2.getStartTime()) &&
                        t1.getEndTime().isEqual(t2.getEndTime());

                boolean isOverlapping = t1.getStartTime().isBefore(t2.getEndTime()) &&
                        t1.getEndTime().isAfter(t2.getStartTime());

                if (isDuplicate) {
                    throw new DateAndTimeOverLapException("Duplicate timing: " + t1.getStartTime() + " - " + t1.getEndTime()
                            + " is duplicated with " + t2.getStartTime() + " - " + t2.getEndTime());
                }

                if (isOverlapping) {
                    throw new DateAndTimeOverLapException(
                            t1.getStartTime(), t1.getEndTime(),
                            t2.getStartTime(), t2.getEndTime()
                    );
                }
            }
        }
    }

    public void checkForOverlappingEvents(Location location, List<Timing> newTimings, String excludeEventId) {
        List<Event> existingEvents = excludeEventId != null ?
                eventRepository.findByLocationAndIdNot(location, excludeEventId) :
                eventRepository.findByLocation(location);

        existingEvents.forEach(existingEvent ->
                existingEvent.getShowTime().forEach(existingTiming ->
                        newTimings.forEach(newTiming -> {
                            if (isOverlap(
                                    newTiming.getStartTime(),
                                    newTiming.getEndTime(),
                                    existingTiming.getStartTime(),
                                    existingTiming.getEndTime()
                            )) {
                                throw new DateAndTimeOverLapException(newTiming.getStartTime(), newTiming.getEndTime(),
                                        existingTiming.getStartTime(), existingTiming.getEndTime(), location);
                            }
                        })
                )
        );
    }

    private static boolean isOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
