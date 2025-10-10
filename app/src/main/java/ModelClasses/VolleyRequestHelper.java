package ModelClasses;


import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.wheelzcustomer.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class VolleyRequestHelper {

    private static RequestQueue requestQueue;
    Context context;
    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    // Track active requests for cancellation
    private Map<String, Request<?>> activeRequests = new ConcurrentHashMap<>();
    private static final int DEFAULT_TIMEOUT_MS = 30000;

    public VolleyRequestHelper(Context context) {
        this.context = context;
        if(IS_DEBUG) {
            getUnsafeRequestQueue();
        }
        requestQueue = Volley.newRequestQueue(context);
    }

    // Interface for handling responses
    public interface VolleyCallback {
        void onSuccess(JSONObject result);
        void onError(String error);
    }

    private void getUnsafeRequestQueue() {
        try {
            // SSL Configuration (same as your original code)
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main method for making requests with request tag for cancellation
    public String makeRequest(String endpoint, Map<String, String> params,
                              String accessToken, VolleyCallback callback) {
        return makeRequest(Request.Method.POST, endpoint, params, accessToken, callback, DEFAULT_TIMEOUT_MS);
    }

    // Overloaded method with HTTP method parameter
    public String makeRequest(int method, String endpoint, Map<String, String> params,
                              String accessToken, VolleyCallback callback) {
        return makeRequest(method, endpoint, params, accessToken, callback, DEFAULT_TIMEOUT_MS);
    }

    // Full method with timeout parameter
    public String makeRequest(int method, String endpoint, Map<String, String> params,
                              String accessToken, VolleyCallback callback, int timeoutMs) {

        // Generate unique request tag
        String requestTag = "request_" + System.currentTimeMillis() + "_" + hashCode();

        StringRequest request = new StringRequest(method, endpoint,
                response -> {
                    // Remove from active requests on success
                    activeRequests.remove(requestTag);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        callback.onSuccess(jsonResponse);
                    } catch (JSONException e) {
                        callback.onError("JSON Parse Error: " + e.getMessage());
                    }
                },
                error -> {
                    // Remove from active requests on error
                    activeRequests.remove(requestTag);
                    String errorMessage = getErrorMessage(error);
                    callback.onError(errorMessage);
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                if (accessToken != null && !accessToken.isEmpty()) {
                    headers.put("Authorization", "Bearer " + accessToken);
                }
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return params != null ? params : new HashMap<>();
            }
        };

        // Set request tag
        request.setTag(requestTag);

        // Set retry policy with custom timeout
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Add to active requests tracking
        activeRequests.put(requestTag, request);

        // Add to queue
        requestQueue.add(request);

        // Return tag for manual cancellation if needed
        return requestTag;
    }

    // GET request method
    public String makeGetRequest(String endpoint, String accessToken, VolleyCallback callback) {
        return makeRequest(Request.Method.GET, endpoint, null, accessToken, callback);
    }

    // GET request method with timeout
    public String makeGetRequest(String endpoint, String accessToken, VolleyCallback callback, int timeoutMs) {
        return makeRequest(Request.Method.GET, endpoint, null, accessToken, callback, timeoutMs);
    }

    // POST request method
    public String makePostRequest(String endpoint, Map<String, String> params,
                                  String accessToken, VolleyCallback callback) {
        return makeRequest(Request.Method.POST, endpoint, params, accessToken, callback);
    }

    // POST request method with timeout
    public String makePostRequest(String endpoint, Map<String, String> params,
                                  String accessToken, VolleyCallback callback, int timeoutMs) {
        return makeRequest(Request.Method.POST, endpoint, params, accessToken, callback, timeoutMs);
    }

    // PUT request method
    public String makePutRequest(String endpoint, Map<String, String> params,
                                 String accessToken, VolleyCallback callback) {
        return makeRequest(Request.Method.PUT, endpoint, params, accessToken, callback);
    }

    // PUT request method with timeout
    public String makePutRequest(String endpoint, Map<String, String> params,
                                 String accessToken, VolleyCallback callback, int timeoutMs) {
        return makeRequest(Request.Method.PUT, endpoint, params, accessToken, callback, timeoutMs);
    }

    // DELETE request method
    public String makeDeleteRequest(String endpoint, String accessToken, VolleyCallback callback) {
        return makeRequest(Request.Method.DELETE, endpoint, null, accessToken, callback);
    }

    // DELETE request method with timeout
    public String makeDeleteRequest(String endpoint, String accessToken, VolleyCallback callback, int timeoutMs) {
        return makeRequest(Request.Method.DELETE, endpoint, null, accessToken, callback, timeoutMs);
    }

    // Cancel specific request by tag
    public void cancelRequest(String requestTag) {
        if (requestTag != null && requestQueue != null) {
            requestQueue.cancelAll(requestTag);
            activeRequests.remove(requestTag);
        }
    }

    // Cancel all requests with specific tag prefix (useful for activity/fragment)
    public void cancelRequestsWithTag(String tagPrefix) {
        if (tagPrefix != null && requestQueue != null) {
            requestQueue.cancelAll(request -> {
                Object tag = request.getTag();
                return tag != null && tag.toString().startsWith(tagPrefix);
            });
            // Clean up from activeRequests
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                activeRequests.entrySet().removeIf(entry ->
                        entry.getKey().startsWith(tagPrefix));
            }
        }
    }

    // Method to cancel all pending requests
    public void cancelAllRequests() {
        if (requestQueue != null) {
            requestQueue.cancelAll(request -> true);
            activeRequests.clear();
        }
    }

    // Get count of active requests
    public int getActiveRequestsCount() {
        return activeRequests.size();
    }

    // Check if specific request is still active
    public boolean isRequestActive(String requestTag) {
        return activeRequests.containsKey(requestTag);
    }

    private String getErrorMessage(VolleyError error) {
        String errorMessage = "";
        String responseBody = "";
        int statusCode = 0;
        Map<String, String> headers = new HashMap<>();

        // Check if networkResponse is available
        if (error.networkResponse != null) {
            statusCode = error.networkResponse.statusCode;
            headers = error.networkResponse.headers;

            // Read response body
            try {
                responseBody = new String(error.networkResponse.data,
                        HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                responseBody = new String(error.networkResponse.data);
            }
        }

        if (error instanceof TimeoutError) {
            errorMessage = "Request timed out";
        } else if (error instanceof NoConnectionError) {
            errorMessage = "No internet connection";
        } else if (error instanceof AuthFailureError) {
            errorMessage = "Authentication failed";
            // Often contains useful error details in response body
            if (!responseBody.isEmpty()) {
                errorMessage += ": " + parseErrorFromResponse(responseBody);
            }
        } else if (error instanceof ServerError) {
            errorMessage = "Server error (" + statusCode + ")";
            if (!responseBody.isEmpty()) {
                errorMessage += ": " + parseErrorFromResponse(responseBody);
            }
        } else if (error instanceof ClientError) {
            errorMessage = "Client error (" + statusCode + ")";
            if (!responseBody.isEmpty()) {
                errorMessage += ": " + parseErrorFromResponse(responseBody);
            }
        } else if (error instanceof NetworkError) {
            errorMessage = "Network error";
        } else if (error instanceof ParseError) {
            errorMessage = "Parse error";
            // Parse error might still have response data
            if (!responseBody.isEmpty()) {
                errorMessage += ". Response: " + responseBody.substring(0, Math.min(100, responseBody.length()));
            }
        } else {
            errorMessage = "Unknown error";
        }

        // Log detailed error information for debugging
        logErrorDetails(error, statusCode, responseBody, headers);

        return errorMessage;
    }
    private String parseErrorFromResponse(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);

            // Common error message fields in APIs
            if (jsonResponse.has("message")) {
                return jsonResponse.getString("message");
            } else if (jsonResponse.has("error")) {
                return jsonResponse.getString("error");
            } else if (jsonResponse.has("error_description")) {
                return jsonResponse.getString("error_description");
            } else if (jsonResponse.has("detail")) {
                return jsonResponse.getString("detail");
            }
        } catch (JSONException e) {
            // If it's not JSON, return first 100 characters
            return responseBody.length() > 100 ?
                    responseBody.substring(0, 100) + "..." : responseBody;
        }
        return responseBody;
    }
    private void logErrorDetails(VolleyError error, int statusCode, String responseBody, Map<String, String> headers) {
        Log.e("VolleyError", "Error Type: " + error.getClass().getSimpleName());
        Log.e("VolleyError", "Status Code: " + statusCode);
        Log.e("VolleyError", "Response Body: " + responseBody);
        Log.e("VolleyError", "Headers: " + headers.toString());

        if (error.getCause() != null) {
            Log.e("VolleyError", "Cause: " + error.getCause().getMessage());
        }
    }

    // Cleanup method - call this in onDestroy
    public void destroy() {
        cancelAllRequests();
        if (requestQueue != null) {
            requestQueue.stop();
        }
        activeRequests.clear();
        context = null;
    }
}