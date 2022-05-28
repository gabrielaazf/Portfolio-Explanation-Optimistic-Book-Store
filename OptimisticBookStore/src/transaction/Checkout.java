package transaction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import connection.DatabaseConnection;
import user.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;


public class Checkout extends JInternalFrame implements ActionListener, MouseListener {
    int totalPrice;
    DatabaseConnection dc;
    User u;
    ResultSet rs;

    JPanel mainPanel, titlePanel, centerPanel, bottomPanel;
    JLabel titleLabel, inputLabel;
    JButton continueButton, payButton;
    JTextField textField;
    String[] comboBoxOptions = {"Credit Card", "Debit Card", "E-Wallet"};
    JComboBox<String> paymentComboBox = new JComboBox<>(comboBoxOptions);

    String selected;
    int paymentMethodId;
    MainMenu mm;
    public Checkout(User u, int totalPrice, MainMenu mm) {
        dc = new DatabaseConnection();
        this.u = u;
        this.totalPrice = totalPrice;
        this.mm = mm;

        init();

        setTitle("Cart");
        setClosable(true);
        setResizable(false);
        
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {      
            	mm.welcomeText();
            	dispose();
            }
        });
    }

    void init() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20,50,450,50));

        // NORTH
        titlePanel = new JPanel();

        titleLabel = new JLabel("Checkout");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 30));
        titlePanel.add(titleLabel);

        // CENTER
        centerPanel = new JPanel();
        inputLabel = new JLabel("Choose payment method: ");
        inputLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        paymentComboBox.setFont(new Font("Calibri", Font.PLAIN, 24));
        centerPanel.add(inputLabel);
        centerPanel.add(paymentComboBox);

        // SOUTH
        bottomPanel = new JPanel();
        continueButton = new JButton("Continue");
        continueButton.addActionListener(this);

        bottomPanel.add(continueButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
      

        add(mainPanel);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == continueButton) {
            selected = String.valueOf(paymentComboBox.getSelectedItem());

            if (selected.equals("Credit Card")) paymentMethodId = 1;
            else if (selected.equals("Debit Card")) paymentMethodId = 2;
            else if (selected.equals("E-Wallet")) paymentMethodId = 3;

            centerPanel.removeAll();
            centerPanel = new JPanel();

            inputLabel = new JLabel(selected.equals("E-Wallet") ? "Input phone number:" : "Input card number:");
            inputLabel.setFont(new Font("Calibri", Font.BOLD, 24));
            textField = new JTextField(20);
            textField.setFont(new Font("Calibri", Font.BOLD, 24));
            
            centerPanel.add(inputLabel);
            centerPanel.add(textField);

            bottomPanel.removeAll();
            bottomPanel = new JPanel(new GridLayout(2, 1,10,10));
            JPanel btn = new JPanel();
            btn.setPreferredSize(new Dimension(75,50));
            payButton = new JButton("Pay");
            payButton.setFont(new Font("Calibri", Font.BOLD, 18));
            payButton.addActionListener(this);
            btn.add(payButton);
            JLabel priceTot = new JLabel("Total Payment: "+totalPrice,JLabel.CENTER);
            priceTot.setForeground(Color.red);
            priceTot.setFont(new Font("Calibri", Font.BOLD, 24));
            
            bottomPanel.add(priceTot);
            bottomPanel.add(btn);

            mainPanel.add(centerPanel, BorderLayout.CENTER);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);

            centerPanel.repaint();
            centerPanel.revalidate();

            bottomPanel.repaint();
            bottomPanel.revalidate();
        }

        if (e.getSource() == payButton) {
        	
        	if(isNotNumeric()) {
        		 JOptionPane.showMessageDialog(null, "Input must be all numeric/digit!", "Warning", JOptionPane.ERROR_MESSAGE);
                 return;
        	}

            if (paymentMethodId != 3 && textField.getText().length() < 16) {
                JOptionPane.showMessageDialog(null, "Input a valid number! (16 digits)", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(paymentMethodId == 3 && (textField.getText().length()<10||textField.getText().length()>12 )) {
            	 JOptionPane.showMessageDialog(null, "Input a valid phone number! (10-12 digits)", "Warning", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            rs = dc.executeQuery("SELECT * FROM header_order GROUP BY OrderID HAVING COUNT(*) = 1 ORDER BY OrderID DESC LIMIT 1");
            int orderId = 0;
            try {
                while (rs.next()) {
                    orderId = rs.getInt("OrderID");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            java.sql.Date sqlDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());

            String query = "INSERT INTO header_order (userid, paymentmethodid, cardnumber, transactiondate, statusorder) VALUES ('%d', '%d', '%s', '%s', '%s')";
            query = String.format(query, u.getId(), paymentMethodId, textField.getText(), sqlDate, "Waiting");

            dc.executeUpdate(query);

            query = "SELECT * FROM cart WHERE UserID = '%d'";
            query = String.format(query, u.getId());

            rs = dc.executeQuery(query);

            Vector<DetailOrder> orderDetails = new Vector<>();

            try {
                while (rs.next()) {
                    orderDetails.add(new DetailOrder(orderId, rs.getInt("BookSKU"), rs.getInt("qty")));
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            for (DetailOrder det : orderDetails) {
                query = "INSERT INTO detail_order VALUES('%d', '%d', '%d')";
                query = String.format(query, (det.getOrderId()+1), det.getBookSku(), det.getQty());
                dc.executeUpdate(query);
            }

            query = "DELETE FROM cart WHERE UserID = '%d'";
            query = String.format(query, u.getId());
            dc.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Processing.. Press OK to finish");
            
            this.dispose();
            mm.welcomeText();
            
        }        
    }

	public boolean isNotNumeric() {
		for(int i=0; i<textField.getText().length();i++) {
			if(!Character.isDigit(textField.getText().charAt(i))) {
				return true;
			}
		}
		return false;
	}
    
}
