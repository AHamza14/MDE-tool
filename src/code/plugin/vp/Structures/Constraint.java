package code.plugin.vp.Structures;

import java.util.UUID;

public class Constraint extends ElementDefinition {
    
    private Concept ConceptA;
    private Concept ConceptB;

    public Constraint(){}

    public Constraint(UUID parId, String parName, String parType, String parDescription, Concept parConceptA, Concept parConceptB){
        super(parId, parName, parType, parDescription);
        this.ConceptA = parConceptA;
        this.ConceptB = parConceptB;
    }

    //Concept A
    public Concept getConceptA() {
        return ConceptA;
    }

    public void setConceptA(Concept paraConcept) {
        this.ConceptA = paraConcept;
    }

    //Concept B
    public Concept getConceptB() {
        return ConceptB;
    }

    public void setConceptB(Concept paraConcept) {
        this.ConceptB = paraConcept;
    }
}