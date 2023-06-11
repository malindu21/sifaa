from flask import Flask
import engine

app = Flask(__name__)


@app.route("/api/v1.0/demand/<int:id>", methods=["GET"])

def get_recomendations(id):

    print("state : " + str(id))
    return engine.get_recommendations(id)

if __name__ == "__main__":
    app.run(host= '0.0.0.0')
    
    