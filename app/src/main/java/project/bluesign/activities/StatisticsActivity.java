package project.bluesign.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import project.bluesign.R;
import project.bluesign.domain.module.Module;
import project.bluesign.service.attendance.AttendanceService;
import project.bluesign.service.settings.SettingsService;

public class StatisticsActivity extends AppCompatActivity {

    private TableLayout table;
    private SettingsService settingsService;
    private AttendanceService attendanceService = new AttendanceService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        table = (TableLayout)findViewById(R.id.tblModules);
        settingsService = new SettingsService(getApplicationContext());
        loadModules();
    }

    private void loadModules() {
        List<Module> modules = attendanceService.getUserAttendance(settingsService.getId(), settingsService.getPin());
        for (Module module : modules) {
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(lp);

            TextView moduleCode = new TextView(this);
            moduleCode.setText(module.getCode());

            TextView moduleName = new TextView(this);
            moduleName.setText(module.getName());

            tableRow.addView(moduleCode);
            tableRow.addView(moduleName);

            table.addView(tableRow);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
