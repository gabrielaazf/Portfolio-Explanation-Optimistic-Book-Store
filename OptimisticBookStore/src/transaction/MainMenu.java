package transaction;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import application.LoginForm;
import connection.DatabaseConnection;
import user.User;

public class MainMenu extends JFrame implements ActionListener {
    static User u;
    
    JPanel titlePanel, hiPanel, transactionsPanel, allPanel;
    
    JLabel titleLbl, hiLbl, totalTransactionLbl;
    
    JMenuBar menuBar;
    
    JMenu book, order, profile;
    JMenuItem buyBook, viewCart, viewOrder, manageProfile, logOut;
    Image img;
    JMenu manage;
    JMenuItem manageBook, manageOrder, manageAccount;
   
    DatabaseConnection dc;

    
    public MainMenu(User u) {
        MainMenu.u = u;
        dc = new DatabaseConnection();

        setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        profile = new JMenu("Profile");
      
        if(u.getRole().equals("Customer")) initCustomerMenu();
        else initStaffMenu();
        
        manageProfile = new JMenuItem("Manage Profile");
        logOut = new JMenuItem("Logout");
        
        manageProfile.addActionListener(this);
        logOut.addActionListener(this);
        
        profile.add(manageProfile);
        profile.add(logOut);
        
        menuBar.add(profile);

        welcomeText();
        
        setTitle("Optimistic Book Store");
        setSize(new Dimension(1366,768));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(menuBar);
    }
    
    public void welcomeText() {
		titlePanel = new JPanel();
		titleLbl = new JLabel("Welcome to Optimistic Book Store!");
		titleLbl.setFont(new Font("Calibri", Font.BOLD,30));
		titlePanel.add(titleLbl);

		hiPanel = new JPanel();
		hiLbl = new JLabel("Hi, "+ u.getFullName());
		hiLbl.setFont(new Font("Calibri", Font.PLAIN,24));
		hiPanel.add(hiLbl);

		 if(u.getRole().equals("Customer")) checkCustTrans();
	        else checkStaffTrans();
		
		allPanel = new JPanel(new GridLayout(3,1));
		allPanel.setPreferredSize(new Dimension (500,500));
		allPanel.setBorder(new EmptyBorder(20, 5, 350, 5));
		
		allPanel.add(titlePanel);
		allPanel.add(hiPanel);
		allPanel.add(transactionsPanel);


		add(allPanel, BorderLayout.NORTH);

	}
    
    public void initCustomerMenu() {
        
        book = new JMenu("Books");
        order = new JMenu("Order");
        
        buyBook = new JMenuItem("Buy Book(s)");
        buyBook.addActionListener(this);
        book.add(buyBook);
        
        viewCart = new JMenuItem("View Cart");
        viewCart.addActionListener(this);
        viewOrder = new JMenuItem("View Order");
        viewOrder.addActionListener(this);
        order.add(viewCart);
        order.add(viewOrder);
        
        menuBar.add(book);
        menuBar.add(order);
        
        // Title Panel (NORTH)
        
      checkCustTrans();
      
    }
    public void checkCustTrans() {
    	ResultSet rs;
    	  String queryTransaction = "SELECT COUNT(*) FROM header_order where userid = '%s'";
          queryTransaction = String.format(queryTransaction, u.getId());
          rs = dc.executeQuery(queryTransaction);
          int totalTrans = 0;
          try {
              rs.next();
              totalTrans = Integer.parseInt(rs.getString(1));
          } catch (SQLException e) {
              e.printStackTrace();
          }
          totalTransactionLbl = new JLabel("Currently, you have done "+ totalTrans + " transaction(s)", JLabel.CENTER);
          totalTransactionLbl.setFont(new Font("Calibri", Font.PLAIN,24));
          totalTransactionLbl.setForeground(Color.WHITE);
          
          
          transactionsPanel = new JPanel();
          transactionsPanel.setBackground(Color.BLUE);
          transactionsPanel.add(totalTransactionLbl);
	}
    
    public void checkStaffTrans() {
    	ResultSet rs;
    	
    	 String queryTransaction = "SELECT COUNT(*) FROM header_order";
         rs = dc.executeQuery(queryTransaction);
         int totalTrans = 0;
         try {
             rs.next();
             totalTrans = Integer.parseInt(rs.getString(1));
         } catch (SQLException e) {
             e.printStackTrace();
         }
         
         totalTransactionLbl = new JLabel("Optimistic book store has successfully created "+ totalTrans + " transaction(s)", JLabel.CENTER);
         totalTransactionLbl.setFont(new Font("Calibri", Font.PLAIN,24));
         totalTransactionLbl.setForeground(Color.WHITE);
         
         transactionsPanel = new JPanel();
         transactionsPanel.setBackground(Color.RED);
         transactionsPanel.add(totalTransactionLbl);
		
	}
    public void initStaffMenu() {
        manage = new JMenu("Manage");
        
        manageBook = new JMenuItem("Manage Book(s)");
        manageOrder  = new JMenuItem("Manage Order(s)");
        manageAccount = new JMenuItem("Manage Account(s)");
        
        manage.add(manageBook);
        manage.add(manageOrder);
        manage.add(manageAccount);

        manageAccount.addActionListener(this);
        manageBook.addActionListener(this);
        manageOrder.addActionListener(this);
        
        menuBar.add(manage);
       
        checkStaffTrans();
    }

    public void openCart() {
    	removeAllContent();
    	add(new Cart(u,this)).setVisible(true);
    }
    
    public void checkOutBook(int totalPrice){
    	removeAllContent();
    	add(new Checkout(u, totalPrice, this)).setVisible(true);;
    }

    public void removeAllContent() {
    	getContentPane().removeAll();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageAccount) {
        	removeAllContent();
            add(new ManageAccount(u,this)).setVisible(true);
        }

        if (e.getSource() == manageBook) {
        	removeAllContent();
            add(new ManageBook(this)).setVisible(true);
        }

        if (e.getSource() == manageOrder) {
        	removeAllContent();
            add(new ManageOrder(this)).setVisible(true);
        }

        if (e.getSource() == manageProfile) {
        	removeAllContent();
            add(new ManageProfile(u,this)).setVisible(true);
        }

        if (e.getSource() == buyBook) {
        	removeAllContent();
        	BuyBook bb = new BuyBook(u, this);
            add(bb).setVisible(true);
        }

        if (e.getSource() == viewCart) {
        	openCart();
        }

        if (e.getSource() == viewOrder) {
        	removeAllContent();
            add(new ViewOrder(u,this)).setVisible(true);
        }

        if (e.getSource() == logOut) {
            this.dispose();
            new LoginForm().setVisible(true);;
        }
 
    }
    
}
