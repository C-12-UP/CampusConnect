import java.awt.*;
import javax.swing.*;

public class ClaimedItem extends JPanel {

    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private JLabel profileLabel;

    private JPanel topPanel;
    private JPanel titlePanel;
    private JPanel profilePanel;
    private JPanel claimedPanel;

    private ItemManager itemManager;
    private String username;

    public ClaimedItem(ItemManager itemManager, String username) {

        this.itemManager = itemManager;
        this.username = username;

        // Main panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        // =========================
        // TOP PANEL
        // =========================

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 231, 233));
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );

        // =========================
        // TITLE
        // =========================

        titlePanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        titlePanel.setBackground(
                new Color(255, 231, 233)
        );

        titleLabel = new JLabel("Claimed Items");

        titleLabel.setFont(
                new Font("Arial Black", Font.BOLD, 14)
        );

        titlePanel.add(titleLabel);

        // =========================
        // PROFILE
        // =========================

        profilePanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        profilePanel.setBackground(Color.ORANGE);

        profileLabel = new JLabel(username);

        try {

            ImageIcon icon = new ImageIcon(
                    getClass().getResource(
                            "/Image/Profile.png"
                    )
            );

            Image image = icon.getImage()
                    .getScaledInstance(
                            25,
                            25,
                            Image.SCALE_SMOOTH
                    );

            profileLabel.setIcon(
                    new ImageIcon(image)
            );

        } catch (Exception e) {

            System.out.println(
                    "Profile image not found."
            );
        }

        profileLabel.setFont(
                new Font(
                        "Vivaldi",
                        Font.BOLD,
                        14
                )
        );

        profilePanel.add(profileLabel);

        // Add title and profile
        topPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        topPanel.add(
                profilePanel,
                BorderLayout.EAST
        );

        // =========================
        // DESCRIPTION
        // =========================

        descriptionLabel = new JLabel(
                "Items that have been successfully claimed by their owners"
        );

        descriptionLabel.setFont(
                new Font(
                        "Yu Gothic UI Semibold",
                        Font.PLAIN,
                        10
                )
        );

        JPanel descriptionPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        descriptionPanel.setBackground(
                new Color(255, 231, 233)
        );

        descriptionPanel.add(
                descriptionLabel
        );

        // =========================
        // CLAIMED ITEMS AREA
        // =========================

        claimedPanel = new JPanel();
        claimedPanel.setLayout(new BoxLayout(claimedPanel, BoxLayout.Y_AXIS));
        claimedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        claimedPanel.setBackground(
                new Color(255, 231, 233)
        );

        // Later you can add claimed item cards here.

        // =========================
        // MAIN CONTENT
        // =========================

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setBackground(
                new Color(255, 231, 233)
        );

        header.add(
                topPanel,
                BorderLayout.NORTH
        );

        header.add(
                descriptionPanel,
                BorderLayout.SOUTH
        );

        add(
                header,
                BorderLayout.NORTH
        );

        add(
                new JScrollPane(claimedPanel),
                BorderLayout.CENTER
        );

        // =========================
        // PROFILE CLICK
        // =========================

        profileLabel.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent evt
                    ) {

                        profileClicked();
                    }
                }
        );
    }
    public void displayClaimed() {
    claimedPanel.removeAll();

    if (itemManager != null) {
        for (Item item : itemManager.getClaimedItems()) {
            claimedPanel.add(createClaimedCard(item));
            claimedPanel.add(Box.createVerticalStrut(10));
        }
    }

        claimedPanel.revalidate();
        claimedPanel.repaint();
        }

private JPanel createClaimedCard(Item item) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(new JLabel("Item: " + item.itemName + "  (ID " + item.itemId + ")"));
        card.add(new JLabel("Type: " + (item instanceof LostItem ? "LOST" : "FOUND")));
        card.add(new JLabel("Claimed by: " + item.claimedByName));
        card.add(new JLabel("Contact: " + item.claimedByContact));
        card.add(new JLabel("Claimed on: " + new java.text.SimpleDateFormat("dd-MM-yyyy").format(item.claimedDate)));

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        return card;
}

    private void profileClicked() {

        System.out.println(
                "Profile clicked"
        );


    }
}