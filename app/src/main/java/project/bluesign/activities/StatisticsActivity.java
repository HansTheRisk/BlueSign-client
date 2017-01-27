package project.bluesign.activities;

import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import project.bluesign.R;
import project.bluesign.service.attendance.AttendanceService;
import project.bluesign.service.settings.SettingsService;

public class StatisticsActivity extends AppCompatActivity {

    private TableLayout modulesTable;
    private LinearLayout historyTableLinearLayout;
    private SettingsService settingsService;
    private AttendanceService attendanceService;
    private TextView status;
    private String[] modulesTableColumnNames = {"Module code:", "Total:", "Attended:", "%:"};
    private String[] historyTableColumnNames = {"History:"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_statistics);
        modulesTable = (TableLayout)findViewById(R.id.tblModules);
        historyTableLinearLayout = (LinearLayout) findViewById(R.id.tblHistoryLinearView);
        attendanceService = new AttendanceService();
        settingsService = new SettingsService(StatisticsActivity.this);
        status = (TextView) findViewById(R.id.txtStatus);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadModules();
        loadHistory();
    }

    private void loadModules() {
        TableRow titlesRow = new TableRow(this);

        for (String name : modulesTableColumnNames) {
            TextView title = new TextView(this);
            title.setText(name);
            title.setGravity(Gravity.CENTER);
            title.setTextSize(18f);
            titlesRow.addView(title, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 16f));
        }
        modulesTable.addView(titlesRow);
        attendanceService.loadUserAttendanceStatistics(getApplicationContext(), status, modulesTable, settingsService.getId());

    }

    private void loadHistory() {
        TableRow titlesRow = (TableRow) findViewById(R.id.rwTitle);

        for (String name : historyTableColumnNames) {
            TextView title = new TextView(this);
            title.setText(name);
            title.setTextSize(18f);
            title.setGravity(Gravity.LEFT);
            titlesRow.addView(title, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,16f));
        }
        attendanceService.loaduserAttendanceHistory(getApplicationContext(), status, historyTableLinearLayout, settingsService.getId());
    }
}
