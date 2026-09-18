import java.util.Scanner;

// Represents an item someone lost
public class LostItem extends Item {
    String location;

    void InputofLostItem(Scanner op) {
        System.out.println("Enter the location where you last saw the item");
        location = op.nextLine();

    }
}