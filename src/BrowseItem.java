import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class BrowseItem extends JPanel {

    // Components
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel profileLabel;

    private JTextField searchField;
    private JComboBox<String> categoryBox;

    private ItemManager itemManager;

    private JPanel headerPanel;
    private JPanel titlePanel;
    private JPanel profilePanel;
    //private JPanel searchPanel;
    //private JPanel categoryPanel;
    private JPanel itemPanel;

    public BrowseItem(ItemManager itemManager) {

        this.itemManager = itemManager;

        // Main panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        // =========================
        // HEADER PANEL
        // =========================

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 231, 233));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Title
        titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(255, 231, 233));

        titleLabel = new JLabel("Browse Item");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 14));

        titlePanel.add(titleLabel);

        // Profile
        profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        profilePanel.setBackground(Color.ORANGE);

        profileLabel = new JLabel("Dhyay");

        // Profile image
        try {
            ImageIcon icon = new ImageIcon(
                getClass().getResource("/gui/Image/Profile.png")
            );

            Image image = icon.getImage().getScaledInstance(
                25, 25, Image.SCALE_SMOOTH
            );

            profileLabel.setIcon(new ImageIcon(image));

        } catch (Exception e) {
            System.out.println("Profile image not found.");
        }

        profileLabel.setFont(new Font("Vivaldi", Font.BOLD, 14));

        profilePanel.add(profileLabel);

        // Add title and profile to header
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(profilePanel, BorderLayout.EAST);

        // =========================
        // SEARCH AREA
        // =========================

        JPanel searchArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        searchArea.setBackground(new Color(255, 231, 233));

        subtitleLabel = new JLabel("Search and filter Items");
        subtitleLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 10));

        searchField = new JTextField("Search by name, etc.", 15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        String[] categories = {
            "All Categories",
            "Electronics",
            "Books",
            "Clothing",
            "Accessories"
        };

        categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        searchArea.add(subtitleLabel);
        searchArea.add(searchField);
        searchArea.add(categoryBox);

        // =========================
        // TOP SECTION
        // =========================

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(new Color(255, 231, 233));

        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(searchArea, BorderLayout.SOUTH);

        // =========================
        // ITEM DISPLAY PANEL
        // =========================

        itemPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(itemPanel, BoxLayout.Y_AXIS);
        itemPanel.setBackground(new Color(255, 218, 235));
        itemPanel.setLayout(boxLayout);   

        // =========================
        // ADD TO MAIN PANEL
        // =========================

        add(topSection, BorderLayout.NORTH);
        add(itemPanel, BorderLayout.CENTER);

        // =========================
        // EVENTS
        // =========================

        profileLabel.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                profileClicked();
            }
        });

        searchField.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchFieldClicked();
            }
        });

        categoryBox.addActionListener(e -> categorySelected());

        displayItems();
        
    }

     private void displayItems() {

        itemPanel.removeAll();

        if (itemManager != null) {

            for (LostItem item : itemManager.lostItem) {

                JPanel itemCard = new JPanel();
                itemCard.setLayout(new BoxLayout(itemCard, BoxLayout.Y_AXIS));

                JLabel nameLabel = new JLabel("Name: " + item.itemName);
                JLabel category = new JLabel("Type: " + item.category);
                JLabel locationf = new JLabel("Location: " + item.locationFound);
                JLabel datE = new JLabel("Date: " + item.date);
                JLabel descriptioN = new JLabel("Description: " + item.description);
                JLabel itemid = new JLabel("ItemId: " + item.itemId);

                itemCard.add(nameLabel);
                itemCard.add(category);
                itemCard.add(locationf);
                itemCard.add(datE);
                itemCard.add(descriptioN);
                itemCard.add(itemid);

                itemPanel.add(itemCard);
            }
        }

        itemPanel.revalidate();
        itemPanel.repaint();
    }


    // =========================
    // EVENT METHODS
    // =========================

    private void profileClicked() {

        System.out.println("Profile clicked");

        // Add profile page logic here later
    }

    private void searchFieldClicked() {

        // Clear default text when user clicks
        if (searchField.getText().equals("Search by name, etc.")) {
            searchField.setText("");
        }
    }

    private void categorySelected() {

        String selectedCategory =
                (String) categoryBox.getSelectedItem();

        System.out.println(
                "Selected Category: " + selectedCategory
        );

        // Add filtering logic here later
    }

    // =========================
    // TEST MAIN METHOD
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Lost & Found");

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setSize(700, 450);

            frame.setLocationRelativeTo(null);

            frame.setContentPane(new BrowseItem(null));

            frame.setVisible(true);
        });
    }
}