package UI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import scraper.Scraper;
import utils.ConfigManager;

public class AccountConfig {

	public static JFrame configWindow;
	public static JTextField emailField;
	public static JPasswordField passwordField;

	public AccountConfig() {
		configWindow = new JFrame();
	}

	public void show() {

		// Email Label
		JLabel emailLabel = new JLabel("LinkedIn Email : ");
		emailLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		emailLabel.setBounds(30, 15, 150, 30);
		configWindow.add(emailLabel);

		// Password Label
		JLabel passwordlLabel = new JLabel("LinkedIn Password : ");
		passwordlLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		passwordlLabel.setBounds(30, 50, 150, 30);
		configWindow.add(passwordlLabel);

		// Email Field
		emailField = new JTextField();
		emailField.setText(new ConfigManager().getProperty("email"));
		emailField.setBounds(180, 20, 160, 25);
		configWindow.add(emailField);

		// Password Field
		passwordField = new JPasswordField();
		passwordField.setBounds(180, 55, 160, 25);
		configWindow.add(passwordField);

		// Update Button
		JButton updateBtn = new JButton("Update");
		updateBtn.setBounds(150, 105, 80, 25);
		configWindow.add(updateBtn);

		updateBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				String email = emailField.getText().toString();
				String password = new String(passwordField.getPassword());
				boolean isCrdentialUpdated = updateAccountCredentials(email, password);

				if (isCrdentialUpdated) {
					UImanager.showAlertMessage(configWindow, "Credentials Updated Successfully.");
					UImanager.refreshAccountConfigDetails();
					configWindow.dispose();

				} else {
					UImanager.showAlertMessage(configWindow, "Error Updating Credentials.");
				}

			}
		});

		configWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		configWindow.setSize(400, 200);
		configWindow.setLayout(null);
		configWindow.setLocation(500, 10);
		configWindow.setResizable(false);
		configWindow.setVisible(true);

		// below method will execute when on close button click.
		configWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				configWindow.dispose();
			}
		});
	}

	private boolean updateAccountCredentials(String useremail, String userpassword) {
		boolean isNewPropertyAdded = false;
		try {
			ConfigManager c = new ConfigManager();
			isNewPropertyAdded = c.setProperties(useremail, userpassword);
			return isNewPropertyAdded;
		} catch (Exception e) {
			e.printStackTrace();
			return isNewPropertyAdded;
		}
	}

	public static boolean isAlreadyOpen() {
		return configWindow.isVisible();
	}

}
