package pl.lodz.p.it.opinioncollector.eventHandling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.opinioncollector.eventHandling.events.Event;

import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Event, UUID> {
}
