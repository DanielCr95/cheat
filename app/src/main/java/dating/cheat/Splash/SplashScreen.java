package dating.cheat.Splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import dating.cheat.MainActivity;
import dating.cheat.R;


public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;

    String testing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.PREF_LOGIN_JUST_STATUS), MODE_PRIVATE);

       // testing = sharedPreferences.getString(getString(R.string.status_points), "");

        if(testing.equals("true"))

        {  new Handler().postDelayed(new Runnable() {

            /*

             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
               startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
}
        else

        {
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);}


    }


}
