#!/home/pi/lamp-venv/bin/python3

import json
import logging
import paho.mqtt.client as MQTT
import shelve
from lamp_driver import LampDriver 

# File to treat as database for service
STATE_DB = "lamp_state.db"

# Details for MQTT broker
BROKER = "localhost"
PORT = 1883
# Topics
CONFIG = "lamp/set_config"
CHANGED = "lamp/changed"

class LampService:
    def __init__(self):
        self.driver = LampDriver()
        self.client = MQTT.Client()
        self.state = self.load_state()  # Load last known state from file
        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.connect(BROKER, PORT, 60)

    def load_state(self):
        with shelve.open(STATE_DB) as db:
            state = db.get("state", None)
            if state is None:
                return {
                    'on': True,
                    'brightness': 1.0,
                    'color': {'h': 1.0, 's': 1.0}
                }
            return state

    def save_state(self):
        with shelve.open(STATE_DB) as db:
            db["state"] = self.state

    def on_connect(self, client, userdata, flags, rc):
        logging.info("Connected to MQTT broker with result code " + str(rc))
        client.subscribe(CONFIG)

    def on_message(self, client, userdata, msg):
        if msg.topic == CONFIG:
            payload = json.loads(msg.payload.decode())
            self.handle_config_change(payload)

    def handle_config_change(self, config):
        if not self.is_valid_config(config):
            logging.warning("Invalid config, no config applied")
            return
        self.state = config
        self.driver.set_lamp_state(config['color']['h'], config['color']['s'], config['brightness'], config['on'])
        self.save_state();
        self.client.publish(CHANGED, json.dumps(self.state), retain=True)

    def is_valid_config(self, config):
        if not (0.0 <= config['brightness'] <= 1.0):
            return False
        if not (0.0 <= config['color']['h'] <= 1.0):
            return False
        if not (0.0 <= config['color']['s'] <= 1.0):
            return False
        if not isinstance(config['on'], bool):
            return False
        return True

    def serve(self):
        self.client.loop_start()
        self.driver.set_lamp_state(self.state['color']['h'], self.state['color']['s'], self.state['brightness'], self.state['on'])
        self.client.publish(CHANGED, json.dumps(self.state), retain=True)
        try:
            while True:
                pass
        except KeyboardInterrupt:
            logging.info("Lamp service stopping")
            self.client.loop_stop()

if __name__ == "__main__":
    logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')
    service = LampService()
    service.serve()
