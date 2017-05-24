package project.bluesign.domain.signIn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignIn {
    private long dateTimestamp;
    private String moduleCode;

    public SignIn() {
    }

    public SignIn(String moduleCode, long dateTimestamp) {
        this.moduleCode = moduleCode;
        this.dateTimestamp = dateTimestamp;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(long timestamp) {
        this.dateTimestamp = timestamp;
    }
}
