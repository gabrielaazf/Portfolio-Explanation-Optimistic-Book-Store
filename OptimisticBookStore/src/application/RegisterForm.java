package application;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import connection.DatabaseConnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;


public class RegisterForm extends JFrame implements ActionListener {
    JLabel titleLbl;
    JLabel emailLbl;
    JLabel usernameLbl;
    JLabel passLbl;
    JLabel fullNameLbl;
    JLabel genderLbl;
    JLabel birthDateLbl;
    JLabel addressLbl;
    JLabel phoneLbl;
    JLabel initPhoneNumLbl;
    JLabel hasAccountLbl;
    JLabel roleLbl;
    
    JTextField emailTxt,usernameTxt,fullNameTxt, phoneTxt;
    JTextArea addressTxt;
    JRadioButton male, female, cust, staff;
    ButtonGroup gender, role;
    JPasswordField passTxt;
    JDateChooser birthDateChooser;
    
    JButton loginBtn;
    JButton registerBtn;
    
    JPanel mainPanel, titlePanel, formPanel, buttonPanel, genderPanel, phonePanel, loginPanel, loginBtnPanel, registBtnPanel, rolePanel;
    
    DatabaseConnection dc;
    
    public RegisterForm() {
        setLayout(new BorderLayout());
        dc = new DatabaseConnection();
        
        init();
        
        setTitle("Optimistic Book Store - Registration");
        setSize(new Dimension(700, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }
    
    private void init() {
        // Main Panel Initiation
        // mainPanel = new JPanel();
        // mainPanel.setLayout(new BorderLayout);
        
        // Title Panel (NORTH)
        titlePanel = new JPanel();
        titleLbl = new JLabel("Optimistic Book Store", JLabel.CENTER);
        titleLbl.setFont(new Font("Calibri", Font.BOLD, 24));
        
        titlePanel.add(titleLbl);
        add(titlePanel, BorderLayout.NORTH);
        
        // Form Panel (CENTER)
        formPanel = new JPanel(new GridLayout(9,2,5,5));
        formPanel.setBorder(new EmptyBorder(5,5,5,5));
        emailLbl = new JLabel("Email Address: ");
        usernameLbl = new JLabel("Username: ");
        passLbl = new JLabel("Password: ");
        fullNameLbl = new JLabel("Full name:");
        genderLbl = new JLabel("Gender: ");
        birthDateLbl = new JLabel("Birth date:");
        addressLbl = new JLabel("Address: ");
        phoneLbl = new JLabel("Phone Number: ");
        
        emailTxt = new JTextField();
        usernameTxt = new JTextField();
        passTxt = new JPasswordField();
        fullNameTxt = new JTextField();
        //gender
        male = new JRadioButton("Male");
        female = new JRadioButton("Female");
        gender = new ButtonGroup();
        gender.add(male);
        gender.add(female);
        genderPanel = new JPanel();
        genderPanel.add(male);
        genderPanel.add(female);
        
        birthDateChooser = new JDateChooser();
        
        addressTxt = new JTextArea();
        initPhoneNumLbl= new JLabel("+62");
        phoneTxt = new JTextField();
        phonePanel = new JPanel(new BorderLayout(10,0));
        phonePanel.add(initPhoneNumLbl, BorderLayout.WEST);
        phonePanel.add(phoneTxt, BorderLayout.CENTER);
        
        //role chooser
        roleLbl = new JLabel("Role");
        staff = new JRadioButton("Staff");
        cust = new JRadioButton("Customer");
        role = new ButtonGroup();
        role.add(staff);
        role.add(cust);
        rolePanel = new JPanel();
        rolePanel.add(staff);
        rolePanel.add(cust);
        
        
        formPanel.add(emailLbl);
        formPanel.add(emailTxt);
        formPanel.add(usernameLbl);
        formPanel.add(usernameTxt);
        formPanel.add(passLbl);
        formPanel.add(passTxt);
        formPanel.add(fullNameLbl);
        formPanel.add(fullNameTxt);
        formPanel.add(genderLbl);
        formPanel.add(genderPanel);
        formPanel.add(birthDateLbl);
        formPanel.add(birthDateChooser);
        formPanel.add(addressLbl);
        formPanel.add(addressTxt);
        formPanel.add(phoneLbl);
        formPanel.add(phonePanel);
        formPanel.add(roleLbl);
        formPanel.add(rolePanel);
      
        
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel (SOUTH)
        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(0, 120, 0, 120));
        loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        loginBtnPanel = new JPanel();
        registBtnPanel = new JPanel();
        
        
        loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Calibri", Font.PLAIN, 10));
        loginBtn.setPreferredSize(new Dimension(80,25));
        loginBtnPanel.add(loginBtn);
        loginBtn.addActionListener(this);
        hasAccountLbl = new JLabel("Already has an account?", JLabel.CENTER);
        registerBtn = new JButton("Register");
        registerBtn.addActionListener(this);
        registerBtn.setPreferredSize(new Dimension(150,30));
        registBtnPanel.add(registerBtn);
        
        loginPanel.add(hasAccountLbl);
        loginPanel.add(loginBtnPanel);
        buttonPanel.add(registBtnPanel, BorderLayout.CENTER);
        buttonPanel.add(loginPanel, BorderLayout.SOUTH);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
    }
    
    boolean isNumberIdentification(String num) {
        for (int i = 0; i < num.length(); i++) {
            try {
                Integer.parseInt(String.valueOf(num.charAt(i)));
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    
    boolean isValidEmail(String email) {
        if (email.contains("@")) {
            String emailArr[] = email.split("@");
            if (emailArr.length == 2){
                if (emailArr[1].contains(".")) return true;
            }
        }
        return false;
    }
    
    boolean isUsedUsername() {
        ResultSet rs = null;
        String username = usernameTxt.getText();
        String query = "SELECT Username FROM user";
        rs = dc.executeQuery(query);
        
        try {
            while(rs.next()){
                if (rs.getString("Username").equalsIgnoreCase(username)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    boolean isUsedEmail() {
        ResultSet rs = null;
        String email = emailTxt.getText();
        String query = "SELECT userEmail FROM user";
        rs = dc.executeQuery(query);
        
        try {
            while(rs.next()){
                if (rs.getString("UserEmail").equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == registerBtn) {
            if (emailTxt.getText().isEmpty() || usernameTxt.getText().isEmpty() || new String(passTxt.getPassword()).isEmpty() ||
            fullNameTxt.getText().isEmpty() || gender.getSelection()==null || birthDateChooser.getDate()==null || addressTxt.getText().isEmpty() ||
            phoneTxt.getText().isEmpty()||role.getSelection()==null) {
                JOptionPane.showMessageDialog(null, "Please fill in the blanks", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            

            if (isUsedEmail()) {
                JOptionPane.showMessageDialog(null, "Email already used, please use another email!", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isValidEmail(emailTxt.getText())) {
                JOptionPane.showMessageDialog(null, "Please input a valid email!", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            
            if (isUsedUsername()) {
                JOptionPane.showMessageDialog(null, "Username already used, please use another username!", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
    
            if(usernameTxt.getText().length()<=5) {
            	JOptionPane.showMessageDialog(null, "Username must be more than 5 characters!", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(usernameTxt.getText().endsWith(".")) {
            	JOptionPane.showMessageDialog(null, "Username cannot be end with \".\"", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            

            
            if (new String(passTxt.getPassword()).length() < 8) {
                JOptionPane.showMessageDialog(null, "Minimum password length is 8 characters!", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (addressTxt.getText().length() < 10) {
                JOptionPane.showMessageDialog(null, "Minimum address length is 10 characters!", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isNumberIdentification(phoneTxt.getText())) {
                JOptionPane.showMessageDialog(null, "Please input a valid phone number!", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(phoneTxt.getText().length()<8) {
            	 JOptionPane.showMessageDialog(null, "Please input more than 8 digits!", "Warning", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            
            String phoneNum = "+62" + phoneTxt.getText();
            
            String passString = new String(passTxt.getPassword());
            
            char selectedGender;
            if (male.isSelected()) selectedGender = 'M';
            else selectedGender = 'F';
            
            String roleUser;
            
            if(staff.isSelected()) roleUser = "Staff";
            else roleUser = "Customer";
            
            java.sql.Date sqldate = new java.sql.Date(birthDateChooser.getDate().getTime());
            
            JOptionPane.showMessageDialog(null, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            String query = "INSERT INTO user(UserFullName, UserAddress, UserPhoneNumber, Username, UserPassword, UserEmail, UserGender, UserBirthDate, UserRole) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')";
            query = String.format(query, fullNameTxt.getText(), addressTxt.getText(), phoneNum, usernameTxt.getText(), passString, emailTxt.getText(),selectedGender, sqldate, roleUser );
            dc.executeUpdate(query);
            
            openLogin();
            
            
        }
        
        if(e.getSource()==loginBtn){
           openLogin();
        }
        
    }
    
    public void openLogin () {
    	this.dispose();
    	new LoginForm().setVisible(true);
    }
    
}
