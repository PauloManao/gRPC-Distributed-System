package ds.project;

import ds.project.service1.Service1Grpc;
import ds.project.service2.Service2Grpc;
import ds.project.service2.Service2OuterClass;
import ds.project.service3.Service3Grpc;
import ds.project.service3.Service3OuterClass;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ds.project.service1.Service1OuterClass;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.*;


public class gRPCServer {
    public static void main(String[] args) {
        gRPCServer gRPCServer = new gRPCServer();

        Properties prop =gRPCServer.getProperties();

        gRPCServer.registerService(prop);

        int port = Integer.valueOf(prop.getProperty("service_port"));

        try {
            Server server = ServerBuilder.forPort(port)
                    .addService(new Service1())
                    .addService(new Service2())
                    .addService(new Service3())
                    .build()
                    .start();
            System.out.println("Server started, listening on "+port);

            server.awaitTermination();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    private Properties getProperties(){
        Properties prop =null;
        try (InputStream input = new FileInputStream("src/main/resources/math.properties")){
            prop = new Properties();

            prop.load(input);

            System.out.println("Service properies ...");
            System.out.println("\t service_type: "+prop.getProperty("service_type"));
            System.out.println("\t service_name: "+prop.getProperty("service_name"));
            System.out.println("\t service_description: "+prop.getProperty("service_description"));
            System.out.println("\t service_port: "+prop.getProperty("service_port"));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    private void registerService(Properties prop){
        try {
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

            String service_type = prop.getProperty("service_type");
            String service_name = prop.getProperty("service_name");

            int service_port = Integer.valueOf(prop.getProperty("service_port"));

            String service_description_properties = prop.getProperty("service_description");

            ServiceInfo serviceInfo = ServiceInfo.create(service_type, service_name, service_port, service_description_properties);
            jmdns.registerService(serviceInfo);

            System.out.printf("registering service with type %s and name %s \n",service_type, service_name);

            Thread.sleep(1000);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    // SERVICE 1 - UNARY
    public static class Service1 extends Service1Grpc.Service1ImplBase{
        @Override
        public void getHeatOutput(Service1OuterClass.HeatOutputRequest request, StreamObserver<Service1OuterClass.HeatOutput> responseObserver) {
            double width;
            double height;
            double length;

            try {

                width = request.getWidthRoom();
                height = request.getHeightRoom();
                length = request.getLengthRoom();

                if(width<=0 || height<= 0 || length<=0){
                    String errorMessage = "Type numbers greater than 0 (zero)";
                    Service1OuterClass.HeatOutput response = Service1OuterClass.HeatOutput.newBuilder()
                            .setError(errorMessage)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    return;
                }

                Service1OuterClass.HeatOutput heatOutput = Service1OuterClass.HeatOutput.newBuilder()
                        .setBTU(width * height * length * 6)
                        .setKW(width * height * length * 0.0606)
                        .build();

                responseObserver.onNext(heatOutput);
                responseObserver.onCompleted();
            }

            catch (NumberFormatException e){
                String errorMessage = "Fill in all fields with numbers only";
                Service1OuterClass.HeatOutput response = Service1OuterClass.HeatOutput.newBuilder()
                        .setError(errorMessage)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }
        }

        @Override
        public void getCostOfElect(Service1OuterClass.CostElectricRequest request, StreamObserver<Service1OuterClass.CostElectricity> responseObserver) {
            double consumptionkW;
            double kWCost;

            try{
                consumptionkW =request.getConsumptionkW();
                kWCost = request.getKWCost();

                if (consumptionkW <= 0 || kWCost <= 0 ){
                    String errorMessage = "Type numbers greater than 0(zero)";
                    Service1OuterClass.CostElectricity response = Service1OuterClass.CostElectricity.newBuilder()
                            .setError(errorMessage)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    return;
                }
            Service1OuterClass.CostElectricity costElectricity = Service1OuterClass.CostElectricity.newBuilder()
                    .setAnnualCost(consumptionkW * kWCost * 365)
                    .setMonthCost(consumptionkW * kWCost *30)
                    .setWeekCost(consumptionkW * kWCost * 7)
                    .build();

                responseObserver.onNext(costElectricity);
                responseObserver.onCompleted();

            }
            catch (NumberFormatException e){
                String errorMessage = "Fill in all fields with numbers only!\n";
                Service1OuterClass.CostElectricity response = Service1OuterClass.CostElectricity.newBuilder()
                        .setError(errorMessage)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }

        }
    }
    //SERVICE 2 - SERVER STREAMING
    private static class Service2 extends Service2Grpc.Service2ImplBase{
        @Override
        public void getTariffs(Service2OuterClass.tariffsRequest request, StreamObserver<Service2OuterClass.Tariffs> responseObserver) {
            Service2OuterClass.tariffsRequest.Country country = request.getCountry();
            Service2OuterClass.tariffsRequest.Service service = request.getService();

            Map<String, String> tariffsMap = getTariffsFromMap(country, service);

            try {
                for (Map.Entry<String, String> entry: tariffsMap.entrySet()){
                    String tariffKey = entry.getKey();
                    String tariffValue = entry.getValue();

                    Service2OuterClass.Tariffs response = Service2OuterClass.Tariffs.newBuilder()
                            .setTariffs(tariffKey+tariffValue)
                            .build();

                    //Await simulation between each tariff response
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    responseObserver.onNext(response);
                }
            }

            finally {
                responseObserver.onCompleted();
            }

        }

        private Map<String, String> getTariffsFromMap(Service2OuterClass.tariffsRequest.Country country, Service2OuterClass.tariffsRequest.Service service){
            Map<String, String> tariffsMap =new HashMap<>();

            if (country == Service2OuterClass.tariffsRequest.Country.Ireland && service == Service2OuterClass.tariffsRequest.Service.Electricity){
                tariffsMap.put("Bord Gais Energy: ", "44.51c /kWh");
                tariffsMap.put("Community Power: ", "48.70c /kWh");
                tariffsMap.put("Ecopower: ", "50.78c /kWh");
                tariffsMap.put("Energia: ", "49.45c /kWh");
            }
            else if (country == Service2OuterClass.tariffsRequest.Country.Ireland && service == Service2OuterClass.tariffsRequest.Service.Gas){
                tariffsMap.put("Flogas: ", "€ 1.63/litre");
                tariffsMap.put("GlowGas: ", "€ 1.90/litre");
                tariffsMap.put("Eco Gas: ", "€ 1.85/litre");
                tariffsMap.put("Bord Gais Energy: ", "2.00/litre");
            }

            else if (country == Service2OuterClass.tariffsRequest.Country.Germany && service == Service2OuterClass.tariffsRequest.Service.Electricity){
                tariffsMap.put("Ostrom: ", "45.51c /kWh");
                tariffsMap.put("Vattenfall: ", "48.75c /kWh");
                tariffsMap.put("NaturStrom: ", "46.80c /kWh");
                tariffsMap.put("Yello: ", "41.99c /kWh");
            }

            else if (country == Service2OuterClass.tariffsRequest.Country.Germany && service == Service2OuterClass.tariffsRequest.Service.Gas){
                tariffsMap.put("Entega: ", "€ 1.83/litre");
                tariffsMap.put("Badenova: ", "€ 1.70/litre");
                tariffsMap.put("German Gas: ", "€ 1.55/litre");
                tariffsMap.put("Green Gas: ", "2.10/litre");
            }
            else if (country == Service2OuterClass.tariffsRequest.Country.France && service == Service2OuterClass.tariffsRequest.Service.Electricity){
                tariffsMap.put("Alterna: ", "39.51c /kWh");
                tariffsMap.put("Cdiscount Energie: ", "41.75c /kWh");
                tariffsMap.put("EDF: ", "41.80c /kWh");
                tariffsMap.put("Engie: ", "44.99c /kWh");
            }

            else if (country == Service2OuterClass.tariffsRequest.Country.France && service == Service2OuterClass.tariffsRequest.Service.Gas){
                tariffsMap.put("Planète Oui: ", "€ 1.93/litre");
                tariffsMap.put("Total Energie: ", "€ 1.80/litre");
                tariffsMap.put("TotalEnergies: ", "€ 1.85/litre");
                tariffsMap.put("France Gas: ", "€ 2.00/litre");
            }

            else if (country == Service2OuterClass.tariffsRequest.Country.Switzerland && service == Service2OuterClass.tariffsRequest.Service.Electricity){
                tariffsMap.put("Aargau: ", "38.51c /kWh");
                tariffsMap.put("Appenzell: ", "42.75c /kWh");
                tariffsMap.put("Basel Land: ", "43.80c /kWh");
                tariffsMap.put("Basel Stadt : ", "44.00c /kWh");
            }

            else if (country == Service2OuterClass.tariffsRequest.Country.Switzerland && service == Service2OuterClass.tariffsRequest.Service.Gas){
                tariffsMap.put("Bern : ", "CHF 1.93/litre");
                tariffsMap.put("Freiburg: ", "CHF 1.80/litre");
                tariffsMap.put("Genève: ", "CHF 1.85/litre");
                tariffsMap.put("Glarus: ", "CHF 2.00/litre");
            }

            else{
                tariffsMap.put("Supplier default", "$ 100.00");
            }
            return tariffsMap;
        }
    }

    //SERVICE3 - CLIENT STREAMING
    public static class Service3 extends Service3Grpc.Service3ImplBase{
        @Override
        public StreamObserver<Service3OuterClass.scheduleRequest> getSchedule(StreamObserver<Service3OuterClass.Schedule> responseObserver) {
            return new StreamObserver<Service3OuterClass.scheduleRequest>() {

                private List<Service3OuterClass.scheduleRequest> updatedTimes =new ArrayList<>();
                @Override
                public void onNext(Service3OuterClass.scheduleRequest dateTimePair) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    try {
                        Date startDate = dateFormat.parse(dateTimePair.getStartDate());
                        Date endDate = dateFormat.parse(dateTimePair.getEndDate());

                        if (startDate.after(endDate)||startDate.equals(endDate)){
                            String errorMessage = "Error: Start Date should be before the End Date";
                            Service3OuterClass.Schedule response = Service3OuterClass.Schedule.newBuilder()
                                    .setError(errorMessage)
                                    .build();
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                            return;
                        }

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(startDate);
                            calendar.add(Calendar.MINUTE, -30);
                            String updatedStartDateTime = dateFormat.format(calendar.getTime());

                            calendar.setTime(endDate);
                            calendar.add(Calendar.MINUTE, -30);
                            String updatedEndDateTime = dateFormat.format(calendar.getTime());

                            Service3OuterClass.scheduleRequest scheduleRequest = Service3OuterClass.scheduleRequest.newBuilder()
                                    .setStartDate(updatedStartDateTime)
                                    .setEndDate(updatedEndDateTime)
                                    .build();

                            updatedTimes.add(scheduleRequest);


                    }
                    catch (ParseException e) {
                        String errorMessage = "Error: Start Date should be before the End Date";
                        Service3OuterClass.Schedule response = Service3OuterClass.Schedule.newBuilder()
                                .setError(errorMessage)
                                .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }

                }

                @Override
                public void onError(Throwable t) {
                    String errorMessage = "Error: Invalid date format or Start Date should be before the End Date";
                    Service3OuterClass.Schedule response = Service3OuterClass.Schedule.newBuilder()
                            .setError(errorMessage)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();

                }

                @Override
                public void onCompleted() {
                    Service3OuterClass.Schedule response = Service3OuterClass.Schedule.newBuilder()
                            .addAllUpdatedScheduleRequest(updatedTimes)
                            .build();
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();

                }
            };
        }
    }


}
