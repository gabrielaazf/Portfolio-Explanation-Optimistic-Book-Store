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
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import connection.DatabaseConnection;
import user.User;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Arrays;
import java.util.Vector;

public class BuyBook extends JInternalFrame implements ActionListener, MouseListener {
    DatabaseConnection dc;
    
    JPanel mainPanel, titlePanel, centerPanel, searchPanel, tablePanel, bottomPanel;
    JLabel titleLabel, bookTitleLabel, bookTitlePlaceholder, quantityLabel, itemsInCartLabel;
    JTextField searchField;
    JButton searchButton, addToCartButton, viewCartButton;
    JSpinner qtySpinner;
    
    JTable table;
    DefaultTableModel dtm;
    JScrollPane jsp;
    TableRowSorter<DefaultTableModel> ts;
    User u;
    
    MainMenu mm;
    
    Vector<Integer> bookSkuVec = new Vector<>();
    
    public BuyBook(User u, MainMenu mm) {
        dc = new DatabaseConnection();
        this.u = u;
        
        this.mm=mm;
        
        init();
        
        setTitle("Book Catalog");
        setClosable(true);
        setResizable(false);
        setSize(new Dimension(1050,500));
        
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
            	mm.welcomeText();
            	dispose();
            }
        });
    }
    
    public void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));
        
        // Title Panel (NORTH)
        titlePanel = new JPanel();
        titleLabel = new JLabel("Book Catalog");
        titlePanel.add(titleLabel);
        
        // Center Panel (CENTER)
        centerPanel = new JPanel();
        
        searchPanel = new JPanel();
        searchField = new JTextField(50);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        tablePanel = new JPanel();

