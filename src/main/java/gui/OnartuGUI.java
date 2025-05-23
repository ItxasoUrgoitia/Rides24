package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Bidaiari;
import domain.Car;
import domain.Driver;
import domain.Eskaera;
import domain.Ride;
import domain.Ride.RideEgoera;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class OnartuGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel jLabelMsg = new JLabel();
	private JComboBox comboBox = new JComboBox();

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { OnartuGUI frame = new OnartuGUI();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */

	/**
	 * Create the frame.
	 */
	public OnartuGUI(Driver driver) {
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("OnartuGUI.Title"));
		setBounds(100, 100, 614, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JComboBox rides = new JComboBox();
		rides.setBounds(10, 78, 580, 22);
		BLFacade facade = MainGUI.getBusinessLogic();
		//List<Bidaiari> bidaiariList = facade.getAllBidaiari();
		rides.removeAllItems();
		
		List<Ride> rideList = facade.getDriverRides(driver);
		for (Ride ride : rideList) {
			if (ride.getEgoera()==RideEgoera.PENDING) {
				rides.addItem(ride);
			}
		}
		
		contentPane.add(rides);

		jLabelMsg.setBounds(new Rectangle(81, 214, 305, 20));
		jLabelMsg.setForeground(Color.red);
		contentPane.add(jLabelMsg);

		JLabel lbl_Title = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("OnartuGUI.Title"));
		lbl_Title.setBounds(49, 35, 335, 32);
		contentPane.add(lbl_Title);

		JButton btnSHesk = new JButton(ResourceBundle.getBundle("Etiquetas").getString("OnartuGUI.Accept"));
		btnSHesk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ride selectedRide = (Ride) rides.getSelectedItem();
				if(selectedRide!=null) {
					JFrame a = new OnartuGUI2(selectedRide);
					a.setVisible(true);
				}
				
			}

		});
		btnSHesk.setBounds(244, 180, 89, 23);
		contentPane.add(btnSHesk);
	}
}

/*
 * if ((comboBox_1.getSelectedItem() != null) && (rides.getSelectedItem() != null)) {
					try {
						Eskaera selectedEskaera = (Eskaera) rides.getSelectedItem();
						Calendar gaur = Calendar.getInstance();
						gaur.set(Calendar.HOUR_OF_DAY, 0);
						gaur.set(Calendar.MINUTE, 0);
						gaur.set(Calendar.SECOND, 0);
					    gaur.set(Calendar.MILLISECOND, 0);
						Calendar fechaRide = Calendar.getInstance();
						fechaRide.setTime(selectedEskaera.getDate());
						fechaRide.set(Calendar.HOUR_OF_DAY, 0);
					    fechaRide.set(Calendar.MINUTE, 0);
					    fechaRide.set(Calendar.SECOND, 0);
					    fechaRide.set(Calendar.MILLISECOND, 0);
					    if((gaur.equals(fechaRide)||gaur.before(fechaRide)) && facade.diruaSartu(selectedEskaera.getBidaiari(), selectedEskaera.getPrez()*(-1))) {
					    	facade.addMovement( selectedEskaera.getPrez(), "-", selectedEskaera.getBidaiari());
					    	facade.jarri(true, selectedEskaera);
							float prezioa = selectedEskaera.getPrez();
							Car selectedCar = (Car) comboBox_1.getSelectedItem();
							int inputSeats = selectedCar.getPlaces();
							try {

								facade.createRide(selectedEskaera.getFrom(), selectedEskaera.getTo(), selectedEskaera.getDate(),
									inputSeats, prezioa, driver.getEmail());
								jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("OnartuGUI.Accepted"));
							} catch (RideMustBeLaterThanTodayException e1) {
								System.out.println("Error: La fecha del viaje debe ser posterior a hoy.");
								jLabelMsg.setText(e1.getMessage());
							} catch (RideAlreadyExistException e2) {
								System.out.println("Error: Ya existe un viaje con esos datos.");
								jLabelMsg.setText(e2.getMessage());
							}
					    }else {
					    	jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("Error"));
					    }
					} catch (NumberFormatException o) {
						System.out.println("Error: el texto del JLabel no es un número válido.");
					}
				}else {
					jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("Error"));
				}
				/*
				 * try { BLFacade facade = MainGUI.getBusinessLogic(); Eskaera selectedEskaera =
				 * (Eskaera) comboBox.getSelectedItem(); try { float prezioa =
				 * Float.parseFloat(lbl_Prezioa.getText()); int numSeat =
				 * Integer.parseInt(lbl_numSeat.getText());
				 * facade.createRide(selectedEskaera.getFrom(),selectedEskaera.getTo(),
				 * selectedEskaera.getDate(), numSeat, prezioa, driver.getEmail()); } catch
				 * (NumberFormatException o) {
				 * System.out.println("Error: el texto del JLabel no es un número válido."); } }
				 * catch (RideMustBeLaterThanTodayException e1) { // TODO Auto-generated catch
				 * block jLabelMsg.setText(e1.getMessage()); } catch (RideAlreadyExistException
				 * e1) { // TODO Auto-generated catch block jLabelMsg.setText(e1.getMessage());
				 * }
				 */
 

