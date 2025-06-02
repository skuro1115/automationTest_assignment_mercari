# Mercari 2025 自動テスト


## ファイル


【課題1】
`src/test/java/hotel/PremiumUserReservationTest.java`

`src/test/java/hotel/ReservationFlowTest.java`

【課題2】
`src/test/java/hotel/InvaildInputTest.java`

#### 目次

- [【課題1】](#【課題1】)
- [【課題2】](#【課題2】)
- [その他](#その他)
  - [環境要件](#環境要件)
  - 【テスト実行方法】(#テスト実行方法)



# 【課題1】

## 概要
- 目的: プレミアム会員の宿泊予約フロー検証
- テストシナリオ:
  1. ログイン → プラン選択 → 予約入力 → 確認 → 予約完了
- 関連ファイル:
  - `PremiumUserReservationTest.java`: リファクタリング後
  - `pages/`配下の各Pageクラス
  - `ReservationFlowTest.java`: リファクタリング前のコード


### シナリオ
- 1. プレミアム会員でログインする
- 2. 「宿泊予約」ボタンをタップ する
- 3. テーマパーク優待プランの「このプランで予約」ボタンをタップする
- 4. 「宿泊予約」画面に下記のデータを入力する:
- 4-1. 宿泊: 2025/07/15
- 4-2. 宿泊数: 3
- 4-3. 人数: 2
- 4-4. 追加プラン: 朝食バイキング
- 4-5. 確認のご連絡: 電話でのご連絡
- 4-6. 電話番号: 00011112222
- 5. 合計は上記のデータにより正しく表示されているのを確認する
- 6. 「予約を確認する」をタップする
- 7. 「宿泊予約確認」画面に前の画面に入力したデータが正しく表示されているのを確認
する
- 8. 「この内容で予約する」ボタンをタップする
- 9. 「予約を完了しました」というポップアップメッセージが表示されているのを確認する
- 10. 「閉じる」ボタンをタップする
- 11. 「宿泊予約」スクリーンに戻るのを確認する



## テストデータ管理
```java
// プレミアム会員テストデータ
TestUser PREMIUM_USER = new TestUser(
    "ichiro@example.com", 
    "password", 
    "山田一郎", 
    "プレミアム会員"
);

// 基本予約データ
ReservationData BASIC_RESERVATION = new ReservationData(
    "テーマパーク優待プラン",
    "2025/07/15",
    "3",
    "2",
    true,
    ReservePage.Contact.電話でのご連絡,
    "00011112222",
    66000
);
```





---
# 【課題2】

## 🧪 サンプル未検証の異常系テストケース

あまり見つからなかった

### 対象
src/test/java/hotel/InvaildInputTest.java

予約画面
入力値エラーケース

関連ファイル
- src/test/java/hotel/PlansTest.java
- src/test/java/hotel/RedirectionTest.java

---

### ✅ ① 形式エラー（数値でない or 整数でない）

| テスト内容  | 入力例  | 対象項目 | 期待結果 | 説明             |
| ------ | ---- | ---- | ---- | -------------- |
| 人数：小数  | 1.5  | 人数   | エラー  | 人数は整数である必要がある  |
| 人数：マイナス値   | -1  | 人数   | エラー  | マイナス人数は受け付けない         |

---

### 不正っぽいが正常動作 先頭ゼロ付きの数値（≒見た目上は不正）

| 入力文字列         | Javaでの挙動（`Integer.parseInt()`） | ユーザー視点の印象  | 問題点                  |
| ------------- | ------------------------------ | ---------- | -------------------- |
| `"2"`         | 2                              | 正常         | OK                   |
| `"000000002"` | 2                              | 不正っぽいが正常動作 | UXや脆弱性の誤検出につながる可能性あり |
| `"2人"`        | NumberFormatException          | 明確に異常      | OK                   |


---



# その他


## 環境要件
- Java 21+
- Gradle 8.0+
- ChromeDriver (最新版)


## テスト実行方法

- 全テスト実行
```bash
./gradlew test
```

- 単一テストクラス実行
```bash
./gradlew test --tests hotel.PremiumUserReservationTest
```


- 単一テストクラス実行
```bash
./gradlew test --tests hotel.InvaildInputTest
```