
package com.example.eddystudio.bartable.Repository.Response.DelayReport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Description {

    @SerializedName("#cdata-section")
    @Expose
    private String cdataSection;

    public String getCdataSection() {
        return cdataSection;
    }

    public void setCdataSection(String cdataSection) {
        this.cdataSection = cdataSection;
    }

}
