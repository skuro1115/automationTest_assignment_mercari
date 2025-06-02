# automationTest_assignment_mercari

---

このプロジェクトはテスト自動化学習のためのサンプルコードを利用した
シナリオテスト例の実装です。

---

## 提出テストコード
src/test/java/hotel[https://github.com/skuro1115/automationTest_assignment_mercari/tree/main/src/test/java/hotel]


hotel test code 
README.md[https://github.com/skuro1115/automationTest_assignment_mercari/tree/main/src/test/java/hotel/README_Mercari2025_auto.md]

---

## テスト対象

https://hotel-example-site.takeyaqa.dev/ja/

## 🔧 環境情報

| カテゴリ              | 使用技術／ツール                                        |
| ----------------- | ----------------------------------------------- |
| **プログラミング言語**     | Java                                            |
| **自動化フレームワーク**    | [Selenium WebDriver](https://www.selenium.dev/) |
| **テスティングフレームワーク** | [JUnit 5](https://junit.org/junit5/)            |
| **ビルドツール**        | [Gradle](https://gradle.org/)                   |

## ✅ 必須環境

| ソフトウェア | バージョン             |
| ------ | ----------------- |
| JDK    | 21                |
| ブラウザ   | Google Chrome 最新版 |

---

## 🚀 実行方法

#### テストの実行
| OS              | 実行コマンド                   |
| --------------- | ------------------------ |
| **Windows**     | `gradlew.bat clean test` |
| **macOS/Linux** | `./gradlew clean test`   |


#### パッケージ化された特定のテストの実行

`hotel` package
`PremiumUserReservationTest.java` の　`PremiumUserReservationTest`　class

```
./gradlew test --tests hotel.PremiumUserReservationTest
```

```
./gradlew test --tests hotel.ReservationFlowTest
```


#### 実行結果の確認
ローカルレポジトリ内
build/reports/tests/test/index.html

mac/linux `open`
windows `start`