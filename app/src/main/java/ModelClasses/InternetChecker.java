package ModelClasses;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
public class InternetChecker {

    public interface InternetCheckListener {
        void onInternetCheckResult(boolean isConnected);
    }

    public static void checkInternetConnection(Context context, InternetCheckListener listener) {
        new InternetCheckTask(context, listener).execute();
    }

    private static class InternetCheckTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private InternetCheckListener listener;

        public InternetCheckTask(Context context, InternetCheckListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (listener != null) {
                listener.onInternetCheckResult(result);
            }
        }
    }
}
