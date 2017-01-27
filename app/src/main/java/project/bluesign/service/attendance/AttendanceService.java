package project.bluesign.service.attendance;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import project.bluesign.domain.accessCode.AccessCode;
import project.bluesign.domain.message.Message;
import project.bluesign.domain.module.Module;
import project.bluesign.domain.signIn.SignIn;

import static project.bluesign.constant.GlobalVariables.STUDENT_ENDPOINT;

public class AttendanceService {

    private final String STATISTICS_ENDPOINT = "mobileMetrics";
    private final String ATTENDANCE_HISTORY_ENDPOINT = "history";
    private final String ATTENDANCE_SIGN_IN = "signIn";

    public void loadUserAttendanceStatistics(Context uiContext, TextView statusTextView, TableLayout tableToLoadDateTo, String studentId) {
        new StatisticsGetter(uiContext, statusTextView, tableToLoadDateTo).execute(studentId);
    }

    public void loaduserAttendanceHistory(Context uiContext, TextView statusTextView, LinearLayout linearLayout, String studentId) {
        new AttendanceHistoryGetter(uiContext, statusTextView, linearLayout).execute(studentId);
    }

    public void signIn(TextView statusText, Button acceptButton, String id, String pin, String accessCode) {
        new SignInExecutor(statusText, acceptButton).execute(id, pin, accessCode.trim());
    }


    private class SignInExecutor extends AsyncTask<String, Void, Message> {

        private Button acceptButton;
        private TextView statusText;

        SignInExecutor(TextView statusText, Button acceptButton) {
            this.statusText = statusText;
            this.acceptButton = acceptButton;
        }

        @Override
        protected void onPreExecute() {
            acceptButton.setEnabled(false);
            statusText.setTextColor(Color.GREEN);
            statusText.setText("Please wait...");
        }

        @Override
        protected Message doInBackground(String... params) {
            try {
                final String url = STUDENT_ENDPOINT + "/" + params[0]  + "/" + params[1] + "/" + ATTENDANCE_SIGN_IN;
                HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                factory.setConnectTimeout(5000);
                RestTemplate restTemplate = new RestTemplate(factory);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                Message message = restTemplate.postForObject(url, new AccessCode(Integer.valueOf(params[2])), Message.class);
                message.setResponseCode(200);
                return message;
            } catch (HttpClientErrorException e) {
                try {
                    JSONObject object = new JSONObject(e.getResponseBodyAsString());
                    return new Message(object.getString("message"), e.getStatusCode().value());
                } catch (JSONException e1) {
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(Message message) {
            if (message == null) {
                statusText.setTextColor(Color.GREEN);
                statusText.setText("Something went wrong! Try Again.");
            }
            else {
                statusText.setTextColor(Color.RED);
                if (message.getResponseCode() == 200)
                    statusText.setTextColor(Color.GREEN);
                statusText.setText(message.getMessage());
            }
            acceptButton.setEnabled(true);
        }
    }

    private class StatisticsGetter extends AsyncTask<String, Void, List<Module>> {

        private TableLayout table;
        private Context context;
        private TextView statusText;

        StatisticsGetter(Context context, TextView statusText, TableLayout table) {
            this.statusText = statusText;
            this.table = table;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            statusText.setTextColor(Color.GREEN);
            statusText.setText("Please wait...");
        }

        @Override
        protected List<Module> doInBackground(String... params) {
            try {
                final String url = STUDENT_ENDPOINT + "/" + params[0] + "/" + STATISTICS_ENDPOINT;
                HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                factory.setConnectTimeout(3000);
                RestTemplate restTemplate = new RestTemplate(factory);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Module[] modules = restTemplate.getForObject(url, Module[].class);
                return Arrays.asList(modules);

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Module> modules) {
            if (modules == null) {
                TableRow tableRow = new TableRow(context);
                TextView error = new TextView(context);
                error.setTextColor(Color.RED);
                error.setText("SOMETHING WENT WRONG!");
                error.setGravity(Gravity.CENTER);
                tableRow.addView(error);
                table.addView(tableRow);
            }else {
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

                    attendancePercentage.setText(String.valueOf(percent) + "%");
                    attendancePercentage.setGravity(Gravity.CENTER);

                    tableRow.addView(moduleCode, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));
                    tableRow.addView(total, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));
                    tableRow.addView(attended, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));
                    tableRow.addView(attendancePercentage, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 12f));

                    table.addView(tableRow);
                }
            }
            statusText.setText("Done!");
        }
    }

    private class AttendanceHistoryGetter extends AsyncTask<String, Void, List<SignIn>> {

        private LinearLayout historyTableLinearLayout;
        private Context context;
        private TextView statusText;

        AttendanceHistoryGetter(Context context, TextView statusText, LinearLayout historyTableLinearLayout) {
            this.statusText = statusText;
            this.historyTableLinearLayout = historyTableLinearLayout;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            statusText.setTextColor(Color.GREEN);
            statusText.setText("Please wait...");
        }

        @Override
        protected List<SignIn> doInBackground(String... params) {
            try {
                final String url = STUDENT_ENDPOINT + "/" + params[0] + "/" + ATTENDANCE_HISTORY_ENDPOINT;
                HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                factory.setConnectTimeout(300);
                RestTemplate restTemplate = new RestTemplate(factory);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                SignIn[] attendance = restTemplate.getForObject(url, SignIn[].class);
                return Arrays.asList(attendance);

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<SignIn> signIns) {
            if (signIns == null) {
                TableRow tableRow = new TableRow(context);
                TextView error = new TextView(context);
                error.setTextColor(Color.RED);
                error.setText("SOMETHING WENT WRONG!");
                error.setGravity(Gravity.CENTER);
                tableRow.addView(error);
                historyTableLinearLayout.addView(tableRow);
            }
            else {
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
            statusText.setText("Done!");
        }
    }
}
