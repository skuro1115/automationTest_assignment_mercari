package hotel.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TopPage {

  private final WebDriver driver;

  private final WebDriverWait wait;

  public TopPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    this.wait.until(ExpectedConditions.titleIs("HOTEL PLANISPHERE - テスト自動化練習サイト"));
    if (this.driver.getTitle() == null || !this.driver.getTitle().equals("HOTEL PLANISPHERE - テスト自動化練習サイト")) {
      throw new IllegalStateException("現在のページが間違っています: " + this.driver.getTitle());
    }
  }

  public LoginPage goToLoginPage() {
    var loginLink = driver.findElement(By.linkText("ログイン"));
    loginLink.click();
    return new LoginPage(driver);
  }

  public SignupPage goToSignupPage() {
    var signupLink = driver.findElement(By.linkText("会員登録"));
    signupLink.click();
    return new SignupPage(driver);
  }

  public PlansPage goToPlansPage() {
    var planLink = driver.findElement(By.linkText("宿泊予約"));
    planLink.click();
    return new PlansPage(driver);
  }
}
