import java.util.Date;
import java.util.Scanner;

// Represents an item someone found
public class FoundItem extends Item {
    String foundLocation;
    String foundDate;
    String finderContact;

    public FoundItem(String name, String category, String location, Date date, String description) {
        
        super(name, category, location, date, description);
    }

    void InputFoundItem(Scanner op) {
        System.out.println("Enter the location the item was found");
        foundLocation = op.nextLine();

        System.out.println("Enter the date the item was found");
        foundDate = op.nextLine();

        System.out.println("Enter the contact of the person who found the item");
        finderContact = op.nextLine();
    }
}