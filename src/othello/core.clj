(ns othello.core
  (:use othello.view
        othello.model))

(defn on-command
  "Veiwからのコマンド通知を処理するハンドラ"
  [[cmd pos]]
  (cond
    (= cmd :move) (play-move pos)
    (= cmd :exit) (System/exit 0)
    :else nil))

