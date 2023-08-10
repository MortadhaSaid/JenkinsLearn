package tn.esprit.cloudwizards.connect4aid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.cloudwizards.connect4aid.entities.Event;
import tn.esprit.cloudwizards.connect4aid.entities.User;

import java.time.LocalDate;
import java.util.List;

public interface EventRepo extends JpaRepository<Event,Long> {
    @Query("select e from Event e where e.eventName=:Ename")
    List<Event> getEventByName(@Param("Ename")String eventName);
    @Query("select e from Event e where e.Responsable=:Responsable")
    List<Event> getEventByResponsable(@Param("Responsable") User Res);

    @Query("select e from Event e where e.eventStart between :start and :end")
    List<Event> retrieveEventByDate(@Param("start") LocalDate startDate, @Param("end") LocalDate endDate);
}
