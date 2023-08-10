package tn.esprit.cloudwizards.connect4aid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.cloudwizards.connect4aid.entities.Event;
import tn.esprit.cloudwizards.connect4aid.entities.Label;

import java.util.List;

public interface LabelRepo extends JpaRepository<Label,Long> {
    @Query("select l from Label l where l.value=:name")
    Label getLabelByName(@Param("name")String name);
}
