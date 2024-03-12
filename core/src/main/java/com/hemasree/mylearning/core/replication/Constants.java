package com.hemasree.mylearning.core.replication;

public class Constants{

    //CF root path
    public static final String CF_ROOTPATH = "/content/dam/pitneybowes/content-fragment";


    //General constants
    public static final String HYPHEN = "-";
    public static final String ACTIVATE = "activate";
    public static final String DEACTIVATE = "deactivate";
    public static final String SERVICE_USER = "SfdcMigrateService";
    public static final String SFK_SYNC_SUCCESSFUL = "SFK Sync Successful";
    public static final String ONLINE = "Online";
    public static final String ARCHIVED = "Archived";
    public static final String JCR_CONTENT = "jcr:content";
    public static final String KNOWLEDGE_KAV = "Knowledge__kav";
    public static final String RELATED_PRODUCT = "relatedProduct";
    public static final String FORWARD_SLASH = "/";
	public static final String SEMI_COLON = ";";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String GREATER_THAN = ">";
	public static final String JAPAN = "jp";
	public static final String UNDERSCORE = "_";
	public static final String DOT_HTML = ".html";
    //public static final String DATE_FORMAT_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    //public static final String DATE_FORMAT_00 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATE_FORMAT_YY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MONTH_DD_YYYY = "MMMM dd, yyyy";

    public static final String DATE_FORMAT_DD_MONTH_YYYY = "dd MMMM yyyy";
    public static final String DATE_FORMAT_DD_DOT_MONTH_YYYY = "dd. MMMM yyyy";

    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";
    public static final String RECORDS_UPDATED_SUCCESSFULLY = "Records updated successfully";
    public static final String INVALID_JSON = "JSON is INVALID/ mandatory json key is missing";
    
    public static final String ARTICLE_TOPIC_CATEGORY = "Article_Topic_Category";
    public static final String CONNECTING_YOUR_PRODUCT = "Connecting_Your_Product";
    public static final String ATTACHED_DATA_TXT = "attachedData.txt";
    public static final String APPLICATION_X_ANY = "application/x-any";
    public static final String INITIAL_BULK_UPLOAD_SUCCESS_EMAIL_SUBJECT = "SFK Initial Bulk Upload: Successfully imported articles";
    public static final String INITIAL_BULK_UPLOAD_FAILURE_EMAIL_SUBJECT = "SFK Initial Bulk Upload: Failed to imported articles";
    
    public static final String BODY_MESSAGE = "bodyMessage";
    public static final String BODY_ERROR = "bodyError";
    
    public static final String FAILURE_INVALID_JSON_SUBJECT = "SFK Article Sync Failure due to invalid json or due to missing mandatory json key";
    public static final String FAILED_ARTICLE_CREATE_AEM_SUBJECT = "Failed to create article in AEM";
    
    public static final String ATTACHED_INVALID_JSON_BODY = "Please find the attached invalid json data.";
    public static final String FAILED_ARTICLE_CREATE_AEM_BODY = "Failed to create article in AEM due to following exceptions.";
    
    public static final String FAILED_INITIAL_BULK_UPLOAD_SUBJECT = "Initial Bulk Upload Job Failed";
    public static final String INITIAL_BULK_UPLOAD_COMPLETED_SUBJECT = "Initial Bulk Upload Job Completed";
    
    public static final String FAILED_INITIAL_BULK_UPLOAD_BODY = "Please verify job config, logs and json file on SFTP verver.";
    public static final String FAILED_INITIAL_BULK_UPLOAD_CONFIG_BODY = "Please verify scheduler job config";
    
    public static final String CHECK_LOG_BODY = "Please check log for more details";
    
    public static final String SFK_ORPHAN_CHILD_ARTICLES = "List of SFK Orphan Child Articles";

    //Email Template
    public static final String SFK_ARTICLE_EMAIL_NOTIFICATION_TEMPLATE = "/etc/notification/sfk-article-email-notification.html";


