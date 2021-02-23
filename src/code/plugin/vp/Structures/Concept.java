package code.plugin.vp.Structures;

import java.util.List;
import java.util.UUID;

public class Concept extends ElementDefinition {

    private List<DesignConcern> DesignConcerns;

    public Concept(){}

    public Concept(UUID parId, String parName, String parType, String parDescription, List<DesignConcern> parDesignConcerns){
        super(parId, parName, parType, parDescription);
        this.DesignConcerns = parDesignConcerns;
    }

    public List<DesignConcern> getDesignConcerns() {
        return DesignConcerns;
    }

    public void setDesignConcerns(List<DesignConcern> paraDesignConcerns) {
        this.DesignConcerns = paraDesignConcerns;
    }
}