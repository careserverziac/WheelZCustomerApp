package ModelClasses;

public class VehicleClass {

    private String veh_mfgname,veh_image,veh_fullname,veh_year,veh_colour,veh_price,veh_model,
            veh_variant,veh_transmission,veh_ownership,veh_fuel,veh_cc,veh_km, rcdetails,vehcategory,
            dealercom_name,dealer_mob,dealer_altermob,dealermailid,dealer_city;


    public VehicleClass(){

    }

    public VehicleClass(String veh_mfgname,String veh_image, String veh_fullname, String veh_year, String veh_colour, String veh_price,
                        String veh_model, String veh_variant, String veh_transmission, String veh_ownership,
                        String veh_fuel, String veh_cc, String veh_km, String rcdetails,String vehcategory,
                        String dealercom_name, String dealer_mob,String dealer_altermob, String dealermailid,String dealer_city){



        this.veh_mfgname = veh_mfgname;
        this.veh_image = veh_image;
        this.veh_fullname = veh_fullname;
        this.veh_year=veh_year;
        this.veh_colour=veh_colour;
        this.veh_model=veh_model;
        this.veh_variant=veh_variant;
        this.veh_transmission=veh_transmission;
        this.veh_ownership=veh_ownership;
        this.veh_fuel=veh_fuel;
        this.veh_cc=veh_cc;
        this.veh_km=veh_km;
        this.rcdetails = rcdetails;
        this.vehcategory =vehcategory;
        this.dealercom_name = dealercom_name;
        this.dealer_mob = dealer_mob;
        this.dealer_altermob = dealer_altermob;
        this.dealermailid = dealermailid;
        this.dealer_city = dealer_city;
    }

    public String getVeh_mfgname() {
        return veh_mfgname;
    }

    public void setVeh_mfgname(String veh_mfgname) {
        this.veh_mfgname = veh_mfgname;
    }

    public String getDealer_altermob() {
        return dealer_altermob;
    }

    public void setDealer_altermob(String dealer_altermob) {
        this.dealer_altermob = dealer_altermob;}

    public String getDealercom_name() {
        return dealercom_name;
    }

    public void setDealercom_name(String dealercom_name) {
        this.dealercom_name = dealercom_name;
    }

    public String getDealer_mob() {
        return dealer_mob;
    }

    public void setDealer_mob(String dealer_mob) {
        this.dealer_mob = dealer_mob;
    }

    public String getDealermailid() {
        return dealermailid;
    }

    public void setDealermailid(String dealermailid) {
        this.dealermailid = dealermailid;
    }

    public String getDealer_city() {
        return dealer_city;
    }

    public void setDealer_city(String dealer_city) {
        this.dealer_city = dealer_city;
    }


    public String getVehcategory() {
        return vehcategory;
    }

    public void setVehcategory(String vehcategory) {
        this.vehcategory = vehcategory;
    }

    public String getRcdetails() {return rcdetails;}

    public void setRcdetails(String rcdetails) {
        this.rcdetails = rcdetails;
    }

    public String getVeh_image() {return veh_image;}

    public void setVeh_image(String veh_image) {this.veh_image = veh_image;}

    public String getVeh_fullname() {return veh_fullname;}

    public void setVeh_fullname(String veh_fullname) {this.veh_fullname = veh_fullname;}

    public String getVeh_year() {return veh_year;}

    public void setVeh_year(String veh_year) {int value = (int) Double.parseDouble(veh_year);this.veh_year = String.valueOf(value);}

    public String getVeh_colour() {return veh_colour;}

    public void setVeh_colour(String veh_colour) {this.veh_colour = veh_colour;}

    public String getVeh_price() {return veh_price;}

    public void setVeh_price(String veh_price) {

        int value = (int) Double.parseDouble(veh_price);
        this.veh_price = "Rs." + String.valueOf(value) + "/-";
    }

    public String getVeh_model() {return veh_model;}

    public void setVeh_model(String veh_model) {this.veh_model = veh_model;}

    public String getVeh_variant() {return veh_variant;}

    public void setVeh_variant(String veh_variant) {this.veh_variant = veh_variant;}

    public String getVeh_transmission() {return veh_transmission;}

    public void setVeh_transmission(String veh_transmission) {this.veh_transmission = veh_transmission;}

    public String getVeh_ownership() {return veh_ownership;}

    public void setVeh_ownership(String veh_ownership) {this.veh_ownership = veh_ownership;}

    public String getVeh_fuel() {return veh_fuel;}

    public void setVeh_fuel(String veh_fuel) {this.veh_fuel = veh_fuel;}

    public String getVeh_cc() {return veh_cc;}


    public void setVeh_cc(String veh_cc) {int ccvalues = (int) Double.parseDouble(veh_cc);this.veh_cc = String.valueOf(ccvalues);}

    public String getVeh_km() {return veh_km;}


    public void setVeh_km(String veh_kms) {
        int kmvalues = (int) Double.parseDouble(veh_kms);
        this.veh_km = String.valueOf(kmvalues);}
}