    //JSON Keys
    public static final String RECORDS = "records";
    public static final String ADOBE_REFERENCE__C = "Adobe_Reference__c";
    public static final String BUSINESSUNIT__C = "BusinessUnit__c";
    public static final String ARTICLE_TYPE__C = "Article_Type__c";
    public static final String ANSWER__C = "Answer__c";
    public static final String SYMPTOM__C = "Symptom__c";
    public static final String PROBLEM__C = "Problem__c";
    public static final String KNOWLEDGE_ARTICLE_ID = "KnowledgeArticleId";
    public static final String LANGUAGE = "Language";
    public static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    public static final String LAST_PUBLISHED_DATE = "LastPublishedDate";
    public static final String SOLUTION__C = "Solution__c";
    public static final String SHORT_TITLE__C = "Short_Title__c";
    public static final String SUMMARY = "Summary";
    public static final String TITLE = "Title";
    public static final String URL_NAME = "UrlName";
    public static final String PUBLISH_STATUS = "PublishStatus";

    public static final String DATA_CATEGORY_NAME = "DataCategoryName";
    public static final String DATA_CATEGORY_GROUP_NAME = "DataCategoryGroupName";
    //public static final String QUESTION__C = "Question__c";		// JIRA-38616 - Remove Question_C field from SFK article
    public static final String ENVIRONMENT__DETAILS__C = "Environment_Details__c";
    public static final String CONTAINS__VIDEO__C = "Contains_Video__c";
    public static final String FIRST_PUBLISHED_DATE = "FirstPublishedDate";
    public static final String SUPPORT__CONTACT__INFORMATION__R = "Support_Contact_Information__r";
    public static final String RECORD__TYPE = "RecordType";
    public static final String ARTICLE_NUMBER = "ArticleNumber";
    public static final String KNOWLEDGE_ARTICLE_VERSION_ID = "KnowledgeArticleVersionID";
    public static final String IS_MASTER_LANGUAGE = "IsMasterLanguage";
    public static final String ARTICLE_MASTER_LANGUAGE = "ArticleMasterLanguage";
    
    
    //CRX Keys
    public static final String PC_METER_CONNECT = "pCMeterConnect";
    public static final String JCR_TITLE = "jcr:title";
    public static final String JCR_DESCRIPTION = "jcr:description";
    
    public static final String CQ_PAGE_CONTENT = "cq:PageContent";
    public static final String REDIRECT_TARGET = "redirectTarget";
    public static final String CQ_TEMPLATE = "cq:template";
    public static final String SLING_RESOURCE_TYPE = "sling:resourceType";
    public static final String AEM_UPDATED = "aemUpdated";
    public static final String LAST_UPDATED_AT = "LastUpdatedAt";

    public static final String ARTICLE_DATA_CATEGORIES = "articleDataCategories";
    public static final String ARTICLE_TOPIC_CATEGORIES = "articleTopicCategories";
    public static final String CATEGORIES_META_DATA = "categoriesMetaData";
    
    public static final String HIDE_IN_NAV = "hideInNav";
    public static final String NO_INDEX = "noindex";
    
    //These constants (118 to 124) are added as part of the jira PBCOM-48380
    public static final String SLING_REDIRECT = "sling:redirect";
    public static final String SLING_VANITYPATH = "sling:vanityPath";
    public static final String CID_VALUE = "cidValue";
    public static final String FOLLOW = "follow";
    public static final String DISABLECANONICALINJECTION = "disableCanonicalInjection";
    public static final String HTTPS = "https://";
    public static final String WWW = "www.";
    
    
    public static final String STRING_TRUE = "true";
    public static final String STRING_FALSE = "false";
    
    //Query Constants
    public static final String CQ_PAGE = "cq:Page";
    public static final String NT_UNSTRUCTURED = "nt:unstructured";
    public static final String SLING_ORDERED_FOLDER = "sling:OrderedFolder";
    public static final String PATH = "path";
    public static final String TYPE = "type";
    public static final String NODE_NAME = "nodename";
    public static final String P_LIMIT = "p.limit";
    
    
    
