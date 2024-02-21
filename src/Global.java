import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Global {
    public static FileIO filehandler = new FileIO(); // file input output object
    public static LocalDateTime global_time; //global time
    public static SmartDevice[] device_list = new SmartDevice[50];  //device list
    public static DecimalFormat df = new DecimalFormat("#.##"); //for decimal format


    /**
     * Returns true if given parameter is named already one of the devices
     *
     *
     * @param  name the name of the device
     * @return      if the name is already taken or not
     */
    public static boolean check_name(String name){
        for(SmartDevice device : device_list){
            if (device != null)//device.getName().equals("name")
                if(device.getName().equals(name)) {
                    return true;
                }
        }
        return false;
    }

    /**
     * Only sorts the devices lists depending on their switchtime
     *
     * @param
     * @return
     */
    public static void sort_switch_list(){
        //Arrays.sort(device_list, Comparator.comparing(SmartDevice::getSwitch_time, Comparator.nullsLast(Comparator.naturalOrder())));
        Arrays.sort(device_list, new Comparator<SmartDevice>() {
            @Override
            public int compare(SmartDevice o1, SmartDevice o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 == null) {
                    return 1;
                } else if (o2 == null) {
                    return -1;
                } else if (o1.getSwitch_time() == null && o2.getSwitch_time() == null) {
                    return 0;
                } else if (o1.getSwitch_time() == null) {
                    return 1;
                } else if (o2.getSwitch_time() == null) {
                    return -1;
                } else {
                    return o1.getSwitch_time().compareTo(o2.getSwitch_time());
                }
            }
        });
    }

    /**
     * Only sorts the devices lists depending on their switchtime
     *
     * @param
     * @return
     */
    public static void sort1_switch_list() {
        Arrays.sort(device_list, new Comparator<SmartDevice>() {
            @Override
            public int compare(SmartDevice o1, SmartDevice o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 == null) {
                    return -1;
                } else if (o2 == null) {
                    return 1;
                } else {
                    Boolean o1IsNullBefore = (o1.getSwitch_time() == null);
                    Boolean o2IsNullBefore = (o2.getSwitch_time() == null);
                    Boolean o1IsNullNow = (o1.getSwitch_time() == null);
                    Boolean o2IsNullNow = (o2.getSwitch_time() == null);

                    if (o1IsNullBefore && o2IsNullBefore) {
                        return 0;
                    } else if (o1IsNullBefore) {
                        return -1;
                    } else if (o2IsNullBefore) {
                        return 1;
                    } else if (o1IsNullNow && o2IsNullNow) {
                        return 0;
                    } else if (o1IsNullNow) {
                        return -1;
                    } else if (o2IsNullNow) {
                        return 1;
                    } else {
                        return o1.getSwitch_time().compareTo(o2.getSwitch_time());
                    }
                }
            }
        });
    }

    /**
     * Returns true if sttring 1 is from string 2
     *
     *
     * @param  str1 the name of the first string
     * @param  str2 the name of the second string
     * @return      true or false
     */
    public static boolean isStringFromOtherString(String str1, String str2) {
        for (char c : str1.toCharArray()) {
            if (str2.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes an item based on its name
     *
     *
     * @param  name the name of the removed device
     * @return
     */
    public static void remove_item(String name){
        boolean removed = false;
        int remove_index = device_list.length-1;
        for (int i = 0; i < Global.device_list.length; i++){
            if(device_list[i]!=null) {
                if (name.equals(device_list[i].getName())) {
                    removed = true;
                    remove_index = i;
                }
            }
        }
        if (removed){
            Global.filehandler.Write("SUCCESS: Information about removed smart device is as follows:");
            object_finder(name).zreport();
            Global.device_list[remove_index] = null;
            sort_switch_list();
        }
        else{
            Global.filehandler.Write("ERROR: Erroneous command!");
        }
    }

    /**
     * finds the object with using its only names
     *
     *
     * @param  name the name of the wanted device
     * @return SmartDevice whhic has the given name
     */
    public static SmartDevice object_finder(String name){
        for(int k = 0; k<Global.device_list.length; k++){
            try{
                if (Global.device_list[k].getName().equals(name)){
                    return Global.device_list[k];
                }
            }
            catch (Exception e){
                continue;
            }
        }
        return null;
    }
}
