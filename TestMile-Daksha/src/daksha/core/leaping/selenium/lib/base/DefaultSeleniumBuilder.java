package daksha.core.leaping.selenium.lib.base;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import daksha.core.batteries.config.Batteries;
import daksha.core.leaping.enums.UiAutomatorPropertyType;
import daksha.core.leaping.selenium.SeleniumWebUiDriver;
import daksha.tpi.enums.Browser;
import daksha.tpi.leaping.enums.UiAutomationContext;
import daksha.tpi.leaping.interfaces.SeleniumBuilder;
import daksha.tpi.leaping.interfaces.UiDriver;
import daksha.tpi.sysauto.utils.SystemUtils;

public class DefaultSeleniumBuilder implements SeleniumBuilder {
	private DesiredCapabilities browserCaps = new DesiredCapabilities();
	private DesiredCapabilities otherCaps = new DesiredCapabilities();
	private UiAutomationContext context = UiAutomationContext.PC_WEB;
	private String appTitle = null;
	private Browser browser = null;
	
	public DefaultSeleniumBuilder() throws Exception{
		this.browser = Batteries.value(UiAutomatorPropertyType.BROWSER_PC_DEFAULT).asEnum(Browser.class);
	}
	
	@Override
	public void browser(Browser browser){
		this.browser = browser;
	}
	
	@Override
	public void capabilities(DesiredCapabilities caps){
		otherCaps.merge(caps);
	}
	
	@Override
	public UiDriver build() throws Exception{
		SeleniumWebUiDriver selenium = new SeleniumWebUiDriver();
		selenium.setBrowser(browser);
		selenium.init();
		switch (this.browser){
		case FIREFOX:
			setFirefoxCaps();
			break;
		case CHROME:
			setChromeCaps();
			break;
		case SAFARI:
			setSafariCaps();
			break;	
		}
		
		browserCaps.merge(otherCaps);
		selenium.setCapabilities(browserCaps.asMap());
		selenium.load();
		return selenium;
	}


	private DesiredCapabilities getFireFoxCapabilitiesSkeleton() { 
		return DesiredCapabilities.firefox();
	}

	private DesiredCapabilities getChromeCapabilitiesSkeleton() {
		return DesiredCapabilities.chrome();
	}

	private DesiredCapabilities getSafariCapabilitiesSkeleton() {
		return DesiredCapabilities.safari();
	}
	
	private void setFirefoxCaps() throws Exception {
		this.appTitle = Batteries.value(UiAutomatorPropertyType.FIREFOX_WINDOWNAME).asString();
		String os = SystemUtils.getOSName();
		String binaryName = null;
		if (os.startsWith("Window")){
			binaryName = "geckodriver.exe";
		} else if (os.startsWith("Mac")) {
			binaryName = "geckodriver";
		}
		System.setProperty("webdriver.gecko.driver", Batteries.value(UiAutomatorPropertyType.DIRECTORY_TOOLS_UIDRIVERS).asString() + "/" + binaryName);

		browserCaps = getFireFoxCapabilitiesSkeleton();
		//driver = new FirefoxDriver(capabilities);
		FirefoxProfile profile = new FirefoxProfile();
		//profile..setEnableNativeEvents(true);
		browserCaps.setCapability(FirefoxDriver.PROFILE, profile);
	}

	private void setChromeCaps() throws Exception {
		this.appTitle = Batteries.value(UiAutomatorPropertyType.CHROME_WINDOWNAME).asString();
		String os = SystemUtils.getOSName();
		String chromeDriverBinaryName = null;
		if (os.startsWith("Window")){
			chromeDriverBinaryName = "chromedriver.exe";
		} else if (os.startsWith("Mac")) {
			chromeDriverBinaryName = "chromedriver";
		}

		System.setProperty("webdriver.chrome.driver", Batteries.value(UiAutomatorPropertyType.DIRECTORY_TOOLS_UIDRIVERS).asString() + "/" + chromeDriverBinaryName);
		browserCaps = getChromeCapabilitiesSkeleton();
	}

	private void setSafariCaps() throws Exception {
		this.appTitle = Batteries.value(UiAutomatorPropertyType.SAFARI_WINDOWNAME).asString();
		browserCaps = getSafariCapabilitiesSkeleton();
	}

}