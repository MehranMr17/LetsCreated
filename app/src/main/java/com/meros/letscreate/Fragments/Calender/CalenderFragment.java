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
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

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
            dialogBinding.getRoot().setVisibility(View.GONE);
            openCalendarDialog(dialogBinding);
        }));
        dialogBinding.btnCancel.setOnClickListener((view -> {
            v.setVisibility(View.GONE);
            dialog.dismiss();
        }));

        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation1.setFillAfter(true);
        scaleAnimation1.setDuration(700);
        dialogBinding.white.startAnimation(scaleAnimation1);


        dialogBinding.white.setOnClickListener((view -> {
            if (tackUrgency != TackUrgency.WHITE) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(700);
                dialogBinding.white.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.WHITE;
            }
        }));

        dialogBinding.yellow.setOnClickListener((view -> {

            if (tackUrgency != TackUrgency.YELLOW) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(700);
                dialogBinding.yellow.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.YELLOW;
            }
        }));

        dialogBinding.green.setOnClickListener((view -> {

            if (tackUrgency != TackUrgency.GREEN) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(700);
                dialogBinding.green.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.GREEN;
            }
        }));

        dialogBinding.orange.setOnClickListener((view -> {
            if (tackUrgency != TackUrgency.ORANGE) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(700);
                dialogBinding.orange.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.ORANGE;
            }
        }));

        dialogBinding.red.setOnClickListener((view -> {

            if (tackUrgency != TackUrgency.RED) {
                changeUrgency(dialogBinding, tackUrgency);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(700);
                dialogBinding.red.startAnimation(scaleAnimation);
                tackUrgency = TackUrgency.RED;
            }
        }));


    }

    public void changeUrgency(NewTackDialogBinding dialogBinding, TackUrgency tackUrgency) {
        ScaleAnimation onscaleAnimation = new ScaleAnimation(1.5f, 1, 1.5f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        onscaleAnimation.setFillAfter(true);
        onscaleAnimation.setDuration(700);
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

        dialog.setCancelable(false);
        dialogBinding.tvDone.setOnClickListener((view -> {
            Toast(getActivity(), "Hello");
            newTackDialogBinding.getRoot().setVisibility(View.VISIBLE);
            dialog.dismiss();
        }));
        dialogBinding.tvCancel.setOnClickListener((view -> {
            newTackDialogBinding.getRoot().setVisibility(View.VISIBLE);
            dialog.dismiss();
        }));

        dialogBinding.tvTimer.setOnClickListener((view -> {
            dialogBinding.getRoot().setVisibility(View.GONE);
            openTimerDialog(dialogBinding);
        }));

        dialogBinding.calender.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            tackDate = String.valueOf(year) + String.valueOf(month + 1) + String.valueOf(dayOfMonth);
            dialogBinding.calenderDisable.setVisibility(View.GONE);

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

        dialog.setCancelable(false);
        dialogBinding.tvDone.setOnClickListener((view -> {
            scheduleDialogBinding.getRoot().setVisibility(View.VISIBLE);
            Toast(getActivity(), "Hello");
            dialog.dismiss();
        }));
        dialogBinding.tvCancel.setOnClickListener((view -> {
            scheduleDialogBinding.getRoot().setVisibility(View.VISIBLE);
            dialog.dismiss();
        }));


        dialogBinding.timePicker.setOnTimeChangedListener((timePicker, hour, min) -> {
            tackTime = String.valueOf(hour) + ":" + String.valueOf(min);
            dialogBinding.timerDisable.setVisibility(View.GONE);

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


            if (min == 0) {
                if (hour == SevenOClock.hour) {
                    if (timeChip != dialogBinding.seven) {
                        changeChip(timeChip, dialogBinding.seven);
                        timeChip = dialogBinding.seven;
                    }
                } 
                else if (hour == NineOClock.hour) {
                    if (timeChip != dialogBinding.nine) {
                        changeChip(timeChip, dialogBinding.nine);
                        timeChip = dialogBinding.nine;
                    }
                } 
                else if (hour == TwelveOClock.hour) {
                    if (timeChip != dialogBinding.twelve) {
                        changeChip(timeChip, dialogBinding.twelve);
                        timeChip = dialogBinding.twelve;
                    }
                } 
                else if (hour == EighteenOClock.hour) {
                    if (timeChip != dialogBinding.eghty) {
                        changeChip(timeChip, dialogBinding.eghty);
                        timeChip = dialogBinding.eghty;
                    }
                } 
                else if (hour == TwentyThreeOClock.hour) {
                    if (timeChip != dialogBinding.tventyTree) {
                        changeChip(timeChip, dialogBinding.tventyTree);
                        timeChip = dialogBinding.tventyTree;
                    }
                }
                else if (hour == currentTime.getHours()) {
                    if (timeChip != dialogBinding.now) {
                        changeChip(timeChip, dialogBinding.now);
                        timeChip = dialogBinding.now;
                    }
                }
            }

        });


        timeChip = dialogBinding.now;
        changeChip(dialogBinding.noTime, dialogBinding.now);

        dialogBinding.noTime.setOnClickListener((view -> {
            if (timeChip != dialogBinding.noTime) {
                changeChip(timeChip, dialogBinding.noTime);
                timeChip = dialogBinding.noTime;
                tackTime = "No";
                scheduleDialogBinding.tvTimerLength.setText(tackTime);
                dialogBinding.timePicker.setVisibility(View.VISIBLE);
            }
        }));

        dialogBinding.now.setOnClickListener((view -> {
            if (timeChip != dialogBinding.now) {
                changeChip(timeChip, dialogBinding.now);
                timeChip = dialogBinding.now;
                scheduleDialogBinding.tvTimerLength.setText(tackTime);
                Date currentTime = Calendar.getInstance().getTime();


                tackTime = String.valueOf(currentTime.getHours()) +":"+String.valueOf(currentTime.getMinutes());

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
                scheduleDialogBinding.tvTimerLength.setText(tackTime);
                Time SevenOClock = new Time();
                SevenOClock.set(0, 0, 7, 0, 0, 0);

                tackTime = String.valueOf(SevenOClock.hour) +":"+String.valueOf(SevenOClock.minute);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(SevenOClock.hour);
                    dialogBinding.timePicker.setMinute(SevenOClock.minute);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.nine.setOnClickListener((view -> {
            if (timeChip != dialogBinding.nine) {
                changeChip(timeChip, dialogBinding.nine);
                timeChip = dialogBinding.nine;
                scheduleDialogBinding.tvTimerLength.setText(tackTime);
                Time NineOClock = new Time();
                NineOClock.set(0, 0, 9, 0, 0, 0);

                tackTime = String.valueOf(NineOClock.hour) +":"+String.valueOf(NineOClock.minute);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(NineOClock.hour);
                    dialogBinding.timePicker.setMinute(NineOClock.minute);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.twelve.setOnClickListener((view -> {
            if (timeChip != dialogBinding.twelve) {
                changeChip(timeChip, dialogBinding.twelve);
                timeChip = dialogBinding.twelve;
                scheduleDialogBinding.tvTimerLength.setText(tackTime);
                Time TwelveOClock = new Time();
                TwelveOClock.set(0, 0, 12, 0, 0, 0);

                tackTime = String.valueOf(TwelveOClock.hour) +":"+String.valueOf(TwelveOClock.minute);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(TwelveOClock.hour);
                    dialogBinding.timePicker.setMinute(TwelveOClock.minute);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.eghty.setOnClickListener((view -> {
            if (timeChip != dialogBinding.eghty) {
                changeChip(timeChip, dialogBinding.eghty);
                timeChip = dialogBinding.eghty;
                scheduleDialogBinding.tvTimerLength.setText(tackTime);
                Time EighteenOClock = new Time();
                EighteenOClock.set(0, 0, 18, 0, 0, 0);

                tackTime = String.valueOf(EighteenOClock.hour) +":"+String.valueOf(EighteenOClock.minute);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(EighteenOClock.hour);
                    dialogBinding.timePicker.setMinute(EighteenOClock.minute);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));
        dialogBinding.tventyTree.setOnClickListener((view -> {
            if (timeChip != dialogBinding.tventyTree) {
                changeChip(timeChip, dialogBinding.tventyTree);
                timeChip = dialogBinding.tventyTree;
                scheduleDialogBinding.tvTimerLength.setText(tackTime);
                Time TwentyThreeOClock = new Time();
                TwentyThreeOClock.set(0, 0, 23, 0, 0, 0);

                tackTime = String.valueOf(TwentyThreeOClock.hour) +":"+String.valueOf(TwentyThreeOClock.minute);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dialogBinding.timePicker.setHour(TwentyThreeOClock.hour);
                    dialogBinding.timePicker.setMinute(TwentyThreeOClock.minute);
                }
                dialogBinding.timerDisable.setVisibility(View.GONE);
            }
        }));

    }
}
