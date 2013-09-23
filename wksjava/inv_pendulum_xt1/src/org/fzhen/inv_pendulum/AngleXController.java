package org.fzhen.inv_pendulum;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;

public class AngleXController {

	private JFrame frame;
	private JTextField textField_M;
	private JTextField textField_m;
	private JTextField textField_l;
	private JTextField textField_x;
	private JTextField textField_v;
	private JTextField textField_t;
	private JTextField textField_w;
	
	
	private AngleXEmulator emulator = new AngleXEmulator();
	private Timer timer;
	private double cycle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(
				            UIManager.getSystemLookAndFeelClassName());
					AngleXController window = new AngleXController();
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
	public AngleXController() {
		initialize();
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{48, 66};
		gbl_panel.rowHeights = new int[]{21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel label = new JLabel("小车质量");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		
		textField_M = new JTextField();
		textField_M.setText("10");
		GridBagConstraints gbc_textField_M = new GridBagConstraints();
		gbc_textField_M.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_M.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_M.insets = new Insets(0, 0, 5, 0);
		gbc_textField_M.gridx = 1;
		gbc_textField_M.gridy = 0;
		panel.add(textField_M, gbc_textField_M);
		textField_M.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("摆杆质量");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		textField_m = new JTextField();
		textField_m.setText("1");
		GridBagConstraints gbc_textField_m = new GridBagConstraints();
		gbc_textField_m.insets = new Insets(0, 0, 5, 0);
		gbc_textField_m.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_m.gridx = 1;
		gbc_textField_m.gridy = 1;
		panel.add(textField_m, gbc_textField_m);
		textField_m.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("摆杆长度");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField_l = new JTextField();
		textField_l.setText("1");
		GridBagConstraints gbc_textField_l = new GridBagConstraints();
		gbc_textField_l.insets = new Insets(0, 0, 5, 0);
		gbc_textField_l.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_l.gridx = 1;
		gbc_textField_l.gridy = 2;
		panel.add(textField_l, gbc_textField_l);
		textField_l.setColumns(10);
		
		JLabel label_2 = new JLabel("初始值设置   ");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.WEST;
		gbc_label_2.gridwidth = 2;
		gbc_label_2.fill = GridBagConstraints.VERTICAL;
		gbc_label_2.insets = new Insets(0, 0, 5, 0);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 3;
		panel.add(label_2, gbc_label_2);
		
		JLabel label_1 = new JLabel("初位移");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 4;
		panel.add(label_1, gbc_label_1);
		
		textField_x = new JTextField();
		textField_x.setText("50");
		GridBagConstraints gbc_textField_x = new GridBagConstraints();
		gbc_textField_x.insets = new Insets(0, 0, 5, 0);
		gbc_textField_x.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_x.gridx = 1;
		gbc_textField_x.gridy = 4;
		panel.add(textField_x, gbc_textField_x);
		textField_x.setColumns(10);
		
		JLabel label_3 = new JLabel("初速度");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 5;
		panel.add(label_3, gbc_label_3);
		
		textField_v = new JTextField();
		textField_v.setText("0");
		GridBagConstraints gbc_textField_v = new GridBagConstraints();
		gbc_textField_v.insets = new Insets(0, 0, 5, 0);
		gbc_textField_v.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_v.gridx = 1;
		gbc_textField_v.gridy = 5;
		panel.add(textField_v, gbc_textField_v);
		textField_v.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("初角度");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 6;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textField_t = new JTextField();
		textField_t.setText("0.1");
		GridBagConstraints gbc_textField_t = new GridBagConstraints();
		gbc_textField_t.insets = new Insets(0, 0, 5, 0);
		gbc_textField_t.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_t.gridx = 1;
		gbc_textField_t.gridy = 6;
		panel.add(textField_t, gbc_textField_t);
		textField_t.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("初角速度");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 7;
		panel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		textField_w = new JTextField();
		textField_w.setText("0");
		GridBagConstraints gbc_textField_w = new GridBagConstraints();
		gbc_textField_w.insets = new Insets(0, 0, 5, 0);
		gbc_textField_w.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_w.gridx = 1;
		gbc_textField_w.gridy = 7;
		panel.add(textField_w, gbc_textField_w);
		textField_w.setColumns(10);
		
		JButton button = new JButton("开始");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double M = Double.valueOf(textField_M.getText());
				double m = Double.valueOf(textField_m.getText());
				double l = Double.valueOf(textField_l.getText());
				double x = Double.valueOf(textField_x.getText());
				double v = Double.valueOf(textField_v.getText());
				double t = Double.valueOf(textField_t.getText());
				double w = Double.valueOf(textField_w.getText());
				
				cycle = 1e-3;
				emulator.init(M, m, l, x, v, t,w);
				if (timer != null){
					try{
					timer.stop();
					timer.removeActionListener(timer.getActionListeners()[0]);
					}catch (Exception e1){
						
					}
				}
				timer = new Timer(1, new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						frame.repaint();
						emulator.getEmulator().emulate(cycle);
					}
				});
				timer.start();
			}
		});
		
		JButton button_4 = new JButton("添加干扰");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emulator.disturb();
			}
		});
		GridBagConstraints gbc_button_4 = new GridBagConstraints();
		gbc_button_4.insets = new Insets(0, 0, 5, 5);
		gbc_button_4.gridx = 0;
		gbc_button_4.gridy = 8;
		panel.add(button_4, gbc_button_4);
		
		JButton button_5 = new JButton("去除干扰");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emulator.undisturb();
			}
		});
		GridBagConstraints gbc_button_5 = new GridBagConstraints();
		gbc_button_5.insets = new Insets(0, 0, 5, 0);
		gbc_button_5.gridx = 1;
		gbc_button_5.gridy = 8;
		panel.add(button_5, gbc_button_5);
		
		JButton btnNewButton = new JButton("重置参数");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_M.setText("10");
				textField_m.setText("1");
				textField_l.setText("1");
				textField_x.setText("50");
				textField_v.setText("0");
				textField_t.setText("0.1");
				textField_w.setText("0");
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.EAST;
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 10;
		panel.add(btnNewButton, gbc_btnNewButton);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 0;
		gbc_button.gridy = 11;
		panel.add(button, gbc_button);
		
		JButton button_2 = new JButton("停止");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (timer == null)
					return;
				timer.stop();
				timer.removeActionListener(timer.getActionListeners()[0]);
				timer = null;
			}
		});
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.insets = new Insets(0, 0, 5, 0);
		gbc_button_2.gridx = 1;
		gbc_button_2.gridy = 11;
		panel.add(button_2, gbc_button_2);
		
		JButton button_3 = new JButton("暂停");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (timer == null)
					return;
				timer.stop();
			}
		});
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.insets = new Insets(0, 0, 5, 5);
		gbc_button_3.gridx = 0;
		gbc_button_3.gridy = 12;
		panel.add(button_3, gbc_button_3);
		
		JButton button_6 = new JButton("继续");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (timer == null)
					return;
				timer.start();
			}
		});
		GridBagConstraints gbc_button_6 = new GridBagConstraints();
		gbc_button_6.insets = new Insets(0, 0, 5, 0);
		gbc_button_6.gridx = 1;
		gbc_button_6.gridy = 12;
		panel.add(button_6, gbc_button_6);
		
		JButton btnx = new JButton("加速(x2)");
		btnx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cycle *= 2;
			}
		});
		GridBagConstraints gbc_btnx = new GridBagConstraints();
		gbc_btnx.insets = new Insets(0, 0, 5, 5);
		gbc_btnx.gridx = 0;
		gbc_btnx.gridy = 14;
		panel.add(btnx, gbc_btnx);
		
		JButton button_1 = new JButton("减速(/2)");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cycle /= 2;
			}
		});
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(0, 0, 5, 0);
		gbc_button_1.gridx = 1;
		gbc_button_1.gridy = 14;
		panel.add(button_1, gbc_button_1);
		
		JButton button_7 = new JButton("重置速度");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cycle = 1e-3;
			}
		});
		GridBagConstraints gbc_button_7 = new GridBagConstraints();
		gbc_button_7.insets = new Insets(0, 0, 0, 5);
		gbc_button_7.gridx = 0;
		gbc_button_7.gridy = 15;
		panel.add(button_7, gbc_button_7);
		
		
		DisplayPanel displayPanel = new DisplayPanel(emulator);
		splitPane.setRightComponent(displayPanel);
	}
}
