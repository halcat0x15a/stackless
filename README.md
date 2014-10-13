# stackless

A trampoling library for Clojure.

[![Build Status](https://travis-ci.org/halcat0x15a/stackless.svg)](https://travis-ci.org/halcat0x15a/stackless)

[![Clojars Project](http://clojars.org/stackless/latest-version.svg)](http://clojars.org/stackless)

## Usage

```clojure
(defn sum [xs]
  (if xs
    (+ (first xs) (sum (next xs)))
    0))

(sum (range 10000)) ; => StackOverflowError

(require '[stackless.core :as s])

(defn sum' [xs]
  (if xs
    (s/fmap (s/call (sum' (next xs))) (fn [n] (+ (first xs) n)))
    0))

(s/run (sum' (range 10000))) ; => 49995000
```

## License

Copyright © 2014 Sanshiro Yoshida.

Distributed under the Eclipse Public License.
