package code.plugin.vp.Structures;

import java.util.UUID;

public class ElementDefinition {

    private UUID Id;
    private String Name;
    private String Type;
    private String Description;

    public ElementDefinition() {}

    public ElementDefinition(UUID parId, String parName, String parType, String parDescription){
        this.Id = parId;
        this.Name = parName;
        this.Type = parType;
        this.Description = parDescription;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID paraId) {
        this.Id = paraId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String paraName) {
        this.Name = paraName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String paraType) {
        this.Type = paraType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String paraDescription) {
        this.Description = paraDescription;
    }


}