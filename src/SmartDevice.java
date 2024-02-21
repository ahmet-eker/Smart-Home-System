import java.time.LocalDateTime;

public abstract class SmartDevice {
   private String name;
   private LocalDateTime switch_time;
   private String status = "Off";


   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   public LocalDateTime getSwitch_time() {
      if(switch_time!=null) {
         return switch_time;
      }
      else{
         return null;
      }
   }

   public void setSwitch_time(LocalDateTime switch_time) {
      this.switch_time = switch_time;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public abstract void zreport();

}
