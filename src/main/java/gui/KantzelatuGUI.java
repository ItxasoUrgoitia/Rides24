package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Bidaiari;
import domain.Driver;
import domain.Eskaera;
import domain.Ride;
import domain.Ride.RideEgoera;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class KantzelatuGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel jLabelMsg = new JLabel();

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { KantzelatuGUI frame = new
	 * KantzelatuGUI(); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	/**
	 * Create the frame.
	 */
	public KantzelatuGUI(Driver driver) {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JComboBox rides = new JComboBox();
		rides.setBounds(27, 115, 384, 22);
		contentPane.add(rides);
		BLFacade facade = MainGUI.getBusinessLogic();
		List<Ride> rideList = facade.getDriverRides(driver);
		rides.removeAllItems();
		for (Ride ride : rideList) {
			rides.addItem(ride);
		}
		jLabelMsg.setBounds(new Rectangle(27, 219, 399, 20));
		jLabelMsg.setForeground(Color.red);
		contentPane.add(jLabelMsg);
		jLabelMsg.setText("");
		JButton btnCanceled = new JButton(ResourceBundle.getBundle("Etiquetas").getString("KantzelatuGUI.Title"));
		btnCanceled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BLFacade facade = MainGUI.getBusinessLogic();
				Ride rideSelect = (Ride) rides.getSelectedItem();
				if(rideSelect != null) {
					Calendar gaur = Calendar.getInstance();
					gaur.set(Calendar.HOUR_OF_DAY, 0);
					gaur.set(Calendar.MINUTE, 0);
					gaur.set(Calendar.SECOND, 0);
				    gaur.set(Calendar.MILLISECOND, 0);
					Calendar fechaRide = Calendar.getInstance();
					fechaRide.setTime(rideSelect.getDate());
					fechaRide.set(Calendar.HOUR_OF_DAY, 0);
				    fechaRide.set(Calendar.MINUTE, 0);
				    fechaRide.set(Calendar.SECOND, 0);
				    fechaRide.set(Calendar.MILLISECOND, 0);
				    fechaRide.add(Calendar.DAY_OF_YEAR, -2);
				    if(gaur.before(fechaRide) && rideSelect.getEgoera()==RideEgoera.PENDING) {
				    	jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("KantzelatuGUI.Canceled"));
				    	facade.kantzelatuRide(rideSelect);
				    }else {
				    	jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("KantzelatuGUI.DateError"));
				    }
				}
				
			}
		});
		btnCanceled.setBounds(221, 182, 190, 23);
		contentPane.add(btnCanceled);
		
		JLabel lblNewLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("KantzelatuGUI.Title"));
		lblNewLabel.setBounds(82, 53, 242, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnFinished = new JButton(ResourceBundle.getBundle("Etiquetas").getString("KantzelatuGUI.Finish"));
		btnFinished.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ride rideSelect = (Ride) rides.getSelectedItem();
				facade.amaituRide(rideSelect);
			}
		});
		btnFinished.setBounds(21, 182, 190, 23);
		contentPane.add(btnFinished);
	}
}
