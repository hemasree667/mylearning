package com.hemasree.mylearning.core.replication;

import java.io.IOException;
import java.rmi.ServerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MailingException;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@SuppressWarnings("all")
public class Utils {

    private Utils () {}
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Replicates a specified path
     *
     * @param session Session object
     * @param resourceResolver ResourceResolver object
     * @param replicator Replicator object
     * @param actionType String action type
     * @param path String path to replicate
     */
    public static void replicate(Session session, ResourceResolver resourceResolver, Replicator replicator, String actionType, String path) {
        try {
            if (actionType.equalsIgnoreCase(Constants.ACTIVATE)) {
                replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
            } else {
                replicator.replicate(session, ReplicationActionType.DEACTIVATE, path);
                Resource resourceObj = resourceResolver.getResource(path);
                Node nodeToBeDeleted = resourceObj.adaptTo(Node.class);
                nodeToBeDeleted.remove();
            }
        } catch (ReplicationException e) {
            LOGGER.error(Constants.REPLICATION_EXCEPTION);
            LOGGER.debug(Constants.REPLICATION_EXCEPTION, e);
        } catch (RepositoryException e) {
            LOGGER.error(Constants.REPOSITORY_EXCEPTION_REPLICATION);
            LOGGER.debug(Constants.REPOSITORY_EXCEPTION_REPLICATION + " for path : " + path, e);
        }
    }

    /**
     * Generates ResourceResolver Object
     *
     * @param resourceResolverFactory ResourceResolverFactory object
     * @param subServiceName String subServiceName
     * @throws LoginException
     *
     * @return resourceResolver ResourceResolver
     */
    public static ResourceResolver getServiceResourceResolver(ResourceResolverFactory resourceResolverFactory, String subServiceName) throws LoginException {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ResourceResolverFactory.SUBSERVICE, subServiceName);
        return resourceResolverFactory.getServiceResourceResolver(props);
    }

    /**
     * Generates Session Object
     *
     * @param resourceResolverFactory ResourceResolverFactory object
     * @param subServiceName String subServiceName
     * @throws LoginException
     * @return session Session
     */
    public static Session getServiceSession(ResourceResolverFactory resourceResolverFactory, String subServiceName) throws LoginException {
        return getServiceResourceResolver(resourceResolverFactory, subServiceName).adaptTo(Session.class);
    }

    /**
     * Generates Session Object
     *
     * @param languageCodeFromJson String
     *
     * @return country/language String
     */
    public static String getCountryAndLanguage(String languageCodeFromJson) {
        HashMap<String, String> countryLanguageMap = new HashMap<String, String>();
        countryLanguageMap.put("en_US", "us/en");
        countryLanguageMap.put("en_AU", "australia/en");
        countryLanguageMap.put("en_CA", "canada/en");
        countryLanguageMap.put("en_IN", "india/en");
        countryLanguageMap.put("en_IE", "ireland/en");
        countryLanguageMap.put("en_NZ", "newzeland/en");
        countryLanguageMap.put("en_GB", "uk/en");
        countryLanguageMap.put("fi", "finland/fi");
        countryLanguageMap.put("fr", "france/fr");
        countryLanguageMap.put("fr_CA", "canada/fr");
        countryLanguageMap.put("de", "germany/de");
        countryLanguageMap.put("it", "italy/it");
        countryLanguageMap.put("ja", "japan/jp");
        countryLanguageMap.put("no", "norway/no");
        countryLanguageMap.put("sv", "sweden/sv");
        return countryLanguageMap.get(languageCodeFromJson);
    }

    /**
     * Sends email
     *
     * @param emailSubject String, emailRecipients String[], emailTemplate String, templateSession Session, emailParametersMap<String, String>, messageGatewayService MessageGatewayService, sfkJsonString String, attachmentFlag boolean
     *
     */
    public static void sendEmail(String emailSubject, String[] emailRecipients, Session templateSession, Map<String, String> emailParameters, MessageGatewayService messageGatewayService, String sfkJsonString, boolean attachmentFlag) {
        try {
            MailTemplate mailTemplate = MailTemplate.create(Constants.SFK_ARTICLE_EMAIL_NOTIFICATION_TEMPLATE, templateSession);
            HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(emailParameters), HtmlEmail.class);
            email.setSubject(emailSubject);
            if (attachmentFlag) {
                ByteArrayDataSource attachmentDS = new ByteArrayDataSource(sfkJsonString, Constants.APPLICATION_X_ANY);
                email.attach(attachmentDS, Constants.ATTACHED_DATA_TXT, Constants.ATTACHED_DATA_TXT);
            }
            for (String emailId : emailRecipients) {
                email.addTo(emailId.trim());
            }
            MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
            messageGateway.send(email);
        } catch (ServerException e) {
            LOGGER.error(Constants.SERVER_EXCEPTION_MESSAGE);
            LOGGER.debug(Constants.SERVER_EXCEPTION_MESSAGE, e);
        } catch (IOException e) {
            LOGGER.error(Constants.IO_EXCEPTION_MESSAGE);
            LOGGER.debug(Constants.IO_EXCEPTION_MESSAGE, e);
        } catch (MessagingException e) {
            LOGGER.error(Constants.MESSAGING_EXCEPTION_MESSAGE);
            LOGGER.debug(Constants.MESSAGING_EXCEPTION_MESSAGE, e);
        } catch (EmailException e) {
            LOGGER.error(Constants.EMAIL_EXCEPTION_MESSAGE);
            LOGGER.debug(Constants.EMAIL_EXCEPTION_MESSAGE, e);
        } catch (MailingException e) {
            LOGGER.error(Constants.MAILING_EXCEPTION_MESSAGE);
            LOGGER.debug(Constants.MAILING_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Returns true if json is valid if not false
     *
     * @param sfkJsonString String json from API call
     * @return Boolean input json is valid or invalid
     */
    public static boolean validateJson(String sfkJsonString) {
        Gson gson = new Gson();
        try {
            gson.fromJson(sfkJsonString, Object.class);
            Object jsonObjType = gson.fromJson(sfkJsonString, Object.class).getClass();
            if(jsonObjType.equals(String.class)){
                return false;
            }
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }

    
    

    /**
     * Calculate value for pCMeterConnect
     *
     * @param sfkJsonString String json from API call
     * @return String true/false flag
     */
    public static String getPcMeterConnectValue(String DataCategoryName, String DataCategoryGroupName) {
        if (DataCategoryGroupName.equals(Constants.ARTICLE_TOPIC_CATEGORY) && DataCategoryName.equals(Constants.CONNECTING_YOUR_PRODUCT)) {
            return Constants.STRING_TRUE;
        } else {
            return Constants.STRING_FALSE;
        }
    }

    /**
     * Sets the Response message in JSON response.
     *
     * @param responseString String message to be set in the response
     * @return String jsonResponse with Response message
     */
    public static String setApiResponse(String status, String message) {
        String jsonResponse = StringUtils.EMPTY;
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Status", status);
        jsonObject.addProperty("Message", message);
        jsonArray.add(jsonObject);
        jsonResponse = jsonArray.toString();
        return jsonResponse;
    }

    
    /**
     * Adds all the Array properties to a map for Knowledge Article Page
     *
     * @param articlePropertyJsonBeanObject ArticlePropertyJsonBean
     * @return Map<String, String[]> knowledgePageNodeArrayValues
     * This method was added as part of JIRA PBCOM-48380
     */
    
    

    }

