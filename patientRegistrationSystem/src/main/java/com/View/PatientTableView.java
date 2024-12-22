package com.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.Model.JsonHandler;
import com.Model.Patient;

public class PatientTableView extends JPanel {
    JTable table;
    public DefaultTableModel tableModel;
    List<Patient> allPatients;
    private JsonHandler jsonHandler;

    public PatientTableView(List<Patient> patients, PatientRegistrationView view) {
        jsonHandler = new JsonHandler();
        allPatients = patients;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(0, 150, 139));
        JTextField mrdSearch = new JTextField(10);
        JTextField nameSearch = new JTextField(10);
        JTextField phoneSearch = new JTextField(10);

        ImageIcon orginalIcon = new ImageIcon("searchicon.jpg");
        Image resizedImage = orginalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JButton searchButton = new JButton(resizedIcon);
        searchButton.setBorder(BorderFactory.createEmptyBorder());
        JButton goBackButton = new JButton("Go Back");
        goBackButton.setBackground(Color.WHITE);
        JButton showallButton = new JButton("ShowAll Patients");
        showallButton.setBackground(Color.white);

        JLabel mrdLabel = new JLabel("Search by MRD:");
        mrdLabel.setForeground(Color.white);
        searchPanel.add(mrdLabel);
        searchPanel.add(mrdSearch);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.white);
        searchPanel.add(nameLabel);
        searchPanel.add(nameSearch);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.white);
        searchPanel.add(phoneLabel);
        searchPanel.add(phoneSearch);
        searchPanel.add(searchButton);
        searchPanel.add(goBackButton);
        searchPanel.add(showallButton);

        String[] columns = {"MRD ID", "Patient Name", "DOB", "Age", "Sex", "BloodGroup", "Email", "Phone", "Address", "EmergencyContactName", "EmergencyContactNumber", "Update"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 11; // Only "Update" column is editable
            }
        };

        table.getColumn("Update").setCellRenderer(new ButtonRenderer());
        table.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), this, view));
        table.getColumn("Update").setMaxWidth(20);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.white);

        searchButton.addActionListener(e -> {
            String mrd = mrdSearch.getText().trim();
            String name = nameSearch.getText().trim().toLowerCase();
            String phone = phoneSearch.getText().trim();

            List<Patient> filtered = allPatients.stream()
                    .filter(p -> (mrd.isEmpty() || String.valueOf(p.getMrdID()).equals(mrd))
                            && (name.isEmpty() || (p.getFirstName() + " " + p.getLastName()).toLowerCase().contains(name))
                            && (phone.isEmpty() || String.valueOf(p.getPhoneNumber()).contains(phone)))
                    .toList();
            updateTable(filtered);
        });

        goBackButton.addActionListener(e -> {
            // Handle go back action
        });

        showallButton.addActionListener(e -> {
            updateTable(allPatients);
            mrdSearch.setText("");
            nameSearch.setText("");
            phoneSearch.setText("");
        });

        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        updateTable(allPatients);
    }

    public void updateTable(List<Patient> patients) {
        tableModel.setRowCount(0);
        for (Patient p : patients) {
            String ageText;
            if (p.getAgeYears() == 0) {
                ageText = p.getAgeMonths() + " Months";
                tableModel.addRow(new Object[]{
                        p.getMrdID(), p.getFirstName() + " " + p.getLastName(), p.getDob(), ageText,
                        p.getSex(), p.getBloodGroup(), p.getEmail(), p.getPhoneNumber(), p.getAddress(), p.getEmergencyContactName(), p.getEmergencyContactNumber() == 0 ? "" : p.getEmergencyContactNumber(), "Edit"
                });
            } else {
                ageText = p.getAgeYears() + " years " + p.getAgeMonths() + " Months";
                tableModel.addRow(new Object[]{
                        p.getMrdID(), p.getFirstName() + " " + p.getLastName(), p.getDob(), ageText,
                        p.getSex(), p.getBloodGroup(), p.getEmail(), p.getPhoneNumber(), p.getAddress(), p.getEmergencyContactName(), p.getEmergencyContactNumber() == 0 ? "" : p.getEmergencyContactNumber(), "Edit"
                });
            }
        }
    }
}