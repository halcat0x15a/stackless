(ns stackless.test
  (:require [clojure.test :refer :all]
            [stackless.core :as s]))

(with-test
  (defn sum [xs]
    (if xs
      (+ (first xs) (sum (next xs)))
      0))
  (is (thrown? StackOverflowError (sum (range 10000)))))

(with-test
  (defn sum' [xs]
    (if xs
      (s/fmap (s/call (sum' (next xs))) (fn [n] (+ (first xs) n)))
      0))
  (is (= (s/run (sum' (range 10000)))
         49995000)))
