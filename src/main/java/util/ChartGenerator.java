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
                // basic validation
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

                double max = values.get(0);
                for (double v : values) if (v > max) max = v;
                if (max == 0) max = 1;

                int marginTop = 30, marginBottom = 40, marginLeft = 60;
                int usableH = h - marginTop - marginBottom, usableW = w - marginLeft - 20;
                int barW = Math.max(10, usableW / (values.size() * 2));

                for (int i = 0; i < values.size(); i++) {
                    double val = values.get(i);
                    int barH = (int) ((val / max) * usableH);
                    int x = marginLeft + i * (barW * 2);
                    int y = marginTop + (usableH - barH);
                    g2.setColor(new Color(100,149,237));
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

    public static JPanel createLineChartPanel(final String title, final List<String> labels, final List<Double> values) {
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

                double max = values.get(0);
                for (double v : values) if (v > max) max = v;
                if (max == 0) max = 1;

                int marginTop = 30, marginBottom = 40, marginLeft = 60;
                int usableH = h - marginTop - marginBottom, usableW = w - marginLeft - 20;
                int points = values.size();
                int gap = (points == 1) ? usableW / 2 : usableW / (points - 1);

                int prevX = -1, prevY = -1;
                g2.setColor(new Color(34,139,34));
                for (int i = 0; i < values.size(); i++) {
                    double val = values.get(i);
                    int x = marginLeft + i * gap;
                    int y = marginTop + (int) (usableH - (val / max) * usableH);
                    g2.fillOval(x - 3, y - 3, 6, 6);
                    if (prevX != -1) g2.drawLine(prevX, prevY, x, y);
                    prevX = x; prevY = y;

                    g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    String lbl = labels.get(i);
                    g2.drawString(lbl, x - g2.getFontMetrics().stringWidth(lbl)/2, marginTop + usableH + 14);
                }
            }
        };
    }

    public static JPanel createPieChartPanel(final String title, final List<String> labels, final List<Double> values) {
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

                double sum = 0;
                for (Double v : values) sum += v;
                if (sum <= 0) { g2.drawString("No numeric data", 10, 40); return; }

                int diameter = Math.min(w, h) - 120;
                int x = 40;
                int y = (h / 2) - diameter / 2;
                double start = 0.0;
                for (int i = 0; i < values.size(); i++) {
                    double portion = values.get(i) / sum;
                    double angle = portion * 360.0;
                    g2.setColor(getColor(i));
                    g2.fillArc(x, y, diameter, diameter, (int)Math.round(start), (int)Math.round(angle));
                    start += angle;
                }
                int legendX = x + diameter + 20;
                int legendY = 40;
                for (int i = 0; i < labels.size(); i++) {
                    g2.setColor(getColor(i));
                    g2.fillRect(legendX, legendY + i*20, 12, 12);
                    g2.setColor(Color.BLACK);
                    g2.drawString(labels.get(i) + " (" + String.format("%.0f", values.get(i)) + ")", legendX + 16, legendY + i*20 + 11);
                }
            }
        };
    }

    private static Color getColor(int i) {
        Color[] palette = new Color[] {
            new Color(100,149,237), new Color(240,128,128), new Color(144,238,144),
            new Color(255,215,102), new Color(216,191,216), new Color(135,206,250)
        };
        return palette[i % palette.length];
    }
}
