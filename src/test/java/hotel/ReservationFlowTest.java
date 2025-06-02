package hotel;

import static hotel.Utils.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.openqa.selenium.By;

import hotel.pages.TopPage;
import hotel.pages.LoginPage;
import hotel.pages.PlansPage;
import hotel.pages.ReservePage;
import hotel.pages.ConfirmPage;
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;


@DisplayName("宿泊予約フローシナリオテスト") 
public class ReservationFlowTest {
    // テストユーザー定義
    private static record TestUser(String email, String password,
                                 String username, String rank) {}
    private static final TestUser PREMIUM_USER = new TestUser(
        "ichiro@example.com", "password", "山田一郎", "プレミアム会員");

    // 予約データ定義
    private static record ReservationData(
        String planName, String date, String term, String headCount,
        boolean breakfast, ReservePage.Contact contact,
        String tel, int expectedAmount
    ) {}
    private static final ReservationData BASIC_RESERVATION = new ReservationData(
        "テーマパーク優待プラン", "2025/07/15", "3", "2", true,
        ReservePage.Contact.電話でのご連絡, "00011112222",
        (10000 + 1000) * 2 * 3
    );

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    static void initAll() {
        driver = Utils.createWebDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void tearDownAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("プレミアム会員で宿泊予約を完了するシナリオテスト")
    void ReservationFlowTest_premiunUser_basicPlan() {

        // 1. プレミアム会員でログイン
        driver.get(BASE_URL);
        var topPage = new TopPage(driver);
        LoginPage loginPage = topPage.goToLoginPage();
        var myPage = loginPage.doLogin(PREMIUM_USER.email(), PREMIUM_USER.password());
        assertAll("ログイン状態の確認",
            () -> assertEquals(PREMIUM_USER.username(), myPage.getUsername()),
            () -> assertEquals(PREMIUM_USER.rank(), myPage.getRank())
        );

        // 2. 宿泊予約ボタンタップ
        PlansPage plansPage = myPage.goToPlansPage();
        var originalHandles = driver.getWindowHandles();
        
        // 3. テーマパーク優待プランの「このプランで予約」ボタンをタップする
        plansPage.openPlanByTitle(BASIC_RESERVATION.planName());
        var newHandles = driver.getWindowHandles();
        var newHandle = Utils.getNewWindowHandle(originalHandles, newHandles);
        driver.switchTo().window(newHandle);


        // 4. 「宿泊予約」画面にデータを入力する
        var reservePage = new ReservePage(driver);

        reservePage.setReserveDate(BASIC_RESERVATION.date());
        reservePage.setReserveTerm(BASIC_RESERVATION.term());
        reservePage.setHeadCount(BASIC_RESERVATION.headCount());
        reservePage.setBreakfastPlan(BASIC_RESERVATION.breakfast());
        reservePage.setContact(BASIC_RESERVATION.contact());
        reservePage.setTel(BASIC_RESERVATION.tel());

        
        // 5. 合計金額を確認する

        String totalBill = driver.findElement(By.id("total-bill")).getText();
        assertAll("合計金額の検証",
            () -> assertTrue(totalBill.matches("\\d{1,3}(,\\d{3})*円"), "正しいフォーマットで表示されていること"),
            () -> {
                int amount = Integer.parseInt(totalBill.replaceAll("[^0-9]", ""));
                assertEquals(BASIC_RESERVATION.expectedAmount(), amount, "合計金額計算結果が正しく表示されていること");
            }
        );

        // 6. 「予約を確認する」をタップする
        ConfirmPage confirmPage = reservePage.goToConfirmPage();
        // 7. 確認画面の表示項目をチェック
        assertAll("確認画面の表示項目",
            () -> {
                String totalBill_cfm = confirmPage.getTotalBill();
                assertTrue(totalBill_cfm.contains("円"), "合計金額に「円」が含まれていること");
                assertTrue(totalBill_cfm.matches(".*\\d{1,3}(,\\d{3})*円.*"), "合計金額が数値形式で表示されていること");
                int amount = Integer.parseInt(totalBill_cfm.replaceAll("[^0-9]", ""));
                assertEquals(BASIC_RESERVATION.expectedAmount(), amount, "合計金額計算結果が正しく表示されていること");
            },
            () -> assertEquals(BASIC_RESERVATION.planName(), confirmPage.getPlanName(), "プラン名が一致すること"),
            () -> assertEquals("2025年7月15日 〜 2025年7月18日 3泊", confirmPage.getTerm(), "宿泊期間が正しいこと"),
            () -> assertEquals("2名様", confirmPage.getHeadCount(), "人数が正しいこと"),
            () -> assertTrue(confirmPage.getPlans().contains("朝食バイキング"), "朝食プランが選択されていること"),
            () -> assertEquals("電話：00011112222", confirmPage.getContact(), "連絡方法が正しいこと")
        );


        // 8. 「この内容で予約する」ボタンをタップする
        confirmPage.doConfirm();

        // 9. 「予約を完了しました」ポップアップ確認
        String modalMessage = wait.until(ExpectedConditions
            .visibilityOfElementLocated(By.cssSelector(".modal-body"))).getText();
        assertAll("モーダル表示内容の確認",
            () -> assertEquals("ご来館、心よりお待ちしております。", modalMessage)
        );

        // 10. 「閉じる」ボタンをタップ
        driver.findElement(By.cssSelector(".modal-footer button")).click();

        // 11. 宿泊予約画面に戻る確認 ==== 未実装 ================================
        
        // new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.numberOfWindowsToBe(1));
        // wait.until(ExpectedConditions.urlContains("reserve.html"));

        // try {
        //     Thread.sleep(5000); // 10秒(1万ミリ秒)間だけ処理を止める
        // } catch (InterruptedException e) {
        // }

        // assertAll("確認画面以外への画面遷移の確認",
        //     // () -> assertTrue(driver.getCurrentUrl().contains("reserve.html"))
        //     () -> assertTrue(driver.getCurrentUrl().contains("plans.html"))
        //     // () -> assertTrue(!driver.getCurrentUrl().contains("confirm.html"))
        // );
        // ===========================11. 宿泊予約画面に戻る確認 ==== 未実装 ====

        driver.quit();

    }

}
