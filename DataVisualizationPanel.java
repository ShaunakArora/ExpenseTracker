import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A panel dedicated to visualizing financial data, such as an expense pie chart.
 */
public class DataVisualizationPanel extends JPanel {

    // Re-using colors and fonts for consistency
    private final Color backgroundDark = new Color(30, 30, 30);
    private final Color textLight = new Color(224, 224, 224);
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 18);
    private final Font legendFont = new Font("SansSerif", Font.PLAIN, 14);

    // A list of colors for the pie chart slices
    private final Color[] pieChartColors = {
            new Color(229, 57, 53), new Color(26, 152, 219), new Color(241, 196, 15),
            new Color(46, 204, 113), new Color(155, 89, 182), new Color(230, 126, 34),
            new Color(52, 73, 94), new Color(149, 165, 166)
    };

    private List<ExpenseTrackerUI.Transaction> transactions;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("en", "IN"));

    public DataVisualizationPanel(List<ExpenseTrackerUI.Transaction> transactions) {
        this.transactions = transactions;
        setBackground(backgroundDark);
    }

    /**
     * Updates the data used for visualization and repaints the panel.
     * @param transactions The new list of transactions.
     */
    public void updateData(List<ExpenseTrackerUI.Transaction> transactions) {
        this.transactions = transactions;
        repaint(); // Trigger a repaint to show the new data
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the title
        g2d.setColor(textLight);
        g2d.setFont(titleFont);
        g2d.drawString("Expense Breakdown by Category", 20, 30);

        drawExpensePieChart(g2d);
    }

    private void drawExpensePieChart(Graphics2D g2d) {
        // 1. Aggregate expense data by category
        Map<String, Double> expensesByCategory = transactions.stream()
                .filter(t -> t.type() == ExpenseTrackerUI.TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        t -> t.category(),
                        Collectors.summingDouble(t -> t.amount())
                ));

        if (expensesByCategory.isEmpty()) {
            g2d.setColor(textLight);
            g2d.setFont(legendFont);
            g2d.drawString("No expense data to display.", getWidth() / 2 - 100, getHeight() / 2);
            return;
        }

        double totalExpense = expensesByCategory.values().stream().mapToDouble(Double::doubleValue).sum();

        // 2. Define chart geometry
        int chartX = 50;
        int chartY = 80;
        int chartDiameter = Math.min(getWidth() / 2, getHeight() - 100);
        int legendX = chartX + chartDiameter + 50;
        int legendY = chartY;

        // 3. Draw pie slices
        double startAngle = 0.0;
        int colorIndex = 0;

        // Sort categories for consistent coloring
        List<String> sortedCategories = expensesByCategory.keySet().stream().sorted().toList();

        for (String category : sortedCategories) {
            double amount = expensesByCategory.get(category);
            double arcAngle = (amount / totalExpense) * 360.0;

            // Set the color for the current slice
            g2d.setColor(pieChartColors[colorIndex % pieChartColors.length]);

            // Draw the pie slice
            g2d.fillArc(chartX, chartY, chartDiameter, chartDiameter, (int) startAngle, (int) Math.ceil(arcAngle));

            // --- Draw the legend for this slice ---
            // 1. Draw the small color rectangle
            g2d.fillRect(legendX, legendY + (colorIndex * 25), 15, 15);

            // 2. Draw the legend text (Category, Percentage, Amount)
            g2d.setColor(textLight);
            g2d.setFont(legendFont);
            double percentage = (amount / totalExpense) * 100;
            String legendText = String.format("%s (%.1f%%) - %s", category, percentage, currencyFormat.format(amount));
            g2d.drawString(legendText, legendX + 25, legendY + (colorIndex * 25) + 13);

            startAngle += arcAngle;
            colorIndex++;
        }
    }
}
