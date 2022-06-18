(ns othello.view
  (:require [clojure.string :as str])
  (:use othello.model))

(def code-a (int \a))
(def code-z (int \z))

(def col-headers
  (take b-size
        (map (comp str char)
             (range code-a (inc code-z)))))

(def col-headers-str
  (str "  " (str/join " " col-headers)))


(defn- st-str
  "マスの状態を表す文字列"
  [st]
  (cond
    (= st :b) "x"
    (= st :w) "o"
    :else " "))

(defn- board-strs
  "文字列シーケンス
   ボードの各行をレンダリングしたもの"
  [brd]
  (for [row (partition b-size brd)]
    (str/join " " (map st-str row))))

(defn- board-strs-with-row
  "board-strに行番号を付与したもの"
  [brd]
  (map str
       (range (inc first-row) (inc last-row))
       (repeat b-size " ")
       (board-strs brd)))
