import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.util.ArrayList;

public class DashboardPanel extends JPanel {

    private ItemManager itemManager;

    private String username;

    private JButton lostItemsButton;
    private JButton foundItemsButton;
    private JButton claimedButton;

    private JLabel lostCountLabel;
    private JLabel foundCountLabel;
    private JLabel claimedCountLabel;

    // Where the recent-items list/table will go later
    private JPanel recentItemsPanel;

    private static final Color BG_COLOR = new Color(255, 231, 233);

    public DashboardPanel(ItemManager itemManager, String username) {
        this.itemManager = itemManager;
        this.username = username;
        initComponents();
    }

    private void initComponents() {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(BG_COLOR);

        topSection.add(buildHeader());
        topSection.add(Box.createVerticalStrut(15));
        topSection.add(buildCardsRow());
        topSection.add(Box.createVerticalStrut(15));

        JLabel recentLabel = new JLabel("Recent Items");
        recentLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        recentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(recentLabel);

        //recentItemsPanel = new JPanel();
        //recentItemsPanel.setBackground(Color.WHITE);

        //add(topSection, BorderLayout.NORTH);
        //add(recentItemsPanel, BorderLayout.CENTER);
        recentItemsPanel = new JPanel();
        recentItemsPanel.setLayout(new BoxLayout(recentItemsPanel, BoxLayout.Y_AXIS));
        recentItemsPanel.setBackground(Color.WHITE);
        recentItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        add(topSection, BorderLayout.NORTH);
        add(new JScrollPane(recentItemsPanel), BorderLayout.CENTER);

    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel("DASHBOARD");
        titleLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

        JLabel subtitleLabel = new JLabel("Overview of lost & found item");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));

        titleBlock.add(titleLabel);
        titleBlock.add(subtitleLabel);

        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(255, 255, 204));
        profilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel profileLabel = new JLabel(username, loadIcon("Profile.png"), JLabel.CENTER);
        profileLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        profilePanel.add(profileLabel);

        header.add(titleBlock, BorderLayout.WEST);
        header.add(profilePanel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildCardsRow() {
        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 15, 0));
        cardsRow.setBackground(BG_COLOR);

        lostItemsButton = new JButton("Lost Items");
        lostCountLabel = new JLabel("COUNT: 000");
        cardsRow.add(buildStatCard(lostItemsButton, "LostItem.png",
                new Color(224, 102, 102), lostCountLabel));
        lostItemsButton.addActionListener(this::lostItemsClicked);

        foundItemsButton = new JButton("Found Items");
        foundCountLabel = new JLabel("COUNT: 000");
        cardsRow.add(buildStatCard(foundItemsButton, "FoundItem.png",
                new Color(129, 199, 132), foundCountLabel));
        foundItemsButton.addActionListener(this::foundItemsClicked);

        claimedButton = new JButton("Claimed");
        claimedCountLabel = new JLabel("COUNT: 000");
        cardsRow.add(buildStatCard(claimedButton, "DClaimed.png",
                new Color(255, 193, 7), claimedCountLabel));
        claimedButton.addActionListener(this::claimedClicked);

        return cardsRow;
    }

    private JPanel buildStatCard(JButton button, String iconFile, Color bgColor, JLabel countLabel) {
        button.setIcon(loadIcon(iconFile));
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Give the button a card-like height and let it stretch to the card's width
        button.setPreferredSize(new Dimension(139, 60));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        countLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        countLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, countLabel.getPreferredSize().height));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_COLOR);
        card.add(button);
        card.add(Box.createVerticalStrut(8));
        card.add(countLabel);
        return card;
    }

    // Loads an icon from the "Images" folder under src.
    private ImageIcon loadIcon(String fileName) {
        java.net.URL url = getClass().getResource("/Images/" + fileName);
        if (url == null) {
            System.out.println("Warning: could not find icon " + fileName);
            return null;
        }
        return new ImageIcon(url);
    }

    // --- Click handlers: fill these in as the pages/logic come together ---

    private void lostItemsClicked(ActionEvent evt) {
        System.out.println("Lost Items clicked");
        ((CardLayout) getRootPane().getLayout()).show(getRootPane(), "LostItems");
    }

    private void foundItemsClicked(ActionEvent evt) {
        System.out.println("Found Items clicked");
        ((CardLayout) getRootPane().getLayout()).show(getRootPane(), "FoundItems");
    }

    private void claimedClicked(ActionEvent evt) {
        System.out.println("Claimed clicked");
        //((CardLayout) getRootPane().getLayout()).show(getRootPane(), "Claimed");
    }

    private void refreshRecentItems() {
        recentItemsPanel.removeAll();

        // Collect every open item, lost and found together.
        ArrayList<Item> recent = new ArrayList<>();
        for (LostItem item : itemManager.lostItem) {
            if (item.status.equals("OPEN")) recent.add(item);
        }
        for (FoundItem item : itemManager.foundItem) {
            if (item.status.equals("OPEN")) recent.add(item);
        }

        // Newest first: a higher itemId means it was reported later.
        recent.sort((a, b) -> b.itemId - a.itemId);

        if (recent.isEmpty()) {
            JLabel empty = new JLabel("No open items yet.");
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            recentItemsPanel.add(empty);
        }

        int shown = 0;
        for (Item item : recent) {
            if (shown == 8) break;   // overview only: show the 8 newest
            boolean lost = item instanceof LostItem;

            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
            row.setBackground(Color.WHITE);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);

         JLabel tag = new JLabel(lost ? "LOST" : "FOUND");
            tag.setFont(new Font("SansSerif", Font.BOLD, 11));
            tag.setForeground(lost ? Color.RED : new Color(0, 102, 0));

            row.add(tag);
            row.add(new JLabel(item.itemName));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));

            recentItemsPanel.add(row);
            shown++;
        }

        recentItemsPanel.revalidate();
        recentItemsPanel.repaint();
    }
    public void updateCounts() {

        lostCountLabel.setText("COUNT: " + itemManager.openLostCount());
        foundCountLabel.setText("COUNT: " + itemManager.openFoundCount());
        claimedCountLabel.setText("COUNT: " + itemManager.getClaimedItems().size());
        refreshRecentItems();
    }
}