//     
//        table = new JTable();
//        table.addMouseListener(this);
//        table.getTableHeader().setReorderingAllowed(false);
//        table.setEnabled(false);
//        
       initTable();
        
      


        jsp = new JScrollPane(table);
        jsp.setPreferredSize(new Dimension(1000,250));
        table.setRowSorter(ts);

        tablePanel.add(jsp);
        
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom Panel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3,3,5,5));

        bookTitleLabel = new JLabel("Book Title");
        bookTitlePlaceholder = new JLabel();

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(this);
        addToCartButton.setEnabled(false);

        quantityLabel = new JLabel("Quantity");
        qtySpinner = new JSpinner();
        qtySpinner.setEnabled(false);

        viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(this);
        
        itemsInCartLabel = new JLabel("Items in Cart:" + userItemsInCartCount());

        bottomPanel.add(bookTitleLabel);
        bottomPanel.add(bookTitlePlaceholder);
        bottomPanel.add(addToCartButton);
        bottomPanel.add(quantityLabel);
        bottomPanel.add(qtySpinner);
        bottomPanel.add(viewCartButton);
        bottomPanel.add(new JLabel());
        bottomPanel.add(new JLabel());
        bottomPanel.add(itemsInCartLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private void initTable() {
		Object [] colName = {
				"ISBN", "Title", "Genre", "Publisher", "Price", "Stock"
		};

		dtm = new DefaultTableModel (colName, 0){
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				case 5:
					return Integer.class;
				case 6:
					return Integer.class;
				default:
					return String.class;
				}
			}
		};
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
		String query = "SELECT booksku, bookisbn, bookname, bookgenre, publishername, bookprice, bookstockqty FROM book b JOIN publisher p ON p.publisherid = b.publisherid";

		rs = dc.executeQuery(query);
		try {
			while (rs.next()) {
				if(rs.getInt("bookstockqty")!=0) { 
				Vector<Object>obj = new Vector<>();
				bookSkuVec.add(rs.getInt("booksku"));
				for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
					obj.add(rs.getObject(i+1));
				}

				dtm.addRow(obj);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

//    void fetchTableData() {
//		String query = "SELECT booksku, bookisbn, bookname, bookgenre, publishername, bookprice, bookstockqty FROM book b, publisher p WHERE b.publisherid = p.publisherid";
//		ResultSet rs = dc.executeQuery(query);
//		
//		Object[] columnNamesArr = {"ISBN", "Title", "Genre", "Publisher", "Price", "Stock"};
//		Vector<Object> columnNames = new Vector<>(Arrays.asList(columnNamesArr));
//		
//		Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
//		
//		try {
//			while(rs.next()) {
//				if(rs.getInt("bookstockqty")!=0) {
//				Vector<Object> addData = new Vector<>();
//                bookSkuVec.add(rs.getInt("booksku"));
//				addData.add(rs.getString("bookisbn"));
//				addData.add(rs.getString("bookname"));
//                addData.add(rs.getString("bookgenre"));
//                addData.add(rs.getString("publishername"));
//                addData.add(rs.getString("bookprice"));
//                addData.add(rs.getString("bookstockqty"));
//				rowData.add(addData);
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		DefaultTableModel dtm = new DefaultTableModel(rowData, columnNames) {
//			public boolean isCellEditable(int row, int column) {
//				return false;
//			}
//		};
//
//        ts = new TableRowSorter<DefaultTableModel>(dtm);
//        table.setModel(dtm);
//	}

    void search() {
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

    int userItemsInCartCount() {
        String query = "SELECT * FROM cart WHERE UserID = '%s'";
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

    boolean bookExistsInCart(int sku) {
        ResultSet rs = null;
        String query = "SELECT booksku FROM cart WHERE userid = '%d'";
        query = String.format(query, u.getId());

        rs = dc.executeQuery(query);

        try {
            while (rs.next()) {
                if (rs.getInt("booksku") == sku) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }      
    String isbn, title, genre, publisher;
    int price, stock;
    int row = 0;
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            search();
        }

        if (e.getSource() == addToCartButton) {
            int sku = bookSkuVec.get(row);
            String query;
            int qty = Integer.parseInt(String.valueOf(qtySpinner.getValue()));;

            if (qty == 0) {
                return;
            }

            if (bookExistsInCart(sku)) {
                query = "UPDATE cart SET qty = qty + %d WHERE booksku = '%d'";
                query = String.format(query, qty, sku);
                dc.executeUpdate(query);

//                int oldQty = Integer.parseInt(String.valueOf(table.getValueAt(row, 5)));
//                if (oldQty > qty) {
//                    query = "UPDATE book SET bookstockqty = '%d' WHERE booksku = '%d'";
//                    query = String.format(query, stock-qty, bookSkuVec.get(row));
//                    dc.executeUpdate(query);
//                } else if (oldQty < qty) {
//                    query = "UPDATE book SET bookstockqty = '%d' WHERE booksku = '%d'";
//                    query = String.format(query, stock+qty, bookSkuVec.get(row));
//                    dc.executeUpdate(query);
//                }
            } else {
                query = "INSERT INTO cart VALUES ('%d', '%d', '%d')";
                query = String.format(query, u.getId(), bookSkuVec.get(row), qty);
                dc.executeUpdate(query);
            }
            
            query = "UPDATE book SET bookstockqty = '%d' WHERE booksku = '%d'";
            query = String.format(query, stock-qty, bookSkuVec.get(row));
            dc.executeUpdate(query);

            itemsInCartLabel.setText("Items in Cart: " + userItemsInCartCount());
            table.clearSelection();
            dtm.setRowCount(0);
           initDataTbl();
        }

        if (e.getSource() == viewCartButton) {
        	mm.openCart();
        	this.dispose();
            
        }
    }
    


	@Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == table) {
            addToCartButton.setEnabled(true);
            qtySpinner.setEnabled(true);

            row = table.rowAtPoint(e.getPoint());
            bookTitlePlaceholder.setText(String.valueOf(table.getValueAt(row, 1)));

            isbn = String.valueOf(table.getValueAt(row, 0));
            title = String.valueOf(table.getValueAt(row, 1));
            genre = String.valueOf(table.getValueAt(row, 2));
            publisher = String.valueOf(table.getValueAt(row, 3));
            price = Integer.valueOf(String.valueOf(table.getValueAt(row, 4)));
            stock = Integer.valueOf(String.valueOf(table.getValueAt(row, 5)));

            qtySpinner.setModel(new SpinnerNumberModel(1, 1, stock, 1));
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
    
}
