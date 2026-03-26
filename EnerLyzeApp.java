import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * EnerLyzeApp.java  —  Multi-Screen CardLayout Edition (Enhanced UI)
 * ======================================================
 * Smart Daily Energy Analyzer using Java Swing.
 * Screens: Home  |  Input  |  Results  |  History
 *
 * HOW TO COMPILE & RUN:
 *   javac *.java
 *   java EnerLyzeApp
 */
public class EnerLyzeApp extends JFrame {

    // ── Screen Keys ──────────────────────────────────────────────────────────
    private static final String SCREEN_HOME    = "HOME";
    private static final String SCREEN_INPUT   = "INPUT";
    private static final String SCREEN_RESULT  = "RESULT";
    private static final String SCREEN_HISTORY = "HISTORY";

    // ── Colour Palette (Enhanced) ────────────────────────────────────────────
    private static final Color BG_MAIN          = new Color(15, 18, 30);      // deep space navy
    private static final Color BG_SURFACE       = new Color(22, 27, 46);      // card surface
    private static final Color BG_CARD          = new Color(28, 34, 56);      // card bg
    private static final Color BG_INPUT         = new Color(20, 25, 42);      // input bg
    private static final Color BG_SIDEBAR       = new Color(10, 13, 26);      // darker sidebar
    private static final Color BG_SIDEBAR_HOV   = new Color(32, 40, 72);
    private static final Color BG_SIDEBAR_SEL   = new Color(99, 102, 241);    // indigo
    private static final Color ACCENT           = new Color(99,  102, 241);   // indigo-500
    private static final Color ACCENT_HOVER     = new Color(79,  82,  221);
    private static final Color ACCENT_LIGHT     = new Color(30,  35,  70);
    private static final Color ACCENT_GLOW      = new Color(99,  102, 241, 40);
    private static final Color SUCCESS          = new Color(16,  185, 129);   // emerald
    private static final Color SUCCESS_BG       = new Color(16,  185, 129, 25);
    private static final Color SUCCESS_HOVER    = new Color(5,   160, 110);
    private static final Color SUCCESS_GLOW     = new Color(16,  185, 129, 40);
    private static final Color WARNING          = new Color(245, 158,  11);   // amber
    private static final Color WARNING_LIGHT    = new Color(245, 158,  11, 25);
    private static final Color DANGER           = new Color(239,  68,  68);   // red
    private static final Color DANGER_LIGHT     = new Color(239,  68,  68, 25);
    private static final Color PURPLE           = new Color(168, 85,  247);   // purple-500
    private static final Color CYAN             = new Color(6,   182, 212);   // cyan-500
    private static final Color PINK             = new Color(236, 72,  153);   // pink-500
    private static final Color TEXT_PRIMARY     = new Color(241, 245, 249);   // slate-100
    private static final Color TEXT_SECONDARY   = new Color(148, 163, 184);   // slate-400
    private static final Color TEXT_MUTED       = new Color(100, 116, 139);   // slate-500
    private static final Color TEXT_HINT        = new Color(71,  85,  105);   // slate-600
    private static final Color TEXT_SIDEBAR     = new Color(148, 163, 184);
    private static final Color BORDER_LIGHT     = new Color(51,  65,  85);    // slate-700
    private static final Color BORDER_FOCUS     = new Color(99,  102, 241, 180);
    private static final Color DIVIDER          = new Color(30,  40,  65);

    // ── Fonts ────────────────────────────────────────────────────────────────
    private static final Font FONT_HERO       = new Font("SansSerif", Font.BOLD,  52);
    private static final Font FONT_SECTION    = new Font("SansSerif", Font.BOLD,  22);
    private static final Font FONT_CARD_TITLE = new Font("SansSerif", Font.BOLD,  11);
    private static final Font FONT_LABEL      = new Font("SansSerif", Font.BOLD,  13);
    private static final Font FONT_INPUT_FLD  = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font FONT_BUTTON     = new Font("SansSerif", Font.BOLD,  13);
    private static final Font FONT_RESULT     = new Font("Monospaced", Font.PLAIN, 13);
    private static final Font FONT_SCORE      = new Font("SansSerif", Font.BOLD,  72);
    private static final Font FONT_STATUS     = new Font("SansSerif", Font.BOLD,  18);
    private static final Font FONT_META       = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BADGE      = new Font("SansSerif", Font.BOLD,  11);
    private static final Font FONT_NAV        = new Font("SansSerif", Font.BOLD,  13);
    private static final Font FONT_TAGLINE    = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_STAT_VAL   = new Font("SansSerif", Font.BOLD,  20);
    private static final Font FONT_STAT_LBL   = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_HINT       = new Font("SansSerif", Font.PLAIN, 11);

    // ── CardLayout routing ───────────────────────────────────────────────────
    private CardLayout cardLayout;
    private JPanel     cardPanel;

