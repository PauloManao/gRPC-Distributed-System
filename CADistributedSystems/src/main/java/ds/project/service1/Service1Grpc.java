package ds.project.service1;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: service1.proto")
public final class Service1Grpc {

  private Service1Grpc() {}

  public static final String SERVICE_NAME = "Service1";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ds.project.service1.Service1OuterClass.HeatOutputRequest,
      ds.project.service1.Service1OuterClass.HeatOutput> getGetHeatOutputMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getHeatOutput",
      requestType = ds.project.service1.Service1OuterClass.HeatOutputRequest.class,
      responseType = ds.project.service1.Service1OuterClass.HeatOutput.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ds.project.service1.Service1OuterClass.HeatOutputRequest,
      ds.project.service1.Service1OuterClass.HeatOutput> getGetHeatOutputMethod() {
    io.grpc.MethodDescriptor<ds.project.service1.Service1OuterClass.HeatOutputRequest, ds.project.service1.Service1OuterClass.HeatOutput> getGetHeatOutputMethod;
    if ((getGetHeatOutputMethod = Service1Grpc.getGetHeatOutputMethod) == null) {
      synchronized (Service1Grpc.class) {
        if ((getGetHeatOutputMethod = Service1Grpc.getGetHeatOutputMethod) == null) {
          Service1Grpc.getGetHeatOutputMethod = getGetHeatOutputMethod = 
              io.grpc.MethodDescriptor.<ds.project.service1.Service1OuterClass.HeatOutputRequest, ds.project.service1.Service1OuterClass.HeatOutput>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Service1", "getHeatOutput"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ds.project.service1.Service1OuterClass.HeatOutputRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ds.project.service1.Service1OuterClass.HeatOutput.getDefaultInstance()))
                  .setSchemaDescriptor(new Service1MethodDescriptorSupplier("getHeatOutput"))
                  .build();
          }
        }
     }
     return getGetHeatOutputMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ds.project.service1.Service1OuterClass.CostElectricRequest,
      ds.project.service1.Service1OuterClass.CostElectricity> getGetCostOfElectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getCostOfElect",
      requestType = ds.project.service1.Service1OuterClass.CostElectricRequest.class,
      responseType = ds.project.service1.Service1OuterClass.CostElectricity.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ds.project.service1.Service1OuterClass.CostElectricRequest,
      ds.project.service1.Service1OuterClass.CostElectricity> getGetCostOfElectMethod() {
    io.grpc.MethodDescriptor<ds.project.service1.Service1OuterClass.CostElectricRequest, ds.project.service1.Service1OuterClass.CostElectricity> getGetCostOfElectMethod;
    if ((getGetCostOfElectMethod = Service1Grpc.getGetCostOfElectMethod) == null) {
      synchronized (Service1Grpc.class) {
        if ((getGetCostOfElectMethod = Service1Grpc.getGetCostOfElectMethod) == null) {
          Service1Grpc.getGetCostOfElectMethod = getGetCostOfElectMethod = 
              io.grpc.MethodDescriptor.<ds.project.service1.Service1OuterClass.CostElectricRequest, ds.project.service1.Service1OuterClass.CostElectricity>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Service1", "getCostOfElect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ds.project.service1.Service1OuterClass.CostElectricRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ds.project.service1.Service1OuterClass.CostElectricity.getDefaultInstance()))
                  .setSchemaDescriptor(new Service1MethodDescriptorSupplier("getCostOfElect"))
                  .build();
          }
        }
     }
     return getGetCostOfElectMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static Service1Stub newStub(io.grpc.Channel channel) {
    return new Service1Stub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static Service1BlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new Service1BlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static Service1FutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new Service1FutureStub(channel);
  }

  /**
   */
  public static abstract class Service1ImplBase implements io.grpc.BindableService {

    /**
     */
    public void getHeatOutput(ds.project.service1.Service1OuterClass.HeatOutputRequest request,
        io.grpc.stub.StreamObserver<ds.project.service1.Service1OuterClass.HeatOutput> responseObserver) {
      asyncUnimplementedUnaryCall(getGetHeatOutputMethod(), responseObserver);
    }

