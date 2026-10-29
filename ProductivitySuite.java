import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.geom.*;
import java.io.*;

import java.util.ArrayList;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 *   🌸 PRODUCTIVITY SUITE  —  Fixed Full-Screen Edition
 *   1. 📝 To-Do List
 *   2. 💰 Expense Tracker
 *   3. 🏃 Health & Fitness Tracker
 *   4. 📚 Study Planner & Pomodoro Timer
 *   5. 🗒  Text Editor (Improved)
 *   Features: Beautiful UI, Persistent Storage, Stats & Progress, Customization  
 * ╚══════════════════════════════════════════════════════════╝
 */
public class ProductivitySuite {

    // ── Shared Palette (high contrast) ───────────────────────────────────────
    static final Color C_PINK    = new Color(255, 182, 193);
    static final Color C_PURPLE  = new Color(160, 100, 230);
    static final Color C_MINT    = new Color( 50, 168, 120);
    static final Color C_PEACH   = new Color(230, 120,  50);
    static final Color C_SKY     = new Color( 50, 140, 220);
    static final Color C_WHITE   = Color.WHITE;
    static final Color C_BG      = new Color(248, 245, 255);   // near-white page bg
    static final Color C_CARD    = new Color(255, 255, 255);   // pure white cards
    static final Color C_TEXT    = new Color( 30,  20,  40);   // very dark = always visible
    static final Color C_TEXTDIM = new Color(100,  85, 115);   // medium dark
    static final Color C_BORDER  = new Color(180, 160, 210);   // visible border

    // Fonts — all large enough to read
    static final Font F_HUGE  = new Font("SansSerif", Font.BOLD,  52);
    static final Font F_TITLE = new Font("SansSerif", Font.BOLD,  36);
    static final Font F_HEAD  = new Font("SansSerif", Font.BOLD,  18);
    static final Font F_BODY  = new Font("SansSerif", Font.PLAIN, 15);
    static final Font F_BOLD  = new Font("SansSerif", Font.BOLD,  15);
    static final Font F_SMALL = new Font("SansSerif", Font.PLAIN, 13);
    static final Font F_MONO  = new Font("Monospaced", Font.PLAIN, 14);

    // Subject colors — actual Color objects matching the emoji label
    static final Color[] SUBJ_COLORS = {
        new Color( 50, 120, 220),  // Blue
        new Color( 40, 160,  80),  // Green
        new Color(200, 150,  20),  // Yellow
        new Color(210,  50,  50),  // Red
        new Color(140,  60, 200),  // Purple
        new Color(220, 110,  20),  // Orange
    };
    static final String[] SUBJ_COLOR_NAMES = {
        "🔵 Blue","🟢 Green","🟡 Yellow","🔴 Red","🟣 Purple","🟠 Orange"
    };

