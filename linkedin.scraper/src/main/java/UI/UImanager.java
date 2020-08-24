package UI;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.commons.configuration2.PropertiesConfiguration;

import logger.LogManager;
import scraper.Scraper;
import utils.ConfigManager;

public class UImanager {

	public static JButton btnStart;
	public static JButton btnStop;
	public static JButton accountConfigBtn;

	public static JLabel emailField;
	public static JLabel loginStatusField;

	public JFrame frame;

	public static JTextArea linkField;

	public static JTextPane logwindow;
	private Thread scraperThread = null;
	private static AtomicBoolean scraperThreadStatus = new AtomicBoolean(false); // dynamic bool - value can be
																				 // get/changed on runtime.

	public static void main(String[] args) {

		UImanager window = new UImanager();
		window.launch();

	}

	public void launch() {

		try {

			frame = new JFrame("LinkedIn Scraper");

			// Scraper btn panel
			JPanel btnPanel = new JPanel();
			btnPanel.setBounds(3, 5, 205, 200);
			btnPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			btnPanel.add(new JSeparator(SwingConstants.VERTICAL));
			btnPanel.setLayout(null);

			// Link text label
			JLabel linkLabel = new JLabel("Add linkedIn post URL below ");
			linkLabel.setBounds(25, 25, 180, 25);
			frame.add(linkLabel);

			// Link text field
			linkField = new JTextArea("", 190, 80);
			linkField.setWrapStyleWord(true);
			linkField.setBounds(10, 55, 190, 80);
			frame.add(linkField);

			// start button
			btnStart = new JButton("Scrap");
			btnStart.setFocusPainted(false);
			btnStart.setContentAreaFilled(false);
			btnStart.setBounds(10, 150, 90, 30);

			btnPanel.add(btnStart);

			btnStart.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getConfigProperty("isCredentialsUpdated").equals("true")) {
						btnStart.setEnabled(false);
						btnStop.setEnabled(true);
						startScraper();
					} else {
						showAlertMessage(frame, "Please Update Account Credentials.");
					}
				}
			});

			// stop button
			btnStop = new JButton("Stop");
			btnStop.setFocusPainted(false);
			btnStop.setContentAreaFilled(false);
			btnStop.setBounds(104, 150, 90, 30);
			btnStop.setEnabled(false);

			btnPanel.add(btnStop);

			btnStop.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					stopScraper(); // this will set thread flag to false.
					if (scraperThread.getState() != State.TIMED_WAITING) {
						LogManager.logError("Scraper will stop after completing current operations.");
					}
				}
			});

			frame.add(btnPanel);

			// LinkedIn Account Configuration panel
			JPanel accountPanel = new JPanel();
			accountPanel.setBounds(210, 5, 272, 200);
			accountPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			accountPanel.setLayout(null);

			// Email Label

			JLabel emailLabel = new JLabel("Login Email: ");
			emailLabel.setBounds(10, 60, 80, 15);
			accountPanel.add(emailLabel);

			// Email Field

			emailField = new JLabel(getConfigProperty("email"));
			emailField.setBounds(90, 60, 150, 15);
			emailField.setForeground(Color.blue);
			accountPanel.add(emailField);

			// Login Status label
			JLabel loginStatusLabel = new JLabel("Login Status: ");
			loginStatusLabel.setBounds(10, 80, 80, 15);
			accountPanel.add(loginStatusLabel);

			// Login Status Field
			loginStatusField = new JLabel("Logged out");
			loginStatusField.setBounds(95, 80, 80, 15);
			loginStatusField.setForeground(Color.red);
			accountPanel.add(loginStatusField);

			// LinkedIn Account Configuration button
			accountConfigBtn = new JButton("Config Account");
			accountConfigBtn.setBounds(80, 130, 120, 20);
			accountPanel.add(accountConfigBtn);

			accountConfigBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					AccountConfig configWindow = new AccountConfig();
					UImanager.accountConfigBtn.setEnabled(false);
					configWindow.show();
				}
			});

			frame.add(accountPanel);

			// Log panel
			logwindow = new JTextPane();
			JScrollPane logPanel = new JScrollPane(logwindow);
			logPanel.setBounds(4, 210, 475, 245);
			logPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

			logwindow.setBounds(5, 5, 465, 235);
			frame.add(logPanel);

			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setSize(500, 500);
			frame.setLayout(null);
			frame.setLocation(10, 10);
			frame.setResizable(false);
			frame.setVisible(true);
			LogManager.logInfo("Start User Interface..........................................[Success]");

			// below method will execute when on close button click.
			frame.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					Scraper.shutDownChrome();
					frame.dispose();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			LogManager.logError("Failed To Start User Interface");
		}

	}

	private boolean startScraper() {

		// creating scraper thread
		scraperThread = new Thread() {
			public void run() {
				scraperThreadStatus.set(true);
				while (scraperThreadStatus.get()) {
					Scraper s = new Scraper();
					try {
						s.startScraper();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (scraperThreadStatus.get() == false && btnStop.isEnabled()) {
					// below statements will execute when thread status is set to false
					LogManager.logAlert(
							"                        -------------------- Scraper Stopped. ------------------\n\n");
					UImanager.btnStart.setEnabled(true);
					UImanager.btnStop.setEnabled(false);
				}

			}
		};
		scraperThread.start();
		return true;
	}

	private boolean stopScraper() {
		scraperThreadStatus.set(false);
		if (scraperThread.getState() == State.TIMED_WAITING) {
			scraperThread.interrupt();
		}
		return true;
	}

	public static void appendToPane(String msg, Color c) {

		Document doc = logwindow.getDocument();

		// Define style
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, c);

		try {
			doc.insertString(doc.getLength(), msg, keyWord);

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean getScraperThreadStatus() {
		return scraperThreadStatus.get();
	}

	public static void setScraperThreadStatus(boolean status) {
		scraperThreadStatus.set(status);
	}

	public static void showAlertMessage(JFrame parentFrame, String msg) {
		JOptionPane.showMessageDialog(parentFrame, msg);
	}

	public static String getConfigProperty(String propertyName) {
		ConfigManager c = new ConfigManager();
		return c.getProperty(propertyName);

	}

	public static void refreshAccountConfigDetails() {
		emailField.setText(getConfigProperty("email"));
		accountConfigBtn.setEnabled(true);
//		System.out.print(getConfigProperty("email") + " " + getConfigProperty("password"));
	}

	public static String getLinkedInPostLink() {
		return linkField.getText();
	}
	
	public static void updateLoginStatus(String status) {
		if(status.equals("LoggedIn")) {
			loginStatusField.setForeground(new Color(6, 138, 6));
			loginStatusField.setText("Logged In");
		}else if(status.equals("LoggedOut")) {
			loginStatusField.setForeground(Color.red);
			loginStatusField.setText("Logged Out");
		}
	}
}
