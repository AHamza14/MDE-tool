package code.plugin.vp.Structures.PIMParameterization;

import java.util.List;

public class VPProject {

    private String Id;
    private String Name;
    private String CreatingDate;
    private List<ParameterizedUmlElement> MarkedUmlElements;

    public VPProject() {}

    public VPProject(String parId, String parName, String parCreatingDate, List<ParameterizedUmlElement> parUmlElements){
        this.Id = parId;
        this.Name = parName;
        this.CreatingDate = parCreatingDate;
        this.MarkedUmlElements = parUmlElements;
    }

    //Id
    public String getId() {
        return Id;
    }

    public void setId(String paraId) {
        this.Id = paraId;
    }

    //Name
    public String getName() {
        return Name;
    }

    public void setName(String parName) {
        this.Name = parName;
    }

    //Type
    public String getCreatingDate() {
        return CreatingDate;
    }

    public void setType(String parCreatingDate) {
        this.CreatingDate = parCreatingDate;
    }

    //Description
    public List<ParameterizedUmlElement> getMarkedUmlElements() {
        return MarkedUmlElements;
    }

    public void setMarkedUmlElements(List<ParameterizedUmlElement> parUmlElements) {
        this.MarkedUmlElements = parUmlElements;
    }


}