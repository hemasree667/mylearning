var codeVersion = "2023-12-29";
'use strict';

var pb = pb || {};

(function () {
	pb.namespace = function (nsString) {
		var parts = nsString.split('.'),
			parent = pb,
			i;
		if (parts[0] === 'pb') {
			parts = parts.slice(1);
		}
		for (i = 0; i < parts.length; i += 1) {
			if (typeof parent[parts[i]] === 'undefined') {
				parent[parts[i]] = {};
			}
			parent = parent[parts[i]];
		}
		return parent;
	};
}());

pb.namespace('globalHeader');

pb.globalHeader = (function () {

	var localeCountry=null,
		localeLanguage=null,

		$return;

	// verifies target DOM element and fires off alert functionality
	function initAlerts() {
		targetElement = $('header.global-header, header.microsite-header');

		if (targetElement.length > 0) {
			getLocaleInfo();
		} else {
			console.log("Can't locate target alert DOM element.");
		}

	}

	// retrieves current locale from og:locale meta tag
	//    and sets variables for language and country
	function getLocaleInfo() {
		var localeTag = $('meta[property="og:locale"]');
		var located = false;

		if (localeTag.length > 0) {
			var localeInfo = localeTag.attr('content');
			var localeArr = localeInfo.split('_');

			localeLanguage = localeArr[0].toLowerCase();
			localeCountry = localeArr[1].toLowerCase();

			located = true;
		} else {
			if (typeof DDO === 'undefined' || DDO === null) {
				located = false;
				console.log("Couldn't locate DDO for Alert.")
			} else {
				if (typeof DDO.siteData.siteLanguage === 'undefined' || DDO.siteData.siteLanguage === null || typeof DDO.siteData.siteCountry === 'undefined' || DDO.siteData.siteCountry === null) {
					located = false;
					console.log("Couldn't locate DDO.siteData for Alert.")

				} else {
					localeLanguage = DDO.siteData.siteLanguage;
					localeCountry = DDO.siteData.siteCountry;
					located = true;
					console.log("Populated Alert locale via DDO object.");
				}
				
			}
		}

		if (located) {
			if (localeCountry == 'gb') { localeCountry = 'uk' };
			if ( (localeLanguage == 'ja') && (localeCountry == '') ) {
				localeCountry = 'jp';
				localeLanguage = 'jp';
			}
			getAlertData(localeCountry, localeLanguage);
		}
		
	}

	// loads the Alert data from a JSON endpoint
	function getAlertData(localeCountry, localeLanguage) {
		var alertJsonPath = '/pbservices/alert.' + localeCountry + "." + localeLanguage + ".json";

		var request = $.ajax({
			url: alertJsonPath,
			type: "GET",
			dataType: "json"
		});

		request.done(function( result ) {
			var alertJsonData = result;
			displayAlerts(alertJsonData);
		});
			
		request.fail(function( jqXHR, textStatus ) {
			console.log("Error retrieving Alert data: " + textStatus);
		});
	}

	function matchAlertPath(path) {
		var curPath = window.location.pathname,
			altPath = [],
			alertPath = path;

		//console.log(`Current: ${curPath} Alert: ${alertPath} Slashes: ${numSlash}`);

		if (curPath == $('.primary-nav__logo').attr('href')) {
			altPath = curPath + ".html";
			//console.log(`AltPath: ${altPath}`);
		}

		if (curPath.includes(alertPath) || altPath.includes(alertPath)) {
			return true;
		} else {
			return false;
		}
	}

	function displayAlerts(jsonData) {
		var alertData = jsonData,
			alertPath,
			pathMatched = 0;


		// iterate through each alert item
		var i = 0;
		for (let item of alertData.alerts) {
			i++;
			var size = item.alertPaths.length;
			alertPath = item.alertPaths;
			var alertShown = false;
			
			// iterate through paths
			alertPath.forEach(function (path, index) {
				pathMatched = matchAlertPath(path);
				
				if (pathMatched && !alertShown) {
					renderAlertMarkup(item.headline, item.body, item.link, item.linkText, item.linkTitle, item.newTab);
					alertShown = true;
				}
			});

			//checkAlertPath(item.alertPaths);
		}
	}

	function renderAlertMarkup(headline, body, link, linkText, linkTitle, newTab) {
		targetElement = $('header.global-header, header.microsite-header');
		var linkTitleTemplate, newTabTemplate, headlineTemplate, bodyTemplate, ctaTemplate;
		newTab = String(newTab).toLowerCase() === "true";

		if (targetElement && targetElement.length) {
			if (headline && headline.length) {
				headlineTemplate = `<span class="pbu-alert__headline">${headline}</span>`;
			} else {
				headlineTemplate = ``;
			}

			if (body && body.length) {
				bodyTemplate = `<span class="pbu-alert__body">${body}</span>`;
			} else {
				bodyTemplate = ``;
			}

			if (linkTitle && linkTitle.length) {
				linkTitleTemplate = `title="${linkTitle}"`;
			} else {
				linkTitleTemplate = ``;
			}

			if (newTab) {
				newTabTemplate = `target="_blank"`;
			} else {
				newTabTemplate = ``;
			}

			if (link && link.length) {
				ctaTemplate = `<a class="pbu-alert__cta" ${linkTitleTemplate} ${newTabTemplate} href="${link}">${linkText}</a>`;
			} else {
				ctaTemplate = ``;
			}

			const markupTemplate =
				`<aside class="pbu-alert">
					<div class="pbu-alert__wrap">
						${headlineTemplate}
						${bodyTemplate}
						${ctaTemplate}
					</div>
				</aside>`;

			targetElement.before(markupTemplate);
		}

	}
		

	function init(options) {
		initAlerts();
	}

	return {
		init: init
	};
})();

$(function () {
	pb.globalHeader.init();
});


