package project.bluesign.domain.module;

public class Module {

    private String name;
    private String code;
    private ModuleMetrics metrics;

    public Module(String name, String code, ModuleMetrics metrics) {
        this.name = name;
        this.code = code;
        this.metrics = metrics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModuleMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(ModuleMetrics metrics) {
        this.metrics = metrics;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
