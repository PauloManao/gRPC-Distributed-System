package ds.project;

import ds.project.service1.Service1Grpc;
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
import java.util.Properties;


public class gRPCServer {
    public static void main(String[] args) {
        gRPCServer gRPCServer = new gRPCServer();

        Properties prop =gRPCServer.getProperties();

        gRPCServer.registerService(prop);

        int port = Integer.valueOf(prop.getProperty("service_port"));

        try {
            Server server = ServerBuilder.forPort(port)
                    .addService(new Service1())
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


}
