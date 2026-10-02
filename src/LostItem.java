// Represents an item someone lost
public class LostItem extends Item {

    LostItem() {
        super();
    }

    LostItem(String itemName, String category, String locationFound,
             java.util.Date date, String description, int itemId) {

        super(itemName, category, locationFound, date, description, itemId);
    }
}
