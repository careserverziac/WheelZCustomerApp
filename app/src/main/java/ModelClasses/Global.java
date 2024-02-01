package ModelClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ziac.wheelzonline.R;

import java.util.ArrayList;


public class Global {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    //public static String baseurl = "http://192.168.1.8/ziaccrm.main/";
   public static String baseurl = "http://192.168.1.9:9394/";

   // public static String baseurl = "https://.in/";
    public static String userimageurl = baseurl+ "/Website_data/web_users/";

    public static String tokenurl = baseurl + "Ziac/WebUsers/TOKEN";
    public static String urlregistration = baseurl + "api/Account/RegisterCustomer";

    public static String registrationurl ="https://192.168.1.16:9393/api/Account/Register";

    public static String urlcreatecrm = baseurl + "api/CRM/Create";
    public static String urleditandupdatecrm = baseurl + "api/CRM/Edit";
    public static String urlUpdateCRM = baseurl + "api/CRM/Updates";
    public static String forgotpasswordurl = baseurl + "api/Account/ForgotPasswordCustomer";
    public static String validateotpurl = baseurl + "api/Account/ValidateOTPCustomer";

    public static String changepasswordurl = baseurl + "api/Account/ChangePassword";
    public static String getuserprofiledetails = baseurl + "api/Users/GetUserProfile";

    public static String getcompanydetails = baseurl + "api/Company/GetCompanyDetails?";
    public static String getdashboarddataurl = baseurl + "api/Dashboard/Index?";

    public static String getmissedflpdata = baseurl + "api/CRM/MissedLeads?";
    public static String gettodaysflpdata = baseurl + "api/CRM/TodaysLeads?";

    public static String postcompanydetails = baseurl + "api/Company/UpdateCompanyDetails";

    public static String urlupdateprofile = baseurl + "api/Users/UpdateProfileCustomer";
    public static String urlGetcategory = baseurl + "api/Lists/GetCategory?";
    public static String urlGetType = baseurl + "api/Lists/GetENQType?";
    public static String urlGetsource = baseurl + "api/Lists/GetENQSource?";
    public static String urlGetComcountry = baseurl + "api/Lists/GetComCountry?";
    public static String urlGetComstate = baseurl + "api/Lists/GetComState?";
    public static String urlGetComcity = baseurl + "api/Lists/GetComCity?";

    public static String urlGetState = baseurl + "api/Lists/GetState";
    public static String urlGetCity = baseurl + "api/Lists/GetCity";
    public static String urlGetstage = baseurl + "api/Lists/GetLeadStage?";
    public static String urlGetassignedto = baseurl + "api/Users/GetUserList?";
    public static String urlUpdateprofileImage = baseurl + "api/Users/UpdateProfilePhoto";
    public static String urlGetImageList = baseurl + "api/CRM/Images?";
    public static String urlUpdateVehicImage = baseurl + "api/CRM/UploadImage";
    public static String urldeleteimage = baseurl + "api/CRM/DeleteImage?";
    public static String UploadFiles = baseurl + "api/CRM/UploadDoc";
    public static String UrlGetDocuments = baseurl + "api/CRM/Documents?";
    public static String urldeletedoc = baseurl + "api/CRM/DeleteDocument?";


  public static void customtoast(Context context, LayoutInflater inflater, String msg) {
   View customToastView = inflater.inflate(R.layout.costom_toast, null);
   ImageView icon = customToastView.findViewById(R.id.toast_icon);
   TextView text = customToastView.findViewById(R.id.toast_text);
   text.setText(msg);
   final Toast customToast = new Toast(context);
   customToast.setDuration(Toast.LENGTH_SHORT);
   customToast.setView(customToastView);
   customToast.show();
   new Handler().postDelayed(new Runnable() {@Override public void run() {customToast.cancel();}}, 2000);}

}
