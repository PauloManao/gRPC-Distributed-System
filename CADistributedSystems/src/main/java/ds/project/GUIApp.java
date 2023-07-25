package ds.project;

import ds.project.service1.Service1Grpc;
import ds.project.service1.Service1OuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.awt.EventQueue;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GUIApp extends JFrame {

	private static Service1Grpc.Service1BlockingStub blockingStub;
	private static Service1Grpc.Service1BlockingStub asyncStub;

	private ServiceInfo serviceInfo;

	private JFrame frame;

	private JPanel contentPane;
	private JTextField tFWidth;
	private JTextField tFLength;
	private JTextField tFHeigth;

	private JTextArea textResponse;
	private JTextField tFPowerConsumed;
	private JTextField tFCostElectricity;
	private JTextArea txtResponseEnergyCost;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIApp frame = new GUIApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIApp() {
		String math_service_type = "_maths._tcp.local.";
		discoverMathService(math_service_type);

		String host = serviceInfo.getHostAddresses()[0];
		int port = serviceInfo.getPort();

		ManagedChannel channel = ManagedChannelBuilder
				.forAddress(host,port)
				.usePlaintext()
				.build();

		blockingStub = Service1Grpc.newBlockingStub(channel);
		//asyncStub = Service1Grpc.newStub(channel);

		//frame = new JFrame();
		this.setTitle("Greener App");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(250, 150, 600, 500);

		this.setVisible(true);

		initialize();

	}

	private void discoverMathService(String service_type){
		try {
			JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());

			jmDNS.addServiceListener(service_type, new ServiceListener() {
				@Override
				public void serviceAdded(ServiceEvent event) {
					System.out.println("Math Service added: " + event.getInfo());
				}

				@Override
				public void serviceRemoved(ServiceEvent event) {
					System.out.println("Math Service removed: " + event.getInfo());

				}

				@Override
				public void serviceResolved(ServiceEvent event) {
					System.out.println("Math Service resolved: "+event.getInfo());
					serviceInfo = event.getInfo();

					int port = serviceInfo.getPort();

					System.out.println("resolving " + service_type + " with properties ...");
					System.out.println("\t port: "+port);
					System.out.println("\t type:"+event.getType());
					System.out.println("\t description/properties: " + serviceInfo.getNiceTextString());
					System.out.println("\t host: " +serviceInfo.getHostAddresses()[0]);

				}
			});
			Thread.sleep(2000);
			jmDNS.close();
		}
		catch (UnknownHostException e){
			System.out.println(e.getMessage());
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
	}


	private void initialize(){

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Service 1 (Unary)");
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		lblNewLabel.setBounds(196, 3, 218, 21);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("1. Room Heat Calculator. Dimensions (meters)");
		lblNewLabel_1.setBounds(10, 34, 306, 13);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Width");
		lblNewLabel_2.setBounds(10, 57, 45, 13);
		contentPane.add(lblNewLabel_2);

		tFWidth = new JTextField();
		tFWidth.setHorizontalAlignment(SwingConstants.CENTER);
		tFWidth.setBounds(43, 54, 82, 19);
		contentPane.add(tFWidth);
		tFWidth.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Length");
		lblNewLabel_3.setBounds(135, 57, 52, 13);
		contentPane.add(lblNewLabel_3);

		tFLength = new JTextField();
		tFLength.setHorizontalAlignment(SwingConstants.CENTER);
		tFLength.setBounds(190, 54, 96, 19);
		contentPane.add(tFLength);
		tFLength.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Height");
		lblNewLabel_4.setBounds(296, 57, 52, 13);
		contentPane.add(lblNewLabel_4);

		tFHeigth = new JTextField();
		tFHeigth.setHorizontalAlignment(SwingConstants.CENTER);
		tFHeigth.setBounds(349, 54, 96, 19);
		contentPane.add(tFHeigth);
		tFHeigth.setColumns(10);

		JButton BTNcalculateRoomHeat = new JButton("Calculate");
		BTNcalculateRoomHeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double width, length, heigth;

				try {

					width = Double.parseDouble(tFWidth.getText());
					length = Double.parseDouble(tFLength.getText());
					heigth = Double.parseDouble(tFHeigth.getText());

					Service1OuterClass.HeatOutputRequest req = Service1OuterClass.HeatOutputRequest.newBuilder()
							.setHeightRoom(heigth).setLengthRoom(length).setWidthRoom(width)
							.build();

					Service1OuterClass.HeatOutput response = blockingStub.getHeatOutput(req);

					if (!response.getError().isEmpty()){
						textResponse.append("Error: "+response.getError()+"\n");
					}
					else {
						textResponse.append("BTU: " + response.getBTU() + " or kW: " + response.getKW() + "\n");
					}
					System.out.println("BTU: "+ response.getBTU()+" or kW: "+response.getKW());
				}
				catch (NumberFormatException ex){
					textResponse.append("Fill in all fields with numbers only!\n");
				}

			}
		});
		BTNcalculateRoomHeat.setBounds(7, 80, 118, 42);
		contentPane.add(BTNcalculateRoomHeat);

		textResponse = new JTextArea();
		textResponse.setWrapStyleWord(true);
		textResponse.setRows(3);
		textResponse.setLineWrap(true);
		textResponse.setColumns(20);
		textResponse.setBounds(151, 80, 294, 57);

		JScrollPane scrollPane = new JScrollPane(textResponse);
		scrollPane.setBounds(135, 80, 441, 42);

		contentPane.add(scrollPane);
		
		
		//Service 1 Part 2 - Cost of Energy
		
		JLabel lblNewLabel_5 = new JLabel("2. Energy Cost Calculator");
		lblNewLabel_5.setBounds(10, 132, 252, 13);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Average Power Consumed per day (kW)");
		lblNewLabel_6.setBounds(10, 155, 261, 13);
		contentPane.add(lblNewLabel_6);
		
		tFPowerConsumed = new JTextField();
		tFPowerConsumed.setHorizontalAlignment(SwingConstants.CENTER);
		tFPowerConsumed.setBounds(281, 152, 67, 19);
		contentPane.add(tFPowerConsumed);
		tFPowerConsumed.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("Cost of Electricity (cents per kW)");
		lblNewLabel_7.setBounds(10, 178, 261, 13);
		contentPane.add(lblNewLabel_7);
		
		tFCostElectricity = new JTextField();
		tFCostElectricity.setBounds(281, 175, 67, 19);
		contentPane.add(tFCostElectricity);
		tFCostElectricity.setColumns(10);
		
		JButton BTNcalculateEnergyCost = new JButton("Calculate");
		BTNcalculateEnergyCost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				double consumptionkW, kWCost;
				try {
					consumptionkW = Double.parseDouble(tFPowerConsumed.getText());
					kWCost = Double.parseDouble(tFCostElectricity.getText());

					Service1OuterClass.CostElectricRequest req = Service1OuterClass.CostElectricRequest.newBuilder()
							.setConsumptionkW(consumptionkW).setKWCost(kWCost)
							.build();
							Service1OuterClass.CostElectricity response = blockingStub.getCostOfElect(req);

					if (!response.getError().isEmpty()){
						txtResponseEnergyCost.append("Error: "+response.getError()+"\n");
					}
					else {
						txtResponseEnergyCost.append("Cost per Week: "+response.getWeekCost()+
								" Cost per Month: "+response.getMonthCost()+" Cost per Year: "+response.getAnnualCost()+"\n");
					}
					System.out.println("Cost per Week: "+response.getWeekCost()+
							" Cost per Month: "+response.getMonthCost()+" Cost per Year: "+response.getAnnualCost());
				}
				catch (NumberFormatException ex){
					txtResponseEnergyCost.append("Filly in all fields wit numbers only ! \n");
				}
			}
		});
		BTNcalculateEnergyCost.setBounds(7, 206, 118, 60);
		contentPane.add(BTNcalculateEnergyCost);
		
		txtResponseEnergyCost = new JTextArea();
		txtResponseEnergyCost.setColumns(20);
		txtResponseEnergyCost.setWrapStyleWord(true);
		txtResponseEnergyCost.setRows(3);
		txtResponseEnergyCost.setBounds(135, 207, 441, 58);
		contentPane.add(txtResponseEnergyCost);
		
		JScrollPane scrollPaneCostElec = new JScrollPane(txtResponseEnergyCost);
		scrollPaneCostElec.setBounds(135, 207, 441, 58);

		contentPane.add(scrollPaneCostElec);
	}
}
