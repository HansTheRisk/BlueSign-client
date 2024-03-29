package project.bluesign.service.attendance;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import project.bluesign.activities.LectureCodeActivity;
import project.bluesign.activities.StatisticsActivity;
import project.bluesign.domain.accessCode.AccessCode;
import project.bluesign.domain.message.Message;
import project.bluesign.domain.module.Module;
import project.bluesign.domain.signIn.SignIn;
import project.bluesign.service.HttpsClient;

import static project.bluesign.constant.GlobalVariables.STUDENT_ENDPOINT;

public class AttendanceService {

    private final String STATISTICS_ENDPOINT = "mobileMetrics";
    private final String ATTENDANCE_HISTORY_ENDPOINT = "history";
    private final String ATTENDANCE_SIGN_IN = "signIn";

    public void loadUserAttendanceStatistics(StatisticsActivity activity, String studentId, String studentPin) {
        new StatisticsGetter(activity).execute(studentId, studentPin);
    }

    public void loaduserAttendanceHistory(StatisticsActivity activity, String studentId, String studentPin) {
        new AttendanceHistoryGetter(activity).execute(studentId, studentPin);
    }

//    public void signIn(TextView statusText, Button acceptButton, String id, String pin, EditText code, InputMethodManager imm) {
//        new SignInExecutor(statusText, acceptButton, code, imm).execute(id, pin);
//    }

    public void signIn(LectureCodeActivity activity, String id, String pin) {
        new SignInExecutor(activity).execute(id, pin);
    }

    private class SignInExecutor extends AsyncTask<String, Void, Message> {

        private LectureCodeActivity activity;
        private String accessCode;

        SignInExecutor(LectureCodeActivity activity) {
            this.activity = activity;
            accessCode = activity.getCode().getText().toString();
        }

        @Override
        protected void onPreExecute() {
            activity.getCode().setEnabled(false);
            activity.getAccept().setEnabled(false);
            activity.getInfo().setTextColor(Color.GREEN);
            activity.getInfo().setText("Please wait...");
        }

