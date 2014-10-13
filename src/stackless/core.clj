(ns stackless.core)

(defprotocol Trampoline
  (bind [m f]))

(deftype Cont [a f]
  Trampoline
  (bind [m g] (Cont. a #(bind (f %) g))))

(defn cont? [x]
  (instance? Cont x))

(deftype Call [k]
  Trampoline
  (bind [m f] (Cont. m f)))

(defn call? [x]
  (instance? Call x))

(defmacro call [& exprs]
  `(Call. (fn [] ~@exprs)))

(extend-protocol Trampoline
  java.lang.Object
  (bind [obj f]
    (call (f obj)))
  nil
  (bind [_ f]
    (call (f nil))))

(defn fmap [m f]
  (bind m #(call (f %))))

(defn run [c]
  (cond (call? c) (recur ((.k ^Call c)))
        (cont? c) (let [a (.a ^Cont c)
                        f (.f ^Cont c)]
                    (cond (call? a) (recur (bind ((.k ^Call a)) f))
                          (cont? a) (recur (bind (.a ^Cont a) #(bind ((.f ^Cont a) %) f)))
                          :else (recur (f a))))
        :else c))
