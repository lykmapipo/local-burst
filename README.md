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
    compile 'com.github.lykmapipo:local-burst:v0.5.1'
}
```

## Usage

Initialize `local-burst`

```java
public class SampleApp extends Application{

    @Override
    public void onCreate() {
   
        super.onCreate();
        LocalBurst.create(getApplicationContext());
        
    }

}
```

In activity(or other component) start listen for broadcasts

```java
public class MainActivity extends AppCompatActivity implements LocalBurst.OnBroadcastListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ACTION = MainActivity.class.getSimpleName();

    private LocalBurst broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain local broadcast instance
        broadcast = LocalBurst.getInstance();

        //simulate force broadcast
        Button emitButton = (Button) findViewById(R.id.btnEmit);
        emitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //emit broadcast
                Bundle bundle = new Bundle();
                bundle.putString(TAG, TAG);
                broadcast.emit(ACTION, bundle);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        broadcast.removeListeners(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcast.on(ACTION, this);
        broadcast.on(LocalBurst.DEFAULT_ACTION, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcast.removeListeners(this);
    }

    /**
     * Receive local broadcast and process it
     *
     * @param action name of the action to listen on
     * @param extras intent extras received from the action
     */
    @Override
    public void onBroadcast(String action, Bundle extras) {
        Toast.makeText(this, "Broadcast Received", Toast.LENGTH_LONG).show();
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

Copyright (c) 2017 lykmapipo, lykmapipo Group && Contributors

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
