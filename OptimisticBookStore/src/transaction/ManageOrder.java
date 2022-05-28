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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
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

public class ManageOrder extends JInternalFrame implements ActionListener, MouseListener{

	JLabel  titleLbl, detailLbl, orderIdLbl, statusOrderLbl, orderAnsLbl;
	JTextField searchField;
	JComboBox<String> statusBox;
	Vector<String>statusList;
	JButton updateBtn, searchBtn;
	JTable tableHeader, tableDetail;
	DefaultTableModel dtmHeader, dtmDetail;
	JScrollPane jspHeader, jspDetail;
	JPanel mainPanel, panelAtas, panelBawah,panelManage, panelField, panelTitle, panelButton, panelSearch, panelAllTbl, panelDetail, panelSearchBtn;
	boolean dataSelected = false;
	DatabaseConnection dc;
	TableRowSorter<DefaultTableModel> tsHeader;

	public ManageOrder(MainMenu mainMenu) {
		dc = new  DatabaseConnection();

		init();

		setTitle("Manage Order");
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
		panelManage = new JPanel(new BorderLayout(5,5));
		panelAtas = new JPanel(new BorderLayout());
		panelAtas.setBorder(new EmptyBorder(0, 50, 0, 50));
		panelDetail = new JPanel(new BorderLayout());
		panelDetail.setBorder(new EmptyBorder(0, 50, 0, 50));
		panelAllTbl = new JPanel(new BorderLayout(5,5));
		panelAllTbl.setPreferredSize(new Dimension(1200, 500));
		panelBawah = new JPanel(new BorderLayout(20,20));
		panelBawah.setBorder(new EmptyBorder(0, 50, 20, 50));
		panelBawah.setSize(new Dimension(1200,150));
		panelButton = new JPanel();
		panelField = new JPanel(new GridLayout(2,2,8,10));
		panelField.setPreferredSize(new Dimension(800,100));
		panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
		panelSearchBtn = new JPanel();
		panelSearchBtn.setBorder(new EmptyBorder(0, 10, 0, 0));

		titleLbl = new JLabel("Manage Order");
		titleLbl.setFont(new Font("Calibri", Font.BOLD, 18));
		titleLbl.setForeground(Color.decode("#0a004e"));
		panelTitle = new JPanel();
		panelTitle.add(titleLbl);
		
		detailLbl = new JLabel("Detail Order",JLabel.CENTER);
		detailLbl.setFont(new Font("Calibri", Font.BOLD, 16));
		detailLbl.setForeground(Color.decode("#0a004e"));
		
		orderIdLbl = new JLabel("Order ID: ");
		statusOrderLbl = new JLabel("Order Status: ");

		//field
		orderAnsLbl = new JLabel("");
		statusList = new Vector<>();
		statusList.add("Waiting");
		statusList.add("Confirmed");
		statusList.add("Packed");
		statusList.add("Shipped");
		statusList.add("Completed");
		statusList.add("Cancelled");
		
		statusBox = new JComboBox<>(statusList);

		panelField.add(orderIdLbl);
		panelField.add(orderAnsLbl);
		panelField.add(statusOrderLbl);
		panelField.add(statusBox);
		
		deactivateAllField();

		initTableHeader();
		initTableDetail();
		
		searchField= new JTextField(50);
		searchField.setText("Input keyword here...");
		searchField.setPreferredSize(new Dimension(50,24));
		searchBtn = new JButton("Search Order");
		searchBtn.setPreferredSize(new Dimension(120,24));
		panelSearchBtn.add(searchBtn);
		searchBtn.addActionListener(this);



		panelSearch.add(searchField);
		panelSearch.add(panelSearchBtn);




		updateBtn = new JButton("Update");
		updateBtn.setPreferredSize(new Dimension(150,30));
		panelButton.setBorder(new EmptyBorder(65, 100, 0, 0));
		panelButton.add(updateBtn);
		updateBtn.addActionListener(this);
		

		panelTitle.add(titleLbl);
		panelAtas.add(jspHeader, BorderLayout.CENTER);
		panelAtas.add(panelSearch, BorderLayout.SOUTH);
		
		
		
		panelAllTbl.add(panelAtas, BorderLayout.CENTER);
		panelAllTbl.add(panelDetail, BorderLayout.SOUTH);

		panelBawah.add(panelField, BorderLayout.CENTER);
		panelBawah.add(panelButton, BorderLayout.EAST);
		panelManage.add(panelAllTbl, BorderLayout.CENTER);
		panelManage.add(panelBawah, BorderLayout.SOUTH);


		mainPanel.add(panelTitle, BorderLayout.NORTH);
		mainPanel.add(panelManage, BorderLayout.CENTER);
		add(mainPanel);


	}

