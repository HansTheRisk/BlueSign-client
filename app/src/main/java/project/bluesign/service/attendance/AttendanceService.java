package project.bluesign.service.attendance;

import java.util.ArrayList;
import java.util.List;

import project.bluesign.domain.module.Module;
import project.bluesign.domain.module.ModuleMetrics;

public class AttendanceService {

    public List<Module> getUserAttendance(String id, String pin) {

        List<Module> modules = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            modules.add(new Module("module" + i, "code " + i, new ModuleMetrics()));
        }

        return modules;
    }
}
