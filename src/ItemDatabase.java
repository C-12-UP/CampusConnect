import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ItemDatabase {

    private final String url;

    public ItemDatabase() {
        // The database file lives in your user folder, e.g. C:\Users\Asus\lostfound.db
        File file = new File(System.getProperty("user.home"), "lostfound.db");
        url = "jdbc:sqlite:" + file.getAbsolutePath();
        System.out.println("Database file: " + file.getAbsolutePath());
        createTable();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "type TEXT NOT NULL, "
                + "name TEXT, category TEXT, location TEXT, "
                + "date INTEGER, description TEXT, "
                + "reporter_name TEXT, contact TEXT, "
                + "status TEXT NOT NULL DEFAULT 'OPEN', "
                + "claimed_by_name TEXT, claimed_by_contact TEXT, claimed_date INTEGER)";
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            System.err.println("Could not create table: " + e.getMessage());
        }
    }

    /** Saves a new item ("LOST" or "FOUND") and returns the id the database gave it, or -1 on failure. */
    public int insert(Item item, String type) {
        String sql = "INSERT INTO items (type, name, category, location, date, description, reporter_name, contact) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, type);
            p.setString(2, item.itemName);
            p.setString(3, item.category);
            p.setString(4, item.locationFound);
            p.setLong(5, item.date.getTime());
            p.setString(6, item.description);
            p.setString(7, item.reporterName);
            p.setString(8, item.contactInfo);
            p.executeUpdate();

            try (Statement s = c.createStatement();
                 ResultSet rs = s.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Could not save item: " + e.getMessage());
        }
        return -1;
    }

    /** Saves the claim details already set on the item. */
    public void markClaimed(Item item) {
        String sql = "UPDATE items SET status = 'CLAIMED', claimed_by_name = ?, "
                + "claimed_by_contact = ?, claimed_date = ? WHERE id = ?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, item.claimedByName);
            p.setString(2, item.claimedByContact);
            p.setLong(3, item.claimedDate.getTime());
            p.setInt(4, item.itemId);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not save claim: " + e.getMessage());
        }
    }

    /** Reads every saved item back as LostItem / FoundItem objects. */
    public ArrayList<Item> loadAll() {
        ArrayList<Item> items = new ArrayList<>();
        try (Connection c = connect();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM items ORDER BY id")) {
            while (rs.next()) {
                Date date = new Date(rs.getLong("date"));
                Item item;
                if (rs.getString("type").equals("LOST")) {
                    item = new LostItem(rs.getString("name"), rs.getString("category"), rs.getString("location"),
                            date, rs.getString("description"), rs.getString("reporter_name"), rs.getString("contact"));
                } else {
                    item = new FoundItem(rs.getString("name"), rs.getString("category"), rs.getString("location"),
                            date, rs.getString("description"), rs.getString("reporter_name"), rs.getString("contact"));
                }
                item.itemId = rs.getInt("id");
                item.status = rs.getString("status");
                item.claimedByName = rs.getString("claimed_by_name");
                item.claimedByContact = rs.getString("claimed_by_contact");
                long claimed = rs.getLong("claimed_date");
                if (!rs.wasNull()) item.claimedDate = new Date(claimed);
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Could not load items: " + e.getMessage());
        }
        return items;
    }
}