    //Path Constants
    public static final String CONTENT_SUPPORT = "/content/support";
    public static final String CONTENT_SUPPORT_SLASH = "/content/support/";
    public static final String CONTENT_KNOWLEDGE = "/content/knowledge";
    public static final String SUPPORT_ARTICLES = "/support/articles";
    public static final String CONTENT_PB = "/content/pb";
    public static final String CONTENT_SUPPORT_DELETED_NODES = "/content/support/deletedNodes";
    public static final String DELETED_NODES = "deletedNodes";
    public static final String NODES_DELETED = "NodesDeleted";
    
    
    //Number Constants
    public static final String NEGATIVE_ONE = "-1";
    
    
    //Template and component path constants
    public static final String PB_CATEGORY_PAGE_TEMPLATE = "/apps/pitneybowes/templates/pbr-home-page";
    
    public static final String SALESFORCE_ARTICLE_TEMPLATE = "/apps/pitneybowes/templates/pbr-support-salesforce-article-page";
    public static final String SALESFORCE_ARTICLE_PAGE_COMPONENT = "/apps/pitneybowes/components/page/pbr-support-salesforce-article-page";
    
    public static final String SFK_ARTICLE_TEMPLATE = "/apps/pitneybowes/templates/pbr-support-sfk-article-page";
    public static final String SFK_ARTICLE_PAGE_COMPONENT = "/apps/pitneybowes/components/page/pbr-support-sfk-article-page";
    
    public static final String PBR_BLANK_PAGE_TEMPLATE = "/apps/pitneybowes/templates/pbr-blank-page";
    public static final String PBR_BLANK_PAGE_COMPONENT = "/apps/pitneybowes/components/page/pbr-blank-page";
    
    
    
    
    //Exception messages constants
    public static final String REPOSITORY_EXCEPTION_REPLICATION = "RepositoryException while replication";
    public static final String REPOSITORY_EXCEPTION = "RepositoryException";
    public static final String REPLICATION_EXCEPTION = "ReplicationException while replication";
    public static final String LOGIN_EXCEPTION = "LoginException while running query";
    public static final String WCM_EXCEPTION_MESSAGE = "WCMException while creating page";
    public static final String PATH_NOT_FOUND_EXCEPTION_MESSAGE = "PathNotFoundException while setting page properties";
    public static final String PARSER_EXCEPTION_MESSAGE = "ParseException while parsing date";
    
    public static final String IO_EXCEPTION_MESSAGE = "IOException while sending email";
    public static final String MESSAGING_EXCEPTION_MESSAGE = "MessagingException while sending email";
    public static final String EMAIL_EXCEPTION_MESSAGE = "EmailException while sending email";
    public static final String SERVER_EXCEPTION_MESSAGE = "ServerException while sending email";
    public static final String MAILING_EXCEPTION_MESSAGE = "MailingException while sending email";
    
    
    
    //Logger messages constants
    public static final String KNOWLEDGE_PAGE_ADDED = "Knowledge Page Added : ";
    public static final String SUPPORT_PAGE_PAGE_ADDED = "Support Page Added : ";
    public static final String KNOWLEDGE_ID_PATH_DELETED = "Knowledge ID Path Deleted : ";
    public static final String KNOWLEDGE_ID_PATH_DOESNT_EXIST = "Knowledge ID Path Doesnt Exist In AEM : ";
    public static final String SUPPORT_PAGE_PAGE_DELETED = "Support Page Deleted : ";
    public static final String PATH_DOESNT_EXIST_IN_AEM = "Failed to Activate/Deactivate, Path doesn't exist in AEM : ";
    public static final String AEM_ARTICLE_IS_UP_TO_DATE = "AEM Article is up to date. Hence not updated : ";
    public static final String KNOWLEDGE_PAGE_UPDATED = "Knowledge Page Updated : ";
    public static final String SUPPORT_PAGE_PAGE_UPDATED = "Support Page Updated : ";
    public static final String INCORRECT_JSON_FORMAT = "IncorrectJSON Format";
}

