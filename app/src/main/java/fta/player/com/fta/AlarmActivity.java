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
import android.widget.DigitalClock;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    public AlarmActivity mInstance;
    private int hour;
    private int minute;
    private TextView clockAlarm;
    private Switch setAlarm;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        final Calendar c = Calendar.getInstance();

        mInstance = this;
        clockAlarm = (TextView) findViewById(R.id.clockAlarm);
        setAlarm = (Switch) findViewById(R.id.setAlarm);

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        clockAlarm.setText(hour+":"+minute);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    public void onClockClick(View view) {
        showDialog();
    }

    public void showDialog() {
        final Calendar c = Calendar.getInstance();
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
                    clockAlarm.setText(hour+":"+minute);
                    setAlarm();
                }
            };

    public void onSetAlarmClick(View view) {
        if(setAlarm.isChecked()){
            clockAlarm.setTextColor(Color.parseColor("#000000"));
        }else{
            clockAlarm.setTextColor(Color.parseColor("#cccccc"));
            setAlarm();
        }
    }

    private void setAlarm()
    {
        if(setAlarm.isChecked()) {
            Log.d("MyActivity", "Alarm On");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            Intent myIntent = new Intent(MainActivity.instance(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.instance(), 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }else{
            alarmManager.cancel(pendingIntent);

            MainActivity.instance().mp.stop();
            MainActivity.instance().mp.reset();
            Log.d("MyActivity", "Alarm Off");
        }
    }
}
