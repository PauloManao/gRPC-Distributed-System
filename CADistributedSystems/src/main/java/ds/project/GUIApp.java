package ds.project;


import ds.project.service1.Service1Grpc;
import ds.project.service1.Service1OuterClass;
import ds.project.service2.Service2Grpc;
import ds.project.service2.Service2OuterClass;
import ds.project.service3.Service3Grpc;
import ds.project.service3.Service3OuterClass;
import ds.project.service4.Service4Grpc;
import ds.project.service4.Service4OuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class GUIApp extends JFrame {

	private static Service1Grpc.Service1BlockingStub blockingStub;
	private static Service2Grpc.Service2Stub asyncStubService2;
	private static Service3Grpc.Service3Stub asyncStubService3;
	private static Service4Grpc.Service4Stub asyncStubService4;
	private StreamObserver<Service4OuterClass.consumptionRequest> requestObserver;

	private ServiceInfo serviceInfo;

	private JPanel contentPane;
	private JTextField tFWidth;
	private JTextField tFLength;
	private JTextField tFHeigth;
	private JTextArea textResponse;
	private JTextField tFPowerConsumed;
	private JTextField tFCostElectricity;
	private JTextArea txtResponseEnergyCost;
	private JTextArea Service2textArea;
	private JTextArea responseDateTimesTextArea;
	private ScheduledExecutorService scheduler;
	private List<Service3OuterClass.scheduleRequest> dateTimesList = new ArrayList<>();
	private JFormattedTextField StartFormattedTextField;
	private JFormattedTextField EndFormattedTextField;
	private JTextArea Service4textArea;
	private BigDecimal totalConsumption = BigDecimal.ZERO;



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
		asyncStubService2 = Service2Grpc.newStub(channel);
		asyncStubService3 = Service3Grpc.newStub(channel);
		asyncStubService4 = Service4Grpc.newStub(channel);


		//frame = new JFrame();
		this.setTitle("Greener App");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(250, 150, 600, 610);

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
		scheduler = Executors.newSingleThreadScheduledExecutor();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//SERVICE 1 - UNARY

		JLabel Service1Label = new JLabel("Service 1 (Unary)");
		Service1Label.setFont(new Font("Verdana", Font.BOLD, 14));
		Service1Label.setBounds(213, 3, 232, 21);
		contentPane.add(Service1Label);

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
					textResponse.append("Error: Fill in all fields with numbers only!\n");
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
		
		JLabel lblNewLabel_6 = new JLabel("Average Power Consumed/day (kW)");
		lblNewLabel_6.setBounds(10, 155, 218, 13);
		contentPane.add(lblNewLabel_6);
		
		tFPowerConsumed = new JTextField();
		tFPowerConsumed.setHorizontalAlignment(SwingConstants.CENTER);
		tFPowerConsumed.setBounds(229, 152, 57, 19);
		contentPane.add(tFPowerConsumed);
		tFPowerConsumed.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("Cost of Electricity (cents/kW)");
		lblNewLabel_7.setBounds(336, 155, 180, 13);
		contentPane.add(lblNewLabel_7);
		
		tFCostElectricity = new JTextField();
		tFCostElectricity.setHorizontalAlignment(SwingConstants.CENTER);
		tFCostElectricity.setBounds(519, 152, 57, 19);
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
					txtResponseEnergyCost.append("Error: Fill in all fields with numbers only ! \n");
				}
			}
		});
		BTNcalculateEnergyCost.setBounds(7, 178, 121, 48);
		contentPane.add(BTNcalculateEnergyCost);
		
		txtResponseEnergyCost = new JTextArea();
		txtResponseEnergyCost.setColumns(20);
		txtResponseEnergyCost.setWrapStyleWord(true);
		txtResponseEnergyCost.setRows(3);
		txtResponseEnergyCost.setBounds(135, 179, 441, 48);
		contentPane.add(txtResponseEnergyCost);
		
		JScrollPane scrollPaneCostElec = new JScrollPane(txtResponseEnergyCost);
		scrollPaneCostElec.setBounds(135, 179, 441, 48);

		contentPane.add(scrollPaneCostElec);
		
		
		//SERVICE 2 - SERVER STREAMING
		
		JLabel Servcice2Label = new JLabel("Service 2 (Server Streaming)");
		Servcice2Label.setFont(new Font("Verdana", Font.BOLD, 14));
		Servcice2Label.setBounds(179, 237, 316, 21);
		contentPane.add(Servcice2Label);
		
		JLabel countryLabel = new JLabel("Country");
		countryLabel.setBounds(10, 266, 82, 13);
		contentPane.add(countryLabel);
		
		JComboBox CountryComboBox = new JComboBox();
		CountryComboBox.setBounds(85, 262, 201, 21);
		CountryComboBox.setModel(new DefaultComboBoxModel(new String[]{"Ireland", "Germany", "France", "Switzerland"}));
		contentPane.add(CountryComboBox);
		
		JLabel ServLabel = new JLabel("Service");
		ServLabel.setBounds(303, 266, 45, 13);
		contentPane.add(ServLabel);
		
		JComboBox ServComboBox = new JComboBox();
		ServComboBox.setBounds(358, 262, 218, 21);
		ServComboBox.setModel(new DefaultComboBoxModel(new  String[]{"Gas","Electricity"}));
		contentPane.add(ServComboBox);
		
		JButton ReqServ2Button = new JButton("Request");
		ReqServ2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int countryIndex = CountryComboBox.getSelectedIndex();
				int serviceIndex = ServComboBox.getSelectedIndex();

				Service2OuterClass.tariffsRequest.Country country = Service2OuterClass.tariffsRequest.Country.forNumber(countryIndex);
				Service2OuterClass.tariffsRequest.Service service = Service2OuterClass.tariffsRequest.Service.forNumber(serviceIndex);

				Service2OuterClass.tariffsRequest req = Service2OuterClass.tariffsRequest.newBuilder()
						.setCountry(country)
						.setService(service)
						.build();

				String countryName = CountryComboBox.getSelectedItem().toString();
				String serviceName = ServComboBox.getSelectedItem().toString();

				asyncStubService2.getTariffs(req, new StreamObserver<Service2OuterClass.Tariffs>() {
					private boolean isFirstTariff = true;

					@Override
					public void onNext(Service2OuterClass.Tariffs tariffs) {
						if (isFirstTariff){
							SwingUtilities.invokeLater(()->{
								Service2textArea.append("COUNTRY: "+countryName+", SERVICE: "+serviceName+"\n");
								Service2textArea.append(tariffs.getTariffs()+"\n");
							});
							isFirstTariff = false;
						}
						else {
							SwingUtilities.invokeLater(()->{
								Service2textArea.append(tariffs.getTariffs()+"\n");
							});
						}
					}

					@Override
					public void onError(Throwable t) {
						t.printStackTrace();
					}

					@Override
					public void onCompleted() {
						//I CAN ADD ANY CLEAN UP HERE IF NEEDED
					}
				});

			}
		});
		ReqServ2Button.setBounds(7, 289, 121, 71);
		contentPane.add(ReqServ2Button);

		Service2textArea = new JTextArea();
		Service2textArea.setBounds(135, 290, 441, 70);
		Service2textArea.setColumns(20);
		Service2textArea.setWrapStyleWord(true);
		Service2textArea.setRows(3);
		contentPane.add(Service2textArea);

		JScrollPane scrollPaneService2 = new JScrollPane(Service2textArea);
		scrollPaneService2.setBounds(135,290,441,70);

		contentPane.add(scrollPaneService2);
		
		
		//SERVICE 3 - CLIENT STREAMING
		JLabel Service3Label = new JLabel("Service 3 (Client Streaming)");
		Service3Label.setFont(new Font("Verdana", Font.BOLD, 14));
		Service3Label.setBounds(179, 364, 316, 21);
		contentPane.add(Service3Label);
		
		JLabel StartDateLabel = new JLabel("Start Date/Time");
		StartDateLabel.setBounds(10, 390, 100, 13);
		contentPane.add(StartDateLabel);
		
		JLabel EndDateLabel = new JLabel("End Date/Time");
		EndDateLabel.setBounds(10, 420, 96, 13);
		contentPane.add(EndDateLabel);
		
		JButton addDatesButton = new JButton("Add >>>");
		addDatesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String startDate = StartFormattedTextField.getText();
				String endDate = EndFormattedTextField.getText();
				boolean hasError = false;

				Service3OuterClass.scheduleRequest dateTimePair = Service3OuterClass.scheduleRequest.newBuilder()
						.setStartDate(startDate)
						.setEndDate(endDate)
						.build();
				dateTimesList.add(dateTimePair);



				SwingUtilities.invokeLater(()->{
					responseDateTimesTextArea.append("Added\n");

				});

			}
		});
		addDatesButton.setBounds(7, 442, 103, 21);
		contentPane.add(addDatesButton);
		
		JButton listBtnNewButton = new JButton("List");
		listBtnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StreamObserver<Service3OuterClass.Schedule> responseObserver = new StreamObserver<Service3OuterClass.Schedule>() {
					private boolean hasError = false;
					@Override
					public void onNext(Service3OuterClass.Schedule schedule) {
						List<Service3OuterClass.scheduleRequest> updateTimes = schedule.getUpdatedScheduleRequestList();
						StringBuilder responseBuilder = new StringBuilder();

						for (Service3OuterClass.scheduleRequest dateTimePair:updateTimes){
							String startDate = dateTimePair.getStartDate();
							String endDate = dateTimePair.getEndDate();
							responseBuilder.append("Start Date: ").append(startDate).append(", End Date: ").append(endDate+"\n");
						}
						if(!schedule.getError().isEmpty()){
							hasError = true;
							responseBuilder.append(schedule.getError()).append("\n");
						}
						SwingUtilities.invokeLater(()->{
							if (hasError) {
								responseDateTimesTextArea.setText(responseBuilder.toString());
								hasError = false;
							}
							else {
								responseDateTimesTextArea.append(responseBuilder.toString());
							}
						});
					}

					@Override
					public void onError(Throwable t) {
						t.printStackTrace();
					}

					@Override
					public void onCompleted() {
						dateTimesList.clear();
					}
				};
				StreamObserver<Service3OuterClass.scheduleRequest> requestStreamObserver = asyncStubService3.getSchedule(responseObserver);
				for (Service3OuterClass.scheduleRequest dateTimePair:dateTimesList){
					requestStreamObserver.onNext(dateTimePair);
				}
				requestStreamObserver.onCompleted();
			}
		});
		listBtnNewButton.setBounds(244, 386, 57, 71);
		contentPane.add(listBtnNewButton);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		StartFormattedTextField = new JFormattedTextField(dateFormat);
		StartFormattedTextField.setBounds(108, 385, 126, 21);
		contentPane.add(StartFormattedTextField);
		
		EndFormattedTextField = new JFormattedTextField(dateFormat);
		EndFormattedTextField.setBounds(108, 413, 126, 21);
		contentPane.add(EndFormattedTextField);
		
		responseDateTimesTextArea = new JTextArea();
		responseDateTimesTextArea.setColumns(20);
		responseDateTimesTextArea.setBounds(303, 386, 273, 71);
		responseDateTimesTextArea.setWrapStyleWord(true);
		responseDateTimesTextArea.setRows(3);
		contentPane.add(responseDateTimesTextArea);

		JScrollPane scrollPaneService3 = new JScrollPane(responseDateTimesTextArea);
		scrollPaneService3.setBounds(303,386,273,71);
		contentPane.add(scrollPaneService3);
		
		//SERVICE 4 - BIDIRECTIONAL
		
		JLabel Service4Label = new JLabel("Service 4 (Bidirectional)");
		Service4Label.setFont(new Font("Verdana", Font.BOLD, 14));
		Service4Label.setBounds(184, 467, 230, 13);
		contentPane.add(Service4Label);
		
		JComboBox yearComboBox = new JComboBox();
		yearComboBox.setBounds(7, 498, 69, 21);
		yearComboBox.setModel(new DefaultComboBoxModel(new String[]{"2022", "2023"}));
		contentPane.add(yearComboBox);
		
		JComboBox monthComboBox = new JComboBox();
		monthComboBox.setBounds(135, 498, 69, 21);
		monthComboBox.setModel(new DefaultComboBoxModel(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL","AUG", "SEP", "OCT", "NOV", "DEC"}));
		contentPane.add(monthComboBox);
		
		JButton addConsumptionButton = new JButton("Add >>>");
		addConsumptionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int yearIndex = yearComboBox.getSelectedIndex();
				int monthIdex = monthComboBox.getSelectedIndex();

				Service4OuterClass.consumptionRequest.Year year = Service4OuterClass.consumptionRequest.Year.forNumber(yearIndex);
				Service4OuterClass.consumptionRequest.Month month = Service4OuterClass.consumptionRequest.Month.forNumber(monthIdex);

				Service4OuterClass.consumptionRequest req = Service4OuterClass.consumptionRequest.newBuilder()
						.setYear(year)
						.setMonth(month)
						.build();

				StreamObserver<Service4OuterClass.Consumption> responseObserver = new StreamObserver<Service4OuterClass.Consumption>() {
					@Override
					public void onNext(Service4OuterClass.Consumption consumption) {
						SwingUtilities.invokeLater(()-> {
							if(!consumption.getError().isEmpty()){
								Service4textArea.append("Error: "+ consumption.getError()+"\n");
							}
							else {
								Service4textArea.append("Consumption for "+year+" "+month+": "+consumption.getConsumption()+"\n");
								BigDecimal total =  new BigDecimal(consumption.getTotal());
								String formatTotalConsumption = total.setScale(2, RoundingMode.HALF_UP).toString();
								Service4textArea.append("Total Consumption: " +formatTotalConsumption+"\n");

								totalConsumption = total;
							}
						});

					}

					@Override
					public void onError(Throwable t) {
						t.printStackTrace();

					}

					@Override
					public void onCompleted() {

					}

				};
				StreamObserver<Service4OuterClass.consumptionRequest> requestObserver = asyncStubService4.getConsumption(responseObserver);

				requestObserver.onNext(req);

			}
		});
		addConsumptionButton.setBounds(58, 532, 103, 21);
		contentPane.add(addConsumptionButton);
		
		Service4textArea = new JTextArea();
		Service4textArea.setColumns(20);
		Service4textArea.setBounds(215, 483, 361, 80);
		Service4textArea.setWrapStyleWord(true);
		Service4textArea.setRows(3);
		contentPane.add(Service4textArea);

		JScrollPane scrollPaneService4 = new JScrollPane(Service4textArea);
		scrollPaneService4.setBounds(215,483,361,80);
		contentPane.add(scrollPaneService4);

	}
}
