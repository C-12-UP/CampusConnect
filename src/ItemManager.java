import java.util.ArrayList;

// Manages the lists of lost items
public class ItemManager {

    /* ArrayList is the list of array, 
       LostItem is the data type of the list,
       lostItem is the object name (just like Scanner),
       
       void is the return data type of the function, 
       addLostItem is the name of the function, 
       the name of the parameter is item and its data type is LostItem */
    ArrayList<LostItem> lostItem = new ArrayList<>();

    void addLostItem(LostItem item) 
    {
        lostItem.add(item);
    }

    /* Same as for the Lost Item  */
    ArrayList<FoundItem> foundItem = new ArrayList<>();

    void addFoundItem(FoundItem item)
    {
        foundItem.add(item);
    }
}