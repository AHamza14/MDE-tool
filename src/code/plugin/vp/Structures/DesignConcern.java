package code.plugin.vp.Structures;

import java.util.List;
import java.util.UUID;

public class DesignConcern extends ElementDefinition {

    private List<String> UmlElements;

    public DesignConcern(){}

    public DesignConcern(UUID parId, String parName, String parType, String parDescription, List<String> parUmlElements){
        super(parId, parName, parType, parDescription);
        this.UmlElements = parUmlElements;
    }
    //UML Element
    public List<String> getUmlElements() {
        return UmlElements;
    }

    public void setUmlElements(List<String> paraUmlElement) {
        this.UmlElements = paraUmlElement;
    }

}