    // =========================================================================
    //  MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(Launcher::new);
    }

    // =========================================================================
    //  LAUNCHER
    // =========================================================================
    static class Launcher extends JFrame {
        Launcher() {
            setTitle("🌸 Productivity Suite");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setExtendedState(MAXIMIZED_BOTH);

            JPanel root = new GradPanel(new Color(250,244,255), new Color(236,228,255));
            root.setLayout(new BorderLayout());
            root.add(buildHeader(), BorderLayout.NORTH);
            root.add(buildCards(),  BorderLayout.CENTER);
            root.add(buildFooter(), BorderLayout.SOUTH);
            setContentPane(root);
            setVisible(true);
        }

        private JPanel buildHeader() {
            JPanel p = new JPanel();
            p.setOpaque(false);
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.setBorder(new EmptyBorder(40, 0, 14, 0));

            JLabel flowers = lbl("🌸  🌼  🌷  🌻  🌺",
                    new Font("SansSerif", Font.PLAIN, 24), new Color(160, 100, 180));
            flowers.setAlignmentX(CENTER_ALIGNMENT);

            JLabel title = lbl("✨  My Productivity Suite  ✨",
                    new Font("SansSerif", Font.BOLD, 50), new Color(90, 40, 120));
            title.setAlignmentX(CENTER_ALIGNMENT);

            JLabel sub = lbl("Five apps to help you stay on top of everything 💖",
                    new Font("SansSerif", Font.PLAIN, 17), C_TEXTDIM);
            sub.setAlignmentX(CENTER_ALIGNMENT);

            p.add(flowers);
            p.add(Box.createVerticalStrut(8));
            p.add(title);
            p.add(Box.createVerticalStrut(6));
            p.add(sub);
            return p;
        }

        private JPanel buildCards() {
            JPanel wrap = new JPanel(new GridBagLayout());
            wrap.setOpaque(false);
            JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 28, 10));
            row.setOpaque(false);

            row.add(appCard("📝", "To-Do List",
                    "Add tasks · Priority\nMark done · Filter",
                    new Color(255, 200, 210), new Color(210, 60, 100),
                    () -> new ToDoApp().setVisible(true)));

            row.add(appCard("💰", "Expense Tracker",
                    "Log expenses in ₹\nCategory summary",
                    new Color(180, 240, 210), new Color(30, 150, 90),
                    () -> new ExpenseTracker().setVisible(true)));

            row.add(appCard("🏃", "Health Tracker",
                    "Log workouts & water\nCalories & BMI",
                    new Color(255, 220, 190), new Color(210, 100, 30),
                    () -> new HealthTracker().setVisible(true)));

            row.add(appCard("📚", "Study Planner",
                    "Add subjects · Pomodoro\nCustom timer & progress",
                    new Color(190, 220, 255), new Color(40, 120, 210),
                    () -> new StudyPlanner().setVisible(true)));

            row.add(appCard("🗒", "Text Editor",
                    "Write · Open · Save\nFind · Dark mode · Stats",
                    new Color(220, 200, 255), new Color(110, 60, 200),
                    () -> new TextEditor().setVisible(true)));

            wrap.add(row);
            return wrap;
        }

        private JPanel appCard(String emoji, String name, String desc,
                               Color strip, Color accent, Runnable action) {
            JPanel c = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // shadow
                    g2.setColor(new Color(0, 0, 0, 25));
                    g2.fillRoundRect(5, 5, getWidth()-5, getHeight()-5, 28, 28);
                    // card body
                    g2.setColor(C_CARD);
                    g2.fillRoundRect(0, 0, getWidth()-7, getHeight()-7, 28, 28);
                    // coloured strip
                    g2.setColor(strip);
                    g2.fillRoundRect(0, 0, getWidth()-7, 112, 28, 28);
                    g2.fillRect(0, 84, getWidth()-7, 30);
                    // card border
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.drawRoundRect(0, 0, getWidth()-8, getHeight()-8, 28, 28);
                }
            };
            c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
            c.setOpaque(false);
            c.setPreferredSize(new Dimension(220, 310));
            c.setBorder(new EmptyBorder(0, 0, 7, 7));
            c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JLabel el = lbl(emoji, new Font("SansSerif", Font.PLAIN, 46), C_TEXT);
            el.setAlignmentX(CENTER_ALIGNMENT);
            el.setBorder(new EmptyBorder(18, 0, 0, 0));

            JLabel nl = lbl(name, new Font("SansSerif", Font.BOLD, 17), new Color(20, 10, 30));
            nl.setAlignmentX(CENTER_ALIGNMENT);

            JLabel dl = new JLabel(
                "<html><center>" + desc.replace("\n", "<br>") + "</center></html>", JLabel.CENTER);
            dl.setFont(F_SMALL);
            dl.setForeground(C_TEXTDIM);
            dl.setAlignmentX(CENTER_ALIGNMENT);
            dl.setBorder(new EmptyBorder(4, 12, 0, 12));

            JButton btn = pillBtn("Open  ✨", accent, Color.WHITE);
            btn.setMaximumSize(new Dimension(160, 38));
            btn.setAlignmentX(CENTER_ALIGNMENT);
            btn.addActionListener(e -> action.run());

            c.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { c.setBorder(new EmptyBorder(0,0,11,11)); c.repaint(); }
                public void mouseExited(MouseEvent e)  { c.setBorder(new EmptyBorder(0,0,7,7));  c.repaint(); }
            });

            c.add(el);
            c.add(Box.createVerticalStrut(12));
            c.add(nl);
            c.add(Box.createVerticalStrut(6));
            c.add(dl);
            c.add(Box.createVerticalGlue());
            c.add(btn);
            c.add(Box.createVerticalStrut(20));
            return c;
        }

        private JPanel buildFooter() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
            p.setOpaque(false);
            p.setBorder(new EmptyBorder(0, 0, 14, 0));
            p.add(lbl("Made with 💖 by Vaishnavi Parekh", F_SMALL, C_TEXTDIM));
            return p;
        }
    }

    // =========================================================================
    //  APP 1 — TO-DO LIST
    // =========================================================================
    static class ToDoApp extends JFrame implements ActionListener {

        static class Task {
            String text, priority; boolean done;
            Task(String t, String p) { text = t; priority = p; }
            public String toString() {
                return (done ? "✅  " : "⬜  ") + "[" + priority + "]   " + text;
            }
        }

        private final ArrayList<Task> tasks = new ArrayList<>();
        private JTextField taskField;
        private JComboBox<String> priorityBox, filterBox;
        private JList<String> taskList;
        private DefaultListModel<String> listModel;
        private JButton addBtn, doneBtn, deleteBtn;
        private JLabel statusLabel, totalLbl, doneLbl, pendingLbl;

        ToDoApp() {
            setTitle("📝 To-Do List");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setExtendedState(MAXIMIZED_BOTH);

            JPanel root = new GradPanel(new Color(255, 242, 248), new Color(255, 225, 238));
            root.setLayout(new BorderLayout());
            root.setBorder(new EmptyBorder(22, 30, 22, 30));
            root.add(appHdr("📝  My To-Do List", new Color(200, 50, 90), new Color(210, 60, 100)), BorderLayout.NORTH);
            root.add(buildBody(),   BorderLayout.CENTER);
            root.add(buildFooter(), BorderLayout.SOUTH);
            setContentPane(root);
            refresh();
        }

        private JPanel buildBody() {
            JPanel p = new JPanel(new BorderLayout(16, 0));
            p.setOpaque(false);
            JPanel left = new JPanel(new BorderLayout(0, 12));
            left.setOpaque(false);

            // Input card
            RoundCard ic = new RoundCard(C_CARD);
            ic.setLayout(new BoxLayout(ic, BoxLayout.Y_AXIS));
            ic.setBorder(new EmptyBorder(16, 18, 16, 18));
            ic.add(lbl("✏️  Add a New Task", F_HEAD, new Color(200, 50, 90)));
            ic.add(Box.createVerticalStrut(8));
            taskField = roundField();
            taskField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            taskField.setAlignmentX(LEFT_ALIGNMENT);
            taskField.addActionListener(this);
            ic.add(taskField);
            JPanel pr = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            pr.setOpaque(false);
            pr.add(lbl("Priority:", F_BODY, C_TEXT));
            priorityBox = combo("🔴 High", "🟡 Medium", "🟢 Low");
            pr.add(priorityBox);
            addBtn = pillBtn("Add Task  ✚", new Color(210, 60, 100), Color.WHITE);
            addBtn.addActionListener(this);
            pr.add(addBtn);
            JPanel prw = new JPanel(new BorderLayout());
            prw.setOpaque(false);
            prw.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            prw.add(pr);
            ic.add(prw);

            // List card
            RoundCard lc = new RoundCard(C_CARD);
            lc.setLayout(new BorderLayout(0, 8));
            lc.setBorder(new EmptyBorder(14, 16, 14, 16));

            JPanel fr = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            fr.setOpaque(false);
            fr.add(lbl("📋  Tasks", F_HEAD, new Color(200, 50, 90)));
            fr.add(Box.createHorizontalStrut(8));
            fr.add(lbl("Show:", F_BODY, C_TEXT));
            filterBox = combo("All", "Pending", "Done", "🔴 High", "🟡 Medium", "🟢 Low");
            filterBox.addActionListener(this);
            fr.add(filterBox);

            listModel = new DefaultListModel<>();
            taskList  = new JList<>(listModel);
            taskList.setFont(new Font("SansSerif", Font.PLAIN, 15));
            taskList.setBackground(new Color(255, 250, 252));
            taskList.setForeground(C_TEXT);
            taskList.setSelectionBackground(new Color(255, 190, 210));
            taskList.setSelectionForeground(new Color(20, 10, 30));
            taskList.setFixedCellHeight(40);
            taskList.setBorder(new EmptyBorder(4, 10, 4, 10));
            taskList.setCellRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> l, Object v,
                        int i, boolean s, boolean f) {
                    JLabel lb = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
                    lb.setFont(new Font("SansSerif", Font.PLAIN, 15));
                    String st = v.toString();
                    if (!s) {
                        if      (st.startsWith("✅"))       lb.setForeground(new Color(140, 130, 150));
                        else if (st.contains("[🔴"))        lb.setForeground(new Color(200, 40, 60));
                        else if (st.contains("[🟡"))        lb.setForeground(new Color(170, 120, 10));
                        else                                lb.setForeground(new Color(30, 140, 70));
                    } else {
                        lb.setForeground(new Color(20, 10, 30));
                    }
                    lb.setBorder(new EmptyBorder(4, 12, 4, 12));
                    return lb;
                }
            });
            JScrollPane sc = styledScroll(taskList, new Color(220, 180, 200));
            lc.add(fr, BorderLayout.NORTH);
            lc.add(sc, BorderLayout.CENTER);

            left.add(ic, BorderLayout.NORTH);
            left.add(lc, BorderLayout.CENTER);

            // Stats panel
            RoundCard sc2 = new RoundCard(new Color(255, 248, 252));
            sc2.setLayout(new BoxLayout(sc2, BoxLayout.Y_AXIS));
            sc2.setBorder(new EmptyBorder(20, 20, 20, 20));
            sc2.setPreferredSize(new Dimension(200, 0));
            totalLbl   = bigStatLbl("0");
            doneLbl    = bigStatLbl("0");
            pendingLbl = bigStatLbl("0");
            sc2.add(lbl("📊Statistics", F_HEAD, new Color(200, 50, 90)));
            sc2.add(Box.createVerticalStrut(14));
            addStat(sc2, totalLbl,   "Total Tasks",  new Color(130, 80, 200));
            sc2.add(Box.createVerticalStrut(12));
            addStat(sc2, doneLbl,    "Completed ✅",  new Color(30, 150, 90));
            sc2.add(Box.createVerticalStrut(12));
            addStat(sc2, pendingLbl, "Remaining ⏳",  new Color(210, 100, 30));

            p.add(left, BorderLayout.CENTER);
            p.add(sc2,  BorderLayout.EAST);
            return p;
        }

        private JPanel buildFooter() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            p.setOpaque(false);
            p.setBorder(new EmptyBorder(12, 0, 0, 0));
            doneBtn   = pillBtn("✅  Mark Done",  new Color(30, 160, 90),  Color.WHITE);
            deleteBtn = pillBtn("🗑  Delete",      new Color(210, 60, 60),  Color.WHITE);
            JButton cl = pillBtn("🧹  Clear All", new Color(150, 120, 180), Color.WHITE);
            doneBtn.addActionListener(this);
            deleteBtn.addActionListener(this);
            cl.addActionListener(e -> {
                if (!tasks.isEmpty() && confirm(this, "Clear all tasks?")) {

                    tasks.clear(); refresh();
                }
            });
            statusLabel = lbl("Add a task to get started! 🌸", F_SMALL, C_TEXTDIM);
            p.add(doneBtn); p.add(deleteBtn); p.add(cl);
            p.add(Box.createHorizontalStrut(14));
            p.add(statusLabel);
            return p;
        }

        public void actionPerformed(ActionEvent e) {
            Object s = e.getSource();
            if      (s == addBtn || s == taskField) addTask();
            else if (s == doneBtn)                  markDone();
            else if (s == deleteBtn)                deleteTask();
            else if (s == filterBox)                refresh();
        }

        private void addTask() {
            String t = taskField.getText().trim();
            if (t.isEmpty()) { st("Type something first! 🌸"); return; }
            String r = (String) priorityBox.getSelectedItem();
            String priority = r.contains("High") ? "🔴 High"
                    : r.contains("Medium") ? "🟡 Medium" : "🟢 Low";
            Task task = new Task(t, priority);
            tasks.add(task);
            taskField.setText(""); refresh(); st("Added: " + t + " ✓");
        }
        private void markDone() {
            int i = taskList.getSelectedIndex();
            if (i < 0) { st("Select a task!"); return; }
            Task t = filtered().get(i);
            t.done = !t.done;
            refresh();
            st(t.done ? "Done! 🎉" : "Back to pending.");
        }
        private void deleteTask() {
            int i = taskList.getSelectedIndex();
            if (i < 0) { st("Select a task!"); return; }
            Task t = filtered().get(i);
            if (confirm(this, "Delete: " + t.text)) {
                tasks.remove(t); refresh(); st("Deleted 🗑");
            }
        }
        private ArrayList<Task> filtered() {
            String f = (String) filterBox.getSelectedItem();
            ArrayList<Task> r = new ArrayList<>();
            for (Task t : tasks) {
                if (f.equals("Pending") && t.done) continue;
                if (f.equals("Done") && !t.done) continue;
                if (f.contains("High")   && !f.equals("All") && !t.priority.contains("High"))   continue;
                if (f.contains("Medium") && !f.equals("All") && !t.priority.contains("Medium")) continue;
                if (f.contains("Low")    && !f.equals("All") && !t.priority.contains("Low"))    continue;
                r.add(t);
            }
            return r;
        }
        private void refresh() {
            listModel.clear();
            for (Task t : filtered()) listModel.addElement(t.toString());
            long d = tasks.stream().filter(t -> t.done).count();
            totalLbl.setText(String.valueOf(tasks.size()));
            doneLbl.setText(String.valueOf(d));
            pendingLbl.setText(String.valueOf(tasks.size() - d));
        }
        private void st(String m) { statusLabel.setText(m); }
    }

    
    static class ExpenseTracker extends JFrame implements ActionListener {

        static class Expense {
            String date, category, description; double amount;
            Expense(String d, String c, String desc, double a) {
                date = d; category = c; description = desc; amount = a;
            }
        }

        private static final String[] CATS = {
            "🍔 Food", "🚌 Travel", "💡 Bills", "🛍 Shopping", "💊 Health", "📦 Other"
        };
        private static final String[] COLS = {"Date", "Category", "Description", "Amount (₹)"};

        private final ArrayList<Expense> expenses = new ArrayList<>();
        private JTextField dateField, descField, amountField;
        private JComboBox<String> categoryBox, filterBox;
        private JTable table;
        private DefaultTableModel tableModel;
        private JButton addBtn, deleteBtn;
        private JLabel totalLabel, statusLabel, foodLbl, travelLbl, billsLbl, otherLbl;

        ExpenseTracker() {
            setTitle("💰 Expense Tracker");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setExtendedState(MAXIMIZED_BOTH);

            JPanel root = new GradPanel(new Color(236, 255, 245), new Color(216, 248, 234));
            root.setLayout(new BorderLayout());
            root.setBorder(new EmptyBorder(22, 30, 22, 30));
            root.add(appHdr("💰  Expense Tracker", new Color(20, 130, 80), new Color(30, 160, 95)), BorderLayout.NORTH);
            root.add(buildBody(),   BorderLayout.CENTER);
            root.add(buildFooter(), BorderLayout.SOUTH);
            setContentPane(root);
            refreshTable();
        }

        private JPanel buildBody() {
            JPanel p = new JPanel(new BorderLayout(16, 0));
            p.setOpaque(false);
            JPanel left = new JPanel(new BorderLayout(0, 12));
            left.setOpaque(false);

            RoundCard fc = new RoundCard(C_CARD);
            fc.setLayout(new GridLayout(2, 4, 10, 8));
            fc.setBorder(new EmptyBorder(16, 18, 16, 18));
            dateField   = roundField();
            descField   = roundField();
            amountField = roundField();
            categoryBox = new JComboBox<>(CATS);
            categoryBox.setFont(F_BODY); categoryBox.setBackground(C_WHITE);
            addBtn = pillBtn("  Add  ✚", new Color(30, 150, 90), Color.WHITE);
            addBtn.addActionListener(this);
            filterBox = new JComboBox<>();
            filterBox.addItem("All");
            for (String c : CATS) filterBox.addItem(c);
            filterBox.setFont(F_BODY); filterBox.setBackground(C_WHITE);
            filterBox.addActionListener(this);
            fc.add(lf("📅 Date",        dateField));
            fc.add(lf("🏷 Category",    categoryBox));
            fc.add(lf("📝 Description", descField));
            fc.add(lf("💵 Amount (₹)",  amountField));
            fc.add(addBtn);
            fc.add(lf("🔍 Filter",      filterBox));
            fc.add(new JLabel()); fc.add(new JLabel());

            RoundCard tc = new RoundCard(C_CARD);
            tc.setLayout(new BorderLayout(0, 8));
            tc.setBorder(new EmptyBorder(14, 16, 14, 16));
            tableModel = new DefaultTableModel(COLS, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            table = new JTable(tableModel);
            table.setFont(F_BODY);
            table.setForeground(C_TEXT);
            table.setRowHeight(32);
            table.setBackground(new Color(245, 255, 250));
            table.setSelectionBackground(new Color(160, 230, 200));
            table.setSelectionForeground(new Color(10, 40, 20));
            table.setGridColor(new Color(180, 220, 200));
            table.getTableHeader().setFont(F_BOLD);
            table.getTableHeader().setBackground(new Color(30, 150, 100));
            table.getTableHeader().setForeground(Color.WHITE);
            DefaultTableCellRenderer ra = new DefaultTableCellRenderer();
            ra.setHorizontalAlignment(JLabel.RIGHT);
            ra.setForeground(C_TEXT);
            table.getColumnModel().getColumn(3).setCellRenderer(ra);
            JScrollPane sc = styledScroll(table, new Color(140, 210, 180));
            tc.add(lbl("🧾  All Expenses", F_HEAD, new Color(20, 130, 80)), BorderLayout.NORTH);
            tc.add(sc, BorderLayout.CENTER);

            left.add(fc, BorderLayout.NORTH);
            left.add(tc, BorderLayout.CENTER);

            RoundCard sum = new RoundCard(new Color(240, 255, 248));
            sum.setLayout(new BoxLayout(sum, BoxLayout.Y_AXIS));
            sum.setBorder(new EmptyBorder(20, 20, 20, 20));
            sum.setPreferredSize(new Dimension(210, 0));
            totalLabel = lbl("₹0.00", new Font("SansSerif", Font.BOLD, 30), new Color(20, 130, 80));
            totalLabel.setAlignmentX(CENTER_ALIGNMENT);
            foodLbl   = sumLbl("🍔 Food      ₹0");
            travelLbl = sumLbl("🚌 Travel    ₹0");
            billsLbl  = sumLbl("💡 Bills     ₹0");
            otherLbl  = sumLbl("📦 Others    ₹0");
            JLabel ts = lbl("Total Spent", F_SMALL, C_TEXTDIM);
            ts.setAlignmentX(CENTER_ALIGNMENT);
            sum.add(lbl("💹Summary", F_HEAD, new Color(20, 130, 80)));
            sum.add(Box.createVerticalStrut(10));
            sum.add(ts); sum.add(totalLabel);
            sum.add(Box.createVerticalStrut(14));
            sum.add(new JSeparator());
            sum.add(Box.createVerticalStrut(10));
            sum.add(lbl("By Category", F_BOLD, C_TEXT));
            sum.add(Box.createVerticalStrut(8));
            sum.add(foodLbl); sum.add(Box.createVerticalStrut(6));
            sum.add(travelLbl); sum.add(Box.createVerticalStrut(6));
            sum.add(billsLbl);  sum.add(Box.createVerticalStrut(6));
            sum.add(otherLbl);

            p.add(left, BorderLayout.CENTER);
            p.add(sum,  BorderLayout.EAST);
            return p;
        }

        private JPanel buildFooter() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            p.setOpaque(false); p.setBorder(new EmptyBorder(12, 0, 0, 0));
            deleteBtn = pillBtn("🗑  Delete Selected", new Color(210, 60, 60), Color.WHITE);
            JButton cl = pillBtn("🧹  Clear All", new Color(130, 160, 145), Color.WHITE);
            deleteBtn.addActionListener(this);
            cl.addActionListener(e -> {
                if (!expenses.isEmpty() && confirm(this, "Clear all expenses?")) {

                    expenses.clear(); refreshTable();
                }
            });
            statusLabel = lbl("Add your first expense! 🌿", F_SMALL, C_TEXTDIM);
            p.add(deleteBtn); p.add(cl);
            p.add(Box.createHorizontalStrut(14)); p.add(statusLabel);
            return p;
        }

        public void actionPerformed(ActionEvent e) {
            if      (e.getSource() == addBtn)    addExpense();
            else if (e.getSource() == deleteBtn) deleteExpense();
            else if (e.getSource() == filterBox) refreshTable();
        }

        private void addExpense() {
            String d = dateField.getText().trim(), desc = descField.getText().trim(),
                   a = amountField.getText().trim();
            if (d.isEmpty() || desc.isEmpty() || a.isEmpty()) { st("Fill all fields! 🌿"); return; }
            double amt;
            try { amt = Double.parseDouble(a); if (amt <= 0) throw new NumberFormatException(); }
            catch (NumberFormatException ex) { st("Enter a valid amount!"); amountField.setText(""); return; }
            String cat = (String) categoryBox.getSelectedItem();
            Expense expense = new Expense(d, cat, desc, amt);
            expenses.add(expense);
            dateField.setText(""); descField.setText(""); amountField.setText("");
            refreshTable(); st("Added ₹" + String.format("%.2f", amt) + " ✓");
        }
        private void deleteExpense() {
            int row = table.getSelectedRow();
            if (row < 0) { st("Select a row first!"); return; }
            if (confirm(this, "Delete this expense?")) {
                String f = (String) filterBox.getSelectedItem(); int c = 0;
                for (int i = 0; i < expenses.size(); i++) {
                    Expense ex = expenses.get(i);
                    if (f.equals("All") || ex.category.equals(f)) {
                        if (c == row) {

                            expenses.remove(i); break;
                        } c++;
                    }
                }
                refreshTable(); st("Deleted 🗑");
            }
        }
        private void refreshTable() {
            tableModel.setRowCount(0);
            String filter = (String) filterBox.getSelectedItem(); double total = 0;
            for (Expense ex : expenses) {
                if (filter.equals("All") || ex.category.equals(filter)) {
                    tableModel.addRow(new Object[]{
                        ex.date, ex.category, ex.description, String.format("%.2f", ex.amount)});
                    total += ex.amount;
                }
            }
            totalLabel.setText("₹" + String.format("%.2f", total));
            double food = 0, travel = 0, bills = 0, other = 0;
            for (Expense ex : expenses) {
                if      (ex.category.contains("Food"))   food   += ex.amount;
                else if (ex.category.contains("Travel")) travel += ex.amount;
                else if (ex.category.contains("Bills"))  bills  += ex.amount;
                else                                     other  += ex.amount;
            }
            foodLbl.setText("🍔 Food ₹" + String.format("%.0f", food));
            travelLbl.setText("🚌 Travel ₹" + String.format("%.0f", travel));
            billsLbl.setText("💡 Bills ₹" + String.format("%.0f", bills));
            otherLbl.setText("📦 Others ₹" + String.format("%.0f", other));
        }
        private void st(String m) { statusLabel.setText(m); }
        private JPanel lf(String l, JComponent comp) {
            JPanel p = new JPanel(new BorderLayout(4, 4)); p.setOpaque(false);
            p.add(lbl(l, F_SMALL, C_TEXT), BorderLayout.NORTH);
            p.add(comp, BorderLayout.CENTER); return p;
        }
        private JLabel sumLbl(String t) {
            JLabel l = new JLabel(t);
            l.setFont(new Font("Monospaced", Font.PLAIN, 13));
            l.setForeground(C_TEXT);
            l.setAlignmentX(LEFT_ALIGNMENT); return l;
        }
    }


    static class HealthTracker extends JFrame implements ActionListener {

        static class Entry {
            String date, activity; int duration, calories; double water;
            Entry(String d, String a, int dur, int cal, double w) {
                date = d; activity = a; duration = dur; calories = cal; water = w;
            }
        }

        private static final String[] ACTS = {
            "🏃 Running","🚴 Cycling","🏊 Swimming","🏋 Weightlifting",
            "🧘 Yoga","🚶 Walking","⚽ Sports","Other"
        };
        private static final String[] COLS = {"Date","Activity","Duration (min)","Calories","Water (L)"};

        private final ArrayList<Entry> entries = new ArrayList<>();
        private JTextField dateF, durF, calF, waterF, heightF, weightF;
        private JComboBox<String> actBox;
        private JTable table; private DefaultTableModel tableModel;
        private JButton addBtn, deleteBtn, bmiBtn;
        private JLabel totalCalLbl, totalWaterLbl, totalMinLbl, bmiLbl, statusLabel;

        HealthTracker() {
            setTitle("🏃 Health Tracker");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setExtendedState(MAXIMIZED_BOTH);

            JPanel root = new GradPanel(new Color(255, 244, 232), new Color(255, 228, 208));
            root.setLayout(new BorderLayout());
            root.setBorder(new EmptyBorder(22, 30, 22, 30));
            root.add(appHdr("🏃  Health & Fitness Tracker", new Color(190, 90, 20), new Color(210, 110, 30)), BorderLayout.NORTH);
            root.add(buildBody(),   BorderLayout.CENTER);
            root.add(buildFooter(), BorderLayout.SOUTH);
            setContentPane(root);
            refreshTable();
        }

        private JPanel buildBody() {
            JPanel p = new JPanel(new BorderLayout(16, 0)); p.setOpaque(false);
            JPanel left = new JPanel(new BorderLayout(0, 12)); left.setOpaque(false);

            RoundCard fc = new RoundCard(C_CARD);
            fc.setLayout(new GridLayout(2, 4, 10, 8)); fc.setBorder(new EmptyBorder(16, 18, 16, 18));
            dateF   = roundField(); durF  = roundField();
            calF    = roundField(); waterF = roundField();
            actBox  = new JComboBox<>(ACTS); actBox.setFont(F_BODY); actBox.setBackground(C_WHITE);
            addBtn  = pillBtn("  Log  ✚", new Color(200, 100, 20), Color.WHITE);
            addBtn.addActionListener(this);
            fc.add(lf2("📅 Date",          dateF));
            fc.add(lf2("🏅 Activity",       actBox));
            fc.add(lf2("⏱ Duration (min)", durF));
            fc.add(lf2("🔥 Calories",       calF));
            fc.add(lf2("💧 Water (L)",      waterF));
            fc.add(addBtn);
            fc.add(new JLabel()); fc.add(new JLabel());

            RoundCard tc = new RoundCard(C_CARD);
            tc.setLayout(new BorderLayout(0, 8)); tc.setBorder(new EmptyBorder(14, 16, 14, 16));
            tableModel = new DefaultTableModel(COLS, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            table = new JTable(tableModel);
            table.setFont(F_BODY); table.setForeground(C_TEXT); table.setRowHeight(30);
            table.setBackground(new Color(255, 248, 240));
            table.setSelectionBackground(new Color(255, 210, 170));
            table.setSelectionForeground(new Color(40, 15, 5));
            table.setGridColor(new Color(240, 210, 185));
            table.getTableHeader().setFont(F_BOLD);
            table.getTableHeader().setBackground(new Color(200, 100, 30));
            table.getTableHeader().setForeground(Color.WHITE);
            JScrollPane sc = styledScroll(table, new Color(220, 185, 155));
            tc.add(lbl("📋  Activity Log", F_HEAD, new Color(190, 90, 20)), BorderLayout.NORTH);
            tc.add(sc, BorderLayout.CENTER);

            left.add(fc, BorderLayout.NORTH);
            left.add(tc, BorderLayout.CENTER);

            JPanel right = new JPanel(new BorderLayout(0, 12));
            right.setOpaque(false); right.setPreferredSize(new Dimension(210, 0));

            RoundCard statsCard = new RoundCard(new Color(255, 246, 236));
            statsCard.setLayout(new BoxLayout(statsCard, BoxLayout.Y_AXIS));
            statsCard.setBorder(new EmptyBorder(18, 18, 18, 18));
            totalCalLbl   = bigStatLbl("0");
            totalWaterLbl = bigStatLbl("0");
            totalMinLbl   = bigStatLbl("0");
            statsCard.add(lbl("📊  Today's Stats", F_HEAD, new Color(190, 90, 20)));
            statsCard.add(Box.createVerticalStrut(12));
            addStat(statsCard, totalCalLbl,   "Calories 🔥",  new Color(210, 70, 30));
            statsCard.add(Box.createVerticalStrut(10));
            addStat(statsCard, totalWaterLbl, "Water 💧 (L)", new Color(40, 150, 210));
            statsCard.add(Box.createVerticalStrut(10));
            addStat(statsCard, totalMinLbl,   "Minutes 🏅",   new Color(200, 140, 20));

            RoundCard bmiCard = new RoundCard(new Color(255, 246, 236));
            bmiCard.setLayout(new BoxLayout(bmiCard, BoxLayout.Y_AXIS));
            bmiCard.setBorder(new EmptyBorder(14, 16, 14, 16));
            heightF = roundField(); heightF.setToolTipText("e.g. 170");
            heightF.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            heightF.setAlignmentX(LEFT_ALIGNMENT);
            weightF = roundField(); weightF.setToolTipText("e.g. 65");
            weightF.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            weightF.setAlignmentX(LEFT_ALIGNMENT);
            bmiBtn = pillBtn("Calculate BMI", new Color(200, 100, 20), Color.WHITE);
            bmiBtn.setAlignmentX(CENTER_ALIGNMENT); bmiBtn.addActionListener(this);
            bmiLbl = lbl("BMI: —", new Font("SansSerif", Font.BOLD, 18), new Color(190, 90, 20));
            bmiLbl.setAlignmentX(CENTER_ALIGNMENT);
            bmiCard.add(lbl("⚖️  BMI Calculator", F_HEAD, new Color(190, 90, 20)));
            bmiCard.add(Box.createVerticalStrut(8));
            bmiCard.add(lbl("Height (cm):", F_BODY, C_TEXT));
            bmiCard.add(Box.createVerticalStrut(4)); bmiCard.add(heightF);
            bmiCard.add(Box.createVerticalStrut(8));
            bmiCard.add(lbl("Weight (kg):", F_BODY, C_TEXT));
            bmiCard.add(Box.createVerticalStrut(4)); bmiCard.add(weightF);
            bmiCard.add(Box.createVerticalStrut(10));
            bmiCard.add(bmiBtn); bmiCard.add(Box.createVerticalStrut(8)); bmiCard.add(bmiLbl);

            right.add(statsCard, BorderLayout.NORTH);
            right.add(bmiCard,   BorderLayout.CENTER);
            p.add(left,  BorderLayout.CENTER);
            p.add(right, BorderLayout.EAST);
            return p;
        }

        private JPanel buildFooter() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            p.setOpaque(false); p.setBorder(new EmptyBorder(12, 0, 0, 0));
            deleteBtn = pillBtn("🗑  Delete", new Color(210, 60, 60), Color.WHITE);
            JButton cl = pillBtn("🧹  Clear All", new Color(170, 140, 110), Color.WHITE);
            deleteBtn.addActionListener(this);
            cl.addActionListener(e -> {
                if (!entries.isEmpty() && confirm(this, "Clear all entries?")) {

                    entries.clear(); refreshTable();
                }
            });
            statusLabel = lbl("Log your first activity! 🏃", F_SMALL, C_TEXTDIM);
            p.add(deleteBtn); p.add(cl);
            p.add(Box.createHorizontalStrut(14)); p.add(statusLabel);
            return p;
        }

        public void actionPerformed(ActionEvent e) {
            if      (e.getSource() == addBtn)    addEntry();
            else if (e.getSource() == deleteBtn) deleteEntry();
            else if (e.getSource() == bmiBtn)    calcBMI();
        }

        private void addEntry() {
            String d = dateF.getText().trim(), act = (String) actBox.getSelectedItem();
            String durs = durF.getText().trim(), cals = calF.getText().trim(), ws = waterF.getText().trim();
            if (d.isEmpty() || durs.isEmpty() || cals.isEmpty() || ws.isEmpty()) {
                st("Fill all fields!"); return;
            }
            int dur, cal; double water;
            try { dur = Integer.parseInt(durs); cal = Integer.parseInt(cals); water = Double.parseDouble(ws); }
            catch (NumberFormatException ex) { st("Enter valid numbers!"); return; }
            Entry entry = new Entry(d, act, dur, cal, water);
            entries.add(entry);
            dateF.setText(""); durF.setText(""); calF.setText(""); waterF.setText("");
            refreshTable(); st("Logged: " + act + " ✓");
        }
        private void deleteEntry() {
            int row = table.getSelectedRow();
            if (row < 0) { st("Select a row first!"); return; }
            if (confirm(this, "Delete this entry?")) {

                entries.remove(row); refreshTable(); st("Deleted 🗑");
            }
        }
        private void calcBMI() {
            try {
                double h = Double.parseDouble(heightF.getText().trim()) / 100.0;
                double w = Double.parseDouble(weightF.getText().trim());
                double bmi = w / (h * h);
                String cat = bmi < 18.5 ? "Underweight 🔵" : bmi < 25 ? "Normal ✅"
                           : bmi < 30 ? "Overweight 🟡" : "Obese 🔴";
                bmiLbl.setText(String.format("BMI: %.1f  %s", bmi, cat));
            } catch (NumberFormatException ex) { st("Enter valid height and weight!"); }
        }
        private void refreshTable() {
            tableModel.setRowCount(0);
            double tc = 0, tw = 0; int tm = 0;
            for (Entry en : entries) {
                tableModel.addRow(new Object[]{
                    en.date, en.activity, en.duration, en.calories, String.format("%.1f", en.water)});
                tc += en.calories; tw += en.water; tm += en.duration;
            }
            totalCalLbl.setText(String.valueOf((int) tc));
            totalWaterLbl.setText(String.format("%.1f", tw));
            totalMinLbl.setText(String.valueOf(tm));
        }
        private void st(String m) { statusLabel.setText(m); }
        private JPanel lf2(String l, JComponent c) {
            JPanel p = new JPanel(new BorderLayout(4, 4)); p.setOpaque(false);
            p.add(lbl(l, F_SMALL, C_TEXT), BorderLayout.NORTH);
            p.add(c, BorderLayout.CENTER); return p;
        }
    }

    
    static class StudyPlanner extends JFrame implements ActionListener {

        static class Subject {
            String name; int colorIdx, targetHours, studiedMins;
            Subject(String n, int ci, int t) { name = n; colorIdx = ci; targetHours = t; }
        }

        private final ArrayList<Subject> subjects = new ArrayList<>();

        // Timer
        private javax.swing.Timer countdownTimer;
        private int  secondsLeft  = 25 * 60;
        private int  workSecs     = 25 * 60;   // ← now changeable by user
        private int  breakSecs    =  5 * 60;   // ← now changeable by user
        private boolean timerRunning = false;
        private boolean isBreak      = false;
        private int  pomodorosDone   = 0;

        // Components — subjects
        private JTextField subjField;
        private JComboBox<String> colorBox;
        private JSpinner hoursSpinner;
        private JList<String> subjList;
        private DefaultListModel<String> listModel;
        private JButton addSubjBtn, deleteSubjBtn, addMinBtn;

        // Components — timer
        private JLabel  timerLabel, timerStatusLbl, pomodoroCountLbl, statusLabel;
        private JButton startBtn, resetBtn;
        private JProgressBar progressBar;

        // FIX: custom minutes spinners
        private JSpinner workMinsSpinner, breakMinsSpinner;

        StudyPlanner() {
            setTitle("📚 Study Planner");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setExtendedState(MAXIMIZED_BOTH);

            JPanel root = new GradPanel(new Color(232, 244, 255), new Color(215, 232, 255));
            root.setLayout(new BorderLayout());
            root.setBorder(new EmptyBorder(22, 30, 22, 30));
            root.add(appHdr("📚  Study Planner & Timer", new Color(30, 110, 200), new Color(40, 130, 215)), BorderLayout.NORTH);
            root.add(buildBody(),   BorderLayout.CENTER);
            root.add(buildFooter(), BorderLayout.SOUTH);
            setContentPane(root);
            countdownTimer = new javax.swing.Timer(1000, e -> tick());
            refreshSubjectList();
        }

        private JPanel buildBody() {
            JPanel p = new JPanel(new BorderLayout(16, 0)); p.setOpaque(false);

            // ── LEFT: subjects panel ──────────────────────────────────────────
            JPanel left = new JPanel(new BorderLayout(0, 12));
            left.setOpaque(false);
            left.setPreferredSize(new Dimension(340, 0));

            RoundCard addCard = new RoundCard(C_CARD);
            addCard.setLayout(new BoxLayout(addCard, BoxLayout.Y_AXIS));
            addCard.setBorder(new EmptyBorder(16, 18, 16, 18));
            addCard.add(lbl("📖  Add Subject", F_HEAD, new Color(30, 110, 200)));
            addCard.add(Box.createVerticalStrut(8));
            addCard.add(lbl("Subject Name:", F_BODY, C_TEXT));
            addCard.add(Box.createVerticalStrut(4));
            subjField = roundField();
            subjField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            subjField.setAlignmentX(LEFT_ALIGNMENT);
            addCard.add(subjField);
            addCard.add(Box.createVerticalStrut(8));

            colorBox = new JComboBox<>(SUBJ_COLOR_NAMES);
            colorBox.setFont(F_BODY); colorBox.setBackground(C_WHITE);
            // FIX: render each item in its actual color so user can see the color
            colorBox.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> l, Object v,
                        int i, boolean s, boolean f) {
                    JLabel lb = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
                    lb.setFont(F_BODY);
                    if (i >= 0 && i < SUBJ_COLORS.length) lb.setForeground(SUBJ_COLORS[i]);
                    else lb.setForeground(C_TEXT);
                    return lb;
                }
            });

            hoursSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
            hoursSpinner.setFont(F_BODY);
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); row.setOpaque(false);
            row.add(lbl("Color:", F_BODY, C_TEXT)); row.add(colorBox);
            row.add(lbl("  Target hrs:", F_BODY, C_TEXT)); row.add(hoursSpinner);
            JPanel rw = new JPanel(new BorderLayout()); rw.setOpaque(false);
            rw.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); rw.add(row);
            addCard.add(rw);
            addCard.add(Box.createVerticalStrut(6));
            addSubjBtn = pillBtn("Add Subject  ✚", new Color(40, 130, 215), Color.WHITE);
            addSubjBtn.setAlignmentX(LEFT_ALIGNMENT);
            addSubjBtn.addActionListener(this);
            addCard.add(addSubjBtn);

            RoundCard listCard = new RoundCard(C_CARD);
            listCard.setLayout(new BorderLayout(0, 8));
            listCard.setBorder(new EmptyBorder(14, 16, 14, 16));
            listCard.add(lbl("📋  My Subjects", F_HEAD, new Color(30, 110, 200)), BorderLayout.NORTH);

            listModel = new DefaultListModel<>();
            subjList  = new JList<>(listModel);
            subjList.setFont(new Font("SansSerif", Font.BOLD, 15));
            subjList.setBackground(new Color(243, 248, 255));
            subjList.setSelectionBackground(new Color(180, 215, 255));
            subjList.setSelectionForeground(new Color(10, 30, 80));
            subjList.setFixedCellHeight(42);
            subjList.setBorder(new EmptyBorder(4, 10, 4, 10));

            // FIX: cell renderer that colors text by subject's chosen color
            subjList.setCellRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> l, Object v,
                        int i, boolean s, boolean f) {
                    JLabel lb = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
                    lb.setFont(new Font("SansSerif", Font.BOLD, 15));
                    if (!s && i >= 0 && i < subjects.size()) {
                        // Use the subject's chosen color for its text
                        lb.setForeground(SUBJ_COLORS[subjects.get(i).colorIdx]);
                    } else if (s) {
                        lb.setForeground(new Color(10, 30, 80));
                    }
                    lb.setBorder(new EmptyBorder(4, 12, 4, 12));
                    return lb;
                }
            });

            JScrollPane sc = styledScroll(subjList, new Color(160, 200, 240));
            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            btnRow.setOpaque(false);
            deleteSubjBtn = pillBtn("🗑 Remove",      new Color(210, 60, 60),  Color.WHITE);
            addMinBtn     = pillBtn("➕ +30 min",     new Color(60, 150, 230), Color.WHITE);
            deleteSubjBtn.addActionListener(this);
            addMinBtn.addActionListener(this);
            btnRow.add(deleteSubjBtn); btnRow.add(addMinBtn);

            listCard.add(sc,     BorderLayout.CENTER);
            listCard.add(btnRow, BorderLayout.SOUTH);

            left.add(addCard,  BorderLayout.NORTH);
            left.add(listCard, BorderLayout.CENTER);

            // ── CENTRE: Pomodoro Timer ────────────────────────────────────────
            RoundCard timerCard = new RoundCard(C_CARD);
            timerCard.setLayout(new BoxLayout(timerCard, BoxLayout.Y_AXIS));
            timerCard.setBorder(new EmptyBorder(24, 36, 24, 36));

            JLabel timerTitle = lbl("🍅  Pomodoro Timer", F_HEAD, new Color(30, 110, 200));
            timerTitle.setAlignmentX(CENTER_ALIGNMENT);

            timerLabel = new JLabel("25:00", JLabel.CENTER);
            timerLabel.setFont(new Font("SansSerif", Font.BOLD, 86));
            timerLabel.setForeground(new Color(30, 110, 200));
            timerLabel.setAlignmentX(CENTER_ALIGNMENT);

            timerStatusLbl = lbl("Focus Time 🎯",
                    new Font("SansSerif", Font.BOLD, 18), new Color(30, 110, 200));
            timerStatusLbl.setAlignmentX(CENTER_ALIGNMENT);

            pomodoroCountLbl = lbl("🍅  Pomodoros completed: 0", F_BODY, C_TEXTDIM);
            pomodoroCountLbl.setAlignmentX(CENTER_ALIGNMENT);

            progressBar = new JProgressBar(0, workSecs);
            progressBar.setValue(workSecs);
            progressBar.setBorderPainted(true);
            progressBar.setBackground(new Color(215, 232, 255));
            progressBar.setForeground(new Color(40, 130, 215));
            progressBar.setStringPainted(true);
            progressBar.setString("Ready");
            progressBar.setFont(F_SMALL);
            progressBar.setPreferredSize(new Dimension(0, 22));
            progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
            progressBar.setAlignmentX(LEFT_ALIGNMENT);

            // ── FIX: Custom timer duration controls ───────────────────────────
            RoundCard settingsCard = new RoundCard(new Color(238, 246, 255));
            settingsCard.setLayout(new BoxLayout(settingsCard, BoxLayout.Y_AXIS));
            settingsCard.setBorder(new EmptyBorder(12, 16, 12, 16));
            settingsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            settingsCard.setAlignmentX(LEFT_ALIGNMENT);

            JLabel settingsTitle = lbl("⚙️  Timer Settings", F_BOLD, new Color(30, 110, 200));
            settingsTitle.setAlignmentX(LEFT_ALIGNMENT);

            workMinsSpinner  = new JSpinner(new SpinnerNumberModel(25, 1, 120, 1));
            breakMinsSpinner = new JSpinner(new SpinnerNumberModel(5,  1,  60, 1));
            workMinsSpinner.setFont(F_BODY);
            breakMinsSpinner.setFont(F_BODY);

            JPanel settingsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            settingsRow.setOpaque(false);
            settingsRow.add(lbl("Focus (min):", F_BODY, C_TEXT));
            settingsRow.add(workMinsSpinner);
            settingsRow.add(Box.createHorizontalStrut(16));
            settingsRow.add(lbl("Break (min):", F_BODY, C_TEXT));
            settingsRow.add(breakMinsSpinner);

            JButton applyBtn = pillBtn("Apply ✓", new Color(40, 130, 215), Color.WHITE);
            applyBtn.setFont(F_SMALL);
            applyBtn.addActionListener(e -> applyTimerSettings());
            settingsRow.add(applyBtn);

            JPanel srw = new JPanel(new BorderLayout()); srw.setOpaque(false);
            srw.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54)); srw.add(settingsRow);

            settingsCard.add(settingsTitle);
            settingsCard.add(Box.createVerticalStrut(4));
            settingsCard.add(srw);

            startBtn = pillBtn("▶  Start", new Color(40, 130, 215), Color.WHITE);
            startBtn.setFont(new Font("SansSerif", Font.BOLD, 17));
            startBtn.setMaximumSize(new Dimension(190, 48));
            startBtn.setAlignmentX(CENTER_ALIGNMENT);
            startBtn.addActionListener(this);

            resetBtn = pillBtn("↺  Reset", new Color(140, 160, 185), C_TEXT);
            resetBtn.setMaximumSize(new Dimension(150, 40));
            resetBtn.setAlignmentX(CENTER_ALIGNMENT);
            resetBtn.addActionListener(this);

            // Study tip
            RoundCard tipCard = new RoundCard(new Color(238, 246, 255));
            tipCard.setLayout(new BoxLayout(tipCard, BoxLayout.Y_AXIS));
            tipCard.setBorder(new EmptyBorder(12, 14, 12, 14));
            tipCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
            tipCard.setAlignmentX(LEFT_ALIGNMENT);
            String[] tips = {
                "💡 Take short breaks — your brain needs rest!",
                "🎯 One subject at a time improves focus.",
                "💧 Stay hydrated while studying.",
                "📵 Put your phone face-down during sessions.",
                "✏️ Handwritten notes improve memory retention.",
                "🎵 Calm music can help with concentration.",
                "🌙 Sleep is the best way to consolidate memory."
            };
            JLabel tipTitle = lbl("💡 Study Tip:", F_BOLD, new Color(30, 110, 200));
            tipTitle.setAlignmentX(LEFT_ALIGNMENT);
            JLabel tipTxt = lbl(tips[(int)(Math.random() * tips.length)], F_BODY, C_TEXT);
            tipTxt.setAlignmentX(LEFT_ALIGNMENT);
            tipCard.add(tipTitle);
            tipCard.add(Box.createVerticalStrut(4));
            tipCard.add(tipTxt);

            timerCard.add(timerTitle);
            timerCard.add(Box.createVerticalStrut(8));
            timerCard.add(timerLabel);
            timerCard.add(Box.createVerticalStrut(4));
            timerCard.add(timerStatusLbl);
            timerCard.add(Box.createVerticalStrut(4));
            timerCard.add(pomodoroCountLbl);
            timerCard.add(Box.createVerticalStrut(12));
            timerCard.add(progressBar);
            timerCard.add(Box.createVerticalStrut(10));
            timerCard.add(settingsCard);       
            timerCard.add(Box.createVerticalStrut(16));
            timerCard.add(startBtn);
            timerCard.add(Box.createVerticalStrut(8));
            timerCard.add(resetBtn);
            timerCard.add(Box.createVerticalGlue());
            timerCard.add(tipCard);

            p.add(left,      BorderLayout.WEST);
            p.add(timerCard, BorderLayout.CENTER);
            return p;
        }

        private JPanel buildFooter() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            p.setOpaque(false); p.setBorder(new EmptyBorder(12, 0, 0, 0));
            statusLabel = lbl("Add a subject and start your Pomodoro! 📚", F_BODY, C_TEXTDIM);
            p.add(statusLabel); return p;
        }

        public void actionPerformed(ActionEvent e) {
            Object s = e.getSource();
            if      (s == addSubjBtn)    addSubject();
            else if (s == deleteSubjBtn) deleteSubject();
            else if (s == addMinBtn)     addMinutes();
            else if (s == startBtn)      toggleTimer();
            else if (s == resetBtn)      resetTimer();
        }

        // FIX: apply the custom work/break durations
        private void applyTimerSettings() {
            if (timerRunning) { st("Stop the timer before changing settings!"); return; }
            workSecs  = (int) workMinsSpinner.getValue()  * 60;
            breakSecs = (int) breakMinsSpinner.getValue() * 60;
            secondsLeft = workSecs;
            isBreak = false;
            progressBar.setMaximum(workSecs);
            progressBar.setValue(workSecs);
            timerStatusLbl.setText("Focus Time 🎯");
            timerStatusLbl.setForeground(new Color(30, 110, 200));
            updateTimerDisplay();
            st("Timer set to " + workMinsSpinner.getValue() + " min focus / "
               + breakMinsSpinner.getValue() + " min break ✓");
        }

        private void addSubject() {
            String name = subjField.getText().trim();
            if (name.isEmpty()) { st("Enter a subject name!"); return; }
            int colorIdx = colorBox.getSelectedIndex();
            int hrs = (Integer) hoursSpinner.getValue();
            Subject subj = new Subject(name, colorIdx, hrs);
            subjects.add(subj);
            subjField.setText("");
            refreshSubjectList();
            st("Added: " + name + " ✓");
        }
        private void deleteSubject() {
            int i = subjList.getSelectedIndex();
            if (i < 0) { st("Select a subject!"); return; }
            if (confirm(this, "Remove: " + subjects.get(i).name + "?")) {

                subjects.remove(i); refreshSubjectList();
            }
        }
        private void addMinutes() {
            int i = subjList.getSelectedIndex();
            if (i < 0) { st("Select a subject to log time!"); return; }
            Subject subj = subjects.get(i);
            subj.studiedMins += 30;

            refreshSubjectList();
            st("Added 30 min to: " + subj.name + " ✓");
        }
        private void refreshSubjectList() {
            listModel.clear();
            for (Subject s : subjects) {
                double studied = s.studiedMins / 60.0;
                int pct = (int) Math.min(100, (studied / s.targetHours) * 100);
                // color name shown as prefix, then subject info
                String colorName = SUBJ_COLOR_NAMES[s.colorIdx].split(" ")[0]; // emoji only
                listModel.addElement(String.format("%s  %s  —  %.1f / %d hrs  [%d%%]",
                        colorName, s.name, studied, s.targetHours, pct));
            }
            // repaint so colors show immediately
            subjList.repaint();
        }
        private void toggleTimer() {
            if (timerRunning) {
                countdownTimer.stop(); timerRunning = false;
                startBtn.setText("▶  Continue");
                progressBar.setString("Paused");
            } else {
                countdownTimer.start(); timerRunning = true;
                startBtn.setText("⏸  Pause");
            }
        }
        private void resetTimer() {
            countdownTimer.stop(); timerRunning = false; isBreak = false;
            secondsLeft = workSecs;
            progressBar.setMaximum(workSecs);
            progressBar.setValue(workSecs);
            progressBar.setForeground(new Color(40, 130, 215));
            progressBar.setString("Ready");
            startBtn.setText("▶  Start");
            timerStatusLbl.setText("Focus Time 🎯");
            timerStatusLbl.setForeground(new Color(30, 110, 200));
            updateTimerDisplay();
        }
        private void tick() {
            secondsLeft--;
            if (secondsLeft <= 0) {
                countdownTimer.stop(); timerRunning = false;
                if (!isBreak) {
                    pomodorosDone++;
                    pomodoroCountLbl.setText("🍅  Pomodoros completed: " + pomodorosDone);
                    JOptionPane.showMessageDialog(this,
                        "🎉 Pomodoro done! Time for a " + (breakSecs/60) + "-min break.",
                        "Well done!", JOptionPane.INFORMATION_MESSAGE);
                    isBreak = true; secondsLeft = breakSecs;
                    timerStatusLbl.setText("Break Time ☕");
                    timerStatusLbl.setForeground(new Color(30, 160, 80));
                    progressBar.setForeground(new Color(30, 170, 90));
                    progressBar.setMaximum(breakSecs);
                    startBtn.setText("▶  Start Break");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Break over! Ready for next Pomodoro? 💪",
                        "Back to work!", JOptionPane.INFORMATION_MESSAGE);
                    isBreak = false; secondsLeft = workSecs;
                    timerStatusLbl.setText("Focus Time 🎯");
                    timerStatusLbl.setForeground(new Color(30, 110, 200));
                    progressBar.setForeground(new Color(40, 130, 215));
                    progressBar.setMaximum(workSecs);
                    startBtn.setText("▶  Start");
                }
            }
            updateTimerDisplay();
        }
        private void updateTimerDisplay() {
            timerLabel.setText(String.format("%02d:%02d", secondsLeft / 60, secondsLeft % 60));
            progressBar.setValue(secondsLeft);
            if (timerRunning) {
                int total = isBreak ? breakSecs : workSecs;
                double ratio = (double) secondsLeft / total;
                progressBar.setString(String.format("%02d:%02d remaining",
                        secondsLeft / 60, secondsLeft % 60));
                if (!isBreak)
                    progressBar.setForeground(ratio > 0.5 ? new Color(40,130,215)
                            : ratio > 0.25 ? new Color(200,150,20) : new Color(210,60,60));
            }
        }
        private void st(String m) { statusLabel.setText(m); }
    }

    static class TextEditor extends JFrame implements ActionListener {

        private JTextArea   textArea;
        private JLabel      statusLabel, wordCountLabel, lineColLabel, fileNameLabel;
        private File        currentFile = null;
        private boolean     modified    = false;
        private UndoManager undoManager = new UndoManager();
        private JSpinner    fontSizeSpinner;
        private JComboBox<String> fontFamilyBox;
        private boolean     darkMode    = false;

        private JMenuItem miNew, miOpen, miSave, miSaveAs, miExit;
        private JMenuItem miUndo, miRedo, miCut, miCopy, miPaste, miSelectAll, miFind, miDuplicate;
        private JMenuItem miWordWrap, miDarkMode;

        TextEditor() {
            setTitle("🗒 Text Editor");
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            setExtendedState(MAXIMIZED_BOTH);
            buildMenuBar();
            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(new Color(244, 240, 255));
            root.add(buildToolBar(),   BorderLayout.NORTH);
            root.add(buildEditor(),    BorderLayout.CENTER);
            root.add(buildStatusBar(), BorderLayout.SOUTH);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) { exitApp(); }
            });
            setContentPane(root);
        }

        private void buildMenuBar() {
            JMenuBar mb = new JMenuBar(); mb.setBackground(new Color(225, 215, 248));
            JMenu file = menu("📁 File"), edit = menu("✏️ Edit"), view = menu("👁 View");
            miNew=mi("New",KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK);
            miOpen=mi("Open...",KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK);
            miSave=mi("Save",KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK);
            miSaveAs=mi("Save As...",0,0);
            miExit=mi("Exit",KeyEvent.VK_Q,InputEvent.CTRL_DOWN_MASK);
            file.add(miNew);file.add(miOpen);file.addSeparator();
            file.add(miSave);file.add(miSaveAs);file.addSeparator();file.add(miExit);

            miUndo=mi("Undo",KeyEvent.VK_Z,InputEvent.CTRL_DOWN_MASK);
            miRedo=mi("Redo",KeyEvent.VK_Y,InputEvent.CTRL_DOWN_MASK);
            miCut=mi("Cut",KeyEvent.VK_X,InputEvent.CTRL_DOWN_MASK);
            miCopy=mi("Copy",KeyEvent.VK_C,InputEvent.CTRL_DOWN_MASK);
            miPaste=mi("Paste",KeyEvent.VK_V,InputEvent.CTRL_DOWN_MASK);
            miSelectAll=mi("Select All",KeyEvent.VK_A,InputEvent.CTRL_DOWN_MASK);
            miFind=mi("Find & Replace",KeyEvent.VK_F,InputEvent.CTRL_DOWN_MASK);
            miDuplicate=mi("Duplicate Line",KeyEvent.VK_D,InputEvent.CTRL_DOWN_MASK);
            edit.add(miUndo);edit.add(miRedo);edit.addSeparator();
            edit.add(miCut);edit.add(miCopy);edit.add(miPaste);edit.addSeparator();
            edit.add(miSelectAll);edit.add(miDuplicate);edit.addSeparator();edit.add(miFind);

            miWordWrap=new JCheckBoxMenuItem("Word Wrap");
            ((JCheckBoxMenuItem)miWordWrap).setSelected(true);
            miWordWrap.addActionListener(this);
            miDarkMode=new JCheckBoxMenuItem("Dark Mode 🌙");
            miDarkMode.addActionListener(this);
            view.add(miWordWrap);view.add(miDarkMode);
            mb.add(file);mb.add(edit);mb.add(view);
            setJMenuBar(mb);
        }

        private JPanel buildToolBar() {
            JPanel tb = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 7));
            tb.setBackground(new Color(228, 218, 252));
            tb.setBorder(new EmptyBorder(4, 14, 4, 14));
            tb.add(tbBtn("📄 New",    new Color(170, 140, 255), miNew));
            tb.add(tbBtn("📂 Open",   new Color(140, 170, 255), miOpen));
            tb.add(tbBtn("💾 Save",   new Color(120, 210, 160), miSave));
            tb.add(new JSeparator(JSeparator.VERTICAL));
            tb.add(tbBtn("↩ Undo",   new Color(185, 178, 210), miUndo));
            tb.add(tbBtn("↪ Redo",   new Color(185, 178, 210), miRedo));
            tb.add(new JSeparator(JSeparator.VERTICAL));
            tb.add(tbBtn("✂ Cut",    new Color(250, 145, 145), miCut));
            tb.add(tbBtn("📋 Copy",  new Color(250, 205, 95),  miCopy));
            tb.add(tbBtn("📌 Paste", new Color(250, 205, 95),  miPaste));
            tb.add(new JSeparator(JSeparator.VERTICAL));
            tb.add(tbBtn("🔍 Find",  new Color(180, 155, 250), miFind));
            tb.add(new JSeparator(JSeparator.VERTICAL));
            tb.add(lbl("Font:", F_SMALL, C_TEXT));
            fontFamilyBox = new JComboBox<>(new String[]{"Monospaced","SansSerif","Serif","Dialog"});
            fontFamilyBox.setFont(F_SMALL); fontFamilyBox.setBackground(C_WHITE);
            fontFamilyBox.addActionListener(e -> applyFont());
            tb.add(fontFamilyBox);
            tb.add(lbl(" Size:", F_SMALL, C_TEXT));
            fontSizeSpinner = new JSpinner(new SpinnerNumberModel(14, 8, 48, 1));
            fontSizeSpinner.setFont(F_SMALL);
            fontSizeSpinner.setPreferredSize(new Dimension(65, 28));
            fontSizeSpinner.addChangeListener(e -> applyFont());
            tb.add(fontSizeSpinner);
            JButton home = pillBtn("🏠 Home", new Color(140, 100, 210), Color.WHITE);
            home.setFont(F_SMALL);
            home.addActionListener(e -> { if (okToDiscard()) dispose(); });
            tb.add(home);
            return tb;
        }

        private JPanel buildEditor() {
            JPanel p = new JPanel(new BorderLayout());
            p.setOpaque(false); p.setBorder(new EmptyBorder(10, 22, 0, 22));
            fileNameLabel = lbl("New File", new Font("SansSerif",Font.BOLD,15), new Color(100, 60, 170));
            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false); top.setBorder(new EmptyBorder(0, 0, 6, 0));
            top.add(fileNameLabel, BorderLayout.WEST);

            textArea = new JTextArea();
            textArea.setFont(F_MONO);
            textArea.setLineWrap(true); textArea.setWrapStyleWord(true);
            textArea.setBackground(new Color(252, 249, 255));
            textArea.setForeground(new Color(25, 15, 45));       // very dark text
            textArea.setCaretColor(new Color(130, 80, 220));
            textArea.setBorder(new EmptyBorder(14, 16, 14, 16));
            textArea.setTabSize(4);
            textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
            textArea.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e)  { updateCount(); setMod(true); }
                public void removeUpdate(DocumentEvent e)  { updateCount(); setMod(true); }
                public void changedUpdate(DocumentEvent e) {}
            });
            textArea.addCaretListener(e -> updateLineCol());

            // Line number gutter
            JTextArea lineNums = new JTextArea("1");
            lineNums.setBackground(new Color(228, 218, 252));
            lineNums.setForeground(new Color(110, 80, 160));
            lineNums.setEditable(false); lineNums.setFont(F_MONO);
            lineNums.setBorder(new EmptyBorder(14, 8, 14, 8));
            textArea.getDocument().addDocumentListener(new DocumentListener() {
                void upd() { SwingUtilities.invokeLater(() -> {
                    int n = textArea.getLineCount();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i <= n; i++) { sb.append(i); if (i < n) sb.append("\n"); }
                    lineNums.setText(sb.toString());
                }); }
                public void insertUpdate(DocumentEvent e)  { upd(); }
                public void removeUpdate(DocumentEvent e)  { upd(); }
                public void changedUpdate(DocumentEvent e) {}
            });

            JScrollPane sc = new JScrollPane(textArea);
            sc.setRowHeaderView(lineNums);
            sc.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(new Color(180, 150, 230), 14),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
            sc.getViewport().setBackground(new Color(252, 249, 255));

            p.add(top, BorderLayout.NORTH);
            p.add(sc,  BorderLayout.CENTER);
            return p;
        }

        private JPanel buildStatusBar() {
            JPanel bar = new JPanel(new BorderLayout());
            bar.setBackground(new Color(218, 206, 248));
            bar.setBorder(new EmptyBorder(5, 16, 5, 16));
            statusLabel    = lbl("Ready ✨",       F_SMALL, new Color(70, 40, 120));
            wordCountLabel = lbl("Words: 0  |  Chars: 0", F_SMALL, new Color(70, 40, 120));
            lineColLabel   = lbl("Ln 1, Col 1",   F_SMALL, new Color(70, 40, 120));
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
            right.setOpaque(false);
            right.add(lineColLabel); right.add(wordCountLabel);
            bar.add(statusLabel, BorderLayout.WEST);
            bar.add(right,       BorderLayout.EAST);
            return bar;
        }

        public void actionPerformed(ActionEvent e) {
            Object s = e.getSource();
            if      (s==miNew)       newFile();
            else if (s==miOpen)      openFile();
            else if (s==miSave)      saveFile();
            else if (s==miSaveAs)    saveAs();
            else if (s==miExit)      exitApp();
            else if (s==miUndo)      { if(undoManager.canUndo()) undoManager.undo(); }
            else if (s==miRedo)      { if(undoManager.canRedo()) undoManager.redo(); }
            else if (s==miCut)       textArea.cut();
            else if (s==miCopy)      textArea.copy();
            else if (s==miPaste)     textArea.paste();
            else if (s==miSelectAll) textArea.selectAll();
            else if (s==miFind)      findReplace();
            else if (s==miDuplicate) duplicateLine();
            else if (s==miWordWrap)  { boolean w=((JCheckBoxMenuItem)s).isSelected();
                                       textArea.setLineWrap(w); textArea.setWrapStyleWord(w); }
            else if (s==miDarkMode)  toggleDark();
        }

        private void newFile() {
            if (!okToDiscard()) return;
            textArea.setText(""); currentFile = null; setMod(false);
            fileNameLabel.setText("New File"); st("New file 📄");
        }
        private void openFile() {
            if (!okToDiscard()) return;
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                currentFile = fc.getSelectedFile();
                try (BufferedReader br = new BufferedReader(new FileReader(currentFile))) {
                    textArea.read(br, null); setMod(false);
                    fileNameLabel.setText(currentFile.getName());
                    setTitle("🗒 " + currentFile.getName()); st("Opened ✓");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        private void saveFile() { if (currentFile == null) saveAs(); else write(currentFile); }
        private void saveAs() {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                currentFile = fc.getSelectedFile();
                if (!currentFile.getName().contains("."))
                    currentFile = new File(currentFile.getAbsolutePath() + ".txt");
                write(currentFile);
            }
        }
        private void write(File f) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                textArea.write(bw); setMod(false);
                fileNameLabel.setText(f.getName()); setTitle("🗒 " + f.getName()); st("Saved 💾");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        private void exitApp() { if (okToDiscard()) dispose(); }
        private boolean okToDiscard() {
            if (!modified) return true;
            int r = JOptionPane.showConfirmDialog(this, "Unsaved changes. Discard?",
                    "Unsaved", JOptionPane.YES_NO_CANCEL_OPTION);
            return r == JOptionPane.YES_OPTION;
        }
        private void duplicateLine() {
            try {
                int pos = textArea.getCaretPosition();
                int line = textArea.getLineOfOffset(pos);
                int start = textArea.getLineStartOffset(line);
                int end   = textArea.getLineEndOffset(line);
                String lineText = textArea.getText(start, end - start);
                textArea.insert(lineText, end);
            } catch (Exception ex) {}
        }
        private void applyFont() {
            String family = (String) fontFamilyBox.getSelectedItem();
            int size = (Integer) fontSizeSpinner.getValue();
            textArea.setFont(new Font(family, Font.PLAIN, size));
        }
        private void toggleDark() {
            darkMode = !darkMode;
            textArea.setBackground(darkMode ? new Color(28, 22, 42) : new Color(252, 249, 255));
            textArea.setForeground(darkMode ? new Color(225, 215, 245) : new Color(25, 15, 45));
            textArea.setCaretColor(darkMode ? new Color(200, 160, 255) : new Color(130, 80, 220));
            getContentPane().setBackground(darkMode ? new Color(22, 18, 35) : new Color(244, 240, 255));
            st(darkMode ? "Dark mode on 🌙" : "Light mode on ☀️");
        }
        private void findReplace() {
            JDialog dlg = new JDialog(this, "🔍 Find & Replace", false);
            dlg.setSize(440, 170); dlg.setLocationRelativeTo(this);
            dlg.setLayout(new GridLayout(3, 3, 8, 8));
            dlg.getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));
            JTextField ff = roundField(), rf = roundField();
            JButton fb = pillBtn("Find Next",    C_PURPLE,         Color.WHITE),
                    rb = pillBtn("Replace All",  new Color(30,150,90), Color.WHITE),
                    cb = pillBtn("Close",        new Color(160,150,175), Color.WHITE);
            dlg.add(lbl("Find:",    F_BODY, C_TEXT)); dlg.add(ff); dlg.add(fb);
            dlg.add(lbl("Replace:", F_BODY, C_TEXT)); dlg.add(rf); dlg.add(rb);
            dlg.add(new JLabel()); dlg.add(new JLabel()); dlg.add(cb);
            fb.addActionListener(ev -> {
                String f = ff.getText(); if (f.isEmpty()) return;
                String c = textArea.getText();
                int idx = c.indexOf(f, textArea.getCaretPosition());
                if (idx == -1) idx = c.indexOf(f);
                if (idx != -1) { textArea.setSelectionStart(idx); textArea.setSelectionEnd(idx + f.length()); }
                else JOptionPane.showMessageDialog(dlg, "Not found 🔍");
            });
            rb.addActionListener(ev -> {
                String f = ff.getText(), r = rf.getText();
                if (!f.isEmpty()) {
                    int cnt = textArea.getText().split(f, -1).length - 1;
                    textArea.setText(textArea.getText().replace(f, r));
                    st("Replaced " + cnt + " occurrence(s).");
                }
            });
            cb.addActionListener(ev -> dlg.dispose());
            dlg.setVisible(true);
        }
        private void setMod(boolean m) {
            modified = m;
            String base = currentFile == null ? "New File" : currentFile.getName();
            setTitle("🗒 " + (m ? "● " : "") + base);
            fileNameLabel.setText((m ? "● " : "") + base);
        }
        private void updateCount() {
            String t = textArea.getText().trim();
            wordCountLabel.setText("Words: " + (t.isEmpty() ? 0 : t.split("\\s+").length)
                    + "  |  Chars: " + textArea.getText().length());
        }
        private void updateLineCol() {
            try {
                int pos  = textArea.getCaretPosition();
                int line = textArea.getLineOfOffset(pos) + 1;
                int col  = pos - textArea.getLineStartOffset(line - 1) + 1;
                lineColLabel.setText("Ln " + line + ", Col " + col);
            } catch (Exception ex) {}
        }
        private void st(String m) { statusLabel.setText(m); }
        private JMenuItem mi(String n, int k, int m) {
            JMenuItem mi = new JMenuItem(n); mi.setFont(F_BODY);
            if (k != 0) mi.setAccelerator(KeyStroke.getKeyStroke(k, m));
            mi.addActionListener(this); return mi;
        }
        private JMenu menu(String n) { JMenu m = new JMenu(n); m.setFont(F_BODY); return m; }
        private JButton tbBtn(String label, Color bg, JMenuItem linked) {
            JButton b = pillBtn(label, bg, C_TEXT);
            b.setFont(new Font("SansSerif", Font.PLAIN, 12));
            b.addActionListener(e -> linked.doClick()); return b;
        }
    }

    

    /** Gradient background with subtle blobs */
    static class GradPanel extends JPanel {
        private final Color from, to;
        GradPanel(Color f, Color t) { from = f; to = t; setOpaque(true); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, from, getWidth(), getHeight(), to));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
            g2.setColor(C_PINK);
            g2.fillOval(-50, -50, 220, 220);
            g2.setColor(new Color(197, 168, 255));
            g2.fillOval(getWidth() - 170, -30, 220, 220);
            g2.setColor(new Color(152, 224, 196));
            g2.fillOval(getWidth() / 2 - 80, getHeight() - 160, 190, 190);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    /** White rounded card with shadow */
    static class RoundCard extends JPanel {
        private final Color bg;
        RoundCard(Color bg) { this.bg = bg; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(3, 3, getWidth()-3, getHeight()-3, 20, 20);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 20, 20);
            // Visible card border
            g2.setColor(C_BORDER);
            g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 20, 20);
        }
    }

    /** Rounded border */
    static class RoundBorder extends AbstractBorder {
        private final Color color; private final int r;
        RoundBorder(Color c, int r) { color = c; this.r = r; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.drawRoundRect(x, y, w - 1, h - 1, r, r);
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(4, 8, 4, 8); }
    }

    /** Pill-shaped button */
    static JButton pillBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                super.paintComponent(g);
            }
        };
        b.setFont(F_BOLD); b.setForeground(fg);
        b.setOpaque(false); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        return b;
    }

    /** Rounded text input field */
    static JTextField roundField() {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        f.setFont(F_BODY);
        f.setForeground(C_TEXT);          
        f.setOpaque(false);
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(C_BORDER, 12),
                new EmptyBorder(6, 12, 6, 12)));
        return f;
    }

    /** Styled scroll pane with visible border */
    static JScrollPane styledScroll(JComponent comp, Color border) {
        JScrollPane sc = new JScrollPane(comp);
        sc.setBorder(BorderFactory.createLineBorder(border, 2));
        return sc;
    }

    /** Shortcut label factory */
    static JLabel lbl(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }

    /** Large stat number label */
    static JLabel bigStatLbl(String t) {
        JLabel l = new JLabel(t, JLabel.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 38));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    /** Adds a big stat number + label to a panel */
    static void addStat(JPanel p, JLabel numLbl, String text, Color color) {
        numLbl.setForeground(color);
        numLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = lbl(text, F_SMALL, C_TEXT);   // dark label — visible
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(numLbl); p.add(sub);
    }

    /** Combo box factory */
    static JComboBox<String> combo(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(F_BODY);
        c.setBackground(C_WHITE);
        c.setForeground(C_TEXT);
        return c;
    }

    /** App header panel with title + back-to-home button */
    static JPanel appHdr(String title, Color titleColor, Color btnColor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false); p.setBorder(new EmptyBorder(0, 0, 16, 0));
        p.add(lbl(title, F_TITLE, titleColor), BorderLayout.WEST);
        JButton home = pillBtn("🏠  Home", btnColor, Color.WHITE);
        home.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(p);
            if (w != null) w.dispose();
        });
        p.add(home, BorderLayout.EAST);
        return p;
    }

    /** Confirm dialog helper */
    static boolean confirm(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg, "Sure?",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
