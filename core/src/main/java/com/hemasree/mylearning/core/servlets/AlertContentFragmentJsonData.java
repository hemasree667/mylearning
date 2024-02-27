package com.hemasree.mylearning.core.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.Servlet;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/pbservices/alert",
                "sling.servlet.methods=get",
                "sling.servlet.extensions=json"
        }
)
@SuppressWarnings("deprecation")
public class AlertContentFragmentJsonData extends SlingSafeMethodsServlet {

  	private static Logger LOG = LoggerFactory.getLogger(AlertContentFragmentJsonData.class);
    
    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws IOException {
    	final ResourceResolver resourceResolver = request.getResourceResolver();
    	String[] selectors = request.getRequestPathInfo().getSelectors();
    	Resource dataResource = resourceResolver.getResource(Constants.CF_ROOTPATH + Constants.FORWARD_SLASH + selectors[0] + Constants.FORWARD_SLASH + selectors[1] + "/alert/jcr:content/data");
    	JsonObject resultJson = new JsonObject();
    	JsonArray alertsArray = new JsonArray();
    	if(dataResource != null && dataResource.hasChildren()) {
    		LOG.info("Alert Resource path::"+dataResource.getPath());
    		for (Resource alertVariation : dataResource.getChildren()) {
    			LOG.info("Variation path::"+alertVariation.getPath());
    			ValueMap variationValueMap = alertVariation.adaptTo(ValueMap.class);   
    			String disableWorkflowCheck = variationValueMap.get("disableAlert", String.class);
    			if(StringUtils.isBlank(disableWorkflowCheck) || StringUtils.equals(disableWorkflowCheck, "false")) {
    	    	createAlertJson(resourceResolver, alertsArray, variationValueMap);
    		}
    		}
            resultJson.add("alerts", alertsArray);
            LOG.info("Result Json::"+resultJson.toString());
			response.setContentType(OAuth.ContentType.JSON);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.getWriter().write(new Gson().toJson(resultJson));
    	}
    }

	private void createAlertJson(final ResourceResolver resourceResolver, JsonArray alertsArray,
			ValueMap variationValueMap) {
		JsonObject alertProperties = new JsonObject();
		JsonArray pageLinkArray = new JsonArray();
		String headline = variationValueMap.get("headline", String.class);
		String body = variationValueMap.get("body", String.class);
		String link = variationValueMap.get("link", String.class);
		String linkText = variationValueMap.get("linkText", String.class);
		String linkTitle = variationValueMap.get("linkTitle", String.class);
		String newTab = variationValueMap.get("newTab", String.class);
		String []arrayEnabledPath = variationValueMap.get("pagesLink", String[].class);
		boolean hasContent = false;
		boolean arrayPaths = false;
		
		if(StringUtils.isNotBlank(headline) || StringUtils.isNotBlank(body) || (StringUtils.isNotBlank(link) && StringUtils.isNotBlank(linkText))) {
			hasContent = true;
		}

		if ((arrayEnabledPath != null && arrayEnabledPath.length > 0) && hasContent) {
			for (int index = 0; index < arrayEnabledPath.length; index++) {
				if(StringUtils.isNotBlank(arrayEnabledPath[index]) && !StringUtils.equals(arrayEnabledPath[index].trim(), Constants.FORWARD_SLASH)) {
					arrayPaths = true;
					if (arrayEnabledPath[index].contains(Constants.DOT_HTML)) {
						arrayEnabledPath[index] = resourceResolver.map(arrayEnabledPath[index].replace(Constants.DOT_HTML, StringUtils.EMPTY)).concat(Constants.DOT_HTML);
						pageLinkArray.add(arrayEnabledPath[index]);
					} else {
						arrayEnabledPath[index] = resourceResolver.map(arrayEnabledPath[index]);
						pageLinkArray.add(arrayEnabledPath[index]);
					}
				}

			}
			if(arrayPaths) {
				if (StringUtils.isNotBlank(headline)) {
					alertProperties.addProperty("headline", headline);
				}
				if (StringUtils.isNotBlank(body)) {
					alertProperties.addProperty("body", body.replace("\n", StringUtils.EMPTY));
				}
				if (StringUtils.isNotBlank(link) && StringUtils.isNotBlank(linkText)) {
					link = resourceResolver.map(link);
					alertProperties.addProperty("link", link);
					alertProperties.addProperty("linkText", linkText);

					if (StringUtils.isNotBlank(linkTitle)) {
						alertProperties.addProperty("linkTitle", linkTitle);
					}
					if(StringUtils.isNotBlank(newTab)) {
					alertProperties.addProperty("newTab", newTab);
					}
				}
				alertProperties.add("alertPaths", pageLinkArray);
				alertsArray.add(alertProperties);
			}

		}
	}
}
