package processors;

import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Callable;
import org.mule.api.registry.MuleRegistry;
import org.mule.modules.salesforce.adapters.SalesforceConnectorConnectionIdentifierAdapter;
import org.mule.modules.salesforce.connectivity.SalesforceConnectorConnectionManager;

public class GetSalesforceSessionId implements Callable, MuleContextAware {

	SalesforceConnectorConnectionManager mgr;
	MuleContext context;
	
	@Override
	public synchronized Object onCall(MuleEventContext eventContext) throws Exception {

		if (mgr == null) {
			MuleRegistry registry = this.context.getRegistry();
			mgr = (SalesforceConnectorConnectionManager)(registry.lookupObject("Salesforce"));
		}
		
		SalesforceConnectorConnectionIdentifierAdapter connection = mgr.acquireConnection(mgr.getDefaultConnectionKey());
		String sessionId = connection.getSessionId();
		mgr.releaseConnection(mgr.getDefaultConnectionKey(), connection);
		return sessionId;
	}

	@Override
	public void setMuleContext(MuleContext context) {
		this.context = context;
	}

	
}
