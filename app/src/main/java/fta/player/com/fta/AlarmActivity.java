package fta.player.com.fta;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DigitalClock;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity {

    public AlarmActivity mInstance;
    private int hour;
    private int minute;
    private TextView clockAlarm;
    private Switch setAlarm;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Spinner radioList;

    private Timer timer = new Timer();
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm);
        c = Calendar.getInstance();

        mInstance = this;
        clockAlarm = (TextView) findViewById(R.id.clockAlarm);
        setAlarm = (Switch) findViewById(R.id.setAlarm);
        radioList = (Spinner) findViewById(R.id.radioList);

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        setAlarmText();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //radioList.add
       // timer = new Timer();
       // timer.schedule(ts, 0, 1000);
    }

    //private TimerTask ts = new TimerTask() {
    //    @Override
    //    public void run() {
    //            c = Calendar.getInstance();
    //            hour = c.get(Calendar.HOUR_OF_DAY);
    //            minute = c.get(Calendar.MINUTE);
    //            setAlarmText();
    //    }
    //};

    public void onClockClick(View view) {
        showDialog();
    }

    public void showDialog() {
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(mInstance, timePickerListener, hour, minute, true);
        timePickerDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    setAlarmText();
                }
            };

    public void onSetAlarmClick(View view) {
        if(setAlarm.isChecked()){
            clockAlarm.setTextColor(Color.parseColor("#000000"));
        }else{
            clockAlarm.setTextColor(Color.parseColor("#cccccc"));
        }
    }

    private void setAlarmText()
    {
        clockAlarm.setText(hour+":"+(minute > 9 ? minute : "0"+minute));
    }

    private void setAlarm()
    {

    }

    public void cancelChanges(View view) {
        alarmManager.cancel(pendingIntent);
        MainActivity.instance().mp.stop();
        MainActivity.instance().mp.reset();
        Log.d("MyActivity", "Alarm Off");

        finish();
    }

    public void submitChanges(View view) {
        if(setAlarm.isChecked()) {
            Log.d("MyActivity", "Alarm On");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            Intent myIntent = new Intent(MainActivity.instance(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.instance(), 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        finish();
    }
}
