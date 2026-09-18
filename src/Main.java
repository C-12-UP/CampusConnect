import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner op = new Scanner(System.in);
        ItemManager manager = new ItemManager();

        int choice;
        System.out.println("Specify whether you found something or lost. ");
        choice =op.nextInt();

        if(choice == 1)
        {
            System.out.println("So you have Lost something. ");
            LostItem li = new LostItem();
            li.InputItem(op);
            li.InputofLostItem(op);
            manager.addLostItem(li);

            System.out.println("\nLost item recorded:");
            System.out.println("Type: " + li.typeOfItem);
            System.out.println("Color: " + li.color);
            System.out.println("Description: " + li.description);
            System.out.println("Item ID: " + li.itemId);
            System.out.println("Last seen location: " + li.location);
            System.out.println("Total lost items tracked: " + manager.lostItem.size());

        }
        else if(choice == 2)
        {
            System.out.println("So you have Found soething !!! .");
            FoundItem fi = new FoundItem();
            fi.InputItem(op);
            fi.InputFoundItem(op);
            manager.addFoundItem(fi);

            System.out.println("\nFound item recorded:");
            System.out.println("Type: " + fi.typeOfItem);
            System.out.println("Color: " + fi.color);
            System.out.println("Description: " + fi.description);
            System.out.println("Item ID: " + fi.itemId);
            System.out.println("Item found location: " + fi.foundLocation);
            System.out.println("Total found items tracked: " + manager.foundItem.size());

        }
        else
        {
            System.out.println("You have made an entry for other, ");
            System.out.print("How can we help you ");
        }

        
        op.close();
    }
}