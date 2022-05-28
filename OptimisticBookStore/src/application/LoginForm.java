package application;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import connection.DatabaseConnection;
import transaction.MainMenu;
import user.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;


public class LoginForm extends JFrame implements ActionListener {
	JLabel titleLbl;
	JLabel usernameLbl;
	JLabel passLbl;

	JTextField usernameTxt;
	JPasswordField passTxt;

	JButton loginBtn;
	JButton registerBtn;

	JPanel mainPanel, titlePanel, formPanel, buttonPanel;

	DatabaseConnection dc;

	public LoginForm() {
		setLayout(new BorderLayout());

		dc = new DatabaseConnection();

		init();

		setTitle("Optimistic Book Store - Login");
		setSize(new Dimension(400, 200));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void init() {

		// Title Panel (NORTH)
		titlePanel = new JPanel();
		titleLbl = new JLabel("Optimistic Book Store", JLabel.CENTER);
		titleLbl.setFont(new Font("Calibri", Font.BOLD, 24));

		titlePanel.add(titleLbl);
		add(titlePanel, BorderLayout.NORTH);

		// Form Panel (CENTER)
		formPanel = new JPanel(new GridLayout(2,2,5,5));
		formPanel.setBorder(new EmptyBorder(5,5,5,5));

		usernameLbl = new JLabel("Username: ");
		passLbl = new JLabel("Password: ");

		usernameTxt = new JTextField();
		passTxt = new JPasswordField();

		formPanel.add(usernameLbl);
		formPanel.add(usernameTxt);
		formPanel.add(passLbl);
		formPanel.add(passTxt);

		add(formPanel, BorderLayout.CENTER);

		// Button Panel (SOUTH)
		buttonPanel = new JPanel();

		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(this);

		buttonPanel.add(loginBtn);
		buttonPanel.add(registerBtn);
		add(buttonPanel, BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginBtn) {
			ResultSet rs = null;
			String tempUsername = usernameTxt.getText();
			String tempPassword = new String(passTxt.getPassword());
			String query = "SELECT * FROM user WHERE username = '%s'";

			//validasi kalau kosong
			if(tempUsername.isEmpty()||tempPassword.isEmpty()){
				JOptionPane.showMessageDialog(null, "Please fill in the blanks", "Warning", JOptionPane.ERROR_MESSAGE);
				return;
			}

			else {
				query = String.format(query, tempUsername);
				rs = dc.executeQuery(query);
				try {

					if(!rs.next()){
						JOptionPane.showMessageDialog(null, "Username incorrect!", "Warning", JOptionPane.ERROR_MESSAGE);
						return;
					}
;
					String checkPassword = rs.getString("UserPassword");
					if(!tempPassword.equals(checkPassword)){
						JOptionPane.showMessageDialog(null, "Password incorrect!", "Warning", JOptionPane.ERROR_MESSAGE);
						return;
					}

					//iniUsernameFound, PasswordFound
					int id = rs.getInt("UserID");
					String fullName = rs.getString("UserFullName");
					String address = rs.getString("userAddress");
					String phoneNumber = rs.getString ("userPhoneNumber");
					String username = rs.getString("Username");
					String password = rs.getString("UserPassword");
					String email = rs.getString("UserEmail");
					char gender = rs.getString("UserGender").charAt(0);
					String birthDate = rs.getDate("UserBirthDate").toString();
					String role = rs.getString("UserRole");
					// String userId = rs.getString("UserID");
					User user = new User(id, fullName, address, phoneNumber, username, password, email, gender, birthDate, role);
					//bukaMainMenu
					JOptionPane.showMessageDialog(null, "Login Success", "Success", JOptionPane.INFORMATION_MESSAGE);
					new MainMenu(user).setVisible(true);
					this.dispose();
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}


		}

		if (e.getSource() == registerBtn) {
			this.dispose();
			new RegisterForm().setVisible(true);
		}

	}

}
