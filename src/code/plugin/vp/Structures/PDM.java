package code.plugin.vp.Structures;

import java.util.List;
import java.util.UUID;

public class PDM {

    private UUID Id;
    private String Name;
    private String Description;
    private String CreatingDate;
    private UmlProfile UmlProfile;
    private List<Transformation> TransformationTemplates;

    public PDM(){}
    
    public PDM(UUID parId, String parName, String parDescription, String parCreatingDate, UmlProfile parUmlProfile, List<Transformation> parTransformationTemplates){
        this.Id = parId;
        this.Name = parName;
        this.Description = parDescription;
        this.CreatingDate = parCreatingDate;
        this.UmlProfile = parUmlProfile;
        this.TransformationTemplates = parTransformationTemplates;
    }
    //Id
    public UUID getId() {
        return Id;
    }

    public void setId(UUID paraId) {
        this.Id = paraId;
    }

    //Name
    public String getName() {
        return Name;
    }

    public void setName(String paraName) {
        this.Name = paraName;
    }

    //Description
    public String getDescription() {
        return Description;
    }

    public void setDescription(String paraDescription) {
        this.Description = paraDescription;
    }

    //Creating Date
    public String getCreatingDate() {
        return CreatingDate;
    }

    public void setCreatingDate(String paraCreatingDate) {
        this.CreatingDate = paraCreatingDate;
    }

    //Uml Profile
    public UmlProfile getPdmUmlProfile() {
        return UmlProfile;
    }

    public void setPdmUmlProfile(UmlProfile parapdmUmlProfile) {
        this.UmlProfile = parapdmUmlProfile;
    }

    //Transformation Template
    public List<Transformation> getPdmTransformationTemplate() {
        return TransformationTemplates;
    }

    public void setPdmTransformationTemplate(List<Transformation> paraTransformationTemplates) {
        this.TransformationTemplates = paraTransformationTemplates;
    }
}