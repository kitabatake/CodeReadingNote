# topicを作る

ユーザーは `tool window` を開き、 "create new topic" ボタンをクリックする。
システムは `new topic dialog` を表示する。
ユーザーは "topic name" と "description" を記入し、 "submit" ボタンを送信する。
システムは入力値をチェックし、 `topic list` に新しい `topic` を追加し、`tool window` に反映し、focusする。

## 入力されたtopic nameが空だった場合

`new topic dialog` は表示したまま、手前に `alert popup` を表示する。

---

# 行をtopicに追加する

ユーザーはエディター上の `topic` に追加したい行にカーソルを合わせ、 `add to topic` アクションを実行する。
システムは `add TopicLine dialog` を表示する。
ユーザーは `topic` を選択し、必要があれば "comment" を入力し、 "submit" ボタンをクリックする。
システムは `topic list` へ新しい `topic` を追加する。

---

# 行を追加するときにtopicを作成する

ユーザーはエディター上の `topic` に追加したい行にカーソルを合わせ、 `add to topic` アクションを実行する。
システムは `add TopicLine dialog` を表示する。
ユーザーは `add TopicLine dialog` 上の `create new topic` ボタンをクリックする。
システムは `new topic dialog` を表示する。
ユーザーは `topic name` と `description` を記入し、 `submit` ボタンを送信する。

システムは入力値をチェックし、 `topic list` に新しい `topic` を追加し、
`add TopicLine dialog` で新しい topic を選択した状態にする。


---

# topicを探す

ユーザーはtool windowを開く。
システムは `topic list` を取得し、日付順に並べ替えた形で一覧で表示する。

!!searchの仕方は仕様がよくわかっていないので後回し。

---


