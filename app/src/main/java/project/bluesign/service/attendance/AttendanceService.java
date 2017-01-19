package project.bluesign.service.attendance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import project.bluesign.domain.module.Module;
import project.bluesign.domain.signIn.SignIn;

public class AttendanceService {

    public List<Module> getUserAttendanceStatistics(String id) {

        List<Module> modules = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            modules.add(new Module("module" + i, "code " + i, 5, 15));
        }

        return modules;
    }

    public List<SignIn> getUserAttendanceHistory(String id) {

        List<SignIn> signIn = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            signIn.add(new SignIn(new Module("module" + i, "code " + i, 10, 15), new Date(2, 2017, 13, 11, 1)));
        }
        return signIn;
    }
}
