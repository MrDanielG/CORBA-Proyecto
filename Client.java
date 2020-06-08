import OperacionesModule.*;
import org.omg.CosNaming.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.omg.CORBA.*;

public class Client {

    static Operaciones operacionesImpl;

    public static void main(String args[])
    {
        try
            {
                // create and initialize the ORB
                ORB orb = ORB.init(args, null);

                // get the root naming context
                org.omg.CORBA.Object objRef = 
                    orb.resolve_initial_references("NameService");
                // Use NamingContextExt instead of NamingContext. This is 
                // part of the Interoperable naming Service.  
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input = " ";

                // resolve the Object Reference in Naming
                String name = "Calculadora";
                operacionesImpl = OperacionesHelper.narrow(ncRef.resolve_str(name));
                Evaluador evaluador = new Evaluador(operacionesImpl);
                while(!input.isEmpty()) {
                    System.out.print(">  ");
                    input=br.readLine();
                    System.out.print("= ");
                    System.out.println(evaluador.evaluar(input.replaceAll("[^0-9 ]", " $0 "))+ '\n');
                }
                System.out.println("Cerrando conexión...");
                operacionesImpl.shutdown();
                System.out.println("Adios!!!");

            }catch (Exception e) {
            System.out.println("ERROR : " + e) ;
            e.printStackTrace(System.out);
        }
    }
}
