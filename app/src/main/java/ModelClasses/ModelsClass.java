package ModelClasses;

public class ModelsClass {

    private String img_code;
    private String img_path;
    private String vmodelimage;
    private String vehshortname;
    private String vehiclemodelname;
    private String vmodelcode;

    private String vtypename;

    private String vtype_code;

    int company_code;
    private String user_code,UserName,person_name, user_image,com_name,user_active,user_mobile,user_mobile1,user_email,user_type,
            ref_code,com_email,LockoutEnabled,Id,com_pin,city_code,state_code,city_name,state_name;


    public ModelsClass(){

    }
    public ModelsClass(String vmodelcode,String vehshortname,String vmodelimage,String vtype_code,String vehiclemodelname,String vtypename,
                       String user_code, String UserName, String person_name, String com_name,
                       String user_active, String user_mobile, String user_mobile1, String user_email, String user_type, String ref_code,
                       String com_email, String LockoutEnabled, String Id, String com_pin, String city_code, String state_code,
                       String city_name, String state_name){
        this.vtypename = vtypename;
        this.vehiclemodelname = vehiclemodelname;
        this.vmodelcode = vmodelcode;
        this.vehshortname = vehshortname;
        this.vmodelimage = vmodelimage;
        this.vtype_code = vtype_code;
    }

    public int getCompany_code() {
        return company_code;
    }

    public void setCompany_code(int company_code) {
        this.company_code = company_code;
    }

    public String getVehshortname() {
        return vehshortname;
    }

    public void setVehshortname(String vehshortname) {
        this.vehshortname = vehshortname;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getUser_active() {
        return user_active;
    }

    public void setUser_active(String user_active) {
        this.user_active = user_active;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_mobile1() {
        return user_mobile1;
    }

    public void setUser_mobile1(String user_mobile1) {
        this.user_mobile1 = user_mobile1;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getRef_code() {
        return ref_code;
    }

    public void setRef_code(String ref_code) {
        this.ref_code = ref_code;
    }

    public String getCom_email() {
        return com_email;
    }

    public void setCom_email(String com_email) {
        this.com_email = com_email;
    }

    public String getLockoutEnabled() {
        return LockoutEnabled;
    }

    public void setLockoutEnabled(String lockoutEnabled) {
        LockoutEnabled = lockoutEnabled;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCom_pin() {
        return com_pin;
    }

    public void setCom_pin(String com_pin) {
        this.com_pin = com_pin;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getVtypename() {
        return vtypename;
    }

    public void setVtypename(String vtypename) {
        this.vtypename = vtypename;
    }

    public String getVehiclemodelname() {
        return vehiclemodelname;
    }

    public void setVehiclemodelname(String vehiclemodelname) {
        this.vehiclemodelname = vehiclemodelname;
    }

    public String getVtype_code() {
        return vtype_code;
    }

    public void setVtype_code(String vtype_code) {
        this.vtype_code = vtype_code;
    }

    public String getVmodelimage() {
        return vmodelimage;
    }

    public String getVmodelname() {
        return vehshortname;
    }

    public String getVmodelcode() {
        return vmodelcode;
    }

    public void setVmodelimage(String vmodelimage) {
        this.vmodelimage = vmodelimage;
    }

    public void setVmodelname(String vehshortname) {
        this.vehshortname = vehshortname;
    }

    public void setVmodelcode(String vmodelcode) {
        this.vmodelcode = vmodelcode;
    }

    public String getImg_code() {
        return img_code;
    }

    public void setImg_code(String img_code) {
        this.img_code = img_code;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}