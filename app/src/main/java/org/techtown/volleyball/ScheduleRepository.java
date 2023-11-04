package org.techtown.volleyball;

import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class ScheduleRepository {
    private static final String TAG = "ScheduleRepository";
    private static final int N = 10;
    private final Executor executor;
    private final Handler resultHandler;

    SimpleDateFormat todayFormat = new SimpleDateFormat("MM. dd");
    Calendar time = Calendar.getInstance();
    String currentDay = todayFormat.format(time.getTime());

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //테스트용 날짜
    //String currentDay = "10. 19";
    public ScheduleRepository(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public void makeParsingRequest(
            int gender,
            final RepositoryCallback<String[]> callback
    ){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Date nowDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("M월 d일 (E)", Locale.KOREA);
                    String todayString = sdf.format(nowDate);

                    Log.d(TAG, "todayString : " + todayString + "Locale : " + Locale.getDefault().toString());

                    String genderKey = "";
                    if(gender == 1) {
                        genderKey = "ManSchedule";
                    } else {
                        genderKey = "WomanSchedule";
                    }
                    //"10월 14일 (토)"
                    mRootRef.child(genderKey).child(todayString).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, Object> resultMap = (Map<String, Object>) snapshot.getValue();
                            if(resultMap != null) {
                                String awayTeam = (String) resultMap.get("away_team");
                                String homeTeam = (String) resultMap.get("home_team");
                                String place = (String) resultMap.get("place");
                                String round = (String) resultMap.get("round");
                                String time = (String) resultMap.get("time");

                                Log.d(TAG, "awayTeam: " + awayTeam + " homeTeam: " + homeTeam + " place: " + place + " round: " + round + " time: " + time);

                                String[] resultData = { homeTeam, awayTeam, place, round, time };
                                Result<String[]> result = new Result.Success<String[]>(resultData);
                                notifyResult(result, callback);
                            } else {
                                Result<String[]> result = new Result.Success<String[]>(null);
                                notifyResult(result, callback);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, error.getMessage());
                            Result<String[]> errorResult = new Result.Error<>(error.toException());
                            notifyResult(errorResult, callback);
                        }
                    });
//                    Log.d(TAG, parseUrl);
//                    Result<List<String[]>> resultResponse = makeSynchronousParsingRequest(parseUrl);
//                    notifyResult(resultResponse, callback);
                }catch(Exception e){
                    e.printStackTrace();
//                    Log.d(TAG, parseUrl);
//                    Result<List<String[]>> errorResult = new Result.Error<>(e);
//                    notifyResult(errorResult, callback);
                }
            }
        });
    }

    private void notifyResult(
            final Result<String[]> result,
            final RepositoryCallback<String[]> callback
    ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG, "notifyResult Started!!");
                callback.onComplete(result);
            }
        });
    }
}

