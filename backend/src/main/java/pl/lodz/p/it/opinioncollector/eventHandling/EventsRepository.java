package pl.lodz.p.it.opinioncollector.eventHandling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.opinioncollector.eventHandling.events.Event;
import pl.lodz.p.it.opinioncollector.eventHandling.events.EventStatus;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Event, UUID> {
    List<Event> findByUser(User user);
    int countByUserAndStatus(User user, EventStatus status);
}
