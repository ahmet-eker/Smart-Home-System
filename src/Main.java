import java.io.File;
import java.util.ArrayList;

// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Global.filehandler.output = args[1]; //input file
        ArrayList<String> read_lines = Global.filehandler.Read(args[0]); // inputs
        Global.filehandler.DeleteAllContent(); //resets output file
        if (!read_lines.get(read_lines.size() - 1).equals("ZReport")){ //this segment is for last ZReport
            read_lines.add("Zreportlast");
        }
        Global.df.setMinimumFractionDigits(2); //this one is for zpeort digits number after comma
        boolean cont = true;
        for (String line : read_lines){  //this segment is for in case first command is not setinitialtime
            if(cont) {
                cont = Command.check(line);
            }
        }
    }
}
