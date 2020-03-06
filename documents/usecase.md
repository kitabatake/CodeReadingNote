# topicを作る

ユーザーは `ToolWindow` を開き、 "create new topic" ボタンをクリックする。
システムは `NewTopicDialog` を表示する。
ユーザーは "topic name" と "description" を記入し、 "submit" ボタンを送信する。
システムは入力値をチェックし、 `TopicList` に新しい `Topic` を追加し、`ToolWindow` に反映し、focusする。

## 入力されたtopic nameが空だった場合

`NewTopicDialog` は表示したまま、手前に `AlertPopup` を表示する。

---

# 行をtopicに追加する

ユーザーはエディター上の `Topic` に追加したい行にカーソルを合わせ、 `AddtoTopic` アクションを実行する。
システムは `AddTopicLineDialog` を表示する。
ユーザーは `Topic` を選択し、必要があれば "comment" を入力し、 "submit" ボタンをクリックする。
システムは `Topic` へ新しい `TopicLine` を追加する。

---

# 行を追加するときにtopicを作成する

ユーザーはエディター上の `Topic` に追加したい行にカーソルを合わせ、 `AddtoTopic` アクションを実行する。
システムは `AddTopicLineDialog` を表示する。
ユーザーは `AddTopicLineDialog` 上の "create new topic" ボタンをクリックする。
システムは `NewTopicDialog` を表示する。
ユーザーは "topic name" と "description" を記入し、 "submit" ボタンを送信する。

システムは入力値をチェックし、 `TopicList` に新しい `Topic` を追加し、
`AddTopicLineDialog` で新しい `Topic` を選択した状態にする。


---

# topicを探す

ユーザーはtool windowを開く。
システムは `topic list` を取得し、日付順に並べ替えた形で一覧で表示する。

!!searchの仕方は仕様がよくわかっていないので後回し。

---


