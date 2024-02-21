import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {
    public static String output = "output.txt";
    public ArrayList<String> Read(String filepath) {
        String fileName = filepath; // dosya adı ve yolu
        ArrayList<String> lines = new ArrayList<>(); // ArrayList oluştur

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) { // dosya satır satır okunuyor
                if(!line.trim().isEmpty()) {
                    lines.add(line); // her satırı ArrayList'e ekleniyor
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void Write(String content) {
        File file = new File(output);

        try {
            FileWriter writer = new FileWriter(file,true);
            if (content==null){
                writer.write("null\n");
            }
            writer.write(content);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            Global.filehandler.Write("ERROR: Erroneous command!" + e.getMessage());
        }
    }

    public static void Write(LocalDateTime content) {
        File file = new File(output);

        try {
            FileWriter writer = new FileWriter(file, true);
            if (content==null){
                writer.write("null\n");
            }
            writer.write(content.toString());
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            Global.filehandler.Write("ERROR: Erroneous command!" + e.getMessage());
        }
    }

    public static void Write() {
        File file = new File(output);

        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            Global.filehandler.Write("ERROR: Erroneous command!" + e.getMessage());
        }
    }

    public static void DeleteAllContent(){
        File file = new File(output);

        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            Global.filehandler.Write("ERROR: Erroneous command!" + e.getMessage());
        }
    }
}