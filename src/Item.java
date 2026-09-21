import java.util.Scanner;

// Base class for any item
public class Item {
    String typeOfItem, color, description;
    int itemId;

    Item() {
        //Its an empty default constructor which is there.
        // As if there are no constructor java creates one on it own, which can call an bydefault constructor which might give garbage value.
    }

    Item(String typeOfItem, String color, String description, int itemId) {
        this.typeOfItem = typeOfItem;
        this.color = color;
        this.description = description;
        this.itemId = itemId;
    }

    void InputItem(Scanner op) {
        System.out.println("Enter the type of item (eg: electronic, etc): ");
        typeOfItem = op.nextLine();
        op.nextLine();

        System.out.println("Enter the itemId of the product : ");
        itemId = op.nextInt();
        op.nextLine();

        System.out.println("Enter the description of item (eg colour, shape, size, etc): ");
        description = op.nextLine();

    }
}
    