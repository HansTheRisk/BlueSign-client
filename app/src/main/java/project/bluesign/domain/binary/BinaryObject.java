package project.bluesign.domain.binary;

public class BinaryObject {

    private boolean value = false;

    public BinaryObject() {};
    public BinaryObject(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
