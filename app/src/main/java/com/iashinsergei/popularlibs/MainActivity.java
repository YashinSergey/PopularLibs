package com.iashinsergei.popularlibs;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView tvCounter;
    Button button;
    Long counter = 60L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = findViewById(R.id.tv_count);
        button = findViewById(R.id.button);

        button.setOnClickListener(v -> Toast.makeText(MainActivity.this, "is pressed", Toast.LENGTH_SHORT).show());

        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .skip(1)
                .take(60);

        observable.safeSubscribe(observer);
    }

    Observer<Long> observer = new Observer<Long>() {
        Disposable disposable;
        @Override
        public void onSubscribe(Disposable d) {
            button.setEnabled(false);
            disposable = d;
        }

        @Override
        public void onNext(Long l) {
            Log.i(TAG, "onNext: " + l);
            tvCounter.setText(String.valueOf(counter - l));
        }
        @Override
        public void onError(Throwable e) {
            Log.i(TAG,"onError: " + e);
        }
        @Override
        public void onComplete() {
            Log.i(TAG,"onCompleted");
            button.setEnabled(true);
            disposable.dispose();
        }
    };

    List<String> getStrings(){
        return Arrays.asList("a", "b", "c", "d");
    }

    Function<Long, Long> mapFunction = new Function<Long, Long>() {
        @Override
        public Long apply(Long num) throws Exception {
            return num * 2;
        }
    };

    Function<Long, Boolean> filterFunction = new Function<Long, Boolean>() {
        @Override
        public Boolean apply(Long num) throws Exception {
            return num % 2 == 0;
        }
    };
}
