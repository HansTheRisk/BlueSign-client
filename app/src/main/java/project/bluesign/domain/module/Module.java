package project.bluesign.domain.module;

public class Module {

    private String name;
    private String code;
    private int attended = 0;
    private int total = 0;

    public Module(String name, String code, int attended, int total) {
        this.name = name;
        this.code = code;
        this.attended = attended;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAttended() {
        return attended;
    }

    public void setAttended(int attended) {
        this.attended = attended;
    }
}
