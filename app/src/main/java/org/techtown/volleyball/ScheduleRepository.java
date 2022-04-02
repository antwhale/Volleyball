package org.techtown.volleyball;

import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class ScheduleRepository {
    private static final String TAG = "ScheduleRepository";
    private static final int N = 10;
    private final Executor executor;
    private final Handler resultHandler;

    SimpleDateFormat todayFormat = new SimpleDateFormat("MM. dd");
    Calendar time = Calendar.getInstance();
    String currentDay = todayFormat.format(time.getTime());
    //테스트용 날짜
    //String currentDay = "10. 19";
    public ScheduleRepository(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public void makeParsingRequest(
            final String parseUrl,
            final RepositoryCallback<List<String[]>> callback
    ){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, parseUrl);
                    Result<List<String[]>> resultResponse = makeSynchronousParsingRequest(parseUrl);
                    notifyResult(resultResponse, callback);
                    //callback.onComplete(resultResponse);
                }catch(Exception e){
                    Log.d(TAG, parseUrl);
                    Result<List<String[]>> errorResult = new Result.Error<>(e);
                    notifyResult(errorResult, callback);
                    //callback.onComplete(errorResult);
                }
            }
        });
    }

    public Result<List<String[]>> makeSynchronousParsingRequest(String parseUrl) {
        try {
            Document doc = Jsoup.connect(parseUrl).userAgent("Mozilla").get();
            //Log.d(TAG, "doc완료");

            Elements mElementDataSize = doc.select("table[class=lst_board lst_schedule] tbody tr");
            //Log.d(TAG, "Elements 완료");
            //Log.d(TAG, "Elements 는 " + mElementDataSize);

            //int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.
            Log.d(TAG, mElementDataSize.toString());
            //Log.d(TAG, "mElementDataSize.size() = " + mElementDataSize.size());
            String[] firstGame = new String[10];
            String[] secondGame = new String[10];
            List<String[]> parsingResponse = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                firstGame[i] = "";
                //Log.d(TAG, "firstGame"+i+" = " + firstGame[i]);
                secondGame[i] = "";
                //Log.d(TAG, "secondGame"+i+" = " + secondGame[i]);
            }
            String beforeVsToday = "";
            String currentVsToday = "";

            for (Element elem : mElementDataSize) { //이렇게 요긴한 기능이
                Log.d(TAG, elem.toString());
                currentVsToday = elem.select("td.date").text();
                Log.d(TAG, "currentVsToday is " + currentVsToday);
                Log.d(TAG, "currentDay is " + currentDay);

                //currentVSToday가 오늘날짜보다 크면 for문 종료!!
                if (currentVsToday != "") {
                    if (Integer.parseInt(currentVsToday.split("\\s")[1]) > Integer.parseInt(currentDay.split("\\s")[1])){
                        Log.d(TAG, "currentDay Over!" );
                        break;
                    }
                }

                if (beforeVsToday.contains(currentDay) || currentVsToday.contains(currentDay)) {
                    Iterator<Element> iterElem = elem.getElementsByTag("td").iterator();
                    Log.d(TAG, "iterator 완성!");
                    if (firstGame[0].equals("")) {
                        int i = 0;
                        while (iterElem.hasNext()) {
                            firstGame[i] = iterElem.next().text();
                            if (firstGame[i] != null)
                                Log.d(TAG, "firstGame" + i + " = " + firstGame[i]);
                            else Log.d(TAG, "\nnull");
                            i++;
                        }
                        parsingResponse.add(firstGame);
                        beforeVsToday = currentVsToday;
                       /* //에러확인용
                        for (int j = 0; j < 10; j++) {
                            Log.d(TAG, "firstGame" + j + " = " + parsingResponse.get(0)[j]);
                        }*/
                    } else {
                        int i = 0;
                        while (iterElem.hasNext()) {
                            secondGame[i] = iterElem.next().text();
                            if (secondGame[i] != null)
                                Log.d(TAG, "secondGame" + i + " = " + secondGame[i]);
                            else Log.d(TAG, "\nnull");
                            i++;

                        }
                        parsingResponse.add(secondGame);
                        //에러 확인용
                        for (int j = 0; j < 10; j++) {
                            Log.d(TAG, "secondGame" + j + " = " + parsingResponse.get(1)[j]);

                        }
                        beforeVsToday = "";
                    }
                }
                Log.d(TAG, "currentVsToday or beforeVsToday don't contain currentDay!");
            /*Log.d(TAG, "parsingResponse.get(1)[0] is "+ parsingResponse.get(1)[0]);
            parsingResponse.add(secondGame);
            for(int j=0; j<10 ; j++){
                Log.d(TAG, "secondGame" + j + " = " + parsingResponse.get(1)[j]);
            }*/
                //왜 자꾸 에러가 나는거야!! -> 리스트 사이즈로 조건 달아!!
            }
                return new Result.Success<List<String[]>>(parsingResponse);
            } catch(IOException e){
                return new Result.Error<List<String[]>>(e);
            }
        }



    private void notifyResult(
            final Result<List<String[]>> result,
            final RepositoryCallback<List<String[]>> callback
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

