package transaction;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import connection.DatabaseConnection;
import user.User;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Cart extends JInternalFrame implements ActionListener, MouseListener {
    
    DatabaseConnection dc;
    JPanel mainPanel, titlePanel, centerPanel, searchPanel, tablePanel, bottomPanel;
    JLabel titleLabel, bookTitleLabel, bookTitlePlaceholder, quantityLabel, itemsInCartLabel;
    JTextField searchField;
    JButton searchButton, deleteButton, checkoutButton, updateButton;
    JSpinner qtySpinner;
    
    JTable table;
    DefaultTableModel dtm;
    JScrollPane jsp;
    TableRowSorter<DefaultTableModel> ts;
    User u;
    int totalPrice = 0;
    int maxValue = 0;
    
    
    Vector<Integer> bookSkuVec = new Vector<>();
    MainMenu mm;
    
    public Cart(User u, MainMenu mainMenu) {
        dc = new DatabaseConnection();
        this.u = u;
        this.mm= mainMenu;
        
        init();
        
        setTitle("Cart");
        setClosable(true);
        setResizable(false);
        
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                mainMenu.welcomeText();
                dispose();
            }
        });
    }
    
    void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));
        
        // Title Panel (NORTH)
        titlePanel = new JPanel();
        titleLabel = new JLabel("Your Cart");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        
        // Center Panel (CENTER)
        centerPanel = new JPanel();
        tablePanel = new JPanel();
        
        fetchTableData();
        
        table.setRowSorter(ts);
        
        tablePanel.add(jsp);
        
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Bottom Panel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3,3,5,5));
        
        bookTitleLabel = new JLabel("Book Title");
        bookTitlePlaceholder = new JLabel();
        
        deleteButton = new JButton("Delete from Cart");
        deleteButton.addActionListener(this);
        deleteButton.setEnabled(false);
        
        quantityLabel = new JLabel("Quantity");
        qtySpinner = new JSpinner();
        qtySpinner.setEnabled(false);
        
        qtySpinner.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                int spinValue=Integer.parseInt(qtySpinner.getValue().toString());
                if(spinValue==0) {
                    JOptionPane.showMessageDialog(null, "Quantity minimum is 1", "Message", JOptionPane.INFORMATION_MESSAGE);
                    qtySpinner.setValue(1);
                    spinValue=1;
                }
                
                else if(spinValue>maxValue&&maxValue!=0) {
                    JOptionPane.showMessageDialog(null, "Quantity cannot be more than available stock", "Message", JOptionPane.INFORMATION_MESSAGE);
                    qtySpinner.setValue(maxValue);
                    spinValue=maxValue;
                    qtySpinner.setValue(maxValue);
                }
                
            }
        });
        
        updateButton = new JButton("Update Qty");
        updateButton.addActionListener(this);
        updateButton.setEnabled(false);
        
        checkoutButton = new JButton("Checkout");
        if(dtm.getRowCount()==0) {
            checkoutButton.setEnabled(false);
        }
        checkoutButton.addActionListener(this);
        
        itemsInCartLabel = new JLabel("Items in Cart:" + userItemsInCartCount());
        
        bottomPanel.add(bookTitleLabel);
        bottomPanel.add(bookTitlePlaceholder);
        bottomPanel.add(deleteButton);
        bottomPanel.add(quantityLabel);
        bottomPanel.add(qtySpinner);
        bottomPanel.add(updateButton);
        bottomPanel.add(new JLabel());
        bottomPanel.add(new JLabel());
        bottomPanel.add(checkoutButton);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    
    
    void fetchTableData() {
        Object[] columnNamesArr = {"ISBN", "Title", "Genre", "Publisher", "Price", "Quantity"};
        dtm = new DefaultTableModel(columnNamesArr,0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(dtm);
        ts = new TableRowSorter<DefaultTableModel>(dtm);
        
        table.addMouseListener(this);
        table.getTableHeader().setReorderingAllowed(false);
        //		table.setEnabled(false);
        
        jsp = new JScrollPane();
        jsp.setViewportView(table);
        jsp.setPreferredSize(new Dimension(1100,450));
        
        
        String query = "SELECT b.booksku, b.bookisbn, b.bookname, b.bookgenre, p.publishername, b.bookprice, qty " +
        "FROM book b, publisher p, user u, cart c " +
        "WHERE b.publisherid = p.publisherid AND c.userid = u.userid AND b.booksku = c.booksku AND c.userid = '%d'";
        
        query = String.format(query, u.getId());
        ResultSet rs = dc.executeQuery(query);
           

        try {
            while(rs.next()) {
                bookSkuVec.add(rs.getInt("booksku"));
                totalPrice += (rs.getInt("bookprice") * rs.getInt("qty"));
                Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
                Vector<Object> addData = new Vector<>();
                addData.add(rs.getString("bookisbn"));
                addData.add(rs.getString("bookname"));
                addData.add(rs.getString("bookgenre"));
                addData.add(rs.getString("publishername"));
                addData.add(rs.getString("bookprice"));
                addData.add(rs.getString("qty"));
                //				rowData.add(addData);
                dtm.addRow(addData);
            }
            
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    int userItemsInCartCount() {
        String query = "SELECT * FROM cart WHERE UserID = '%d'";
        query = String.format(query, u.getId());
        
        ResultSet rs = dc.executeQuery(query);
        
        int count = 0;
        try {
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    int bookStock(int sku) {
        ResultSet rs = null;
        String query = "SELECT bookstockqty FROM book WHERE booksku = '%d'";
        query = String.format(query, bookSkuVec.get(row));

        rs = dc.executeQuery(query);

        try {
            while (rs.next()) {
                return rs.getInt("bookstockqty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == table) {
            int currentStock=0;
            
            deleteButton.setEnabled(true);
            updateButton.setEnabled(true);
            qtySpinner.setEnabled(true);
            
            row = table.rowAtPoint(e.getPoint());
            bookTitlePlaceholder.setText(String.valueOf(table.getModel().getValueAt(row, 1)));
            qtySpinner.setValue(Integer.parseInt((String) table.getValueAt(row, 5)));
            isbn = String.valueOf(table.getModel().getValueAt(row, 0));
            title = String.valueOf(table.getModel().getValueAt(row, 1));
            genre = String.valueOf(table.getModel().getValueAt(row, 2));
            publisher = String.valueOf(table.getModel().getValueAt(row, 3));
            price = Integer.valueOf(String.valueOf(table.getModel().getValueAt(row, 4)));
            stock = Integer.valueOf(String.valueOf(table.getModel().getValueAt(row, 5)));
            
            String queryStock = "SELECT bookstockqty from book WHERE bookisbn = '%s'";
            queryStock = String.format(queryStock, isbn);
            
            ResultSet rs = dc.executeQuery(queryStock);
            
            try {
                rs.next();
                currentStock = rs.getInt("bookstockqty");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
            maxValue = currentStock+(Integer.parseInt((String) table.getValueAt(row, 5)));
        }
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
    
    String isbn, title, genre, publisher;
    int price, stock;
    int row = 0;
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource()==deleteButton||e.getSource()==updateButton) {
            if (e.getSource() == deleteButton) {
                String query = "DELETE FROM cart WHERE booksku = '%d' and userid = '%d'";
                query = String.format(query, bookSkuVec.get(row), u.getId());
                dc.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Book removed from cart!");
                
            }
            if (e.getSource() == updateButton) {
                
                String query = "UPDATE cart SET qty = '%d' WHERE booksku = '%d' and userid = '%d'";
                int qty = Integer.parseInt(String.valueOf(qtySpinner.getValue()));
                query = String.format(query, qty, bookSkuVec.get(row), u.getId());
                dc.executeUpdate(query);
                
                int oldQty = Integer.parseInt(String.valueOf(table.getValueAt(row, 5)));
                
                if (oldQty > qty) {
                    query = "UPDATE book SET bookstockqty = bookstockqty+'%d' WHERE booksku = '%d'";
                    query = String.format(query, (oldQty-qty), bookSkuVec.get(row));
                    dc.executeUpdate(query);
                } else if (oldQty < qty) {
                    query = "UPDATE book SET bookstockqty =bookstockqty-'%d' WHERE booksku = '%d'";
                    query = String.format(query, (qty-oldQty), bookSkuVec.get(row));
                    dc.executeUpdate(query);
                }
                
            } 
            this.dispose();
            mm.openCart();
            return;
        }
        if (e.getSource() == checkoutButton) {
            this.dispose();
            mm.checkOutBook(totalPrice);
        }
        
    }
 
}
