(ns forms-backend.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as json]
            [ring.util.http-response :as response]
            [cheshire.core :as ch])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn json-handler [request]
  (response/ok {:json "some json"}))

(defn results-handler [request]
  (spit "resources/results.json"
        (ch/generate-string (merge (ch/parse-string (slurp "resources/results.json") true)
               (:json-params request))))
  (response/ok (slurp "resources/results.json")))


(defroutes handler
  (GET "/" [] (slurp "resources/form.json"))
  ;; (GET "/results" [] (slurp "resources/results.json"))
  (GET "/json" [] json-handler)
  (POST "/results" [] results-handler)
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> handler
      (json/wrap-json-response)
      (json/wrap-json-params)))

(comment
  (jetty/run-jetty app {:port 8080
                            :join? false}))
