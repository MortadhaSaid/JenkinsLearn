package tn.esprit.cloudwizards.connect4aid.services.LabelServices;

import tn.esprit.cloudwizards.connect4aid.entities.Label;
import tn.esprit.cloudwizards.connect4aid.entities.User;

import java.util.List;

public interface ILabelService {

    public Label postLabel(Label L);
    List<Label> getLabels();
    Label findLabelbyvalue(String name);

}
