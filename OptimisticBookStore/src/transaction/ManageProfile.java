package transaction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.toedter.calendar.JDateChooser;

import connection.DatabaseConnection;
import user.User;

public class ManageProfile extends JInternalFrame implements ActionListener, KeyListener {
	User u;
	DatabaseConnection dc;

	JPanel mainPanel, profilePanel, profileFieldPanel, phoneFieldPanel;
	JLabel myProfileLbl, usernameLbl, usernameAnsLbl, emailLbl, fullNameLbl, addressLbl, phoneNumberLbl, initPhNumLbl, genderLbl, birthDateLbl, titlePwLbl, oldPwLbl, newPwLbl, rePwLbl, rePwValidationLbl;
	JTextField emailField, fullNameField, phoneNumberField;
	JPasswordField oldPwField, newPwField, rePwField;
	JPanel genderPanel, updateProfileBtnPanel, updatePwPanel, pwFieldPanel, updatePwBtnPanel, rePwPanel, rePwFieldPanel, newPwFieldPanel, oldPwFieldPanel;
	JTextArea addressArea;
	JDateChooser birthDateChooser;
	JRadioButton male, female;
	ButtonGroup gender;
	JButton updateMyProfileBtn, updatePwBtn;

	public ManageProfile(User u, MainMenu mainMenu) {
		this.u = u;

		dc = new  DatabaseConnection();

		init();

		setTitle("Manage Profile");
		setClosable(true);
		setResizable(false);
		
		addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
            	mainMenu.welcomeText();
            	dispose();
            }
        });
	}


	private void init() {
		//initPanel
		mainPanel = new JPanel(new GridLayout(1,2,4,4));
		profilePanel = new JPanel(new BorderLayout());
		profilePanel.setBorder(new EmptyBorder(5, 25, 5, 25));
		profileFieldPanel = new JPanel(new GridLayout(7,2,10,30));
		phoneFieldPanel = new JPanel(new BorderLayout(10,0));
		updateProfileBtnPanel = new JPanel();
		updatePwPanel = new JPanel(new BorderLayout());
		pwFieldPanel = new JPanel(new GridLayout(3,2));
		updatePwPanel.setBorder(new EmptyBorder(5, 25, 315, 25));
		pwFieldPanel.setBorder(new EmptyBorder(20, 0, 15, 0));
		updatePwBtnPanel = new JPanel();
		rePwPanel = new JPanel(new BorderLayout());
		rePwFieldPanel= new JPanel();
		rePwFieldPanel.setBorder(new EmptyBorder(0, 0, 105, 0));
		newPwFieldPanel = new JPanel();
		oldPwFieldPanel = new JPanel();

		//profileSide
		myProfileLbl = new JLabel("My Profile", JLabel.CENTER);
		myProfileLbl.setFont(new Font("Calibri", Font.BOLD, 18));
		myProfileLbl.setForeground(Color.decode("#0a004e"));

		initProfileFieldPanel();

		updateMyProfileBtn = new JButton("Update My Profile");
		updateMyProfileBtn.setPreferredSize(new Dimension(250, 40));
		updateProfileBtnPanel.add(updateMyProfileBtn);
		updateMyProfileBtn.addActionListener(this);

		profilePanel.add(myProfileLbl, BorderLayout.NORTH);
		profilePanel.add(profileFieldPanel, BorderLayout.CENTER);
		profilePanel.add(updateProfileBtnPanel, BorderLayout.SOUTH);

		mainPanel.add(profilePanel);

		//initPasswordUpdate
		titlePwLbl = new JLabel("Update My Password", JLabel.CENTER);
		titlePwLbl.setFont(new Font("Calibri", Font.BOLD, 18));
		titlePwLbl.setForeground(Color.decode("#0a004e"));

		initPwField();

		updatePwBtn = new JButton("Update My Password");
		updatePwBtn.setPreferredSize(new Dimension(250, 40));
		updatePwBtnPanel.add(updatePwBtn);
		updatePwBtn.setEnabled(false);
		updatePwBtn.addActionListener(this);

		updatePwPanel.add(titlePwLbl, BorderLayout.NORTH);
		updatePwPanel.add(pwFieldPanel, BorderLayout.CENTER);
		updatePwPanel.add(updatePwBtnPanel, BorderLayout.SOUTH);

		mainPanel.add(updatePwPanel);

		add(mainPanel);

	}


	private void initPwField() {
		oldPwLbl = new JLabel("Input your old password: ");
		oldPwField = new JPasswordField();
		oldPwField.setPreferredSize(new Dimension(250, 40));
		oldPwFieldPanel.add(oldPwField);
		oldPwField.addKeyListener(this);

		newPwLbl = new JLabel("Input new password: ");
		newPwField = new JPasswordField();
		newPwField.setPreferredSize(new Dimension(250, 40));
		newPwFieldPanel.add(newPwField);
		newPwField.addKeyListener(this);

		rePwLbl = new JLabel("Re-enter your new password: ");
		rePwField = new JPasswordField();
		rePwField.setPreferredSize(new Dimension(250,40));
		rePwFieldPanel.add(rePwField);
		rePwValidationLbl = new JLabel("", JLabel.CENTER);
		rePwValidationLbl.setBorder(new EmptyBorder(-1, 0, -1, 0));
		
		rePwPanel.add(rePwFieldPanel, BorderLayout.CENTER);
		rePwPanel.add(rePwValidationLbl, BorderLayout.SOUTH);
		rePwField.addKeyListener(this);

		pwFieldPanel.add(oldPwLbl);
		pwFieldPanel.add(oldPwFieldPanel);
		pwFieldPanel.add(newPwLbl);
		pwFieldPanel.add(newPwFieldPanel);
		pwFieldPanel.add(rePwLbl);
		pwFieldPanel.add(rePwPanel);

	}


	private void initProfileFieldPanel() {
		usernameLbl = new JLabel("Username: ");
		usernameAnsLbl = new JLabel(u.getUsername());

		emailLbl = new JLabel("Email: ");
		emailField = new JTextField();

		fullNameLbl = new JLabel("Full Name: ");
		fullNameField = new JTextField();

		addressLbl = new JLabel("Address: ");
		addressArea = new JTextArea();

		phoneNumberLbl = new JLabel("Phone Number: ");
		initPhNumLbl = new JLabel("+62");
		phoneNumberField= new JTextField();
		phoneFieldPanel.add(initPhNumLbl, BorderLayout.WEST);
		phoneFieldPanel.add(phoneNumberField, BorderLayout.CENTER);

		genderLbl = new JLabel("Gender: ");
		gender = new ButtonGroup();
		male = new JRadioButton("Male");
		female = new JRadioButton("Female");
		gender.add(male);
		gender.add(female);
		genderPanel = new JPanel();
		genderPanel.add(male);
		genderPanel.add(female);

		birthDateLbl = new JLabel("Birth Date: ");
		birthDateChooser = new JDateChooser();
		
		updateProfileField();
		
		profileFieldPanel.add(usernameLbl);
		profileFieldPanel.add(usernameAnsLbl);
		profileFieldPanel.add(emailLbl);
		profileFieldPanel.add(emailField);
		profileFieldPanel.add(fullNameLbl);
		profileFieldPanel.add(fullNameField);
		profileFieldPanel.add(addressLbl);
		profileFieldPanel.add(addressArea);
		profileFieldPanel.add(phoneNumberLbl);
		profileFieldPanel.add(phoneFieldPanel);
		profileFieldPanel.add(genderLbl);
		profileFieldPanel.add(genderPanel);
		profileFieldPanel.add(birthDateLbl);
		profileFieldPanel.add(birthDateChooser);

	}


	private Date getBirthDate(String birthDate) {
		String birthDateArr[] = birthDate.split("-");
		LocalDate localDate = LocalDate.of(Integer.parseInt(birthDateArr[0]), Integer.parseInt(birthDateArr[1]), Integer.parseInt(birthDateArr[2]));

		ZoneId systemTimeZone = ZoneId.systemDefault();

		Date date = Date.from(localDate.atStartOfDay(systemTimeZone).toInstant());

		return date;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == updateMyProfileBtn) {
			int res= JOptionPane.showConfirmDialog(this, "Are you sure you want to update your profile?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (res==JOptionPane.YES_OPTION) {
				if (!emptyFieldValidation()) {
					if (allFieldValid()) {
						String phoneNum = "+62" + phoneNumberField.getText();
						char selectedGender;
						if (male.isSelected())
							selectedGender = 'M';
						else
							selectedGender = 'F';
						java.sql.Date sqldate = new java.sql.Date(birthDateChooser.getDate().getTime());
						JOptionPane.showMessageDialog(null, "Update Successful!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						String query = "UPDATE user SET userfullname = '%s',useraddress = '%s', userphonenumber ='%s', useremail = '%s', usergender ='%s', userbirthdate = '%s' WHERE username = '%s'";
						query = String.format(query, fullNameField.getText(), addressArea.getText(), phoneNum,
								emailField.getText(), selectedGender, sqldate, usernameAnsLbl.getText());
						dc.executeUpdate(query);

						changeUserData(phoneNum,selectedGender);

						updateProfileField();

					}
				} 
			}
		}
		
		if(e.getSource()==updatePwBtn) {
			String oldPw = String.valueOf(oldPwField.getPassword());
			String newPw = String.valueOf(newPwField.getPassword());
			
			//Sistem akan memberikan peringatan apabila rePw dan newPw blm sama
			if(oldPw.equals(u.getPassword())) {
				if(newPw.equals(oldPw)) {
					JOptionPane.showMessageDialog(this, "New Password must not be same with current password!", "Warning", JOptionPane.WARNING_MESSAGE);
				}
				else {
					int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to change your password?", "Confirmation", JOptionPane.YES_NO_OPTION);
					
					if (res == JOptionPane.YES_OPTION) {
						updatePassword(newPw);
						
					}
				}
			}
			
			else JOptionPane.showMessageDialog(null, "Please enter the correct old password!","Warning",JOptionPane.WARNING_MESSAGE);
			
		}

	}


	private void updatePassword(String newPw) {

		String query = "UPDATE user SET userpassword = '%s' WHERE userid = %s";
		query = String.format(query, newPw, u.getId());
		
		dc.executeUpdate(query);
		
		u.setPassword(newPw);
		
		JOptionPane.showMessageDialog(this, "Password Updated", "Updated", JOptionPane.INFORMATION_MESSAGE);
		
		oldPwField.setText("");
		newPwField.setText("");
		rePwField.setText("");
		rePwValidationLbl.setText("");
		updatePwBtn.setEnabled(false);
	}


	private void changeUserData(String phoneNum, char selectedGender) {
		u.setFullName(fullNameField.getText());
		u.setAddress(addressArea.getText());
		u.setEmail(emailField.getText());
		Date birthDate = birthDateChooser.getDate();
		LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		u.setBirthDate(birthDateLocal.toString());
		u.setGender(selectedGender);
		u.setPhoneNumber(phoneNum);
	}


	private void updateProfileField() {
		emailField.setText(u.getEmail());

		fullNameField.setText(u.getFullName());

		addressArea.setText(u.getAddress());

		phoneNumberField.setText(u.getPhoneNumber().substring(3, u.getPhoneNumber().length()));
		
		if(u.getGender()=='M') {
			male.setSelected(true);
		} else female.setSelected(true);
		
		birthDateChooser.setDate(getBirthDate(u.getBirthDate().toString()));
		
	}


	private boolean allFieldValid() {

		if (isUsedEmail()) {
			JOptionPane.showMessageDialog(null, "Email already used, please use another email!", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!isValidEmail(emailField.getText())) {
			JOptionPane.showMessageDialog(null, "Please input a valid email!", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		
		if (addressArea.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "Minimum address length is 10 characters!", "Warning", JOptionPane.ERROR_MESSAGE);
            return false;
        }
		
		if (!isNumberIdentification(phoneNumberField.getText())) {
            JOptionPane.showMessageDialog(null, "Please input a valid phone number!", "Warning", JOptionPane.ERROR_MESSAGE);
            return false;
        }
		
		return true;
	}


	private boolean isNumberIdentification(String num) {
		 for (int i = 0; i < num.length(); i++) {
	            try {
	                Integer.parseInt(String.valueOf(num.charAt(i)));
	            } catch (Exception e) {
	                return false;
	            }
	        }
        return true;
	}


	private boolean isValidEmail(String email) {
		if (email.contains("@")) {
			String emailArr[] = email.split("@");
			if (emailArr.length == 2){
				if (emailArr[1].contains(".")) return true;
			}
		}
		return false;
	}


	private boolean isUsedEmail() {
		ResultSet rs = null;
		String email = emailField.getText();
		String query = "SELECT username,userEmail FROM user";
		rs = dc.executeQuery(query);

		try {
			while(rs.next()){
				if (rs.getString("userEmail").equalsIgnoreCase(email)) {
					if(rs.getString("username").equals(usernameAnsLbl.getText()))
						return false;
					else
						return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}


	public boolean emptyFieldValidation() {
		if(emailField.getText().isEmpty()||fullNameField.getText().isEmpty()||addressArea.getText().isEmpty()||phoneNumberField.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Please fill in the blanks", "Warning", JOptionPane.ERROR_MESSAGE);
			return true;
		}

		else return false;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		
		if(new String(rePwField.getPassword()).length()==0&&e.getSource()==newPwField) 
			return;
		
		if(e.getSource()==rePwField) {
			String pwTemp = (new String(rePwField.getPassword())+e.getKeyChar());
			
			if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
				pwTemp=trimPw(pwTemp);
			}
			if(new String (newPwField.getPassword()).equals(pwTemp)) {
				passwordValid(pwTemp);
			}
			else {
				passwordInvalid();
			}
		}
		
		if(e.getSource()==newPwField) {
			String pwTemp = (new String(newPwField.getPassword())+e.getKeyChar());

			if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
				pwTemp=trimPw(pwTemp);

			}
			if(pwTemp.equals(new String(rePwField.getPassword()))){
				passwordValid(pwTemp);
			}
			else {
				passwordInvalid();

			}
		}
		
		if(e.getSource()==oldPwField) {
			int length = ((new String(oldPwField.getPassword()))+e.getKeyChar()).length();
			
			if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE)
				length=-2;
			if(length>=8){
				if(new String (newPwField.getPassword()).equals(new String(rePwField.getPassword()))&&(!new String (newPwField.getPassword()).isEmpty())) {
					updatePwBtn.setEnabled(true);
				}
				else updatePwBtn.setEnabled(false);
			}
			else updatePwBtn.setEnabled(false);

		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	private String trimPw(String pwTemp) {
		return pwTemp.substring(0, pwTemp.length()-2);
	}
	
	private void passwordInvalid() {
		rePwValidationLbl.setText("password mismatch!");
		rePwValidationLbl.setForeground(Color.red);
		updatePwBtn.setEnabled(false);

	}

	private void passwordValid(String pwTemp) {
		rePwValidationLbl.setText("password match!");
		rePwValidationLbl.setForeground(Color.BLUE);
		if(new String(oldPwField.getPassword()).isEmpty()||pwTemp.length()<8) return;
		else updatePwBtn.setEnabled(true);
		
	}

}
