package pay;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.sql.*;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

class PaymentController{
	int location ;
    DBMgr dbm = new DBMgr();
    public boolean verify(String uid, String pwd){
        String[][] data = dbm.getAccount();
        for(int i = 0 ; i<3 ; i++){
            if(uid.equals(data[0][i])){
                if(pwd.equals(data[1][i])){
                    location=i;
                    return true;        
                }
            }
        }
        return false;
    }
    
    /*public void showQRcode(){
        System.out.println("This is QRcode:\n囗");
    }*/
    
    public void check(int amount){
        int[] data2 = dbm.getBalance();
            if(data2[location]<amount){
            	JOptionPane.showMessageDialog(null, "餘額不足，交易取消");    
            }else{
                paying(data2[location],location,amount);
            }
    }   
    public void paying(int balance,int location, int amount){
        dbm.saveBalance(balance,amount,location);
    }
}


class DBMgr{
	Account a = new Account();
	//enterAmount c = new enterAmount();
    public String[][] getAccount(){
        return a.getPayerData();   
    }
    public int[] getBalance(){
        return a.getBalance();
    }
    
    public void saveBalance(int balance,int amount,int location){
        a.updateBalance(location, amount, balance);//更新陣列的餘額
        int[] data = a.getBalance();
        JOptionPane.showMessageDialog(null, "您的餘額剩：" + data[location]);
        new selectPayFrm();
    }
    
    
}

class Account {	
	private String uid;
    String payerData[][] = {{"user1","user2","user3"},{"123456","654321","123456"}};
    int balance[] = {950,99999,1};
    public String[][] getPayerData(){
        return payerData;
    }
    public int[] getBalance(){
        return balance;
    }
    public void updateBalance(int location,int amount,int balance){
        this.balance[location] = balance-amount; 
    }
}

public class GUI extends JFrame{

	private JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI(){
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 *
	 */
	private void initialize(){
		//登入畫面物件設置
		frame = new JFrame("Payment");
		frame.setBounds(100, 100, 402, 517);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JLabel UidLab = new JLabel("Uid:");
		UidLab.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		UidLab.setBounds(46, 146, 71, 23);
		frame.getContentPane().add(UidLab);
		
		JLabel PwdLab = new JLabel("Password:");
		PwdLab.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		PwdLab.setBounds(46, 222, 121, 23);
		frame.getContentPane().add(PwdLab);
		
		final JLabel info = new JLabel("");
		info.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		info.setForeground(Color.RED);
		info.setBounds(135, 394, 160, 23);
		frame.getContentPane().add(info);
		
		final JPasswordField passwordField = new JPasswordField();
		passwordField.setEchoChar('*');
		passwordField.setBounds(149, 221, 168, 29);
		frame.getContentPane().add(passwordField);
		
		final JTextField txtUid = new JTextField();
		txtUid.setBounds(149, 145, 168, 29);
		frame.getContentPane().add(txtUid);
		txtUid.setColumns(10);

		final PaymentController pc = new PaymentController();
		
		//verify button
		JButton btnNewButton = new JButton("Verify");
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {					
				
					final String pwd,uid;
					uid = txtUid.getText();
					pwd = passwordField.getText();
					if(pc.verify(uid, pwd)==true){
						frame.setVisible(false);
						new selectPayFrm();//開啟選擇付款方式表單
					}else{
						info.setText("Login failed.");
					}
			}
		});
		btnNewButton.setBounds(135, 321, 111, 31);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblPlease = new JLabel("Please enter your Uid and Password.");
		lblPlease.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		lblPlease.setBounds(31, 65, 334, 23);
		frame.getContentPane().add(lblPlease);		
		
	}

}

class selectPayFrm extends JFrame {
	public selectPayFrm(){
		final JFrame selectPay = new JFrame("Payment");
		selectPay.setBounds(100, 100, 402, 517);
		selectPay.show();
		selectPay.getContentPane().setLayout(null);
		selectPay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		selectPay.setContentPane(contentPane);
		contentPane.setLayout(null);
	
		JLabel lblNewLabel = new JLabel("Please choose pay method.");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		lblNewLabel.setBounds(70, 59, 247, 23);
		contentPane.add(lblNewLabel);
		
		
		//EPS Account button
		JButton btnNewButton = new JButton("EPS Account");
		btnNewButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {		
				selectPay.setVisible(false);
				new enterAmount(); //開啟付款金額表單
		}
	});
	btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));
	btnNewButton.setBounds(112, 148, 157, 31);
	contentPane.add(btnNewButton);
	
	JButton btnNewButton_1 = new JButton("Credit Card");
	btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
	btnNewButton_1.setBounds(112, 241, 157, 31);
	contentPane.add(btnNewButton_1);
	
	JButton btnNewButton_2 = new JButton("Bank Account");
	btnNewButton_2.setFont(new Font("Times New Roman", Font.PLAIN, 20));
	btnNewButton_2.setBounds(112, 347, 157, 31);
	contentPane.add(btnNewButton_2);
	}
}

class enterAmount extends GUI{
	public enterAmount(){
		final JFrame enterAmount = new JFrame("Payment");
		enterAmount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		enterAmount.setBounds(100, 100, 402, 517);
		enterAmount.show();
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		enterAmount.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JLabel lblNewLabel = new JLabel("Enter amount:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		lblNewLabel.setBounds(30, 90, 128, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblBalance = new JLabel("");
		lblBalance.setBounds(75, 295, 272, 23);
		contentPane.add(lblBalance);
		
		final JTextField txtAmount = new JTextField();
		txtAmount.setBounds(173, 89, 174, 29);
		contentPane.add(txtAmount);
		txtAmount.setColumns(10);
		
		//ok button
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int amount;
				amount = Integer.parseInt(txtAmount.getText());
				PaymentController pc = new PaymentController();
					pc.check(amount);
			}
		});
		btnOK.setBounds(132, 163, 111, 31);
		contentPane.add(btnOK);
	}
}
