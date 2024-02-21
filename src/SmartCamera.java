import java.time.Duration;
import java.time.LocalDateTime;

public class SmartCamera extends SmartDevice{
    private double mgb_per_minute;
    private LocalDateTime start_time;
    private double consumed_megabyte = 0.00;

    public SmartCamera(String name, double mgb_per_minute) {
        this.setName(name);
        this.mgb_per_minute = mgb_per_minute;
    }

    public SmartCamera(String name, double mgb_per_minute, String status) {
        this.setName(name);
        this.mgb_per_minute = mgb_per_minute;
        this.setStatus(status);
    }

    public double getMgb_per_minute() {
        return mgb_per_minute;
    }

    public void setMgb_per_minute(double mgb_per_minute) {
        this.mgb_per_minute = mgb_per_minute;
    }

    public void add_storage(double mgb_per_minute){
        consumed_megabyte += (Duration.between(start_time, Global.global_time).toMinutes())*mgb_per_minute;
        setStart_time(null);
     }

    public double getConsumed_megabyte() {
        return consumed_megabyte;
    }

    public void setConsumed_megabyte(double consumed_megabyte) {
        this.consumed_megabyte = consumed_megabyte;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public void setStatus(String status) {
        if(status.equals("On")){
            setStart_time(Global.global_time);
        } else if (status.equals("Off")) {
            if(getStart_time()!=null) {
                add_storage(mgb_per_minute);
            }
        }
        super.setStatus(status);
    }

    public void zreport(){
        String switchtime = "null";
        if(getSwitch_time() != null){
            switchtime = getSwitch_time().format(Command.formatter1);
        }
        else {
            switchtime = "null";
        }
        Global.filehandler.Write("Smart Camera " + getName() + " is " + getStatus().toLowerCase() + " and used " + Global.df.format(getConsumed_megabyte()).replace(".",",") +" MB of storage so far (excluding current status), and its time to switch its status is " + switchtime + ".");
    }

}
