package businessLogic;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import dataAccess.DataAccess;

public class BLFactory {
	/*public static BLFacade createBLFacade() throws Exception {
        ConfigXML c = ConfigXML.getInstance();
        BLFacade appFacadeInterface;

        if (c.isBusinessLogicLocal()) {
            DataAccess da = new DataAccess();
            appFacadeInterface = new BLFacadeImplementation(da);
        } else {
            String serviceName = "http://" + c.getBusinessLogicNode() + ":" +
                                  c.getBusinessLogicPort() + "/ws/" +
                                  c.getBusinessLogicName() + "?wsdl";
            URL url = new URL(serviceName);
            QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
            Service service = Service.create(url, qname);
            appFacadeInterface = service.getPort(BLFacade.class);
        }

        return appFacadeInterface;
    }*/
	
	public BLFacade getBusinessLogicFactory(boolean isLocal) throws Exception {
        if (isLocal) {
            return new BLFacadeImplementation(new DataAccess());
        } else {
            URL url = new URL("http://localhost:9999/ws?wsdl");
            QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
            Service service = Service.create(url, qname);
            return service.getPort(BLFacade.class);
        }
    }
	
	
}
