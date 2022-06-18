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

(defn- in-board?
  "盤面の中に存在するかどうかを判定"
  [pos]
  (<= first-pos pos last-pos))

(def not-wrapped? ; 折り返していないかどうかの判定
  (let [east? (fn [pos]
                (< first-col (col-from-pos pos)))
        west? (fn [pos]
                (< (col-from-pos pos) (dec last-col)))]
    {:n identity
     :ne east?
     :e east?
     :se east?
     :s identity
     :sw west?
     :w west?
     :nw west?}))


(defn posline-for-dir
  "posにおけるdir方向へのposline"
  [pos dir]
  (let [suc (succesor dir)
        nwrap? (not-wrapped? dir)]
    (take-while
     #(and (nwrap? %) (in-board? %))
     (iterate suc (suc pos)))))

(defn- free?
  "空きマス可動化を判定"
  [brd pos]
  (= (brd pos) :free))

(defn- self?
  "posが自陣のマスかどうかを判定"
  [brd pos bw]
  (and (not (free? brd pos)) (= (brd pos) bw)))

(defn- opponent?
  "posが敵陣のマスかどうかを判定"
  [brd pos bw]
  (and (not (free? brd pos)) (not= (brd pos) bw)))

(defn- all-posline
  "posにおける各方向へのposlineを集めたシーケンス"
  [pos]
  (filter not-empty
          (for [dir dirs] (posline-for-dir pos dir))))

(defn- clamping?
  "bwにとって、poslineは挟めるか？"
  [brd posline bw]
  (and
   (opponent? brd (first posline) bw)
   (if-let [fst ; poslineを眺めて一番初めに現れる敵側のマスではないマス
            (first
             (filter
              (fn [pos] (not (opponent? brd pos bw)))
              (rest posline)))]
     (self? brd fst bw)
     false)))

(defn- playable?
  "bwにとってposは着手可能か？"
  [brd pos bw]
  (and ; マスか空いている　かつ　挟めるposlineが存在
   (free? brd pos)
   (some
    (fn [pl] (clamping? brd pl bw))
    (all-posline pos))))