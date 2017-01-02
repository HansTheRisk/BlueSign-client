package project.bluesign.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import project.bluesign.R;
import project.bluesign.domain.module.Module;
import project.bluesign.domain.signIn.SignIn;
import project.bluesign.service.attendance.AttendanceService;
import project.bluesign.service.settings.SettingsService;

public class StatisticsActivity extends AppCompatActivity {

    private TableLayout modulesTable;
    private LinearLayout historyTableLinearLayout;
    private SettingsService settingsService;
    private AttendanceService attendanceService = new AttendanceService();
    private String[] modulesTableColumnNames = {"Module code:", "Module name:"};
    private String[] historyTableColumnNames = {"Module code:", "Date:"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        modulesTable = (TableLayout)findViewById(R.id.tblModules);
        historyTableLinearLayout = (LinearLayout) findViewById(R.id.tblHistoryLinearView);
        settingsService = new SettingsService(getApplicationContext());
        loadModules();
        loadHistory();
    }

    private void loadModules() {
        List<Module> modules = attendanceService.getUserAttendanceStatistics(settingsService.getId(), settingsService.getPin());

        TableRow titlesRow = new TableRow(this);

        for (String name : modulesTableColumnNames) {
            TextView title = new TextView(this);
            title.setText(name);
            title.setGravity(Gravity.CENTER);
            titlesRow.addView(title, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,16f));
        }

        modulesTable.addView(titlesRow);

        for (Module module : modules) {
            TableRow tableRow = new TableRow(this);

            TextView moduleCode = new TextView(this);
            moduleCode.setText(module.getCode());
            moduleCode.setGravity(Gravity.CENTER);

            TextView moduleName = new TextView(this);
            moduleName.setText(module.getName());
            moduleName.setGravity(Gravity.CENTER);

            tableRow.addView(moduleCode, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));
            tableRow.addView(moduleName, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));

            modulesTable.addView(tableRow);
        }
    }

    private void loadHistory() {
        List<SignIn> signIns = attendanceService.getUserAttendanceHistory(settingsService.getId(), settingsService.getPin());

        TableRow titlesRow = (TableRow) findViewById(R.id.rwTitle);

        for (String name : historyTableColumnNames) {
            TextView title = new TextView(this);
            title.setText(name);
            title.setGravity(Gravity.CENTER);
            titlesRow.addView(title, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,16f));
        }

        for (SignIn signIn : signIns) {
            TableRow tableRow = new TableRow(this);

            TextView moduleCode = new TextView(this);
            moduleCode.setText(signIn.getModule().getCode());
            moduleCode.setGravity(Gravity.CENTER);

            TextView date = new TextView(this);
            date.setText(signIn.getDate().toString());
            date.setGravity(Gravity.CENTER);

            tableRow.addView(moduleCode, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));
            tableRow.addView(date, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));

            historyTableLinearLayout.addView(tableRow);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
