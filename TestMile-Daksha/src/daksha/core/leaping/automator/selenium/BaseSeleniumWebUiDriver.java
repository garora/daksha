/*******************************************************************************
 * Copyright 2015-18 Test Mile Software Testing Pvt Ltd
 * 
 * Website: www.TestMile.com
 * Email: support [at] testmile.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package daksha.core.leaping.automator.selenium;

import java.awt.Toolkit;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import daksha.core.batteries.config.TestContext;
import daksha.core.leaping.automator.AbstractGuiAutomator;
import daksha.core.leaping.enums.ElementLoaderType;
import daksha.core.leaping.enums.UiDriverEngine;
import daksha.tpi.enums.Browser;
import daksha.tpi.enums.DakshaOption;
import daksha.tpi.leaping.enums.GuiAutomationContext;
import daksha.tpi.sysauto.utils.FileSystemUtils;

public class BaseSeleniumWebUiDriver<D,E> extends AbstractGuiAutomator<D,E>{
	
	private D driver = null;
	private WebDriverWait waiter = null;
	private Browser browser = null;
	protected Capabilities capabilities = null;
	
	public BaseSeleniumWebUiDriver(TestContext testContext, UiDriverEngine engine, GuiAutomationContext automatorContext, ElementLoaderType loaderType) throws Exception{
		super(testContext, UiDriverEngine.WEBDRIVER, automatorContext, loaderType);
	}
	
	@Override
	public void init() throws Exception{
		//this.setBrowser(Browser.valueOf(this.getTestContext().getConfig().value(DakshaOption.BROWSER_PC_DEFAULT).asString().toUpperCase()));
		this.setWaitTime(this.getTestContext().getConfig().value(DakshaOption.GUIAUTO_MAX_WAIT).asInt());
		this.setUiTestEngineName(UiDriverEngine.WEBDRIVER);		
	}
	
	@Override
	public void setCapabilities(Map<String,?> caps){
		this.capabilities = new MutableCapabilities(caps);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void load() throws Exception{
		D driver = null;
		switch (this.getBrowser()){
		case FIREFOX:
			driver = (D) new FirefoxDriver(new FirefoxOptions(capabilities));
			break;
		case CHROME:
			ChromeOptions coptions = new ChromeOptions();
			coptions.merge(capabilities);
			driver = (D) new ChromeDriver(coptions);
			break;
		case SAFARI:
			SafariOptions soptions = new SafariOptions();
			soptions.merge(capabilities);
			driver = (D) new SafariDriver(soptions);
			break; 
		}
		this.setDriver(driver);
		initWait();
		maximizeWindow();
	}
	
	protected void setDriver(D driver) {
		this.driver = driver;
	}

	public void maximizeWindow(){
		// Check for some property here. To override this default.
		try{
			getUnderlyingEngine().manage().window().maximize();
		} catch (WebDriverException e){
			java.awt.Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			// This dimension is webdriver Dimension
			getUnderlyingEngine().manage().window().setSize(new Dimension((int)d.getWidth(), (int) d.getHeight()));
		}
	}

	public void initWait() {
		this.setWaiter(new WebDriverWait(this.getUnderlyingEngine(), getWaitTime()));
		if(this.getBrowser() != Browser.SAFARI){
			getUnderlyingEngine().manage().timeouts().pageLoadTimeout(getWaitTime(), TimeUnit.SECONDS);
		}	
	}

	public WebDriverWait getWaiter() {
		return this.waiter;
	}

	public void setWaiter(WebDriverWait waiter) {
		this.waiter = waiter;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Browser getBrowser() {
		return this.browser;
	}

	@Override
	public D getUiDriverEngine() {
		return this.driver;
	}

	private WebDriver getUnderlyingEngine() {
		return (WebDriver) getUiDriverEngine();
	}

	public void setCapabilities(MutableCapabilities capabilities) throws Exception {
		if (this.getTestContext().getConfig().value(DakshaOption.BROWSER_PC_PROXY_ON).asBoolean()){
			Proxy proxy = new Proxy();
			String p = this.getTestContext().getConfig().value(DakshaOption.BROWSER_PC_PROXY_HOST).asString() + ":" + this.getTestContext().getConfig().value(DakshaOption.BROWSER_PC_PROXY_PORT).asString();
			setHttpProxy(proxy, p);
			setSslProxy(proxy, p);
			capabilities.setCapability("proxy", proxy);
		}
	}

	public void setHttpProxy(Proxy proxy, String proxyString) {
		proxy.setHttpProxy(proxyString);
	}

	public void setSslProxy(Proxy proxy, String proxyString) {
		proxy.setSslProxy(proxyString);
	}

	/**********************************************************************************/

	public TakesScreenshot getScreenshotAugmentedDriver() {
		return (TakesScreenshot) (new Augmenter().augment(getUnderlyingEngine()));
	}
	
	/**********************************************************************************/
	/*					AUTOMATOR API												*/
	/**********************************************************************************/
	
	public void goTo(String url) throws Exception {
		getUnderlyingEngine().get(url);
		waitForBody();
	}
	
	public void waitForBody() throws Exception {
		this.getIdentifier().elementWithTagName("body").waitUntilPresent();
	}
	
	public void refresh() throws Exception {
		getUnderlyingEngine().navigate().refresh();
	}
	
	public void back() throws Exception {
		getUnderlyingEngine().navigate().back();
	}
	
	public void forward() throws Exception {
		getUnderlyingEngine().navigate().forward();
	}

	public void close(){
		getUnderlyingEngine().quit();
	}

	public void confirmAlertIfPresent() {
		WebDriver d = getUnderlyingEngine();
		try{
			Alert alert = d.switchTo().alert();
			alert.accept();
			d.switchTo().defaultContent();
		} catch (Exception e){ // ignore
		}
	}
	
	// Windows related
	public String getCurrentWindow() {
		return getUnderlyingEngine().getWindowHandle();
	}
	
	public void switchToWindow(String windowHandle){
		getUnderlyingEngine().switchTo().window(windowHandle); 		
	}
	
	public void switchToNewWindow() {
		WebDriver driver = getUnderlyingEngine();
		String parentHandle = getCurrentWindow();
		for (String winHandle : driver.getWindowHandles()) {
			if (!winHandle.equals(parentHandle)) {
				switchToWindow(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
			}
		}
	}
	
	public void closeCurrentWindow(){
		getUnderlyingEngine().close();
	}
	
	public void switchToFrame(int index) throws Exception {
		this.getUnderlyingEngine().switchTo().frame(index);
	}

	public void switchToFrame(String name) throws Exception {
		this.getUnderlyingEngine().switchTo().frame(name);
	}
	
	public void switchToDefaultFrame() throws Exception {
		this.getUnderlyingEngine().switchTo().defaultContent();
	}
	
	@Override
	public File takeScreenshot() throws Exception {
		TakesScreenshot augDriver = getScreenshotAugmentedDriver();
        File srcFile = augDriver.getScreenshotAs(OutputType.FILE);
        return FileSystemUtils.moveFiletoDir(srcFile, this.getTestContext().getConfig().value(DakshaOption.SCREENSHOTS_DIR).asString());
	}
	
	public void focusOnApp() throws Exception{
		
	}
	
	public Actions getActionChain(){
		return new Actions(getUnderlyingEngine());
	}
	
}