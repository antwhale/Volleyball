package org.techtown.volleyball;

import android.os.Handler;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.techtown.volleyball.data.entity.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class NewsRepository {
    private static final String TAG = "NewsRepository";

    private final Executor executor;
    private final Handler resultHandler;

    public NewsRepository(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public void makeParsingRequest(
            final String parseUrl,
            final RepositoryCallback<List<NewsItem>> callback
    ){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, parseUrl);
                    Result<List<NewsItem>> resultResponse = makeSynchronousParsingRequest(parseUrl);
                    notifyResult(resultResponse, callback);
                    //callback.onComplete(resultResponse);
                } catch(Exception e) {
                    Log.d(TAG, parseUrl);
                    Result<List<NewsItem>> errorResult = new Result.Error<>(e);
                    notifyResult(errorResult, callback);
                    //callback.onComplete(errorResult);
                }
            }
        });
    }

    public Result<List<NewsItem>> makeSynchronousParsingRequest(String parseUrl){
        try{
            Log.d(TAG, "parsing start!!");

            List<NewsItem> parsingResponse = new ArrayList<>();
            String imgUrl = null;
            String newsUrl = null;
            String title = null;

            Document doc = Jsoup.connect(parseUrl).get();
            //Log.d(TAG, doc.toString());

            Elements mElements = doc.select("div#listWrap").select("div.listPhoto");
            //Log.d(TAG, links.toString());
            int count = 0;
            for (Element element : mElements) {
                if(count == 4) break;
                newsUrl = element.select("p.img a").attr("href");
                imgUrl = element.select("img").attr("src");
                title = element.select("dt a").text();
                parsingResponse.add(new NewsItem(imgUrl, newsUrl, title));
                Log.d(TAG, newsUrl+"\n"+imgUrl+"\n"+title);
                count++;
                //Log.d(TAG, "parsingResult = "+parsingResult);
            }

            return new Result.Success<List<NewsItem>>(parsingResponse);

        }catch(Exception e){
            return new Result.Error<List<NewsItem>>(e);
        }
    }

    private void notifyResult(
            final Result<List<NewsItem>> result,
            final RepositoryCallback<List<NewsItem>> callback
    ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "notifyResult Started!!");
                callback.onComplete(result);
            }
        });
    }
}
