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

(def succesor ; 隣のposを得る無名関数のマップ
  (let [north (fn [pos] (- pos b-size))
        east inc
        south (fn [pos] (+ pos b-size))
        west dec]
    {:n north
     :ne (comp north east)
     :e east
     :se (comp south east)
     :s south
     :sw (comp south west)
     :w west
     :nw (comp north west)}))

(defn- in-board? [pos]
  "盤面の中に存在するかどうかを判定"
  (<= first-pos pos last-pos))

(def not-wrapped? ; 折り返していないかどうかの判定
  (let [east? (fn [pos]
                (<= first-col (col-from-pos pos)))
        west? (fn [pos]
                (<= (col-from-pos pos) (dec last-col)))]
    {:n identity
     :ne east?
     :e east?
     :se east?
     :s identity
     :sw west?
     :w west?
     :nw west?}))

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

(defn posline-for-dir
  "posにおけるdir方向へのposline"
  [pos dir]
  (let [suc (succesor dir)
        nwrap? (not-wrapped? dir)]
    (take-while
     #(and (nwrap? %) (in-board? %))
     (iterate suc (suc pos)))))