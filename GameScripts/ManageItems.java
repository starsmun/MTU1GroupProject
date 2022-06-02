package GameScripts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ManageItems {

    public static List<Item> items = new LinkedList<>();

    public static void setupItems() {
        Scanner scanner = null;
        int lineNumber = 0;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader("GameScripts/ItemStats.txt")));

            String line;
            String[] iDetails;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if(lineNumber != 0) {

                    iDetails = line.split(",");

                    items.add(new Item(iDetails));
                }
                lineNumber++;
            }

        } catch (
                FileNotFoundException e) { // Catches when the file is not found
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public static Item getItem(int ID){
        return items.get(ID);
    }
}