    /**
     */
    public void getCostOfElect(ds.project.service1.Service1OuterClass.CostElectricRequest request,
        io.grpc.stub.StreamObserver<ds.project.service1.Service1OuterClass.CostElectricity> responseObserver) {
      asyncUnimplementedUnaryCall(getGetCostOfElectMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetHeatOutputMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ds.project.service1.Service1OuterClass.HeatOutputRequest,
                ds.project.service1.Service1OuterClass.HeatOutput>(
                  this, METHODID_GET_HEAT_OUTPUT)))
          .addMethod(
            getGetCostOfElectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ds.project.service1.Service1OuterClass.CostElectricRequest,
                ds.project.service1.Service1OuterClass.CostElectricity>(
                  this, METHODID_GET_COST_OF_ELECT)))
          .build();
    }
  }

  /**
   */
  public static final class Service1Stub extends io.grpc.stub.AbstractStub<Service1Stub> {
    private Service1Stub(io.grpc.Channel channel) {
      super(channel);
    }

    private Service1Stub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected Service1Stub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new Service1Stub(channel, callOptions);
    }

    /**
     */
    public void getHeatOutput(ds.project.service1.Service1OuterClass.HeatOutputRequest request,
        io.grpc.stub.StreamObserver<ds.project.service1.Service1OuterClass.HeatOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetHeatOutputMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCostOfElect(ds.project.service1.Service1OuterClass.CostElectricRequest request,
        io.grpc.stub.StreamObserver<ds.project.service1.Service1OuterClass.CostElectricity> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetCostOfElectMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class Service1BlockingStub extends io.grpc.stub.AbstractStub<Service1BlockingStub> {
    private Service1BlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private Service1BlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected Service1BlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new Service1BlockingStub(channel, callOptions);
    }

    /**
     */
    public ds.project.service1.Service1OuterClass.HeatOutput getHeatOutput(ds.project.service1.Service1OuterClass.HeatOutputRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetHeatOutputMethod(), getCallOptions(), request);
    }

    /**
     */
    public ds.project.service1.Service1OuterClass.CostElectricity getCostOfElect(ds.project.service1.Service1OuterClass.CostElectricRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetCostOfElectMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class Service1FutureStub extends io.grpc.stub.AbstractStub<Service1FutureStub> {
    private Service1FutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private Service1FutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected Service1FutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new Service1FutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ds.project.service1.Service1OuterClass.HeatOutput> getHeatOutput(
        ds.project.service1.Service1OuterClass.HeatOutputRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetHeatOutputMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ds.project.service1.Service1OuterClass.CostElectricity> getCostOfElect(
        ds.project.service1.Service1OuterClass.CostElectricRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetCostOfElectMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_HEAT_OUTPUT = 0;
  private static final int METHODID_GET_COST_OF_ELECT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final Service1ImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(Service1ImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_HEAT_OUTPUT:
          serviceImpl.getHeatOutput((ds.project.service1.Service1OuterClass.HeatOutputRequest) request,
              (io.grpc.stub.StreamObserver<ds.project.service1.Service1OuterClass.HeatOutput>) responseObserver);
          break;
        case METHODID_GET_COST_OF_ELECT:
          serviceImpl.getCostOfElect((ds.project.service1.Service1OuterClass.CostElectricRequest) request,
              (io.grpc.stub.StreamObserver<ds.project.service1.Service1OuterClass.CostElectricity>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class Service1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    Service1BaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ds.project.service1.Service1OuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Service1");
    }
  }

  private static final class Service1FileDescriptorSupplier
      extends Service1BaseDescriptorSupplier {
    Service1FileDescriptorSupplier() {}
  }

  private static final class Service1MethodDescriptorSupplier
      extends Service1BaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    Service1MethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (Service1Grpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new Service1FileDescriptorSupplier())
              .addMethod(getGetHeatOutputMethod())
              .addMethod(getGetCostOfElectMethod())
              .build();
        }
      }
    }
    return result;
  }
}
