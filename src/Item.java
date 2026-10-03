import java.util.Date;
//import java.util.Scanner;

// Base class for any item
public class Item {

    String itemName;
    String category;
    String locationFound;
    Date date;
    String description;
    int itemId;

    Item() {
        //Its an empty default constructor which is there.
        // As if there are no constructor java creates one on it own, which can call an bydefault constructor which might give garbage value.
    }

    Item(String itemName, String category, String locationFound, Date date, String description ) {
        this.itemName = itemName;
        this.category = category;
        this.locationFound = locationFound;
        this.date = date;
        this.description = description;

    /* 
    void InputItem(Scanner op) {
        System.out.println("Enter the name of the item: ");
        itemName = op.nextLine();
        op.nextLine();

        System.out.println("Enter the category of the item: ");
        category = op.nextLine();
        op.nextLine();

        System.out.println("Enter the location where the item was found: ");
        locationFound = op.nextLine();
        op.nextLine();

        System.out.println("Enter the date when the item was found: ");
        date = op.nextDate();
        op.nextLine();

        System.out.println("Enter the description of the item: ");
        description = op.nextLine();
        op.nextLine();

        System.out.println("Enter the itemId of the product: ");
        itemId = op.nextInt();
        op.nextLine();

        System.out.println("Enter the description of item (eg colour, shape, size, etc): ");
        description = op.nextLine();
        */

    }
}
    