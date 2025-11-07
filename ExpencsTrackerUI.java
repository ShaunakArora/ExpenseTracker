import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * A panel to display user profile information.
 */
public class ProfilePanel extends JPanel {

    // Re-using colors and fonts for consistency
    private final Color backgroundDark = new Color(30, 30, 30);
    private final Color panelLight = new Color(45, 45, 45);
    private final Color textLight = new Color(224, 224, 224);
    private final Color textMuted = new Color(158, 158, 158);
    private final Color primaryRed = new Color(229, 57, 53);
    private final Font nameFont = new Font("SansSerif", Font.BOLD, 24);
    private final Font detailFont = new Font("SansSerif", Font.PLAIN, 14);
    private final Font valueFont = new Font("SansSerif", Font.PLAIN, 16);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

    public ProfilePanel() {
        setBackground(backgroundDark);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(30, 50, 30, 50));

        // Main container panel with a rounded border and a subtle shadow
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(panelLight);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // --- Profile Picture Panel ---
        mainPanel.add(new ProfilePicturePanel(), BorderLayout.WEST);

        // --- Details Panel ---
        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        // Name
        JLabel nameLabel = new JLabel("Demo User");
        nameLabel.setFont(nameFont);
        nameLabel.setForeground(textLight);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(nameLabel);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Email
        detailsPanel.add(createDetailItem("\u2709", "demo.user@example.com")); // Email icon
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Member Since
        detailsPanel.add(createDetailItem("\uD83D\uDCC5", "Member Since: January 2024")); // Calendar icon

        detailsPanel.add(Box.createVerticalGlue()); // Pushes button to the bottom

        // --- Edit Button ---
        JButton editButton = new JButton("Edit Profile");
        editButton.setBackground(primaryRed);
        editButton.setForeground(Color.BLACK);
        editButton.setFont(buttonFont);
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Edit profile functionality is not yet implemented.", "In Progress", JOptionPane.INFORMATION_MESSAGE));
        detailsPanel.add(editButton);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(mainPanel, gbc);
    }

    private JPanel createDetailItem(String icon, String text) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(textMuted);
        iconLabel.setFont(detailFont);
        panel.add(iconLabel, BorderLayout.WEST);

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(textMuted);
        textLabel.setFont(detailFont);
        panel.add(textLabel, BorderLayout.CENTER);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    /**
     * A custom panel to draw a circular profile picture placeholder.
     */
    private class ProfilePicturePanel extends JPanel {
        ProfilePicturePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(120, 120));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw circle background
            g2d.setColor(new Color(60, 60, 60));
            g2d.fill(new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));

            // Draw user icon (head and shoulders)
            g2d.setColor(textMuted);
            int headDiameter = getWidth() / 3;
            int headX = (getWidth() - headDiameter) / 2;
            int headY = getHeight() / 4;
            g2d.fill(new Ellipse2D.Double(headX, headY, headDiameter, headDiameter));

            int shoulderWidth = getWidth() * 2 / 3;
            int shoulderHeight = getHeight() / 3;
            int shoulderX = (getWidth() - shoulderWidth) / 2;
            int shoulderY = headY + headDiameter - 5;
            g2d.fill(new Ellipse2D.Double(shoulderX, shoulderY, shoulderWidth, shoulderHeight));

            g2d.dispose();
        }
    }
}
