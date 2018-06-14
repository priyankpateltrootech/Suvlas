package common;

public class Comman_message {
    public static final String Getdata_error_message = "Sorry, please try after some time.";
    public static final String No_internet = "No Internet Connection";
    public static final String Dont_internet = "You don't have internet connection.";
    public static final String Gps_enable = "GPS is disabled in your device. Would you like to enable it?";
    public static final String Image_upload_multipal = "Please select maximum 10 image only.";
    public static final String Image_upload_noselect = "Please select at least 1 image.";
    public static final String Play_enable = "You dont have google play service please update it.";

    public static String getMonth(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";

            default:
                return "January";
        }
    }

    public static final String Select_Category = "";

}
