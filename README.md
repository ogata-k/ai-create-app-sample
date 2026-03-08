# AI Sample (Android)

モダンなAndroid開発手法を用いた、Jetpack Composeベースのサンプルアプリケーションです。

## プロジェクトの概要

このプロジェクトは、Jetpack
Compose、Paging3、および独自の実装によるMVI（Model-View-Intent）アーキテクチャを採用したAndroidアプリのサンプルです。
JSONPlaceholder APIを使用して、アイテムの一覧表示と詳細表示を行います。

## 技術スタック

* **UI**: Jetpack Compose
* **Dependency Injection**: Hilt
* **Navigation**: Navigation Compose (Type-safe navigation)
* **Networking**: Retrofit 2 & Moshi
* **Image Loading**: Coil
* **Paging**: Paging 3
* **Asynchronous**: Kotlin Coroutines & Flow
* **Architecture**: MVI (Model-View-Intent)
  * `BaseViewModel`:
    状態管理、Intent、Action、Mutation、Effectのフローを制御。入力中のテキストや押されたボタンの状態などのComposable内部だけで完結する状態は持たないが、監視しているページングデータフローなどの監視中のフローはライフサイクルを合わせるために適宜持つ。
    * `Reducer`: Mutationに基づいて新しい状態を生成。ステートレスなクラス。
    * `Middleware`: ログ出力やタイミング計測などの共通処理
  * `UseCase`: ビジネスロジックをカプセル化し、ViewModelとRepositoryの依存を分離。

## 主な機能

* **一覧表示**: JSONPlaceholderから取得したアイテムをリスト形式で表示（Paging 3によるページング対応）。
* **詳細表示**: アイテムをタップすることで詳細情報を表示。
* **依存関係注入**: HiltによるDIを導入し、ViewModelやRepositoryの管理を効率化。
* **エラーハンドリング**: 通信エラーなどの際、Snackbarを使用してユーザーに通知。
* **MVIアーキテクチャ**: 予測可能でテストしやすい状態管理を実現。

## プロジェクト構成

* `com.example.ai_sample.core`: MVIの基底クラス、EventBus、共通ミドルウェアなど。
* `com.example.ai_sample.data`: API定義、データモデル、リポジトリ実装。
* `com.example.ai_sample.domain`: `UseCase` 定義。ビジネスロジックを保持。
* `com.example.ai_sample.di`: HiltのModule定義（ネットワーク、リポジトリ、ユースケース設定など）。
* `com.example.ai_sample.ui`:
    * `feature`: 各画面（一覧、詳細）のUI、ViewModel、Contract。
    * `theme`: Composeのテーマ設定。

## 今後の改善方針 (Roadmap)

さらなる保守性と拡張性の向上のため、以下の実装を予定しています。

1. **エラーハンドリングの整理**: グローバルな通知 (`AppEventBus`) と画面固有の通知 (`Effect`)
   の使い分けを明確化し、リトライ処理などの共通化を推進します。
2. **UI プレビューの充実**: `PreviewParameterProvider` 等を活用し、エラー時や空データ時などの多様な状態を
   Compose Preview で確認可能にします。
3. **マルチモジュール化**: プロジェクトの規模拡大に備え、`core`, `data`, `feature` 単位でのモジュール分割を検討します。

## セットアップ

1. Android Studio (最新の安定版推奨) を開きます。
2. このプロジェクトをクローンまたはインポートします。
3. Gradleの同期が完了するのを待ちます。
4. `app` モジュールを実行します。
