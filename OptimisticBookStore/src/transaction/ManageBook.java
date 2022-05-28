package transaction;
import javax.swing.JInternalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ManageBook extends JInternalFrame implements ActionListener, MouseListener{

	JLabel  titleLbl, skuLbl, isbnLbl, nameLbl, genreLbl, publisherLbl, priceLbl, stockLbl, bookSkuGeneratedLbl;
	JTextField isbnField, nameField, priceField, searchField;
	JComboBox<String> publisherBox;
	JComboBox<String> genreBox;
	Vector<String>publisherList;
	Vector<String>genreList;
	JSpinner stockSpinner;
	JRadioButton staffButton, customerButton;
	JButton addNewBookBtn, insertBtn, updateBtn, deleteBtn, searchBtn;
	JTable table;
	DefaultTableModel dtm;
	JScrollPane jsp;
	JPanel mainPanel, panelAtas, panelBawah,panelManage, panelField, panelTitle, genderPanel, rolePanel, phonePanel, panelButton, panelAddNewBook, panelSearch, panelSearchAdd;
	boolean dataSelected = false;
	DatabaseConnection dc;
	TableRowSorter<DefaultTableModel> ts;

	public ManageBook(MainMenu mainMenu) {
		dc = new  DatabaseConnection();

		init();

		setTitle("Manage Book");
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
		panelButton = new JPanel(new GridLayout(3,1,40,10));
		panelField = new JPanel(new GridLayout(7,2,5,5));
		panelField.setPreferredSize(new Dimension(800,300));
		panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		panelSearchAdd = new JPanel(new BorderLayout(0,50));

		titleLbl = new JLabel("Manage Book");
		titleLbl.setFont(new Font("Calibri", Font.BOLD, 18));
		titleLbl.setForeground(Color.decode("#0a004e"));
		panelTitle = new JPanel();
		panelTitle.add(titleLbl);
		skuLbl = new JLabel("Book SKU: ");
		isbnLbl = new JLabel("Book ISBN: ");
		nameLbl= new JLabel("Book Name/Title: ");
		genreLbl = new JLabel("Book Genre: ");
		publisherLbl = new JLabel("Book Publisher : ");
		priceLbl = new JLabel("Book Price: ");
		stockLbl = new JLabel("Current Stock: ");

		//field
		bookSkuGeneratedLbl = new JLabel("");
		isbnField = new JTextField();
		isbnField.setEnabled(false);
		nameField = new JTextField();
		nameField.setEnabled(false);
		priceField = new JTextField();
		priceField.setEnabled(false);
		generatePublisherComboBox();
		generateGenreBox();
		stockSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
		stockSpinner.setEnabled(false);



		panelField.add(skuLbl);
		panelField.add(bookSkuGeneratedLbl);
		panelField.add(isbnLbl);
		panelField.add(isbnField);
		panelField.add(nameLbl);
		panelField.add(nameField);
		panelField.add(genreLbl);
		panelField.add(genreBox);
		panelField.add(publisherLbl);
		panelField.add(publisherBox);
		panelField.add(priceLbl);
		panelField.add(priceField);
		panelField.add(stockLbl);
		panelField.add(stockSpinner);

		initTable();
		searchField= new JTextField(50);
		searchField.setText("Input keyword here...");
		searchBtn = new JButton("Search Account");
		searchBtn.addActionListener(this);

		addNewBookBtn = new JButton("Add New Book");
		addNewBookBtn.addActionListener(this);

		panelAddNewBook = new JPanel();
		panelAddNewBook.add(addNewBookBtn);

		panelSearch.add(searchField);
		panelSearch.add(searchBtn);

		panelSearchAdd.add(panelSearch, BorderLayout.CENTER);
		panelSearchAdd.add(panelAddNewBook, BorderLayout.EAST);

		insertBtn = new JButton("Insert");
		updateBtn = new JButton("Update");
		deleteBtn= new JButton("Delete");
		panelButton.setPreferredSize(new Dimension(200, 50));
		panelButton.setBorder(new EmptyBorder(100,0,0,0));

		panelButton.add(insertBtn);
		panelButton.add(updateBtn);
		panelButton.add(deleteBtn);
		insertBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		deleteBtn.addActionListener(this);

		panelTitle.add(titleLbl);
		panelAtas.add(jsp, BorderLayout.CENTER);
		panelAtas.add(panelSearchAdd, BorderLayout.SOUTH);

		panelBawah.add(panelField, BorderLayout.CENTER);
		panelBawah.add(panelButton, BorderLayout.EAST);
		panelManage.add(panelAtas);
		panelManage.add(panelBawah);

		mainPanel.add(panelTitle, BorderLayout.NORTH);
		mainPanel.add(panelManage, BorderLayout.CENTER);
		add(mainPanel);
	}

	public void generateGenreBox() {

		genreList = new Vector<>();
		genreList.add("Science Fiction");
		genreList.add("Mystery");
		genreList.add("Education");
		genreList.add("Thriller");
		genreList.add("Horror");
		genreList.add("Historical");
		genreList.add("Romance");
		genreList.add("Western");
		genreList.add("Fantasy");
		genreList.add("Realist Literature");
		genreList.add("Other");

		genreBox = new JComboBox<>(genreList);

		genreBox.setEnabled(false);
		genreBox.setSelectedItem(null);
	}

	public void generatePublisherComboBox() {
		ResultSet rs;

		publisherList = new Vector<>();

		String query = "SELECT publishername from publisher";

		rs = dc.executeQuery(query);

		try {
			while(rs.next()) {
				publisherList.add(rs.getString("publishername"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		publisherBox = new JComboBox<> (publisherList);
		publisherBox.setEnabled(false);
		publisherBox.setSelectedItem(null);

	}



	private void initTable() {
		Object [] colName = {
				"SKU","ISBN", "Title", "Genre", "Publisher", "Price", "Stock"
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
		if(e.getSource()==addNewBookBtn) {
			activateAllField();
			if(!priceField.getText().isEmpty()) {
				clearAllField();
			}
		}

		if(e.getSource()==searchBtn)
		{

			search();
		}

		if(e.getSource()==insertBtn) {
			bookSkuGeneratedLbl.setText("");
			if(isEmptyField()) {
				return;
			}
			if(allFieldValid()) {
				insertBook();
				dtm.setRowCount(0);
				initDataTbl();
				deactivateAllField();
			}
		}

		if(e.getSource()==updateBtn) {
			if(!dataSelected) {
				JOptionPane.showMessageDialog(null, "Please select data!", "Warning", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(isEmptyField()) return;
			if(allFieldValid()) {
				updateBook();
				dtm.setRowCount(0);
				initDataTbl();
				deactivateAllField();
			}
		}
		
		if(e.getSource()==deleteBtn) {
			if(!dataSelected) {
				JOptionPane.showMessageDialog(null, "Please select data!", "Warning", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?","Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			if(res==JOptionPane.YES_OPTION) {
				deleteBook();
				dtm.setRowCount(0);
				initDataTbl();
				deactivateAllField();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==table) {
			dataSelected = false;
			int row = table.getSelectedRow();
			String isbnTemp = table.getValueAt(row, 1).toString();
			if(!isbnTemp.isEmpty())
				dataSelected=true;

			if(dataSelected) {
				bookSkuGeneratedLbl.setText(table.getValueAt(row, 0).toString());
				isbnField.setText(isbnTemp);
				nameField.setText(table.getValueAt(row, 2).toString());
				//checkgenre
				int idxGenre = checkGenre(row);
				genreBox.setSelectedIndex(idxGenre);
				//checkpublisher
				int idxPublisher = checkPublisher(row);
				publisherBox.setSelectedIndex(idxPublisher);
				priceField.setText(table.getValueAt(row, 5).toString());
				stockSpinner.setValue(Integer.parseInt(table.getValueAt(row, 6).toString()));
				activateAllField();

			}
		}
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

	public int checkPublisher(int row) {
		String publisherTemp = table.getValueAt(row, 4).toString();
		for (int i = 0; i < publisherList.size(); i++) {
			if(publisherTemp.equals(publisherList.get(i))) {
				return i;
			}
		}
		return 0;
	}

	public int checkGenre(int row) {
		String genreTemp = table.getValueAt(row, 3).toString();
		for(int i =0;i<genreList.size();i++) {
			if(genreTemp.equals(genreList.get(i))) {
				return i;
			}
		}
		return 0;
	}
	
	private void deleteBook() {
		String query = "DELETE FROM book WHERE booksku = '%s'";
		query = String.format(query, bookSkuGeneratedLbl.getText());
		
		dc.executeUpdate(query);
		
		JOptionPane.showMessageDialog(this, "Book Deleted!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
		
	}
	private void updateBook() {
		String query = "UPDATE book SET bookIsbn = '%s', bookName = '%s', bookGenre = '%s', PublisherId = '%s', bookPrice = '%s', bookStockQty = '%s' WHERE bookSku = '%s' ";
		
		int idPublisher = getPublisherId();
		
		query = String.format(query, isbnField.getText(), nameField.getText(), genreBox.getSelectedItem().toString(), Integer.toString(idPublisher), priceField.getText(), stockSpinner.getValue(), bookSkuGeneratedLbl.getText());
		
		dc.executeUpdate(query);
		
		JOptionPane.showMessageDialog(this, "Book Updated!", "Updated", JOptionPane.INFORMATION_MESSAGE);
		
	}

	private boolean isEmptyField() {
		if(isbnField.getText().isEmpty()||nameField.getText().isEmpty()||genreBox.getSelectedIndex()==-1||publisherBox.getSelectedIndex()==-1||priceField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill in the blanks", "Warning", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}
	
	public void activateAllField() {

		isbnField.setEnabled(true);
		nameField.setEnabled(true);
		priceField.setEnabled(true);
		publisherBox.setEnabled(true);
		genreBox.setEnabled(true);
		stockSpinner.setEnabled(true);

	}

	private void deactivateAllField() {
		isbnField.setEnabled(false);
		nameField.setEnabled(false);
		priceField.setEnabled(false);
		publisherBox.setEnabled(false);
		genreBox.setEnabled(false);
		stockSpinner.setEnabled(false);
		
		clearAllField();


	}

	private void clearAllField() {
		bookSkuGeneratedLbl.setText("");
		isbnField.setText("");
		nameField.setText("");
		priceField.setText("");
		publisherBox.setSelectedItem(null);
		genreBox.setSelectedItem(null);
		stockSpinner.setValue(1);
		
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

	public boolean allFieldValid() {
		if (isUsedIsbn()) {
			JOptionPane.showMessageDialog(null, "Book already inserted (ISBN can't be same)!", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if(isbnField.getText().length()!=13) {
			JOptionPane.showMessageDialog(null, "ISBN must be 13 numerical!", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!isNumberIdentification(isbnField.getText())) {
			JOptionPane.showMessageDialog(null, "Please input a valid isbn!", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if(nameField.getText().length()<5) {
			JOptionPane.showMessageDialog(null, "Book name must be more than 5 characters", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if(!isNumberIdentification(priceField.getText())) {
			JOptionPane.showMessageDialog(null, "Please input a valid price!", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if(Integer.parseInt(priceField.getText())<1000) {
			JOptionPane.showMessageDialog(null, "Price must be more than 1000", "Warning", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	public boolean isNumberIdentification(String num) {
		for (int i = 0; i < num.length(); i++) {
			try {
				Integer.parseInt(String.valueOf(num.charAt(i)));
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public boolean isUsedIsbn() {
		ResultSet rs = null;
		String isbnTemp = isbnField.getText();
		String query = "SELECT booksku,bookisbn FROM book";
		rs = dc.executeQuery(query);

		try {
			while(rs.next()){
				if (rs.getString("BookIsbn").equals(isbnTemp)) {
					if(rs.getString("bookSku").equals(bookSkuGeneratedLbl.getText()))
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

	public void insertBook() {

		int idPublisher = getPublisherId();
		String query = "INSERT INTO book (bookIsbn, bookName, bookGenre, publisherId, bookPrice, bookStockQty) VALUES ('%s','%s','%s','%s','%s','%s')";

		query = String.format(query, isbnField.getText(), nameField.getText(), genreBox.getSelectedItem().toString(), Integer.toString(idPublisher), priceField.getText(), stockSpinner.getValue().toString());

		dc.executeUpdate(query);

		JOptionPane.showMessageDialog(this, "Book Inserted!", "Inserted", JOptionPane.INFORMATION_MESSAGE);

	}

	public int getPublisherId() {

		ResultSet rs ;
		String pubName = publisherBox.getSelectedItem().toString();
		String query = "SELECT publisherid FROM publisher WHERE publishername = '%s'";
		query = String.format(query, pubName);

		rs = dc.executeQuery(query);
		try {
			rs.next();
			return rs.getInt("publisherid");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;

	}

}