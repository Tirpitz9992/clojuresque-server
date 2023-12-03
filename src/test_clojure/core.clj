(ns test-clojure.core
  (:require [clojure.repl :refer [doc]])
  (:require [clojure.core.matrix :as ml]))
(def m1 [[1 2][3 4]])
(def m2 [[1 2][3 4]])

;(println (apply mapv vector m1))

(println (map (fn [row] (map + row (nth m1 0))) m1))
(println (map #(map + % (nth m1 0)) m1 ))
(println (ml/mmul m1 m2)) ; MatrixProduct from Matrix pageake


(defn MatrixProduct
  "This is a simple MatrixProduct."
  [m1 m2]
  (let [m2-cols (apply mapv vector m2)]
    (for [row m1]
      (for [col m2-cols]
        (reduce + (map * row col))))))

(println (MatrixProduct m1 m2))






;(println ((fn [x] ((fn [x] (+ x x)) x)) 2))
;(doc Loop)
;(Loop 5)
;(defn Loop
;  "this is a simple loop"
;  [x]

;  (loop[x 0]
;    (if(< x 5)
;      (recur (inc x))
;      x))

;  (println x))