        @Override
        protected Message doInBackground(String... params) {
            try {
                final String url = STUDENT_ENDPOINT + "/" + params[0]  + "/" + params[1] + "/" + ATTENDANCE_SIGN_IN;
                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(HttpsClient.getNewHttpClient());
                requestFactory.setConnectTimeout(5000);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setRequestFactory(requestFactory);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                Message message = restTemplate.postForObject(url, new AccessCode(Integer.valueOf(accessCode)), Message.class);
                message.setResponseCode(200);
                return message;
            }
            catch (HttpClientErrorException e) {
                try {
                    JSONObject object = new JSONObject(e.getResponseBodyAsString());
                    return new Message(object.getString("message"), e.getStatusCode().value());
                } catch (Exception e1) {
                    return null;
                }
            }
            catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Message message) {

            TextView statusText = activity.getInfo();
            EditText code = activity.getCode();

            if (message == null) {
                statusText.setTextColor(Color.RED);
                statusText.setText("Something went wrong! Try Again.");
                code.selectAll();
                code.requestFocus();
                activity.getImm().toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
            else {
                if (message.getResponseCode() == 200) {
                    statusText.setTextColor(Color.GREEN);
                }
                else {
                    statusText.setTextColor(Color.RED);
                    code.selectAll();
                    code.requestFocus();
                    activity.getImm().toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                statusText.setText(message.getMessage());
            }
            activity.getAccept().setEnabled(true);
            code.setEnabled(true);
        }
    }

    private class StatisticsGetter extends AsyncTask<String, Void, List<Module>> {

        private StatisticsActivity activity;

        StatisticsGetter(StatisticsActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            activity.loadingInitiated();
        }

        @Override
        protected List<Module> doInBackground(String... params) {
            try {
                final String url = STUDENT_ENDPOINT + "/" + params[0] + "/" + params[1] + "/" + STATISTICS_ENDPOINT;
                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(HttpsClient.getNewHttpClient());
                requestFactory.setConnectTimeout(3000);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setRequestFactory(requestFactory);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Module[] modules = restTemplate.getForObject(url, Module[].class);
                return Arrays.asList(modules);

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Module> modules) {

            TableLayout table = activity.getModulesTable();
            Context context = activity.getApplicationContext();

            if (modules == null) {
                TableRow tableRow = new TableRow(context);
                TextView error = new TextView(context);
                error.setTextColor(Color.RED);
                error.setText("Something went wrong!");
                error.setGravity(Gravity.CENTER);
                tableRow.addView(error);
                table.addView(tableRow);
            }
            else if(modules.isEmpty()) {
                TableRow tableRow = new TableRow(context);
                TextView error = new TextView(context);
                error.setTextColor(Color.BLUE);
                error.setText("Nothing to show!");
                error.setGravity(Gravity.CENTER);
                tableRow.addView(error);
                table.addView(tableRow);
            }
            else {
                for (Module module : modules) {
                    TableRow tableRow = new TableRow(context);
                    TextView moduleCode = new TextView(context);
                    moduleCode.setTextColor(Color.BLACK);
                    moduleCode.setText(module.getModuleCode());
                    moduleCode.setGravity(Gravity.CENTER);

                    TextView total = new TextView(context);
                    total.setTextColor(Color.BLACK);
                    total.setText(String.valueOf(module.getTotalToDate()));
                    total.setGravity(Gravity.CENTER);

                    TextView attended = new TextView(context);
                    attended.setTextColor(Color.BLACK);
                    attended.setText(String.valueOf(module.getTotalAttended()));
                    attended.setGravity(Gravity.CENTER);

                    TextView attendancePercentage = new TextView(context);
                    Double percent = Double.valueOf(Double.valueOf(module.getTotalAttended()) / Double.valueOf(module.getTotalToDate())) * 100;

                    if (percent < 50)
                        attendancePercentage.setTextColor(Color.RED);
                    else {
                        attendancePercentage.setTextColor(Color.GREEN);
                    }

                    attendancePercentage.setText(String.valueOf(percent.intValue()) + "%");
                    attendancePercentage.setGravity(Gravity.CENTER);

                    tableRow.addView(moduleCode, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));
                    tableRow.addView(total, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));
                    tableRow.addView(attended, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));
                    tableRow.addView(attendancePercentage, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));

                    table.addView(tableRow);
                }
            }
            activity.moduleStatisticsLoaded();
        }
    }

    private class AttendanceHistoryGetter extends AsyncTask<String, Void, List<SignIn>> {

        private StatisticsActivity activity;

        AttendanceHistoryGetter(StatisticsActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            activity.loadingInitiated();
        }

        @Override
        protected List<SignIn> doInBackground(String... params) {
            try {
                final String url = STUDENT_ENDPOINT + "/" + params[0] + "/" + params[1] + "/" + ATTENDANCE_HISTORY_ENDPOINT;

                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(HttpsClient.getNewHttpClient());
                requestFactory.setConnectTimeout(300);
                RestTemplate restTemplate = new RestTemplate(requestFactory);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                SignIn[] attendance = restTemplate.getForObject(url, SignIn[].class);
                return Arrays.asList(attendance);

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<SignIn> signIns) {

            LinearLayout historyTableLinearLayout = activity.getHistoryTable();
            Context context = activity.getApplicationContext();

            if (signIns == null) {
                TableRow tableRow = new TableRow(context);
                TextView error = new TextView(context);
                error.setTextColor(Color.RED);
                error.setText("Something went wrong!");
                error.setGravity(Gravity.CENTER);
                tableRow.addView(error);
                historyTableLinearLayout.addView(tableRow);
            }
            else if(signIns.isEmpty()) {
                TableRow tableRow = new TableRow(context);
                TextView error = new TextView(context);
                error.setTextColor(Color.BLUE);
                error.setText("Nothing to show!");
                error.setGravity(Gravity.CENTER);
                tableRow.addView(error);
                historyTableLinearLayout.addView(tableRow);
            }
            else {
                Collections.sort(signIns, new Comparator<SignIn>() {
                    @Override
                    public int compare(SignIn o1, SignIn o2) {
                        if(o1.getDateTimestamp() > o2.getDateTimestamp())
                            return -1;
                        else if(o1.getDateTimestamp() < o2.getDateTimestamp())
                            return 1;
                        else
                            return 0;
                    }
                });
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                for (SignIn signIn : signIns) {
                    TableRow tableRow = new TableRow(context);
                    tableRow.setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView moduleCode = new TextView(context);
                    moduleCode.setTextColor(Color.BLACK);
                    moduleCode.setText(signIn.getModuleCode());
                    moduleCode.setGravity(Gravity.LEFT);

                    TextView date = new TextView(context);
                    Date dateObj = new Date(signIn.getDateTimestamp());
                    date.setTextColor(Color.BLACK);
                    date.setText(dateFormat.format(dateObj).trim());
                    date.setGravity(Gravity.RIGHT);

                    tableRow.addView(moduleCode, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 12f));
                    tableRow.addView(date, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 12f));
                    historyTableLinearLayout.addView(tableRow);
                }
            }
            activity.historyLoaded();
        }
    }
}
