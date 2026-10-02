import javax.swing.*;
import java.awt.*;

public class ClaimedItem extends JPanel {

    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private JLabel profileLabel;

    private JPanel topPanel;
    private JPanel titlePanel;
    private JPanel profilePanel;
    private JPanel claimedPanel;

    private ItemManager itemManager;

    public ClaimedItem(ItemManager itemManager) {

        this.itemManager = itemManager;

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

        profileLabel = new JLabel("Dhyay");

        try {

            ImageIcon icon = new ImageIcon(
                    getClass().getResource(
                            "/gui/Image/Profile.png"
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
                claimedPanel,
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

    private void profileClicked() {

        System.out.println(
                "Profile clicked"
        );

        // Add profile page logic later
    }
}