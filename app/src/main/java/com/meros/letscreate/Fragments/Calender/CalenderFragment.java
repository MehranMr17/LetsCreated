package com.meros.letscreate.Fragments.Calender;

import static com.meros.letscreate.Constants.TACK_TB;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.meros.letscreate.Constants;
import com.meros.letscreate.MainActivity;
import com.meros.letscreate.R;
import com.meros.letscreate.Utils.LetFragment;
import com.meros.letscreate.Utils.LetActivity;
import com.meros.letscreate.databinding.FragmentCalenderBinding;
import com.meros.letscreate.databinding.NewTackDialogBinding;
import com.meros.letscreate.databinding.PickTimerDialogBinding;
import com.meros.letscreate.databinding.ScheduleDialogBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalenderFragment extends LetFragment {

    FragmentCalenderBinding binding;
    private static final String TAG = "CalenderFragment";
    TackAdapter adapter;
    ArrayList<TackModel> tacks;
    String date = "", tackDate = "No", tackTime = "No";
    Chip dateChip, timeChip;
    TackModel tackModel = new TackModel();
    TackUrgency tackUrgency = TackUrgency.WHITE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalenderBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        date = String.valueOf(calendar.get(Calendar.YEAR)) +
                String.valueOf(calendar.get(Calendar.MONTH)) +
                String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        initViews();
    }

    private void initViews() {
        binding.calender.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {

            date = String.valueOf(year) +
                    String.valueOf(month) +
                    String.valueOf(dayOfMonth);
            tacks = Constants.getMangeTackTable(getActivity()).getTacks(generateQuery(), new String[]{date});
        });
        tacks = Constants.getMangeTackTable(getActivity()).getTacks(generateQuery(), new String[]{date});
        adapter = new TackAdapter(tacks);
        binding.recTacks.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recTacks.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recTacks);

        binding.btnAddTack.setOnClickListener((view -> {
            openCreateTackDialog();
        }));
    }

    private String generateQuery() {
        return " SELECT * FROM " + TACK_TB + " WHERE " + TackModel.DATE + " = ?";
    }

    public void openCreateTackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        NewTackDialogBinding dialogBinding = NewTackDialogBinding.inflate(LayoutInflater.from(getActivity()));
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation1.setFillAfter(true);
        alphaAnimation1.setDuration(500);
        dialogBinding.getRoot().startAnimation(alphaAnimation1);

        LetActivity activity = ((MainActivity) getActivity());
        ConstraintLayout v = activity.showBackgroundView(true);
        dialog.setCancelable(false);
        dialogBinding.btnOk.setOnClickListener((view -> {
            tackModel.setFinished(false);

            if (!dialogBinding.edtTitle.getText().toString().isEmpty()) {
                tackModel.setTitle(dialogBinding.edtTitle.getText().toString());
            } else {
                Toast(getActivity(), "Enter Tack Title");
                return;
            }

            if (!dialogBinding.edtDescription.getText().toString().isEmpty()) {
                tackModel.setDescription(dialogBinding.edtDescription.getText().toString());
            }

            v.setVisibility(View.GONE);
            dialog.dismiss();
        }));
        dialogBinding.schedule.setOnClickListener((view -> {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(700);
            dialogBinding.getRoot().startAnimation(alphaAnimation);
            openCalendarDialog(dialogBinding);
        }));
        dialogBinding.btnCancel.setOnClickListener((view -> {
            v.setVisibility(View.GONE);
            dialog.dismiss();
        }));

        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation1.setFillAfter(true);
        scaleAnimation1.setDuration(500);
        dialogBinding.white.startAnimation(scaleAnimation1);


        dialogBinding.white.setOnClickListener((view -> {
            if (tackUrgency != TackUrgency.WHITE) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(500);
                dialogBinding.white.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.WHITE;
            }
        }));

        dialogBinding.yellow.setOnClickListener((view -> {

            if (tackUrgency != TackUrgency.YELLOW) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(500);
                dialogBinding.yellow.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.YELLOW;
            }
        }));

        dialogBinding.green.setOnClickListener((view -> {

            if (tackUrgency != TackUrgency.GREEN) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(500);
                dialogBinding.green.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.GREEN;
            }
        }));

        dialogBinding.orange.setOnClickListener((view -> {
            if (tackUrgency != TackUrgency.ORANGE) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(500);
                dialogBinding.orange.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.ORANGE;
            }
        }));

        dialogBinding.red.setOnClickListener((view -> {

            if (tackUrgency != TackUrgency.RED) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(500);
                dialogBinding.red.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.RED;
            }
        }));


    }

    public void changeUrgency(NewTackDialogBinding dialogBinding, TackUrgency tackUrgency) {
        ScaleAnimation onscaleAnimation = new ScaleAnimation(1.5f, 1, 1.5f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        onscaleAnimation.setFillAfter(true);
        onscaleAnimation.setDuration(500);
        if (tackUrgency == TackUrgency.WHITE) {
            dialogBinding.white.startAnimation(onscaleAnimation);
        } else if (tackUrgency == TackUrgency.YELLOW) {
            dialogBinding.yellow.startAnimation(onscaleAnimation);
        } else if (tackUrgency == TackUrgency.GREEN) {
            dialogBinding.green.startAnimation(onscaleAnimation);
        } else if (tackUrgency == TackUrgency.ORANGE) {
            dialogBinding.orange.startAnimation(onscaleAnimation);
        } else if (tackUrgency == TackUrgency.RED) {
            dialogBinding.red.startAnimation(onscaleAnimation);
        }
    }

    public void openCalendarDialog(NewTackDialogBinding newTackDialogBinding) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ScheduleDialogBinding dialogBinding = ScheduleDialogBinding.inflate(LayoutInflater.from(getActivity()));
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation1.setFillAfter(true);
        alphaAnimation1.setDuration(500);
        dialogBinding.getRoot().startAnimation(alphaAnimation1);


        dialog.setCancelable(false);
        dialogBinding.tvDone.setOnClickListener((view -> {
            Toast(getActivity(), "Hello");
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(500);
            newTackDialogBinding.getRoot().startAnimation(alphaAnimation);
            dialog.dismiss();
        }));
        dialogBinding.tvCancel.setOnClickListener((view -> {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(500);
            newTackDialogBinding.getRoot().startAnimation(alphaAnimation);
            dialog.dismiss();
        }));

        dialogBinding.tvTimer.setOnClickListener((view -> {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(700);
            dialogBinding.getRoot().startAnimation(alphaAnimation);
            openTimerDialog(dialogBinding);
        }));

        dialogBinding.calender.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            tackDate = String.valueOf(year) + String.valueOf(month + 1) + String.valueOf(dayOfMonth);
            dialogBinding.calenderDisable.setVisibility(View.GONE);

            changeChip(dialogBinding.today, dialogBinding.today);

            Calendar TodayCalendar = Calendar.getInstance();
            Calendar SelectedCalendar = Calendar.getInstance();
            Calendar TomorrowCalendar = Calendar.getInstance();
            TomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);
            Calendar TreeDayLaterCalendar = Calendar.getInstance();
            TreeDayLaterCalendar.add(Calendar.DAY_OF_MONTH, 3);
            SelectedCalendar.set(Calendar.YEAR, year);
            SelectedCalendar.set(Calendar.MONTH, month);
            SelectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            long TodayDateMillis = TodayCalendar.getTimeInMillis();
            long TomorrowDateMillis = TomorrowCalendar.getTimeInMillis();
            long TreeDayLaterDateMillis = TreeDayLaterCalendar.getTimeInMillis();
            long selectedDateMillis = SelectedCalendar.getTimeInMillis();

            Date TodayDate = new Date(TodayDateMillis);
            Date TomorrowDate = new Date(TomorrowDateMillis);
            Date TreeDayLaterDate = new Date(TreeDayLaterDateMillis);
            Date selectedDate = new Date(selectedDateMillis);

            String TodayDay = DateFormat.format("dd", TodayDate).toString();
            String TomorrowDay = DateFormat.format("dd", TomorrowDate).toString();
            String TreeDayLaterDay = DateFormat.format("dd", TreeDayLaterDate).toString();
            String selectedDay = DateFormat.format("dd", selectedDate).toString();

            String selectedDayOfWeek = DateFormat.format("EEEE", selectedDate).toString();

            Log.i(TAG, "selectedDay: " + selectedDay);
            Log.i(TAG, "TodayDay: " + TodayDay);
            Log.i(TAG, "TomorrowDay: " + TomorrowDay);
            Log.i(TAG, "TreeDayLaterDay: " + TreeDayLaterDay);


            if (TodayDay.equals(selectedDay)) {
                if (dateChip != dialogBinding.today) {
                    changeChip(dateChip, dialogBinding.today);
                    dateChip = dialogBinding.today;
                }
                Log.i(TAG, "today: ");
            } else if (selectedDay.equals(TomorrowDay)) {
                if (dateChip != dialogBinding.tomorrow) {
                    changeChip(dateChip, dialogBinding.tomorrow);
                    dateChip = dialogBinding.tomorrow;
                }
                Log.i(TAG, "tomorrow: ");
            } else if (selectedDay.equals(TreeDayLaterDay)) {
                if (dateChip != dialogBinding.treeDaysLater) {
                    changeChip(dateChip, dialogBinding.treeDaysLater);
                    dateChip = dialogBinding.treeDaysLater;
                }
                Log.i(TAG, "treeDaysLater: ");
            } else if (selectedDayOfWeek.equals("Sunday")) {
                if (dateChip != dialogBinding.thisSunday) {
                    changeChip(dateChip, dialogBinding.thisSunday);
                    dateChip = dialogBinding.thisSunday;
                }
                Log.i(TAG, "thisSunday: ");
            } else {
                dateChip = dialogBinding.noDate;
                changeChip(dateChip, dialogBinding.noDate);
                Log.i(TAG, "noDate: ");
            }
        });


        dateChip = dialogBinding.today;
        changeChip(dialogBinding.noDate, dialogBinding.today);

        dialogBinding.noDate.setOnClickListener((view -> {
            if (dateChip != dialogBinding.noDate) {
                changeChip(dateChip, dialogBinding.noDate);
                dateChip = dialogBinding.noDate;
                tackDate = "";
                dialogBinding.calenderDisable.setVisibility(View.VISIBLE);
            }
        }));

        dialogBinding.today.setOnClickListener((view -> {
            if (dateChip != dialogBinding.today) {
                changeChip(dateChip, dialogBinding.today);
                dateChip = dialogBinding.today;
                Calendar calendar = Calendar.getInstance();
                long dateMillis = calendar.getTimeInMillis();
                Date date1 = new Date(dateMillis);

                String selectedDay = DateFormat.format("dd", date1).toString(); // 05
                String selectedMonthNumber = DateFormat.format("MM", date1).toString(); // 6 --> Month Code as Jan = 0 till Dec = 11.
                String selectedYear = DateFormat.format("yyyy", date1).toString(); // 2021
                tackDate = selectedYear + selectedMonthNumber + selectedDay;

                dialogBinding.calender.setDate(dateMillis);
                dialogBinding.calenderDisable.setVisibility(View.GONE);
            }
        }));

        dialogBinding.tomorrow.setOnClickListener((view -> {
            if (dateChip != dialogBinding.tomorrow) {
                changeChip(dateChip, dialogBinding.tomorrow);
                dateChip = dialogBinding.tomorrow;

                Calendar calendar = Calendar.getInstance();
                long dateMillis = calendar.getTimeInMillis();
                long aDay = 1000 * 60 * 60 * 24;
                dateMillis += aDay;
                Date date1 = new Date(dateMillis);

                String selectedDay = DateFormat.format("dd", date1).toString(); // 05
                String selectedMonthNumber = DateFormat.format("MM", date1).toString(); // 6 --> Month Code as Jan = 0 till Dec = 11.
                String selectedYear = DateFormat.format("yyyy", date1).toString(); // 2021
                tackDate = selectedYear + selectedMonthNumber + selectedDay;

                dialogBinding.calender.setDate(dateMillis);
                dialogBinding.calenderDisable.setVisibility(View.GONE);
            }
        }));

        dialogBinding.treeDaysLater.setOnClickListener((view -> {
            if (dateChip != dialogBinding.treeDaysLater) {
                changeChip(dateChip, dialogBinding.treeDaysLater);
                dateChip = dialogBinding.treeDaysLater;

                Calendar calendar = Calendar.getInstance();
                long dateMillis = calendar.getTimeInMillis();
                long aDay = 1000 * 60 * 60 * 24;
                dateMillis += (aDay * 3);
                Date date1 = new Date(dateMillis);

                String selectedDay = DateFormat.format("dd", date1).toString(); // 05
                String selectedMonthNumber = DateFormat.format("MM", date1).toString(); // 6 --> Month Code as Jan = 0 till Dec = 11.
                String selectedYear = DateFormat.format("yyyy", date1).toString(); // 2021
                tackDate = selectedYear + selectedMonthNumber + selectedDay;

                dialogBinding.calender.setDate(dateMillis);
                dialogBinding.calenderDisable.setVisibility(View.GONE);
            }
        }));

        dialogBinding.thisSunday.setOnClickListener((view -> {
            if (dateChip != dialogBinding.thisSunday) {
                changeChip(dateChip, dialogBinding.thisSunday);
                dateChip = dialogBinding.thisSunday;

                Calendar calendar = Calendar.getInstance();
                long dateMillis = calendar.getTimeInMillis();
                long aDay = 1000 * 60 * 60 * 24;
                Date date1 = new Date(dateMillis);

                String selectedDayOfWeek = DateFormat.format("EEEE", date1).toString(); // Monday
                while (!selectedDayOfWeek.equals("Sunday")) {
                    dateMillis += aDay;
                    date1 = new Date(dateMillis);
                    selectedDayOfWeek = DateFormat.format("EEEE", date1).toString();
                }
                String selectedDay = DateFormat.format("dd", date1).toString(); // 05
                String selectedMonthNumber = DateFormat.format("MM", date1).toString(); // 6 --> Month Code as Jan = 0 till Dec = 11.
                String selectedYear = DateFormat.format("yyyy", date1).toString(); // 2021
                tackDate = selectedYear + selectedMonthNumber + selectedDay;

                dialogBinding.calender.setDate(dateMillis);
                dialogBinding.calenderDisable.setVisibility(View.GONE);
            }
        }));
    }

    public void changeChip(Chip oldChip, Chip newChip) {
        if (newChip.getId() != oldChip.getId()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                newChip.setChipBackgroundColor(ColorStateList.valueOf(getActivity().getColor(R.color.blue)));
            }
        }
        newChip.setChecked(true);
        oldChip.setChecked(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            oldChip.setChipBackgroundColor(ColorStateList.valueOf(getActivity().getColor(R.color.lightBlue)));
        }
    }

    public void openTimerDialog(ScheduleDialogBinding scheduleDialogBinding) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        PickTimerDialogBinding dialogBinding = PickTimerDialogBinding.inflate(LayoutInflater.from(getActivity()));
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation1.setFillAfter(true);
        alphaAnimation1.setDuration(500);
        dialogBinding.getRoot().startAnimation(alphaAnimation1);


        dialog.setCancelable(false);
        dialogBinding.tvDone.setOnClickListener((view -> {
            scheduleDialogBinding.tvTimerLength.setText(tackTime);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(500);
            scheduleDialogBinding.getRoot().startAnimation(alphaAnimation);
            Log.i(TAG, "tackTime: " + tackTime);
            dialog.dismiss();
        }));
        dialogBinding.tvCancel.setOnClickListener((view -> {
            tackTime = "No";
            Toast(getActivity(), tackTime);
            scheduleDialogBinding.tvTimerLength.setText(tackTime);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(500);
            scheduleDialogBinding.getRoot().startAnimation(alphaAnimation);
            dialog.dismiss();
        }));


        dialogBinding.timePicker.setOnTimeChangedListener((timePicker, hour, min) -> {
            dialogBinding.timerDisable.setVisibility(View.GONE);
            changeChip(dialogBinding.seven, dialogBinding.seven);

            Date currentTime = Calendar.getInstance().getTime();
            Time SevenOClock = new Time();
            SevenOClock.set(0, 0, 7, 0, 0, 0);
            Time NineOClock = new Time();
            NineOClock.set(0, 0, 9, 0, 0, 0);
            Time TwelveOClock = new Time();
            TwelveOClock.set(0, 0, 12, 0, 0, 0);
            Time EighteenOClock = new Time();
            EighteenOClock.set(0, 0, 18, 0, 0, 0);
            Time TwentyThreeOClock = new Time();
            TwentyThreeOClock.set(0, 0, 23, 0, 0, 0);


//            if (min == 0) {
                if (hour == SevenOClock.hour) {
                    if (timeChip != dialogBinding.seven) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dialogBinding.timePicker.setMinute(0);
                        }
                        changeChip(timeChip, dialogBinding.seven);
                        timeChip = dialogBinding.seven;
                        tackTime = "07:00";
                        scheduleDialogBinding.tvTimerLength.setText(tackTime);
                    }
                    return;
                }
                else if (hour == NineOClock.hour) {
                    if (timeChip != dialogBinding.nine) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dialogBinding.timePicker.setMinute(0);
                        }
                        changeChip(timeChip, dialogBinding.nine);
                        timeChip = dialogBinding.nine;
                        tackTime = "09:00";
                        scheduleDialogBinding.tvTimerLength.setText(tackTime);
                    }
                    return;
                }
                else if (hour == TwelveOClock.hour) {
                    if (timeChip != dialogBinding.twelve) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dialogBinding.timePicker.setMinute(0);
                        }
                        changeChip(timeChip, dialogBinding.twelve);
                        timeChip = dialogBinding.twelve;
                        tackTime = "12:00";
                        scheduleDialogBinding.tvTimerLength.setText(tackTime);
                    }
                    return;
                }
                else if (hour == EighteenOClock.hour) {
                    if (timeChip != dialogBinding.eghty) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dialogBinding.timePicker.setMinute(0);
                        }
                        changeChip(timeChip, dialogBinding.eghty);
                        timeChip = dialogBinding.eghty;
                        tackTime = "18:00";
                        scheduleDialogBinding.tvTimerLength.setText(tackTime);
                    }
                    return;
                }
                else if (hour == TwentyThreeOClock.hour) {
                    if (timeChip != dialogBinding.tventyTree) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dialogBinding.timePicker.setMinute(0);
                        }
                        changeChip(timeChip, dialogBinding.tventyTree);
                        timeChip = dialogBinding.tventyTree;
                        tackTime = "23:00";
                        scheduleDialogBinding.tvTimerLength.setText(tackTime);
                    }
                    return;
                }
                else if (hour == currentTime.getHours()) {
                    if (timeChip != dialogBinding.now) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dialogBinding.timePicker.setMinute(0);
                        }
                        changeChip(timeChip, dialogBinding.now);
                        timeChip = dialogBinding.now;
                        tackTime = String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes());
                        scheduleDialogBinding.tvTimerLength.setText(tackTime);
                    }
                    return;
                }
