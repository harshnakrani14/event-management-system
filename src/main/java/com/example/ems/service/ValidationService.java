package com.example.ems.service;

import com.example.ems.dto.EventDto;
import com.example.ems.exception.DateAndTimeOverLapException;
import com.example.ems.mapper.TimingMapper;
import com.example.ems.model.Event;
import com.example.ems.model.core.Location;
import com.example.ems.model.core.Timing;
import com.example.ems.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final EventRepository eventRepository;

    public void validateTimings(List<Timing> timings) {

        for (int i = 0; i < timings.size(); i++) {
            Timing t1 = timings.get(i);

            if (t1.getEndTime().before(t1.getStartTime())) {
                throw new DateAndTimeOverLapException("End time must be after start time.");
            }

            for (int j = i + 1; j < timings.size(); j++) {
                Timing t2 = timings.get(j);

                boolean isDuplicate = t1.getStartTime().equals(t2.getStartTime()) &&
                        t1.getEndTime().equals(t2.getEndTime());

                boolean isOverlapping = t1.getStartTime().before(t2.getEndTime()) &&
                        t1.getEndTime().after(t2.getStartTime());

                if (isDuplicate) {
                    throw new DateAndTimeOverLapException("Duplicate timing: " + t1.getStartTime() + " - "
                            + t1.getEndTime() + " is duplicated with " + t2.getStartTime() + " - " + t2.getEndTime());
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

    private static boolean isOverlap(Date start1, Date end1, Date start2,Date end2) {

        return start1.before(end2) && end1.after(start2);
    }

    public List<Timing> convertAndValidateTimings (EventDto eventDto) {

        List<Timing> timings = eventDto.getShowTime().stream()
                .map(TimingMapper.INSTANCE::toEntity)
                .collect(Collectors.toList());

        validateTimings(timings);
        return timings;
    }

}