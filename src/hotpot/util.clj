(ns hotpot.util
  (:require [clojure.java.io :as io]))

(defn file-exists?
  "Check both the file system and the resources/ directory
  on the classpath for the existence of a file"
  [file]
  (let [f (clojure.string/trim file)
        classf (io/resource file)
        relf (when (.exists (io/as-file f)) (io/as-file f))]
    (or classf relf)))

(defn file->properties-map
  "Take a URI of a properties file, return a map with key and value untouched."
  [file]
  (when-let [reader (io/reader (file-exists? file))]
    (let [p (java.util.Properties.)]
      (.load p reader)
      (into {} p))))
