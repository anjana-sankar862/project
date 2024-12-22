package com.hospitalInformationSystem.patientRegistrationSystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.Controller.PatientRegistrationController;
import com.Model.Patient;
import com.View.PatientRegistrationView;

public class WelcomePage extends JFrame{
	static JPanel  topPanel;
	static CardLayout cardLayout;
	public WelcomePage() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Patient Registration");
		menuBar.add(menu);
		JMenuItem viewPatients = new JMenuItem("View Patients");
		menu.add(viewPatients);
		setLayout(new BorderLayout());
		
		cardLayout = new CardLayout();
//		App app = new App();
		PatientRegistrationView patientRegistrationView = new PatientRegistrationView();
		
		topPanel = new JPanel();
		topPanel.setLayout(cardLayout);

		topPanel.add(patientRegistrationView);
		cardLayout.show(topPanel, "Hello");
		add(topPanel,BorderLayout.CENTER);
		setExtendedState(MAXIMIZED_BOTH);
		
		setJMenuBar(menuBar);
		
		viewPatients.addActionListener(new ActionListener() {
			PatientRegistrationController controller = new PatientRegistrationController(patientRegistrationView);

			
			@Override
			public void actionPerformed(ActionEvent e) {
				patientRegistrationView.registerButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							String firstName = patientRegistrationView.firstNameField.getText();
							String lastName = patientRegistrationView.lastNameField.getText();
							
							Date dobDate = patientRegistrationView.dobChooser.getDate();
							String sex = (String) patientRegistrationView.sexCombo.getSelectedItem();
							String maritalStatus = (String) patientRegistrationView.maritalStatusCombo.getSelectedItem();
							String addressLine1 = patientRegistrationView.addressLine1Field.getText();
							String addressLine2 = patientRegistrationView.addressLine2Field.getText();
							String addressLine3 = patientRegistrationView.addressLine3Field.getText();
							String pincode = patientRegistrationView.pincodeField.getText();
							String state = (String) patientRegistrationView.stateComboBox.getSelectedItem();
							String fullAddress = String.join(", ", addressLine1, addressLine2, addressLine3, pincode, state).trim();
							String email = patientRegistrationView.emailField.getText();
							String bloodGroup = (String) patientRegistrationView.bloodGroupCombo.getSelectedItem();
							String contactText = patientRegistrationView.contactField.getText().trim();
							long contactNumber = Long.parseLong(contactText);
							String emergencyContactName = patientRegistrationView.emergencyContactNameField.getText();
							String emergencyContactText = patientRegistrationView.emergencyContactNumberField.getText().trim();
							long emergencyContactNumber=0 ;
							LocalDate dob= dobDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

							// Validation checks
							try {
								if(firstName.isEmpty() || dobDate == null || addressLine1.isEmpty() || pincode.isEmpty() || state.isEmpty()
										|| email.isEmpty() || contactText.isEmpty() || pincode.isEmpty() || state.isEmpty()) {
									System.out.println("FIrst name empty");
									JOptionPane.showMessageDialog(new JFrame(),"All fields are mandatory!");
									return;
								}
							
							if (!firstName.matches("^[A-Za-z]+$")) {
								JOptionPane.showMessageDialog(new JFrame(), "Name fields must contain letters only");
								return;
							}
							dob = dobDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
							if (dob.isAfter(LocalDate.now())) {
								JOptionPane.showMessageDialog(new JFrame(), "Invalid date of birth");
								return;
							}
							if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
								JOptionPane.showMessageDialog(new JFrame(), "Please enter a valid email!");
								return;
							}
							if (!contactText.matches("^[6-9][0-9]{9}$")) {
								JOptionPane.showMessageDialog(new JFrame(), "Invalid phone number format!");
								return;
							}
							if (!pincode.matches("\\d{6}")) {
								JOptionPane.showMessageDialog(new JFrame(), "Invalid pincode");
								return;
							}
							if (!emergencyContactText.isEmpty()) {
								emergencyContactNumber=Long.parseLong(emergencyContactText);
								}
								
							}catch(Exception e1){
								JOptionPane.showMessageDialog(new JFrame(), "Error");
							}
								//emergencyContactNumber = Long.parseLong(emergencyContactText);
							
							// Calculate age

							LocalDate currentDate = LocalDate.now();
							Period agePeriod = Period.between(dob, currentDate);
							String ageText;
							int years = agePeriod.getYears();
							int months = agePeriod.getMonths();
							if (dob.getYear() == currentDate.getYear()) {
								// Age in months only if DOB year matches current year
								ageText = months + " Months";
								years = 0; // Set years to 0
							} else {
								// Age in years and months
								ageText = years + " Years, " + months + " Months";
							}
							// Display age in the UI
							patientRegistrationView.ageField.setText(ageText);
							// Create Patient object
							Patient patient = new Patient(firstName,lastName, dob, years, months, sex, maritalStatus, bloodGroup, email,
									contactNumber, fullAddress, pincode, state, emergencyContactName, emergencyContactText.isEmpty()?0:emergencyContactNumber);
							// Save to JSON File
							List<Patient> patients = controllwer.jsonHandler.readPatients();
							patients.add(patient);
							controller.jsonHandler.savePatients(patients);
							JOptionPane.showMessageDialog(new JFrame(), "Patient registered successfully!");
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(new JFrame(), "All fields are mandatory");
							return;
						}
						// Reset form fields
						patientRegistrationView.resetFields();						
					}
				});
//				PatientRegistrationView view = new PatientRegistrationView();
//				PatientRegistrationController controller = new PatientRegistrationController(patientRegistrationView);
				JPanel viewPatientPanel = controller.viewPatients();
				
				topPanel.add(viewPatientPanel,"ViewPatients");
				cardLayout.show(topPanel, "ViewPatients");
				//add(topPanel,BorderLayout.CENTER);

			}
		});
		setVisible(true);

		
	}
	public static void main(String[] args) {
		
		new WelcomePage();
	}
	public static void setUpdatePanel(PatientRegistrationView patientRegistrationView) {
		topPanel.add(patientRegistrationView,"Hii");
		cardLayout.show(topPanel, "Hii");
	}

}
