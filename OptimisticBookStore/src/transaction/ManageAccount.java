package transaction;
import javax.swing.JInternalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import connection.DatabaseConnection;
import user.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Vector;

public class ManageAccount extends JInternalFrame implements ActionListener, MouseListener{

	JLabel titleLbl, fullnameLbl, addressLbl, phoneLbl, usernameLbl, emailLbl, genderLbl, birthDateLbl, roleLbl, initPhoneNumLbl;
	JTextField fullnameField, phoneField, emailField, searchField, usernameField;
	JTextArea addressField;
	JRadioButton staffButton, customerButton;
	JDateChooser birthDateChooser;
	ButtonGroup roleButtonGroup;
	JRadioButton maleButton, femaleButton;
	ButtonGroup genderButtonGroup;
	JButton updateBtn, deleteBtn, searchBtn;
	JTable table;
	DefaultTableModel dtm;
	JScrollPane jsp;
	JPanel mainPanel, panelAtas, panelBawah,panelManage, panelField, panelTitle, genderPanel, rolePanel, phonePanel, panelButton, panelSearchAcc;
	boolean dataSelected = false;
	DatabaseConnection dc;
	TableRowSorter<DefaultTableModel> ts;
	User u;

	public ManageAccount(User u, MainMenu mainMenu) {
		dc = new  DatabaseConnection();
		this.u = u;

		init();

		setTitle("Manage Account");
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
		mainPanel = new JPanel(new BorderLayout());
		panelManage = new JPanel(new GridLayout(2,1));
		panelAtas = new JPanel(new BorderLayout());
		panelAtas.setBorder(new EmptyBorder(0, 50, 0, 50));
		panelBawah = new JPanel(new BorderLayout(20,20));
		panelBawah.setBorder(new EmptyBorder(0, 50, 20, 100));
		panelButton = new JPanel(new GridLayout(2,1,40,10));
		panelField = new JPanel(new GridLayout(8,2,5,5));
		panelField.setPreferredSize(new Dimension(800,300));
		panelSearchAcc = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));

		titleLbl = new JLabel("Manage Account");
		titleLbl.setFont(new Font("Calibri", Font.BOLD, 18));
		titleLbl.setForeground(Color.decode("#0a004e"));
		panelTitle = new JPanel();
		panelTitle.add(titleLbl);
		usernameLbl = new JLabel("Username: ");
		fullnameLbl = new JLabel("Fullname: ");
		addressLbl= new JLabel("Address: ");
		phoneLbl = new JLabel("Phone Number: ");
		emailLbl = new JLabel("Email : ");
		genderLbl = new JLabel("Gender: ");
		roleLbl = new JLabel("Role: ");
		birthDateLbl=new JLabel("Birth Date: ");

		usernameField = new JTextField();
		usernameField.setEnabled(false);
		fullnameField = new JTextField();
		fullnameField.setEnabled(false);
		addressField = new JTextArea();
		addressField.setEnabled(false);
		phoneField = new JTextField();
		initPhoneNumLbl = new JLabel("+62");
		phoneField = new JTextField();
		phonePanel = new JPanel(new BorderLayout(10,0));
		phonePanel.add(initPhoneNumLbl, BorderLayout.WEST);
		phonePanel.add(phoneField, BorderLayout.CENTER);
		phoneField.setEnabled(false);
		initPhoneNumLbl.setEnabled(false);
		emailField = new JTextField();
		emailField.setEnabled(false);
		maleButton = new JRadioButton("Male");
		femaleButton = new JRadioButton("Female");
		maleButton.setEnabled(false);
		femaleButton .setEnabled(false);
		genderButtonGroup= new ButtonGroup();
		genderButtonGroup.add(maleButton);
		genderButtonGroup.add(femaleButton);
		genderPanel = new JPanel();
		genderPanel.add(maleButton);
		genderPanel.add(femaleButton);
		birthDateChooser = new JDateChooser();
		birthDateChooser.setEnabled(false);
		staffButton = new JRadioButton("Staff");
		customerButton = new JRadioButton("Customer");
		staffButton .setEnabled(false);
		customerButton .setEnabled(false);
		roleButtonGroup= new ButtonGroup();
		roleButtonGroup.add(staffButton);
		roleButtonGroup.add(customerButton);
		rolePanel = new JPanel();
		rolePanel.add(staffButton);
		rolePanel.add(customerButton);

		panelField.add(usernameLbl);
		panelField.add(usernameField);
		panelField.add(fullnameLbl);
		panelField.add(fullnameField);
		panelField.add(addressLbl);
		panelField.add(addressField);
		panelField.add(phoneLbl);
		panelField.add(phonePanel);
		panelField.add(emailLbl);
		panelField.add(emailField);
		panelField.add(genderLbl);
		panelField.add(genderPanel);
		panelField.add(birthDateLbl);
		panelField.add(birthDateChooser);
		panelField.add(roleLbl);
		panelField.add(rolePanel);

		initTable();
		searchField= new JTextField(50);
		searchField.setText("Input keyword here...");
		searchBtn = new JButton("Search Account");
		searchBtn.addActionListener(this);

		panelSearchAcc.add(searchField);
		panelSearchAcc.add(searchBtn);




		updateBtn = new JButton("Update");
		deleteBtn= new JButton("Delete");
		panelButton.setPreferredSize(new Dimension(200, 50));
		panelButton.setBorder(new EmptyBorder(200,0,0,0));

		panelButton.add(updateBtn);
		panelButton.add(deleteBtn);
		updateBtn.addActionListener(this);
		deleteBtn.addActionListener(this);


		panelTitle.add(titleLbl);
		panelAtas.add(jsp, BorderLayout.CENTER);
		panelAtas.add(panelSearchAcc, BorderLayout.SOUTH);

		panelBawah.add(panelField, BorderLayout.CENTER);
		panelBawah.add(panelButton, BorderLayout.EAST);
		panelManage.add(panelAtas);
		panelManage.add(panelBawah);


		mainPanel.add(panelTitle, BorderLayout.NORTH);
		mainPanel.add(panelManage, BorderLayout.CENTER);
		add(mainPanel);

		mainPanel.repaint();
		mainPanel.revalidate();
	}



	private void initTable() {

		Object [] colName = {
				"Username","FullName", "Address", "PhoneNumber", "Email", "Gender", "BirthDate", "Role"
		};

		dtm = new DefaultTableModel (colName, 0);
		table = new JTable(dtm);
		jsp = new JScrollPane();
		jsp.setViewportView(table);
		jsp.setPreferredSize(new Dimension(1200,250));
		ts = new TableRowSorter<DefaultTableModel>(dtm);
		table.setRowSorter(ts);


		initDataTbl();

		table.addMouseListener(this);

	}

	void initDataTbl() {
		ResultSet rs;
		String query = "SELECT username, userfullname, useraddress, userphonenumber, useremail, usergender, userbirthdate, userrole FROM user WHERE userid != %s";
		query = String.format(query, u.getId());
		rs = dc.executeQuery(query);
		try {
			while (rs.next()) {
				Vector<Object>obj = new Vector<>();

				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					obj.add(rs.getObject(i+1));
				}

				dtm.addRow(obj);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==searchBtn)
		{

			search();
		}
		
		if(e.getSource()==updateBtn) {
			if(!dataSelected) {
				JOptionPane.showMessageDialog(null, "Please select data!", "Warning", JOptionPane.ERROR_MESSAGE);
				return;
			}
			//validateEmptyField
			if(fullnameField.getText().isEmpty()||addressField.getText().isEmpty()||phoneField.getText().isEmpty()||emailField.getText().isEmpty()||birthDateChooser.getDate()==null) {
				JOptionPane.showMessageDialog(null, "Please fill in the blanks", "Warning", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//validateeveryfield
			if(validateField()) {
				String phoneNum = "+62" + phoneField.getText();
				char selectedGender;
				String role;
				if(staffButton.isSelected()) role = "Staff";
				else role = "Customer";
	            if (maleButton.isSelected()) selectedGender = 'M';
	            else selectedGender = 'F';
	            java.sql.Date sqldate = new java.sql.Date(birthDateChooser.getDate().getTime());
	            JOptionPane.showMessageDialog(null, "Update Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
	            String query = "UPDATE user SET userfullname = '%s',useraddress = '%s', userphonenumber ='%s', useremail = '%s', usergender ='%s', userbirthdate = '%s', userrole = '%s' WHERE username = '%s'";
	            query = String.format(query, fullnameField.getText(), addressField.getText(), phoneNum, emailField.getText(), selectedGender, sqldate, role, usernameField.getText());
	            dc.executeUpdate(query);
	            
	            resetField();
	            dtm.setRowCount(0);
	            initDataTbl();
			}
			
		}
		
		if(e.getSource()==deleteBtn) {
			String query = "DELETE FROM user WHERE username = '%s'";
			query = String.format(query, usernameField.getText());
			JOptionPane.showMessageDialog(null, "Delete Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
			dc.executeUpdate(query);
			resetField();
			dtm.setRowCount(0);
			initDataTbl();
		}

	}



	private void search() {
		String temp = searchField.getText().trim();

		if(temp.isEmpty()){
			ts.setRowFilter(null);
		}

		else if(temp.length()<3){
			JOptionPane.showMessageDialog(null, "Please enter more than or equal 3 characters");
			return;
		}

		else if(temp.length()>=3 ){
			ts.setRowFilter(RowFilter.regexFilter("(?i)"+temp));

			//(?i) -> case insensitive
		} 
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==table) {
			dataSelected = false;
			int row = table.getSelectedRow();
			String usernameTemp = table.getValueAt(row, 0).toString();
			if(!usernameTemp.isEmpty())
				dataSelected=true;

			if(dataSelected) {
				fillAllField(usernameTemp, row);
				
			}
		}


	}


	private void fillAllField(String usernameTemp, int row) {
		usernameField.setText(usernameTemp);
		fullnameField.setText(table.getValueAt(row, 1).toString());
		fullnameField.setEnabled(true);
		addressField.setText(table.getValueAt(row, 2).toString());
		phoneField.setText(table.getValueAt(row, 3).toString().substring(3, table.getValueAt(row, 3).toString().length()));
		phoneField.setEnabled(true);
		addressField.setEnabled(true);
		emailField.setText(table.getValueAt(row, 4).toString());
		emailField.setEnabled(true);
		maleButton.setEnabled(true);
		femaleButton.setEnabled(true);
		if(table.getValueAt(row, 5).toString().equals("M")) {
			maleButton.setSelected(true);
		} else femaleButton.setSelected(true);

		birthDateChooser.setEnabled(true);
		birthDateChooser.setDate(getBirthDate(table.getValueAt(row, 6).toString()));
		staffButton.setEnabled(true);
		customerButton.setEnabled(true);
		if(table.getValueAt(row, 7).toString().equals("Customer")) {
			customerButton.setSelected(true);
		} else staffButton.setSelected(true);
		
	}



	private Date getBirthDate(String birthDate) {
		String birthDateArr[] = birthDate.split("-");
		LocalDate localDate = LocalDate.of(Integer.parseInt(birthDateArr[0]), Integer.parseInt(birthDateArr[1]), Integer.parseInt(birthDateArr[2]));

		ZoneId systemTimeZone = ZoneId.systemDefault();

		Date date = Date.from(localDate.atStartOfDay(systemTimeZone).toInstant());

		return date;
	}





	


	private boolean validateField() {
		if (isUsedEmail()) {
			JOptionPane.showMessageDialog(null, "Email already used, please use another email!", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!isValidEmail(emailField.getText())) {
			JOptionPane.showMessageDialog(null, "Please input a valid email!", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		
		if (addressField.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "Minimum address length is 10 characters!", "Warning", JOptionPane.ERROR_MESSAGE);
            return false;
        }
		
		if (!isNumberIdentification(phoneField.getText())) {
            JOptionPane.showMessageDialog(null, "Please input a valid phone number!", "Warning", JOptionPane.ERROR_MESSAGE);
            return false;
        }
		
		return true;
	}



	@Override
	public void mouseEntered(MouseEvent e) {

	}



	@Override
	public void mouseExited(MouseEvent e) {

	}



	@Override
	public void mousePressed(MouseEvent e) {

	}



	@Override
	public void mouseReleased(MouseEvent e) {

	}

	boolean isUsedEmail() {
		ResultSet rs = null;
		String email = emailField.getText();
		String query = "SELECT username,userEmail FROM user";
		rs = dc.executeQuery(query);

		try {
			while(rs.next()){
				if (rs.getString("userEmail").equalsIgnoreCase(email)) {
					if(rs.getString("username").equals(usernameField.getText()))
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

	boolean isValidEmail(String email) {
		if (email.contains("@")) {
			String emailArr[] = email.split("@");
			if (emailArr.length == 2){
				if (emailArr[1].contains(".")) return true;
			}
		}
		return false;
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
    
    void resetField() {
    	usernameField.setText("");
		fullnameField.setText("");
		fullnameField.setEnabled(false);
		addressField.setText("");
		phoneField.setText("");
		phoneField.setEnabled(false);
		addressField.setEnabled(false);
		emailField.setText("");
		emailField.setEnabled(false);
		maleButton.setEnabled(false);
		femaleButton.setEnabled(false);
		genderButtonGroup.clearSelection();
		birthDateChooser.setEnabled(false);
		birthDateChooser.cleanup();
		staffButton.setEnabled(false);
		customerButton.setEnabled(false);
		roleButtonGroup.clearSelection();
    }

}
