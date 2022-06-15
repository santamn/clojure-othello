(ns othello.model)

(def b-size 8)    ; 一辺のサイズ
(def first-pos 0) ; 先頭のマスのpos
(def last-pos (* b-size b-size)) ; 最後のマスのpos
(def all-pos (range first-pos last-pos)) ; 全てのposからなるリスト
(def first-col 0)     ; 先頭の桁インデックス
(def last-col b-size) ; 最後の桁インデックス
(def first-row 0)     ; 最初の行インデックス
(def last-row b-size) ; 最後の行インデックス
(def dirs #{:n :ne :e :se :s :sw :w :nw})

(def board (ref []))   ; 盤面の状態 :b/:w/:free
(def player (ref nil)) ; 手番のプレイヤー :b/:w


(defn col-from-pos [pos]
  (mod pos b-size))

(defn row-from-pos [pos]
  (quot pos b-size))

(defn pos-from-rowcol [row col]
  (+ (* row b-size) col))

