import java.util.ArrayList;
import java.util.Date;

// Manages the lists of lost items
public class ItemManager {

    int nextItemId = 1; // Unique ID for each item
    /* ArrayList is the list of array, 
       LostItem is the data type of the list,
       lostItem is the object name (just like Scanner),
       
       void is the return data type of the function, 
       addLostItem is the name of the function, 
       the name of the parameter is item and its data type is LostItem */
    ArrayList<LostItem> lostItem = new ArrayList<>();

    void addLostItem(LostItem item) 
    {
        item.itemId = nextItemId;
        nextItemId++;

        lostItem.add(item);
    }

    /* Same as for the Lost Item  */
    ArrayList<FoundItem> foundItem = new ArrayList<>();

    void addFoundItem(FoundItem item)
    {
        item.itemId = nextItemId;
        nextItemId++;

        foundItem.add(item);
    }

    // Marks an item as claimed and records who claimed it.
    void claimItem(Item item, String name, String contact) {
        item.status = "CLAIMED";
        item.claimedByName = name;
        item.claimedByContact = contact;
        item.claimedDate = new Date();
    }

    // Every claimed item, lost and found together.
    ArrayList<Item> getClaimedItems() {
        ArrayList<Item> result = new ArrayList<>();
        for (LostItem item : lostItem) {
            if (item.status.equals("CLAIMED")) result.add(item);
        }
        for (FoundItem item : foundItem) {
            if (item.status.equals("CLAIMED")) result.add(item);
        }
        return result;
    }

    // How many lost / found items are still open (not claimed).
    int openLostCount() {
        int count = 0;
        for (LostItem item : lostItem) {
            if (item.status.equals("OPEN")) count++;
        }
        return count;
    }

    int openFoundCount() {
        int count = 0;
        for (FoundItem item : foundItem) {
            if (item.status.equals("OPEN")) count++;
        }
        return count;
    }
}