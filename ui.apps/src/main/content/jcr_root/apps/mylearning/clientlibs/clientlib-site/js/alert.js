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
	var doc = $(document),
		primaryLinks = $('.js-megamenu-trigger'),
		megaMenus = $('.mega-menu__cat'),
		miniNavTrigger = $('.utility-nav__list__item--account .utility-nav__list__link'),
		miniNav = $('.mini-menu'),
		dropMenuTrigger = $('.ancillary-nav__list__link--dropdown.js-dropmenu-trigger'),
		dropdowns = $('.js-dropdown-trigger'),
		dropMenu = $('.ancillary-nav__drop-menu'),
		dropMenuInput = $('.ancillary-nav__drop-menu .input-control__field'),
		dropMenuButton = $('.ancillary-nav__drop-menu .input-control__submit'),
		siteSearchTrigger = $('.utility-nav__list__item--search .utility-nav__list__link'),
		siteSearchField = $("#menu--search .mega-menu__form__input[name='q']"),
		interstitalModal = $('.interstitial-popup'),
		trackPackageMobile = $('.primary-nav__track-package-mobile'),
		trackPackageDesktop = $('.mega-menu__form__track-package'),
		navMenu	= $('.utility-nav__list__item--register .utility-nav__list__link'),
		dropMenuList = $('.utility-nav__list__item--register .dropdown-menu__list'),
		localeCountry = null,
		localeLanguage = null,

		$return;

	// raise the Region Intercept Overlay Modal
	function openInterstitial() {
		interstitalModal = $('.interstitial-popup');

		interstitalModal.addClass('interstitial-popup--is-shown');
		interstitalModal.attr("aria-hidden", false);
	}

	function getInterstitialPopUpData() {
		$.ajax({
			url: '/content/dam/support/admin/region-popup/interstitial.json',
			type: "GET",
			dataType: "json",
			success: function (result) {
				geoLocation(result);
			},
			error: function () {
				console.log("Error retrieving interstital json data.")
			}
		});
	}

	function geoLocation(data) {
		var popupData = data;
		var client = new XMLHttpRequest();
		var resp;
		var countryCode;

		client.open("GET", "/us/geolocate.html", true);
		client.timeout = 2000;
		client.send();
		client.onreadystatechange = function () {
			if (this.readyState == this.DONE) {
				try {
					resp = JSON.parse(this.responseText);
				} catch (e) {
					resp = 0;
				}

				if (resp != 0) {
					countryCode = resp.country;
				} else {
					countryCode = 'us';
				}

				interstitialPopUp(countryCode, popupData);
			}
		}
		client.ontimeout = function () {
			//Default to 'us' upon timeout
			countryCode = 'us';

			interstitialPopUp(countryCode, popupData);
		}


	}

	function interstitialPopUp(location, data) {
		let userLocation = location; // for local testing default user location is "us"  change local and check popup
		let interstitialData = data;
		var path = window.location.pathname.substring(0, window.location.pathname.indexOf('.')) || window.location.pathname;
		var urlPath = path.split("/")[1];
		var language = window.location.pathname.split("/")[2] || 'default'
		let $headerContainer = $(".global-header");

		if((urlPath !== userLocation && localStorage.getItem('closeRedirectModal') !== urlPath) && (interstitialData[0].hasOwnProperty([urlPath])) && (interstitialData[0].hasOwnProperty([userLocation])) && language !== "fr") {
			if (!interstitialData[0][urlPath].hasOwnProperty([language])) {
				language = "default";
			}
			let currentLocationdata = interstitialData[0][urlPath][language];
			let switchLocationdata = interstitialData[0][userLocation]["default"];

			let lang = switchLocationdata["hreflang"];
			let supportPage = switchLocationdata["support-landing-page"].split("/").slice(-1).toString().split(".")[0];

			let currentLocationMessage = switchLocationdata["message-current"];
			let switchLocationMessage = switchLocationdata["message-switch"];
			let switchLocationButton = switchLocationdata["button-switch"];
			let currentLocationButton = switchLocationdata["button-continue"];
			let switchCountry = switchLocationdata["country"];
			let currentCountry = currentLocationdata["country"];
			let currentWeb = switchLocationdata["Web"];
			let switchWeb = currentWeb;
			let switchFlagIcon = switchLocationdata["flag-icon"];
			let currentFlagIcon = currentLocationdata["flag-icon"];
			let switchLink = "";
			let title = switchLocationdata["title"];
			let hrefLangTag = $('link[hreflang=' + lang + ']');

			if(lang && hrefLangTag.length) {
				switchLink = hrefLangTag.attr("href");
			} else {
				if (window.location.href.indexOf(supportPage) > -1) {
					switchLink = switchLocationdata["support-landing-page"];
				} else {
					switchLink = switchLocationdata["home-page"];
				}
			}

			let template = `
			<article id="regionalRedirect" class="interstitial-popup" aria-hidden="true">
				<div class="interstitial-popup__backdrop"></div>
				<div class="interstitial-popup__wrapper">
					<div class="interstitial-popup__content" role="region" aria-labelledby="locationTitle">
						<div class="interstitial-popup__header">
							<span class="interstitial-popup__location-icon"></span>
							<h4 class="interstitial-popup__title" id="locationTitle">${title}</h4>
							<button class="interstitial-popup__close" aria-label="Close region select window" title="Close"></button>
						</div>
						<div class="interstitial-popup__body">
							<div class="interstitial-popup__msg">
								<p class="interstitial-popup__current-location-msg">${currentLocationMessage} ${currentCountry}.</p>
								<p class="interstitial-popup__switch-location-msg">${switchLocationMessage} ${switchCountry} ${switchWeb}?</p>
							</div>
							<nav class="interstitial-popup__button-wrap">
								<div class="interstitial-popup__button interstitial-popup__button--switch">
									<a href="${switchLink}" class="${switchFlagIcon} interstitial-popup__button-link">
										<span class="flag icn"></span>
										<span class="interstitial-popup__button-link-text">${switchLocationButton} ${switchCountry} ${switchWeb}</span>
									</a>
								</div>
								<div class="interstitial-popup__button interstitial-popup__button--continue">
									<a href="?regionContinue" class="${currentFlagIcon} interstitial-popup__button-link">
										<span class="flag icn"></span>
										<span class="interstitial-popup__button-link-text">${currentLocationButton} ${currentCountry} ${currentWeb}</span>
									</a>
								</div>
							</nav>
						</div>
					</div>
				</div>
			</article>`;
			$headerContainer.before(template);
			openInterstitial();
		}

		$(".interstitial-popup__button--switch").click(function (e) {
			e.preventDefault();
			localStorage.setItem('closeRedirectModal', userLocation);
			window.location = ($(this).children('a.interstitial-popup__button-link').attr('href'));
		})

		$(".interstitial-popup__close, .interstitial-popup__button--continue").click(function (e) {
			e.preventDefault();
			localStorage.setItem('closeRedirectModal', urlPath);
			interstitalModal.removeClass("interstitial-popup--is-shown");
			interstitalModal.attr("aria-hidden", true);
		})

		$(".interstitial-popup__content").on('clickout', function (e) {
			oneTrustContainer = $('#onetrust-consent-sdk');
			oneTrustVisible = $('#onetrust-banner-sdk').is(":visible");

			// test to see if OneTrust container exists and if the banner is visible
			if (oneTrustContainer.length && oneTrustVisible) {
				console.log("Avoiding interstitial clickout event due to OneTrust dialog.");
			} else {
				localStorage.setItem('closeRedirectModal', urlPath);
				interstitalModal.removeClass("interstitial-popup--is-shown");
				interstitalModal.attr("aria-hidden", true);
			}

		});

	}

	function initPrimaryNav() {
		primaryLinks.click(function (e) {
			e.preventDefault();
			var _this = $(this);
			var target = _this.data("menutarget");

			if (target === 'undefined' || target === null) {
				var target = false;
			} else {
				var targetEl = $(target);
			}

			if (_this.hasClass('is-open')) {
				_this.removeClass('is-open');
				megaMenus.removeClass('is-open');
				dropdowns.parent().removeClass('is-open');
				primaryLinks.filter(".primary-nav__list__link--suppressed").removeClass("primary-nav__list__link--suppressed").addClass("primary-nav__list__link--active");
				dropMenu.removeClass('is-open');
				dropMenuTrigger.removeClass('is-open');
			} else {
				primaryLinks.filter(".primary-nav__list__link--active").removeClass("primary-nav__list__link--active").addClass("primary-nav__list__link--suppressed");
				primaryLinks.removeClass('is-open');
				_this.addClass('is-open');
				megaMenus.removeClass('is-open');
				dropdowns.parent().removeClass('is-open');
				dropMenu.removeClass('is-open');
				dropMenuTrigger.removeClass('is-open');
				if (target) {
					$('.mega-menu__wrap').find('#' + target).addClass('is-open');
					miniNav.removeClass('is-open');
					miniNavTrigger.removeClass('is-open');
					dropMenu.removeClass('is-open');
					dropMenuTrigger.removeClass('is-open');
				}
			}
		});
		// for locales that don't have track package(mobile)
		if(trackPackageMobile.length == 0) {
			$('.mega-menu').css('top','unset');
		}
		// for locales that don't have track package(desktop)
		if(trackPackageDesktop.length == 0) {
			$('.mega-menu__form').css('justify-content','center');
		}
	}

	function initMiniMenu() {
		miniNavTrigger.click(function (e) {
			e.preventDefault();
			var _this = $(this);
			var target = _this.data("menutarget");

			if (target === 'undefined' || target === null) {
				var target = false;
			} else {
				var targetEl = $(target);
			}

			if (_this.hasClass('is-open')) {
				_this.removeClass('is-open');
				miniNav.removeClass('is-open');
				dropMenu.removeClass('is-open');
				dropMenuTrigger.removeClass('is-open');
			} else {
				miniNavTrigger.removeClass('is-open');
				_this.addClass('is-open');
				miniNav.removeClass('is-open');
				dropMenu.removeClass('is-open');
				dropMenuTrigger.removeClass('is-open');
				if (target) {
					miniNav.addClass('is-open');
					megaMenus.removeClass('is-open');
					dropMenu.removeClass('is-open');
					dropMenuTrigger.removeClass('is-open');
					primaryLinks.removeClass('is-open');
				}
			}
		});
	}

	// dropdown menu for Track a Package
	function initDropMenu() {
		// tab index should be -1 on page loads
		dropMenuInput.attr('tabindex', -1);
		dropMenuButton.attr('tabindex', -1);
		dropMenuTrigger.click(function (e) {
			e.preventDefault();
			var _this = $(this);
			var target = _this.data("menutarget");

			if (target === 'undefined' || target === null) {
				var target = false;
			} else {
				var targetEl = $(target);
			}

			if (_this.hasClass('is-open')) {
				_this.removeClass('is-open');
				dropMenu.removeClass('is-open').attr('aria-hidden', true);
				dropMenuTrigger.removeClass('is-open');
				dropMenuInput.attr('tabindex', -1);
				dropMenuButton.attr('tabindex', -1);
			} else {
				dropMenuTrigger.removeClass('is-open');
				_this.addClass('is-open');
				dropMenu.addClass('is-open').attr('aria-hidden', false);
				dropMenuInput.removeAttr("tabindex");
				dropMenuButton.removeAttr("tabindex");
				if (target) {
					dropMenu.addClass('is-open').attr('aria-hidden', false);
					dropMenuTrigger.addClass('is-open')
					megaMenus.removeClass('is-open');
					primaryLinks.removeClass('is-open');
				}
			}
		});
	}

	function initSearchFocus() {
		siteSearchTrigger.click(function (e) {
			e.preventDefault();
			var _this = $(this);

			siteSearchField.focus();
		});
	}

	function initDropdown() {
		dropdowns.click(function (e) {
			e.preventDefault();

			var contain = $(this).parent();

			if (contain.hasClass('is-open')) {
				contain.removeClass('is-open');
				miniNav.removeClass('is-open');
				megaMenus.removeClass('is-open');
				primaryLinks.filter(".primary-nav__list__link--suppressed").removeClass("primary-nav__list__link--suppressed").addClass("primary-nav__list__link--active");
			} else {
				dropdowns.parent().removeClass('is-open');
				contain.addClass('is-open');
				miniNav.removeClass('is-open');
				megaMenus.removeClass('is-open');
				primaryLinks.filter(".primary-nav__list__link--active").removeClass("primary-nav__list__link--active").addClass("primary-nav__list__link--suppressed");
				primaryLinks.removeClass('is-open');
			}
		});
	}

	function initMenuClickOut() {
		$(".js-megamenu-trigger, .js-dropdown-trigger, .mini-menu, .mega-menu__cat, .utility-nav__list__item__dropdown, .utility-nav__list__item--account .utility-nav__list__link").on('clickout', function (e) {
			this.removeClass("is-open");
			if (this.hasClass('js-dropdown-trigger')) {
				this.parent().removeClass("is-open");
			}
			primaryLinks.filter(".primary-nav__list__link--suppressed").removeClass("primary-nav__list__link--suppressed").addClass("primary-nav__list__link--active");
			//$('.mega-menu__subcat__list').hide().parent().find(".is-open").removeClass("is-open");
		});

		$(".ancillary-nav__drop-menu, .js-dropmenu-trigger").on('clickout', function (e) {
			$(".ancillary-nav__drop-menu").removeClass("is-open").attr("aria-hidden", true);
			$(".js-dropmenu-trigger").removeClass("is-open");
		});
	}

	// contracts subcategories for mobile display rendering a caret for expanding each
	function initMegamenuSubcats() {
		var $subcats = $('.mega-menu__subcat__list'),
			indicator = '<span class="mega-menu__subcat__expand-icon js-subcat-trigger"><svg><use xlink:href="/etc/designs/pb-redesign/resources/images/SVG/header-sprite.svg#svg_caret_down"></use></svg></span>';


		// debounce resize trigger by 200ms for performance
		$(window).resize($.debounce(200, function () {
			// for mobile screens
			if ($(window).width() <= 600) {
				var openDuration = 300;
				var closeDuration = 160;

				$subcats.hide();

				$subcats.each(function () {
					var currentList = $(this);
					var currentListHeader = currentList.parent().find('.mega-menu__subcat__head');

					// make sure there are children in the list
					if (currentList.children().size() > 0) {
						if (currentListHeader.parent().find('.mega-menu__subcat__expand-icon').length == 0) {
							currentListHeader.append(indicator);

							currentList.hide();
							var $triggerIcon = currentList.parent().find(".js-subcat-trigger");
							$triggerIcon.removeClass('is-open');

							$triggerIcon.on('click', function (e) {
								e.preventDefault;
								if ($(this).hasClass('is-open')) {
									$(this).removeClass('is-open');
									currentList.slideUp(closeDuration);
								} else {
									$(this).addClass('is-open');
									currentList.slideDown(openDuration);
								};
							});
						}
					}
				});
			} else { // for desktop and larger screens
				$subcats.show();
				$subcats.parent().find(".is-open").removeClass("is-open");
			}
		})).trigger('resize');
	}

	function initReturnToTop() {
		var topLink = $('#returnToTop');
		var scrollDist = 750;

		$(window).scroll($.debounce(250, function () {
			if ($(this).scrollTop() >= scrollDist) {
				topLink.fadeTo(250, 0.8, function () {
					topLink.addClass('is-shown');
				});

			} else {
				topLink.fadeTo(250, 0, function () {
					topLink.css("display", "none");
					topLink.removeClass('is-shown');
				});
			}
		}));

		topLink.click(function (e) {
			e.preventDefault();
			$('body,html').animate({
				scrollTop: 0
			}, 500);
		});
	}

	function setSameHeight(eachElement) {
		var tallestElement = null;

		eachElement.each(function () {
			if (tallestElement == null || $(this).height() > tallestElement) {
				tallestElement = $(this).height();
			}
		});

		eachElement.height(tallestElement);

	}

	// for Track Package
	function setAriaHiddenProperty() {
		// desktop view
		if (window.innerWidth > 900) {
			trackPackageMobile.attr("aria-hidden", true);
			trackPackageDesktop.attr("aria-hidden", false).removeAttr("tabindex");
		}
		// mobile view
		else {
			trackPackageMobile.attr("aria-hidden", false);
			trackPackageDesktop.attr("aria-hidden", true).attr("tabindex", -1);
		}
	}

	function initAriaHiddenProperty() {
		setAriaHiddenProperty();
		var timer;
		$(window).on('resize', function() {
			clearTimeout(timer);
			timer = setTimeout(function () {
				setAriaHiddenProperty();
			}, 1000);
		});
	}

	function initNavMenu() {
		navMenu.click(function (e) {
			e.preventDefault();
			// to stop event bubbling
			e.stopPropagation();
			var _this = $(this);
			let expanded = _this.attr('aria-expanded') === 'true' || false;
    	_this.attr('aria-expanded', !expanded);
			if (!dropMenuList.hasClass('is-open')) {
				dropMenuList.addClass('is-open').attr('aria-hidden', false).removeAttr("tabindex");
			} else {
				dropMenuList.removeClass('is-open').attr('aria-hidden', true).attr('tabindex', -1);
			}
		});
		// remove dropMenu when clicked element isn't container or its descendant
		doc.click(function (e) {
			if (!dropMenuList.is(e.target) && !dropMenuList.has(e.target).length) {
				dropMenuList.removeClass('is-open').attr('aria-hidden', true).attr('tabindex', -1);
			}
		});
	}

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
		getInterstitialPopUpData()
		initPrimaryNav();
		initMiniMenu();
		initDropdown();
		initDropMenu();
		initMenuClickOut();
		initMegamenuSubcats();
		initSearchFocus();
		initReturnToTop();
		initAriaHiddenProperty();
		initNavMenu();
		initAlerts();
	}

	return {
		init: init
	};
})();

$(function () {
	pb.globalHeader.init();
});

/**
 * jQuery Clickout Plugin
 * @version 0.0.3
 * @author Guilherme Santiago - github.com/gsantiago
 */

var $document = $(document)
var hash = {}
var setup = false

$.event.special.clickout = {
	setup: function (data, namespaces, eventHandle) {
		if (setup) return

		$document.on('click.clickout-handler tap.clickout-handler', function (event) {
			$.each(hash, function (index, obj) {
				var condition = true

				$.each(obj.elements, function (i, el) {
					if ($(event.target).closest(el).length) condition = false
				})

				if (condition) {
					obj.handler.call($(obj.elements), event)
				}
			})
		})

		setup = true
	},

	teardown: function () {
		if (hash) return
		$document.off('click.clickout-handler tap.clickout-handler')
		setup = false
	},

	add: function (handleObj) {
		var guid = handleObj.guid
		if (!hash.hasOwnProperty(guid)) {
			hash[guid] = { elements: [this], handler: handleObj.handler }
		} else {
			hash[guid].elements.push(this)
		}
	},

	remove: function (handleObj) {
		delete hash[handleObj.guid]
	}
}
