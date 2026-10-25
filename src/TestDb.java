import java.util.Date;

public class TestDb {
    public static void main(String[] args) {
        ItemDatabase db = new ItemDatabase();

        LostItem x = new LostItem("Bottle", "Accessories", "Library", new Date(), "Blue", "Asur", "123");
        x.itemId = db.insert(x, "LOST");
        System.out.println("Inserted id: " + x.itemId);

        x.claimedByName = "Ravi";
        x.claimedByContact = "999";
        x.claimedDate = new Date();
        db.markClaimed(x);

        System.out.println("Rows in database: " + db.loadAll().size());
    }
}