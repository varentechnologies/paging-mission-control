import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class PagingMissionControl {

    public static void main(String[] args) {
        File f=new File("./input.txt");
        try(Scanner sc= new Scanner(f, StandardCharsets.UTF_8)) { // Ensures Scanner Disposed
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] lineSplit = line.split("|");
                SimpleDateFormat parser = new SimpleDateFormat("YYYYMMDD HH:mm:ss.SSS");
                Date timestamp = parser.parse(lineSplit[0]);
                int satelliteId = Integer.parseInt(lineSplit[1]);
                int redHighLimit = Integer.parseInt(lineSplit[2]);
                int yellowHighLimit = Integer.parseInt(lineSplit[3]);
                int yellowLowLimit = Integer.parseInt(lineSplit[4]);
                int redLowLimit = Integer.parseInt(lineSplit[5]);
                float rawValue = Float.parseFloat(lineSplit[6]);
                String component = lineSplit[7];
                
                System.out.println(line);
            }
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }
}