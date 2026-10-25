import java.util.ArrayList;
import java.util.Date;

// Manages the lists of lost items
public class ItemManager {

    ItemDatabase db = new ItemDatabase();   // the saved data lives here

    static final String[] CATEGORIES = {"Electronics", "Books", "Clothing", "Accessories", "Other"};
    
    /* ArrayList is the list of array, 
       LostItem is the data type of the list,
       lostItem is the object name (just like Scanner),
       
       void is the return data type of the function, 
       addLostItem is the name of the function, 
       the name of the parameter is item and its data type is LostItem */
    ArrayList<LostItem> lostItem = new ArrayList<>();

    ItemManager() {
     // When the app starts, load everything saved in earlier runs
     for (Item item : db.loadAll()) {
        if (item instanceof LostItem) {
            lostItem.add((LostItem) item);
        } else {
            foundItem.add((FoundItem) item);
        }
    }
    }

    void addLostItem(LostItem item) 
    {
        int id = db.insert(item, "LOST");
        if (id == -1) return;      // saving failed, so don't show it as if it worked
        item.itemId = id;
        lostItem.add(item);
    }

    /* Same as for the Lost Item  */
    ArrayList<FoundItem> foundItem = new ArrayList<>();

    void addFoundItem(FoundItem item)
    {
        int id = db.insert(item, "FOUND");
        if (id == -1) return;
        item.itemId = id;
        foundItem.add(item);
    }

    // Marks an item as claimed and records who claimed it.
    void claimItem(Item item, String name, String contact) {
        item.status = "CLAIMED";
        item.claimedByName = name;
        item.claimedByContact = contact;
        item.claimedDate = new Date();
        db.markClaimed(item);
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