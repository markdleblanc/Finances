/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.spikes;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.table.DefaultTableModel;

import com.mistfalls.finances.models.Account;
import com.mistfalls.finances.models.Currency;
import com.mistfalls.finances.models.Report;

/**
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class JFrameSpike {
	/**
	 * The window that holds our components.
	 */
	private JFrame window;
	/**
	 * The root content pane.
	 */
	private JPanel pane;

	public JFrameSpike() {}

	public static void main(String[] arguments) {
		JFrame window = new JFrame("MistFalls Finances");
		JPanel pane = new JPanel();

		final JTextField balance	= new JTextField();
		final JTextField name		= new JTextField();

		final DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new Object[] {"Account", "Balance"});
		final JTable table = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(table);

		GroupLayout manager = new GroupLayout(pane);
		
		manager.setHorizontalGroup(manager.createParallelGroup()
			.addGroup(manager.createSequentialGroup()
				.addComponent(name, 120, 120, 120)
				.addComponent(balance, 120, 120, 120))
			.addComponent(scrollPane));
		manager.setVerticalGroup(manager.createSequentialGroup()
			.addGroup(manager.createParallelGroup()
				.addComponent(name, 22, 22, 22)
				.addComponent(balance, 22, 22, 22))
			.addComponent(scrollPane));

		manager.setAutoCreateGaps(true);
		manager.setAutoCreateContainerGaps(true);

		pane.setLayout(manager);

		window.setContentPane(pane);
		window.setVisible(true);
		window.pack();

		balance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String account_name = name.getText();
				Currency account_balance = new Currency(Integer.parseInt(balance.getText()));

				Account account = new Account(account_name, account_balance);
				Report report = new Report(account);

				model.addRow(new Object[] {name.getText(),report.getBalance()});
			}

		});

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}