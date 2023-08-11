package code.plugin.vp.Structures;

import java.util.*;

public class Transformation extends ElementDefinition {

    private String FileUri;
    private Concept PrimaryImplementedConcept;
    private List<Concept> ImplmementedConceptsForVariation;

    public Transformation(){}

    public Transformation(UUID parId, String parName, String parType, String parDescription, String parFileUri, Concept parPrimaryConcept, List<Concept> parConceptsVariation){
        super(parId, parName, parType, parDescription);
        this.FileUri = parFileUri;
        this.PrimaryImplementedConcept = parPrimaryConcept;
        this.ImplmementedConceptsForVariation = parConceptsVariation;
    }

    public String getFileUri() {
        return FileUri;
    }

    public void setFileUri(String paraFileUri) {
        this.FileUri = paraFileUri;
    }

    public Concept getPrimaryImplementedConcept(){
        return PrimaryImplementedConcept;
    }

    public void setPrimaryImplementedConcept(Concept paraId) {
        this.PrimaryImplementedConcept = paraId;
    }

    public List<Concept> getImplmementedConceptsForVariation(){
        return ImplmementedConceptsForVariation;
    }

    public void setImplmementedConceptsForVariation(List<Concept> paraId) {
        this.ImplmementedConceptsForVariation = paraId;
    }
}