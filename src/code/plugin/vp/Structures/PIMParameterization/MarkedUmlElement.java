package code.plugin.vp.Structures.PIMParameterization;

import java.util.List;

public class MarkedUmlElement {

    private String Id;
    private String FullQualifiedName;
    private String Name;
    private String Type;
    private List<DesignConcernMarking> DesignConcerns;
    //private String RelationDirection;//Needed to make diffrence between Relationships with same name 
    //private String ParentClass; //Needed to make diffrence between classes sub childs (attribute,..) 

    public MarkedUmlElement() {}

    public MarkedUmlElement(String parId, String parFullQualifiedName, String parName, String parType, List<DesignConcernMarking> parDesignConcerns){
        this.Id = parId;
        this.FullQualifiedName = parFullQualifiedName;
        this.Name = parName;
        this.Type = parType;
        this.DesignConcerns = parDesignConcerns;
    }

    //Id
    public String getId() {
        return Id;
    }

    public void setId(String paraId) {
        this.Id = paraId;
    }

    //Full Qualified Name
    public String getFullQualifiedName() {
        return FullQualifiedName;
    }

    public void setFullQualifiedName(String paraFullQualifiedName) {
        this.FullQualifiedName = paraFullQualifiedName;
    }

    //Name
    public String getName() {
        return Name;
    }

    public void setName(String parName) {
        this.Name = parName;
    }

    //Type
    public String getType() {
        return Type;
    }

    public void setType(String paraType) {
        this.Type = paraType;
    }

    //Description
    public List<DesignConcernMarking> getDesignConcerns() {
        return DesignConcerns;
    }

    public void setDesignConcerns(List<DesignConcernMarking> paraDesignConcerns) {
        this.DesignConcerns = paraDesignConcerns;
    }

}