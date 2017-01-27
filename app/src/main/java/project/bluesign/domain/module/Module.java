package project.bluesign.domain.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Module {

    private String moduleCode;
    private long totalAttended = 0;
    private long totalToDate = 0;

    public Module() {
    }

    public Module(String moduleCode, long totalAttended, long totalToDate) {
        this.moduleCode = moduleCode;
        this.totalAttended = totalAttended;
        this.totalToDate = totalToDate;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public long getTotalToDate() {
        return totalToDate;
    }

    public void setTotalToDate(long totalToDate) {
        this.totalToDate = totalToDate;
    }

    public long getTotalAttended() {
        return totalAttended;
    }

    public void setTotalAttended(long totalAttended) {
        this.totalAttended = totalAttended;
    }
}
