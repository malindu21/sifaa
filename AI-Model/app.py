
from flask import Flask
import train
import engine
import writer
import survey

app = Flask(__name__)


@app.route("/api/v1.0/recommendations/<int:id>", methods=["GET"])

def rec(id):

    print("state : " + str(id))
    return train.rec(id)

@app.route("/api/v1.0/fav/<int:id>", methods=["GET"])

def fav(id):

    print("state : " + str(id))
    return train.fav(id)

@app.route("/api/v1.0/demand/<int:id>", methods=["GET"])

def get_recomendations(id):

    print("state : " + str(id))
    return engine.get_recommendations(id)

@app.route("/api/v1.0/centerdemand/<int:id>", methods=["GET"])

def get_barChart(id):

    print("state : " + str(id))
    return engine.get_barChart(id)

@app.route("/api/v1.0/writer/<string:id>", methods=["GET"])

def get_wri(id):

    print("state : " + str(id))
    return writer.get_wri(id)

@app.route("/api/v1.0/survey/<int:id>", methods=["GET"])

def getSurvey(id):

    print("state : " + str(id))
    return survey.getSurvey(id)

@app.route("/api/v1.0/predemand/<int:id>", methods=["GET"])

def get_predicted_demands(id):

    print("state : " + str(id))
    return engine.get_predicted_demands(id)

@app.route("/api/v1.0/oridemand/<int:id>", methods=["GET"])

def get_original_demands(id):

    print("state : " + str(id))
    return engine.get_original_demands(id)

@app.route("/api/v1.0/itemdemand/<int:id>", methods=["GET"])

def get_self_item_demands(id):

    print("state : " + str(id))
    return engine.get_self_item_demands(id)

if __name__ == "__main__":
    app.run(host= '0.0.0.0')
    
    