    // ── Sidebar nav buttons ───────────────────────────────────────────────────
    private JButton[] navButtons = new JButton[4];

    // ── Input Fields ─────────────────────────────────────────────────────────
    private JTextField sleepField;
    private JTextField workField;
    private JTextField screenField;

    // ── Output Widgets ────────────────────────────────────────────────────────
    private JLabel    scoreLabel;
    private JLabel    statusLabel;
    private JLabel    compareLabel;
    private JLabel    avgLabel;
    private JTextArea historyArea;
    private JTextArea suggestionArea;
    private JPanel    scoreBgPanel;
    private JPanel    scoreRingPanel;

    // ── Data ─────────────────────────────────────────────────────────────────
    private ArrayList<EnergyRecord> records = new ArrayList<>();
    private int lastScore = -1;

    // ═════════════════════════════════════════════════════════════════════════
    //  Constructor
    // ═════════════════════════════════════════════════════════════════════════
    public EnerLyzeApp() {
        setTitle("EnerLyze – Smart Daily Energy Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1060, 720);
        setMinimumSize(new Dimension(880, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout(0, 0));

        records = FileManager.loadRecords();

        add(buildSidebar(),  BorderLayout.WEST);
        add(buildCardArea(), BorderLayout.CENTER);

        setVisible(true);
    }

  // ═════════════════════════════════════════════════════════════════════════
    //  SIDEBAR  — deep navy vertical nav strip
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(300, 0));
 
        // ── Brand block (fully centred) ───────────────────────────────────────
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(new EmptyBorder(44, 20, 36, 20));
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
 
        // Row 1 — ⚡ EnerLyze
        JLabel logoLbl = new JLabel("⚡ EnerLyze");
        logoLbl.setFont(new Font("SansSerif", Font.BOLD, 30));
        logoLbl.setForeground(Color.WHITE);
        logoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLbl.setHorizontalAlignment(SwingConstants.CENTER);
 
        // Row 2 — Smart Energy Analyzer
        JLabel tagLbl = new JLabel("Smart Energy Analyzer");
        tagLbl.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tagLbl.setForeground(new Color(148, 163, 200));
        tagLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagLbl.setHorizontalAlignment(SwingConstants.CENTER);
        tagLbl.setBorder(new EmptyBorder(10, 0, 0, 0));
 
        // Row 3 — 🔔 Notifications chip
        JPanel notifWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        notifWrap.setOpaque(false);
        notifWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        notifWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        notifWrap.setBorder(new EmptyBorder(14, 0, 0, 0));
 
        JLabel notifChip = new JLabel("🔔  Notifications") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 58, 120));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        notifChip.setFont(new Font("SansSerif", Font.PLAIN, 14));
        notifChip.setForeground(new Color(203, 213, 225));
        notifChip.setOpaque(false);
        notifChip.setHorizontalAlignment(SwingConstants.CENTER);
        notifChip.setBorder(new EmptyBorder(8, 22, 8, 22));
        notifWrap.add(notifChip);
 
        brand.add(logoLbl);
        brand.add(tagLbl);
        brand.add(notifWrap);
        sidebar.add(brand);
 
        // ── Divider ───────────────────────────────────────────────────────────
        JSeparator divider = new JSeparator();
        divider.setForeground(new Color(40, 58, 120));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(divider);
        sidebar.add(Box.createVerticalStrut(18));
 
        // ── Nav items ─────────────────────────────────────────────────────────
        String[][] items = {
            { "🏠", "Home",    SCREEN_HOME,    "0" },
            { "📝", "Input",   SCREEN_INPUT,   "1" },
            { "🎯", "Results", SCREEN_RESULT,  "2" },
            { "📜", "History", SCREEN_HISTORY, "3" }
        };
 
        for (String[] item : items) {
            int idx = Integer.parseInt(item[3]);
            navButtons[idx] = buildNavButton(item[0], item[1], item[2], idx);
            JPanel wrap = new JPanel(new BorderLayout());
            wrap.setOpaque(false);
            wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
            wrap.add(navButtons[idx]);
            sidebar.add(wrap);
            sidebar.add(Box.createVerticalStrut(6));
        }
 
        // No footer — sidebar ends here
        sidebar.add(Box.createVerticalGlue());
 
        return sidebar;
    }
 
    private JButton buildNavButton(String icon, String label, String key, int idx) {
        JButton btn = new JButton("<html><span style='font-size:16px'>"
                                 + icon + "</span>&nbsp;&nbsp;" + label + "</html>") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getBackground().equals(BG_SIDEBAR_SEL)) {
                    g2.setColor(BG_SIDEBAR_SEL);
                    g2.fillRoundRect(10, 4, getWidth() - 20, getHeight() - 8, 12, 12);
                } else if (getModel().isRollover()) {
                    g2.setColor(BG_SIDEBAR_HOV);
                    g2.fillRoundRect(10, 4, getWidth() - 20, getHeight() - 8, 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setForeground(TEXT_SIDEBAR);
        btn.setBackground(BG_SIDEBAR);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(14, 28, 14, 24));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> navigateTo(key, idx));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { btn.repaint(); }
        });
        return btn;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  CARD AREA
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildCardArea() {
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(BG_MAIN);

        cardPanel.add(buildHomeScreen(),    SCREEN_HOME);
        cardPanel.add(buildInputScreen(),   SCREEN_INPUT);
        cardPanel.add(buildResultScreen(),  SCREEN_RESULT);
        cardPanel.add(buildHistoryScreen(), SCREEN_HISTORY);

        cardLayout.show(cardPanel, SCREEN_HOME);
        setNavActive(0);
        return cardPanel;
    }

    private void navigateTo(String key, int navIdx) {
        cardLayout.show(cardPanel, key);
        setNavActive(navIdx);
    }

    private void setNavActive(int active) {
        for (int i = 0; i < navButtons.length; i++) {
            boolean sel = (i == active);
            navButtons[i].setBackground(sel ? BG_SIDEBAR_SEL : BG_SIDEBAR);
            navButtons[i].setForeground(sel ? Color.WHITE     : TEXT_SIDEBAR);
            navButtons[i].repaint();
        }
    }

 // ═════════════════════════════════════════════════════════════════════════
    //  SCREEN 1 — HOME
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildHomeScreen() {
        JPanel screen = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Radial glow in top-center
                RadialGradientPaint rgp = new RadialGradientPaint(
                    new Point2D.Float(getWidth() / 2f, 80),
                    300,
                    new float[]{0f, 1f},
                    new Color[]{new Color(99, 102, 241, 40), new Color(99, 102, 241, 0)}
                );
                g2.setPaint(rgp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bottom-right secondary glow
                RadialGradientPaint rgp2 = new RadialGradientPaint(
                    new Point2D.Float(getWidth() * 0.8f, getHeight() * 0.7f),
                    200,
                    new float[]{0f, 1f},
                    new Color[]{new Color(168, 85, 247, 25), new Color(168, 85, 247, 0)}
                );
                g2.setPaint(rgp2);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        screen.setBackground(BG_MAIN);

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBorder(new EmptyBorder(10, 0, 20, 0));

        // Hero logo — matches sidebar brand style
        JLabel heroTitle = new JLabel("⚡ EnerLyze");
        heroTitle.setFont(new Font("SansSerif", Font.BOLD, 52));
        heroTitle.setForeground(TEXT_PRIMARY);
        heroTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        heroTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel heroSub = new JLabel("Your Smart Daily Energy Analyzer");
        heroSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        heroSub.setForeground(TEXT_SECONDARY);
        heroSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Feature chips
        JPanel chips = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        chips.setOpaque(false);
        chips.setAlignmentX(Component.CENTER_ALIGNMENT);
        chips.add(makeChip("🛌  Sleep Tracking", SUCCESS,   new Color(16, 185, 129, 30)));
        chips.add(makeChip("💼  Work Balance",   ACCENT,    new Color(99, 102, 241, 30)));
        chips.add(makeChip("📱  Screen Control", WARNING,   new Color(245, 158, 11, 30)));

        // CTA button (glowing)
        JButton startBtn = createGlowButton("  ⚡  Get Started  →", ACCENT, new Color(79, 82, 221));
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setMaximumSize(new Dimension(220, 52));
        startBtn.addActionListener(e -> navigateTo(SCREEN_INPUT, 1));

        // Three stat cards row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(620, 100));
        statsRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsRow.add(buildStatCard("🛌", "Ideal Sleep", "7 – 9 hrs", SUCCESS, new Color(16, 185, 129, 30)));
        statsRow.add(buildStatCard("💼", "Work Limit",  "≤ 10 hrs",  WARNING, new Color(245, 158, 11, 30)));
        statsRow.add(buildStatCard("📱", "Screen Cap",  "≤ 4 hrs",   DANGER,  new Color(239, 68, 68, 30)));

        // How it works row
        JPanel howRow = new JPanel(new GridLayout(1, 3, 16, 0));
        howRow.setOpaque(false);
        howRow.setMaximumSize(new Dimension(620, 70));
        howRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        howRow.add(buildStepChip("① Enter", "Daily metrics", CYAN));
        howRow.add(buildStepChip("② Analyze", "Score & tips", PURPLE));
        howRow.add(buildStepChip("③ Improve", "Track progress", PINK));

        col.add(heroTitle);
        col.add(Box.createVerticalStrut(10));
        col.add(heroSub);
        col.add(Box.createVerticalStrut(20));
        col.add(chips);
        col.add(Box.createVerticalStrut(24));
        col.add(startBtn);
        col.add(Box.createVerticalStrut(30));
        col.add(statsRow);
        col.add(Box.createVerticalStrut(14));
        col.add(howRow);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(col);
        screen.add(center, BorderLayout.CENTER);
        return screen;
    }

    private JPanel buildStepChip(String step, String desc, Color color) {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 15));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel stepLbl = new JLabel(step);
        stepLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        stepLbl.setForeground(color);
        stepLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        descLbl.setForeground(TEXT_MUTED);
        descLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(stepLbl);
        panel.add(Box.createVerticalStrut(3));
        panel.add(descLbl);
        return panel;
    }

    private JPanel buildStatCard(String icon, String title, String value, Color accent, Color bgColor) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                // Top accent bar
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth() - 1, 3, 3, 3);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 16));

        JLabel iconLbl = new JLabel(icon + "  " + title);
        iconLbl.setFont(FONT_STAT_LBL);
        iconLbl.setForeground(TEXT_SECONDARY);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(FONT_STAT_VAL);
        valLbl.setForeground(accent);

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(valLbl);
        return card;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SCREEN 2 — INPUT
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildInputScreen() {
        JPanel screen = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                RadialGradientPaint rg = new RadialGradientPaint(
                    new Point2D.Float(getWidth() * 0.5f, getHeight() * 0.4f), 350,
                    new float[]{0f, 1f},
                    new Color[]{new Color(99, 102, 241, 20), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(rg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        screen.setBackground(BG_MAIN);
        screen.add(buildScreenHeader("✏️  Enter Today's Data",
            "Fill in your daily health metrics to receive your personalized energy score"), BorderLayout.NORTH);

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel fieldsCard = buildInputFieldsCard();
        fieldsCard.setMaximumSize(new Dimension(520, 320));
        fieldsCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tips strip
        JPanel tipsStrip = buildTipsStrip();
        tipsStrip.setMaximumSize(new Dimension(520, 50));
        tipsStrip.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btns = new JPanel(new GridLayout(1, 2, 12, 0));
        btns.setOpaque(false);
        btns.setMaximumSize(new Dimension(520, 50));
        btns.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton analyzeBtn = createGlowButton("⚡  Analyze Now", ACCENT, new Color(79, 82, 221));
        JButton saveBtn    = createGlowButton("💾  Save Record", SUCCESS, SUCCESS_HOVER);
        analyzeBtn.addActionListener(e -> onAnalyze());
        saveBtn.addActionListener(e -> onSave());
        btns.add(analyzeBtn);
        btns.add(saveBtn);

        col.add(fieldsCard);
        col.add(Box.createVerticalStrut(12));
        col.add(tipsStrip);
        col.add(Box.createVerticalStrut(14));
        col.add(btns);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(col);

        screen.add(wrapper,             BorderLayout.CENTER);
        screen.add(buildScreenFooter(), BorderLayout.SOUTH);
        return screen;
    }

    private JPanel buildTipsStrip() {
        JPanel strip = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 158, 11, 15));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(new Color(245, 158, 11, 50));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        strip.setOpaque(false);
        strip.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        JLabel tip = new JLabel("💡  Tip: Enter values in decimal hours (e.g. 7.5 = 7h 30m). All fields are required.");
        tip.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tip.setForeground(WARNING);
        strip.add(tip);
        return strip;
    }

    private JPanel buildInputFieldsCard() {
        JPanel card = makeCard("📋  DAILY HEALTH INPUTS");

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        sleepField  = createStyledField("e.g.  7.5  (hours)");
        workField   = createStyledField("e.g.  8.0  (hours)");
        screenField = createStyledField("e.g.  4.0  (hours)");

        g.insets = new Insets(0, 0, 5, 0);   g.gridy = 0;
        fields.add(buildLabelRow("🛌", "Sleep Hours", "Recommended: 7–9 hrs", SUCCESS), g);
        g.insets = new Insets(0, 0, 16, 0);  g.gridy = 1;
        fields.add(sleepField, g);

        g.insets = new Insets(0, 0, 5, 0);   g.gridy = 2;
        fields.add(buildLabelRow("💼", "Work Hours", "Limit: ≤ 10 hrs", WARNING), g);
        g.insets = new Insets(0, 0, 16, 0);  g.gridy = 3;
        fields.add(workField, g);

        g.insets = new Insets(0, 0, 5, 0);   g.gridy = 4;
        fields.add(buildLabelRow("📱", "Screen Time", "Limit: ≤ 4 hrs", DANGER), g);
        g.insets = new Insets(0, 0, 0, 0);   g.gridy = 5;
        fields.add(screenField, g);

        card.add(fields, BorderLayout.CENTER);
        return card;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SCREEN 3 — RESULT
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildResultScreen() {
        JPanel screen = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                RadialGradientPaint rg = new RadialGradientPaint(
                    new Point2D.Float(getWidth() * 0.25f, getHeight() * 0.4f), 300,
                    new float[]{0f, 1f},
                    new Color[]{new Color(16, 185, 129, 20), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(rg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        screen.setBackground(BG_MAIN);
        screen.add(buildScreenHeader("🎯  Your Energy Result",
            "Personalized analysis based on today's health data"), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 20, 0));
        body.setBackground(BG_MAIN);
        body.setBorder(new EmptyBorder(24, 28, 24, 28));
        body.add(buildScoreColumn());
        body.add(buildSuggestionsColumn());

        screen.add(body,                BorderLayout.CENTER);
        screen.add(buildScreenFooter(), BorderLayout.SOUTH);
        return screen;
    }

    private JPanel buildScoreColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        // Main score card
        scoreBgPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Card shadow
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(3, 5, getWidth() - 4, getHeight() - 4, 16, 16);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                g2.setColor(BORDER_LIGHT);
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                // Score glow if available
                if (lastScore >= 0) {
                    Color glowColor = getScoreGlowColor(lastScore);
                    RadialGradientPaint rg = new RadialGradientPaint(
                        new Point2D.Float((getWidth() - 3) / 2f, (getHeight() - 4) * 0.42f), 90,
                        new float[]{0f, 1f},
                        new Color[]{glowColor, new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 0)}
                    );
                    g2.setPaint(rg);
                    g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                }
                g2.dispose();
            }
        };
        scoreBgPanel.setOpaque(false);
        scoreBgPanel.setBackground(BG_CARD);
        scoreBgPanel.setLayout(new BoxLayout(scoreBgPanel, BoxLayout.Y_AXIS));
        scoreBgPanel.setBorder(new EmptyBorder(30, 28, 28, 28));

        JLabel secLbl = new JLabel("ENERGY SCORE");
        secLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        secLbl.setForeground(TEXT_HINT);
        secLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("—");
        scoreLabel.setFont(FONT_SCORE);
        scoreLabel.setForeground(ACCENT);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel outOf = new JLabel("out of 100");
        outOf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        outOf.setForeground(TEXT_HINT);
        outOf.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel divPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int mid = getWidth() / 2;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 0, 0),
                                                     mid, 0, DIVIDER);
                g2.setPaint(gp);
                g2.fillRect(0, 0, mid, getHeight());
                GradientPaint gp2 = new GradientPaint(mid, 0, DIVIDER,
                                                      getWidth(), 0, new Color(0, 0, 0, 0));
                g2.setPaint(gp2);
                g2.fillRect(mid, 0, mid, getHeight());
                g2.dispose();
            }
        };
        divPanel.setOpaque(false);
        divPanel.setPreferredSize(new Dimension(0, 1));
        divPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        statusLabel = new JLabel("Analyze to see your score");
        statusLabel.setFont(FONT_STATUS);
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreBgPanel.add(secLbl);
        scoreBgPanel.add(Box.createVerticalStrut(10));
        scoreBgPanel.add(scoreLabel);
        scoreBgPanel.add(outOf);
        scoreBgPanel.add(Box.createVerticalStrut(16));
        scoreBgPanel.add(divPanel);
        scoreBgPanel.add(Box.createVerticalStrut(14));
        scoreBgPanel.add(statusLabel);

        // Insights card
        JPanel insightCard = makeCard("📊  INSIGHTS & COMPARISON");
        insightCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel insightInner = new JPanel(new GridLayout(2, 1, 0, 8));
        insightInner.setOpaque(false);

        compareLabel = new JLabel("ℹ️  Compare: —");
        compareLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        compareLabel.setForeground(TEXT_MUTED);

        avgLabel = new JLabel("📈  Average Score: —");
        avgLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        avgLabel.setForeground(TEXT_MUTED);

        insightInner.add(compareLabel);
        insightInner.add(avgLabel);
        insightCard.add(insightInner, BorderLayout.CENTER);

        JButton editBtn = new JButton("← Edit Inputs") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER_FOCUS);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        editBtn.setFont(FONT_BUTTON);
        editBtn.setForeground(ACCENT);
        editBtn.setBackground(new Color(99, 102, 241, 20));
        editBtn.setBorder(new EmptyBorder(12, 20, 12, 20));
        editBtn.setFocusPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setOpaque(false);
        editBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        editBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        editBtn.addActionListener(e -> navigateTo(SCREEN_INPUT, 1));
        editBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                editBtn.setBackground(new Color(99, 102, 241, 35));
                editBtn.repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                editBtn.setBackground(new Color(99, 102, 241, 20));
                editBtn.repaint();
            }
        });

        col.add(scoreBgPanel);
        col.add(Box.createVerticalStrut(16));
        col.add(insightCard);
        col.add(Box.createVerticalStrut(14));
        col.add(editBtn);
        return col;
    }

    private JPanel buildSuggestionsColumn() {
        JPanel card = makeCard("💡  PERSONALISED SUGGESTIONS");
        suggestionArea = new JTextArea("⚡  Run an analysis to see your personalized energy optimization suggestions…\n\n"
                                     + "Your recommendations will appear here based on your:\n"
                                     + "  • Sleep hours and quality indicators\n"
                                     + "  • Work-life balance assessment\n"
                                     + "  • Screen time impact analysis");
        styleTextArea(suggestionArea, BG_INPUT);
        card.add(styledScrollPane(suggestionArea), BorderLayout.CENTER);
        return card;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SCREEN 4 — HISTORY
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildHistoryScreen() {
        JPanel screen = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                RadialGradientPaint rg = new RadialGradientPaint(
                    new Point2D.Float(getWidth() * 0.5f, 100), 280,
                    new float[]{0f, 1f},
                    new Color[]{new Color(168, 85, 247, 20), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(rg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        screen.setBackground(BG_MAIN);
        screen.add(buildScreenHeader("📜  History",
            "Track your energy trends over time"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(BG_MAIN);
        body.setBorder(new EmptyBorder(24, 28, 0, 28));

        JPanel histCard = makeCard("📜  SAVED RECORDS");
        historyArea = new JTextArea("Click 'Load History' to see your saved records…\n\n"
                                  + "Your records are stored locally and persist between sessions.");
        styleTextArea(historyArea, BG_INPUT);
        histCard.add(styledScrollPane(historyArea), BorderLayout.CENTER);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnBar.setOpaque(false);
        btnBar.setBorder(new EmptyBorder(14, 0, 24, 0));

        JButton loadBtn = createGlowButton("📂  Load History", ACCENT,   ACCENT_HOVER);
        JButton saveBtn = createGlowButton("💾  Save Current", SUCCESS,  SUCCESS_HOVER);
        loadBtn.addActionListener(e -> onLoadHistory());
        saveBtn.addActionListener(e -> onSave());
        btnBar.add(loadBtn);
        btnBar.add(saveBtn);

        body.add(histCard, BorderLayout.CENTER);
        body.add(btnBar,   BorderLayout.SOUTH);

        screen.add(body,                BorderLayout.CENTER);
        screen.add(buildScreenFooter(), BorderLayout.SOUTH);
        return screen;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SHARED SCREEN CHROME
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildScreenHeader(String title, String sub) {
        JPanel hdr = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                // Bottom border with gradient
                GradientPaint gp = new GradientPaint(0, getHeight() - 1, new Color(99, 102, 241, 60),
                                                     getWidth(), getHeight() - 1, new Color(168, 85, 247, 30));
                g2.setPaint(gp);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        hdr.setBackground(BG_SURFACE);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, DIVIDER),
            new EmptyBorder(22, 30, 22, 30)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_SECTION);
        titleLbl.setForeground(TEXT_PRIMARY);

        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(TEXT_MUTED);
        subLbl.setBorder(new EmptyBorder(5, 0, 0, 0));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(titleLbl);
        left.add(subLbl);

        hdr.add(left, BorderLayout.WEST);
        return hdr;
    }

    private JPanel buildScreenFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(99, 102, 241, 40),
                                                     getWidth(), 0, new Color(168, 85, 247, 20));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), 1);
                g2.dispose();
            }
        };
        footer.setBackground(BG_SURFACE);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, DIVIDER));
        JLabel lbl = new JLabel("⚡ EnerLyze v1.0  ·  Data: " + FileManager.getFilePath() + "  ·  " + records.size() + " records");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(TEXT_HINT);
        footer.add(lbl);
        return footer;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  ACTION HANDLERS (backend logic unchanged)
    // ═════════════════════════════════════════════════════════════════════════
    private void onAnalyze() {
        try {
            double sleepHours = parsePositiveDouble(sleepField.getText(), "Sleep Hours");
            double workHours  = parsePositiveDouble(workField.getText(),  "Work Hours");
            double screenTime = parsePositiveDouble(screenField.getText(), "Screen Time");

            validateRange(sleepHours, "Sleep Hours", 0, 24);
            validateRange(workHours,  "Work Hours",  0, 24);
            validateRange(screenTime, "Screen Time", 0, 24);

            int    score       = EnergyEngine.calculateScore(sleepHours, workHours, screenTime);
            String status      = EnergyEngine.getStatus(score);
            String suggestions = EnergyEngine.getSuggestions(sleepHours, workHours, screenTime, score);
            String comparison  = EnergyEngine.compareWithLast(records, score);
            double avg         = EnergyEngine.calculateAverage(records);

            lastScore = score;

            scoreLabel.setText(String.valueOf(score));
            scoreLabel.setForeground(getScoreColor(score));
            statusLabel.setText(status);
            statusLabel.setForeground(getScoreColor(score));
            scoreBgPanel.setBackground(getScoreBgColor(score));
            scoreBgPanel.repaint();

            suggestionArea.setText(suggestions);
            suggestionArea.setCaretPosition(0);

            compareLabel.setText(comparison);
            compareLabel.setForeground(TEXT_PRIMARY);

            String avgText = records.isEmpty()
                ? "📈  Average Score: — (no saved records yet)"
                : String.format("📈  Average Score: %.1f  (over %d record%s)",
                                avg, records.size(), records.size() == 1 ? "" : "s");
            avgLabel.setText(avgText);
            avgLabel.setForeground(WARNING);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            EnergyRecord record = new EnergyRecord(
                sleepHours, workHours, screenTime, score, status, timestamp);

            String historyLine = record.toString();
            if (historyArea.getText().startsWith("Click Load") || historyArea.getText().startsWith("⚡")) {
                historyArea.setText(historyLine);
            } else {
                historyArea.append("\n" + historyLine);
            }
            historyArea.setCaretPosition(historyArea.getDocument().getLength());

            setFieldBorderState(SUCCESS, ACCENT_LIGHT, false);
            navigateTo(SCREEN_RESULT, 2);

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
            setFieldBorderState(DANGER, DANGER_LIGHT, true);
        }
    }

    private void onSave() {
        try {
            double sleepHours = parsePositiveDouble(sleepField.getText(), "Sleep Hours");
            double workHours  = parsePositiveDouble(workField.getText(),  "Work Hours");
            double screenTime = parsePositiveDouble(screenField.getText(), "Screen Time");

            int    score  = EnergyEngine.calculateScore(sleepHours, workHours, screenTime);
            String status = EnergyEngine.getStatus(score);
            String ts     = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            EnergyRecord record = new EnergyRecord(
                sleepHours, workHours, screenTime, score, status, ts);
            records.add(record);

            FileManager.saveRecords(records);

            double avg = EnergyEngine.calculateAverage(records);
            avgLabel.setText(String.format("📈  Average Score: %.1f  (over %d record%s)",
                             avg, records.size(), records.size() == 1 ? "" : "s"));
            avgLabel.setForeground(WARNING);

            showInfo("✅ Record saved successfully!\n\nFile: " + FileManager.getFilePath()
                   + "\nTotal records: " + records.size());

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("❌ Failed to save: " + ex.getMessage());
        }
    }

    private void onLoadHistory() {
        records = FileManager.loadRecords();

        if (records.isEmpty()) {
            historyArea.setText("📭  No records found in " + FileManager.getFilePath()
                              + "\n\nAnalyze and save your first record to get started!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("  ✅ Loaded ").append(records.size()).append(" record(s) from disk:\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        for (int i = 0; i < records.size(); i++) {
            sb.append(String.format("  #%02d  ", i + 1));
            sb.append(records.get(i).toString());
            sb.append("\n");
        }

        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        double avg = EnergyEngine.calculateAverage(records);
        sb.append(String.format("  📊 Average Score: %.1f  across %d record(s)\n", avg, records.size()));

        historyArea.setText(sb.toString());
        historyArea.setCaretPosition(0);

        avgLabel.setText(String.format("📈  Average Score: %.1f  (over %d record%s)",
                         avg, records.size(), records.size() == 1 ? "" : "s"));
        avgLabel.setForeground(WARNING);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  UI HELPERS
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel makeCard(String sectionTitle) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(3, 5, getWidth() - 4, getHeight() - 4, 16, 16);
                // Card body
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                // Subtle border
                g2.setColor(BORDER_LIGHT);
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                // Top shimmer line
                GradientPaint gp = new GradientPaint(0, 0, new Color(99, 102, 241, 80),
                                                     (getWidth() - 3) / 2f, 0, new Color(168, 85, 247, 50));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth() - 3, 2, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel lbl = new JLabel(sectionTitle);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(new Color(99, 102, 241, 180));
        lbl.setBorder(new EmptyBorder(0, 0, 12, 0));
        card.add(lbl, BorderLayout.NORTH);
        return card;
    }

    private JPanel buildLabelRow(String icon, String label, String hint, Color hintColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel main = new JLabel(icon + "  " + label);
        main.setFont(new Font("SansSerif", Font.BOLD, 13));
        main.setForeground(TEXT_PRIMARY);
        JLabel hintLbl = new JLabel(hint);
        hintLbl.setFont(FONT_HINT);
        hintLbl.setForeground(hintColor);
        row.add(main,    BorderLayout.WEST);
        row.add(hintLbl, BorderLayout.EAST);
        return row;
    }

    private JTextField createStyledField(String placeholder) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public void setBorder(Border border) {
                super.setBorder(border);
            }
        };
        field.setFont(FONT_INPUT_FLD);
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_HINT);
        field.setCaretColor(ACCENT);
        field.setBorder(createInputBorder(BORDER_LIGHT));
        field.setOpaque(false);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (field.getForeground().equals(TEXT_HINT)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
                field.setBorder(createInputBorder(BORDER_FOCUS));
            }
            @Override public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_HINT);
                }
                field.setBorder(createInputBorder(BORDER_LIGHT));
            }
        });
        return field;
    }

    private Border createInputBorder(Color color) {
        return new CompoundBorder(
            new LineBorder(color, 1, true),
            new EmptyBorder(11, 14, 11, 14)
        );
    }

    private JLabel makeChip(String text, Color fg, Color bg) {
        JLabel chip = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        chip.setFont(new Font("SansSerif", Font.BOLD, 12));
        chip.setForeground(fg);
        chip.setBackground(bg);
        chip.setOpaque(false);
        chip.setBorder(new EmptyBorder(7, 16, 7, 16));
        return chip;
    }

    private JButton createGlowButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Glow shadow
                g2.setColor(new Color(getBackground().getRed(), getBackground().getGreen(),
                                      getBackground().getBlue(), 60));
                g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 2, 12, 12);
                // Button fill
                GradientPaint gp = new GradientPaint(0, 0,
                    getBackground().brighter(),
                    0, getHeight(),
                    getBackground()
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() - 2, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(13, 22, 13, 22));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hover); btn.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg);    btn.repaint(); }
        });
        return btn;
    }

    private void styleTextArea(JTextArea area, Color bg) {
        area.setFont(FONT_RESULT);
        area.setBackground(bg);
        area.setForeground(TEXT_SECONDARY);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(14, 16, 14, 16));
        area.setCaretColor(ACCENT);
        area.setSelectionColor(new Color(99, 102, 241, 80));
    }

    private JScrollPane styledScrollPane(JTextArea area) {
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(new LineBorder(BORDER_LIGHT, 1, true));
        sp.getViewport().setBackground(area.getBackground());
        sp.getVerticalScrollBar().setUnitIncrement(12);
        sp.setBackground(area.getBackground());
        return sp;
    }

    private void setFieldBorderState(Color borderColor, Color bgColor, boolean isError) {
        Color bg = isError ? new Color(239, 68, 68, 20) : BG_INPUT;
        for (JTextField f : new JTextField[]{sleepField, workField, screenField}) {
            f.setBorder(createInputBorder(borderColor));
            f.setBackground(bg);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  VALIDATION (unchanged)
    // ═════════════════════════════════════════════════════════════════════════
    private double parsePositiveDouble(String text, String fieldName) {
        text = text.trim();
        if (text.isEmpty() || text.contains("e.g."))
            throw new IllegalArgumentException("⚠️  " + fieldName + " cannot be empty.");
        try {
            double val = Double.parseDouble(text);
            if (val < 0)
                throw new IllegalArgumentException("⚠️  " + fieldName + " must be ≥ 0.");
            return val;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "⚠️  Invalid value for " + fieldName + ": \"" + text + "\"");
        }
    }

    private void validateRange(double val, String name, double min, double max) {
        if (val < min || val > max)
            throw new IllegalArgumentException(
                "⚠️  " + name + " must be between " + (int)min + " and " + (int)max + ".");
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  COLOUR HELPERS
    // ═════════════════════════════════════════════════════════════════════════
    private Color getScoreColor(int score) {
        if (score >= 70) return SUCCESS;
        if (score >= 40) return WARNING;
        return DANGER;
    }

    private Color getScoreBgColor(int score) {
        if (score >= 70) return new Color(16, 185, 129, 20);
        if (score >= 40) return new Color(245, 158, 11, 20);
        return new Color(239, 68, 68, 20);
    }

    private Color getScoreGlowColor(int score) {
        if (score >= 70) return new Color(16, 185, 129, 50);
        if (score >= 40) return new Color(245, 158, 11, 50);
        return new Color(239, 68, 68, 50);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  DIALOGS
    // ═════════════════════════════════════════════════════════════════════════
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "EnerLyze – Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "EnerLyze", JOptionPane.INFORMATION_MESSAGE);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  MAIN
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Panel.background",             BG_MAIN);
            UIManager.put("OptionPane.background",        BG_CARD);
            UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
            UIManager.put("Button.background",            ACCENT);
            UIManager.put("Button.foreground",            Color.WHITE);
            UIManager.put("ScrollBar.thumb",              new Color(51, 65, 85));
            UIManager.put("ScrollBar.track",              BG_MAIN);
            UIManager.put("TextField.background",         BG_INPUT);
            UIManager.put("TextField.foreground",         TEXT_PRIMARY);
            UIManager.put("TextArea.background",          BG_INPUT);
            UIManager.put("TextArea.foreground",          TEXT_SECONDARY);
        } catch (Exception e) {
            System.out.println("Could not set look-and-feel: " + e.getMessage());
        }
        SwingUtilities.invokeLater(EnerLyzeApp::new);
    }
}

