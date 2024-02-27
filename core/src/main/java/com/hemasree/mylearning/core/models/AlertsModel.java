package com.hemasree.mylearning.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;

@Model(adaptables = Resource.class)
public class AlertsModel {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(optional = true)
    private String txHeadline;

    @ValueMapValue(optional = true)
    private String txBody;

    @ValueMapValue(optional = true)
    private String txLink;

    @ValueMapValue(optional = true)
    private String txText;

    @ValueMapValue(optional = true)
    private boolean txNewTab;

    public String getTxHeadline() {
        return txHeadline;
    }

    public void setTxHeadline(String txHeadline) {
        this.txHeadline = txHeadline;
    }

    public String getTxBody() {
        return txBody;
    }

    public void setTxBody(String txBody) {
        this.txBody = txBody;
    }

    public String getTxLink() {
        return txLink;
    }

    public void setTxLink(String txLink) {
        this.txLink = txLink;
    }

    public String getTxText() {
        return txText;
    }

    public void setTxText(String txText) {
        this.txText = txText;
    }

    public boolean getTxNewTab() {
        return txNewTab;
    }

    public void setTxNewTab(boolean txNewTab) {
        this.txNewTab = txNewTab;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(txHeadline) ||
                StringUtils.isNotBlank(txBody) ||
                StringUtils.isNotBlank(txLink) ||
                StringUtils.isNotBlank(txText);
    }

    @PostConstruct
    protected void init() throws Exception {
        LOG.info("Start ResourcesModel class init method !");
        populate();
    }

    private void populate() {
        try {
            // Configured object
            if (this.isValid()) {
                if (!txLink.isEmpty()) {

                    if (txLink.startsWith("/")) {
                        String link = txLink;
                        Page page = resourceResolver.resolve(link).adaptTo(Page.class);
                        link = checkURLByPage(page);
                        setTxLink(link);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in Alerts Model", e.getMessage());
        }
    }

    public static String checkURLByPage(Page page) {
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