	private void initTableHeader() {

		Object [] colName = {
				"OrderID","CustomerName", "Address", "PhoneNumber", "PaymentMethod", "Transaction Date", "Status Order"
		};

		dtmHeader = new DefaultTableModel (colName, 0){
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
					return String.class;
				default:
					return String.class;
				}
			}
		};
		tableHeader = new JTable(dtmHeader);
		jspHeader = new JScrollPane();
		jspHeader.setViewportView(tableHeader);
		jspHeader.setPreferredSize(new Dimension(1200,250));
		tsHeader = new TableRowSorter<DefaultTableModel>(dtmHeader);
		tableHeader.setRowSorter(tsHeader);

		initDataTbl();

		tableHeader.addMouseListener(this);

	}

	void initDataTbl() {
		ResultSet rs;
		String query = "SELECT orderid, userfullname, useraddress, userphonenumber, paymentmethodname, transactiondate, statusorder FROM header_order ho JOIN user u ON u.userid = ho.userid JOIN payment_method pm ON pm.paymentmethodid = ho.paymentmethodid";

		rs = dc.executeQuery(query);
		try {
			while (rs.next()) {
				Vector<Object>obj = new Vector<>();

				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					obj.add(rs.getObject(i+1));
				}

				dtmHeader.addRow(obj);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	void initTableDetail() {
		Object [] colName = {
				"Book Name","Book Publisher", "Qty of Order"
		};
		
		dtmDetail = new DefaultTableModel(colName, 0);
		tableDetail = new JTable(dtmDetail);
		jspDetail = new JScrollPane();
		jspDetail.setViewportView(tableDetail);
		jspDetail.setPreferredSize(new Dimension(1200,100));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==updateBtn) {
			if(!dataSelected) {
				JOptionPane.showMessageDialog(null, "Please select data!", "Warning", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this order?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(res==JOptionPane.YES_OPTION) {
				updateOrder();
				dtmHeader.setRowCount(0);
				initDataTbl();
				deactivateAllField();
				panelDetail.remove(detailLbl);
				panelDetail.remove(jspDetail);
			}
		}
		
		if(e.getSource()==searchBtn) {
			search();
			return;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==tableHeader) {
			dataSelected = false;
			dtmDetail.setRowCount(0);
			
			panelDetail.add(detailLbl, BorderLayout.NORTH);
			panelDetail.add(jspDetail, BorderLayout.CENTER);
			
			int row = tableHeader.getSelectedRow();
			String idString = tableHeader.getValueAt(row, 0).toString();
			if(!idString.isEmpty())
				dataSelected=true;

			if(dataSelected) {
				orderAnsLbl.setText(idString);
				statusBox.setSelectedItem(tableHeader.getValueAt(row, 6));
				
				activateAllField();
				
				addDetailData(idString);

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

	private void addDetailData(String orderId) {
		ResultSet rs;
		
		String query = "SELECT bookname, publishername, qty FROM detail_order o JOIN book b ON o.bookSKU = b.bookSKU JOIN publisher p ON p.publisherid = b.publisherid WHERE orderid = %s";
		
		query = String.format(query, orderId);
		
		try {
			rs = dc.executeQuery(query);
			while(rs.next()) {
				Vector<Object>obj = new Vector<>();

				for(int i=1;i<=rs.getMetaData().getColumnCount();i++) {
					obj.add(rs.getObject(i));
				};
				
				dtmDetail.addRow(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void updateOrder() {
		String query = "UPDATE header_order SET statusorder = '%s' WHERE orderid = '%s' ";
		
		query = String.format(query, statusBox.getSelectedItem().toString(), orderAnsLbl.getText());
		
		dc.executeUpdate(query);
		
		JOptionPane.showMessageDialog(this, "Order Updated!", "Updated", JOptionPane.INFORMATION_MESSAGE);
		
	}

	public void activateAllField() {

		orderAnsLbl.setEnabled(true);
		statusBox.setEnabled(true);
		
	}

	private void deactivateAllField() {
		orderAnsLbl.setEnabled(false);
		statusBox.setEnabled(false);
		
		orderAnsLbl.setText("");
		statusBox.setSelectedItem(null);

	}

	private void search() {
		String temp = searchField.getText().trim();

		if(temp.isEmpty()){
			tsHeader.setRowFilter(null);
		}

		else if(temp.length()<3){
			JOptionPane.showMessageDialog(null, "Please enter more than or equal 3 characters");
			return;
		}

		else if(temp.length()>=3 ){
			tsHeader.setRowFilter(RowFilter.regexFilter("(?i)"+temp));

			//(?i) -> case insensitive
		} 
	}

}