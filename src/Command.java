import javax.swing.event.DocumentEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Command {
    public static int command_no = 1;
    public static int device_no = 0;
    public static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    
    public static boolean check(String command){
        String[] com_arr = command.split("\t");
        if (!command.equals("Zreportlast")){
            Global.filehandler.Write("COMMAND: " + command);
        }
        else {
            Global.filehandler.Write("ZReport:");
            check_zreport();
            return true;
        }

        if(com_arr[0].equals("SetInitialTime")){
            boolean cont = check_set_initial_time(command);
            command_no++;
            if(cont){
                return true;
            } else {
                return false;
            }
        } else if(!com_arr[0].equals("SetInitialTime") && command_no == 1){
            Global.filehandler.Write("ERROR: First command must be set initial time! Program is going to terminate!");
            command_no++;
            return false;
        } else if (com_arr[0].equals("SetTime")) {
            check_set_time(command);
        } else if (com_arr[0].equals("SkipMinutes")) {
            check_skip_minutes(command);
        } else if (com_arr[0].equals("Nop")) {
            check_nop();
        } else if (com_arr[0].equals("Add")) {
            check_add(command);
        } else if (com_arr[0].equals("Remove")) {
            check_remove(command);
        } else if (com_arr[0].equals("SetSwitchTime")) {
            check_set_switch_time(command);
        } else if (com_arr[0].equals("Switch")) {
            check_switch(command);
        } else if (com_arr[0].equals("ChangeName")) {
            check_change_name(command);
        } else if (com_arr[0].equals("PlugIn")) {
            check_plug_in(command);
        } else if (com_arr[0].equals("PlugOut")) {
            check_plug_out(command);
        } else if (com_arr[0].equals("SetKelvin")) {
            check_set_kelvin(command);
        } else if (com_arr[0].equals("SetBrightness")) {
            check_set_brightness(command);
        } else if (com_arr[0].equals("SetColorCode")) {
            check_set_color_code(command);
        } else if (com_arr[0].equals("SetWhite")) {
            check_set_white(command);
        } else if (com_arr[0].equals("SetColor")) {
            check_set_color(command);
        } else if (com_arr[0].equals("ZReport")) {
            check_zreport();
        } else {
            Global.filehandler.Write("ERROR: Erroneous command!");
        }
        command_no++;
        return true;
    }


    public static boolean check_set_initial_time(String command){
        if (command_no == 1){
            String words[] = command.split("\t");
            if(words.length != 2){
                Global.filehandler.Write("ERROR: First command must be set initial time! Program is going to terminate!");
                return false;
            }
            else{
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(words[1], formatter1);
                    Global.global_time = dateTime;
                    Global.filehandler.Write("SUCCESS: Time has been set to " + dateTime.format(formatter1) + "!");
                }
                catch (Exception e){
                    Global.filehandler.Write("ERROR: Format of the initial date is wrong! Program is going to terminate!");
                    return false;
                }
            }
        }
        else{
            Global.filehandler.Write("ERROR: Erroneous command!");

        }
        return true;
    }

    public static void check_set_time(String command){
        String words[] = command.split("\t");
        if(words.length != 2){
            Global.filehandler.Write("ERROR: Erroneous command!");
        }

        else{
            try {
                LocalDateTime dateTime = LocalDateTime.parse(words[1], formatter1);
                if (Global.global_time.isAfter(dateTime)){
                    Global.filehandler.Write("ERROR: Time cannot be reversed!");
                } else if (Global.global_time.isEqual(dateTime)) {
                    Global.filehandler.Write("ERROR: There is nothing to change!");
                } else{
                    Global.global_time = dateTime;
                    Global.sort_switch_list();
                    for (int i = 0; i < Global.device_list.length; i++){
                        if(Global.device_list[i]!=null) {
                            if (Global.device_list[i].getSwitch_time() != null){
                                if (Global.device_list[i].getSwitch_time().isBefore(Global.global_time) || Global.device_list[i].getSwitch_time().isEqual(Global.global_time)) {
                                    if (Global.device_list[i].getStatus().equals("On")) {
                                        Global.device_list[i].setStatus("Off");
                                    } else if (Global.device_list[i].getStatus().equals("Off")) {
                                        Global.device_list[i].setStatus("On");
                                    }
                                    Global.device_list[i].setSwitch_time(null);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    Global.sort_switch_list();
                }
            }
            catch (Exception e){
                Global.filehandler.Write("ERROR: Time format is not correct!");
            }
        }
    }

    public static void check_skip_minutes(String command){
        String words[] = command.split("\t");
        if(words.length != 2){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!(words[1].matches("-?\\d+"))) {
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else{
            int skip_min = Integer.parseInt(words[1]);
            if (skip_min == 0){
                Global.filehandler.Write("ERROR: There is nothing to skip!");
            }
            else if (skip_min < 0){
                Global.filehandler.Write("ERROR: Time cannot be reversed!");
            }
            else{
                Global.global_time = Global.global_time.plusMinutes(skip_min);
                Global.sort_switch_list();
                for (int i = 0; i < Global.device_list.length; i++){
                    if(Global.device_list[i]!=null) {
                        if (Global.device_list[i].getSwitch_time() != null){
                            if (Global.device_list[i].getSwitch_time().isBefore(Global.global_time) || Global.device_list[i].getSwitch_time().isEqual(Global.global_time)) {
                                if (Global.device_list[i].getStatus().equals("On")) {
                                    Global.device_list[i].setStatus("Off");
                                } else if (Global.device_list[i].getStatus().equals("Off")) {
                                    Global.device_list[i].setStatus("On");
                                }
                                Global.device_list[i].setSwitch_time(null);
                            } else {
                                break;
                            }
                        }
                    }
                }
                Global.sort_switch_list();

            }

        }
    }

    public static void check_nop(){
        Global.sort_switch_list();
        if(Global.device_list[0]!=null) {
            if (Global.device_list[0].getSwitch_time() != null) {
                Global.global_time = Global.device_list[0].getSwitch_time();
                Global.sort_switch_list();
                for (int i = 0; i < Global.device_list.length; i++){
                    if(Global.device_list[i]!=null) {
                        if (Global.device_list[i].getSwitch_time() != null){
                            if (Global.device_list[i].getSwitch_time().isBefore(Global.global_time) || Global.device_list[i].getSwitch_time().isEqual(Global.global_time)) {
                                if (Global.device_list[i].getStatus().equals("On")) {
                                    Global.device_list[i].setStatus("Off");
                                } else if (Global.device_list[i].getStatus().equals("Off")) {
                                    Global.device_list[i].setStatus("On");
                                }
                                Global.device_list[i].setSwitch_time(null);
                            } else {
                                break;
                            }
                        }
                    }
                }
                Global.sort_switch_list();
            }
            else {
                Global.filehandler.Write("ERROR: There is nothing to switch!");
            }
        }
        else{
            Global.filehandler.Write("ERROR: There is nothing to switch!");
        }
    }

    public static void check_add(String command){
        String words[] = command.split("\t");
        if (words[1].equals("SmartPlug")) {

            if (words.length > 5 || words.length < 3){
                Global.filehandler.Write("ERROR: Erroneous command!");
            }

            else if(words.length==3){
                if (Global.check_name(words[2])){
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                }
                else {
                    SmartDevice smartPlug = new SmartPlug(words[2]);
                    Global.device_list[device_no] = smartPlug;
                    device_no++;
                }
            }

            else if (words.length==4) {
                if (Global.check_name(words[2])){
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                } else if (!("OnOff".contains(words[3]))) {
                    Global.filehandler.Write("ERROR: Erroneous command!");
                } else {
                    SmartDevice smartPlug = new SmartPlug(words[2],words[3]);
                    Global.device_list[device_no] = smartPlug;
                    device_no++;
                }
            }

            else if (words.length==5) {
                if (Global.check_name(words[2])){
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                }
                else if (!("OnOff".contains(words[3]))) {
                    Global.filehandler.Write("ERROR: Erroneous command!");
                }
                else if (!(words[4].matches("-?\\d+(\\.\\d+)?"))) {
                    Global.filehandler.Write("ERROR: Ampere value must be a positive number!");
                }
                else if (Double.parseDouble(words[4])<=0){
                    Global.filehandler.Write("ERROR: Ampere value must be a positive number!");
                }
                else {
                    SmartDevice smartPlug = new SmartPlug(words[2], words[3], Double.parseDouble(words[4]));
                    Global.device_list[device_no] = smartPlug;
                    device_no++;
                }
            }

        }

        else if (words[1].equals("SmartCamera")){

            if(words.length > 5 || words.length < 4){
                Global.filehandler.Write("ERROR: Erroneous command!");
            }

            else if (words.length==4){
                if (Global.check_name(words[2])) {
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                } else if (!(words[3].matches("-?\\d+(\\.\\d+)?"))) {
                    Global.filehandler.Write("ERROR: Megabyte value must be a positive number!");
                } else if (Double.parseDouble(words[3]) <= 0) {
                    Global.filehandler.Write("ERROR: Megabyte value must be a positive number!");
                } else{
                    SmartDevice smartCamera = new SmartCamera(words[2],Double.parseDouble(words[3]));
                    Global.device_list[device_no] = smartCamera;
                    device_no++;
                }

            }

            else if (words.length==5){
                if (Global.check_name(words[2])) {
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                }
                else if (!(words[3].matches("-?\\d+(\\.\\d+)?"))) {
                    Global.filehandler.Write("ERROR: Megabyte value must be a positive number!");
                }
                else if (Double.parseDouble(words[3]) <= 0) {
                    Global.filehandler.Write("ERROR: Megabyte value must be a positive number!");
                }
                else if (!("OnOff".contains(words[4]))) {
                    Global.filehandler.Write("ERROR: Erroneous command!");
                }
                else{
                    SmartDevice smartCamera = new SmartCamera(words[2],Double.parseDouble(words[3]),words[4]);
                    Global.device_list[device_no] = smartCamera;
                    device_no++;
                }
            }

        }

        else if (words[1].equals("SmartLamp")){
            if(words.length > 6 || words.length < 3 || words.length == 5){
                Global.filehandler.Write("ERROR: Erroneous command!");
            }

            else if (words.length == 3){
                 if (Global.check_name(words[2])) {
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                } else {
                     SmartDevice smartLamp = new SmartLamp(words[2]);
                     Global.device_list[device_no] = smartLamp;
                     device_no++;
                }
            }

            else if (words.length == 4) {
                if (Global.check_name(words[2])) {
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                } else if (!("OnOff".contains(words[3]))) {
                    Global.filehandler.Write("ERROR: Erroneous command!");
                } else {
                    SmartDevice smartLamp = new SmartLamp(words[2],words[3]);
                    Global.device_list[device_no] = smartLamp;
                    device_no++;
                }
            }

            else if (words.length == 6) {
                if (Global.check_name(words[2])) {
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                } else if (!("OnOff".contains(words[3]))) {
                    Global.filehandler.Write("ERROR: Erroneous command!");
                } else if (!(words[4].matches("\\d+"))) {
                    Global.filehandler.Write("ERROR: Erroneous command!");
                } else if (Integer.parseInt(words[4])<2000 || Integer.parseInt(words[4])>6500) {
                    Global.filehandler.Write("ERROR: Kelvin value must be in range of 2000K-6500K!");
                } else if (!(words[5].matches("\\d+"))) {
                    Global.filehandler.Write("ERROR: Erroneous command!");
                } else if (Integer.parseInt(words[5])<0 || Integer.parseInt(words[5])>100) {
                    Global.filehandler.Write("ERROR: Brightness must be in range of 0%-100%!");
                } else {
                    SmartDevice smartLamp = new SmartLamp(words[2],words[3],Integer.parseInt(words[4]),Integer.parseInt(words[5]));
                    Global.device_list[device_no] = smartLamp;
                    device_no++;
                }
            }

        }

        else if (words[1].equals("SmartColorLamp")){
            String mode = "";
            if(words.length>5){
                if(words[4].startsWith("0x")){
                    mode = "color_mode";
                }
                else if ((words[4].matches("\\d+"))){
                    mode = "white_mode";
                }
            }

            if(words.length > 6 || words.length < 3 || words.length == 5) {
                Global.filehandler.Write("ERROR: Erroneous command!");
            }

            else if (Global.check_name(words[2])) {
                Global.filehandler.Write("ERROR: There is already a smart device with same name!");
            }

            else if (words.length == 3){
                if (Global.check_name(words[2])) {
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                }
                else{
                    SmartDevice smartColorLamp = new SmartColorLamp(words[2]);
                    Global.device_list[device_no] = smartColorLamp;
                    device_no++;
                }
            }

            else if (words.length == 4) {
                if (Global.check_name(words[2])) {
                    Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                }
                else if (!("OnOff".contains(words[3]))) {
                    Global.filehandler.Write("ERROR: Erroneous command!");
                }
                else{
                    SmartDevice smartColorLamp = new SmartColorLamp(words[2],words[3]);
                    Global.device_list[device_no] = smartColorLamp;
                    device_no++;
                }
            }

            else if (words.length == 6) {
                if(mode == "color_mode"){
                    if (Global.check_name(words[2])) {
                        Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                    } else if (!("OnOff".contains(words[3]))) {
                        Global.filehandler.Write("ERROR: Erroneous command!");
                    } else if ((words[4].length() != 8)){
                        Global.filehandler.Write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
                    } else if ((!Global.isStringFromOtherString(words[4],"0x123456789ABCDEF"))) {
                        Global.filehandler.Write("ERROR: Erroneous command!");
                    } else if (!(words[5].matches("\\d+"))) {
                        Global.filehandler.Write("ERROR: Erroneous command!");
                    } else if (Integer.parseInt(words[5])<0 || Integer.parseInt(words[5])>100) {
                        Global.filehandler.Write("ERROR: Brightness must be in range of 0%-100%!");
                    } else{
                        SmartDevice smartColorLamp = new SmartColorLamp(words[2],words[3],words[4],Integer.parseInt(words[5]));
                        ((SmartColorLamp) smartColorLamp).setMode("color");
                        Global.device_list[device_no] = smartColorLamp;
                        device_no++;
                    }
                }

                else if (mode == "white_mode") {
                    if (Global.check_name(words[2])) {
                        Global.filehandler.Write("ERROR: There is already a smart device with same name!");
                    }
                    else if (!("OnOff".contains(words[3]))) {
                        Global.filehandler.Write("ERROR: Erroneous command!");
                    }
                    else if (!(words[4].matches("\\d+"))) {
                        Global.filehandler.Write("ERROR: Erroneous command!");
                    }
                    else if ((Integer.parseInt(words[4])<2000 || Integer.parseInt(words[4])>6500)) {
                        Global.filehandler.Write("ERROR: Kelvin value must be in range of 2000K-6500K!");
                    }
                    else if (!(words[5].matches("\\d+"))) {
                        Global.filehandler.Write("ERROR: Erroneous command!");
                    }
                    else if (Integer.parseInt(words[5])<0 || Integer.parseInt(words[5])>100) {
                        Global.filehandler.Write("ERROR: Brightness must be in range of 0%-100%!");
                    }
                    else{
                        SmartDevice smartColorLamp = new SmartColorLamp(words[2],words[3],Integer.parseInt(words[4]),Integer.parseInt(words[5]));
                        Global.device_list[device_no] = smartColorLamp;
                        device_no++;
                    }
                }

                else{
                    Global.filehandler.Write("ERROR: Erroneous command!");
                }
            }
        }

}

    public static void check_remove(String command){
        String words[] = command.split("\t");
        if(words.length!=2) {
            Global.filehandler.Write("ERROR: Erroneous command!");
        }
        else if (!Global.check_name(words[1])){
            Global.filehandler.Write("ERROR: There is not such a device!");
        }
        else{
            if(Global.object_finder(words[1]).getStatus().equals("On")) {
                Global.object_finder(words[1]).setStatus("Off");
            }
            Global.remove_item(words[1]);
            Global.sort_switch_list();
            device_no--;
        }

    }

    public static void check_set_switch_time(String command){
        String words[] = command.split("\t");
        if(words.length!=3) {
            Global.filehandler.Write("ERROR: Erroneous command!");
        }
        else if (!Global.check_name(words[1])){
            Global.filehandler.Write("ERROR: There is not such a device!");
        } else{
            try{
                LocalDateTime dateTime = LocalDateTime.parse(words[2], formatter1);
                if(!Global.global_time.isAfter(dateTime)) {
                    Global.object_finder(words[1]).setSwitch_time(dateTime);
                    Global.sort_switch_list();
                    if(Global.global_time.isEqual(dateTime)){
                        Global.object_finder(words[1]).setSwitch_time(null);
                        if(Global.object_finder(words[1]).getStatus().equals("On")){
                            Global.object_finder(words[1]).setStatus("Off");
                        }else if (Global.object_finder(words[1]).getStatus().equals("Off")){
                            Global.object_finder(words[1]).setStatus("On");
                        }
                        Global.sort_switch_list();
                    }
                }
                else {
                    Global.filehandler.Write("ERROR: Switch time cannot be in the past!");
                }
            }
            catch (Exception e){
                Global.filehandler.Write("ERROR: Time format is not correct!");
            }

        }
    }

    public static void check_switch(String command){
        String words[] = command.split("\t");
        if (words.length != 3){
            Global.filehandler.Write("ERROR: Erroneous command!");
        }
        else if (!Global.check_name(words[1])){
            Global.filehandler.Write("ERROR: There is not such a device!");
        }
        else if (!("OnOff".contains(words[2]))) {
            Global.filehandler.Write("ERROR: Erroneous command!");
        }
        else {
            if ((Global.object_finder(words[1]).getStatus().equals(words[2]))&&words[2].equals("On")){
                Global.filehandler.Write("ERROR: This device is already switched on!");
            } else if ((Global.object_finder(words[1]).getStatus().equals(words[2]))&&words[2].equals("Off")) {
                Global.filehandler.Write("ERROR: This device is already switched off!");
            } else {
               Global.object_finder(words[1]).setStatus(words[2]);
               Global.object_finder(words[1]).setSwitch_time(null);
               Global.sort_switch_list();
            }
        }
    }

    public static void check_change_name(String command){
        String words[] = command.split("\t");
        if(words.length!=3){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (words[1].equals(words[2])) {
            Global.filehandler.Write("ERROR: Both of the names are the same, nothing changed!");
        } else if (!Global.check_name(words[1])) {
            Global.filehandler.Write("ERROR: There is not such a device!");
        } else if (Global.check_name(words[2])) {
            Global.filehandler.Write("ERROR: There is already a smart device with same name!");
        } else{
            Global.object_finder(words[1]).setName(words[2]);
        }
    }

    public static void check_plug_in(String command){
        String words[] = command.split("\t");
        if(words.length!=3){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!Global.object_finder(words[1]).getClass().getName().equals("SmartPlug")) {
            Global.filehandler.Write("ERROR: This device is not a smart plug!");
        } else if (!(words[2].matches("\\d+"))) {
            Global.filehandler.Write("ERROR: Ampere value must be a positive number!");
        } else if (((SmartPlug) Global.object_finder(words[1])).isPlug()) {
            Global.filehandler.Write("ERROR: There is already an item plugged in to that plug!");
        } else{
            ((SmartPlug) Global.object_finder(words[1])).setPlug(true);
            ((SmartPlug) Global.object_finder(words[1])).setAmpere(Integer.parseInt(words[2]));
        }

    }

    public static void check_plug_out(String command){
        String words[] = command.split("\t");
        if(words.length!=2){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!Global.object_finder(words[1]).getClass().getName().equals("SmartPlug")) {
            Global.filehandler.Write("ERROR: This device is not a smart plug!");
        } else if (!((SmartPlug) Global.object_finder(words[1])).isPlug()) {
            Global.filehandler.Write("ERROR: This plug has no item to plug out from that plug!");
        } else{
            ((SmartPlug) Global.object_finder(words[1])).setPlug(false);
        }

    }

    public static void check_set_kelvin(String command){
        String words[] = command.split("\t");
        if(words.length!=3){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!(Global.object_finder(words[1]).getClass().getName().equals("SmartLamp") || Global.object_finder(words[1]).getClass().getName().equals("SmartColorLamp"))) {
            Global.filehandler.Write("ERROR: This device is not a smart lamp!");
        } else if (!(words[2].matches("\\d+"))) {
            Global.filehandler.Write("ERROR: Kelvin value must be a positive number!");
        } else if (Integer.parseInt(words[2])<2000 || Integer.parseInt(words[2])>6500) {
            Global.filehandler.Write("ERROR: Kelvin value must be in range of 2000K-6500K!");
        } else{
            ((SmartLamp) Global.object_finder(words[1])).setKelvin(Integer.parseInt(words[2]));
            if(Global.object_finder(words[1]).getClass().getName().equals("SmartColorLamp")){
                ((SmartColorLamp) Global.object_finder(words[1])).setMode("white");
            }
        }
    }

    public static void check_set_brightness(String command){
        String words[] = command.split("\t");
        if(words.length!=3){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!(Global.object_finder(words[1]).getClass().getName().equals("SmartLamp") || Global.object_finder(words[1]).getClass().getName().equals("SmartColorLamp"))) {
            Global.filehandler.Write("ERROR: This device is not a smart lamp!");
        } else if (!(words[2].matches("\\d+"))) {
            Global.filehandler.Write("ERROR: Brightness value must be a positive number!");
        } else if (Integer.parseInt(words[5])<0 || Integer.parseInt(words[5])>100) {
            Global.filehandler.Write("ERROR: Brightness must be in range of 0%-100%!");
        } else{
            ((SmartLamp) Global.object_finder(words[1])).setBrightness(Integer.parseInt(words[2]));
        }
    }

    public static void check_set_color_code(String command){
        String words[] = command.split("\t");
        if(words.length!=3){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!(Global.object_finder(words[1]).getClass().getName().equals("SmartColorLamp"))) {
            Global.filehandler.Write("ERROR: This device is not a smart color lamp!");
        } else if ((words[2].length() != 8)){
            Global.filehandler.Write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
        } else if ((!Global.isStringFromOtherString(words[2],"0x123456789ABCDEF"))) {
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else{
            ((SmartColorLamp) Global.object_finder(words[1])).setColor_code(words[2]);
            ((SmartColorLamp) Global.object_finder(words[1])).setMode("color");
        }
    }

    public static void check_set_white(String command){
        String words[] = command.split("\t");
        if(words.length!=4){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!(Global.object_finder(words[1]).getClass().getName().equals("SmartLamp") || Global.object_finder(words[1]).getClass().getName().equals("SmartColorLamp"))) {
            Global.filehandler.Write("ERROR: This device is not a smart lamp!");
        } else if (!(words[2].matches("\\d+"))) {
            Global.filehandler.Write("ERROR: Kelvin value must be a positive number!");
        } else if (Integer.parseInt(words[2])<2000 || Integer.parseInt(words[2])>6500) {
            Global.filehandler.Write("ERROR: Kelvin value must be in range of 2000K-6500K!");
        } else if (!(words[3].matches("\\d+"))) {
            Global.filehandler.Write("ERROR: Brightness value must be a positive number!");
        } else if (Integer.parseInt(words[3])<0 || Integer.parseInt(words[3])>100) {
            Global.filehandler.Write("ERROR: Brightness must be in range of 0%-100%!");
        } else{
            ((SmartColorLamp) Global.object_finder(words[1])).setKelvin(Integer.parseInt(words[2]));
            ((SmartColorLamp) Global.object_finder(words[1])).setBrightness(Integer.parseInt(words[3]));
            if(Global.object_finder(words[1]).getClass().getName().equals("SmartColorLamp")){
                ((SmartColorLamp) Global.object_finder(words[1])).setMode("white");
            }
        }
    }

    public static void check_set_color(String command){
        String words[] = command.split("\t");
        if(words.length!=4){
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!(Global.object_finder(words[1]).getClass().getName().equals("SmartColorLamp"))) {
            Global.filehandler.Write("ERROR: This device is not a smart color lamp!");
        } else if ((words[2].length() != 8)){
            Global.filehandler.Write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
        } else if ((!Global.isStringFromOtherString(words[2],"0x123456789ABCDEF"))) {
            Global.filehandler.Write("ERROR: Erroneous command!");
        } else if (!(words[3].matches("\\d+"))) {
            Global.filehandler.Write("ERROR: Brightness value must be a positive number!");
        } else if (Integer.parseInt(words[3])<0 || Integer.parseInt(words[3])>100) {
            Global.filehandler.Write("ERROR: Brightness must be in range of 0%-100%!");
        } else{
            ((SmartColorLamp) Global.object_finder(words[1])).setColor_code(words[2]);
            ((SmartColorLamp) Global.object_finder(words[1])).setBrightness(Integer.parseInt(words[3]));
            if(Global.object_finder(words[1]).getClass().getName().equals("SmartColorLamp")){
                ((SmartColorLamp) Global.object_finder(words[1])).setMode("color");
            }
        }
    }

    public static void check_zreport(){
        Global.filehandler.Write("Time is:\t" + Global.global_time.format(formatter1));
        for(int i = 0; i < Global.device_list.length; i++){
            if (Global.device_list[i]!=null){
                Global.device_list[i].zreport();
            }
        }
    }

}
