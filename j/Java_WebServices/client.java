
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
import javax.xml.namespace.QName;

public class AxisClient ()
{
  // Define the endpoint of the service.
  String endpoint = "http://localhost:8080/axis/services/HelloService";

  public void client ()
  {
    // Create a service.
    Service service = new Service ();

    // Create a SOAP request call.
    Call call = (Call) service.createCall ();

    // Set the target endpoint of the provider location.
    call.setTargetEndpointAddress ( endpoint );

    // Set the service operation name and its methods.
    call.setOperationName ( new QName ( "HelloService", "echo" ) );

    call.addParameter ( "myName", MLType.XSD_STRING, ParameterMode.IN );

    call.setReturnType ( XMLType.XSD_STRING );

    // Invoke the service.
    Object responseObj = call.invoke ( new Object [] {new Integer (myName) } );

  }  // method client

}  // class AxisClient

