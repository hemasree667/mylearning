package com.hemasree.mylearning.core.replication;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;

import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
@Component(service = EventHandler.class, immediate = true, property = {
		EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
		EventConstants.EVENT_FILTER + "=(paths="+Constants.CF_ROOTPATH+"/*)" })
public class CFReplicationEventHandler implements EventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CFReplicationEventHandler.class);
	public static final String PBSERVICES_ALERT = "/pbservices/alert.";
	public static final String DOT_JSON = ".json";
	public static final String EXCEPTION = "Error while Activating/Deactivating Content Fragment";
	public static final String FORWARD_SLASH = "/";
	public static final String DOT = ".";
	public static final String DATA_WRITE_USER = "dataWriteUser";

	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private Replicator replicator;

	public void handleEvent(final Event event) {
		try {
			LOG.info("\n Event Type : {} ", event.getTopic());
			String path = ReplicationAction.fromEvent(event).getPath();
			if (StringUtils.isNotEmpty(path) && path.startsWith(Constants.CF_ROOTPATH)) {
				ReplicationActionType eventType = ReplicationAction.fromEvent(event).getType();
				String country = path.split(FORWARD_SLASH)[5];
				String lang = path.split(FORWARD_SLASH)[6];
				ResourceResolver resolver = null;
				Session session = null;
				String servletPath = PBSERVICES_ALERT + country + DOT + lang + DOT_JSON;
				resolver = Utils.getServiceResourceResolver(resolverFactory, DATA_WRITE_USER);
				session = resolver.adaptTo(Session.class);
				LOG.info("\n servletPath :  {}", servletPath);
				if (eventType.equals(ReplicationActionType.ACTIVATE) || eventType.equals(ReplicationActionType.DEACTIVATE)) {
					Utils.replicate(session, resolver, replicator, eventType.getName(), servletPath);
					LOG.info(eventType.getName()+" Page path :  {}", path);
				}
			}
		} catch (Exception e) {
			LOG.error(EXCEPTION, e);
		}
	}
}
