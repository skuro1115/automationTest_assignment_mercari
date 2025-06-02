package hotel;

import hotel.pages.ReservePage;
import hotel.pages.TopPage;
import hotel.pages.PlansPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("異常入力: 人数・泊数・形式・制約")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvaildInputTest {

    private static WebDriver driver;

    private static WebDriverWait wait;

    @BeforeAll
    static void initAll() {
        driver = Utils.createWebDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void tearDownAll() {
        if (driver != null) driver.quit();
    }

    /** 各テストの冒頭で毎回プラン選択 → 予約ページへ遷移 */
    void prepareReservePage() {
        try {
            driver.get(Utils.BASE_URL);
            TopPage topPage = new TopPage(driver);            
            topPage.goToPlansPage();
            PlansPage plansPage = new PlansPage(driver);

            String firstPlan = plansPage.getPlanTitles().get(0);
            plansPage.openPlanByTitle(firstPlan);            
            ReservePage reservePage = new ReservePage(driver);

        } catch (Exception e) {
            System.err.println("Error in prepareReservePage: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    // === 形式エラー ===

    @Test @Order(1) @DisplayName("人数：小数 → エラー")
    void testDecimalHeadCount() {
        prepareReservePage();
        ReservePage page = new ReservePage(driver);
        page.setReserveDate("2025/07/15");
        page.setReserveTerm("1");
        page.setHeadCount("1.5");
        page.setTel("00000000000");
        page.goToConfirmPageExpectingFailure();
        assertTrue(!page.getHeadCountMessage().isEmpty());
    }

    // @Test @Order(2) @DisplayName("人数：文字列 → エラー")
    // void testStringHeadCount() {
    //     prepareReservePage();
    //     ReservePage page = new ReservePage(driver);
    //     page.setReserveDate("2025/07/15");
    //     page.setReserveTerm("1");
    //     page.setHeadCount("一人");
    //     page.setTel("00000000000");
    //     page.goToConfirmPageExpectingFailure();
    //     assertValidationError(page, "headCount");
    // }

    // === 数値範囲エラー ===

    // @Test @Order(3) @DisplayName("泊数：0泊 → エラー")
    // void testZeroStay() {
    //     prepareReservePage();
    //     ReservePage page = new ReservePage(driver);
    //     page.setReserveDate("2025/07/15");
    //     page.setReserveTerm("0");
    //     page.setHeadCount("2");
    //     page.setTel("00000000000");
    //     page.goToConfirmPageExpectingFailure();
    //     assertValidationError(page, "term");
    // }

    @Test @Order(2) @DisplayName("人数：マイナス → エラー")
    void testNegativeHeadCount() {
        prepareReservePage();
        ReservePage page = new ReservePage(driver);
        page.setReserveDate("2025/07/15");
        page.setReserveTerm("2");
        page.setHeadCount("-1");
        page.setTel("00000000000");
        page.goToConfirmPageExpectingFailure();
        assertTrue(!page.getHeadCountMessage().isEmpty());
    }

    // === プラン制約エラー（先頭プランが制約を持つ場合に限定）===

    // @Test @Order(5) @DisplayName("人数：100人（過剰）→ エラー")
    // void testTooManyPeople() {
    //     prepareReservePage();
    //     ReservePage page = new ReservePage(driver);
    //     page.setReserveDate("2025/07/15");
    //     page.setReserveTerm("1");
    //     page.setHeadCount("100");  // 常識的な上限超過
    //     page.setTel("00000000000");
    //     page.goToConfirmPageExpectingFailure();
    //     assertValidationError(page, "headCount");
    // }

    @Test @Order(3) @DisplayName("人数：000000002（先頭ゼロ）→ 正常動作 エラーにすべき")
    void testLeadingZeros() {
        prepareReservePage();
        ReservePage page = new ReservePage(driver);
        page.setReserveDate("2025/07/15");
        page.setReserveTerm("1");
        page.setHeadCount("000000002"); // パース上は正常でもUX上は不正としたい
        page.setTel("00000000000");
        page.goToConfirmPageExpectingFailure();
        assertTrue(page.getHeadCountMessage().isEmpty());
    }
}
