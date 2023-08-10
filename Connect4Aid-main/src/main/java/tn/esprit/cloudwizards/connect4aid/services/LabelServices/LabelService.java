package tn.esprit.cloudwizards.connect4aid.services.LabelServices;

import org.springframework.stereotype.Service;
import tn.esprit.cloudwizards.connect4aid.entities.Label;
import tn.esprit.cloudwizards.connect4aid.repositories.LabelRepo;

import java.util.List;
@Service
public class LabelService implements ILabelService {
    LabelRepo labelRepo;

    public LabelService(LabelRepo labelRepo) {
        this.labelRepo = labelRepo;
    }
    public Label postLabel(Label L) {
        return labelRepo.save(L);
    }


    public List<Label> getLabels() {
        return labelRepo.findAll();
    }
    public Label findLabelbyvalue(String name)
    {   return labelRepo.getLabelByName(name);
    }
}
