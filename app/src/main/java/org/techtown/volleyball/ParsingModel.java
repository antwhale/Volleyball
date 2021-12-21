package org.techtown.volleyball;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.techtown.volleyball.slideradapter.SliderItem;

import java.util.List;

public class ParsingModel extends AndroidViewModel {
    private static final String TAG = "ParsingModel";
    private final NaverTvRepository naverTvRepository;
    private final NewsRepository newsRepository;
    private final ScheduleRepository scheduleRepository;

    MutableLiveData<List<SliderItem>> parsingLivedata = new MutableLiveData<List<SliderItem>>();
    MutableLiveData<List<NewsItem>> parsingNewsLivedata = new MutableLiveData<>();
    MutableLiveData<List<String[]>> parsingScheduleLivedata = new MutableLiveData<>();

    public ParsingModel(@NonNull Application application) {
        super(application);
        naverTvRepository = new NaverTvRepository (
                ((MyApplication)application).executorService,
                ((MyApplication)application).mainThreadHandler
        );

        newsRepository = new NewsRepository(
                ((MyApplication)application).executorService,
                ((MyApplication)application).mainThreadHandler
        );

        scheduleRepository = new ScheduleRepository(
                ((MyApplication)application).executorService,
                ((MyApplication)application).mainThreadHandler
                );
    }




    public void makeParsingRequest(String parsingUrl){
        Log.d(TAG, "첫번째 " + parsingUrl);
        naverTvRepository.makeParsingRequest(parsingUrl, new RepositoryCallback<List<SliderItem>>() {
            @Override
            public void onComplete(Result<List<SliderItem>> result) {
                if(result instanceof Result.Success){
                    Log.d(TAG, parsingUrl);

                    parsingLivedata.postValue(((Result.Success<List<SliderItem>>)result).data);

                    //Log.d(TAG, ((Result.Success<List<String>>) result).data);
                    Log.d(TAG, "성공이다");

                }else{
                    Log.d(TAG, parsingUrl);
                    Log.d(TAG, String.valueOf(((Result.Error<List<SliderItem>>) result).exception));
                    Log.d(TAG, "실패이다");

                }
            }
        });
    }

    public void makeNewsParsingRequest(String parsingUrl){
        Log.d(TAG, "첫번째 뉴스 " + parsingUrl);
        newsRepository.makeParsingRequest(parsingUrl, new RepositoryCallback<List<NewsItem>>() {
            @Override
            public void onComplete(Result<List<NewsItem>> result) {
                if(result instanceof Result.Success){
                    Log.d(TAG, parsingUrl);

                    parsingNewsLivedata.postValue(((Result.Success<List<NewsItem>>)result).data);

                    //Log.d(TAG, ((Result.Success<List<String>>) result).data);
                    Log.d(TAG, "성공이다");

                }else{
                    Log.d(TAG, parsingUrl);
                    Log.d(TAG, String.valueOf(((Result.Error<List<NewsItem>>) result).exception));
                    Log.d(TAG, "실패이다");
                }
            }
        });
    }

    public void makeScheduleParsingRequest(String parsingUrl){
        Log.d(TAG, "코보 스케줄 url = " + parsingUrl);
        scheduleRepository.makeParsingRequest(parsingUrl, new RepositoryCallback<List<String[]>>(){
            @Override
            public void onComplete(Result<List<String[]>> result) {
                if(result instanceof Result.Success){
                    Log.d(TAG, parsingUrl);

                    parsingScheduleLivedata.postValue(((Result.Success<List<String[]>>)result).data);

                    //Log.d(TAG, ((Result.Success<List<String>>) result).data);
                    Log.d(TAG, "schedule 성공이다");
                }else{
                    Log.d(TAG, parsingUrl);
                    Log.d(TAG, String.valueOf(((Result.Error<List<String[]>>) result).exception));
                    Log.d(TAG, "schedule 실패이다");
                }
            }
        });
    }

}
