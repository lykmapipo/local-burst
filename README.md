local-burst
=======================

[![](https://jitpack.io/v/lykmapipo/local-burst.svg)](https://jitpack.io/#lykmapipo/local-burst)


Simple Local Broadcast(s) on top of android [LocalBroadcastManager](https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html).

## Installation
Add [https://jitpack.io](https://jitpack.io) to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
add `local-burst` dependency into your project

```gradle
dependencies {
    compile 'com.github.lykmapipo:local-burst:v0.6.0'
}
```

## Usage

Initialize `local-burst`

```java
public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //initialize
        LocalBurst.of(new Provider() {
            @NonNull
            @Override
            public Context getApplicationContext() {
                return SampleApp.this;
            }
        });
    }
}
```

In activity(or other component) start listen for broadcasts

```java
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ACTION = "Custom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //simulate force custom broadcast
        Button emitCustomButton = findViewById(R.id.btnEmitCustom);
        emitCustomButton.setOnClickListener(v -> {
            //emit broadcast
            Bundle bundle = new Bundle();
            bundle.putString(TAG, TAG);
            LocalBurst.$emit(ACTION, bundle);
        });


        //simulate force default broadcast
        Button emitDefaultButton = findViewById(R.id.btnEmitDefault);
        emitDefaultButton.setOnClickListener(v -> {
            //emit broadcast
            Bundle bundle = new Bundle();
            bundle.putString(TAG, TAG);
            LocalBurst.$emit(bundle);
        });

        // use live data observers

        LocalBurst.observe(this, bundle -> {
            toast(LocalBurst.DEFAULT_ACTION, bundle);
        });

        LocalBurst.observe(this, ACTION, bundle -> {
            toast(ACTION, bundle);
        });

    }

    private void toast(@NonNull String action, @NonNull Bundle extras) {
        Toast.makeText(this, action + " Broadcast Received", Toast.LENGTH_LONG).show();
    }
}
```


## Test
```sh
./gradlew test
```

## Contribute
It will be nice, if you open an issue first so that we can know what is going on, then, fork this repo and push in your ideas.
Do not forget to add a bit of test(s) of what value you adding.

## License

(The MIT License)

Copyright (c) lykmapipo && Contributors

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
