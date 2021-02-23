package code.plugin.vp.Structures;

import java.util.List;

public class UmlProfile {

    private List<Concept> Concepts;
    private List<Constraint> Constraints;

    public UmlProfile(){}

    public UmlProfile(List<Concept> parConcepts, List<Constraint> parConstraints){
        this.Concepts = parConcepts;
        this.Constraints = parConstraints;
    }

    //Concepts
    public List<Concept> getConcepts() {
        return Concepts;
    }

    public void setConcepts(List<Concept> paraConcepts) {
        this.Concepts = paraConcepts;
    }

    //Constraints
    public List<Constraint> getConstraints() {
        return Constraints;
    }

    public void setConstraints(List<Constraint> paraConstraints) {
        this.Constraints = paraConstraints;
    }
}