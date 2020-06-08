import OperacionesModule.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

class OperacionesImpl extends OperacionesPOA {
    private ORB orb;
    public void setORB(ORB orb_val) 
    {
        orb = orb_val; 
    }

    // Implementación de métodos remotos
    public String suma(float lhs, float rhs) {
        return Float.toString(lhs + rhs);
    }
    public String resta(float lhs, float rhs) {
        return this.suma(lhs, -rhs);
    }
    public String division(float lhs, float rhs) {
        return Float.toString(lhs / rhs);
    }
    public String multiplicacion(float lhs, float rhs) {
        return Float.toString(lhs * rhs);
    }
    public String raizCuadrada(float a) {
        return Double.toString(Math.sqrt(a));
    }
    public String cuadrado(float a) {
        return Double.toString(Math.pow(a, 2));
    }
    public String potenciaN(float a, float b) {
        return Double.toString(Math.pow(a, b));
    }
    public String modulo (float a, float b) {
        return Float.toString(a % b);		
    }
    public void shutdown()
    {
        orb.shutdown(false);
    }

}

public class Server {
    
    public static void main(String args[])
    {
        try
            {
                // create and initialize the ORB
                ORB orb = ORB.init(args, null);

                // get reference to rootpoa & activate the POAManager
                POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
                rootpoa.the_POAManager().activate();

                // create servant and register it with the ORB
                OperacionesImpl helloImpl = new OperacionesImpl();
                helloImpl.setORB(orb); 

                // get object reference from the servant
                org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
                Operaciones href = OperacionesHelper.narrow(ref);

                // get the root naming context
                // NameService invokes the name service
                org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
                // Use NamingContextExt which is part of the Interoperable
                // Naming Service (INS) specification.
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

                // bind the Object Reference in Naming
                String name = "Calculadora";
                NameComponent path[] = ncRef.to_name( name );
                ncRef.rebind(path, href);

                System.out.println("CalculadoraServer está listo y esperando ...");

                // wait for invocations from clients
                orb.run();
            } 
        catch (Exception e) 
            {
                System.err.println("ERROR: " + e);
                e.printStackTrace(System.out);
            }

        System.out.println("HelloServer Exiting ...");
    }
}
