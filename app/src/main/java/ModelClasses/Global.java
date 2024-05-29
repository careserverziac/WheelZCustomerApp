package ModelClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.ziac.wheelzonline.R;

import java.util.ArrayList;

/**
 * Created on 28/2/2024.
 */
public class Global {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private static Picasso picassoInstance;
    public static String baseurl = "http://nds.ziaconline.com/";
   // public static String baseurl = "http://192.168.1.21:9394/";

    public static String userimageurl = baseurl+"/Website_data/web_users/";
    public static String modelsimageurl = baseurl+"/Website_data/models/";

    public static String companyimageurl = baseurl+"/Website_data/Company/Logo/";

    public static String tokenurl = baseurl + "Ziac/WebUsers/TOKEN";
    public static String urlregistration = baseurl + "api/Account/RegisterCustomer";


    public static String forgotpasswordurl = baseurl + "api/Account/ForgotPasswordCustomer";
    public static String otpfordeleteaccounturl = baseurl + "api/Users/DeleteCustomerAccount";


    public static String validateOTPurl = baseurl + "api/Account/ForgotPasswordCustomer";
    public static String velidateanddeleteaccounturl = baseurl + "api/Users/DeleteCustomerValidateOTP?";
    public static String validateotpurl = baseurl + "api/Account/ValidateOTPCustomer";

    public static String changepasswordurl = baseurl + "api/Account/ChangePassword";
    public static String getuserprofiledetails = baseurl + "api/Users/GetUserProfile";
    public static String GetStates = baseurl + "api/List/GetStates";
    public static ArrayList<zList> statearraylist;
    public static ArrayList<CommonClass> dealersarraylist;
    public static String GetCity = baseurl + "api/List/Getcity?";
    public static ArrayList<zList> cityarraylist;

    public static String Getdealerslist = baseurl + "api/Company/GetCompanies?";

    public static String getallbrands = baseurl + "api/Brands/GetAllBrands";

    public static String getallMyVehicles = baseurl + "api/MyVehicles/Get?";
    public static String searchallbrands = baseurl + "api/Brands/GetAllBrands?searchtext=";


    public static String urlupdateprofile = baseurl + "api/Users/UpdateProfileCustomer";

    public static String urladdvehicle = baseurl + "api/MyVehicles/Add";
    public static String urlUpdateprofileImage = baseurl + "api/Users/UpdateProfilePhoto";
    public static String urlSearchdealers = baseurl + "";


      public static ArrayList<CommonClass> allleadslist;
      public static CommonClass modellist;


  public static void customtoast(Context context, LayoutInflater inflater, String msg) {
   View customToastView = inflater.inflate(R.layout.costom_toast, null);
   ImageView icon = customToastView.findViewById(R.id.toast_icon);
   TextView text = customToastView.findViewById(R.id.toast_text);
   text.setText(msg);
   final Toast customToast = new Toast(context);
   customToast.setDuration(Toast.LENGTH_SHORT);
   customToast.setView(customToastView);
   customToast.show();
   new Handler().postDelayed(new Runnable() {@Override public void run() {customToast.cancel();}}, 6000);}



    private static Picasso getPicassoInstance(Context context) {
        if (picassoInstance == null) {

            picassoInstance = new Picasso.Builder(context.getApplicationContext()).build();
        }
        return picassoInstance;
    }


    public static void loadWithPicasso(Context context, ImageView imageView, String imageUrl) {
        Picasso picasso = getPicassoInstance(context);
        picasso.load(imageUrl)
                .error(R.drawable.no_image_available_icon)
                .into(imageView);
    }



}
