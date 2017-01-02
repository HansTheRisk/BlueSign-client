package project.bluesign.domain.signIn;

import java.util.Date;

import project.bluesign.domain.module.Module;

public class SignIn {
    private Date date;
    private Module module;

    public SignIn(Module module, Date date) {
        this.module = module;
        this.date = date;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
