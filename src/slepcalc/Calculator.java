/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slepcalc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author Yannik Bramkamp
 */
public class Calculator extends javax.swing.JFrame {

    /**
     * Data instance, contains mission data
     */
    private Data chapter_data;
    /**
     * Stores missions sorted by exp provided
     */
    private List<Integer> sortedList;
    /**
     * Stores possible combinations
     */
    private Map<Integer, String> options;

    private javax.swing.JLabel max_exp;
    private javax.swing.JTextField max_exp_input;
    private javax.swing.JButton submit;
    private javax.swing.JLabel current_exp;
    private javax.swing.JTextField current_exp_input;
    private javax.swing.JLabel current_ap;
    private javax.swing.JTextField current_ap_input;
    private javax.swing.JTextPane display;
    private javax.swing.JScrollPane display_scroll;

    /**
     * Initializes a calculator
     */
    public Calculator() {
        this.chapter_data = new Data();
        //sort missions
        sortedList = new ArrayList<Integer>(chapter_data.getExp_cost().keySet());
        Comparator c = Collections.reverseOrder();
        Collections.sort(sortedList, c);
        initComponents();

    }
    /**
     * initializes user interface
     */
    private void initComponents() {
        max_exp_input = new javax.swing.JTextField("0");
        //add focus listeners to automatically clear inputs when selected
        max_exp_input.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                max_exp_input.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        max_exp = new javax.swing.JLabel("Maximum exp");
        submit = new javax.swing.JButton("Calculate");
        current_exp_input = new javax.swing.JTextField("0");
        current_exp_input.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                current_exp_input.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        current_exp = new javax.swing.JLabel("Current exp");
        current_ap_input = new javax.swing.JTextField("0");
        current_ap_input.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                current_ap_input.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        current_ap = new javax.swing.JLabel("Current AP");
        display = new JTextPane();
        display_scroll = new JScrollPane(display);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SlepCalc");
        this.setLayout(new GridBagLayout());
        // Actionlistener for the buttin, basic check for non-int inputs
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    int xp_needed = Integer.valueOf(max_exp_input.getText()) - Integer.valueOf(current_exp_input.getText());
                    display.setText(guesstimate(Integer.valueOf(current_ap_input.getText()), xp_needed));
                } catch (Exception e) {
                    display.setText("Check input");
                }
            }
        });
        //layouting
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.getContentPane().add(max_exp, constraints);
        constraints.weightx = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.getContentPane().add(max_exp_input, constraints);
        constraints.weightx = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.getContentPane().add(current_exp, constraints);
        constraints.weightx = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 1;
        this.getContentPane().add(current_exp_input, constraints);
        constraints.weightx = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 2;
        this.getContentPane().add(current_ap, constraints);
        constraints.weightx = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 2;
        this.getContentPane().add(current_ap_input, constraints);
        constraints.weightx = 0.3;
        constraints.gridx = 0;
        constraints.gridy = 3;
        this.getContentPane().add(submit, constraints);
        constraints.weightx = 0.0;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.ipady = 300;
        this.getContentPane().add(display_scroll, constraints);
        this.pack();
        this.setSize(400, 450);
        this.setVisible(true);
    }

    /**
     * Wrapper for the calculation, takes care of output
     * @param ap_left How much AP the player has available
     * @param exp_needed Experience needed to level up
     * @return Formatted output String
     */
    public String guesstimate(int ap_left, int exp_needed) {
        options = new HashMap(); //set up hashmap for possible combinations
        String missions = algo(exp_needed, ap_left, ""); // calculate combinations
        if (missions.substring(0, 3).equals("err")) { // Not enough AP was available, handle output
            //output has form "err;missing experience;missions you can do for most xp"
            System.out.println(missions);
            String[] results = missions.split(":");
            if (results.length == 3) {
                String output = "You do not have enough AP, you will be short " + results[1] + " experience";
                output += "\nYour optimal route with the AP available is: \n";
                String[] submissions = results[2].split(";"); // Split act and chapter
                int tally = 0; // sum up total AP used
                for (String i : submissions) {
                    String[] entries = i.split("#");
                    if (!entries[0].isEmpty() && entries[0].length() == 2) {
                        output += "Act " + entries[0].substring(0, 1) + " chapter " + entries[0].substring(1) + "; " + entries[1] + " AP\n";
                        tally += Integer.valueOf(entries[1]);
                    }
                }
                return output;
            }
        } else { //We did have enough AP to level, output format is "exp over cap:mission1#cost1;mission2#cost2"
            String[] results = missions.split(":");
            if (results.length == 2) {
                String output = "Your optimal route with the AP available is: \n";
                String[] submissions = results[1].split(";");
                int tally = 0; // sum up total AP used
                for (String i : submissions) {
                    String[] entries = i.split("#");
                    if (!entries[0].isEmpty() && entries[0].length() == 2) {
                        output += "Act " + entries[0].substring(0, 1) + " chapter " + entries[0].substring(1) + "; " + entries[1] + " AP\n";
                        tally += Integer.valueOf(entries[1]);
                    }
                }
                output += "This will put you " + Integer.toString(Math.abs(Integer.valueOf(results[0]))) + " exp over the level cap\n";
                output += "You have " + String.valueOf(ap_left - tally) + " AP surplus";
                return output;
            }
        }

        return "";
    }

    /**
     * Algorithm to calculate mission combinations. optimizes for least xp over cap
     * The algorithm will in each step iterate over available missions, 
     * starting with the one that gives the highest experience, which also largely 
     * coincides with experience efficiency, i.e. AP/exp.
     * In essence the algorithm calculates paths in a tree, the leaves are the 
     * currently needed exp, the branches are the missions. Once we reach a leaf
     * labeled with 0 or a negative integer we have leveled up, the absolute of the integer
     * indicating how much we went over the level cap.
     * Of these possible solutions the one that is closest to the levelcap while leveling up is chosen.
     * @param exp_needed Experience needed to level.
     * @param ap_avail AP the player has
     * @param result String to store result sequence
     * @return 
     */
    public String algo(int exp_needed, int ap_avail, String result) {
        //iterate over all missions, start with the one that gives most xp
        for (int exp : sortedList) {
            if (options.containsKey(0)) { // If we have an optimal solution we want to stop
                break;
            }
            int cost = chapter_data.getCost(exp);
            //Cehck if current mission can be done with remaining AP
            if (cost <= ap_avail) {
                int leftover_exp = exp_needed - exp;
                int leftover_ap = ap_avail - cost;
                if (leftover_exp == 0) { //Peerfect solution, add to results and break
                    options.putIfAbsent(leftover_exp, result + ";" + chapter_data.getMission(exp) + "#" + String.valueOf(cost));
                    break;
                } else if (leftover_exp > 0) { //Still need mroe exp, continue if we have AP
                    String return_value = algo(leftover_exp, leftover_ap, result + ";" + chapter_data.getMission(exp) + "#" + String.valueOf(cost));
                    if (return_value.substring(0, 3).equals("err")) {
                        return return_value;
                    }
                } else {
                    options.putIfAbsent(leftover_exp, result + ";" + chapter_data.getMission(exp) + "#" + String.valueOf(cost));
                }
            }
        }
        try {
            int key = Collections.max(options.keySet());
            String fin_result = Integer.toString(key) + ":" + options.get(key);
            return fin_result;
        } catch (Exception e) {
            return "err:" + Integer.toString(exp_needed) + ":" + result;
        }

    }

}
