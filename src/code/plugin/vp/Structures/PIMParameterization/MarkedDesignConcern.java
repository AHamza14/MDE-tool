package code.plugin.vp.Structures.PIMParameterization;

import code.plugin.vp.Structures.DesignConcern;

public class MarkedDesignConcern {

    private DesignConcern DesignConcern;
    private String Value;
    private String Pdm;


    public MarkedDesignConcern() {}

    public MarkedDesignConcern(DesignConcern designConcern, String parValue, String pdm){
        this.DesignConcern = designConcern;
        this.Value = parValue;
        this.Pdm = pdm;
    }

    // Design Concern
    public DesignConcern getDesignConcern() {
        return DesignConcern;
    }

    public void setDesignConcern(DesignConcern designConcern) {
        this.DesignConcern = designConcern;
    }
    
    //Value
    public String getValue() {
        return Value;
    }

    public void setValue(String paraValue) {
        this.Value = paraValue;
    }

     //PDM
     public String getPdm() {
        return Pdm;
    }

    public void setPdm(String pdm) {
        this.Pdm = pdm;
    }

}