//            }
            Log.i(TAG, "openTimerDialog: "+min);
            tackTime = String.valueOf(hour) + ":" + String.valueOf(min);
            scheduleDialogBinding.tvTimerLength.setText(tackTime);

        });


        timeChip = dialogBinding.now;
        changeChip(dialogBinding.noTime, dialogBinding.now);
        Date currentTime = Calendar.getInstance().getTime();


        tackTime = String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes());


        dialogBinding.noTime.setOnClickListener((view -> {
            if (timeChip != dialogBinding.noTime) {
                changeChip(timeChip, dialogBinding.noTime);
                timeChip = dialogBinding.noTime;
                tackTime = "No";
                dialogBinding.timerDisable.setVisibility(View.VISIBLE);
            }
        }));

        dialogBinding.now.setOnClickListener((view -> {
            if (timeChip != dialogBinding.now) {
                changeChip(timeChip, dialogBinding.now);
                timeChip = dialogBinding.now;
                Date currentTime2 = Calendar.getInstance().getTime();


                tackTime = String.valueOf(currentTime2.getHours()) + ":" + String.valueOf(currentTime2.getMinutes());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(currentTime.getHours());
                    dialogBinding.timePicker.setMinute(currentTime.getMinutes());
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.seven.setOnClickListener((view -> {
            if (timeChip != dialogBinding.seven) {
                changeChip(timeChip, dialogBinding.seven);
                timeChip = dialogBinding.seven;
                Time SevenOClock = new Time();
                SevenOClock.set(0, 0, 7, 0, 0, 0);

                tackTime = "07:00";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(SevenOClock.hour);
                    dialogBinding.timePicker.setMinute(0);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.nine.setOnClickListener((view -> {
            if (timeChip != dialogBinding.nine) {
                changeChip(timeChip, dialogBinding.nine);
                timeChip = dialogBinding.nine;
                Time NineOClock = new Time();
                NineOClock.set(0, 0, 9, 0, 0, 0);

                tackTime = "09:00";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(NineOClock.hour);
                    dialogBinding.timePicker.setMinute(0);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.twelve.setOnClickListener((view -> {
            if (timeChip != dialogBinding.twelve) {
                changeChip(timeChip, dialogBinding.twelve);
                timeChip = dialogBinding.twelve;
                Time TwelveOClock = new Time();
                TwelveOClock.set(0, 0, 12, 0, 0, 0);

                tackTime = "12:00";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(TwelveOClock.hour);
                    dialogBinding.timePicker.setMinute(0);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.eghty.setOnClickListener((view -> {
            if (timeChip != dialogBinding.eghty) {
                changeChip(timeChip, dialogBinding.eghty);
                timeChip = dialogBinding.eghty;
                Time EighteenOClock = new Time();
                EighteenOClock.set(0, 0, 18, 0, 0, 0);

                tackTime = "18:00";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(EighteenOClock.hour);
                    dialogBinding.timePicker.setMinute(0);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.tventyTree.setOnClickListener((view -> {
            if (timeChip != dialogBinding.tventyTree) {
                changeChip(timeChip, dialogBinding.tventyTree);
                timeChip = dialogBinding.tventyTree;
                Time TwentyThreeOClock = new Time();
                TwentyThreeOClock.set(0, 0, 23, 0, 0, 0);

                tackTime = "23:00";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(TwentyThreeOClock.hour);
                    dialogBinding.timePicker.setMinute(0);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));

    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN, ItemTouchHelper.START) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.notifyItemChanged(viewHolder.getPosition());
        }
    };
}
