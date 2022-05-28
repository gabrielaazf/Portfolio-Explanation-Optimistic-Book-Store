package application;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class HomeForm extends JFrame implements ActionListener {
    JLabel titleLbl;

    JTextArea introductionArea;

	JButton loginBtn;
	JButton registBtn;
    JButton exitBtn;

    JPanel logRegistPanel, allButtonPanel;

    public HomeForm() {
        setLayout(new BorderLayout());
        
        init();

        setTitle("Optimistic Book Store");
		setSize(new Dimension(600, 300));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

	private void init() {
        logRegistPanel = new JPanel(new GridLayout(1,2,10,10));
        allButtonPanel = new JPanel(new BorderLayout(5,5));

        titleLbl = new JLabel("Welcome to Optimistic Book Store", JLabel.CENTER);
        titleLbl.setFont(new Font("Calibri", Font.BOLD, 24));

        introductionArea = new JTextArea("Optimistic Book Store is an online book store, which provides a lot of popular books. "+
        "Optimistic was founded in 2019 to provide high-quality books and magazines to readers in Indonesia. "+
        "\nFor the following reasons, this service is recognized as the greatest online store in Indonesia: "+
        "\n(1) Prompt delivery of best-sellers"+
        "\n(2) Ordering best-selling books and periodicals in advance for delivery to your home or workplace within two days of their official release date in Indonesia."+
        "\n(3) Convenient online buying puts your favorite books and publications at your fingertips without leaving the convenience of your home or office.");
        introductionArea.setPreferredSize(new Dimension(100,50));
        introductionArea.setBorder(new EmptyBorder(5, 15, 5, 15));
        introductionArea.setLineWrap(true);
		introductionArea.setWrapStyleWord(true);
		introductionArea.setEditable(false);
        loginBtn = new JButton("Login");
        registBtn = new JButton("Register");
        exitBtn = new JButton("Exit");
        exitBtn.setPreferredSize(new Dimension(200,25));

        loginBtn.addActionListener(this);
        registBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        logRegistPanel.add(loginBtn);
        logRegistPanel.add(registBtn);

        allButtonPanel.add(logRegistPanel,BorderLayout.CENTER);
        allButtonPanel.add(exitBtn, BorderLayout.SOUTH);

        add(titleLbl, BorderLayout.NORTH);
        add(introductionArea, BorderLayout.CENTER);
        add(allButtonPanel, BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==loginBtn){
            this.dispose();
            new LoginForm().setVisible(true);
        }

        if (e.getSource() == registBtn) {
            this.dispose();
            new RegisterForm().setVisible(true);
        }

        if (e.getSource() == exitBtn) {
            this.dispose();
        }
		
	}

}
