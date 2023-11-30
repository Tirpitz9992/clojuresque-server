(ns test-clojure.core
  (:require [clojure.repl :refer [doc]]))

(defn foo
  "this is a doc"
  [x]
  (println x "Hello, World!"))

(println (doc foo))
