package com.hemasree.mylearning.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sudhakar Hemasree
 * 
 */
@SuppressWarnings("all")
@Model(adaptables = Resource.class)
public class AlertsModel {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@SlingObject
	private ResourceResolver resourceResolver;

	//Config Tab
	@ValueMapValue(optional = true)
	private String txHeadline;

	@ValueMapValue(optional = true)
	private String txBody;

	@ValueMapValue(optional = true)
	private String ctaLink;

	@ValueMapValue(optional = true)
	private String txCtaText;

	@ValueMapValue(optional = true)
	private String txCtaTitle;

	@ValueMapValue(optional = true)
	private boolean ctaNewTab;

	public String getTxHeadline() { return txHeadline; }

	public void setTxHeadline(String txHeadline) { this.txHeadline = txHeadline; }

	public String getTxBody() { return txBody; }

	public void setTxBody(String txBody) { this.txBody = txBody; }

	public String getCtaLink() {
		return ctaLink;
	}

	public void setCtaLink(String ctaLink) {
		this.ctaLink = ctaLink;
	}

	public String getTxCtaText() {
		return txCtaText;
	}

	public void setTxCtaText(String txCtaText) {
		this.txCtaText = txCtaText;
	}

	public String getTxCtaTitle() {
		return txCtaTitle;
	}

	public void setTxCtaTitle(String txCtaTitle) {
		this.txCtaTitle = txCtaTitle;
	}

	public boolean isCtaNewTab() {
		return ctaNewTab;
	}

	public void setCtaNewTab(boolean ctaNewTab) {
		this.ctaNewTab = ctaNewTab;
	}

	public boolean isValid() {
		return StringUtils.isNotBlank(txHeadline) ||
				StringUtils.isNotBlank(txBody) ||
				StringUtils.isNotBlank(ctaLink) ||
				StringUtils.isNotBlank(txCtaText);
	}

	@PostConstruct
	protected void init() throws JSONException, Exception {
		LOG.info("Start ResourcesModel class init method !");
		populate();
	}

	private void populate() {
		try {
			//Configured object
			if(this.isValid()) {
				if (!ctaLink.isEmpty()) {

					if (ctaLink.startsWith("/")) {
						String link = ctaLink;
						Page page = resourceResolver.resolve(link).adaptTo(Page.class);
						link = checkInternalURLByPage(page);
						setCtaLink(link);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Exception in Alerts Model", e.getMessage());
		}

	}

    
    public static String checkInternalURLByPage(Page page) {
        String url = null;
        if (page != null) {
            url = page.getPath();
            if (url.endsWith(".html")) {
                return url;
            } else {
                url = url + ".html";
                return url;
            }
        }
        return url;
    }


}
