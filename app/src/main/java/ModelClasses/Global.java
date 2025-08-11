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
import com.ziac.wheelzcustomer.R;

import java.util.ArrayList;

/**
 * Created on 28/2/2024.
 */
public class Global {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private static Picasso picassoInstance;

   // public static String baseurl = "http://nds.ziaconline.com/";


    //public static String baseurl = "https://wheelzonline.co.in/";
    public static String baseurl = "http://192.168.100.21:7777/";
   //public static String baseurl_image = "https://wheelzonline.in/";
    public static String baseurl_image = "http://192.168.100.21:7778/";

    public static String tokenurl = baseurl + "TOKEN";
    public static String urlregistration = baseurl + "api/Account/RegisterCustomer";
    public static String urlBookServices = baseurl + "api/Users/BookServices";
    public static String urlBookTestDrive = baseurl + "api/Users/BookTestDrive";
    public static String forgotpasswordurl = baseurl + "api/Account/ForgotPasswordCustomer";
    public static String otpfordeleteaccounturl = baseurl + "api/Users/DeleteCustomerAccount";
    public static String validateOTPurl = baseurl + "api/Account/ForgotPasswordCustomer";
    public static String velidateanddeleteaccounturl = baseurl + "api/Users/DeleteCustomerValidateOTP?";
    public static String validateotpurl = baseurl + "api/Account/ValidateOTPCustomer";
    public static String productimageurl = baseurl + "website_data/products/";
    public static String ProductCatPrdFilter = baseurl + "api/List/CatPrdFilter?";
    public static String changepasswordurl = baseurl + "api/Account/ChangePassword";
    public static String getuserprofiledetails = baseurl + "api/Users/GetUserProfile";
    public static String getvehimages = baseurl + "api/List/GetVehicleUsedTypeImages?";
    public static String deletevehicle = baseurl + "api/MyVehicles/DeleteVehicle?";
    public static String deletevehimages = baseurl + "api/MyVehicles/VehImgDelete?";
    public static String Getcountries = baseurl + "api/List/GetCountries?";
    public static String GetStates = baseurl + "api/List/GetStates";
    public static String urlpostaddvehicle = baseurl + "api/MyVehicles/Add_Vehicle_Android";
    public static String urlGetVehiclesModel = baseurl + "api/List/GetBrandsByMfgCode?";
    public static String urlgetvariant = baseurl + "api/List/GetVariant?";
    public static String urlgetmanufacture = baseurl + "api/List/GetManufacturer";
    public static String urlGetVehicletype = baseurl +"api/List/GetCategory?";

    public static ArrayList<zList> vehicletypearraylist;
    public static ArrayList<zList> vehiclevariantlist;
    public static ArrayList<zList> vehiclecolourlist;
    public static ArrayList<zList> vehiclefuellist;
    public static ArrayList<zList> Countryarraylist;
    public static ArrayList<zList> statearraylist;
    public static ArrayList<zList> vehiclearraylist;
    public static ArrayList<zList> vehiclemanufacturelist;

    public static ArrayList<CommonClass> dealersarraylist;
    public static ArrayList<zList> vehiclemodellist;
    public static String urlgetvehcolour = baseurl + "api/List/GetColor?";

    public static String GetCity = baseurl + "api/List/Getcity?";
    public static String urlgetfuel = baseurl + "api/List/GetFuel";
    public static ArrayList<zList> cityarraylist;
    public static String UrlGetDocuments = baseurl + "api/List/GetDocumentList?";

    public static String getallbrands = baseurl + "api/Brands/GetAllBrands";
    public static String getallvehimages = baseurl + "api/List/GetVehicleUsedImages?";

    public static String getallMyVehicles = baseurl + "api/MyVehicles/Get?";
    public static String getservicehistory = baseurl + "api/MyVehicles/GetServiceHistory?";
    public static String getGetBookings = baseurl + "api/Users/GetBookings?";
    public static String searchallbrands = baseurl + "api/Brands/GetAllBrands?searchtext=";
    public static String searchalldealers = baseurl + "api/Company/GetAllDealers?";
    public static String GetDocumentList = baseurl + "api/List/GetDocumentList?";
    public static String getCategoriesList = baseurl + "api/List/GetCategory?";
    public static String ProductList = baseurl + "api/List/GetProductList?";
    public static String urlupdateprofile = baseurl + "api/Users/UpdateProfileCustomer";
    public static String uploadVehImage = baseurl + "api/MyVehicles/UploadVehImage";
    public static ArrayList<LatestVehiclesClass> latestVehicleslist;
    public static String urllessdriven = baseurl + "api/Vehicles/GetLessDrivenVehicles";

    public static ArrayList<LessDrivenClass> lessdrivenlist;

    public static String Getsearchdetails = baseurl +"api/Search/POSTSearchDetails";
    public static ArrayList<ModelsClass> modelsList;
    public static ArrayList<CommonClass> commonclassList;

    public static VehicleClass selectedvehicle;
    public static CommonVehClass commonVehClass;

    public static String urlGetbrands = baseurl +"api/Brands/GetBrands";
    public static String Getmodels = baseurl +"api/Brands/GetAllBrands";
    public static String vehicleimageurl = baseurl + "websitedata/vehicles/";
    public static String urladdvehicle = baseurl + "api/MyVehicles/Add";
    public static String urlUpdateprofileImage = baseurl + "api/Users/UpdateProfilePhoto";
    public static String GetRecentVehicles = baseurl + "api/List/GetRecentVehicles?";
    public static String GetLessDrivenVehicles = baseurl + "api/List/GetLessDrivenVehicles?";
    public static String urluploadfiles = baseurl + "api/MyVehicles/DLUpload?";
    public static String urldeletefiles= baseurl + "api/MyVehicles/Delete?";
    public static String urlUpdateVehicleDetails= baseurl + "api/MyVehicles/Update_Vehicle_Details";
    public static String userimageurl = baseurl + "/Website_data/web_users/";
    public static String modelsimageurl = baseurl_image + "/Website_data/brands/";
    public static String brandsimageurl = baseurl_image + "/Website_data/brands/";
    public static String companyimageurl = baseurl_image + "/Website_data/Logo_image/";
    public static String vehimageurl = baseurl + "/Website_Data/Vehicle/UsedVehicle/";
    public static String websitedataurl = baseurl_image + "/Website_data/Logo_image/";

    public static zList selectedvstock;

    public static LatestVehiclesClass latestVehiclesClass;
    public static String dlpath = baseurl +  "Website_Data/Vehicle/DL/";
    public static String rcpath = baseurl +  "Website_Data/Vehicle/RC/";
    public static String insurancepath = baseurl + "Website_Data/Vehicle/Insurance/";
    public static String emissionpath = baseurl +  "Website_Data/Vehicle/Emission/";
    public static String warrantypath = baseurl + "Website_Data/Vehicle/Warranty/";
    public static String permitpath = baseurl +  "Website_Data/Vehicle/Permit/";
    public static String urlfilepath= baseurl + "Website_Data/web_users/Docs/";


      public static ArrayList<CommonClass> allleadslist;
      public static ArrayList<CommonClass> myvehiclelist;
      public static CommonClass modellist;
      public static CommonClass dealersdetails;
     public static ArrayList<ServiceClass> commonarraylist;
      public static zList vehiclelist;
    public static ServiceClass Commonclass;
      public static CommonClass vehicledetails;


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
