package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

/**
 * ChartGenerator - provides lightweight Swing JPanel charts (bar/line/pie).
 * No external dependencies.
 */
public class ChartGenerator {

    public static JPanel createBarChartPanel(final String title, final List<String> labels, final List<Double> values) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (labels == null || values == null || labels.isEmpty() || values.isEmpty()) {
                    g.drawString("No data available", 10, 20);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, w, h);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString(title, 10, 18);

                double max = values.stream().mapToDouble(v -> v).max().orElse(1);

                int marginTop = 30, marginBottom = 40, marginLeft = 60;
                int usableH = h - marginTop - marginBottom, usableW = w - marginLeft - 20;
                int barW = Math.max(10, usableW / (values.size() * 2));

                for (int i = 0; i < values.size(); i++) {
                    double val = values.get(i);
                    int barH = (int) ((val / max) * usableH);
                    int x = marginLeft + i * (barW * 2);
                    int y = marginTop + (usableH - barH);

                    g2.setColor(new Color(100, 149, 237));
                    g2.fillRect(x, y, barW, barH);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x, y, barW, barH);

                    g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    String lbl = labels.get(i);
                    int lblW = g2.getFontMetrics().stringWidth(lbl);
                    g2.drawString(lbl, x + (barW - lblW) / 2, marginTop + usableH + 14);

                    String vstr = String.format("%.1f", val);
                    int vsW = g2.getFontMetrics().stringWidth(vstr);
                    g2.drawString(vstr, x + (barW - vsW) / 2, y - 6);
                }
            }
        };
    }

}
